package com.example.data.safety

object EmergencyResources {
    // Primary verified resources for India (Solace+ core deployment)
    val IndiaResources = listOf(
        EmergencyResource(
            name = "Emergency services",
            number = "112",
            description = "India — emergency assistance",
            category = "emergency",
            country = "IN"
        ),
        EmergencyResource(
            name = "Mental health support",
            number = "14416",
            description = "Tele-MANAS (24/7 free toll-free)",
            category = "mental_health",
            country = "IN"
        ),
        EmergencyResource(
            name = "KIRAN Helpline",
            number = "1800-599-0019",
            description = "Ministry of Social Justice 24/7 mental health helpline",
            category = "mental_health",
            country = "IN"
        )
    )

    // Global / US resources for cross-region fallback
    val GlobalResources = listOf(
        EmergencyResource(
            name = "988 Suicide & Crisis Lifeline",
            number = "988",
            description = "Free, confidential 24/7 support (US & Canada)",
            category = "mental_health",
            country = "US"
        ),
        EmergencyResource(
            name = "Crisis Text Line",
            number = "741741",
            description = "Text HOME to 741741 for 24/7 support",
            category = "crisis_text",
            country = "GLOBAL"
        )
    )

    fun getPrimaryEmergencyNumber(country: String = "IN"): String {
        return if (country.equals("IN", ignoreCase = true)) "112" else "911"
    }

    fun getPrimaryMentalHealthNumber(country: String = "IN"): String {
        return if (country.equals("IN", ignoreCase = true)) "14416" else "988"
    }
}
