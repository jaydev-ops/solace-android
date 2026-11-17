package com.example.data.safety

/**
 * Solace+ Four-Level Risk Classification for Mental Health Safety.
 * Strictly adheres to clinical and deterministic priority tiers.
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
 * Explicit conversational state machine states for Solace+.
 * Tracks conversation progression from ordinary emotional dialogue to active safety screening,
 * professional help recommendations, confirmation follow-up, and critical emergency workflows.
 */
enum class ConversationState {
    NORMAL,
    SUPPORTIVE_CONVERSATION,
    SAFETY_TRIGGERED,
    SAFETY_ASSESSMENT,
    SAFETY_SCREENING,
    ELEVATED_DISTRESS,
    SUICIDAL_IDEATION,
    HELP_RECOMMENDED,
    HELP_CONTACT_PENDING,
    HUMAN_SUPPORT_CONTACTED,
    SAFETY_CONFIRMATION_PENDING,
    UNRESOLVED_SAFETY_CONCERN,
    EMERGENCY_WORKFLOW_ACTIVE,
    EMERGENCY_RESPONSE,
    AWAITING_HUMAN_SUPPORT
}

/**
 * Structured identifiers for each safety screening question in the deterministic progression.
 */
enum class SafetyQuestionId(val id: String, val questionText: String) {
    CURRENT_SUICIDAL_THOUGHTS(
        "CURRENT_SUICIDAL_THOUGHTS",
        "Are you having thoughts of ending your life at this moment?"
    ),
    IMMEDIATE_INTENT(
        "IMMEDIATE_INTENT",
        "Have you thought about acting on these thoughts?"
    ),
    PLAN_AND_TIMEFRAME(
        "PLAN_AND_TIMEFRAME",
        "Have you thought about how or when you might act on these thoughts?"
    ),
    ACCESS_TO_MEANS(
        "ACCESS_TO_MEANS",
        "Do you have access to anything you might use to hurt yourself?"
    ),
    ACTION_ALREADY_TAKEN(
        "ACTION_ALREADY_TAKEN",
        "Have you already taken any steps to hurt yourself?"
    ),
    HUMAN_SUPPORT(
        "HUMAN_SUPPORT",
        "Is there someone you trust who could come and stay with you right now?"
    )
}

/**
 * Categorization of user answer to a safety question.
 */
enum class SafetyAnswer(val value: String) {
    YES("YES"),
    NO("NO"),
    AMBIGUOUS("AMBIGUOUS"),
    NONE("NONE")
}

/**
 * Categorization of user responses during safety follow-up and verification.
 */
enum class UserConfirmationType {
    CONTACTED_SOMEONE,
    SOMEONE_PHYSICALLY_PRESENT,
    CONTACTED_PROFESSIONAL,
    DANGER_REDUCED,
    REFUSED_ASSISTANCE,
    AMBIGUOUS_ANSWER,
    NO_RESPONSE
}

/**
 * Structured risk signals extracted from user messages and conversational context.
 */
data class RiskSignals(
    // Keyword Triggering
    val hasKeywordTrigger: Boolean = false,
    val matchedKeywords: List<String> = emptyList(),
    val confirmationType: UserConfirmationType? = null,
    val selfHarmOnly: Boolean = false,

    // Level 1: Emotional distress
    val emotionalDistress: Boolean = false,
    val sadness: Boolean = false,
    val loneliness: Boolean = false,
    val stress: Boolean = false,
    val emotionalExhaustion: Boolean = false,
    val frustration: Boolean = false,
    val temporaryHopelessness: Boolean = false,

    // Level 2: Significant psychological distress
    val persistentHopelessness: Boolean = false,
    val worthlessness: Boolean = false,
    val perceivedBurdensomeness: Boolean = false,
    val persistentDespair: Boolean = false,
    val futurelessness: Boolean = false,
    val lossOfPurpose: Boolean = false,
    val persistentEmotionalNumbness: Boolean = false,
    val beliefNothingWillImprove: Boolean = false,

    // Level 3: Suicidal ideation
    val passiveDeathWish: Boolean = false,
    val activeSuicidalThoughts: Boolean = false,
    val recurrentDeathThoughts: Boolean = false,
    val desireToDisappear: Boolean = false,
    val notWantingToExist: Boolean = false,

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
    val isQuotedOrThirdParty: Boolean = false,
    val isSarcastic: Boolean = false,

    // Evaluation metadata
    val confidence: Float = 1.0f,
    val evidence: List<String> = emptyList()
)

/**
 * Context wrapper passed into the deterministic decision engine.
 */
data class ConversationContext(
    val conversationId: String = "active_session",
    val userId: String = "",
    val currentState: RiskState = RiskState(),
    val messageHistory: List<Pair<String, String>> = emptyList(),
    val currentMood: String? = null,
    val moodIntensity: Int? = null,
    val userName: String? = null,
    val country: String = "IN"
)

/**
 * Structured JSON analysis returned by Gemini (when available).
 * Gemini provides analysis only; it NEVER decides or overrides the final risk state.
 */
data class GeminiContextAnalysis(
    val intent: String = "emotional_support",
    val emotion: String = "neutral",
    val responseMode: String = "LISTEN",
    val suggestedAction: String = "explore_feelings",
    val riskIndicators: List<String> = emptyList(),
    val requiresClarification: Boolean = false,
    val contextSummary: String = "",
    val isLiteral: Boolean = true,
    val isFigurative: Boolean = false,
    val isFictionalOrHypothetical: Boolean = false
)

/**
 * Persistent risk escalation state and state machine across conversation turns.
 */
data class RiskState(
    val userId: String = "",
    val conversationId: String = "active_session",
    val currentLevel: RiskLevel = RiskLevel.LEVEL_0_NORMAL,
    val highestLevel: RiskLevel = RiskLevel.LEVEL_0_NORMAL,
    val conversationState: ConversationState = ConversationState.NORMAL,
    val lastAssessment: String = "",
    val needsHumanSupport: Boolean = false,
    val emergencyTriggered: Boolean = false,
    val unresolvedSafetyConcern: Boolean = false,
    val turnCount: Int = 0,
    val lastScreeningQuestionAsked: String? = null,
    val awaitingScreeningAnswer: Boolean = false,
    val pendingQuestionId: String? = null,
    val lastAnsweredQuestionId: String? = null,
    val lastAnswer: String? = null,
    val questionAnswersMap: Map<String, String> = emptyMap(),
    val helpRecommended: Boolean = false,
    val helpContactPending: Boolean = false,
    val humanSupportContacted: Boolean = false,
    val safetyConfirmed: Boolean = false,
    val physicalSupportPresent: Boolean = false,
    val answeredQuestions: List<String> = emptyList(),
    val deliveredResponseHashes: List<String> = emptyList(),
    val lastConfirmationType: String? = null,
    val sequentialScreeningStep: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Updates the risk state adhering to strict safety invariants:
     * - Immediate escalation on higher risk
     * - LEVEL_4_CRITICAL cannot be casually downgraded
     * - Topic changes or positive moods cannot clear unresolved safety concerns
     */
    fun updateWith(
        newLevel: RiskLevel,
        reason: String,
        askedScreening: Boolean = false,
        resolvedConcern: Boolean = false,
        stateOverride: ConversationState? = null,
        newHelpRecommended: Boolean = this.helpRecommended,
        newHelpPending: Boolean = this.helpContactPending,
        newHumanContacted: Boolean = this.humanSupportContacted,
        newSafetyConfirmed: Boolean = this.safetyConfirmed,
        newPhysicalPresent: Boolean = this.physicalSupportPresent,
        newScreeningStep: Int = this.sequentialScreeningStep,
        newConfirmationType: String? = this.lastConfirmationType,
        deliveredHash: String? = null,
        answeredQuestion: String? = null,
        newPendingQuestionId: String? = this.pendingQuestionId,
        newLastAnsweredQuestionId: String? = this.lastAnsweredQuestionId,
        newLastAnswer: String? = this.lastAnswer,
        newQuestionAnswersMap: Map<String, String> = this.questionAnswersMap
    ): RiskState {
        val updatedLevel = if (emergencyTriggered && newLevel.severity < RiskLevel.LEVEL_4_CRITICAL.severity) {
            // Level 4 cannot be casually downgraded unless verified safe resolution with human support
            if (newPhysicalPresent || newHumanContacted) {
                newLevel
            } else {
                RiskLevel.LEVEL_4_CRITICAL
            }
        } else {
            newLevel
        }

        val updatedHighest = if (updatedLevel.isHigherThan(highestLevel)) updatedLevel else highestLevel
        val isEmergency = updatedLevel == RiskLevel.LEVEL_4_CRITICAL
        val needsSupport = (updatedLevel.isAtLeast(RiskLevel.LEVEL_2_ELEVATED) || newHelpRecommended) && !resolvedConcern
        val hasConcern = if (resolvedConcern) {
            false
        } else {
            (unresolvedSafetyConcern || updatedLevel.isAtLeast(RiskLevel.LEVEL_2_ELEVATED)) &&
                !isSafeResolution(newLevel)
        }

        val nextState = stateOverride ?: when {
            isEmergency -> ConversationState.EMERGENCY_RESPONSE
            newHelpPending -> ConversationState.HELP_CONTACT_PENDING
            newHumanContacted -> ConversationState.HUMAN_SUPPORT_CONTACTED
            updatedLevel == RiskLevel.LEVEL_3_SUICIDAL_IDEATION -> ConversationState.SUICIDAL_IDEATION
            updatedLevel == RiskLevel.LEVEL_2_ELEVATED && askedScreening -> ConversationState.SAFETY_SCREENING
            updatedLevel == RiskLevel.LEVEL_2_ELEVATED -> ConversationState.ELEVATED_DISTRESS
            newHelpRecommended -> ConversationState.HELP_RECOMMENDED
            updatedLevel == RiskLevel.LEVEL_1_DISTRESS -> ConversationState.SUPPORTIVE_CONVERSATION
            hasConcern -> ConversationState.SAFETY_SCREENING
            else -> ConversationState.NORMAL
        }

        val updatedHashes = if (deliveredHash != null && !deliveredResponseHashes.contains(deliveredHash)) {
            deliveredResponseHashes + deliveredHash
        } else {
            deliveredResponseHashes
        }

        val updatedAnswered = if (answeredQuestion != null && !answeredQuestions.contains(answeredQuestion)) {
            answeredQuestions + answeredQuestion
        } else {
            answeredQuestions
        }

        return this.copy(
            currentLevel = updatedLevel,
            highestLevel = updatedHighest,
            conversationState = nextState,
            lastAssessment = reason,
            needsHumanSupport = needsSupport,
            emergencyTriggered = emergencyTriggered || isEmergency,
            unresolvedSafetyConcern = hasConcern,
            turnCount = turnCount + 1,
            awaitingScreeningAnswer = if (resolvedConcern) false else askedScreening,
            helpRecommended = newHelpRecommended,
            helpContactPending = newHelpPending,
            humanSupportContacted = newHumanContacted,
            safetyConfirmed = newSafetyConfirmed,
            physicalSupportPresent = newPhysicalPresent,
            sequentialScreeningStep = newScreeningStep,
            lastConfirmationType = newConfirmationType,
            deliveredResponseHashes = updatedHashes,
            answeredQuestions = updatedAnswered,
            pendingQuestionId = newPendingQuestionId,
            lastAnsweredQuestionId = newLastAnsweredQuestionId,
            lastAnswer = newLastAnswer,
            questionAnswersMap = newQuestionAnswersMap,
            updatedAt = System.currentTimeMillis()
        )
    }

    private fun isSafeResolution(newLevel: RiskLevel): Boolean {
        // Explicit safety confirmations without ongoing indicators allow de-escalation
        return (newLevel == RiskLevel.LEVEL_0_NORMAL || newLevel == RiskLevel.LEVEL_1_DISTRESS) && !emergencyTriggered
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
    val showSupportCard: Boolean,
    val conversationState: ConversationState = updatedState.conversationState
)
