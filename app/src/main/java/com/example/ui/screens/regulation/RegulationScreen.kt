package com.example.ui.screens.regulation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CuratedRegulationActivities
import com.example.data.model.RegulationActivity
import com.example.ui.theme.LocalMoodColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegulationScreen(
    initialActivityId: String? = null,
    onBack: () -> Unit,
    onCompleteSession: (activity: RegulationActivity, rating: Int, note: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val moodColors = LocalMoodColors.current
    var selectedActivity by remember {
        mutableStateOf(
            CuratedRegulationActivities.firstOrNull { it.id == initialActivityId }
                ?: CuratedRegulationActivities.first()
        )
    }

    var showCompletionDialog by remember { mutableStateOf(false) }
    var sessionDurationRecorded by remember { mutableIntStateOf(180) }

    Scaffold(
        modifier = modifier.testTag("regulation_screen"),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Somatic Sanctuary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Gentle nervous system regulation",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("regulation_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to home",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Activity category pills selector
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(CuratedRegulationActivities) { activity ->
                    val isSelected = activity.id == selectedActivity.id
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) moodColors.primary else MaterialTheme.colorScheme.surface,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { selectedActivity = activity }
                            .testTag("activity_pill_${activity.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = activity.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color(0xFF1E211A) else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${activity.durationSeconds / 60}m",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Color(0xFF4A4B43) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Exercise Workspace
            if (selectedActivity.id == "box_breathing" || selectedActivity.category.equals("Breathing", ignoreCase = true)) {
                BoxBreathingPlayer(
                    activity = selectedActivity,
                    onFinish = { durationSec ->
                        sessionDurationRecorded = durationSec
                        showCompletionDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                )
            } else if (selectedActivity.id == "grounding_54321") {
                SensoryGroundingPlayer(
                    activity = selectedActivity,
                    onFinish = { durationSec ->
                        sessionDurationRecorded = durationSec
                        showCompletionDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                )
            } else {
                GenericRegulationPlayer(
                    activity = selectedActivity,
                    onFinish = { durationSec ->
                        sessionDurationRecorded = durationSec
                        showCompletionDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                )
            }
        }
    }

    if (showCompletionDialog) {
        RegulationCompletionDialog(
            activityTitle = selectedActivity.title,
            durationSeconds = sessionDurationRecorded,
            onDismiss = { showCompletionDialog = false },
            onSubmitRating = { rating ->
                showCompletionDialog = false
                onCompleteSession(selectedActivity, rating, "Completed in solace sanctuary")
                onBack()
            }
        )
    }
}

/**
 * Animated Box Breathing (4s Inhale, 4s Hold, 4s Exhale, 4s Hold)
 */
@Composable
private fun BoxBreathingPlayer(
    activity: RegulationActivity,
    onFinish: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val moodColors = LocalMoodColors.current
    var isRunning by remember { mutableStateOf(false) }
    var phaseIndex by remember { mutableIntStateOf(0) }
    var secondsInPhase by remember { mutableIntStateOf(4) }
    var totalSecondsElapsed by remember { mutableIntStateOf(0) }

    val phases = listOf(
        "Breathe In" to 4,
        "Gently Hold" to 4,
        "Release Slowly" to 4,
        "Rest in Stillness" to 4
    )

    val scaleAnim = remember { Animatable(1f) }

    // Breathing loop
    LaunchedEffect(isRunning, phaseIndex) {
        if (!isRunning) return@LaunchedEffect
        val targetScale = when (phaseIndex) {
            0 -> 1.35f // Inhale expands
            1 -> 1.35f // Hold retains
            2 -> 1.0f  // Exhale contracts
            3 -> 1.0f  // Hold relaxed
            else -> 1.0f
        }
        val animDuration = if (phaseIndex == 0 || phaseIndex == 2) 4000 else 200
        scaleAnim.animateTo(
            targetValue = targetScale,
            animationSpec = tween(durationMillis = animDuration, easing = LinearEasing)
        )
    }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            totalSecondsElapsed++
            if (secondsInPhase > 1) {
                secondsInPhase--
            } else {
                phaseIndex = (phaseIndex + 1) % phases.size
                secondsInPhase = phases[phaseIndex].second
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Description Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Circular Breathing Animation Center
        Box(
            modifier = Modifier
                .size(280.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer pulse circle
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(scaleAnim.value)
                    .clip(CircleShape)
                    .background(moodColors.primary.copy(alpha = 0.22f))
            )

            // Middle soft glowing aura
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .scale(scaleAnim.value * 0.92f)
                    .clip(CircleShape)
                    .background(moodColors.primary.copy(alpha = 0.45f))
            )

            // Inner core
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(2.dp, moodColors.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isRunning) phases[phaseIndex].first else "Ready",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    if (isRunning) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${secondsInPhase}s",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = moodColors.primary
                        )
                    }
                }
            }
        }

        // Timer info and Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total elapsed: ${totalSecondsElapsed / 60}m ${totalSecondsElapsed % 60}s",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { isRunning = !isRunning },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = moodColors.primary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .testTag("regulation_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Close else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Start"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRunning) "Pause Pace" else "Begin Breathing",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                if (totalSecondsElapsed >= 15) {
                    FilledTonalButton(
                        onClick = { onFinish(totalSecondsElapsed) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .height(54.dp)
                            .testTag("regulation_finish_button")
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Complete")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Complete", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

/**
 * 5-4-3-2-1 Sensory Grounding interactive checklist
 */
@Composable
private fun SensoryGroundingPlayer(
    activity: RegulationActivity,
    onFinish: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val moodColors = LocalMoodColors.current
    var currentStep by remember { mutableIntStateOf(0) }
    val completedSteps = remember { mutableStateListOf(false, false, false, false, false) }
    var secondsElapsed by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            secondsElapsed++
        }
    }

    val steps = listOf(
        "5 Things You Can See" to "Look around your space right now. Notice 5 distinct visual details: light reflection, textures, a corner, an object's hue.",
        "4 Things You Can Feel" to "Bring attention to physical sensations: feet on the ground, clothes against your skin, back resting on the chair, cool breeze.",
        "3 Things You Can Hear" to "Listen for subtle auditory cues: distant traffic, computer fan hum, bird chirp, your own gentle breathing.",
        "2 Things You Can Smell" to "Notice ambient scent: fresh air, coffee, paper, or simply take an unhurried conscious inhale.",
        "1 Thing You Can Taste" to "Notice any lingering taste: water, mint, tea, or simply rest your tongue against the roof of your mouth."
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = activity.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Gently anchor into the physical environment to interrupt looping rumination.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            items(steps.indices.toList()) { index ->
                val (title, hint) = steps[index]
                val isDone = completedSteps[index]
                val isCurrent = index == currentStep

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDone) {
                            moodColors.backgroundTint.copy(alpha = 0.5f)
                        } else if (isCurrent) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        }
                    ),
                    border = if (isCurrent) androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        moodColors.primary
                    ) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            completedSteps[index] = !completedSteps[index]
                            if (completedSteps[index] && index < steps.size - 1) {
                                currentStep = index + 1
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        IconButton(
                            onClick = {
                                completedSteps[index] = !completedSteps[index]
                                if (completedSteps[index] && index < steps.size - 1) {
                                    currentStep = index + 1
                                }
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.Refresh,
                                contentDescription = if (isDone) "Completed step" else "Mark done",
                                tint = if (isDone) moodColors.primary else MaterialTheme.colorScheme.outlineVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hint,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // Bottom CTA
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val allDone = completedSteps.all { it }
            Button(
                onClick = { onFinish(secondsElapsed.coerceAtLeast(30)) },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = moodColors.primary,
                    contentColor = Color(0xFF1E211A)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("grounding_complete_button")
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (allDone) "Complete Grounding Sanctuary" else "Complete Sanctuary Session",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Generic Somatic/Regulation Step-by-Step Player
 */
@Composable
private fun GenericRegulationPlayer(
    activity: RegulationActivity,
    onFinish: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val moodColors = LocalMoodColors.current
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var currentStepIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            secondsElapsed++
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = activity.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activity.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = moodColors.backgroundTint.copy(alpha = 0.35f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, moodColors.primary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Step ${currentStepIndex + 1} of ${activity.instructions.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = moodColors.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = activity.instructions.getOrElse(currentStepIndex) { "" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 26.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation between steps
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { if (currentStepIndex > 0) currentStepIndex-- },
                    enabled = currentStepIndex > 0,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Previous")
                }

                Button(
                    onClick = {
                        if (currentStepIndex < activity.instructions.size - 1) {
                            currentStepIndex++
                        }
                    },
                    enabled = currentStepIndex < activity.instructions.size - 1,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = moodColors.primary,
                        contentColor = Color(0xFF1E211A)
                    )
                ) {
                    Text("Next Step")
                }
            }
        }

        // Completion bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { onFinish(secondsElapsed.coerceAtLeast(30)) },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = moodColors.primary,
                    contentColor = Color(0xFF1E211A)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("generic_regulation_finish")
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Complete Sanctuary Session", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/**
 * Completion dialog asking for gentle reflection rating
 */
@Composable
private fun RegulationCompletionDialog(
    activityTitle: String,
    durationSeconds: Int,
    onDismiss: () -> Unit,
    onSubmitRating: (Int) -> Unit
) {
    val moodColors = LocalMoodColors.current
    var selectedRating by remember { mutableIntStateOf(5) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("regulation_completion_dialog")
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(moodColors.primary.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = moodColors.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Gentle Pause Completed",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "You devoted ${durationSeconds / 60}m ${durationSeconds % 60}s to caring for your nervous system.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "How did this practice feel?",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (1..5).forEach { star ->
                        IconButton(
                            onClick = { selectedRating = star },
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$star stars",
                                tint = if (star <= selectedRating) {
                                    moodColors.primary
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant
                                },
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Skip")
                    }

                    Button(
                        onClick = { onSubmitRating(selectedRating) },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = moodColors.primary,
                            contentColor = Color(0xFF1E211A)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Note")
                    }
                }
            }
        }
    }
}
