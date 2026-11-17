package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LocalMoodColors
import com.example.ui.theme.LocalSolaceColors
import com.example.ui.theme.MutedBeige
import com.example.ui.theme.SolaceColors
import com.example.ui.theme.WarmGrey
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    onLoginWithPassword: suspend (String, String) -> Result<String>,
    onRegisterWithPassword: suspend (String, String, String) -> Result<String>,
    onContinueAsGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val moodColors = LocalMoodColors.current
    val solaceColors = LocalSolaceColors.current

    var showAuthForm by remember { mutableStateOf(false) }
    var isSignUp by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    fun handleSubmit() {
        focusManager.clearFocus()
        errorMessage = null
        successMessage = null

        val cleanEmail = email.trim()
        val cleanPassword = password.trim()

        if (cleanEmail.isBlank()) {
            errorMessage = "Please enter your email"
            return
        }
        if (cleanPassword.length < 6) {
            errorMessage = "Password must be at least 6 characters"
            return
        }

        if (isSignUp) {
            val cleanName = name.trim()
            if (cleanName.isBlank()) {
                errorMessage = "Please enter your name"
                return
            }
            if (cleanPassword != confirmPassword.trim()) {
                errorMessage = "Passwords do not match"
                return
            }

            isLoading = true
            coroutineScope.launch {
                val result = onRegisterWithPassword(cleanName, cleanEmail, cleanPassword)
                isLoading = false
                result.fold(
                    onSuccess = {
                        successMessage = it
                        onLoginSuccess()
                    },
                    onFailure = {
                        errorMessage = it.localizedMessage ?: "Registration failed"
                    }
                )
            }
        } else {
            isLoading = true
            coroutineScope.launch {
                val result = onLoginWithPassword(cleanEmail, cleanPassword)
                isLoading = false
                result.fold(
                    onSuccess = {
                        successMessage = it
                        onLoginSuccess()
                    },
                    onFailure = {
                        errorMessage = it.localizedMessage ?: "Invalid credentials"
                    }
                )
            }
        }
    }

    AnimatedContent(
        targetState = showAuthForm,
        transitionSpec = {
            if (targetState) {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            } else {
                (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> width } + fadeOut()
                )
            }
        },
        label = "AuthScreenStateTransition"
    ) { inFormMode ->
        if (!inFormMode) {
            // ==========================================
            // ONBOARDING / LANDING SCREEN (SPEC MOCKUP)
            // ==========================================
            LandingOnboardingView(
                solaceColors = solaceColors,
                onSkip = onContinueAsGuest,
                onGetStartedAsGuest = onContinueAsGuest,
                onSignUp = {
                    isSignUp = true
                    errorMessage = null
                    showAuthForm = true
                },
                onSignIn = {
                    isSignUp = false
                    errorMessage = null
                    showAuthForm = true
                },
                modifier = modifier
            )
        } else {
            // ==========================================
            // CREDENTIALS SIGN IN / SIGN UP FORM
            // ==========================================
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(solaceColors.mainBackground)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Bar with Back Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { showAuthForm = false },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = solaceColors.primaryText
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSignUp) "Create Account" else "Welcome Back",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = solaceColors.primaryText
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sanctuary Emblem / Logo
                    Image(
                        painter = painterResource(id = R.drawable.ic_solace_logo),
                        contentDescription = "Solace+ Logo",
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.dp, solaceColors.border, RoundedCornerShape(18.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // App Title
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "solace",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = solaceColors.primaryText
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(solaceColors.accent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = solaceColors.onAccent
                            )
                        }
                    }

                    Text(
                        text = "your private emotional sanctuary",
                        style = MaterialTheme.typography.bodyMedium,
                        color = solaceColors.secondaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )

                    // Auth Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = solaceColors.cardBackground),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(solaceColors.border)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Sign In / Create Account Toggle
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(solaceColors.cardHighlight)
                                    .border(1.dp, solaceColors.border, RoundedCornerShape(16.dp))
                                    .padding(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (!isSignUp) solaceColors.inputBackground else Color.Transparent)
                                        .clickable {
                                            isSignUp = false
                                            errorMessage = null
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Sign In",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = if (!isSignUp) FontWeight.Bold else FontWeight.Medium,
                                        color = if (!isSignUp) solaceColors.primaryText else solaceColors.secondaryText
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSignUp) solaceColors.inputBackground else Color.Transparent)
                                        .clickable {
                                            isSignUp = true
                                            errorMessage = null
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Create Account",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = if (isSignUp) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSignUp) solaceColors.primaryText else solaceColors.secondaryText
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Error & Success Feedback
                            AnimatedVisibility(visible = errorMessage != null) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFDE8E8)
                                ) {
                                    Text(
                                        text = errorMessage.orEmpty(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFC81E1E),
                                        modifier = Modifier.padding(12.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            AnimatedVisibility(visible = successMessage != null) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFDEF7EC)
                                ) {
                                    Text(
                                        text = successMessage.orEmpty(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF03543F),
                                        modifier = Modifier.padding(12.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            // Name (only for Sign Up)
                            if (isSignUp) {
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = { Text("Your Display Name") },
                                    placeholder = { Text("e.g. Arafat") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = WarmGrey)
                                    },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = moodColors.primary,
                                        unfocusedBorderColor = MutedBeige,
                                        focusedTextColor = solaceColors.primaryText,
                                        unfocusedTextColor = solaceColors.primaryText
                                    ),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                            }

                            // Email Field
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email Address") },
                                placeholder = { Text("you@example.com") },
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = WarmGrey)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = moodColors.primary,
                                    unfocusedBorderColor = MutedBeige,
                                    focusedTextColor = solaceColors.primaryText,
                                    unfocusedTextColor = solaceColors.primaryText
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Password Field
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password") },
                                placeholder = { Text("At least 6 characters") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = WarmGrey)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                            tint = WarmGrey
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = if (isSignUp) ImeAction.Next else ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = { handleSubmit() }),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = moodColors.primary,
                                    unfocusedBorderColor = MutedBeige,
                                    focusedTextColor = solaceColors.primaryText,
                                    unfocusedTextColor = solaceColors.primaryText
                                )
                            )

                            // Confirm Password (only for Sign Up)
                            if (isSignUp) {
                                Spacer(modifier = Modifier.height(14.dp))
                                OutlinedTextField(
                                    value = confirmPassword,
                                    onValueChange = { confirmPassword = it },
                                    label = { Text("Confirm Password") },
                                    placeholder = { Text("Re-enter password") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = WarmGrey)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                            Icon(
                                                imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = null,
                                                tint = WarmGrey
                                            )
                                        }
                                    },
                                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = { handleSubmit() }),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = moodColors.primary,
                                        unfocusedBorderColor = MutedBeige,
                                        focusedTextColor = solaceColors.primaryText,
                                        unfocusedTextColor = solaceColors.primaryText
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(22.dp))

                            // Primary Submit Button
                            Button(
                                onClick = { handleSubmit() },
                                enabled = !isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = solaceColors.accent,
                                    contentColor = solaceColors.onAccent
                                )
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = solaceColors.onAccent,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        text = if (isSignUp) "Create My Sanctuary Account" else "Sign In with Password",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Continue as Guest Option
                            OutlinedButton(
                                onClick = onContinueAsGuest,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, solaceColors.border)
                            ) {
                                Text(
                                    text = "Continue as Guest (Offline Sanctuary)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = solaceColors.primaryText,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Security Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = solaceColors.secondaryText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Encrypted local SQLite database • 100% private sanctuary",
                            style = MaterialTheme.typography.labelSmall,
                            color = solaceColors.secondaryText
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

/**
 * Onboarding Landing View tailored directly from the reference UI design.
 */
@Composable
private fun LandingOnboardingView(
    solaceColors: SolaceColors,
    onSkip: () -> Unit,
    onGetStartedAsGuest: () -> Unit,
    onSignUp: () -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pageBg = solaceColors.mainBackground

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(pageBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar: Skip Button at top-right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(solaceColors.cardHighlight)
                        .border(1.dp, solaceColors.border.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .clickable(onClick = onSkip)
                        .padding(horizontal = 18.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "Skip",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = solaceColors.primaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Upper Section: Floating Smartphone Mockup
            SmartphoneMockupCard(
                solaceColors = solaceColors,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Headline
            Text(
                text = "Empower yourself\nwith An AI Assistant!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp,
                    lineHeight = 35.sp
                ),
                fontWeight = FontWeight.Bold,
                color = solaceColors.primaryText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle
            Text(
                text = "Unleash you potentials & boost productivity\nwith an AI powered Assistant.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                fontWeight = FontWeight.Normal,
                color = solaceColors.secondaryText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Button 1: "Get Start as Guest" (Periwinkle Lavender Capsule)
            Button(
                onClick = onGetStartedAsGuest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(4.dp, RoundedCornerShape(28.dp), spotColor = Color(0x33C5CBFC)),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC5CBFC),
                    contentColor = Color(0xFF1E202B)
                )
            ) {
                Text(
                    text = "Get Start as Guest",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Button 2: "Sign Up" (Dark Charcoal Navy Capsule)
            Button(
                onClick = onSignUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E202B),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Already have an account? Sign In
            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = solaceColors.accent)) {
                        append("Sign In")
                    }
                },
                fontSize = 13.sp,
                color = solaceColors.secondaryText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable(onClick = onSignIn)
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Detailed Smartphone device mockup reflecting the reference UI preview.
 */
@Composable
private fun SmartphoneMockupCard(
    solaceColors: SolaceColors,
    modifier: Modifier = Modifier
) {
    val phoneBg = Color(0xFFFAF7F0)
    val phoneFrameBorder = Color(0xFF2A2823)

    Box(
        modifier = modifier
            .width(280.dp)
            .height(315.dp)
            .clip(RoundedCornerShape(38.dp))
            .border(3.5.dp, phoneFrameBorder, RoundedCornerShape(38.dp))
            .background(phoneBg),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dynamic Island Pill
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF161514))
            )

            Spacer(modifier = Modifier.height(10.dp))

            // In-phone Header: Avatar on left, icon pills on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Avatar Circle
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2DDD2))
                        .border(1.dp, Color(0xFFD0C9BC), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF4A463E),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Action icon buttons
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEFEBE1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            tint = Color(0xFF5A554C),
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEFEBE1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = Color(0xFF5A554C),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // In-phone Greetings
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Hello Arafat 👋",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E1C18)
                )
                Text(
                    text = "What's on your mind?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1C18)
                )
                Text(
                    text = "What type of help you need?",
                    fontSize = 9.sp,
                    color = Color(0xFF7A756C)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // In-phone Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1ECE3))
                    .border(1.dp, Color(0xFFE2DDD1), CircleShape)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFF9E988D),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Search what you want...",
                    fontSize = 9.sp,
                    color = Color(0xFF9E988D),
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E1C18)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Section Label
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Upcoming Task",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1C18)
                )
                Text(
                    text = "See all",
                    fontSize = 8.5.sp,
                    color = Color(0xFF7A756C)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Two Feature Cards (Journal & Image)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Card 1: Journal
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(88.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF3EFE4))
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = null,
                                tint = Color(0xFF5A554C),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Journal",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1C18)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "Reflect &\njournal",
                                fontSize = 8.sp,
                                lineHeight = 10.sp,
                                color = Color(0xFF5A554C)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF5A554C),
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }

                // Card 2: Image
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(88.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFDF1BE))
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = Color(0xFF6B5818),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Image",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1C18)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "Visual\nreflection",
                                fontSize = 8.sp,
                                lineHeight = 10.sp,
                                color = Color(0xFF6B5818)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF6B5818),
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Soft Gradient Overlay at bottom of phone to blend seamlessly
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            solaceColors.mainBackground.copy(alpha = 0.95f)
                        )
                    )
                )
        )
    }
}
