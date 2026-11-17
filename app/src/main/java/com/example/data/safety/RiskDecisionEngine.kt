package com.example.data.safety

/**
 * Deterministic IF-ELSE Risk Decision Engine for Solace+.
 *
 * CRITICAL SAFETY INVARIANT:
 * Highest-risk conditions are ALWAYS evaluated first.
 * Probabilistic LLM outputs or keyword absences can NEVER override or downgrade safety.
 */
object RiskDecisionEngine {

    /**
     * Primary deterministic risk evaluation taking signals and full conversation context.
     */
    fun determineRisk(
        signals: RiskSignals,
        context: ConversationContext
    ): RiskLevel {
        return evaluateDeterministicRules(signals, context.currentState)
    }

    /**
     * Overload taking signals and optional RiskState for backward compatibility.
     */
    fun determineRisk(
        signals: RiskSignals,
        context: RiskState? = null
    ): RiskLevel {
        return evaluateDeterministicRules(signals, context)
    }

    private fun evaluateDeterministicRules(
        signals: RiskSignals,
        state: RiskState?
    ): RiskLevel {
        // 0. Filter out non-suicidal figurative idioms and fictional contexts
        // Unless combined with contradictory high-risk signals (e.g. actual attempt or explicit plan)
        if ((signals.isFigurative || signals.isFictionalOrHypothetical || signals.isQuotedOrThirdParty) &&
            !signals.recentAttempt && !signals.currentSuicidalIntent && !signals.imminentPlan
        ) {
            return RiskLevel.LEVEL_0_NORMAL
        }

        // 1. LEVEL 4: Immediate danger / Emergency
        // Prioritized above all other conditions
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
                signals.recurrentDeathThoughts ||
                signals.desireToDisappear ||
                signals.notWantingToExist
            )
        ) {
            return RiskLevel.LEVEL_3_SUICIDAL_IDEATION
        }

        // 3. LEVEL 2: Significant psychological distress & clinical warning signals
        else if (
            signals.selfHarmOnly ||
            signals.persistentHopelessness ||
            signals.worthlessness ||
            signals.perceivedBurdensomeness ||
            signals.persistentDespair ||
            signals.futurelessness ||
            signals.lossOfPurpose ||
            signals.persistentEmotionalNumbness ||
            signals.beliefNothingWillImprove ||
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
            signals.frustration ||
            signals.temporaryHopelessness
        ) {
            return RiskLevel.LEVEL_1_DISTRESS
        }

        // 5. No detected risk
        else {
            return RiskLevel.LEVEL_0_NORMAL
        }
    }
}
