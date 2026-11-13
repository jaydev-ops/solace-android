package com.example.data.safety

object SafetyResponseHandler {

    /**
     * Generate deterministic, compassionate response tailored for each safety level.
     */
    fun generateResponse(
        level: RiskLevel,
        signals: RiskSignals,
        userName: String? = null,
        country: String = "IN"
    ): String {
        val cleanName = userName?.trim()
        val hasName = !cleanName.isNullOrBlank() &&
            !cleanName.equals("Sanctuary", ignoreCase = true) &&
            !cleanName.equals("User", ignoreCase = true) &&
            !cleanName.equals("Guest", ignoreCase = true)
        val nameComma = if (hasName) ", $cleanName" else ""

        return when (level) {
            RiskLevel.LEVEL_4_CRITICAL -> {
                val emergencyNum = EmergencyResources.getPrimaryEmergencyNumber(country)
                val teleManas = EmergencyResources.getPrimaryMentalHealthNumber(country)

                """
                I'm really glad you told me$nameComma. I'm concerned about your immediate safety, and I don't want you to handle this alone.

                Please contact emergency services ($emergencyNum) or Tele-MANAS ($teleManas), or go to the nearest emergency department now. If possible, move away from anything you could use to hurt yourself and ask someone you trust to stay with you.

                Can you contact someone who can be with you right now?
                """.trimIndent()
            }

            RiskLevel.LEVEL_3_SUICIDAL_IDEATION -> {
                """
                Thank you for trusting me enough to share this$nameComma. I'm sorry things have become so painful. I want to take what you're saying seriously, and you don't have to face this alone.

                I'd like to understand whether you're safe right now. Are you having thoughts of ending your life at this moment?
                """.trimIndent()
            }

            RiskLevel.LEVEL_2_ELEVATED -> {
                if (signals.needsClarification && signals.ambiguous) {
                    """
                    It sounds like you are carrying a very heavy burden right now$nameComma, and I want to make sure I truly understand what you mean.

                    When you say that, are you thinking about hurting yourself or ending your life, or are you feeling deeply overwhelmed and needing rest?
                    """.trimIndent()
                } else {
                    """
                    It sounds like you've been carrying a lot for quite some time$nameComma. I don't want to minimise what you're experiencing. You deserve support beyond this conversation, and speaking with a mental health professional could help.

                    Can I ask something important? Have you recently had thoughts about hurting yourself or ending your life?
                    """.trimIndent()
                }
            }

            RiskLevel.LEVEL_1_DISTRESS -> {
                """
                I can understand that things might feel overwhelming right now$nameComma. You don't have to figure everything out immediately. Would it help to take a moment, slow down, and talk about what's been weighing on you?
                """.trimIndent()
            }

            RiskLevel.LEVEL_0_NORMAL -> {
                ""
            }
        }
    }
}

