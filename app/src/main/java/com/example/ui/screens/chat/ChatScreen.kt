package com.example.ui.screens.chat

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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.ChatbotRole
import com.example.data.ai.GeminiChatModel
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.safety.RiskLevel
import com.example.ui.components.AudioTranscriptionModal
import com.example.ui.components.EmergencySupportCard
import com.example.ui.components.SafetyScreeningSupportBanner
import com.example.ui.theme.ButteryYellow
import com.example.ui.theme.CreamWhite
import com.example.ui.theme.LocalMoodColors
import com.example.ui.theme.MutedBeige
import com.example.ui.theme.PaleVanilla
import com.example.ui.theme.SoftButter
import com.example.ui.theme.SoftCharcoal
import com.example.ui.theme.WarmGrey
import com.example.ui.viewmodel.ChatUiState
import com.example.ui.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.messages.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()
    val selectedRole by viewModel.selectedRole.collectAsState()
    val isRecordingAudio by viewModel.isRecordingAudio.collectAsState()
    val isTranscribingAudio by viewModel.isTranscribingAudio.collectAsState()
    val userPreferences by viewModel.userPreferences.collectAsState()

    val currentHour = remember {
        java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    }
    val timeGreeting = remember(currentHour) {
        when {
            currentHour in 4..11 -> "Good morning"
            currentHour in 12..16 -> "Good afternoon"
            else -> "Good evening"
        }
    }
    val greetingHeading = remember(timeGreeting, userPreferences.userName) {
        val clean = userPreferences.userName.trim()
        val isValid = clean.isNotBlank() &&
            !clean.equals("Sanctuary", ignoreCase = true) &&
            !clean.equals("User", ignoreCase = true) &&
            !clean.equals("Guest", ignoreCase = true) &&
            !clean.equals("Sanctuary Guest", ignoreCase = true)
        if (isValid) "$timeGreeting, $clean" else timeGreeting
    }

    var inputText by remember { mutableStateOf("") }
    var showAudioModal by remember { mutableStateOf(false) }
    var showEmergencySheet by remember { mutableStateOf(false) }
    val riskState by viewModel.riskState.collectAsState()
    val listState = rememberLazyListState()
    val moodColors = LocalMoodColors.current
    val isImeVisible = WindowInsets.isImeVisible

    // Auto-scroll when new messages arrive, when streaming, or when keyboard opens
    LaunchedEffect(messages.size, uiState, isImeVisible) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("chat_screen"),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Gemini AI Chat",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SoftButter)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = selectedModel.modelId,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftCharcoal
                                )
                            }
                        }
                        Text(
                            text = "Role: ${selectedRole.title}",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmGrey,
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("chat_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.clearChat() },
                        modifier = Modifier.testTag("chat_clear_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear Chat",
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
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
                .statusBarsPadding()
                .imePadding()
        ) {
            // Model Selector Strip
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Gemini Model Engine",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = WarmGrey
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(GeminiChatModel.entries) { model ->
                        val isSelected = model == selectedModel
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) ButteryYellow else CreamWhite,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SoftCharcoal.copy(alpha = 0.3f) else MutedBeige
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { viewModel.selectModel(model) }
                                .testTag("chat_model_${model.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) moodColors.primary else WarmGrey)
                                )
                                Text(
                                    text = model.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = SoftCharcoal
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Chatbot System Role Strip
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(ChatbotRole.entries) { role ->
                        val isSelected = role == selectedRole
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) SoftButter else CreamWhite,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) moodColors.primary else MutedBeige
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.selectRole(role) }
                                .testTag("chat_role_${role.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = role.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 11.sp,
                                    color = SoftCharcoal
                                )
                            }
                        }
                    }
                }
            }

            // Message Conversation History in a LazyColumn
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("chat_message_list"),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = PaleVanilla)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = moodColors.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = greetingHeading,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftCharcoal
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Welcome to your solace AI sanctuary. Send a message or tap the microphone to begin a new conversation.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = WarmGrey,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                // Historical messages
                items(messages, key = { it.id }) { message ->
                    ChatMessageBubble(message = message)
                }

                // Level 4 Emergency Support Card / Level 2-3 Screening Banner
                if (riskState.currentLevel == RiskLevel.LEVEL_4_CRITICAL || riskState.emergencyTriggered) {
                    item {
                        EmergencySupportCard(
                            country = "IN",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                } else if (riskState.currentLevel == RiskLevel.LEVEL_3_SUICIDAL_IDEATION || riskState.currentLevel == RiskLevel.LEVEL_2_ELEVATED) {
                    item {
                        SafetyScreeningSupportBanner(
                            riskLevel = riskState.currentLevel,
                            onShowResources = { showEmergencySheet = true },
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }

                // Streaming partial response
                if (uiState is ChatUiState.Streaming) {
                    val partial = (uiState as ChatUiState.Streaming).partialResponse
                    item {
                        StreamingMessageBubble(
                            text = partial,
                            modelName = selectedModel.displayName
                        )
                    }
                }

                // Loading spinner
                if (uiState is ChatUiState.Loading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = moodColors.primary
                            )
                            Text(
                                text = "Gemini is reflecting...",
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmGrey,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Error Banner
                if (uiState is ChatUiState.Error) {
                    val errorMsg = (uiState as ChatUiState.Error).message
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Notice: $errorMsg",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Composer Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Microphone Voice Input
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isRecordingAudio) moodColors.primary else PaleVanilla)
                            .border(1.dp, MutedBeige, CircleShape)
                            .clickable { showAudioModal = true }
                            .testTag("chat_mic_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Input",
                            tint = if (isRecordingAudio) Color.White else SoftCharcoal,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = "Type a message to Gemini AI...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmGrey.copy(alpha = 0.7f)
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("chat_input_field"),
                        shape = RoundedCornerShape(26.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PaleVanilla,
                            unfocusedContainerColor = PaleVanilla,
                            focusedBorderColor = moodColors.primary,
                            unfocusedBorderColor = MutedBeige
                        ),
                        maxLines = 3,
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendMessage(inputText)
                                inputText = ""
                            }
                        },
                        enabled = inputText.isNotBlank(),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (inputText.isNotBlank()) ButteryYellow else MutedBeige)
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Message",
                            tint = if (inputText.isNotBlank()) SoftCharcoal else WarmGrey,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    if (showAudioModal) {
        AudioTranscriptionModal(
            isRecording = isRecordingAudio,
            isTranscribing = isTranscribingAudio,
            onStartRecording = { viewModel.startAudioRecording() },
            onStopRecordingAndTranscribe = { onSuccess, onError ->
                viewModel.stopAudioRecordingAndTranscribe(onSuccess, onError)
            },
            onCancelRecording = { viewModel.cancelAudioRecording() },
            onUseTranscription = { transcript ->
                inputText = transcript
            },
            onDismiss = { showAudioModal = false }
        )
    }

    if (showEmergencySheet) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showEmergencySheet = false }) {
            EmergencySupportCard(
                country = "IN",
                onContactTrustedPerson = null
            )
        }
    }
}

@Composable
private fun ChatMessageBubble(message: ChatMessageEntity) {
    val isUser = message.senderRole == "user"
    val moodColors = LocalMoodColors.current
    val formattedTime = remember(message.timestamp) {
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(message.timestamp))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(if (isUser) "user_message_bubble" else "model_message_bubble"),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            if (!isUser) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = moodColors.primary,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "Gemini",
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

        Box(
            modifier = Modifier
                .widthIn(max = 310.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .background(if (isUser) ButteryYellow else CreamWhite)
                .border(
                    1.dp,
                    if (isUser) SoftCharcoal.copy(alpha = 0.15f) else MutedBeige,
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = SoftCharcoal,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun StreamingMessageBubble(text: String, modelName: String) {
    val moodColors = LocalMoodColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("streaming_message_bubble"),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = moodColors.primary,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = "$modelName (streaming)",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = SoftCharcoal
            )
        }

        Box(
            modifier = Modifier
                .widthIn(max = 310.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 18.dp
                    )
                )
                .background(CreamWhite)
                .border(1.dp, moodColors.primary.copy(alpha = 0.5f), RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = SoftCharcoal,
                lineHeight = 20.sp
            )
        }
    }
}
