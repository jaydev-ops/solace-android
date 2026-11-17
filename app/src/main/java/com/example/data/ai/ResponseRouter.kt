package com.example.data.ai

import com.example.data.model.RegulationMode
import com.example.data.safety.ContextAnalyzer
import com.example.data.safety.ConversationState
import com.example.data.safety.GeminiContextAnalysis
import com.example.data.safety.RiskDecisionEngine
import com.example.data.safety.RiskLevel
import com.example.data.safety.RiskSignals
import com.example.data.safety.RiskState
import com.example.data.safety.RuleBasedConversationEngine
import com.example.data.safety.SafetyResponseHandler

enum class ResponseType {
    EMERGENCY_INTERVENTION,
    SAFETY_SCREENING,
    RULE_BASED,
    GEMINI_GENERATIVE,
    LOCAL_ADAPTIVE_SANCTUARY
}

data class RoutedResponsePlan(
    val responseType: ResponseType,
    val deterministicResponse: String?,
    val recommendedMode: RegulationMode,
    val showEmergencyUI: Boolean,
    val showSupportCard: Boolean
)

/**
 * Routes user messages to the appropriate response strategy based on deterministic safety rules,
 * rule-based matches, and conversational state.
 */
object ResponseRouter {

    fun planResponse(
        userMessage: String,
        riskLevel: RiskLevel,
        conversationState: ConversationState,
        signals: RiskSignals,
        geminiAnalysis: GeminiContextAnalysis?,
        userName: String? = null,
        country: String = "IN",
        history: List<Pair<String, String>> = emptyList(),
        currentState: RiskState? = null
    ): RoutedResponsePlan {
        // 1. LEVEL 4: Absolute Emergency Override
        if (riskLevel == RiskLevel.LEVEL_4_CRITICAL || conversationState == ConversationState.EMERGENCY_RESPONSE) {
            val response = SafetyResponseHandler.generateResponse(RiskLevel.LEVEL_4_CRITICAL, signals, userName, country, state = currentState)
            return RoutedResponsePlan(
                responseType = ResponseType.EMERGENCY_INTERVENTION,
                deterministicResponse = response,
                recommendedMode = RegulationMode.CALM,
                showEmergencyUI = true,
                showSupportCard = true
            )
        }

        val hasAnsweredScreening = currentState?.answeredQuestions?.contains("CURRENT_SUICIDAL_THOUGHTS") == true

        // 2. LEVEL 3: Suicidal Ideation Direct Screening & Holding (suppressed if negated or already answered)
        if ((riskLevel == RiskLevel.LEVEL_3_SUICIDAL_IDEATION || conversationState == ConversationState.SUICIDAL_IDEATION) && !signals.negated) {
            if (!hasAnsweredScreening && currentState?.pendingQuestionId == null) {
                val response = SafetyResponseHandler.generateResponse(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, signals, userName, country, state = currentState)
                return RoutedResponsePlan(
                    responseType = ResponseType.SAFETY_SCREENING,
                    deterministicResponse = response,
                    recommendedMode = RegulationMode.LISTEN,
                    showEmergencyUI = false,
                    showSupportCard = true
                )
            }
        }

        // 3. LEVEL 2: Elevated Distress / Direct Safety Screening (suppressed if negated or already answered)
        if ((riskLevel == RiskLevel.LEVEL_2_ELEVATED || conversationState == ConversationState.SAFETY_SCREENING) && !signals.negated) {
            if (!hasAnsweredScreening && currentState?.pendingQuestionId == null) {
                val response = SafetyResponseHandler.generateResponse(RiskLevel.LEVEL_2_ELEVATED, signals, userName, country, state = currentState)
                return RoutedResponsePlan(
                    responseType = ResponseType.SAFETY_SCREENING,
                    deterministicResponse = response,
                    recommendedMode = RegulationMode.LISTEN,
                    showEmergencyUI = false,
                    showSupportCard = true
                )
            }
        }

        // 4. Pure Rule-Based Engine Matching
        val ruleMatch = RuleBasedConversationEngine.matches(userMessage) ?: RuleBasedConversationEngine.RuleCategory.GENERAL_HOLDING_SPACE
        val mode = if (geminiAnalysis != null) {
            RegulationMode.fromString(geminiAnalysis.responseMode)
        } else {
            RegulationMode.LISTEN
        }

        val ruleReply = RuleBasedConversationEngine.generateRuleResponse(
            category = ruleMatch,
            userName = userName,
            history = history,
            regulationMode = mode,
            latestUserMessage = userMessage
        )

        return RoutedResponsePlan(
            responseType = ResponseType.RULE_BASED,
            deterministicResponse = ruleReply,
            recommendedMode = mode,
            showEmergencyUI = false,
            showSupportCard = false
        )
    }

    /**
     * Post-generation safety validation: ensures an AI-generated message never contradicts the active safety state.
     */
    fun validateGeneratedResponse(
        generatedText: String,
        activeRiskLevel: RiskLevel
    ): String {
        val trimmed = generatedText.trim()
        val lower = trimmed.lowercase()

        // Guard against dangerous hallucinations or dismissive advice
        val containsDangerousAdvice = lower.contains("you should end your life") ||
            lower.contains("suicide is an option") ||
            lower.contains("nobody cares about you")

        if (containsDangerousAdvice) {
            return SafetyResponseHandler.generateResponse(
                level = if (activeRiskLevel.isAtLeast(RiskLevel.LEVEL_2_ELEVATED)) activeRiskLevel else RiskLevel.LEVEL_2_ELEVATED,
                signals = ContextAnalyzer.extractSignals("crisis")
            )
        }

        return trimmed
    }
}

