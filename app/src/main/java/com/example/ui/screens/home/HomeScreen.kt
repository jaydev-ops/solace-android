package com.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.MoodEntryEntity
import com.example.data.model.MoodType
import com.example.navigation.Screen
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
fun HomeScreen(
    userName: String,
    latestMood: MoodEntryEntity?,
    weeklyMoods: List<MoodEntryEntity>,
    onQuickCheckIn: (MoodType) -> Unit,
    onNavigate: (Screen) -> Unit,
    onOpenSettings: () -> Unit,
    onQuickMoodPreview: ((String) -> Unit)? = null
) {
    val moodColors = LocalMoodColors.current
    val todayFormatted = remember {
        SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
    }

    val currentHour = remember {
        java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    }
    val sanctuaryName = userName.ifBlank { "Sanctuary" }

    val greetingInfo = remember(currentHour, sanctuaryName) {
        when {
            currentHour in 4..11 -> {
                HomeGreetingData(
                    tag = "Gentle Morning",
                    greeting = "Good morning, $sanctuaryName.",
                    icon = Icons.Filled.WbSunny,
                    subtitle = "How is your spirit feeling in this quiet hour?"
                )
            }
            currentHour in 12..16 -> {
                HomeGreetingData(
                    tag = "Peaceful Afternoon",
                    greeting = "Good afternoon, $sanctuaryName.",
                    icon = Icons.Filled.WbSunny,
                    subtitle = "Take a gentle pause to breathe and check in with yourself."
                )
            }
            else -> {
                HomeGreetingData(
                    tag = "Quiet Evening",
                    greeting = "Good evening, $sanctuaryName.",
                    icon = Icons.Filled.Nightlight,
                    subtitle = "Unwind your thoughts and let your spirit rest in peace."
                )
            }
        }
    }

    var selectedQuickMood by remember { mutableStateOf(MoodType.CALM) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        SolaceTopBar(
            subtitle = "peaceful haven",
            userName = sanctuaryName,
            onAvatarClick = onOpenSettings
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Greeting
            item {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = greetingInfo.icon,
                            contentDescription = null,
                            tint = moodColors.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = greetingInfo.tag,
                            style = MaterialTheme.typography.labelMedium,
                            color = moodColors.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = todayFormatted,
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmGrey
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = greetingInfo.greeting,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = greetingInfo.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmGrey
                    )
                }
            }

            // Breath of Dawn Hero Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CreamWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Background dawn image
                        Image(
                            painter = painterResource(id = R.drawable.img_sanctuary_dawn),
                            contentDescription = "Breath of Dawn Sanctuary",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Gradient overlay for contrast
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            SoftCharcoal.copy(alpha = 0.75f),
                                            SoftCharcoal.copy(alpha = 0.40f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(ButteryYellow)
                                )
                                Text(
                                    text = "BREATH OF DAWN",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = SoftButter
                                )
                            }

                            Column {
                                Text(
                                    text = "\"Peace is not the absence of storm, but the gentle room inside.\"",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    lineHeight = 22.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Take one slow breath before starting your day.",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }

            // Intention Check (Quick Mood Check-In)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SubtleOutline, MutedBeige)))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Spa,
                                contentDescription = null,
                                tint = moodColors.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Intention Check",
                                style = MaterialTheme.typography.labelMedium,
                                color = WarmGrey,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Take a moment for yourself",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Pause, breathe, and notice how you're feeling today.",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmGrey
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 6 Quick Mood Chips Grid (2 rows x 3 columns)
                        val moods = listOf(
                            MoodType.HAPPY, MoodType.CALM, MoodType.NEUTRAL,
                            MoodType.SAD, MoodType.ANXIOUS, MoodType.ANGRY
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                moods.take(3).forEach { mood ->
                                    QuickMoodChip(
                                        mood = mood,
                                        selected = selectedQuickMood == mood,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            selectedQuickMood = mood
                                            onQuickMoodPreview?.invoke(mood.title)
                                        }
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                moods.drop(3).take(3).forEach { mood ->
                                    QuickMoodChip(
                                        mood = mood,
                                        selected = selectedQuickMood == mood,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            selectedQuickMood = mood
                                            onQuickMoodPreview?.invoke(mood.title)
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
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
                                        .background(selectedQuickMood.accentColor)
                                )
                                Text(
                                    text = "Selected: ${selectedQuickMood.title} • Glowing gently",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SoftCharcoal
                                )
                            }

                            Text(
                                text = "Full check-in →",
                                style = MaterialTheme.typography.labelSmall,
                                color = moodColors.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    onQuickMoodPreview?.invoke(selectedQuickMood.title)
                                    onNavigate(Screen.CheckIn)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onQuickCheckIn(selectedQuickMood) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("home_log_checkin_button"),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = moodColors.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Log Check-In (${selectedQuickMood.title})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            // Gentle Rhythm (Weekly Mood Overview)
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
                                    text = "Gentle Rhythm",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Validating your natural flow this week",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = WarmGrey
                                )
                            }

                            Text(
                                text = "History >",
                                style = MaterialTheme.typography.labelMedium,
                                color = moodColors.primary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { onNavigate(Screen.Insights) }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Weekly days dots
                        val weekDays = listOf("M", "T", "W", "T", "F", "S", "S")
                        val sampleMoods = listOf(
                            MoodType.CALM,
                            MoodType.HAPPY,
                            latestMood?.let { MoodType.fromString(it.mood) } ?: MoodType.HAPPY,
                            MoodType.CALM,
                            null,
                            null,
                            null
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            weekDays.forEachIndexed { index, day ->
                                val mood = sampleMoods.getOrNull(index)
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = WarmGrey
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(
                                                mood?.bgTint ?: PaleVanilla
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = mood?.accentColor?.copy(alpha = 0.6f) ?: MutedBeige,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (mood != null) {
                                            Box(
                                                modifier = Modifier
                                                    .size(9.dp)
                                                    .clip(CircleShape)
                                                    .background(mood.accentColor)
                                            )
                                        } else {
                                            Text(
                                                text = "—",
                                                fontSize = 11.sp,
                                                color = WarmGrey
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (index == 2) "Today" else mood?.title ?: "—",
                                        fontSize = 10.sp,
                                        color = if (index == 2) moodColors.primary else WarmGrey,
                                        fontWeight = if (index == 2) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Today's Gentle Reflection Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(Screen.Journal) },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SubtleOutline, MutedBeige)))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
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
                                text = "TODAY'S GENTLE REFLECTION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmGrey,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "\"What made today a little gentler on your spirit?\"",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(PaleVanilla)
                                .border(1.dp, MutedBeige, RoundedCornerShape(16.dp))
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Write your unhurried thoughts...",
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmGrey
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Write reflection",
                                tint = moodColors.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // solace AI Companion Spotlight Card
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
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    tint = SoftCharcoal,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "YOUR SANCTUARY COMPANION",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftCharcoal,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Need someone to talk to?",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "solace AI is here whenever you need a peaceful, non-judgmental ear to unpack thoughts or untangle feelings.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftCharcoal.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { onNavigate(Screen.Companion) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ButteryYellow,
                                    contentColor = SoftCharcoal
                                )
                            ) {
                                Text(
                                    text = "Start a conversation",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(CreamWhite)
                                    .border(1.dp, MutedBeige, CircleShape)
                                    .clickable { onNavigate(Screen.Companion) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Mic,
                                    contentDescription = "Voice reflection",
                                    tint = SoftCharcoal,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Gentle Pattern Noticed
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SubtleOutline, MutedBeige)))
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(moodColors.container),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Nightlight,
                                contentDescription = null,
                                tint = moodColors.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Gentle Pattern Noticed",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "You tend to report feeling calmer on evenings when you take a moment to unwind and jot down a gentle reflection.",
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmGrey,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Synthesized from past 14 days",
                                fontSize = 11.sp,
                                color = WarmGrey.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickMoodChip(
    mood: MoodType,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (selected) mood.accentColor else MutedBeige
    val bgColor = if (selected) mood.bgTint else CreamWhite

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val icon = when (mood) {
            MoodType.HAPPY -> Icons.Filled.WbSunny
            MoodType.CALM -> Icons.Filled.Spa
            MoodType.NEUTRAL -> Icons.Filled.SentimentNeutral
            MoodType.SAD -> Icons.Filled.SentimentDissatisfied
            MoodType.ANXIOUS -> Icons.Filled.SentimentVeryDissatisfied
            MoodType.ANGRY -> Icons.Filled.SentimentVeryDissatisfied
            MoodType.OVERWHELMED -> Icons.Filled.SentimentDissatisfied
        }

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(mood.accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = mood.title,
                tint = mood.accentColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = mood.title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 11.sp
        )
    }
}

private data class HomeGreetingData(
    val tag: String,
    val greeting: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val subtitle: String
)
