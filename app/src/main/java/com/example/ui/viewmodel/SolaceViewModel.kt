package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.ChatbotRole
import com.example.data.ai.GeminiChatModel
import com.example.data.ai.GeminiChatService
import com.example.data.ai.SolaceAiService
import com.example.data.audio.AudioRecorderHelper
import com.example.data.local.SolaceDatabase
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.JournalEntryEntity
import com.example.data.local.entity.MoodEntryEntity
import com.example.data.local.entity.RegulationSessionEntity
import com.example.data.local.entity.UserPreferencesEntity
import com.example.data.model.FirestoreUserProfile
import com.example.data.model.RegulationActivity
import com.example.data.model.RegulationMode
import com.example.data.repository.FirestoreRepository
import com.example.data.repository.SolaceRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val TAG = "SolaceViewModel"

class SolaceViewModel(application: Application) : AndroidViewModel(application) {

    private val database = SolaceDatabase.getDatabase(application)
    private val repository = SolaceRepository(database)
    private val firestoreRepository = FirestoreRepository(application)
    private val aiService = SolaceAiService()
    private val geminiChatService = GeminiChatService()
    private val audioRecorder = AudioRecorderHelper(application)
    private val auth: FirebaseAuth? = try {
        Firebase.auth
    } catch (e: Exception) {
        Log.w(TAG, "FirebaseAuth initialization fallback: ${e.message}")
        null
    }

    // Firebase Auth State
    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth?.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _currentUser.value = firebaseAuth.currentUser
        firebaseAuth.currentUser?.let { user ->
            viewModelScope.launch {
                restoreUserDataFromFirestore(user.uid)
            }
        }
    }

    init {
        auth?.addAuthStateListener(authListener)
        viewModelScope.launch {
            repository.migrateLegacyDefaultName("Sarah", "Sanctuary")
        }
    }

    override fun onCleared() {
        super.onCleared()
        auth?.removeAuthStateListener(authListener)
        audioRecorder.cancelRecording()
    }

    private fun syncUserProfileToFirestore(user: FirebaseUser) {
        viewModelScope.launch {
            try {
                val currentPrefs = userPreferences.value
                val profile = FirestoreUserProfile(
                    userId = user.uid,
                    displayName = user.displayName ?: currentPrefs.userName,
                    email = user.email,
                    pinnedTheme = currentPrefs.pinnedTheme,
                    isDarkMode = currentPrefs.isDarkMode,
                    aiPersonalisationConsent = currentPrefs.aiPersonalisationConsent
                )
                firestoreRepository.saveUserProfile(profile)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to sync user profile to Firestore", e)
            }
        }
    }

    // Preferences
    val userPreferences: StateFlow<UserPreferencesEntity> = repository.userPreferences
        .combine(MutableStateFlow(Unit)) { prefs, _ ->
            prefs ?: UserPreferencesEntity()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferencesEntity()
        )

    // Moods
    val allMoods: StateFlow<List<MoodEntryEntity>> = repository.allMoodEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val latestMood: StateFlow<MoodEntryEntity?> = repository.latestMoodEntry
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Instant mood preview for interactive theme attunement
    private val _previewMood = MutableStateFlow<String?>(null)
    val previewMood: StateFlow<String?> = _previewMood.asStateFlow()

    fun setPreviewMood(mood: String?) {
        _previewMood.value = mood
    }

    // Dynamic active mood: if previewMood is set, use it; otherwise pinnedTheme, otherwise latest mood, fallback to "Calm"
    val activeMoodTheme: StateFlow<String> = combine(userPreferences, latestMood, _previewMood) { prefs, latest, preview ->
        preview ?: prefs.pinnedTheme ?: latest?.mood ?: "Calm"
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Calm"
    )

    // Journals
    val allJournals: StateFlow<List<JournalEntryEntity>> = repository.allJournalEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Chat
    val allMessages: StateFlow<List<ChatMessageEntity>> = repository.allChatMessages
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _streamingAiChunk = MutableStateFlow<String?>(null)
    val streamingAiChunk: StateFlow<String?> = _streamingAiChunk.asStateFlow()

    private val _currentRegulationMode = MutableStateFlow(RegulationMode.LISTEN)
    val currentRegulationMode: StateFlow<RegulationMode> = _currentRegulationMode.asStateFlow()

    // Gemini Chatbot Model & Role Selection
    private val _selectedGeminiModel = MutableStateFlow(GeminiChatModel.FLASH)
    val selectedGeminiModel: StateFlow<GeminiChatModel> = _selectedGeminiModel.asStateFlow()

    private val _selectedChatbotRole = MutableStateFlow(ChatbotRole.SANCTUARY_COMPANION)
    val selectedChatbotRole: StateFlow<ChatbotRole> = _selectedChatbotRole.asStateFlow()

    fun selectGeminiModel(model: GeminiChatModel) {
        _selectedGeminiModel.value = model
    }

    fun selectChatbotRole(role: ChatbotRole) {
        _selectedChatbotRole.value = role
    }

    // Audio Transcription State
    private val _isRecordingAudio = MutableStateFlow(false)
    val isRecordingAudio: StateFlow<Boolean> = _isRecordingAudio.asStateFlow()

    private val _isTranscribingAudio = MutableStateFlow(false)
    val isTranscribingAudio: StateFlow<Boolean> = _isTranscribingAudio.asStateFlow()

    private val _lastTranscribedText = MutableStateFlow<String?>(null)
    val lastTranscribedText: StateFlow<String?> = _lastTranscribedText.asStateFlow()

    fun startAudioRecording(): Boolean {
        val started = audioRecorder.startRecording()
        _isRecordingAudio.value = started
        return started
    }

    fun stopAudioRecordingAndTranscribe(onSuccess: (String) -> Unit, onError: (String) -> Unit = {}) {
        val result = audioRecorder.stopRecording()
        _isRecordingAudio.value = false
        if (result == null) {
            onError("No audio recorded.")
            return
        }

        viewModelScope.launch {
            _isTranscribingAudio.value = true
            try {
                // Call Gemini 3.5 Transcribe
                val transcript = geminiChatService.transcribeAudio(result.audioBytes, result.mimeType)
                _lastTranscribedText.value = transcript

                // Save to Firestore if user signed in
                if (auth?.currentUser != null) {
                    try {
                        firestoreRepository.saveAudioTranscription(
                            transcription = transcript,
                            durationSeconds = result.durationSeconds,
                            source = "microphone"
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to persist audio transcription to Firestore", e)
                    }
                }
                onSuccess(transcript)
            } catch (e: Exception) {
                Log.e(TAG, "Audio transcription failed", e)
                onError(e.localizedMessage ?: "Transcription failed")
            } finally {
                _isTranscribingAudio.value = false
            }
        }
    }

    fun cancelAudioRecording() {
        audioRecorder.cancelRecording()
        _isRecordingAudio.value = false
    }

    // Regulation Sessions
    val allRegulationSessions: StateFlow<List<RegulationSessionEntity>> = repository.allRegulationSessions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedRegulationActivity = MutableStateFlow<RegulationActivity?>(null)
    val selectedRegulationActivity: StateFlow<RegulationActivity?> = _selectedRegulationActivity.asStateFlow()

    fun selectRegulationActivity(activity: RegulationActivity?) {
        _selectedRegulationActivity.value = activity
    }

    fun setRegulationMode(mode: RegulationMode) {
        _currentRegulationMode.value = mode
        // Align matching chatbot role
        when (mode) {
            RegulationMode.LISTEN -> _selectedChatbotRole.value = ChatbotRole.SANCTUARY_COMPANION
            RegulationMode.UNDERSTAND -> _selectedChatbotRole.value = ChatbotRole.THOUGHT_CLARIFIER
            RegulationMode.CALM -> _selectedChatbotRole.value = ChatbotRole.SOMATIC_GUIDE
            RegulationMode.REFLECT -> _selectedChatbotRole.value = ChatbotRole.WISDOM_MIRROR
            RegulationMode.REFOCUS -> _selectedChatbotRole.value = ChatbotRole.SOMATIC_GUIDE
            RegulationMode.GRATITUDE -> _selectedChatbotRole.value = ChatbotRole.WISDOM_MIRROR
        }
    }

    fun saveMoodCheckIn(
        mood: String,
        valence: Int,
        intensity: Int,
        tags: List<String>,
        note: String
    ) {
        _previewMood.value = null
        viewModelScope.launch {
            // Local Room
            repository.saveMoodEntry(
                mood = mood,
                valence = valence,
                intensity = intensity,
                tags = tags.joinToString(", "),
                note = note
            )

            // Cloud Firestore
            if (auth?.currentUser != null) {
                try {
                    firestoreRepository.saveMoodEntry(
                        mood = mood,
                        valence = valence,
                        intensity = intensity,
                        tags = tags.joinToString(", "),
                        note = note
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save mood entry to Firestore", e)
                }
            }
        }
    }

    fun deleteMood(id: Long) {
        viewModelScope.launch {
            repository.deleteMoodEntry(id)
        }
    }

    fun saveJournal(
        id: Long = 0,
        title: String,
        content: String,
        associatedMood: String? = null,
        tags: List<String> = emptyList()
    ) {
        viewModelScope.launch {
            // Local Room
            repository.saveJournalEntry(
                id = id,
                title = title,
                content = content,
                associatedMood = associatedMood,
                tags = tags.joinToString(", ")
            )

            // Cloud Firestore
            if (auth?.currentUser != null) {
                try {
                    firestoreRepository.saveJournalEntry(
                        title = title,
                        content = content,
                        associatedMood = associatedMood,
                        tags = tags.joinToString(", ")
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save journal to Firestore", e)
                }
            }
        }
    }

    fun deleteJournal(id: Long) {
        viewModelScope.launch {
            repository.deleteJournalEntry(id)
        }
    }

    fun sendChatMessage(userText: String) {
        if (userText.isBlank()) return

        val mode = _currentRegulationMode.value
        val model = _selectedGeminiModel.value
        val role = _selectedChatbotRole.value
        val latest = latestMood.value

        viewModelScope.launch {
            // 1. Save user message locally and to Firestore
            repository.saveChatMessage(
                senderRole = "user",
                content = userText,
                regulationMode = mode.name
            )
            if (auth?.currentUser != null) {
                try {
                    firestoreRepository.saveChatMessage(
                        senderRole = "user",
                        content = userText,
                        modelName = model.modelId,
                        systemRole = role.id
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save user chat to Firestore", e)
                }
            }

            // 2. Crisis interceptor check
            if (aiService.checkCrisisSignal(userText)) {
                val crisisMsg = aiService.getCrisisResponse()
                repository.saveChatMessage(
                    senderRole = "assistant",
                    content = crisisMsg,
                    regulationMode = mode.name
                )
                if (auth?.currentUser != null) {
                    try {
                        firestoreRepository.saveChatMessage(
                            senderRole = "model",
                            content = crisisMsg,
                            modelName = model.modelId,
                            systemRole = role.id
                        )
                    } catch (_: Exception) {}
                }
                return@launch
            }

            // 3. Multimodal multi-turn streaming via Gemini
            _isAiThinking.value = true
            val accumulated = StringBuilder()

            val history = allMessages.value.takeLast(10).map {
                val sender = if (it.senderRole == "user") "user" else "model"
                sender to it.content
            }

            val contextAnchor = if (userPreferences.value.aiPersonalisationConsent && latest != null) {
                "${latest.mood} Resonance (intensity ${latest.intensity}/10)"
            } else null

            try {
                geminiChatService.streamChatResponse(
                    model = model,
                    role = role,
                    history = history,
                    latestUserMessage = userText,
                    contextAnchor = contextAnchor
                ).collect { chunk ->
                    accumulated.append(chunk)
                    _streamingAiChunk.value = accumulated.toString()
                }

                val fullResponse = accumulated.toString().ifBlank {
                    aiService.generateResponse(
                        userName = userPreferences.value.userName,
                        currentMood = latest?.mood,
                        moodIntensity = latest?.intensity,
                        recentJournalSnippet = null,
                        regulationMode = mode,
                        conversationHistory = history,
                        latestUserMessage = userText
                    )
                }

                repository.saveChatMessage(
                    senderRole = "assistant",
                    content = fullResponse,
                    regulationMode = mode.name,
                    contextAnchor = contextAnchor
                )

                if (auth?.currentUser != null) {
                    try {
                        firestoreRepository.saveChatMessage(
                            senderRole = "model",
                            content = fullResponse,
                            modelName = model.modelId,
                            systemRole = role.id
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to save AI chat to Firestore", e)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Gemini chat generation failed", e)
            } finally {
                _isAiThinking.value = false
                _streamingAiChunk.value = null
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
            auth?.currentUser?.uid?.let { uid ->
                try {
                    firestoreRepository.clearChatMessages(uid)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to clear Firestore chat", e)
                }
            }
        }
    }

    fun saveRegulationSession(activity: RegulationActivity, rating: Int, note: String) {
        viewModelScope.launch {
            repository.saveRegulationSession(
                activityId = activity.id,
                activityTitle = activity.title,
                category = activity.category,
                durationSeconds = activity.durationSeconds,
                rating = rating,
                note = note
            )
        }
    }

    fun updateUserName(name: String) {
        viewModelScope.launch {
            repository.updateUserName(name)
            auth?.currentUser?.let { user ->
                syncUserProfileToFirestore(user)
            }
        }
    }

    fun setPinnedTheme(pinnedMood: String?) {
        viewModelScope.launch {
            repository.setPinnedTheme(pinnedMood)
            auth?.currentUser?.let { user ->
                syncUserProfileToFirestore(user)
            }
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            repository.toggleDarkMode(isDark)
            auth?.currentUser?.let { user ->
                syncUserProfileToFirestore(user)
            }
        }
    }

    fun toggleReducedMotion(reduced: Boolean) {
        viewModelScope.launch {
            repository.toggleReducedMotion(reduced)
        }
    }

    fun toggleAiConsent(consent: Boolean) {
        viewModelScope.launch {
            repository.toggleAiConsent(consent)
            auth?.currentUser?.let { user ->
                syncUserProfileToFirestore(user)
            }
        }
    }

    suspend fun exportDataJson(): String {
        return repository.exportAllDataAsJson()
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllSanctuaryData()
            auth?.currentUser?.uid?.let { uid ->
                try {
                    firestoreRepository.clearChatMessages(uid)
                } catch (_: Exception) {}
            }
        }
    }

    // --- Authentication with Password ---
    private fun hashPassword(password: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun restoreUserDataFromFirestore(userId: String) {
        try {
            val profile = firestoreRepository.fetchUserProfile(userId)
            val moods = firestoreRepository.fetchMoodEntries(userId)
            val journals = firestoreRepository.fetchJournalEntries(userId)
            val chats = firestoreRepository.fetchChatMessages(userId)
            repository.restoreUserData(profile, moods, journals, chats)
            Log.i(TAG, "Restored user data from Firestore for $userId: moods=${moods.size}, journals=${journals.size}, chats=${chats.size}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to restore user data from Firestore", e)
        }
    }

    suspend fun loginWithPassword(email: String, password: String): Result<String> {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            return Result.failure(IllegalArgumentException("Please enter a valid email address"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        // 1. Try local SQLite account (same installation)
        val localAccount = repository.getAccountByEmail(cleanEmail)
        val inputHash = hashPassword(password)

        if (localAccount != null) {
            if (localAccount.passwordHash == inputHash) {
                repository.setLoggedIn(true, localAccount.email, localAccount.displayName)
                // Connect Firebase backend if available
                try {
                    auth?.signInWithEmailAndPassword(cleanEmail, password)?.await()
                } catch (_: Exception) {}
                auth?.currentUser?.uid?.let { uid ->
                    restoreUserDataFromFirestore(uid)
                }
                return Result.success("Welcome back, ${localAccount.displayName}!")
            } else {
                return Result.failure(IllegalArgumentException("Incorrect password for $cleanEmail"))
            }
        }

        // 2. Local account not found (app was reinstalled / data cleared)
        // Authenticate with remote Firebase backend to restore account & data
        try {
            val authResult = auth?.signInWithEmailAndPassword(cleanEmail, password)?.await()
            val user = authResult?.user ?: auth?.currentUser
            if (user != null) {
                val displayName = user.displayName?.ifBlank { null }
                    ?: cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() }

                // Restore local account record so subsequent offline launches work
                repository.createAccount(cleanEmail, displayName, inputHash)
                repository.setLoggedIn(true, cleanEmail, displayName)

                // Restore all cloud-persisted user data from Firestore
                restoreUserDataFromFirestore(user.uid)

                return Result.success("Welcome back, $displayName! Your account data has been restored.")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Firebase signIn failed", e)
            val msg = when {
                e.message?.contains("password", ignoreCase = true) == true -> "Incorrect password for $cleanEmail"
                e.message?.contains("no user", ignoreCase = true) == true ||
                e.message?.contains("user-not-found", ignoreCase = true) == true -> "Account not found. Please create an account."
                else -> "Unable to sign in. Please verify your email and password."
            }
            return Result.failure(IllegalArgumentException(msg))
        }

        return Result.failure(IllegalArgumentException("Account not found. Please create an account."))
    }

    suspend fun registerWithPassword(name: String, email: String, password: String): Result<String> {
        val cleanEmail = email.trim().lowercase()
        val cleanName = name.trim().ifBlank { cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() } }
        if (cleanEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            return Result.failure(IllegalArgumentException("Please enter a valid email address"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        val existing = repository.getAccountByEmail(cleanEmail)
        if (existing != null) {
            return Result.failure(IllegalArgumentException("An account with this email already exists. Please sign in."))
        }

        val passHash = hashPassword(password)
        repository.createAccount(cleanEmail, cleanName, passHash)
        repository.setLoggedIn(true, cleanEmail, cleanName)

        // Firebase Auth remote account creation & profile sync
        try {
            val result = auth?.createUserWithEmailAndPassword(cleanEmail, password)?.await()
            val user = result?.user
            if (user != null) {
                try {
                    val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(cleanName)
                        .build()
                    user.updateProfile(profileUpdates).await()
                } catch (_: Exception) {}

                val profile = FirestoreUserProfile(
                    userId = user.uid,
                    displayName = cleanName,
                    email = cleanEmail,
                    pinnedTheme = userPreferences.value.pinnedTheme,
                    isDarkMode = userPreferences.value.isDarkMode,
                    aiPersonalisationConsent = userPreferences.value.aiPersonalisationConsent
                )
                firestoreRepository.saveUserProfile(profile)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Firebase createUser failed: ${e.message}")
            if (e.message?.contains("already in use", ignoreCase = true) == true) {
                return Result.failure(IllegalArgumentException("An account with this email already exists on the server. Please sign in."))
            }
        }

        return Result.success("Account created successfully. Welcome, $cleanName!")
    }

    fun continueAsGuest() {
        viewModelScope.launch {
            repository.setLoggedIn(true, "guest@solace.local", "Guest")
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                auth?.signOut()
            } catch (_: Exception) {}
            repository.setLoggedIn(false, "", "")
        }
    }

    fun deleteAccount(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val user = auth?.currentUser
                val uid = user?.uid
                if (uid != null) {
                    try {
                        firestoreRepository.deleteUserAccountAndData(uid)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to delete firestore user data", e)
                    }
                    try {
                        user.delete().await()
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to delete Firebase user", e)
                    }
                }
                repository.clearAllSanctuaryData()
                repository.setLoggedIn(false, "", "")
                onComplete(true)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete account", e)
                onComplete(false)
            }
        }
    }

    // --- Privacy & Memory Permissions ---
    fun setMemorySavingPermission(allowed: Boolean) {
        viewModelScope.launch {
            repository.setMemoryPermission(allowed)
        }
    }

    // --- Custom Gemini API Key ---
    fun saveCustomGeminiApiKey(apiKey: String) {
        viewModelScope.launch {
            repository.setCustomGeminiApiKey(apiKey)
        }
    }
}
