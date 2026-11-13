package com.example.data.model

enum class RegulationMode(
    val title: String,
    val iconEmoji: String,
    val description: String,
    val promptPlaceholder: String,
    val starterPrompts: List<String>
) {
    LISTEN(
        title = "Listen",
        iconEmoji = "👂",
        description = "Silent holding space with unconditional validation and gentle presence.",
        promptPlaceholder = "Speak whatever feels heavy right now...",
        starterPrompts = listOf(
            "I don't know what I'm feeling",
            "I just want someone to listen",
            "Everything feels heavy today",
            "Can I just vent for a moment?"
        )
    ),
    UNDERSTAND(
        title = "Understand",
        iconEmoji = "💡",
        description = "Gentle exploration of emotional triggers, underlying roots, and thought patterns.",
        promptPlaceholder = "What thoughts are circling your mind?",
        starterPrompts = listOf(
            "Help me unpack this week",
            "Why did that interaction sting so much?",
            "I feel conflicted between two emotions",
            "Help me understand my anxiety today"
        )
    ),
    CALM(
        title = "Calm",
        iconEmoji = "🌿",
        description = "Somatic de-escalation, paced breathwork guidance, and soothing down-regulation.",
        promptPlaceholder = "Where do you feel physical tension?",
        starterPrompts = listOf(
            "Let's do a quick grounding breath",
            "My chest feels tight and fluttery",
            "Guide me through physical relaxation",
            "Help me calm my racing thoughts"
        )
    ),
    REFLECT(
        title = "Reflect",
        iconEmoji = "🪞",
        description = "Deep cognitive mirroring, compassionate reframing, and discovering wisdom.",
        promptPlaceholder = "What perspective feels hard to see?",
        starterPrompts = listOf(
            "What can this difficult feeling teach me?",
            "How can I treat myself with more grace?",
            "Reflect on my growth this past month",
            "Help me reframe this self-criticism"
        )
    ),
    REFOCUS(
        title = "Refocus",
        iconEmoji = "🧭",
        description = "Gentle micro-actions to break paralysis, overwhelm, and restore personal agency.",
        promptPlaceholder = "What is one small thing you can control?",
        starterPrompts = listOf(
            "I have so much to do I'm frozen",
            "Give me one tiny step for right now",
            "How do I unstick myself gently?",
            "Help me prioritize with kindness"
        )
    ),
    GRATITUDE(
        title = "Gratitude",
        iconEmoji = "☀️",
        description = "Savoring quiet peace, gentle warmth, and anchoring small everyday mercies.",
        promptPlaceholder = "What brought a moment of quiet relief?",
        starterPrompts = listOf(
            "Three small comforts from this morning",
            "Someone whose presence warmed my spirit",
            "A gentle thing I appreciate about myself",
            "Finding light in a difficult week"
        )
    );

    companion object {
        fun fromString(name: String): RegulationMode {
            return entries.find { it.name.equals(name, ignoreCase = true) || it.title.equals(name, ignoreCase = true) } ?: LISTEN
        }
    }
}
