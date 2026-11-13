package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalMoodColors = staticCompositionLocalOf { CalmMoodColors }

fun getLightColorScheme(tokens: MoodColorTokens) = lightColorScheme(
    primary = tokens.primary,
    onPrimary = tokens.onPrimary,
    primaryContainer = tokens.container,
    onPrimaryContainer = tokens.onContainer,
    secondary = tokens.border,
    onSecondary = SoftCharcoal,
    secondaryContainer = tokens.surfaceHighlight,
    onSecondaryContainer = tokens.primary,
    tertiary = tokens.primary,
    onTertiary = Color.White,
    background = WarmIvory,
    onBackground = SoftCharcoal,
    surface = CreamWhite,
    onSurface = SoftCharcoal,
    surfaceVariant = tokens.surfaceHighlight,
    onSurfaceVariant = WarmGrey,
    outline = tokens.border,
    outlineVariant = SubtleOutline
)

fun getDarkColorScheme(tokens: MoodColorTokens) = darkColorScheme(
    primary = tokens.primary,
    onPrimary = tokens.onPrimary,
    primaryContainer = tokens.container.copy(alpha = 0.35f),
    onPrimaryContainer = tokens.surfaceHighlight,
    secondary = tokens.border,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E221E),
    onSecondaryContainer = tokens.surfaceHighlight,
    tertiary = DarkTextPrimary,
    onTertiary = DarkSanctuaryBg,
    background = DarkSanctuaryBg,
    onBackground = DarkTextPrimary,
    surface = DarkSanctuarySurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSanctuarySurfaceRaised,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = Color(0xFF4C4A42)
)

fun getMoodTokens(mood: String): MoodColorTokens {
    return when (mood.lowercase().trim()) {
        "happy", "joyful" -> HappyMoodColors
        "calm", "peaceful" -> CalmMoodColors
        "neutral", "steady" -> NeutralMoodColors
        "sad", "tender" -> SadMoodColors
        "anxious", "restless" -> AnxiousMoodColors
        "angry", "stormy" -> AngryMoodColors
        "overwhelmed" -> OverwhelmedMoodColors
        else -> CalmMoodColors
    }
}

@Composable
fun SolaceTheme(
    activeMood: String = "Calm",
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val moodTokens = getMoodTokens(activeMood)
    val colorScheme = if (darkTheme) getDarkColorScheme(moodTokens) else getLightColorScheme(moodTokens)

    CompositionLocalProvider(LocalMoodColors provides moodTokens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
