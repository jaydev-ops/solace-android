package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Core Warm Sanctuary Brand Tokens
val ButteryYellow = Color(0xFFF5D982)
val SoftButter = Color(0xFFFAEAB5)
val PaleVanilla = Color(0xFFFFF8E8)
val WarmIvory = Color(0xFFF9F6EE)
val CreamWhite = Color(0xFFFFFCF5)
val SoftCharcoal = Color(0xFF30312D)
val CharcoalDark = Color(0xFF1E201B)
val WarmGrey = Color(0xFF89877F)
val MutedBeige = Color(0xFFEAE3D5)
val SubtleOutline = Color(0xFFE0D8C8)

// Supporting Emotional Accents
val SereneTeal = Color(0xFF6FB5B5)
val SoftSage = Color(0xFFB9C9B3)
val SoftPeach = Color(0xFFE8C8B8)
val DustyBlue = Color(0xFFAEBED2)
val MutedLavender = Color(0xFFC9BCE0)
val SoftTerracotta = Color(0xFFD59B83)

// Dark Theme Surfaces
val DarkSanctuaryBg = Color(0xFF191916)
val DarkSanctuarySurface = Color(0xFF23231F)
val DarkSanctuarySurfaceRaised = Color(0xFF2C2C26)
val DarkTextPrimary = Color(0xFFF7F5EE)
val DarkTextSecondary = Color(0xFFB5B2A8)
val DarkBorder = Color(0xFF3C3B33)

// 7 Mood Specific Accent Definitions
data class MoodColorTokens(
    val primary: Color,
    val onPrimary: Color,
    val container: Color,
    val onContainer: Color,
    val surfaceHighlight: Color,
    val border: Color,
    val ambientGlow: Color
) {
    val backgroundTint: Color get() = surfaceHighlight
    val primaryAccent: Color get() = primary
}

val HappyMoodColors = MoodColorTokens(
    primary = Color(0xFFD98E04),
    onPrimary = Color(0xFF30312D),
    container = Color(0xFFFDE3A7),
    onContainer = Color(0xFF5A3B00),
    surfaceHighlight = Color(0xFFFFF9EE),
    border = Color(0xFFF2D99E),
    ambientGlow = Color(0xFFFFF2D0)
)

val CalmMoodColors = MoodColorTokens(
    primary = Color(0xFF4C7D5C),
    onPrimary = Color.White,
    container = Color(0xFFA8D5BA),
    onContainer = Color(0xFF1B3825),
    surfaceHighlight = Color(0xFFEFF5F1),
    border = Color(0xFFCCE0D3),
    ambientGlow = Color(0xFFE0EFE5)
)

val NeutralMoodColors = MoodColorTokens(
    primary = Color(0xFF5E5854),
    onPrimary = Color.White,
    container = Color(0xFFE2DDD6),
    onContainer = Color(0xFF2E2A27),
    surfaceHighlight = Color(0xFFF4F1EB),
    border = Color(0xFFDDD7CE),
    ambientGlow = Color(0xFFEFECE5)
)

val SadMoodColors = MoodColorTokens(
    primary = Color(0xFF4E6E8A),
    onPrimary = Color.White,
    container = Color(0xFFCAD5E2),
    onContainer = Color(0xFF1D3247),
    surfaceHighlight = Color(0xFFEEF2F7),
    border = Color(0xFFD0DCE7),
    ambientGlow = Color(0xFFE5ECF3)
)

val AnxiousMoodColors = MoodColorTokens(
    primary = Color(0xFF3B7C91),
    onPrimary = Color.White,
    container = Color(0xFFBCE0E8),
    onContainer = Color(0xFF143944),
    surfaceHighlight = Color(0xFFEAF1F3),
    border = Color(0xFFC7DFE5),
    ambientGlow = Color(0xFFDFEFF2)
)

val AngryMoodColors = MoodColorTokens(
    primary = Color(0xFF9C5135),
    onPrimary = Color.White,
    container = Color(0xFFE8C8BC),
    onContainer = Color(0xFF471F11),
    surfaceHighlight = Color(0xFFF5ECE7),
    border = Color(0xFFE4D0C7),
    ambientGlow = Color(0xFFF4E4DC)
)

val OverwhelmedMoodColors = MoodColorTokens(
    primary = Color(0xFF545A61),
    onPrimary = Color.White,
    container = Color(0xFFD8DCE0),
    onContainer = Color(0xFF262A2E),
    surfaceHighlight = Color(0xFFECEEF0),
    border = Color(0xFFD2D6DA),
    ambientGlow = Color(0xFFE8EAEB)
)
