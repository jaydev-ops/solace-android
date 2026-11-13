package com.example.ui.screens.checkin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.model.MoodType
import com.example.ui.components.SolaceTopBar
import com.example.ui.theme.CreamWhite
import com.example.ui.theme.LocalMoodColors
import com.example.ui.theme.MutedBeige
import com.example.ui.theme.PaleVanilla
import com.example.ui.theme.SoftCharcoal
import com.example.ui.theme.SubtleOutline
import com.example.ui.theme.WarmGrey
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckInScreen(
    userName: String,
    onSaveCheckIn: (mood: String, valence: Int, intensity: Int, tags: List<String>, note: String) -> Unit,
    onOpenSettings: () -> Unit,
    onMoodPreview: ((String) -> Unit)? = null
) {
    val moodColors = LocalMoodColors.current

    var selectedMood by remember { mutableStateOf(MoodType.CALM) }
    var intensityValue by remember { mutableFloatStateOf(4f) }
    val selectedTags = remember {
        mutableStateListOf("Morning quiet", "Peace of mind")
    }
    var noteText by remember { mutableStateOf("") }
    var isSavedConfirmationVisible by remember { mutableStateOf(false) }

    // Notify preview of initial selection
    LaunchedEffect(Unit) {
        onMoodPreview?.invoke(selectedMood.title)
    }

    // Dynamic contextual tags adapted to what is making them happy or bothering them
    val contextualTags = remember(selectedMood) {
        when (selectedMood) {
            MoodType.HAPPY -> listOf(
                "Joyful spark", "Gratitude", "Celebration", "Progress", "Good news",
                "Loving kindness", "Sunlight & Warmth", "Friendship", "Music & Play", "Accomplishment"
            )
            MoodType.ANGRY, MoodType.ANXIOUS, MoodType.SAD, MoodType.OVERWHELMED -> listOf(
                "Frustration", "Overthinking", "Heavy heart", "Fatigue", "Unfairness",
                "Deadlines & Work", "Conflict", "Body tension", "Disappointment", "Feeling unheard", "Burnout"
            )
            MoodType.CALM, MoodType.NEUTRAL -> listOf(
                "Quiet morning", "Peace of mind", "Sleep & Rest", "Nature & Walk",
                "Gentle breathing", "Solitude", "Creativity", "Patience", "Physical Health"
            )
        }
    }

    // Contextual prompts dynamically changing with mood
    val isHappy = selectedMood == MoodType.HAPPY
    val isBothered = selectedMood == MoodType.ANGRY || selectedMood == MoodType.ANXIOUS ||
            selectedMood == MoodType.SAD || selectedMood == MoodType.OVERWHELMED

    val moodResonanceBadge = when (selectedMood) {
        MoodType.HAPPY -> "✨ Joyful Resonance • Celebrating your light"
        MoodType.CALM -> "🌿 Serene Sanctuary • Rooted in peace"
        MoodType.NEUTRAL -> "⚖️ Steady Baseline • Present in the moment"
        MoodType.SAD -> "💧 Tender Space • Holding sadness with gentle care"
        MoodType.ANXIOUS -> "🌊 Restless Waves • Soothing tension within"
        MoodType.ANGRY -> "🔥 Stormy Space • Safe place to let the heat out"
        MoodType.OVERWHELMED -> "☁️ Heavy Weight • Softly unloading your shoulders"
    }

    val reflectionSectionTitle = when {
        isHappy -> "What is making you happy in this moment?"
        isBothered -> "What is bothering or weighing on you?"
        else -> "What is resting in your thoughts right now?"
    }

    val reflectionSectionSubtitle = when {
        isHappy -> "Savor the light. Naming what brings you warmth and joy wires it deeper into your wellbeing."
        isBothered -> "Zero judgment here. Spill whatever is frustrating, heavy, or uncomfortable. It stays safe."
        else -> "Notice whatever quiet thoughts or sensations are present without rush or expectation."
    }

    val reflectionPlaceholder = when (selectedMood) {
        MoodType.HAPPY -> "Tell solace what's making you smile, feel grateful, or spark joy today..."
        MoodType.ANGRY -> "Let out what's bothering you... what made you feel angry, frustrated, or treated unfairly?"
        MoodType.ANXIOUS -> "What's bothering or racing in your mind? Write down the worry to take its power away..."
        MoodType.SAD -> "What is hurting or feeling heavy? You are safe to let it be tender here..."
        MoodType.OVERWHELMED -> "Write down what's piling up so you don't have to carry it all in your head..."
        MoodType.CALM, MoodType.NEUTRAL -> "Record whatever thoughts, sensations, or quiet reflections are here..."
    }

    val animatedPrimaryColor by animateColorAsState(
        targetValue = moodColors.primary,
        animationSpec = tween(400),
        label = "primaryColor"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("check_in_screen")
    ) {
        SolaceTopBar(
            subtitle = "peaceful haven",
            userName = userName,
            onAvatarClick = onOpenSettings
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Live Attunement Header Banner
            item {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = moodColors.container.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, animatedPrimaryColor.copy(alpha = 0.4f)),
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(animatedPrimaryColor)
                                )
                                Text(
                                    text = moodResonanceBadge,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "How are you feeling right now?",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "The entire sanctuary attunes to your emotional state. Select what fits your spirit, and express what is bothering you or bringing you joy.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmGrey,
                        lineHeight = 22.sp
                    )
                }
            }

            // Primary Emotional Resonance: 7 Mood Cards Grid with Real-Time Theme Shift
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Select Your Mood to Attune Sanctuary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Theme adapts live",
                            style = MaterialTheme.typography.labelSmall,
                            color = animatedPrimaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val allMoods = MoodType.entries

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        for (i in allMoods.indices step 2) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                val mood1 = allMoods[i]
                                MoodResonanceCard(
                                    mood = mood1,
                                    selected = selectedMood == mood1,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        selectedMood = mood1
                                        onMoodPreview?.invoke(mood1.title)
                                    }
                                )

                                if (i + 1 < allMoods.size) {
                                    val mood2 = allMoods[i + 1]
                                    MoodResonanceCard(
                                        mood = mood2,
                                        selected = selectedMood == mood2,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            selectedMood = mood2
                                            onMoodPreview?.invoke(mood2.title)
                                        }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // Intensity Calibration Slider
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
                                    text = "Intensity",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "From delicate whisper to consuming wave",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = WarmGrey
                                )
                            }

                            val intensityInt = intensityValue.roundToInt()
                            val intensityDescriptor = when (intensityInt) {
                                in 1..3 -> "$intensityInt • Subtle & Gentle"
                                in 4..6 -> "$intensityInt • Gentle & Present"
                                in 7..8 -> "$intensityInt • Noticeable & Heavy"
                                else -> "$intensityInt • Intense & Consuming"
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(selectedMood.bgTint)
                                    .border(1.dp, animatedPrimaryColor.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = intensityDescriptor,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = animatedPrimaryColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Slider(
                            value = intensityValue,
                            onValueChange = { intensityValue = it },
                            valueRange = 1f..10f,
                            steps = 8,
                            colors = SliderDefaults.colors(
                                thumbColor = animatedPrimaryColor,
                                activeTrackColor = animatedPrimaryColor,
                                inactiveTrackColor = MutedBeige
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("check_in_intensity_slider")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "• 1 Whisper", style = MaterialTheme.typography.labelSmall, color = WarmGrey)
                            Text(text = "5 Noticeable", style = MaterialTheme.typography.labelSmall, color = WarmGrey)
                            Text(text = "10 Consuming •", style = MaterialTheme.typography.labelSmall, color = WarmGrey)
                        }
                    }
                }
            }

            // Dynamic Contextual Reflection Note (What's bothering or making them happy)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.verticalGradient(listOf(animatedPrimaryColor.copy(alpha = 0.35f), MutedBeige))
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = reflectionSectionTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(animatedPrimaryColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = if (isHappy) "SAVOR LIGHT" else if (isBothered) "SAFE UNBURDEN" else "REFLECTION",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = animatedPrimaryColor,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = reflectionSectionSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmGrey,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = noteText,
                            onValueChange = { noteText = it },
                            placeholder = {
                                Text(
                                    text = reflectionPlaceholder,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = WarmGrey.copy(alpha = 0.7f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .testTag("check_in_note_input"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = PaleVanilla,
                                unfocusedContainerColor = PaleVanilla,
                                focusedBorderColor = animatedPrimaryColor,
                                unfocusedBorderColor = MutedBeige
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null,
                                tint = WarmGrey,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Zero analytics • Strictly private on your device & Cloud sanctuary",
                                fontSize = 11.sp,
                                color = WarmGrey
                            )
                        }
                    }
                }
            }

            // Situational Tags Selector (Adapted to selected mood)
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
                            Text(
                                text = "What is coloring this space?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Tap tags that apply",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmGrey
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            contextualTags.forEach { tag ->
                                val isSelected = selectedTags.contains(tag)
                                TagPill(
                                    label = tag,
                                    selected = isSelected,
                                    activeColor = animatedPrimaryColor,
                                    onClick = {
                                        if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Emotional Grounding Quote
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PaleVanilla),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.verticalGradient(listOf(animatedPrimaryColor.copy(alpha = 0.3f), MutedBeige))
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(selectedMood.bgTint)
                                .border(1.dp, animatedPrimaryColor.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isHappy) Icons.Default.WbSunny else Icons.Filled.Spa,
                                contentDescription = null,
                                tint = animatedPrimaryColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = if (isHappy) "Cultivating Joy" else "Emotional Sanctuary",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SoftCharcoal
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isHappy) {
                                    "When joy arrives, give yourself full permission to revel in it. Good feelings deserve your quiet presence."
                                } else {
                                    "Feelings are visitors; they arrive, linger, and eventually pass like clouds across an open sky. You are the sky."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = SoftCharcoal.copy(alpha = 0.85f),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Save Check-in Button
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            onSaveCheckIn(
                                selectedMood.title,
                                selectedMood.defaultValence,
                                intensityValue.roundToInt(),
                                selectedTags.toList(),
                                noteText
                            )
                            isSavedConfirmationVisible = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("save_check_in_button"),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = animatedPrimaryColor,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Save check-in & attune sanctuary",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = animatedPrimaryColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Sanctuary theme automatically flows with your recorded emotional state.",
                            fontSize = 11.sp,
                            color = WarmGrey
                        )
                    }

                    AnimatedVisibility(visible = isSavedConfirmationVisible) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(selectedMood.bgTint)
                                .border(1.dp, animatedPrimaryColor, RoundedCornerShape(16.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓ Check-in saved. Your sanctuary is now attuned to ${selectedMood.title}.",
                                color = animatedPrimaryColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodResonanceCard(
    mood: MoodType,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (selected) mood.accentColor else MutedBeige
    val bgColor = if (selected) mood.bgTint else CreamWhite

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("mood_card_${mood.name.lowercase()}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(listOf(borderColor, borderColor))
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val icon: ImageVector = when (mood) {
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
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(mood.accentColor.copy(alpha = if (selected) 0.25f else 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = mood.title,
                    tint = mood.accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = mood.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (selected) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected",
                        tint = mood.accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = mood.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = WarmGrey,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun TagPill(
    label: String,
    selected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (selected) activeColor.copy(alpha = 0.2f) else PaleVanilla,
        animationSpec = tween(200),
        label = "tagBg"
    )
    val borderColor = if (selected) activeColor else MutedBeige

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bgColor)
            .border(1.dp, borderColor, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("tag_pill_$label")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(activeColor)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) SoftCharcoal else SoftCharcoal.copy(alpha = 0.85f)
            )
        }
    }
}
