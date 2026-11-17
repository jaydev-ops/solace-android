package com.example.data.safety

/**
 * Deterministic Escalation Manager for Solace+.
 * Enforces state machine transitions, safety invariants, multi-turn follow-ups,
 * question progression state machine, and response deduplication.
 */
object EscalationManager {

    /**
     * Primary evaluation method taking a ConversationContext.
     */
    fun evaluate(
        context: ConversationContext
    ): SafetyAssessmentResult {
        return evaluate(
            userMessage = context.messageHistory.lastOrNull { it.first == "user" }?.second ?: "",
            history = context.messageHistory.dropLast(1),
            currentState = context.currentState,
            userName = context.userName,
            country = context.country
        )
    }

    /**
     * Evaluates user message against current risk state and history to perform deterministic escalation.
     */
    fun evaluate(
        userMessage: String,
        history: List<Pair<String, String>> = emptyList(),
        currentState: RiskState = RiskState(),
        userName: String? = null,
        country: String = "IN"
    ): SafetyAssessmentResult {
        // 0. Safety Question State Machine: Check if user is answering a pending safety question
        val pendingQuestion = SafetyConversationManager.identifyPendingQuestion(currentState, history)
        val answer = SafetyConversationManager.interpretAnswer(userMessage, pendingQuestion)

        if (pendingQuestion != null && answer != SafetyAnswer.NONE) {
            val progression = SafetyConversationManager.processQuestionProgression(
                userMessage = userMessage,
                pendingQuestion = pendingQuestion,
                answer = answer,
                currentState = currentState,
                userName = userName,
                country = country
            )

            val updatedAnswers = currentState.questionAnswersMap + (progression.answeredQuestionId.id to progression.answer.value)
            val isExplicitNegativeResolution = (progression.answer == SafetyAnswer.NO && progression.answeredQuestionId == SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS)

            val updatedState = currentState.updateWith(
                newLevel = progression.evaluatedLevel,
                reason = "Answered ${progression.answeredQuestionId.name} with ${progression.answer.value}",
                askedScreening = progression.nextQuestionId != null,
                resolvedConcern = isExplicitNegativeResolution,
                stateOverride = progression.conversationState,
                answeredQuestion = progression.answeredQuestionId.id,
                newPendingQuestionId = progression.nextQuestionId?.id,
                newLastAnsweredQuestionId = progression.answeredQuestionId.id,
                newLastAnswer = progression.answer.value,
                newQuestionAnswersMap = updatedAnswers
            )

            ResponseDeduplicationManager.recordDelivered(
                currentState.conversationId,
                progression.responseText,
                progression.answeredQuestionId.id
            )

            val signals = ContextAnalyzer.extractSignals(userMessage, history, currentState)

            return SafetyAssessmentResult(
                level = progression.evaluatedLevel,
                signals = signals,
                updatedState = updatedState,
                overrideResponse = progression.responseText,
                isEmergency = progression.isEmergency,
                showEmergencyUI = progression.showEmergencyUI,
                showSupportCard = progression.showSupportCard,
                conversationState = progression.conversationState
            )
        }

        // 1. Contextual signal extraction (including Keyword Detection & Confirmations)
        val signals = ContextAnalyzer.extractSignals(userMessage, history, currentState)

        // 2. Deterministic IF-ELSE evaluation
        var evaluatedLevel = RiskDecisionEngine.determineRisk(signals, currentState)

        val lower = userMessage.lowercase().trim()
        val isExplicitPhysicalSafe = lower.contains("i am with my friend now") ||
            lower.contains("i am with a doctor") ||
            lower.contains("at the hospital") ||
            lower.contains("the doctor is here") ||
            signals.confirmationType == UserConfirmationType.SOMEONE_PHYSICALLY_PRESENT

        val isStillUnsafe = lower.contains("still alone and unsafe") ||
            lower.contains("not safe") ||
            lower.contains("still unsafe")

        val isFeelingBetterOnly = (lower == "i'm feeling better now" ||
            lower == "im feeling better now" ||
            lower == "feeling better now" ||
            lower == "i feel better now" ||
            lower == "i'm better now")

        // 3. State-based escalation & safety persistence rules
        // Rule: A Level 4 emergency cannot be casually downgraded by changing the subject or saying something positive
        if (currentState.emergencyTriggered && evaluatedLevel.severity < RiskLevel.LEVEL_4_CRITICAL.severity) {
            if (!isExplicitPhysicalSafe) {
                evaluatedLevel = RiskLevel.LEVEL_4_CRITICAL
            }
        }

        // Rule: If previous state had unresolved safety concern or was in screening
        var hasUnresolvedSafetyConcern = currentState.unresolvedSafetyConcern

        if (isStillUnsafe) {
            hasUnresolvedSafetyConcern = true
            if (evaluatedLevel < RiskLevel.LEVEL_2_ELEVATED) {
                evaluatedLevel = RiskLevel.LEVEL_2_ELEVATED
            }
        }

        if (currentState.unresolvedSafetyConcern) {
            if (signals.negated) {
                evaluatedLevel = if (signals.emotionalDistress || signals.sadness || signals.stress) {
                    RiskLevel.LEVEL_1_DISTRESS
                } else {
                    RiskLevel.LEVEL_0_NORMAL
                }
                hasUnresolvedSafetyConcern = false
            } else if (evaluatedLevel == RiskLevel.LEVEL_0_NORMAL && !isExplicitPhysicalSafe) {
                evaluatedLevel = RiskLevel.LEVEL_2_ELEVATED
            }
        }

        // 4. Handle Safety Follow-up Confirmations (e.g. "I contacted my counsellor")
        var followUpResponse: String? = null
        var stateOverride: ConversationState? = null
        var humanContacted = currentState.humanSupportContacted
        var physicalPresent = currentState.physicalSupportPresent || isExplicitPhysicalSafe
        var safetyConfirmed = currentState.safetyConfirmed

        if (signals.confirmationType != null) {
            when (signals.confirmationType) {
                UserConfirmationType.CONTACTED_PROFESSIONAL,
                UserConfirmationType.CONTACTED_SOMEONE -> {
                    humanContacted = true
                    safetyConfirmed = true
                    stateOverride = ConversationState.HUMAN_SUPPORT_CONTACTED
                    followUpResponse = SafetyConversationManager.generateSafetyFollowUp(
                        confirmationType = signals.confirmationType,
                        userName = userName,
                        country = country,
                        conversationId = currentState.conversationId,
                        state = currentState
                    )
                }
                UserConfirmationType.SOMEONE_PHYSICALLY_PRESENT -> {
                    humanContacted = true
                    physicalPresent = true
                    safetyConfirmed = true
                    stateOverride = ConversationState.NORMAL
                    followUpResponse = SafetyConversationManager.generateSafetyFollowUp(
                        confirmationType = signals.confirmationType,
                        userName = userName,
                        country = country,
                        conversationId = currentState.conversationId,
                        state = currentState
                    )
                }
                UserConfirmationType.DANGER_REDUCED -> {
                    stateOverride = ConversationState.SAFETY_CONFIRMATION_PENDING
                    followUpResponse = SafetyConversationManager.generateSafetyFollowUp(
                        confirmationType = signals.confirmationType,
                        userName = userName,
                        country = country,
                        conversationId = currentState.conversationId,
                        state = currentState
                    )
                }
                UserConfirmationType.REFUSED_ASSISTANCE -> {
                    stateOverride = ConversationState.UNRESOLVED_SAFETY_CONCERN
                    hasUnresolvedSafetyConcern = true
                    followUpResponse = SafetyConversationManager.generateSafetyFollowUp(
                        confirmationType = signals.confirmationType,
                        userName = userName,
                        country = country,
                        conversationId = currentState.conversationId,
                        state = currentState
                    )
                }
                UserConfirmationType.AMBIGUOUS_ANSWER -> {
                    stateOverride = ConversationState.SAFETY_CONFIRMATION_PENDING
                    followUpResponse = SafetyConversationManager.generateSafetyFollowUp(
                        confirmationType = signals.confirmationType,
                        userName = userName,
                        country = country,
                        conversationId = currentState.conversationId,
                        state = currentState
                    )
                }
                else -> {}
            }
        } else if (isFeelingBetterOnly && (currentState.emergencyTriggered || currentState.unresolvedSafetyConcern)) {
            // Never assume user is safe simply because they say they feel better
            followUpResponse = "I'm glad to hear that you're feeling a bit better, but because of what you shared earlier, your well-being remains my priority. Are you somewhere safe right now, with someone who can support you?"
            stateOverride = ConversationState.SAFETY_CONFIRMATION_PENDING
        } else if (isStillUnsafe) {
            val emergencyNum = EmergencyResources.getPrimaryEmergencyNumber(country)
            val teleManas = EmergencyResources.getPrimaryMentalHealthNumber(country)
            followUpResponse = "Thank you for telling me that. Because you are alone and feeling unsafe, please don't stay isolated right now. Can you call Tele-MANAS ($teleManas) or dial $emergencyNum for immediate safety? I want to make sure you have real human support right now."
            stateOverride = ConversationState.UNRESOLVED_SAFETY_CONCERN
        }

        val lastAssistantMessage = history.lastOrNull { 
            it.first.equals("model", ignoreCase = true) || it.first.equals("assistant", ignoreCase = true) 
        }?.second

        val alreadyAskedScreening = lastAssistantMessage != null && (
            lastAssistantMessage.contains("thoughts about hurting yourself", ignoreCase = true) ||
            lastAssistantMessage.contains("thoughts of ending your life", ignoreCase = true)
        )

        val willAskScreening = (evaluatedLevel == RiskLevel.LEVEL_2_ELEVATED || evaluatedLevel == RiskLevel.LEVEL_3_SUICIDAL_IDEATION) &&
            !signals.negated && !alreadyAskedScreening && followUpResponse == null

        val reason = "Evaluated as ${evaluatedLevel.name} based on evidence: ${signals.evidence.joinToString("; ")}"
        val resolvedConcern = (currentState.unresolvedSafetyConcern || currentState.awaitingScreeningAnswer) && signals.negated

        val newPendingQuestion = when {
            willAskScreening -> SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS.id
            else -> currentState.pendingQuestionId
        }

        val updatedState = currentState.updateWith(
            newLevel = evaluatedLevel,
            reason = reason,
            askedScreening = willAskScreening,
            resolvedConcern = resolvedConcern,
            stateOverride = stateOverride,
            newHumanContacted = humanContacted,
            newPhysicalPresent = physicalPresent,
            newSafetyConfirmed = safetyConfirmed,
            newConfirmationType = signals.confirmationType?.name,
            newPendingQuestionId = newPendingQuestion
        ).let { state ->
            if (isStillUnsafe) state.copy(unresolvedSafetyConcern = true) else state
        }

        val isEmergency = evaluatedLevel == RiskLevel.LEVEL_4_CRITICAL
        val showEmergencyUI = isEmergency
        val showSupportCard = evaluatedLevel.isAtLeast(RiskLevel.LEVEL_2_ELEVATED) && !signals.negated && !physicalPresent

        // Generate response for safety levels
        val response = if (followUpResponse != null) {
            followUpResponse
        } else if (evaluatedLevel.isAtLeast(RiskLevel.LEVEL_2_ELEVATED) && !signals.negated && !alreadyAskedScreening) {
            SafetyResponseHandler.generateResponse(
                level = evaluatedLevel,
                signals = signals,
                userName = userName,
                country = country,
                conversationId = currentState.conversationId,
                state = currentState
            )
        } else if (evaluatedLevel == RiskLevel.LEVEL_1_DISTRESS && history.isEmpty()) {
            SafetyResponseHandler.generateResponse(
                level = evaluatedLevel,
                signals = signals,
                userName = userName,
                country = country,
                conversationId = currentState.conversationId,
                state = currentState
            )
        } else {
            null
        }

        if (response != null) {
            ResponseDeduplicationManager.recordDelivered(currentState.conversationId, response)
        }

        return SafetyAssessmentResult(
            level = evaluatedLevel,
            signals = signals,
            updatedState = updatedState,
            overrideResponse = response,
            isEmergency = isEmergency,
            showEmergencyUI = showEmergencyUI,
            showSupportCard = showSupportCard,
            conversationState = updatedState.conversationState
        )
    }
}
