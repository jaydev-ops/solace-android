package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.theme.ButteryYellow
import com.example.ui.theme.CreamWhite
import com.example.ui.theme.LocalMoodColors
import com.example.ui.theme.MutedBeige
import com.example.ui.theme.PaleVanilla
import com.example.ui.theme.SoftButter
import com.example.ui.theme.SoftCharcoal
import com.example.ui.theme.WarmGrey
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioTranscriptionModal(
    isRecording: Boolean,
    isTranscribing: Boolean,
    onStartRecording: () -> Boolean,
    onStopRecordingAndTranscribe: (onSuccess: (String) -> Unit, onError: (String) -> Unit) -> Unit,
    onCancelRecording: () -> Unit,
    onUseTranscription: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val moodColors = LocalMoodColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var transcribedResult by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var recordingSeconds by remember { mutableIntStateOf(0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasAudioPermission = granted
        if (granted) {
            val started = onStartRecording()
            if (!started) errorMessage = "Unable to start microphone recording"
        } else {
            errorMessage = "Microphone permission is needed to record voice reflections"
        }
    }

    // Pulse animation while recording
    val pulseScale = remember { Animatable(1f) }
    LaunchedEffect(isRecording) {
        if (isRecording) {
            pulseScale.animateTo(
                targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            pulseScale.snapTo(1f)
        }
    }

    // Recording duration timer
    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingSeconds = 0
            while (isRecording) {
                delay(1000L)
                recordingSeconds++
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            if (isRecording) onCancelRecording()
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("audio_transcription_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Voice Sanctuary Transcription",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Transcribing with gemini-3.5-transcribe",
                        style = MaterialTheme.typography.bodySmall,
                        color = moodColors.primary
                    )
                }

                IconButton(
                    onClick = {
                        if (isRecording) onCancelRecording()
                        onDismiss()
                    }
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = WarmGrey)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // State 1: Transcribed Text Ready
            if (transcribedResult != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PaleVanilla),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.verticalGradient(listOf(SoftButter, MutedBeige)))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = moodColors.primary, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Transcribed via gemini-3.5-transcribe",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = moodColors.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = transcribedResult.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = SoftCharcoal,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            transcribedResult = null
                            errorMessage = null
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Record Again")
                    }

                    Button(
                        onClick = {
                            transcribedResult?.let { onUseTranscription(it) }
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButteryYellow,
                            contentColor = SoftCharcoal
                        )
                    ) {
                        Text("Use in Reflection", fontWeight = FontWeight.Bold)
                    }
                }
            }
            // State 2: Transcribing in progress
            else if (isTranscribing) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = moodColors.primary,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Transcribing audio with gemini-3.5-transcribe...",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Listening to nuances and punctuation",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmGrey
                    )
                }
            }
            // State 3: Recording or Ready to Record
            else {
                // Microphone Visual Indicator
                Box(
                    modifier = Modifier.size(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isRecording) {
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .scale(pulseScale.value)
                                .clip(CircleShape)
                                .background(moodColors.primary.copy(alpha = 0.2f))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(if (isRecording) moodColors.primary else PaleVanilla)
                            .border(2.dp, if (isRecording) moodColors.primary else MutedBeige, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                            tint = if (isRecording) Color.White else SoftCharcoal,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (isRecording) {
                    Text(
                        text = "Listening... ${recordingSeconds}s",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = moodColors.primary
                    )
                    Text(
                        text = "Speak freely. Your voice is private.",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmGrey
                    )
                } else {
                    Text(
                        text = "Tap to speak your unhurried thoughts",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "solace will transcribe your voice using gemini-3.5-transcribe",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmGrey,
                        textAlign = TextAlign.Center
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                if (isRecording) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onCancelRecording() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                onStopRecordingAndTranscribe(
                                    { transcript -> transcribedResult = transcript },
                                    { err -> errorMessage = err }
                                )
                            },
                            modifier = Modifier.weight(1.5f),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = moodColors.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Stop & Transcribe", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            if (!hasAudioPermission) {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            } else {
                                errorMessage = null
                                val started = onStartRecording()
                                if (!started) errorMessage = "Failed to start audio recording"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_voice_recording_button"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButteryYellow,
                            contentColor = SoftCharcoal
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Mic, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Begin Voice Recording", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
