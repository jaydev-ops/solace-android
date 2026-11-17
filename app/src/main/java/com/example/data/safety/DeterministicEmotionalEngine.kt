package com.example.data.safety

import com.example.data.model.RegulationMode
import java.util.ArrayDeque
import java.util.concurrent.ConcurrentHashMap

/**
 * Solace+ Deterministic Emotional Support Engine.
 *
 * Implements context-aware, multi-turn emotional conversations without cloud AI dependencies.
 * Features:
 * - 5-Stage Conversational Progression (Disclosure -> Discovery -> Support -> Continuation -> Follow-through)
 * - Contextual Emotion Transitions (preserves previous emotions as secondary when new triggers are revealed)
 * - Dedicated Situational Context & Trigger Recognition
 * - Intelligent Non-Repetitive Fallback & Repeated-Disclosure Handling
 * - Response Relevance Scoring & Sliding-Window Deduplication
 * - Robust Name Personalization (prevents single-letter/placeholder bugs like "p.")
 * - Complete sovereign offline execution.
 */
data class EngineTurnResult(
    val responseText: String,
    val selectedTemplateId: String,
    val detectedCategory: EmotionalClassificationEngine.EmotionCategory?,
    val appliedMode: RegulationMode,
    val riskLevel: RiskLevel,
    val conversationState: ConversationState,
    val requiresEmergencyUI: Boolean = false,
    val showSupportCard: Boolean = false,
    val pendingQuestionId: SafetyQuestionId? = null,
    val isClarification: Boolean = false
)

object DeterministicEmotionalEngine {

    private val sessionTemplateHistory = ConcurrentHashMap<String, ArrayDeque<String>>()
    private const val SLIDING_WINDOW_SIZE = 10

    /**
     * Resets or clears session memory for a given conversation ID.
     */
    fun clearSession(conversationId: String) {
        val safeId = conversationId.ifBlank { "active_session" }
        sessionTemplateHistory.remove(safeId)
        ConversationalStateTracker.clearSession(safeId)
    }

    /**
     * Retrieves recent template IDs for a session.
     */
    fun getRecentTemplates(conversationId: String): List<String> {
        val safeId = conversationId.ifBlank { "active_session" }
        return sessionTemplateHistory[safeId]?.toList() ?: emptyList()
    }

    /**
     * Processes an incoming user message deterministically with contextual intelligence.
     */
    fun processTurn(
        userMessage: String,
        context: ConversationContext,
        preferredMode: RegulationMode? = null
    ): EngineTurnResult {
        val trimmed = userMessage.trim()
        val convId = context.conversationId.ifBlank { "active_session" }
        val currentState = context.currentState

        // =====================================================================
        // STEP 1: SAFETY SCREENING QUESTION PROGRESSION
        // If an assessment question was awaiting the user's answer, process it first.
        // =====================================================================
        val activePendingQuestion = currentState.pendingQuestionId?.let { qIdStr ->
            try { SafetyQuestionId.valueOf(qIdStr) } catch (_: Exception) { null }
        }

        if (activePendingQuestion != null) {
            val progression = SafetyConversationManager.evaluateAnswer(
                userMessage = trimmed,
                pendingQuestion = activePendingQuestion,
                currentState = currentState,
                userName = context.userName,
                country = context.country
            )

            val updatedRiskLevel = progression.evaluatedLevel
            val updatedState = progression.conversationState
            val nextQ = progression.nextQuestionId

            return EngineTurnResult(
                responseText = progression.responseText,
                selectedTemplateId = "SAFETY_PROG_${activePendingQuestion.name}",
                detectedCategory = EmotionalClassificationEngine.EmotionCategory.SUICIDAL_IDEATION,
                appliedMode = RegulationMode.CALM,
                riskLevel = updatedRiskLevel,
                conversationState = updatedState,
                requiresEmergencyUI = progression.showEmergencyUI,
                showSupportCard = progression.showSupportCard,
                pendingQuestionId = nextQ
            )
        }

        // =====================================================================
        // STEP 2: DETERMINISTIC SITUATION & EMOTION CLASSIFICATION
        // =====================================================================
        val situationResult = SituationalContextEngine.detectSituation(trimmed)
        val classification = EmotionalClassificationEngine.classify(
            message = trimmed,
            history = context.messageHistory
        )

        // =====================================================================
        // STEP 3: CRISIS SAFETY ENGINE (LEVELS 2, 3, 4 PRIORITY OVERRIDE)
        // =====================================================================
        val detectedCat = classification.primaryCategory

        if (detectedCat == EmotionalClassificationEngine.EmotionCategory.IMMEDIATE_PHYSICAL_DANGER) {
            val response = EmotionalResponseLibrary.crisisResponses[4]?.firstOrNull() ?:
                "Please stay with me. If you are in immediate danger, call 112 (India) or 988 immediately."
            return EngineTurnResult(
                responseText = response,
                selectedTemplateId = "CRISIS_LVL_4",
                detectedCategory = detectedCat,
                appliedMode = RegulationMode.CALM,
                riskLevel = RiskLevel.LEVEL_4_CRITICAL,
                conversationState = ConversationState.EMERGENCY_WORKFLOW_ACTIVE,
                requiresEmergencyUI = true,
                showSupportCard = true,
                pendingQuestionId = null
            )
        }

        if (detectedCat == EmotionalClassificationEngine.EmotionCategory.SUICIDAL_IDEATION ||
            detectedCat == EmotionalClassificationEngine.EmotionCategory.SELF_HARM) {
            val firstQuestion = SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS
            val intro = "I hear how much pain you are carrying right now, and I want you to know you are not alone in this sanctuary. Because your life and safety are precious, may I ask gently:\n\n${firstQuestion.questionText}"
            return EngineTurnResult(
                responseText = intro,
                selectedTemplateId = "CRISIS_LVL_3_TRIGGER",
                detectedCategory = detectedCat,
                appliedMode = RegulationMode.CALM,
                riskLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                conversationState = ConversationState.SAFETY_SCREENING,
                requiresEmergencyUI = true,
                showSupportCard = true,
                pendingQuestionId = firstQuestion
            )
        }

        // Active Crisis Topic-Lock Check: If previous turn was in unresolved crisis, don't allow casual topic switch
        if (currentState.unresolvedSafetyConcern && currentState.currentLevel.isAtLeast(RiskLevel.LEVEL_3_SUICIDAL_IDEATION)) {
            val lockResponse = "I hear you, and we can definitely talk about that, but first I need to make sure you are safe and supported right now. Are you safe in this moment?"
            return EngineTurnResult(
                responseText = lockResponse,
                selectedTemplateId = "CRISIS_TOPIC_LOCK",
                detectedCategory = detectedCat,
                appliedMode = RegulationMode.CALM,
                riskLevel = currentState.currentLevel,
                conversationState = ConversationState.UNRESOLVED_SAFETY_CONCERN,
                requiresEmergencyUI = true,
                showSupportCard = true,
                pendingQuestionId = SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS
            )
        }

        // =====================================================================
        // STEP 4: MULTI-TURN CONVERSATION STATE & EMOTION TRANSITION
        // =====================================================================
        val stateRecord = ConversationalStateTracker.updateState(
            conversationId = convId,
            userMessage = trimmed,
            detectedCategory = detectedCat,
            secondaryCategories = classification.secondaryCategories,
            detectedSituation = situationResult.primarySituation
        )

        // =====================================================================
        // STEP 5: REPEATED MESSAGE / REPEATED DISCLOSURE HANDLING
        // =====================================================================
        if (stateRecord.consecutiveIdenticalMessageCount == 1 && stateRecord.lastDisclosedSituation == UserSituation.BREAKUP) {
            val repeatResponse = "I hear you. Going through a breakup is clearly weighing on you. You don't have to keep explaining yourself for your feelings to matter."
            recordTemplateUsed(convId, "REPEAT_DISCLOSURE_BREAKUP")
            ConversationalStateTracker.recordDelivery(convId, "REPEAT_DISCLOSURE_BREAKUP", repeatResponse)
            return EngineTurnResult(
                responseText = repeatResponse,
                selectedTemplateId = "REPEAT_DISCLOSURE_BREAKUP",
                detectedCategory = stateRecord.primaryEmotion,
                appliedMode = RegulationMode.LISTEN,
                riskLevel = RiskLevel.LEVEL_0_NORMAL,
                conversationState = ConversationState.NORMAL,
                requiresEmergencyUI = false,
                showSupportCard = false,
                pendingQuestionId = null
            )
        }

        // =====================================================================
        // STEP 6: CATEGORY NORMALIZATION & SITUATIONAL MAPPING
        // Map fine-grained categories to response pools so no category is orphaned
        // =====================================================================
        val effectiveCategory = when (stateRecord.primaryEmotion) {
            EmotionalClassificationEngine.EmotionCategory.HEARTBREAK,
            EmotionalClassificationEngine.EmotionCategory.REJECTION ->
                EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS

            EmotionalClassificationEngine.EmotionCategory.PANIC ->
                EmotionalClassificationEngine.EmotionCategory.ANXIETY

            EmotionalClassificationEngine.EmotionCategory.WORK_RELATED_STRESS ->
                EmotionalClassificationEngine.EmotionCategory.BURNOUT

            EmotionalClassificationEngine.EmotionCategory.DEPRESSION_DISTRESS,
            EmotionalClassificationEngine.EmotionCategory.EMOTIONAL_EXHAUSTION ->
                EmotionalClassificationEngine.EmotionCategory.SADNESS

            EmotionalClassificationEngine.EmotionCategory.FRUSTRATION ->
                EmotionalClassificationEngine.EmotionCategory.ANGER

            EmotionalClassificationEngine.EmotionCategory.INSECURITY ->
                EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM

            EmotionalClassificationEngine.EmotionCategory.SHAME ->
                EmotionalClassificationEngine.EmotionCategory.GUILT

            EmotionalClassificationEngine.EmotionCategory.PROCRASTINATION ->
                EmotionalClassificationEngine.EmotionCategory.LACK_OF_MOTIVATION

            else -> stateRecord.primaryEmotion
        }

        // =====================================================================
        // STEP 7: LOW-CONFIDENCE CLARIFICATION CHECK (NON-REPETITIVE ROTATION)
        // =====================================================================
        if (effectiveCategory == null && situationResult.primarySituation == null) {
            val recentClarifications = sessionTemplateHistory[convId]?.toSet() ?: emptySet()
            val candidate = EmotionalResponseLibrary.clarificationInquiries.firstOrNull {
                !recentClarifications.contains(it.hashCode().toString())
            } ?: EmotionalResponseLibrary.clarificationInquiries.first()

            recordTemplateUsed(convId, candidate.hashCode().toString())
            ConversationalStateTracker.recordDelivery(convId, "CLARIFICATION", candidate)

            return EngineTurnResult(
                responseText = candidate,
                selectedTemplateId = "CLARIFICATION",
                detectedCategory = null,
                appliedMode = preferredMode ?: RegulationMode.LISTEN,
                riskLevel = RiskLevel.LEVEL_0_NORMAL,
                conversationState = ConversationState.NORMAL,
                requiresEmergencyUI = false,
                showSupportCard = false,
                pendingQuestionId = null,
                isClarification = true
            )
        }

        // =====================================================================
        // STEP 8: CONVERSATIONAL MODE RESOLUTION (LISTEN, UNDERSTAND, REFLECT, CALM)
        // =====================================================================
        val resolvedMode = preferredMode ?: when (effectiveCategory) {
            EmotionalClassificationEngine.EmotionCategory.PANIC,
            EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            EmotionalClassificationEngine.EmotionCategory.SLEEP_DIFFICULTIES -> RegulationMode.CALM

            EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            EmotionalClassificationEngine.EmotionCategory.WORK_RELATED_STRESS,
            EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            EmotionalClassificationEngine.EmotionCategory.HEARTBREAK,
            EmotionalClassificationEngine.EmotionCategory.REJECTION,
            EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            EmotionalClassificationEngine.EmotionCategory.ANGER,
            EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            EmotionalClassificationEngine.EmotionCategory.LACK_OF_MOTIVATION,
            EmotionalClassificationEngine.EmotionCategory.PROCRASTINATION -> RegulationMode.UNDERSTAND

            EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            EmotionalClassificationEngine.EmotionCategory.GUILT,
            EmotionalClassificationEngine.EmotionCategory.SHAME,
            EmotionalClassificationEngine.EmotionCategory.FEAR,
            EmotionalClassificationEngine.EmotionCategory.EMOTIONAL_NUMBNESS -> RegulationMode.REFLECT

            EmotionalClassificationEngine.EmotionCategory.SADNESS,
            EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            EmotionalClassificationEngine.EmotionCategory.GRIEF,
            EmotionalClassificationEngine.EmotionCategory.DEPRESSION_DISTRESS,
            EmotionalClassificationEngine.EmotionCategory.EMOTIONAL_EXHAUSTION,
            EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS -> RegulationMode.LISTEN

            else -> RegulationMode.LISTEN
        }

        // =====================================================================
        // STEP 9: RESPONSE RELEVANCE SCORING & SELECTION
        // =====================================================================
        val recentTemplates = sessionTemplateHistory.getOrPut(convId) { ArrayDeque() }

        // A. Prefer situation-specific templates if situation is recognized
        val currentSituation = situationResult.primarySituation ?: stateRecord.lastDisclosedSituation
        var situationCandidates = if (currentSituation != null) {
            EmotionalResponseLibrary.templates.filter { it.situation == currentSituation }
        } else {
            emptyList()
        }

        // B. If situation candidates found, filter by unused in sliding window
        val selectedTemplate: EmotionalResponseLibrary.ResponseTemplate = if (situationCandidates.isNotEmpty()) {
            val unusedSituational = situationCandidates.filter { !recentTemplates.contains(it.id) }
            val sitPool = if (unusedSituational.isNotEmpty()) unusedSituational else situationCandidates
            val scoredSit = sitPool.map { template ->
                var score = 0
                if (template.preferredMode == resolvedMode) score += 10
                if (classification.intensity == EmotionalClassificationEngine.EmotionIntensity.HIGH && template.suggestedGrounding) score += 5
                if (template.stage == stateRecord.currentStage) score += 15
                template to score
            }.sortedByDescending { it.second }
            scoredSit.first().first
        } else {
            // C. Fall back to emotion category templates (excluding situation-specific templates)
            var candidates = EmotionalResponseLibrary.templates.filter { it.category == effectiveCategory && it.situation == null }
            if (candidates.isEmpty()) {
                candidates = EmotionalResponseLibrary.templates.filter {
                    (classification.secondaryCategories.contains(it.category) ||
                        stateRecord.secondaryEmotions.contains(it.category)) && it.situation == null
                }
            }
            if (candidates.isEmpty()) {
                candidates = EmotionalResponseLibrary.templates.filter {
                    it.category == EmotionalClassificationEngine.EmotionCategory.CONFUSION && it.situation == null
                }
            }

            // Exclude templates used in the last 10 turns
            val unusedCandidates = candidates.filter { !recentTemplates.contains(it.id) }
            val pool = if (unusedCandidates.isNotEmpty()) unusedCandidates else candidates

            // Score candidates based on mode alignment, stage, and intensity
            val scored = pool.map { template ->
                var score = 0
                if (template.preferredMode == resolvedMode) score += 10
                if (classification.intensity == EmotionalClassificationEngine.EmotionIntensity.HIGH && template.suggestedGrounding) score += 5
                if (template.stage == stateRecord.currentStage) score += 15
                template to score
            }.sortedByDescending { it.second }

            scored.first().first
        }

        // Record template in sliding window and state tracker
        recordTemplateUsed(convId, selectedTemplate.id)
        ConversationalStateTracker.recordDelivery(convId, selectedTemplate.id, selectedTemplate.text)

        // =====================================================================
        // STEP 10: SAFE PERSONALIZATION (Prevents single-letter bugs like "p.")
        // =====================================================================
        var finalReply = selectedTemplate.text
        val rawName = context.userName?.trim()
        val isValidHumanName = !rawName.isNullOrBlank() &&
            rawName.length >= 2 &&
            rawName.all { it.isLetter() || it.isWhitespace() } &&
            !rawName.equals("Sanctuary", ignoreCase = true) &&
            !rawName.equals("User", ignoreCase = true) &&
            !rawName.equals("Guest", ignoreCase = true) &&
            !rawName.equals("Sanctuary Guest", ignoreCase = true) &&
            !rawName.equals("Anonymous", ignoreCase = true)

        if (isValidHumanName && !finalReply.contains(rawName!!)) {
            // Only format at natural intro points, never through raw substring slicing
            if (finalReply.startsWith("Hello", ignoreCase = true) || finalReply.startsWith("Hi", ignoreCase = true)) {
                val firstWordEnd = finalReply.indexOf(' ')
                if (firstWordEnd != -1) {
                    finalReply = finalReply.substring(0, firstWordEnd) + ", $rawName" + finalReply.substring(firstWordEnd)
                }
            }
        }

        val riskLevel = when {
            classification.intensity == EmotionalClassificationEngine.EmotionIntensity.HIGH -> RiskLevel.LEVEL_1_DISTRESS
            else -> RiskLevel.LEVEL_0_NORMAL
        }

        return EngineTurnResult(
            responseText = finalReply,
            selectedTemplateId = selectedTemplate.id,
            detectedCategory = effectiveCategory,
            appliedMode = resolvedMode,
            riskLevel = riskLevel,
            conversationState = ConversationState.NORMAL,
            requiresEmergencyUI = false,
            showSupportCard = false,
            pendingQuestionId = null
        )
    }

    private fun recordTemplateUsed(convId: String, templateId: String) {
        val deque = sessionTemplateHistory.getOrPut(convId) { ArrayDeque() }
        deque.addLast(templateId)
        while (deque.size > SLIDING_WINDOW_SIZE) {
            deque.removeFirst()
        }
    }
}
