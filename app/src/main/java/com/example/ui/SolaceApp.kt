package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.firebase.onGoogleSignInClicked
import com.example.data.firebase.signOut
import com.example.navigation.Screen
import com.example.ui.components.AmbientSanctuaryCanvas
import com.example.ui.components.MemoryPermissionDialog
import com.example.ui.components.SolaceBottomNav
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.chat.ChatScreen
import com.example.ui.screens.checkin.CheckInScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.insights.InsightsScreen
import com.example.ui.screens.journal.JournalScreen
import com.example.ui.screens.regulation.RegulationScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.theme.LocalMoodColors
import com.example.ui.theme.SolaceTheme
import com.example.ui.viewmodel.ChatViewModel
import com.example.ui.viewmodel.SolaceViewModel

@Composable
fun SolaceApp(
    viewModel: SolaceViewModel,
    chatViewModel: ChatViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    val activeMoodTheme by viewModel.activeMoodTheme.collectAsState()
    val userPreferences by viewModel.userPreferences.collectAsState()
    val latestMood by viewModel.latestMood.collectAsState()
    val allMoods by viewModel.allMoods.collectAsState()
    val allJournals by viewModel.allJournals.collectAsState()
    val allMessages by viewModel.allMessages.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()
    val currentRegulationMode by viewModel.currentRegulationMode.collectAsState()

    val currentUser by viewModel.currentUser.collectAsState()
    val selectedGeminiModel by viewModel.selectedGeminiModel.collectAsState()
    val selectedChatbotRole by viewModel.selectedChatbotRole.collectAsState()
    val isRecordingAudio by viewModel.isRecordingAudio.collectAsState()
    val isTranscribingAudio by viewModel.isTranscribingAudio.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var selectedRegulationActivityId by remember { mutableStateOf<String?>("box_breathing") }

    // Sub-screen Back navigation
    BackHandler(enabled = currentScreen != Screen.Home) {
        currentScreen = Screen.Home
    }

    SolaceTheme(
        activeMood = activeMoodTheme,
        darkTheme = userPreferences.isDarkMode
    ) {
        val moodColors = LocalMoodColors.current

        AmbientSanctuaryCanvas(
            activeMood = activeMoodTheme,
            modifier = modifier.fillMaxSize()
        ) {
            val isBottomNavVisible = when (currentScreen) {
                Screen.Home,
                Screen.CheckIn,
                Screen.Companion,
                Screen.Journal,
                Screen.Insights -> true
                Screen.Regulation,
                Screen.Settings,
                Screen.Auth -> false
            }

            Scaffold(
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                bottomBar = {
                    AnimatedVisibility(
                        visible = isBottomNavVisible,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        SolaceBottomNav(
                            currentScreen = currentScreen,
                            onNavigate = { destination ->
                                currentScreen = destination
                            }
                        )
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                ) {
                    Crossfade(
                        targetState = currentScreen,
                        label = "ScreenCrossfade"
                    ) { targetScreen ->
                        when (targetScreen) {
                            Screen.Auth -> {
                                AuthScreen(
                                    onLoginSuccess = { currentScreen = Screen.Home },
                                    onLoginWithPassword = { email, password ->
                                        viewModel.loginWithPassword(email, password)
                                    },
                                    onRegisterWithPassword = { name, email, password ->
                                        viewModel.registerWithPassword(name, email, password)
                                    },
                                    onContinueAsGuest = {
                                        viewModel.continueAsGuest()
                                        currentScreen = Screen.Home
                                    }
                                )
                            }

                            Screen.Home -> {
                                HomeScreen(
                                    userName = userPreferences.userName,
                                    latestMood = latestMood,
                                    weeklyMoods = allMoods,
                                    onQuickCheckIn = { mood ->
                                        viewModel.saveMoodCheckIn(
                                            mood = mood.title,
                                            valence = mood.defaultValence,
                                            intensity = 4,
                                            tags = listOf("Morning quiet"),
                                            note = ""
                                        )
                                    },
                                    onQuickMoodPreview = { moodTitle ->
                                        viewModel.setPreviewMood(moodTitle)
                                    },
                                    onNavigate = { screen ->
                                        if (screen == Screen.Regulation) {
                                            selectedRegulationActivityId = "box_breathing"
                                        }
                                        currentScreen = screen
                                    },
                                    onOpenSettings = { currentScreen = Screen.Settings }
                                )
                            }

                            Screen.CheckIn -> {
                                CheckInScreen(
                                    userName = userPreferences.userName,
                                    onSaveCheckIn = { mood, valence, intensity, tags, note ->
                                        viewModel.saveMoodCheckIn(mood, valence, intensity, tags, note)
                                        currentScreen = Screen.Home
                                    },
                                    onMoodPreview = { moodTitle ->
                                        viewModel.setPreviewMood(moodTitle)
                                    },
                                    onOpenSettings = { currentScreen = Screen.Settings }
                                )
                            }

                            Screen.Companion -> {
                                ChatScreen(
                                    viewModel = chatViewModel,
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }

                            Screen.Journal -> {
                                JournalScreen(
                                    userName = userPreferences.userName,
                                    entries = allJournals,
                                    onSaveEntry = { title, content, associatedMood, tags ->
                                        viewModel.saveJournal(
                                            title = title,
                                            content = content,
                                            associatedMood = associatedMood,
                                            tags = tags
                                        )
                                    },
                                    onDeleteEntry = { id ->
                                        viewModel.deleteJournal(id)
                                    },
                                    onOpenSettings = { currentScreen = Screen.Settings }
                                )
                            }

                            Screen.Insights -> {
                                InsightsScreen(
                                    userName = userPreferences.userName,
                                    moods = allMoods,
                                    onStartRegulation = { activity ->
                                        selectedRegulationActivityId = activity.id
                                        viewModel.selectRegulationActivity(activity)
                                        currentScreen = Screen.Regulation
                                    },
                                    onOpenSettings = { currentScreen = Screen.Settings }
                                )
                            }

                            Screen.Regulation -> {
                                RegulationScreen(
                                    initialActivityId = selectedRegulationActivityId,
                                    onBack = { currentScreen = Screen.Home },
                                    onCompleteSession = { activity, rating, note ->
                                        viewModel.saveRegulationSession(activity, rating, note)
                                    }
                                )
                            }

                            Screen.Settings -> {
                                SettingsScreen(
                                    userPreferences = userPreferences,
                                    onUpdateName = { name ->
                                        viewModel.updateUserName(name)
                                    },
                                    onUpdatePinnedTheme = { pinned ->
                                        viewModel.setPinnedTheme(pinned)
                                    },
                                    onToggleDarkMode = { isDark ->
                                        viewModel.toggleDarkMode(isDark)
                                    },
                                    onToggleAiConsent = { consent ->
                                        viewModel.toggleAiConsent(consent)
                                    },
                                    onToggleMemorySaving = { allowed ->
                                        viewModel.setMemorySavingPermission(allowed)
                                    },
                                    onSaveCustomGeminiApiKey = { key ->
                                        viewModel.saveCustomGeminiApiKey(key)
                                    },
                                    onExportDataJson = {
                                        viewModel.exportDataJson()
                                    },
                                    onClearAllData = {
                                        viewModel.clearAllData()
                                        currentScreen = Screen.Home
                                    },
                                    onBack = { currentScreen = Screen.Home },
                                    currentUser = currentUser,
                                    onSignInWithGoogle = {
                                        onGoogleSignInClicked(
                                            context = context,
                                            credentialManager = credentialManager,
                                            onAuthSuccess = { /* updated via AuthListener */ },
                                            onAuthError = { /* logged */ },
                                            scope = coroutineScope
                                        )
                                    },
                                    onSignOut = {
                                        viewModel.logout()
                                        signOut(
                                            context = context,
                                            credentialManager = credentialManager,
                                            onSignOutComplete = { /* updated via AuthListener */ },
                                            scope = coroutineScope
                                        )
                                    },
                                    onNavigateToAuth = { currentScreen = Screen.Auth }
                                )
                            }
                        }
                    }
                }
            }

            // Memory Permission Prompt (Asks user permission to save memories)
            if (!userPreferences.memorySavingPermissionAsked) {
                MemoryPermissionDialog(
                    onAllow = {
                        viewModel.setMemorySavingPermission(true)
                    },
                    onDeny = {
                        viewModel.setMemorySavingPermission(false)
                    }
                )
            }
        }
    }
}
