package com.example.ui.screens.companion

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.ai.ChatbotRole
import com.example.data.ai.GeminiChatModel
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.model.RegulationMode
import com.example.ui.components.AudioTranscriptionModal
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
fun CompanionScreen(
    userName: String,
    messages: List<ChatMessageEntity>,
    activeMode: RegulationMode,
    isThinking: Boolean,
    onSelectMode: (RegulationMode) -> Unit,
    onSendMessage: (String) -> Unit,
    onOpenBreathingExercise: () -> Unit,
    onOpenSettings: () -> Unit,
    selectedModel: GeminiChatModel = GeminiChatModel.FLASH,
    selectedRole: ChatbotRole = ChatbotRole.SANCTUARY_COMPANION,
    onSelectModel: (GeminiChatModel) -> Unit = {},
    onSelectRole: (ChatbotRole) -> Unit = {},
    isRecordingAudio: Boolean = false,
    isTranscribingAudio: Boolean = false,
    onStartRecordingAudio: () -> Boolean = { false },
    onStopRecordingAndTranscribe: (onSuccess: (String) -> Unit, onError: (String) -> Unit) -> Unit = { _, _ -> },
    onCancelRecordingAudio: () -> Unit = {}
) {
    val moodColors = LocalMoodColors.current
    var inputText by remember { mutableStateOf("") }
    var showAudioModal by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .testTag("companion_screen")
    ) {
        SolaceTopBar(
            subtitle = "peaceful haven",
            userName = userName,
            onAvatarClick = onOpenSettings
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Sanctuary Badge
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PaleVanilla),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SoftButter, MutedBeige)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(ButteryYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = SoftCharcoal,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "solace AI",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SoftCharcoal
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SoftButter)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "SANCTUARY",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftCharcoal,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(moodColors.primary)
                            )
                            Text(
                                text = "Powered by ${selectedModel.modelId} • ${selectedRole.title}",
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmGrey,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Gemini Model Selector (gemini-3.8-flash, gemini-4-argon, gemini-3.1-flash-lite)
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Gemini Model Engine",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Task-specific intelligence",
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmGrey
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        items(GeminiChatModel.entries) { model ->
                            val isSelected = model == selectedModel
                            val bgColor = if (isSelected) ButteryYellow else CreamWhite
                            val borderColor = if (isSelected) SoftCharcoal.copy(alpha = 0.3f) else MutedBeige

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = bgColor,
                                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onSelectModel(model) }
                                    .testTag("model_chip_${model.name.lowercase()}")
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = model.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = SoftCharcoal
                                    )
                                    Text(
                                        text = model.description,
                                        fontSize = 9.sp,
                                        color = WarmGrey
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Chatbot Role System Instruction Selector
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chatbot Role & Focus",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "System role calibration",
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmGrey
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        items(ChatbotRole.entries) { role ->
                            val isSelected = role == selectedRole
                            val bgColor = if (isSelected) SoftButter else CreamWhite
                            val borderColor = if (isSelected) moodColors.primary else MutedBeige

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = bgColor,
                                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onSelectRole(role) }
                                    .testTag("role_chip_${role.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = when (role) {
                                            ChatbotRole.SANCTUARY_COMPANION -> Icons.Default.Spa
                                            ChatbotRole.SOMATIC_GUIDE -> Icons.Default.SelfImprovement
                                            ChatbotRole.THOUGHT_CLARIFIER -> Icons.Default.Psychology
                                            ChatbotRole.WISDOM_MIRROR -> Icons.Default.AutoAwesome
                                        },
                                        contentDescription = null,
                                        tint = if (isSelected) moodColors.primary else WarmGrey,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = role.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = SoftCharcoal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Atmosphere & Mode calibration ribbon
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Atmosphere & Mode",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Gentle calibration",
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmGrey
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        items(RegulationMode.entries) { mode ->
                            val isSelected = mode == activeMode
                            val bgColor = if (isSelected) ButteryYellow else CreamWhite
                            val borderColor = if (isSelected) SoftCharcoal.copy(alpha = 0.3f) else MutedBeige

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(bgColor)
                                    .border(1.dp, borderColor, RoundedCornerShape(20.dp))
                                    .clickable { onSelectMode(mode) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                                    .testTag("mode_tab_${mode.name.lowercase()}"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = mode.iconEmoji, fontSize = 14.sp)
                                Text(
                                    text = mode.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = SoftCharcoal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mode description banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(PaleVanilla)
                            .border(1.dp, MutedBeige, RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = moodColors.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Mode: ${activeMode.title} — ${activeMode.description}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftCharcoal,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Message Thread
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    userName = userName,
                    onOpenBreathing = onOpenBreathingExercise
                )
            }

            // Typing / Thinking Indicator
            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = moodColors.primary,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "solace is reflecting with ${selectedModel.displayName}...",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmGrey,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Starters / Gentle Suggestions
            if (messages.size <= 2) {
                item {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = "Gently begin with...",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = WarmGrey
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        activeMode.starterPrompts.forEach { prompt ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(CreamWhite)
                                    .border(1.dp, MutedBeige, RoundedCornerShape(14.dp))
                                    .clickable { onSendMessage(prompt) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "\" $prompt \"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoftCharcoal
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = moodColors.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Composer Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Microphone Voice Input Button
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isRecordingAudio) moodColors.primary else PaleVanilla)
                            .border(1.dp, MutedBeige, CircleShape)
                            .clickable { showAudioModal = true }
                            .testTag("companion_mic_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Mic,
                            contentDescription = "Voice transcription (gemini-3.5-transcribe)",
                            tint = if (isRecordingAudio) Color.White else SoftCharcoal,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = activeMode.promptPlaceholder,
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmGrey.copy(alpha = 0.7f),
                                maxLines = 1
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("companion_input_field"),
                        shape = RoundedCornerShape(26.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PaleVanilla,
                            unfocusedContainerColor = PaleVanilla,
                            focusedBorderColor = moodColors.primary,
                            unfocusedBorderColor = MutedBeige
                        ),
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                onSendMessage(inputText)
                                inputText = ""
                            }
                        },
                        enabled = inputText.isNotBlank(),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (inputText.isNotBlank()) ButteryYellow else MutedBeige)
                            .testTag("companion_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send message",
                            tint = if (inputText.isNotBlank()) SoftCharcoal else WarmGrey,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = WarmGrey.copy(alpha = 0.6f),
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Private sanctuary • Multi-turn Gemini • Mic transcribed by gemini-3.5-transcribe",
                        fontSize = 10.sp,
                        color = WarmGrey.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

    // Audio Transcription Modal
    if (showAudioModal) {
        AudioTranscriptionModal(
            isRecording = isRecordingAudio,
            isTranscribing = isTranscribingAudio,
            onStartRecording = onStartRecordingAudio,
            onStopRecordingAndTranscribe = onStopRecordingAndTranscribe,
            onCancelRecording = onCancelRecordingAudio,
            onUseTranscription = { transcript ->
                inputText = transcript
            },
            onDismiss = { showAudioModal = false }
        )
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessageEntity,
    userName: String,
    onOpenBreathing: () -> Unit
) {
    val isUser = message.senderRole == "user"
    val moodColors = LocalMoodColors.current
    val formattedTime = remember(message.timestamp) {
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(message.timestamp))
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            if (!isUser) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = moodColors.primary,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "solace AI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SoftCharcoal
                )
            }
            Text(
                text = formattedTime,
                fontSize = 10.sp,
                color = WarmGrey
            )
            if (isUser) {
                Text(
                    text = "You",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SoftCharcoal
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Message body bubble
        Box(
            modifier = Modifier
                .widthIn(max = 310.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isUser) 20.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 20.dp
                    )
                )
                .background(if (isUser) ButteryYellow else CreamWhite)
                .border(
                    1.dp,
                    if (isUser) SoftCharcoal.copy(alpha = 0.15f) else MutedBeige,
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isUser) 20.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 20.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = SoftCharcoal,
                lineHeight = 22.sp
            )
        }

        // Context anchor pill
        if (!message.contextAnchor.isNullOrBlank() && !isUser) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(PaleVanilla)
                    .border(1.dp, MutedBeige, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Spa,
                    contentDescription = null,
                    tint = moodColors.primary,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = message.contextAnchor,
                    fontSize = 10.sp,
                    color = SoftCharcoal
                )
            }
        }
    }
}
