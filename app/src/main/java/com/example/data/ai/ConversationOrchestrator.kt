package com.example.data.ai

import com.example.data.model.RegulationMode
import com.example.data.safety.ContextAnalyzer
import com.example.data.safety.ConversationContext
import com.example.data.safety.ConversationState
import com.example.data.safety.EscalationManager
import com.example.data.safety.GeminiContextAnalysis
import com.example.data.safety.RiskDecisionEngine
import com.example.data.safety.RiskLevel
import com.example.data.safety.RiskSignals
import com.example.data.safety.RiskState
import com.example.data.safety.ResponseDeduplicationManager
import com.example.data.safety.SafetyResponseHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class OrchestratedTurnResult(
    val replyText: String,
    val updatedRiskState: RiskState,
    val riskLevel: RiskLevel,
    val conversationState: ConversationState,
    val responseType: ResponseType,
    val showEmergencyUI: Boolean,
    val showSupportCard: Boolean,
    val signals: RiskSignals,
    val geminiAnalysis: GeminiContextAnalysis
)

/**
 * Hybrid Response Orchestrator for Solace+.
 * Implements the complete 11-step execution pipeline from raw user message to verified, safe response.
 */
class ConversationOrchestrator(
    private val solaceAiService: SolaceAiService = SolaceAiService(),
    private val contextAnalyzer: GeminiContextAnalyzer = GeminiContextAnalyzer()
) {

    /**
     * Executes the complete rule-based conversational pipeline synchronously or suspendable.
     */
    suspend fun processTurn(
        rawMessage: String,
        context: ConversationContext,
        customApiKey: String? = null
    ): OrchestratedTurnResult {
        // 1. INPUT NORMALISATION
        val normalizedInput = rawMessage.trim()
        if (normalizedInput.isEmpty()) {
            return OrchestratedTurnResult(
                replyText = "",
                updatedRiskState = context.currentState,
                riskLevel = context.currentState.currentLevel,
                conversationState = context.currentState.conversationState,
                responseType = ResponseType.LOCAL_ADAPTIVE_SANCTUARY,
                showEmergencyUI = context.currentState.emergencyTriggered,
                showSupportCard = context.currentState.needsHumanSupport,
                signals = RiskSignals(),
                geminiAnalysis = GeminiContextAnalysis()
            )
        }

        // 2. CONTEXT RETRIEVAL: history & current risk state
        val history = context.messageHistory
        val currentState = context.currentState

        // 3. CLINICAL SAFETY EVALUATION (Priority Override)
        val safetyResult = EscalationManager.evaluate(
            userMessage = normalizedInput,
            history = history,
            currentState = currentState,
            userName = context.userName,
            country = context.country
        )

        // If clinical safety override is active (Level 2-4 or safety question progression)
        if (safetyResult.overrideResponse != null || safetyResult.level.isAtLeast(RiskLevel.LEVEL_3_SUICIDAL_IDEATION)) {
            val reply = safetyResult.overrideResponse ?: SafetyResponseHandler.generateResponse(
                level = safetyResult.level,
                signals = safetyResult.signals,
                userName = context.userName,
                country = context.country,
                state = safetyResult.updatedState
            )
            val verifiedReply = ResponseRouter.validateGeneratedResponse(reply, safetyResult.level)
            ResponseDeduplicationManager.recordDelivered(context.conversationId, verifiedReply)

            val assignedResponseType = when {
                safetyResult.level == RiskLevel.LEVEL_4_CRITICAL -> ResponseType.EMERGENCY_INTERVENTION
                safetyResult.level.isAtLeast(RiskLevel.LEVEL_2_ELEVATED) -> ResponseType.SAFETY_SCREENING
                else -> ResponseType.RULE_BASED
            }

            return OrchestratedTurnResult(
                replyText = verifiedReply,
                updatedRiskState = safetyResult.updatedState,
                riskLevel = safetyResult.level,
                conversationState = safetyResult.conversationState,
                responseType = assignedResponseType,
                showEmergencyUI = safetyResult.level == RiskLevel.LEVEL_4_CRITICAL,
                showSupportCard = safetyResult.level.isAtLeast(RiskLevel.LEVEL_2_ELEVATED) && safetyResult.showSupportCard,
                signals = safetyResult.signals,
                geminiAnalysis = GeminiContextAnalysis()
            )
        }

        // 4. DETERMINISTIC EMOTIONAL SUPPORT ENGINE (Levels 0-1)
        val engineResult = com.example.data.safety.DeterministicEmotionalEngine.processTurn(
            userMessage = normalizedInput,
            context = context.copy(currentState = safetyResult.updatedState)
        )

        val finalRiskLevel = if (safetyResult.level == RiskLevel.LEVEL_1_DISTRESS) RiskLevel.LEVEL_1_DISTRESS else engineResult.riskLevel

        val updatedRiskState = safetyResult.updatedState.copy(
            currentLevel = finalRiskLevel,
            conversationState = engineResult.conversationState,
            pendingQuestionId = engineResult.pendingQuestionId?.name,
            turnCount = currentState.turnCount + 1,
            updatedAt = System.currentTimeMillis()
        )

        val verifiedReply = ResponseRouter.validateGeneratedResponse(engineResult.responseText, finalRiskLevel)
        ResponseDeduplicationManager.recordDelivered(context.conversationId, verifiedReply)

        return OrchestratedTurnResult(
            replyText = verifiedReply,
            updatedRiskState = updatedRiskState,
            riskLevel = finalRiskLevel,
            conversationState = engineResult.conversationState,
            responseType = ResponseType.RULE_BASED,
            showEmergencyUI = false,
            showSupportCard = false,
            signals = safetyResult.signals,
            geminiAnalysis = GeminiContextAnalysis(responseMode = engineResult.appliedMode.name)
        )
    }

    /**
     * Streams responses for Jetpack Compose UI while maintaining deterministic safety checks.
     */
    fun streamTurn(
        rawMessage: String,
        context: ConversationContext,
        customApiKey: String? = null
    ): Flow<Pair<String, OrchestratedTurnResult>> = flow {
        val result = processTurn(rawMessage, context, customApiKey)

        // Stream the verified reply text in gentle words
        val words = result.replyText.split(" ")
        val sb = StringBuilder()
        for (i in words.indices step 3) {
            val chunk = words.subList(i, minOf(i + 3, words.size)).joinToString(" ") + " "
            sb.append(chunk)
            emit(sb.toString() to result)
            kotlinx.coroutines.delay(35)
        }
    }
}

