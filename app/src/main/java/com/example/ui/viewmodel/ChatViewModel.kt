package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.ChatbotRole
import com.example.data.ai.ConversationOrchestrator
import com.example.data.ai.ResponseType
import com.example.data.local.SolaceDatabase
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.UserPreferencesEntity
import com.example.data.repository.FirestoreRepository
import com.example.data.repository.SolaceRepository
import com.example.data.safety.ConversationContext
import com.example.data.safety.RiskLevel
import com.example.data.safety.RiskState
import com.example.data.safety.SafetyStateManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    private val safetyStateManager = SafetyStateManager(repository)
    private val orchestrator = ConversationOrchestrator()

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

    // Role selection
    private val _selectedRole = MutableStateFlow(ChatbotRole.SANCTUARY_COMPANION)
    val selectedRole: StateFlow<ChatbotRole> = _selectedRole.asStateFlow()

    // Graduated Mental Health Risk State (Persistently tracked across turns and restarts)
    private val _riskState = MutableStateFlow(RiskState())
    val riskState: StateFlow<RiskState> = _riskState.asStateFlow()

    init {
        // Restore active safety state upon ViewModel initialization (Phase 5 / Phase 9)
        viewModelScope.launch {
            try {
                val restoredState = safetyStateManager.getInitialState()
                _riskState.value = restoredState
            } catch (e: Exception) {
                Log.w(TAG, "Could not restore safety state: ${e.message}")
            }
        }
    }

    fun selectRole(role: ChatbotRole) {
        _selectedRole.value = role
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val currentRole = _selectedRole.value
        val trimmed = userText.trim()
        val prefs = userPreferences.value
        val saveMemory = prefs.allowMemorySaving

        viewModelScope.launch {
            val userMsg = ChatMessageEntity(
                senderRole = "user",
                content = trimmed,
                regulationMode = currentRole.id,
                riskLevel = null,
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
                        modelName = "solace-rule-based",
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

            // 3. Build Conversation Context
            val conversationContext = ConversationContext(
                conversationId = _riskState.value.conversationId.ifBlank { "active_session" },
                currentState = _riskState.value,
                messageHistory = history,
                userName = prefs.userName.trim(),
                country = "IN"
            )

            _uiState.value = ChatUiState.Loading

            try {
                // 4. Process Turn through Hybrid Orchestrator Pipeline (Phases 1, 2, 3, 4, 6, 7)
                val turnResult = orchestrator.processTurn(
                    rawMessage = trimmed,
                    context = conversationContext,
                    customApiKey = prefs.customGeminiApiKey
                )

                // 5. Update and Persist Safety State immediately
                _riskState.value = turnResult.updatedRiskState
                safetyStateManager.persistState(turnResult.updatedRiskState)

                val replyText = turnResult.replyText

                // Stream reply in word chunks for gentle, soothing cadence
                if (replyText.isNotBlank()) {
                    val words = replyText.split(" ")
                    val accumulated = StringBuilder()
                    val chunkSize = 3
                    for (i in words.indices step chunkSize) {
                        val chunk = words.subList(i, minOf(i + chunkSize, words.size)).joinToString(" ") + " "
                        accumulated.append(chunk)
                        _uiState.value = ChatUiState.Streaming(accumulated.toString())
                        delay(35)
                    }

                    val contextLabel = when (turnResult.responseType) {
                        ResponseType.EMERGENCY_INTERVENTION -> "Safety • Immediate Danger (112)"
                        ResponseType.SAFETY_SCREENING -> "Safety • ${turnResult.riskLevel.title}"
                        ResponseType.RULE_BASED -> "Solace Sanctuary Grounding"
                        else -> "Solace AI"
                    }

                    val aiMsg = ChatMessageEntity(
                        senderRole = "assistant",
                        content = replyText,
                        regulationMode = currentRole.id,
                        contextAnchor = contextLabel,
                        riskLevel = turnResult.riskLevel.name,
                        timestamp = System.currentTimeMillis()
                    )

                    if (saveMemory) {
                        repository.saveChatMessage(
                            senderRole = "assistant",
                            content = replyText,
                            regulationMode = currentRole.id,
                            contextAnchor = contextLabel,
                            riskLevel = turnResult.riskLevel.name
                        )
                    } else {
                        _ephemeralMessages.value = _ephemeralMessages.value + aiMsg
                    }

                    if (saveMemory && auth?.currentUser != null) {
                        try {
                            firestoreRepository.saveChatMessage(
                                senderRole = "model",
                                content = replyText,
                                modelName = "solace-rule-based",
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
                Log.e(TAG, "Chat orchestration error", e)
                _uiState.value = ChatUiState.Error(e.localizedMessage ?: "Failed to generate reply.")
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
            safetyStateManager.resetState()
            _ephemeralMessages.value = emptyList()
            _riskState.value = RiskState()
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
}
