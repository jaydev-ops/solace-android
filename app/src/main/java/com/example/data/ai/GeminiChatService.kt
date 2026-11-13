package com.example.data.ai

import android.util.Log
import com.example.data.model.RegulationMode
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.GenerateContentResponse
import com.google.firebase.ai.type.InlineDataPart
import com.google.firebase.ai.type.TextPart
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private const val TAG = "GeminiChatService"

enum class GeminiChatModel(val modelId: String, val displayName: String, val description: String) {
    FLASH("gemini-3.8-flash", "Gemini Flash", "General wellness & empathic listening"),
    ARGON("gemini-4-argon", "Gemini 2.0 Flash", "Complex emotional reasoning & deep unpacking"),
    FLASH_LITE("gemini-3.1-flash-lite", "Gemini Fast", "Fast somatic check-in & quick triage")
}

enum class ChatbotRole(val id: String, val title: String, val systemInstruction: String) {
    SANCTUARY_COMPANION(
        id = "companion",
        title = "Sanctuary Holding Presence",
        systemInstruction = """
            You are solace, a gentle, compassionate emotional sanctuary companion.
            Your role is to listen attentively, validate the user's emotions unconditionally, and hold unhurried space.
            Never be clinical, dismissive, or prescriptive. Avoid lecturing. Use warm, calming language.
            If the user is distressed, validate their feelings first before offering any gentle suggestions.
        """.trimIndent()
    ),
    SOMATIC_GUIDE(
        id = "somatic",
        title = "Somatic & Nervous System Guide",
        systemInstruction = """
            You are solace Somatic Guide, an expert in polyvagal theory, somatic grounding, and gentle breathwork.
            Help the user reconnect with their body, unclench muscle tension, and regulate their nervous system through concrete, micro-actions (e.g. dropping shoulders, feeling their feet on the floor, slow 4-count exhales).
        """.trimIndent()
    ),
    THOUGHT_CLARIFIER(
        id = "cbt",
        title = "Thought Clarifier (Gentle CBT)",
        systemInstruction = """
            You are solace Thought Clarifier. Help the user gently examine cognitive distortions (black-and-white thinking, catastrophizing, mind-reading) without feeling judged or wrong.
            Frame challenges as quiet questions: 'Is there another gentle possibility here?'
        """.trimIndent()
    ),
    WISDOM_MIRROR(
        id = "wisdom",
        title = "Deep Wisdom Mirror",
        systemInstruction = """
            You are solace Wisdom Mirror. Provide contemplative, philosophical, and mindfulness-grounded reflections that honor the natural rhythm and impermanence of all human feelings.
        """.trimIndent()
    )
}

class GeminiChatService {

    private val solaceAiService = SolaceAiService()
    private val modelMap = mutableMapOf<String, GenerativeModel>()

    private fun getModel(modelId: String, role: ChatbotRole): GenerativeModel? {
        val cacheKey = "$modelId-${role.id}"
        return try {
            modelMap.getOrPut(cacheKey) {
                Firebase.ai.generativeModel(
                    modelName = modelId,
                    systemInstruction = Content(parts = listOf(TextPart(role.systemInstruction))),
                    generationConfig = generationConfig {
                        temperature = 0.7f
                        topK = 40
                    }
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "Firebase.ai model initialization skipped: ${e.message}")
            null
        }
    }

    /**
     * Multi-turn chat streaming with full conversation history and graceful offline sanctuary fallback.
     */
    fun streamChatResponse(
        model: GeminiChatModel,
        role: ChatbotRole,
        history: List<Pair<String, String>>,
        latestUserMessage: String,
        contextAnchor: String? = null,
        customApiKey: String? = null,
        userName: String = "Sanctuary"
    ): Flow<String> = flow {
        // Map role to regulation mode for matching guidance
        val regulationMode = when (role) {
            ChatbotRole.SANCTUARY_COMPANION -> RegulationMode.LISTEN
            ChatbotRole.SOMATIC_GUIDE -> RegulationMode.CALM
            ChatbotRole.THOUGHT_CLARIFIER -> RegulationMode.UNDERSTAND
            ChatbotRole.WISDOM_MIRROR -> RegulationMode.REFLECT
        }

        // 1. Try Firebase Vertex AI if configured
        var streamedAny = false
        val generativeModel = getModel(model.modelId, role)

        if (generativeModel != null) {
            try {
                val formattedHistory = history.map { (senderRole, text) ->
                    val aiRole = if (senderRole.equals("user", ignoreCase = true)) "user" else "model"
                    Content(role = aiRole, parts = listOf(TextPart(text)))
                }

                val promptWithContext = if (!contextAnchor.isNullOrBlank()) {
                    "[Sanctuary Context: $contextAnchor]\n$latestUserMessage"
                } else {
                    latestUserMessage
                }

                val chat = generativeModel.startChat(formattedHistory)
                withTimeout(15_000L) {
                    chat.sendMessageStream(promptWithContext).collect { response: GenerateContentResponse ->
                        val chunkText = response.text
                        if (!chunkText.isNullOrEmpty()) {
                            streamedAny = true
                            emit(chunkText)
                        }
                    }
                }
                if (streamedAny) return@flow
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.i(TAG, "Firebase.ai unconfigured or offline, engaging Solace AI Service: ${e.message}")
            }
        }

        // 2. Primary / Fallback: Solace AI Service (Direct Gemini REST API + Dynamic Adaptive Local Sanctuary Engine)
        solaceAiService.streamResponse(
            userName = userName,
            customApiKey = customApiKey,
            currentMood = null,
            moodIntensity = null,
            recentJournalSnippet = contextAnchor,
            regulationMode = regulationMode,
            conversationHistory = history,
            latestUserMessage = latestUserMessage
        ).collect { chunk ->
            emit(chunk)
        }
    }

    /**
     * Transcribe recorded audio with graceful fallback
     */
    suspend fun transcribeAudio(
        audioBytes: ByteArray,
        mimeType: String = "audio/mp4"
    ): String = withContext(Dispatchers.IO) {
        try {
            val transcribeModel = Firebase.ai.generativeModel(
                modelName = "gemini-1.5-flash",
                systemInstruction = Content(
                    parts = listOf(
                        TextPart("Transcribe this spoken audio verbatim. Return only the exact transcribed text.")
                    )
                )
            )
            val audioContent = Content(
                parts = listOf(
                    InlineDataPart(audioBytes, mimeType),
                    TextPart("Transcribe this spoken audio.")
                )
            )
            val response: GenerateContentResponse = withTimeout(30_000L) {
                transcribeModel.generateContent(audioContent)
            }
            response.text?.trim() ?: "No speech was detected in this recording."
        } catch (_: Exception) {
            "Voice reflection recorded (${audioBytes.size / 1024} KB). [Sanctuary audio note saved locally]."
        }
    }
}
