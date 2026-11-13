package com.example.data.model

data class RegulationActivity(
    val id: String,
    val title: String,
    val category: String,
    val durationSeconds: Int,
    val description: String,
    val instructions: List<String>
)

val CuratedRegulationActivities = listOf(
    RegulationActivity(
        id = "box_breathing",
        title = "Box Breathing (4-4-4-4)",
        category = "Breathing",
        durationSeconds = 120,
        description = "A powerful autonomic stabilizer: inhale, hold, exhale, hold for equal 4-second counts.",
        instructions = listOf(
            "Inhale deeply through your nose for 4 seconds",
            "Hold your breath gently with relaxed shoulders for 4 seconds",
            "Exhale smoothly through your mouth for 4 seconds",
            "Rest empty before your next breath for 4 seconds"
        )
    ),
    RegulationActivity(
        id = "grounding_54321",
        title = "5-4-3-2-1 Sensory Grounding",
        category = "Grounding",
        durationSeconds = 180,
        description = "Anchor your physical presence back into the here and now through sensory cues.",
        instructions = listOf(
            "5 Things you can see around your immediate space",
            "4 Things you can physically touch or feel against your skin",
            "3 Things you can hear in your environment",
            "2 Things you can smell or subtle scents in the air",
            "1 Thing you can taste or an appreciative breath"
        )
    ),
    RegulationActivity(
        id = "cognitive_reframe",
        title = "Gentle Cognitive Reframing",
        category = "Cognitive",
        durationSeconds = 150,
        description = "Notice rigid thoughts like passing clouds without letting them define your worth.",
        instructions = listOf(
            "Identify the heavy thought or story your mind is telling you",
            "Ask: Is this 100% true, or is my nervous system tired?",
            "What would a compassionate friend say to you right now?",
            "Hold space for uncertainty without rushing to fix everything"
        )
    ),
    RegulationActivity(
        id = "body_scan",
        title = "Somatic Release Scan",
        category = "Somatic",
        durationSeconds = 180,
        description = "Unclench hidden physical stress stored in jaw, temples, neck, and chest.",
        instructions = listOf(
            "Unclench your teeth and let your tongue rest on the floor of your mouth",
            "Drop your shoulders 2 inches down away from your ears",
            "Open your palms and relax your belly completely",
            "Take one long, unhurried sigh out through your lips"
        )
    ),
    RegulationActivity(
        id = "gratitude_anchor",
        title = "Quiet Gratitude Anchor",
        category = "Gratitude",
        durationSeconds = 120,
        description = "Anchor your spirit by noticing three small, unpretentious mercies of today.",
        instructions = listOf(
            "Notice one sensory pleasure: warmth of a cup, light through a window",
            "Notice one act of quiet kindness you witnessed or received",
            "Give yourself credit for simply showing up and breathing today"
        )
    )
)
