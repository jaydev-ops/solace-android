package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.ChatbotRole
import com.example.data.ai.GeminiChatModel
import com.example.data.ai.GeminiChatService
import com.example.data.audio.AudioRecorderHelper
import com.example.data.local.SolaceDatabase
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.UserPreferencesEntity
import com.example.data.repository.FirestoreRepository
import com.example.data.repository.SolaceRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "ChatViewModel"

sealed interface ChatUiState {
    data object Idle : ChatUiState
    data object Loading : ChatUiState
    data class Streaming(val partialResponse: String) : ChatUiState
    data class Error(val message: String) : ChatUiState
}

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val database = SolaceDatabase.getDatabase(application)
    private val repository = SolaceRepository(database)
    private val firestoreRepository = FirestoreRepository(application)
    private val geminiService = GeminiChatService()
    private val audioRecorder = AudioRecorderHelper(application)
    private val auth = try {
        Firebase.auth
    } catch (_: Exception) {
        null
    }

    // User preferences
    val userPreferences: StateFlow<UserPreferencesEntity> = repository.userPreferences
        .combine(MutableStateFlow(Unit)) { prefs, _ -> prefs ?: UserPreferencesEntity() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferencesEntity()
        )

    // Ephemeral messages for when memory saving is disabled
    private val _ephemeralMessages = MutableStateFlow<List<ChatMessageEntity>>(emptyList())

    // Conversation messages: from Room if memory allowed, or ephemeral
    val messages: StateFlow<List<ChatMessageEntity>> = combine(
        repository.allChatMessages,
        _ephemeralMessages,
        userPreferences
    ) { dbMessages, ephemeralList, prefs ->
        if (prefs.allowMemorySaving) dbMessages else ephemeralList
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Idle)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    // Model and Role selection
    private val _selectedModel = MutableStateFlow(GeminiChatModel.FLASH)
    val selectedModel: StateFlow<GeminiChatModel> = _selectedModel.asStateFlow()

    private val _selectedRole = MutableStateFlow(ChatbotRole.SANCTUARY_COMPANION)
    val selectedRole: StateFlow<ChatbotRole> = _selectedRole.asStateFlow()

    // Audio recording state
    private val _isRecordingAudio = MutableStateFlow(false)
    val isRecordingAudio: StateFlow<Boolean> = _isRecordingAudio.asStateFlow()

    private val _isTranscribingAudio = MutableStateFlow(false)
    val isTranscribingAudio: StateFlow<Boolean> = _isTranscribingAudio.asStateFlow()

    fun selectModel(model: GeminiChatModel) {
        _selectedModel.value = model
    }

    fun selectRole(role: ChatbotRole) {
        _selectedRole.value = role
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val currentModel = _selectedModel.value
        val currentRole = _selectedRole.value
        val trimmed = userText.trim()
        val prefs = userPreferences.value
        val saveMemory = prefs.allowMemorySaving

        viewModelScope.launch {
            val userMsg = ChatMessageEntity(
                senderRole = "user",
                content = trimmed,
                regulationMode = currentRole.id,
                timestamp = System.currentTimeMillis()
            )

            // 1. Save user message locally if memory saving allowed, otherwise add to ephemeral list
            if (saveMemory) {
                repository.saveChatMessage(
                    senderRole = "user",
                    content = trimmed,
                    regulationMode = currentRole.id
                )
            } else {
                _ephemeralMessages.value = _ephemeralMessages.value + userMsg
            }

            // Save to Firestore if authenticated & memory saving allowed
            if (saveMemory && auth?.currentUser != null) {
                try {
                    firestoreRepository.saveChatMessage(
                        senderRole = "user",
                        content = trimmed,
                        modelName = currentModel.modelId,
                        systemRole = currentRole.id
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to persist user message to Firestore", e)
                }
            }

            // 2. Prepare conversation history for multi-turn
            val history = messages.value.takeLast(8).map {
                val role = if (it.senderRole == "user") "user" else "model"
                role to it.content
            }

            _uiState.value = ChatUiState.Loading
            val accumulatedResponse = StringBuilder()

            try {
                geminiService.streamChatResponse(
                    model = currentModel,
                    role = currentRole,
                    history = history,
                    latestUserMessage = trimmed,
                    customApiKey = prefs.customGeminiApiKey,
                    userName = prefs.userName.trim()
                ).collect { chunk ->
                    accumulatedResponse.append(chunk)
                    _uiState.value = ChatUiState.Streaming(accumulatedResponse.toString())
                }

                val fullReply = accumulatedResponse.toString().trim()
                if (fullReply.isNotEmpty()) {
                    val aiMsg = ChatMessageEntity(
                        senderRole = "assistant",
                        content = fullReply,
                        regulationMode = currentRole.id,
                        contextAnchor = currentModel.displayName,
                        timestamp = System.currentTimeMillis()
                    )

                    if (saveMemory) {
                        repository.saveChatMessage(
                            senderRole = "assistant",
                            content = fullReply,
                            regulationMode = currentRole.id,
                            contextAnchor = currentModel.displayName
                        )
                    } else {
                        _ephemeralMessages.value = _ephemeralMessages.value + aiMsg
                    }

                    if (saveMemory && auth?.currentUser != null) {
                        try {
                            firestoreRepository.saveChatMessage(
                                senderRole = "model",
                                content = fullReply,
                                modelName = currentModel.modelId,
                                systemRole = currentRole.id
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to persist AI reply to Firestore", e)
                        }
                    }
                    _uiState.value = ChatUiState.Idle
                } else {
                    _uiState.value = ChatUiState.Error("No response received.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Gemini chat stream error", e)
                _uiState.value = ChatUiState.Error(e.localizedMessage ?: "Failed to generate reply.")
            }
        }
    }

    fun startAudioRecording(): Boolean {
        val started = audioRecorder.startRecording()
        _isRecordingAudio.value = started
        return started
    }

    fun stopAudioRecordingAndTranscribe(onSuccess: (String) -> Unit, onError: (String) -> Unit = {}) {
        val recording = audioRecorder.stopRecording()
        _isRecordingAudio.value = false
        if (recording == null) {
            onError("No audio recorded.")
            return
        }

        viewModelScope.launch {
            _isTranscribingAudio.value = true
            try {
                val transcript = geminiService.transcribeAudio(recording.audioBytes, recording.mimeType)
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

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
            _ephemeralMessages.value = emptyList()
            auth?.currentUser?.uid?.let { uid ->
                try {
                    firestoreRepository.clearChatMessages(uid)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to clear Firestore chat messages", e)
                }
            }

            // Seed clean welcome message for new conversation with actual user name or neutral greeting
            val prefs = userPreferences.value
            val cleanName = prefs.userName.trim()
            val hasValidName = cleanName.isNotBlank() &&
                !cleanName.equals("Sanctuary", ignoreCase = true) &&
                !cleanName.equals("User", ignoreCase = true) &&
                !cleanName.equals("Guest", ignoreCase = true) &&
                !cleanName.equals("Sanctuary Guest", ignoreCase = true)
            val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            val timeGreeting = when {
                currentHour in 4..11 -> "Good morning"
                currentHour in 12..16 -> "Good afternoon"
                else -> "Good evening"
            }
            val greeting = if (hasValidName) "$timeGreeting, $cleanName." else "$timeGreeting."
            val initialMsg = "$greeting I'm here with you in your sanctuary. What's on your mind right now?"
            if (prefs.allowMemorySaving) {
                repository.saveChatMessage(
                    senderRole = "assistant",
                    content = initialMsg,
                    regulationMode = _selectedRole.value.id
                )
            } else {
                _ephemeralMessages.value = listOf(
                    ChatMessageEntity(
                        senderRole = "assistant",
                        content = initialMsg,
                        regulationMode = _selectedRole.value.id
                    )
                )
            }
            _uiState.value = ChatUiState.Idle
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioRecorder.cancelRecording()
    }
}
