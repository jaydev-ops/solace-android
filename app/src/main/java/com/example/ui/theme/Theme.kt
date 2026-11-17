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
val LocalSolaceColors = staticCompositionLocalOf { LightSolaceColors }

fun getLightColorScheme(tokens: MoodColorTokens, solace: SolaceColors = LightSolaceColors) = lightColorScheme(
    primary = tokens.primary,
    onPrimary = tokens.onPrimary,
    primaryContainer = tokens.container,
    onPrimaryContainer = tokens.onContainer,
    secondary = solace.accent,
    onSecondary = solace.onAccent,
    secondaryContainer = tokens.surfaceHighlight,
    onSecondaryContainer = tokens.primary,
    tertiary = tokens.primary,
    onTertiary = Color.White,
    background = solace.mainBackground,
    onBackground = solace.primaryText,
    surface = solace.cardBackground,
    onSurface = solace.primaryText,
    surfaceVariant = solace.surfaceRaised,
    onSurfaceVariant = solace.secondaryText,
    outline = solace.border,
    outlineVariant = solace.subtleBorder,
    error = solace.emergency,
    onError = Color.White
)

fun getDarkColorScheme(tokens: MoodColorTokens, solace: SolaceColors = DarkSolaceColors) = darkColorScheme(
    primary = solace.accent, // #D9B65D warm atmospheric accent
    onPrimary = solace.mainBackground,
    primaryContainer = Color(0xFF383324),
    onPrimaryContainer = solace.primaryText,
    secondary = tokens.primary,
    onSecondary = Color.White,
    secondaryContainer = solace.secondaryBackground,
    onSecondaryContainer = solace.primaryText,
    tertiary = solace.accent,
    onTertiary = solace.mainBackground,
    background = solace.mainBackground, // #171714
    onBackground = solace.primaryText, // #F2EDE2
    surface = solace.cardBackground, // #292720
    onSurface = solace.primaryText, // #F2EDE2
    surfaceVariant = solace.secondaryBackground, // #201F1B
    onSurfaceVariant = solace.secondaryText, // #B5ADA0
    outline = solace.border, // #39362F
    outlineVariant = solace.subtleBorder, // #302D26
    error = solace.emergency, // #D96B60
    onError = Color.White
)

fun getMoodTokens(mood: String, isDark: Boolean = false): MoodColorTokens {
    val base = when (mood.lowercase().trim()) {
        "happy", "joyful" -> HappyMoodColors
        "calm", "peaceful" -> CalmMoodColors
        "neutral", "steady" -> NeutralMoodColors
        "sad", "tender" -> SadMoodColors
        "anxious", "restless" -> AnxiousMoodColors
        "angry", "stormy" -> AngryMoodColors
        "overwhelmed" -> OverwhelmedMoodColors
        else -> CalmMoodColors
    }
    return if (isDark) base.toDark() else base
}

@Composable
fun SolaceTheme(
    activeMood: String = "Calm",
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val solaceColors = if (darkTheme) DarkSolaceColors else LightSolaceColors
    val moodTokens = getMoodTokens(activeMood, isDark = darkTheme)
    val colorScheme = if (darkTheme) getDarkColorScheme(moodTokens, solaceColors) else getLightColorScheme(moodTokens, solaceColors)

    CompositionLocalProvider(
        LocalMoodColors provides moodTokens,
        LocalSolaceColors provides solaceColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
