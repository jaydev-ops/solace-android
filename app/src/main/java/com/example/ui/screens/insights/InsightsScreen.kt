package com.example.ui.screens.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.MoodEntryEntity
import com.example.data.model.CuratedRegulationActivities
import com.example.data.model.MoodType
import com.example.data.model.RegulationActivity
import com.example.ui.components.SolaceTopBar
import com.example.ui.theme.ButteryYellow
import com.example.ui.theme.CreamWhite
import com.example.ui.theme.LocalMoodColors
import com.example.ui.theme.MutedBeige
import com.example.ui.theme.PaleVanilla
import com.example.ui.theme.SoftButter
import com.example.ui.theme.SoftCharcoal
import com.example.ui.theme.SubtleOutline
import com.example.ui.theme.WarmGrey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InsightsScreen(
    userName: String,
    moods: List<MoodEntryEntity>,
    onStartRegulation: (RegulationActivity) -> Unit,
    onOpenSettings: () -> Unit
) {
    val moodColors = LocalMoodColors.current
    var selectedTimeframe by remember { mutableStateOf("Last 7 Days") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("insights_screen")
    ) {
        SolaceTopBar(
            subtitle = "emotional sanctuary analytics",
            userName = userName,
            onAvatarClick = onOpenSettings
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "Emotional Insights",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Validating your natural flow without judgment, streaks, or scores.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmGrey
                    )
                }
            }

            // Timeframe Selector Segmented Control (Apple HIG style)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(PaleVanilla)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Last 7 Days", "Last 30 Days", "All Time").forEach { timeframe ->
                        val isSelected = selectedTimeframe == timeframe
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) CreamWhite else Color.Transparent)
                                .clickable { selectedTimeframe = timeframe }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = timeframe,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) SoftCharcoal else WarmGrey
                            )
                        }
                    }
                }
            }

            // Weekly Emotional Rhythm Card (Matching Screenshot 3)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SubtleOutline, MutedBeige)))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Weekly Rhythm",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Height indicates emotional intensity (1–10)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = WarmGrey
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(PaleVanilla)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Balanced Flow",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = moodColors.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // 7 Pill Bars (Sun to Sat)
                        val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                        val weeklyData = listOf(
                            Pair(MoodType.CALM, 6),
                            Pair(MoodType.SAD, 4),
                            Pair(MoodType.HAPPY, 8),
                            Pair(MoodType.OVERWHELMED, 3),
                            Pair(MoodType.CALM, 7),
                            Pair(MoodType.HAPPY, 5),
                            Pair(MoodType.HAPPY, 8)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            days.forEachIndexed { index, day ->
                                val (mood, intensity) = weeklyData[index]
                                val barHeightFraction = (intensity / 10f).coerceIn(0.2f, 1f)

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Pillar
                                    Box(
                                        modifier = Modifier
                                            .width(26.dp)
                                            .fillMaxHeight(barHeightFraction)
                                            .clip(RoundedCornerShape(13.dp))
                                            .background(mood.bgTint)
                                            .border(1.dp, mood.accentColor.copy(alpha = 0.5f), RoundedCornerShape(13.dp))
                                            .padding(top = 4.dp),
                                        contentAlignment = Alignment.TopCenter
                                    ) {
                                        // Face or dot on top
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(mood.accentColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = when (mood) {
                                                    MoodType.HAPPY -> "◡"
                                                    MoodType.CALM -> "—"
                                                    MoodType.NEUTRAL -> "•"
                                                    MoodType.SAD -> "︵"
                                                    MoodType.ANXIOUS -> "~"
                                                    MoodType.ANGRY -> "!"
                                                    MoodType.OVERWHELMED -> "·"
                                                },
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = WarmGrey,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 7-Mood Distribution Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SubtleOutline, MutedBeige)))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Mood Taxonomy Distribution",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Your emotional tapestry over the selected horizon",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmGrey
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Percentage bars
                        val distribution = listOf(
                            Pair(MoodType.CALM, 38),
                            Pair(MoodType.HAPPY, 32),
                            Pair(MoodType.NEUTRAL, 12),
                            Pair(MoodType.ANXIOUS, 10),
                            Pair(MoodType.SAD, 8)
                        )

                        distribution.forEach { (mood, percentage) ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(mood.accentColor)
                                        )
                                        Text(
                                            text = mood.title,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = SoftCharcoal
                                        )
                                    }
                                    Text(
                                        text = "$percentage%",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = WarmGrey
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MutedBeige)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(percentage / 100f)
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(mood.accentColor)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Contextual Triggers Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SubtleOutline, MutedBeige)))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Situational Correlates",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Factors associated with noticeable shifts in well-being",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmGrey
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        val correlates = listOf(
                            Triple("Morning quiet", "+3.8 Calmer valence", MoodType.CALM.accentColor),
                            Triple("Solitude & Reading", "+2.4 Grounded", MoodType.HAPPY.accentColor),
                            Triple("Deadlines & Fast Pacing", "-1.9 Emotional tension", MoodType.ANXIOUS.accentColor),
                            Triple("Restful Sleep (7+ hrs)", "+3.1 Restorative", MoodType.CALM.accentColor)
                        )

                        correlates.forEach { (tag, effect, color) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(PaleVanilla)
                                    .border(1.dp, MutedBeige, RoundedCornerShape(14.dp))
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftCharcoal
                                )
                                Text(
                                    text = effect,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = color
                                )
                            }
                        }
                    }
                }
            }

            // Somatic Regulation Quick Launch
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PaleVanilla),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SoftButter, MutedBeige)))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(ButteryYellow),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Spa,
                                    contentDescription = null,
                                    tint = SoftCharcoal,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "SOMATIC SANCTUARY PRACTICE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftCharcoal,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Ready to reset your nervous system?",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        CuratedRegulationActivities.take(2).forEach { activity ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(CreamWhite)
                                    .border(1.dp, MutedBeige, RoundedCornerShape(16.dp))
                                    .clickable { onStartRegulation(activity) }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = activity.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SoftCharcoal
                                    )
                                    Text(
                                        text = "${activity.durationSeconds / 60} mins • ${activity.category}",
                                        fontSize = 11.sp,
                                        color = WarmGrey
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(ButteryYellow),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Start",
                                        tint = SoftCharcoal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
