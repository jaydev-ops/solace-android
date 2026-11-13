package com.example.data.safety

object RiskDecisionEngine {

    /**
     * Deterministic, rule-based risk evaluation using explicit IF-ELSE priority logic.
     * Highest-risk conditions are ALWAYS evaluated first.
     */
    fun determineRisk(signals: RiskSignals, context: RiskState? = null): RiskLevel {
        // 0. Filter out non-suicidal figurative expressions and fictional contexts
        if (signals.isFigurative || signals.isFictionalOrHypothetical) {
            return RiskLevel.LEVEL_0_NORMAL
        }

        // 1. LEVEL 4: Immediate danger / Emergency
        // Prioritized above all else
        if (
            signals.recentAttempt ||
            signals.currentSuicidalIntent ||
            signals.imminentPlan ||
            signals.preparatoryBehaviour ||
            signals.seriousCurrentSelfHarm
        ) {
            return RiskLevel.LEVEL_4_CRITICAL
        }

        // 2. LEVEL 3: Suicidal ideation (active or passive)
        else if (
            !signals.negated && (
                signals.activeSuicidalThoughts ||
                signals.passiveDeathWish ||
                signals.recurrentDeathThoughts
            )
        ) {
            return RiskLevel.LEVEL_3_SUICIDAL_IDEATION
        }

        // 3. LEVEL 2: Significant psychological distress & warning signals
        else if (
            signals.persistentHopelessness ||
            signals.worthlessness ||
            signals.perceivedBurdensomeness ||
            signals.persistentDespair ||
            signals.futurelessness ||
            signals.needsClarification
        ) {
            return RiskLevel.LEVEL_2_ELEVATED
        }

        // 4. LEVEL 1: General emotional distress
        else if (
            signals.emotionalDistress ||
            signals.sadness ||
            signals.loneliness ||
            signals.stress ||
            signals.emotionalExhaustion ||
            signals.frustration
        ) {
            return RiskLevel.LEVEL_1_DISTRESS
        }

        // 5. No detected risk
        else {
            return RiskLevel.LEVEL_0_NORMAL
        }
    }
}

