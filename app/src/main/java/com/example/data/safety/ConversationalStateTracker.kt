package com.example.data.safety

import java.util.concurrent.ConcurrentHashMap

enum class ConversationalStage {
    STAGE_1_INITIAL_DISCLOSURE,
    STAGE_2_CONTEXT_DISCOVERY,
    STAGE_3_EMOTIONAL_SUPPORT,
    STAGE_4_CONTINUED_CONVERSATION,
    STAGE_5_SUPPORTIVE_FOLLOW_THROUGH
}

data class ConversationStateRecord(
    val conversationId: String,
    var primaryEmotion: EmotionalClassificationEngine.EmotionCategory? = null,
    val secondaryEmotions: MutableSet<EmotionalClassificationEngine.EmotionCategory> = mutableSetOf(),
    val disclosedSituations: MutableSet<UserSituation> = mutableSetOf(),
    var lastDisclosedSituation: UserSituation? = null,
    var currentStage: ConversationalStage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
    var turnCount: Int = 0,
    var lastUserMessage: String = "",
    var consecutiveIdenticalMessageCount: Int = 0,
    val deliveredTemplateIds: MutableList<String> = mutableListOf(),
    val deliveredResponses: MutableList<String> = mutableListOf()
)

object ConversationalStateTracker {

    private val sessions = ConcurrentHashMap<String, ConversationStateRecord>()

    fun getOrCreate(conversationId: String): ConversationStateRecord {
        val safeId = conversationId.ifBlank { "active_session" }
        return sessions.getOrPut(safeId) {
            ConversationStateRecord(conversationId = safeId)
        }
    }

    fun clearSession(conversationId: String) {
        val safeId = conversationId.ifBlank { "active_session" }
        sessions.remove(safeId)
    }

    /**
     * Updates state upon receiving a user message and running classification.
     */
    fun updateState(
        conversationId: String,
        userMessage: String,
        detectedCategory: EmotionalClassificationEngine.EmotionCategory?,
        secondaryCategories: List<EmotionalClassificationEngine.EmotionCategory>,
        detectedSituation: UserSituation?
    ): ConversationStateRecord {
        val state = getOrCreate(conversationId)
        val normalized = userMessage.trim().lowercase()

        // 1. Check for identical message repetition
        if (state.lastUserMessage.isNotBlank() && state.lastUserMessage == normalized) {
            state.consecutiveIdenticalMessageCount += 1
        } else {
            state.consecutiveIdenticalMessageCount = 0
            state.lastUserMessage = normalized
        }

        state.turnCount += 1

        // 2. Situation Tracking & Preservation
        if (detectedSituation != null) {
            state.disclosedSituations.add(detectedSituation)
            state.lastDisclosedSituation = detectedSituation
        }

        // 3. Contextual Emotion Transitions
        // If we previously had an emotion (e.g. ANXIETY) and now learn of a situational trigger (e.g. BREAKUP),
        // elevate the situational emotion to primary and demote previous emotion to secondary!
        if (detectedSituation != null) {
            val previousPrimary = state.primaryEmotion
            if (previousPrimary != null && previousPrimary != detectedCategory) {
                state.secondaryEmotions.add(previousPrimary)
            }

            when (detectedSituation) {
                UserSituation.BREAKUP,
                UserSituation.HEARTBREAK,
                UserSituation.BEING_CHEATED_ON,
                UserSituation.RELATIONSHIP_ARGUMENTS,
                UserSituation.BEING_IGNORED,
                UserSituation.BEING_REJECTED,
                UserSituation.LONG_DISTANCE,
                UserSituation.FEAR_OF_LOSING_SOMEONE,
                UserSituation.FEELING_UNWANTED -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS
                    if (previousPrimary == EmotionalClassificationEngine.EmotionCategory.ANXIETY) {
                        state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.ANXIETY)
                    }
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.SADNESS)
                }

                UserSituation.MISSING_EX -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.SADNESS
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.LONELINESS)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS)
                }

                UserSituation.EX_SOCIAL_MEDIA_CHECKING -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.ANXIETY)
                }

                UserSituation.UNSUCCESSFUL_EFFORT,
                UserSituation.EXAM_FAILURE,
                UserSituation.POOR_GRADES,
                UserSituation.ASSIGNMENT_PRESSURE,
                UserSituation.DIFFICULTY_CONCENTRATING -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.ANXIETY)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM)
                }

                UserSituation.ACADEMIC_COMPARISON,
                UserSituation.PEER_ACHIEVEMENT_ENVY,
                UserSituation.SOCIAL_MEDIA_ENVY -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.INSECURITY)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.ANXIETY)
                }

                UserSituation.FRIENDSHIP_DRIFT,
                UserSituation.ONE_SIDED_FRIENDSHIP,
                UserSituation.THIRD_WHEEL,
                UserSituation.LOSING_FRIENDS,
                UserSituation.FEELING_EXCLUDED,
                UserSituation.SOCIAL_REJECTION -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.LONELINESS
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.SADNESS)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS)
                }

                UserSituation.BEING_JUDGED,
                UserSituation.FRIENDSHIP_CONFLICTS -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY
                }

                UserSituation.PARENTAL_CAREER_PRESSURE,
                UserSituation.SIBLING_COMPARISON,
                UserSituation.FAMILY_CONFLICTS,
                UserSituation.DISAPPOINTING_PARENTS -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.ANGER)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.GUILT)
                }

                UserSituation.DIRECTIONLESS,
                UserSituation.FEAR_WRONG_DEGREE,
                UserSituation.FINANCIAL_PRESSURE,
                UserSituation.CAREER_UNCERTAINTY -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.ANXIETY
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.FEAR)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.OVERTHINKING)
                }

                UserSituation.LOSS_OF_LOVED_ONE -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.GRIEF
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.SADNESS)
                }

                UserSituation.PERSONAL_DISAPPOINTMENT -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.GUILT)
                }

                // Batch 2: Scenario 6 (Misunderstood and Unheard)
                UserSituation.PERSPECTIVE_DISMISSED,
                UserSituation.LABELED_OVERLY_SENSITIVE,
                UserSituation.COMMUNICATION_BREAKDOWN -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.LONELINESS)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.ANGER)
                }

                // Batch 2: Scenario 7 (Regret and Guilt)
                UserSituation.PAST_DECISION_REGRET,
                UserSituation.HURT_SOMEONE,
                UserSituation.SELF_BLAME -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.GUILT
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.SHAME)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.OVERTHINKING)
                }

                // Batch 2: Scenario 8 (Sleep Difficulties & Night Rumination)
                UserSituation.CANNOT_SLEEP_INSOMNIA,
                UserSituation.NIGHT_RUMINATION,
                UserSituation.LATE_NIGHT_LONELINESS -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.SLEEP_DIFFICULTIES
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.OVERTHINKING)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.ANXIETY)
                }

                // Batch 2: Scenario 9 (Social Anxiety & Fear of Judgement)
                UserSituation.MEETING_NEW_PEOPLE,
                UserSituation.POST_EVENT_RUMINATION,
                UserSituation.FEELING_AWKWARD,
                UserSituation.PUBLIC_SPEAKING_FEAR -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.FEAR)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.INSECURITY)
                }

                // Batch 2: Scenario 10 (Procrastination & Productivity Distress)
                UserSituation.PHONE_DOOMSCROLLING,
                UserSituation.TASK_OVERWHELM,
                UserSituation.AVOIDING_TASKS -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.PROCRASTINATION
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.GUILT)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.ANXIETY)
                }

                // Batch 3: Scenario 11 (Emotional Exhaustion & Burnout)
                UserSituation.MENTAL_DRAIN,
                UserSituation.ANHEDONIA_LOSS_OF_INTEREST,
                UserSituation.RESPONSIBILITY_OVERWHELM,
                UserSituation.WITHDRAWAL_URGE -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.BURNOUT
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.EMOTIONAL_EXHAUSTION)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.DEPRESSION_DISTRESS)
                }

                // Batch 3: Scenario 12 (Financial Stress & Dependence)
                UserSituation.EXPENSE_MANAGEMENT,
                UserSituation.PARENTAL_FINANCIAL_DEPENDENCE,
                UserSituation.PEER_WEALTH_COMPARISON -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.ANXIETY
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.INSECURITY)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.SHAME)
                }

                // Batch 3: Scenario 13 (Life Transitions & Unfamiliar Environments)
                UserSituation.COLLEGE_START,
                UserSituation.RELOCATING_NEW_CITY,
                UserSituation.ROUTINE_LOSS,
                UserSituation.NOSTALGIA_PAST -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.ANXIETY
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.LONELINESS)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.SADNESS)
                }

                // Batch 3: Scenario 14 (Interpersonal Conflict & Boundary Setting)
                UserSituation.HEATED_ARGUMENT,
                UserSituation.DISRESPECT_FELT -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.ANGER
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.FRUSTRATION)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS)
                }

                // Batch 3: Scenario 15 (Rejection & Unreciprocated Feelings)
                UserSituation.ROMANTIC_REJECTION,
                UserSituation.UNRECIPROCATED_FEELINGS,
                UserSituation.FEELING_UNWANTED_AFTER_NO -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.REJECTION
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.HEARTBREAK)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM)
                }

                // Batch 4: Scenario 16 (Grief and Loss)
                UserSituation.PET_LOSS,
                UserSituation.ANNIVERSARY_GRIEF,
                UserSituation.ABSENCE_LONGING -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.GRIEF
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.SADNESS)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.LONELINESS)
                }

                // Batch 4: Scenario 17 (Body Image & Self-Confidence)
                UserSituation.APPEARANCE_INSECURITY,
                UserSituation.WEIGHT_DISSATISFACTION,
                UserSituation.PHOTO_ANXIETY -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.INSECURITY
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.SHAME)
                }

                // Batch 4: Scenario 18 (Positive Emotions & Celebration)
                UserSituation.ACADEMIC_ACHIEVEMENT,
                UserSituation.PERSONAL_GOAL_REACHED,
                UserSituation.GENERAL_GRATITUDE -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS
                }

                // Batch 4: Scenario 19 (Everyday Frustration & Casual Venting)
                UserSituation.BAD_DAY,
                UserSituation.IRRITATED_SMALL_THINGS,
                UserSituation.CASUAL_VENTING -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.ANGER)
                }

                // Batch 4: Scenario 20 (Romantic Relationship Support)
                UserSituation.RELATIONSHIP_INSECURITY,
                UserSituation.PARTNER_DISTANCING,
                UserSituation.FEAR_OF_ABANDONMENT,
                UserSituation.JEALOUSY -> {
                    state.primaryEmotion = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.INSECURITY)
                    state.secondaryEmotions.add(EmotionalClassificationEngine.EmotionCategory.ANXIETY)
                }
            }
        } else if (detectedCategory != null) {
            // No situation matched, but emotion category matched
            if (state.primaryEmotion == null) {
                state.primaryEmotion = detectedCategory
            } else {
                state.secondaryEmotions.add(state.primaryEmotion!!)
                state.primaryEmotion = detectedCategory
            }
        }

        secondaryCategories.forEach { state.secondaryEmotions.add(it) }

        // 4. Conversational Stage Transition
        state.currentStage = when {
            state.disclosedSituations.contains(UserSituation.EX_SOCIAL_MEDIA_CHECKING) ->
                ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH

            state.disclosedSituations.contains(UserSituation.MISSING_EX) ->
                ConversationalStage.STAGE_4_CONTINUED_CONVERSATION

            state.turnCount >= 5 ->
                ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH

            state.turnCount == 4 ->
                ConversationalStage.STAGE_4_CONTINUED_CONVERSATION

            state.turnCount == 3 ->
                ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT

            state.turnCount == 2 ->
                ConversationalStage.STAGE_2_CONTEXT_DISCOVERY

            else ->
                ConversationalStage.STAGE_1_INITIAL_DISCLOSURE
        }

        return state
    }

    fun recordDelivery(conversationId: String, templateId: String, responseText: String) {
        val state = getOrCreate(conversationId)
        state.deliveredTemplateIds.add(templateId)
        state.deliveredResponses.add(responseText)
    }
}
