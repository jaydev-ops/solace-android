package com.example.data.safety

import java.util.Locale

/**
 * Manages the multi-turn Emergency Conversation Workflow, deterministic question progression state machine,
 * professional help recommendations, and rigorous safety confirmation verification.
 *
 * Core invariants:
 * 1. Every answered question is stored and never repeated.
 * 2. The AI always acknowledges the user's latest answer before presenting the next assessment.
 * 3. Never confuse displaying a helpline with actually helping someone reach support.
 */
object SafetyConversationManager {

    data class QuestionProgressionResult(
        val answeredQuestionId: SafetyQuestionId,
        val answer: SafetyAnswer,
        val nextQuestionId: SafetyQuestionId?,
        val evaluatedLevel: RiskLevel,
        val conversationState: ConversationState,
        val responseText: String,
        val showSupportCard: Boolean,
        val showEmergencyUI: Boolean,
        val isEmergency: Boolean
    )

    private val explicitYesIdeationRegex = Regex(
        """\b((yes\s*[,.]?\s*)?(i['\s]*m|i\s+am|im)\s+having\s+thoughts\s+of\s+ending\s+my\s+life|yes\s*[,.]?\s*(i\s+do|i\s+have|i\s+am|i\s+want\s+to\s+die|i\s+want\s+to\s+kill\s+myself))\b""",
        RegexOption.IGNORE_CASE
    )

    private val safeDenialRegex = Regex(
        """\b(no|nope|nah|never|not\s+really|not\s+at\s+all|no\s+way|nothing\s+like\s+that|not\s+right\s+now|""" +
        """i\s+do\s*n['o]t\s+think\s+so|dont\s+think\s+so|i\s+am\s+not|i['\s]*m\s+not|im\s+not|not\s+me|no\s+i\s+do\s*n['o]t|""" +
        """definitely\s+not|absolutely\s+not|no\s+thoughts?|never\s+had\s+thoughts?|not\s+having\s+thoughts?|""" +
        """not\s+suicidal|no\s+suicidal\s+thoughts?|not\s+trying\s+to\s+hurt\s+myself|not\s+hurting\s+myself|""" +
        """don'?t\s+want\s+to\s+die|do\s+not\s+want\s+to\s+die|""" +
        """(i['\s]*m|i\s+am|im|i\s+m)\s+(good|fine|okay|ok|safe|better|alright|all\s+right|doing\s+well|doing\s+good|doing\s+fine|doing\s+ok|doing\s+okay|feeling\s+good|feeling\s+fine|feeling\s+ok|feeling\s+okay|feeling\s+better|happy|chilling|great|peaceful)|""" +
        """(doing\s+(good|fine|well|ok|okay)|all\s+(good|fine|okay|ok|right|alright)|feeling\s+(good|fine|ok|okay|better|great))|""" +
        """(just\s+overwhelmed|overwhelmed|describing\s+how\s+overwhelmed|just\s+stressed|just\s+tired|just\s+exhausted|feeling\s+overwhelmed|so\s+overwhelmed)|""" +
        """^(good|fine|okay|ok|safe|better|great|alright|all\s+right|chilling|doing\s+good|doing\s+well|doing\s+fine|all\s+good)$)\b""",
        RegexOption.IGNORE_CASE
    )

    private val affirmativeYesRegex = Regex(
        """\b(yes|yeah|yep|yup|i\s+do|i\s+have|sure|definitely|absolutely|honestly\s+yes|frequently|all\s+the\s+time|sometimes\s+i\s+do)\b""",
        RegexOption.IGNORE_CASE
    )

    private val ambiguousRegex = Regex(
        """\b(maybe|perhaps|i\s+do\s*n['o]t\s+know|dont\s+know|not\s+sure|it'?s\s+complicated|its\s+complicated|i\s+can'?t\s+explain|cant\s+explain|hard\s+to\s+say|somewhat|kinda|kind\s+of)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Identifies which safety screening question is currently pending in the conversation.
     */
    fun identifyPendingQuestion(
        currentState: RiskState,
        history: List<Pair<String, String>>
    ): SafetyQuestionId? {
        if (!currentState.pendingQuestionId.isNullOrBlank()) {
            return try {
                SafetyQuestionId.valueOf(currentState.pendingQuestionId)
            } catch (_: Exception) {
                null
            }
        }

        // Infer from last assistant message in history if not recorded in state
        val lastAssistant = history.lastOrNull {
            it.first.equals("model", ignoreCase = true) || it.first.equals("assistant", ignoreCase = true)
        }?.second ?: return null

        return when {
            lastAssistant.contains("thoughts of ending your life", ignoreCase = true) ||
            lastAssistant.contains("thoughts about hurting yourself", ignoreCase = true) ||
            lastAssistant.contains("thoughts about ending your life", ignoreCase = true) -> SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS

            lastAssistant.contains("acting on these thoughts", ignoreCase = true) ||
            lastAssistant.contains("thought about acting", ignoreCase = true) -> SafetyQuestionId.IMMEDIATE_INTENT

            lastAssistant.contains("how or when", ignoreCase = true) ||
            lastAssistant.contains("timeline", ignoreCase = true) ||
            lastAssistant.contains("specific plan", ignoreCase = true) -> SafetyQuestionId.PLAN_AND_TIMEFRAME

            lastAssistant.contains("access to anything", ignoreCase = true) ||
            lastAssistant.contains("means near you", ignoreCase = true) -> SafetyQuestionId.ACCESS_TO_MEANS

            lastAssistant.contains("already taken any steps", ignoreCase = true) ||
            lastAssistant.contains("already hurt yourself", ignoreCase = true) -> SafetyQuestionId.ACTION_ALREADY_TAKEN

            lastAssistant.contains("someone you trust who could come and stay", ignoreCase = true) ||
            lastAssistant.contains("someone who can be with you", ignoreCase = true) -> SafetyQuestionId.HUMAN_SUPPORT

            else -> null
        }
    }

    /**
     * Interprets user answer to the active safety question.
     */
    fun interpretAnswer(
        userMessage: String,
        pendingQuestion: SafetyQuestionId?
    ): SafetyAnswer {
        if (pendingQuestion == null) return SafetyAnswer.NONE

        val trimmed = userMessage.trim()
        val lower = trimmed.lowercase(Locale.ROOT)
        // Normalize punctuation so "i m. good", "good.", "im good!", "no, i am fine" match cleanly
        val normalized = lower
            .replace(Regex("""[.,!?;:"'’\-–—_/\\#@$%^&*()+=\[\]{}<>|~`]+"""), " ")
            .replace(Regex("""\s+"""), " ")
            .trim()

        // 1. Explicit affirmative crisis statements always take priority (e.g. "Yes, I'm having thoughts of ending my life.")
        if (explicitYesIdeationRegex.containsMatchIn(lower) || explicitYesIdeationRegex.containsMatchIn(normalized)) {
            return SafetyAnswer.YES
        }

        // 2. Clear denial / safe confirmation ("i m. good", "i am fine", "no", "just overwhelmed", "doing ok")
        if (safeDenialRegex.containsMatchIn(lower) || safeDenialRegex.containsMatchIn(normalized)) {
            // Guard: don't classify as NO if user explicitly stated suicidal ideation
            val containsSuicidalIntent = lower.contains("want to die") ||
                lower.contains("kill myself") ||
                lower.contains("end my life") ||
                lower.contains("having thoughts of ending")
            if (!containsSuicidalIntent) {
                return SafetyAnswer.NO
            }
        }

        // 3. Affirmative response to safety question (e.g. "yes", "yeah", "i do", "i have")
        if (affirmativeYesRegex.containsMatchIn(lower) || affirmativeYesRegex.containsMatchIn(normalized)) {
            return SafetyAnswer.YES
        }

        // 4. Ambiguous response
        if (ambiguousRegex.containsMatchIn(lower) || ambiguousRegex.containsMatchIn(normalized)) {
            return SafetyAnswer.AMBIGUOUS
        }

        return SafetyAnswer.NONE
    }

    /**
     * Evaluates user answer and advances the safety progression state machine.
     */
    fun evaluateAnswer(
        userMessage: String,
        pendingQuestion: SafetyQuestionId,
        currentState: RiskState,
        userName: String? = null,
        country: String = "IN"
    ): QuestionProgressionResult {
        val answer = interpretAnswer(userMessage, pendingQuestion)
        return processQuestionProgression(userMessage, pendingQuestion, answer, currentState, userName, country)
    }

    /**
     * Executes deterministic safety question state progression.
     * Always acknowledges the user's latest response before presenting the next question.
     */
    fun processQuestionProgression(
        userMessage: String,
        pendingQuestion: SafetyQuestionId,
        answer: SafetyAnswer,
        currentState: RiskState,
        userName: String? = null,
        country: String = "IN"
    ): QuestionProgressionResult {
        val cleanName = userName?.trim()
        val hasName = !cleanName.isNullOrBlank() &&
            !cleanName.equals("Sanctuary", true) &&
            !cleanName.equals("User", true) &&
            !cleanName.equals("Guest", true)
        val nameComma = if (hasName) ", $cleanName" else ""

        val emergencyNum = EmergencyResources.getPrimaryEmergencyNumber(country)
        val teleManas = EmergencyResources.getPrimaryMentalHealthNumber(country)

        return when (pendingQuestion) {
            SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS -> {
                when (answer) {
                    SafetyAnswer.YES -> {
                        val response = "Thank you for trusting me enough to tell me$nameComma. I'm taking this seriously, and I want to make sure you aren't facing this alone. Have you thought about acting on these thoughts?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS,
                            answer = SafetyAnswer.YES,
                            nextQuestionId = SafetyQuestionId.IMMEDIATE_INTENT,
                            evaluatedLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                            conversationState = ConversationState.SAFETY_ASSESSMENT,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                    SafetyAnswer.NO -> {
                        val response = "I'm so relieved and glad to hear you're doing okay$nameComma. Thank you for being open and letting me know.\n\nI'm right here in your sanctuary whenever you need a listening ear or a quiet pause. What would you like to talk about today?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS,
                            answer = SafetyAnswer.NO,
                            nextQuestionId = null,
                            evaluatedLevel = RiskLevel.LEVEL_0_NORMAL,
                            conversationState = ConversationState.NORMAL,
                            responseText = response,
                            showSupportCard = false,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                    SafetyAnswer.AMBIGUOUS, SafetyAnswer.NONE -> {
                        val response = "I hear that it feels complicated to put into words$nameComma. Because your safety matters so much, when you say that, are you having thoughts of ending your life right now, or are you describing how overwhelmed you feel?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS,
                            answer = SafetyAnswer.AMBIGUOUS,
                            nextQuestionId = SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS,
                            evaluatedLevel = RiskLevel.LEVEL_2_ELEVATED,
                            conversationState = ConversationState.SAFETY_SCREENING,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                }
            }

            SafetyQuestionId.IMMEDIATE_INTENT -> {
                when (answer) {
                    SafetyAnswer.YES -> {
                        val response = """
                            Because you are thinking about acting on these thoughts$nameComma, your safety is in immediate danger. Please do not stay alone with this.

                            Please call emergency services at $emergencyNum or contact Tele-MANAS immediately at $teleManas. Move away from anything you could use to hurt yourself, and reach out to someone trusted who can physically stay with you.

                            Can you contact someone who can be with you right now?
                        """.trimIndent()
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.IMMEDIATE_INTENT,
                            answer = SafetyAnswer.YES,
                            nextQuestionId = null,
                            evaluatedLevel = RiskLevel.LEVEL_4_CRITICAL,
                            conversationState = ConversationState.EMERGENCY_RESPONSE,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = true,
                            isEmergency = true
                        )
                    }
                    SafetyAnswer.NO -> {
                        val response = "I am so relieved to hear that you haven't decided to act on them$nameComma. Have you thought about how or when you might act on these thoughts?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.IMMEDIATE_INTENT,
                            answer = SafetyAnswer.NO,
                            nextQuestionId = SafetyQuestionId.PLAN_AND_TIMEFRAME,
                            evaluatedLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                            conversationState = ConversationState.SAFETY_ASSESSMENT,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                    SafetyAnswer.AMBIGUOUS, SafetyAnswer.NONE -> {
                        val response = "I want to take what you're telling me seriously$nameComma. Are there moments where it feels like you might act on these thoughts?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.IMMEDIATE_INTENT,
                            answer = SafetyAnswer.AMBIGUOUS,
                            nextQuestionId = SafetyQuestionId.IMMEDIATE_INTENT,
                            evaluatedLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                            conversationState = ConversationState.SAFETY_ASSESSMENT,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                }
            }

            SafetyQuestionId.PLAN_AND_TIMEFRAME -> {
                when (answer) {
                    SafetyAnswer.YES -> {
                        val response = """
                            Having a plan means you are in immediate danger$nameComma, and I want to help you stay safe right now.

                            Please contact emergency services ($emergencyNum) or Tele-MANAS ($teleManas) immediately. Step away from any dangerous setting, and call someone close to you who can be with you physically.

                            Is there someone who can stay with you right now?
                        """.trimIndent()
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.PLAN_AND_TIMEFRAME,
                            answer = SafetyAnswer.YES,
                            nextQuestionId = null,
                            evaluatedLevel = RiskLevel.LEVEL_4_CRITICAL,
                            conversationState = ConversationState.EMERGENCY_RESPONSE,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = true,
                            isEmergency = true
                        )
                    }
                    SafetyAnswer.NO -> {
                        val response = "Thank you for being open with me$nameComma. It is a comfort to know there is no specific plan. Do you have access to anything around you that you might use to hurt yourself?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.PLAN_AND_TIMEFRAME,
                            answer = SafetyAnswer.NO,
                            nextQuestionId = SafetyQuestionId.ACCESS_TO_MEANS,
                            evaluatedLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                            conversationState = ConversationState.SAFETY_ASSESSMENT,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                    SafetyAnswer.AMBIGUOUS, SafetyAnswer.NONE -> {
                        val response = "Have you started thinking about when or how you might do it$nameComma?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.PLAN_AND_TIMEFRAME,
                            answer = SafetyAnswer.AMBIGUOUS,
                            nextQuestionId = SafetyQuestionId.PLAN_AND_TIMEFRAME,
                            evaluatedLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                            conversationState = ConversationState.SAFETY_ASSESSMENT,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                }
            }

            SafetyQuestionId.ACCESS_TO_MEANS -> {
                when (answer) {
                    SafetyAnswer.YES -> {
                        val response = """
                            Please move away from anything you could use to harm yourself right now$nameComma. Your safety is the priority.

                            Please call emergency services at $emergencyNum or reach Tele-MANAS at $teleManas. Reach out to a loved one, neighbour or friend to be with you immediately.
                        """.trimIndent()
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.ACCESS_TO_MEANS,
                            answer = SafetyAnswer.YES,
                            nextQuestionId = null,
                            evaluatedLevel = RiskLevel.LEVEL_4_CRITICAL,
                            conversationState = ConversationState.EMERGENCY_RESPONSE,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = true,
                            isEmergency = true
                        )
                    }
                    SafetyAnswer.NO -> {
                        val response = "I'm glad you don't have access to anything unsafe$nameComma. Before we talk further, have you already taken any steps to hurt yourself today?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.ACCESS_TO_MEANS,
                            answer = SafetyAnswer.NO,
                            nextQuestionId = SafetyQuestionId.ACTION_ALREADY_TAKEN,
                            evaluatedLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                            conversationState = ConversationState.SAFETY_ASSESSMENT,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                    SafetyAnswer.AMBIGUOUS, SafetyAnswer.NONE -> {
                        val response = "Are there any items or means near you that make you feel unsafe$nameComma?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.ACCESS_TO_MEANS,
                            answer = SafetyAnswer.AMBIGUOUS,
                            nextQuestionId = SafetyQuestionId.ACCESS_TO_MEANS,
                            evaluatedLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                            conversationState = ConversationState.SAFETY_ASSESSMENT,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                }
            }

            SafetyQuestionId.ACTION_ALREADY_TAKEN -> {
                when (answer) {
                    SafetyAnswer.YES -> {
                        val response = """
                            If you have already taken steps to harm yourself$nameComma, this is an immediate medical emergency.

                            Please call emergency services ($emergencyNum) or go to the nearest emergency department right now. Please tell someone nearby immediately.
                        """.trimIndent()
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.ACTION_ALREADY_TAKEN,
                            answer = SafetyAnswer.YES,
                            nextQuestionId = null,
                            evaluatedLevel = RiskLevel.LEVEL_4_CRITICAL,
                            conversationState = ConversationState.EMERGENCY_RESPONSE,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = true,
                            isEmergency = true
                        )
                    }
                    SafetyAnswer.NO -> {
                        val response = "I am so glad you haven't taken any steps to hurt yourself$nameComma. You don't have to carry this heavy weight alone. Is there someone you trust who could come and stay with you right now?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.ACTION_ALREADY_TAKEN,
                            answer = SafetyAnswer.NO,
                            nextQuestionId = SafetyQuestionId.HUMAN_SUPPORT,
                            evaluatedLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                            conversationState = ConversationState.SAFETY_ASSESSMENT,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                    SafetyAnswer.AMBIGUOUS, SafetyAnswer.NONE -> {
                        val response = "Have you taken any pills, injured yourself, or done anything towards that plan today$nameComma?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.ACTION_ALREADY_TAKEN,
                            answer = SafetyAnswer.AMBIGUOUS,
                            nextQuestionId = SafetyQuestionId.ACTION_ALREADY_TAKEN,
                            evaluatedLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                            conversationState = ConversationState.SAFETY_ASSESSMENT,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                }
            }

            SafetyQuestionId.HUMAN_SUPPORT -> {
                when (answer) {
                    SafetyAnswer.YES -> {
                        val response = "That is a huge relief to hear$nameComma. Having someone physically with you makes an immense difference. Can you reach out to them now and ask them to stay close?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.HUMAN_SUPPORT,
                            answer = SafetyAnswer.YES,
                            nextQuestionId = null,
                            evaluatedLevel = RiskLevel.LEVEL_2_ELEVATED,
                            conversationState = ConversationState.HUMAN_SUPPORT_CONTACTED,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                    SafetyAnswer.NO, SafetyAnswer.AMBIGUOUS, SafetyAnswer.NONE -> {
                        val response = "If no one is able to be there physically right now$nameComma, please remember Tele-MANAS is free and confidential 24/7 at $teleManas. You can speak to someone compassionate who understands what you're going through. Would you be open to calling them?"
                        QuestionProgressionResult(
                            answeredQuestionId = SafetyQuestionId.HUMAN_SUPPORT,
                            answer = answer,
                            nextQuestionId = null,
                            evaluatedLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
                            conversationState = ConversationState.HELP_RECOMMENDED,
                            responseText = response,
                            showSupportCard = true,
                            showEmergencyUI = false,
                            isEmergency = false
                        )
                    }
                }
            }
        }
    }

    /**
     * Determines whether the user's message is a confirmation of reaching human support
     * or refusal/ambiguity during a safety follow-up.
     */
    fun analyzeSafetyConfirmation(
        userMessage: String,
        state: RiskState
    ): UserConfirmationType? {
        val lower = userMessage.lowercase(Locale.ROOT).trim()

        val physicalPresenceRegex = Regex(
            """\b(with\s+(my\s+)?(friend|mom|dad|family|doctor|counsellor|partner|brother|sister|roommate)|doctor\s+is\s+here|hospital|emergency\s+room|someone\s+is\s+(with\s+me|here\s+with\s+me)|not\s+alone\s+anymore)\b"""
        )
        if (physicalPresenceRegex.containsMatchIn(lower)) {
            return UserConfirmationType.SOMEONE_PHYSICALLY_PRESENT
        }

        val professionalContactRegex = Regex(
            """\b(called\s+(the\s+)?(helpline|tele[- ]manas|14416|112|988|counsellor|therapist|doctor)|speaking\s+with\s+(a\s+)?(counsellor|doctor|therapist)|contacted\s+(my\s+)?(counsellor|therapist|psychiatrist|doctor|helpline))\b"""
        )
        if (professionalContactRegex.containsMatchIn(lower)) {
            return UserConfirmationType.CONTACTED_PROFESSIONAL
        }

        val contactedSomeoneRegex = Regex(
            """\b(called\s+(my\s+)?(friend|mom|dad|sister|brother|partner|someone)|talked\s+to\s+someone|spoke\s+to\s+someone|reached\s+out\s+to\s+(someone|my\s+friend|my\s+family))\b"""
        )
        if (contactedSomeoneRegex.containsMatchIn(lower)) {
            return UserConfirmationType.CONTACTED_SOMEONE
        }

        val safeNowRegex = Regex(
            """\b(safe\s+right\s+now|danger\s+has\s+passed|threw\s+away\s+the\s+pills|away\s+from\s+danger|feeling\s+safe\s+now)\b"""
        )
        if (safeNowRegex.containsMatchIn(lower)) {
            return UserConfirmationType.DANGER_REDUCED
        }

        val refusalRegex = Regex(
            """\b(wo\s*n['o]t\s+call|don['o]t\s+want\s+to\s+call|refuse\s+to|do\s*n['o]t\s+want\s+(help|to\s+talk\s+to\s+anyone)|leave\s+me\s+alone|no\s+help)\b"""
        )
        if (refusalRegex.containsMatchIn(lower)) {
            return UserConfirmationType.REFUSED_ASSISTANCE
        }

        val ambiguousRegex = Regex(
            """\b(maybe|i\s+guess|not\s+sure|perhaps|i\s+dunno|don['o]t\s+know)\b"""
        )
        if (ambiguousRegex.containsMatchIn(lower) && (state.helpContactPending || state.unresolvedSafetyConcern)) {
            return UserConfirmationType.AMBIGUOUS_ANSWER
        }

        return null
    }

    /**
     * Generates safety follow-up questions to ensure the user actually connected with human help.
     */
    fun generateSafetyFollowUp(
        confirmationType: UserConfirmationType?,
        userName: String? = null,
        country: String = "IN",
        conversationId: String = "active_session",
        state: RiskState? = null
    ): String {
        val cleanName = userName?.trim()
        val hasName = !cleanName.isNullOrBlank() && !cleanName.equals("Sanctuary", true)
        val nameComma = if (hasName) ", $cleanName" else ""
        val helpline = EmergencyResources.getPrimaryMentalHealthNumber(country)
        val emergencyNumber = EmergencyResources.getPrimaryEmergencyNumber(country)

        return when (confirmationType) {
            UserConfirmationType.CONTACTED_SOMEONE,
            UserConfirmationType.CONTACTED_PROFESSIONAL -> {
                val options = listOf(
                    "I'm so glad and relieved that you were able to reach out$nameComma. Are you somewhere safe right now, with someone who can support you physically?",
                    "Thank you so much for taking that brave step$nameComma. That takes real courage. Are you in a safe physical space at this moment?",
                    "It's a genuine relief to hear you connected with someone$nameComma. Is somebody with you right now, or are you in a safe place?"
                )
                ResponseDeduplicationManager.selectVariant(conversationId, options, state)
            }
            UserConfirmationType.SOMEONE_PHYSICALLY_PRESENT -> {
                val options = listOf(
                    "That is such a comfort to hear$nameComma. Having someone physically there with you makes a world of difference. Please take things very gently today, and let them be there for you.",
                    "I am so glad you're not alone$nameComma. Lean on them right now, and give yourself permission to just rest and be supported. I'm right here whenever you need a calm sanctuary.",
                    "Thank you for letting me know$nameComma. Being in safe company is so important. Take a slow, gentle breath and let them support you through this."
                )
                ResponseDeduplicationManager.selectVariant(conversationId, options, state)
            }
            UserConfirmationType.DANGER_REDUCED -> {
                val options = listOf(
                    "I am deeply relieved to hear the immediate danger has eased$nameComma. But because you went through something so intense, is there a trusted person or a counsellor you can stay close to today?",
                    "I'm glad you're feeling safer in this moment$nameComma. Because heavy feelings can fluctuate, is there someone nearby who knows what you've been experiencing?",
                    "Hearing that you feel safer brings a lot of relief$nameComma. Please continue to be gentle with yourself. Would you still consider having someone you trust stay with you for a while?"
                )
                ResponseDeduplicationManager.selectVariant(conversationId, options, state)
            }
            UserConfirmationType.REFUSED_ASSISTANCE -> {
                val options = listOf(
                    "I understand that reaching out can feel terrifying or exhausting$nameComma, and I won't pressure you. But your life matters deeply. Would you consider just sending a text to a friend or keeping the Tele-MANAS number ($helpline) handy in case you need it?",
                    "It makes sense that you might feel hesitant$nameComma. You don't have to face everything at once. Even if you don't feel ready to speak, would you be open to sitting with someone you trust, or keeping $helpline saved in your phone?",
                    "I hear you$nameComma, and there is no judgment here. I will stay right here with you. If things become too heavy at any point, remember that free, confidential help is available 24/7 at $helpline."
                )
                ResponseDeduplicationManager.selectVariant(conversationId, options, state)
            }
            UserConfirmationType.AMBIGUOUS_ANSWER, null -> {
                val options = listOf(
                    "Were you able to connect with someone or speak with a counsellor$nameComma? Even a short message to a trusted friend can help you not feel so alone.",
                    "Is there someone you feel comfortable calling right now$nameComma? You don't have to carry this alone.",
                    "Have you managed to connect with a counsellor or someone you trust, or is somebody with you at the moment?"
                )
                ResponseDeduplicationManager.selectVariant(conversationId, options, state)
            }
            UserConfirmationType.NO_RESPONSE -> {
                "I'm still right here beside you$nameComma. If you're feeling unsafe or in pain, please dial $emergencyNumber for emergency help or call Tele-MANAS at $helpline. Your life is valuable."
            }
        }
    }
}
