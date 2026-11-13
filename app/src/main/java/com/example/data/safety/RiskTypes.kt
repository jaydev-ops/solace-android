package com.example.data.safety

/**
 * Solace+ Four-Level Risk Classification for Mental Health Safety
 */
enum class RiskLevel(val severity: Int, val id: String, val title: String) {
    LEVEL_0_NORMAL(0, "LEVEL_0_NORMAL", "Normal / No Detected Risk"),
    LEVEL_1_DISTRESS(1, "LEVEL_1_DISTRESS", "Emotional Distress"),
    LEVEL_2_ELEVATED(2, "LEVEL_2_ELEVATED", "Significant Psychological Distress"),
    LEVEL_3_SUICIDAL_IDEATION(3, "LEVEL_3_SUICIDAL_IDEATION", "Suicidal Ideation"),
    LEVEL_4_CRITICAL(4, "LEVEL_4_CRITICAL", "Immediate Danger / Emergency");

    fun isHigherThan(other: RiskLevel): Boolean = this.severity > other.severity
    fun isAtLeast(other: RiskLevel): Boolean = this.severity >= other.severity
}

/**
 * Structured risk signals extracted from user messages and conversational context.
 */
data class RiskSignals(
    // Level 1: Emotional distress
    val emotionalDistress: Boolean = false,
    val sadness: Boolean = false,
    val loneliness: Boolean = false,
    val stress: Boolean = false,
    val emotionalExhaustion: Boolean = false,
    val frustration: Boolean = false,

    // Level 2: Significant psychological distress
    val persistentHopelessness: Boolean = false,
    val worthlessness: Boolean = false,
    val perceivedBurdensomeness: Boolean = false,
    val persistentDespair: Boolean = false,
    val futurelessness: Boolean = false,
    val lossOfPurpose: Boolean = false,

    // Level 3: Suicidal ideation
    val passiveDeathWish: Boolean = false,
    val activeSuicidalThoughts: Boolean = false,
    val recurrentDeathThoughts: Boolean = false,
    val desireToDisappear: Boolean = false,

    // Level 4: Immediate danger / Emergency
    val currentSuicidalIntent: Boolean = false,
    val specificPlan: Boolean = false,
    val accessToMeans: Boolean = false,
    val imminentPlan: Boolean = false,
    val preparatoryBehaviour: Boolean = false,
    val recentAttempt: Boolean = false,
    val seriousCurrentSelfHarm: Boolean = false,

    // Contextual modifiers
    val negated: Boolean = false,
    val ambiguous: Boolean = false,
    val needsClarification: Boolean = false,
    val isFigurative: Boolean = false,
    val isFictionalOrHypothetical: Boolean = false,

    // Evaluation metadata
    val confidence: Float = 1.0f,
    val evidence: List<String> = emptyList()
)

/**
 * Persistent risk escalation state across conversation turns.
 */
data class RiskState(
    val userId: String = "",
    val conversationId: String = "",
    val currentLevel: RiskLevel = RiskLevel.LEVEL_0_NORMAL,
    val highestLevel: RiskLevel = RiskLevel.LEVEL_0_NORMAL,
    val lastAssessment: String = "",
    val needsHumanSupport: Boolean = false,
    val emergencyTriggered: Boolean = false,
    val unresolvedSafetyConcern: Boolean = false,
    val turnCount: Int = 0,
    val lastScreeningQuestionAsked: String? = null,
    val awaitingScreeningAnswer: Boolean = false
) {
    fun updateWith(newLevel: RiskLevel, reason: String): RiskState {
        // Rule: Level 4 cannot be casually downgraded
        val updatedLevel = if (emergencyTriggered && newLevel.severity < RiskLevel.LEVEL_4_CRITICAL.severity) {
            RiskLevel.LEVEL_4_CRITICAL
        } else {
            newLevel
        }

        val updatedHighest = if (updatedLevel.isHigherThan(highestLevel)) updatedLevel else highestLevel
        val isEmergency = updatedLevel == RiskLevel.LEVEL_4_CRITICAL
        val needsSupport = updatedLevel.isAtLeast(RiskLevel.LEVEL_2_ELEVATED)
        val hasConcern = updatedLevel.isAtLeast(RiskLevel.LEVEL_2_ELEVATED)

        return this.copy(
            currentLevel = updatedLevel,
            highestLevel = updatedHighest,
            lastAssessment = reason,
            needsHumanSupport = needsSupport,
            emergencyTriggered = emergencyTriggered || isEmergency,
            unresolvedSafetyConcern = hasConcern,
            turnCount = turnCount + 1
        )
    }
}

/**
 * Configurable emergency and mental health resources.
 */
data class EmergencyResource(
    val name: String,
    val number: String,
    val description: String,
    val category: String, // "emergency", "mental_health", "crisis_text"
    val country: String = "IN"
)

/**
 * Complete assessment outcome returned by the safety module.
 */
data class SafetyAssessmentResult(
    val level: RiskLevel,
    val signals: RiskSignals,
    val updatedState: RiskState,
    val overrideResponse: String?,
    val isEmergency: Boolean,
    val showEmergencyUI: Boolean,
    val showSupportCard: Boolean
)
