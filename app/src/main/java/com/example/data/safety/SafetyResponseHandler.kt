package com.example.data.safety

/**
 * Generates deterministic, compassionate, human-like safety responses
 * with natural variations to prevent repetitive robotic phrasing.
 */
object SafetyResponseHandler {

    fun generateResponse(
        level: RiskLevel,
        signals: RiskSignals,
        userName: String? = null,
        country: String = "IN",
        conversationId: String = "active_session",
        state: RiskState? = null
    ): String {
        val cleanName = userName?.trim()
        val hasName = !cleanName.isNullOrBlank() &&
            !cleanName.equals("Sanctuary", ignoreCase = true) &&
            !cleanName.equals("User", ignoreCase = true) &&
            !cleanName.equals("Guest", ignoreCase = true)
        val nameComma = if (hasName) ", $cleanName" else ""

        val emergencyNum = EmergencyResources.getPrimaryEmergencyNumber(country)
        val teleManas = EmergencyResources.getPrimaryMentalHealthNumber(country)

        return when (level) {
            RiskLevel.LEVEL_4_CRITICAL -> {
                val templates = listOf(
                    """
                    I'm really glad you told me$nameComma. I'm concerned about your immediate safety, and I don't want you to handle this alone.

                    Please contact emergency services ($emergencyNum) or Tele-MANAS ($teleManas), or go to the nearest emergency department now. If possible, move away from anything you could use to hurt yourself and ask someone you trust to stay with you.

                    Can you contact someone who can be with you right now?
                    """.trimIndent(),
                    """
                    Thank you for being completely honest with me$nameComma. Your life is precious, and what you're describing is a critical medical emergency.

                    Please call emergency services at $emergencyNum or reach Tele-MANAS immediately at $teleManas. If you can, step away from any means of harm right now, and reach out to someone nearby who can physically stay beside you.

                    Is there someone close by you can call to come be with you immediately?
                    """.trimIndent(),
                    """
                    I hear how intense this moment is$nameComma, and I want to keep you safe. Because you are in immediate danger, please do not stay alone with this.

                    Please dial $emergencyNum or call the 24/7 mental health helpline Tele-MANAS at $teleManas. Move to a safe space right now and let someone you trust know what you're experiencing.

                    Can you reach out to a trusted friend, family member, or neighbour right now?
                    """.trimIndent()
                )
                ResponseDeduplicationManager.selectVariant(conversationId, templates, state)
            }

            RiskLevel.LEVEL_3_SUICIDAL_IDEATION -> {
                val templates = listOf(
                    """
                    Thank you for trusting me enough to share this$nameComma. I'm sorry things have become so painful. I want to take what you're saying seriously, and you don't have to face this alone.

                    I'd like to understand whether you're safe right now. Are you having thoughts of ending your life at this moment?
                    """.trimIndent(),
                    """
                    It takes courage to say that out loud$nameComma. Hearing that you're having these thoughts tells me how much weight you've been carrying, and I am right here listening.

                    Because your safety is my priority: Are you having thoughts of ending your life at this moment?
                    """.trimIndent(),
                    """
                    I hear you$nameComma, and I want you to know you aren't alone with these thoughts. When things feel this unbearable, taking a safe pause is so important.

                    May I ask you directly: Are you having thoughts of ending your life at this moment?
                    """.trimIndent()
                )
                ResponseDeduplicationManager.selectVariant(conversationId, templates, state)
            }

            RiskLevel.LEVEL_2_ELEVATED -> {
                if (signals.selfHarmOnly) {
                    val nssiTemplates = listOf(
                        """
                        I hear how overwhelming the pain is right now$nameComma, and I appreciate you being direct with me. You deserve support that doesn't require hurting yourself to find relief.

                        Because your safety matters: have you already taken any steps to hurt yourself today, or are you feeling an urge right now?
                        """.trimIndent(),
                        """
                        Thank you for sharing that with me$nameComma. Feeling the urge to hurt yourself is a sign of how deeply you are hurting inside, and I want to help you stay safe.

                        Can I check with you: have you already taken any steps to hurt yourself, or do you have any unsafe items near you right now?
                        """.trimIndent()
                    )
                    ResponseDeduplicationManager.selectVariant(conversationId, nssiTemplates, state)
                } else if (signals.needsClarification && signals.ambiguous) {
                    val clarificationTemplates = listOf(
                        """
                        It sounds like you are carrying a very heavy burden right now$nameComma, and I want to make sure I truly understand what you mean.

                        When you say that, are you thinking about hurting yourself or ending your life, or are you feeling deeply overwhelmed and needing rest?
                        """.trimIndent(),
                        """
                        That sounds like a lot to carry$nameComma. I don't want to overlook what you're telling me, so may I ask for clarity?

                        When you say that, are you thinking about hurting yourself or ending your life, or are you expressing how completely drained you feel?
                        """.trimIndent()
                    )
                    ResponseDeduplicationManager.selectVariant(conversationId, clarificationTemplates, state)
                } else {
                    val elevatedTemplates = listOf(
                        """
                        It sounds like you've been carrying a lot for quite some time$nameComma. I don't want to minimise what you're experiencing. You deserve support beyond this conversation, and speaking with a mental health professional could help.

                        Can I ask something important? Have you recently had thoughts about hurting yourself or ending your life?
                        """.trimIndent(),
                        """
                        What you're going through sounds deeply exhausting$nameComma. When hopelessness builds up like this, it's vital that you have the right support around you.

                        Because I care about your safety: have you recently had thoughts about hurting yourself or ending your life?
                        """.trimIndent(),
                        """
                        I hear how painful this has been for you$nameComma, and you don't deserve to feel worthless or like a burden.

                        Before we continue, could you tell me: have you recently had thoughts about hurting yourself or ending your life?
                        """.trimIndent()
                    )
                    ResponseDeduplicationManager.selectVariant(conversationId, elevatedTemplates, state)
                }
            }

            RiskLevel.LEVEL_1_DISTRESS -> {
                val distressTemplates = listOf(
                    """
                    I can understand that things might feel overwhelming right now$nameComma. You don't have to figure everything out immediately. Would it help to take a moment, slow down, and talk about what's been weighing on you?
                    """.trimIndent(),
                    """
                    It sounds like this has been really weighing on you$nameComma. I'm right here with you in this space. What part of everything feels heaviest for you at this moment?
                    """.trimIndent(),
                    """
                    Thank you for letting me know how you're feeling$nameComma. It's completely valid to feel exhausted or stressed after carrying so much. Would you like to share more about what happened today?
                    """.trimIndent()
                )
                ResponseDeduplicationManager.selectVariant(conversationId, distressTemplates, state)
            }

            RiskLevel.LEVEL_0_NORMAL -> ""
        }
    }
}
