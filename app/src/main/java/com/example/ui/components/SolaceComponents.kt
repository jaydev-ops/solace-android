package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Equalizer
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navigation.Screen
import com.example.ui.theme.ButteryYellow
import com.example.ui.theme.LocalMoodColors
import com.example.ui.theme.MutedBeige
import com.example.ui.theme.SoftButter
import com.example.ui.theme.SoftCharcoal
import com.example.ui.theme.SubtleOutline
import com.example.ui.theme.WarmGrey
import com.example.ui.theme.WarmIvory

@Composable
fun SolaceTopBar(
    subtitle: String = "peaceful haven",
    userName: String = "Sanctuary",
    onAvatarClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onAvatarClick() }
        ) {
            Text(
                text = "solace",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(3.dp))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(ButteryYellow),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftCharcoal,
                    lineHeight = 12.sp
                )
            }
        }

        // Subtitle badge & avatar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (subtitle.isNotBlank()) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmGrey
                    )
                }
            }

            // User Avatar
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(SoftButter)
                    .border(1.5.dp, ButteryYellow, CircleShape)
                    .clickable { onAvatarClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SoftCharcoal
                )
            }
        }
    }
}

@Composable
fun SolaceBottomNav(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    val moodColors = LocalMoodColors.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        tonalElevation = 0.dp,
        shadowElevation = 10.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                label = "Home",
                selected = currentScreen == Screen.Home,
                selectedIcon = Icons.Filled.Spa,
                unselectedIcon = Icons.Outlined.Spa,
                onClick = { onNavigate(Screen.Home) }
            )
            NavItem(
                label = "Check-In",
                selected = currentScreen == Screen.CheckIn,
                selectedIcon = Icons.Filled.WbSunny,
                unselectedIcon = Icons.Outlined.WbSunny,
                onClick = { onNavigate(Screen.CheckIn) }
            )
            // Prominent solace AI center item (Apple-grade floating capsule)
            SolaceAiNavItem(
                selected = currentScreen == Screen.Companion,
                onClick = { onNavigate(Screen.Companion) }
            )
            NavItem(
                label = "Journal",
                selected = currentScreen == Screen.Journal,
                selectedIcon = Icons.Filled.AutoStories,
                unselectedIcon = Icons.Outlined.AutoStories,
                onClick = { onNavigate(Screen.Journal) }
            )
            NavItem(
                label = "Insights",
                selected = currentScreen == Screen.Insights,
                selectedIcon = Icons.Filled.Equalizer,
                unselectedIcon = Icons.Outlined.Equalizer,
                onClick = { onNavigate(Screen.Insights) }
            )
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    selected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    onClick: () -> Unit
) {
    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(200),
        label = "navColor"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("nav_item_${label.lowercase()}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = if (selected) selectedIcon else unselectedIcon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = contentColor,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun SolaceAiNavItem(
    selected: Boolean,
    onClick: () -> Unit
) {
    val solaceColors = com.example.ui.theme.LocalSolaceColors.current
    val bgColor by animateColorAsState(
        targetValue = if (selected) solaceColors.accent else solaceColors.inputBackground,
        animationSpec = tween(200),
        label = "aiNavBg"
    )
    val contentColor = if (selected) solaceColors.onAccent else MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(
                width = 1.dp,
                color = if (selected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("nav_item_solace_ai"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.AutoAwesome,
            contentDescription = "solace AI",
            tint = contentColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "solace AI",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            fontSize = 11.sp
        )
    }
}

@Composable
fun AmbientSanctuaryCanvas(
    activeMood: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val moodColors = LocalMoodColors.current
    val solaceColors = com.example.ui.theme.LocalSolaceColors.current
    val ambientColor by animateColorAsState(
        targetValue = moodColors.ambientGlow,
        animationSpec = tween(500),
        label = "ambientGlow"
    )
    val highlightColor by animateColorAsState(
        targetValue = moodColors.surfaceHighlight,
        animationSpec = tween(500),
        label = "highlightColor"
    )

    val gradientColors = if (solaceColors.isDark) {
        listOf(
            ambientColor.copy(alpha = 0.45f),
            solaceColors.secondaryBackground,
            solaceColors.mainBackground
        )
    } else {
        listOf(
            ambientColor.copy(alpha = 0.75f),
            highlightColor.copy(alpha = 0.45f),
            solaceColors.mainBackground
        )
    }

    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                )
            )
    ) {
        content()
    }
}

@Composable
fun MoodPillBadge(
    moodName: String,
    modifier: Modifier = Modifier
) {
    val isDark = com.example.ui.theme.LocalSolaceColors.current.isDark
    val moodColors = com.example.ui.theme.getMoodTokens(moodName, isDark)
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(moodColors.surfaceHighlight)
            .border(1.dp, moodColors.border.copy(alpha = 0.8f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(moodColors.primary)
        )
        Text(
            text = moodName,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
