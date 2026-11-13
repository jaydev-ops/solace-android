package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class MoodType(
    val title: String,
    val subtitle: String,
    val defaultValence: Int,
    val accentColor: Color,
    val bgTint: Color
) {
    HAPPY(
        title = "Joyful",
        subtitle = "Warm & radiant",
        defaultValence = 8,
        accentColor = Color(0xFFD98E04),
        bgTint = Color(0xFFFFF7E2)
    ),
    CALM(
        title = "Calm",
        subtitle = "Peaceful & centered",
        defaultValence = 8,
        accentColor = Color(0xFF4C7D5C),
        bgTint = Color(0xFFEFF7F2)
    ),
    NEUTRAL(
        title = "Steady",
        subtitle = "Grounded & quiet",
        defaultValence = 5,
        accentColor = Color(0xFF5E5854),
        bgTint = Color(0xFFF4F1EB)
    ),
    SAD(
        title = "Tender",
        subtitle = "Reflective & heavy",
        defaultValence = 3,
        accentColor = Color(0xFF4E6E8A),
        bgTint = Color(0xFFEEF3F8)
    ),
    ANXIOUS(
        title = "Restless",
        subtitle = "Fluttering & tense",
        defaultValence = 3,
        accentColor = Color(0xFF3B7C91),
        bgTint = Color(0xFFEAF4F7)
    ),
    ANGRY(
        title = "Stormy",
        subtitle = "Heated & frustrated",
        defaultValence = 2,
        accentColor = Color(0xFF9C5135),
        bgTint = Color(0xFFF9EFEA)
    ),
    OVERWHELMED(
        title = "Overwhelmed",
        subtitle = "Scattered & heavy",
        defaultValence = 2,
        accentColor = Color(0xFF545A61),
        bgTint = Color(0xFFEFF1F3)
    );

    val primaryColor: Color get() = accentColor

    companion object {
        fun fromString(name: String): MoodType {
            return entries.find { 
                it.name.equals(name, ignoreCase = true) || 
                it.title.equals(name, ignoreCase = true) 
            } ?: CALM
        }
    }
}
