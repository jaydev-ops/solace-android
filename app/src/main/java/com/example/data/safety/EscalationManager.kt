package com.example.data.safety

object EscalationManager {

    /**
     * Process user message against current risk state and history to perform deterministic escalation.
     */
    fun evaluate(
        userMessage: String,
        history: List<Pair<String, String>> = emptyList(),
        currentState: RiskState = RiskState(),
        userName: String? = null,
        country: String = "IN"
    ): SafetyAssessmentResult {
        // 1. Contextual signal extraction
        val signals = ContextAnalyzer.extractSignals(userMessage, history, currentState)

        // 2. Deterministic IF-ELSE evaluation
        var evaluatedLevel = RiskDecisionEngine.determineRisk(signals, currentState)

        // 3. State-based escalation & safety persistence rules
        // Rule: A Level 4 emergency cannot be casually downgraded by changing the subject
        if (currentState.emergencyTriggered && evaluatedLevel.severity < RiskLevel.LEVEL_4_CRITICAL.severity) {
            // Check if user is actively cooperating with safety or if emergency remains active
            val lower = userMessage.lowercase()
            val isExplicitSafeResolution = lower.contains("i am with my friend now") ||
                lower.contains("i am with a doctor") ||
                lower.contains("at the hospital")

            if (!isExplicitSafeResolution) {
                evaluatedLevel = RiskLevel.LEVEL_4_CRITICAL
            }
        }

        // Rule: If an unresolved safety concern exists from Level 2/3 and user hasn't explicitly answered,
        // do not silently treat them as safe Level 0.
        if (currentState.unresolvedSafetyConcern && evaluatedLevel == RiskLevel.LEVEL_0_NORMAL && !signals.negated) {
            // Keep at least Level 2 supportive posture
            evaluatedLevel = RiskLevel.LEVEL_2_ELEVATED
        }

        val reason = "Evaluated as ${evaluatedLevel.name} based on evidence: ${signals.evidence.joinToString("; ")}"
        val updatedState = currentState.updateWith(evaluatedLevel, reason)

        val isEmergency = evaluatedLevel == RiskLevel.LEVEL_4_CRITICAL
        val showEmergencyUI = isEmergency
        val showSupportCard = evaluatedLevel.isAtLeast(RiskLevel.LEVEL_2_ELEVATED)

        // Generate response for safety levels (Levels 2, 3, 4 override or guide chat)
        val response = if (evaluatedLevel.isAtLeast(RiskLevel.LEVEL_2_ELEVATED)) {
            SafetyResponseHandler.generateResponse(evaluatedLevel, signals, userName, country)
        } else if (evaluatedLevel == RiskLevel.LEVEL_1_DISTRESS && history.isEmpty()) {
            SafetyResponseHandler.generateResponse(evaluatedLevel, signals, userName, country)
        } else {
            null
        }

        return SafetyAssessmentResult(
            level = evaluatedLevel,
            signals = signals,
            updatedState = updatedState,
            overrideResponse = response,
            isEmergency = isEmergency,
            showEmergencyUI = showEmergencyUI,
            showSupportCard = showSupportCard
        )
    }
}

