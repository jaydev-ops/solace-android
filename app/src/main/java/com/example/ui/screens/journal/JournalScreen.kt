package com.example.ui.screens.journal

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.JournalEntryEntity
import com.example.data.model.MoodType
import com.example.ui.components.MoodPillBadge
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    userName: String,
    entries: List<JournalEntryEntity>,
    onSaveEntry: (title: String, content: String, associatedMood: String?, tags: List<String>) -> Unit,
    onDeleteEntry: (Long) -> Unit,
    onOpenSettings: () -> Unit
) {
    val moodColors = LocalMoodColors.current

    var selectedMoodFilter by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isEditorOpen by remember { mutableStateOf(false) }

    // Editor Form State
    var editorTitle by remember { mutableStateOf("") }
    var editorContent by remember { mutableStateOf("") }
    var editorMood by remember { mutableStateOf("Calm") }
    var guidedPromptIndex by remember { mutableStateOf(0) }

    val guidedPrompts = listOf(
        "What is one burden you can gently lay down for the rest of today?",
        "Describe a quiet moment today where you felt even a fraction of relief.",
        "What does your body need right now that you have been holding back?",
        "If your tiredness could speak with love, what would it ask of you?",
        "What is one expectation placed upon you that doesn't actually belong to you?"
    )

    val filteredEntries = entries.filter { entry ->
        val matchesFilter = selectedMoodFilter == null || entry.associatedMood.equals(selectedMoodFilter, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() || entry.title.contains(searchQuery, ignoreCase = true) || entry.content.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editorTitle = ""
                    editorContent = ""
                    editorMood = "Calm"
                    isEditorOpen = true
                },
                containerColor = ButteryYellow,
                contentColor = SoftCharcoal,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .testTag("new_journal_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "New reflection")
                    Text(text = "Reflect", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("journal_screen")
        ) {
            SolaceTopBar(
                subtitle = "quiet sanctuary reflections",
                userName = userName,
                onAvatarClick = onOpenSettings
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    Column {
                        Text(
                            text = "Your Journal",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "A tender, unhurried space to untangle your inner world.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmGrey
                        )
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(text = "Search reflections by keyword...", color = WarmGrey)
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = WarmGrey)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear", tint = WarmGrey)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PaleVanilla,
                            unfocusedContainerColor = PaleVanilla,
                            focusedBorderColor = moodColors.primary,
                            unfocusedBorderColor = MutedBeige
                        ),
                        singleLine = true
                    )
                }

                // Filter Chips Ribbon
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                label = "All",
                                selected = selectedMoodFilter == null,
                                onClick = { selectedMoodFilter = null }
                            )
                        }
                        items(MoodType.entries) { mood ->
                            FilterChip(
                                label = mood.title,
                                selected = selectedMoodFilter.equals(mood.title, ignoreCase = true) || selectedMoodFilter.equals(mood.name, ignoreCase = true),
                                onClick = {
                                    selectedMoodFilter = if (selectedMoodFilter == mood.title) null else mood.title
                                }
                            )
                        }
                    }
                }

                // Empty State or List
                if (filteredEntries.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = PaleVanilla)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(ButteryYellow),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AutoStories,
                                        contentDescription = null,
                                        tint = SoftCharcoal,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "Your journal is quiet",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftCharcoal
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Whenever you're ready, tap 'Reflect' below to let your thoughts land softly.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = WarmGrey,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(filteredEntries, key = { it.id }) { entry ->
                        JournalCardItem(
                            entry = entry,
                            onDelete = { onDeleteEntry(entry.id) }
                        )
                    }
                }
            }
        }

        // Journal Editor Bottom Sheet
        if (isEditorOpen) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { isEditorOpen = false },
                sheetState = sheetState,
                containerColor = CreamWhite,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "New Reflection",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { isEditorOpen = false }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = WarmGrey)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Guided Prompt Inspiration Banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(PaleVanilla)
                            .border(1.dp, MutedBeige, RoundedCornerShape(16.dp))
                            .clickable {
                                guidedPromptIndex = (guidedPromptIndex + 1) % guidedPrompts.size
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lightbulb,
                            contentDescription = null,
                            tint = moodColors.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = guidedPrompts[guidedPromptIndex],
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftCharcoal,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Next ↻",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = moodColors.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Title Field
                    OutlinedTextField(
                        value = editorTitle,
                        onValueChange = { editorTitle = it },
                        placeholder = { Text(text = "Give this feeling a name...", color = WarmGrey) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PaleVanilla,
                            unfocusedContainerColor = PaleVanilla,
                            focusedBorderColor = moodColors.primary,
                            unfocusedBorderColor = MutedBeige
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Associated Mood Selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Associated Mood:", style = MaterialTheme.typography.labelSmall, color = WarmGrey)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(MoodType.entries) { mood ->
                                val isSelected = editorMood.equals(mood.title, ignoreCase = true) || editorMood.equals(mood.name, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) mood.bgTint else PaleVanilla)
                                        .border(1.dp, if (isSelected) mood.accentColor else MutedBeige, RoundedCornerShape(12.dp))
                                        .clickable { editorMood = mood.title }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = mood.title,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) mood.accentColor else SoftCharcoal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Body Content
                    OutlinedTextField(
                        value = editorContent,
                        onValueChange = { editorContent = it },
                        placeholder = {
                            Text(
                                text = "This space is completely yours. Write without editing yourself...",
                                color = WarmGrey.copy(alpha = 0.7f),
                                lineHeight = 22.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PaleVanilla,
                            unfocusedContainerColor = PaleVanilla,
                            focusedBorderColor = moodColors.primary,
                            unfocusedBorderColor = MutedBeige
                        )
                    )

                    val words = editorContent.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }.size
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$words words • Autosaved locally",
                        fontSize = 11.sp,
                        color = WarmGrey
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (editorContent.isNotBlank()) {
                                onSaveEntry(
                                    editorTitle.ifBlank { "Gentle Reflection" },
                                    editorContent,
                                    editorMood,
                                    listOf("Reflection")
                                )
                                isEditorOpen = false
                            }
                        },
                        enabled = editorContent.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButteryYellow,
                            contentColor = SoftCharcoal
                        )
                    ) {
                        Text(text = "Save Reflection", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun JournalCardItem(
    entry: JournalEntryEntity,
    onDelete: () -> Unit
) {
    val dateFormatted = remember(entry.updatedAt) {
        SimpleDateFormat("EEE, MMM d • h:mm a", Locale.getDefault()).format(Date(entry.updatedAt))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SubtleOutline, MutedBeige)))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                entry.associatedMood?.let { mood ->
                    MoodPillBadge(moodName = mood)
                } ?: Spacer(modifier = Modifier.width(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dateFormatted,
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmGrey
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DeleteOutline,
                            contentDescription = "Delete entry",
                            tint = WarmGrey,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = SoftCharcoal,
                maxLines = 4,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${entry.wordCount} words • ~1 min read",
                fontSize = 11.sp,
                color = WarmGrey
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (selected) ButteryYellow else PaleVanilla
    val borderColor = if (selected) SoftCharcoal.copy(alpha = 0.25f) else MutedBeige

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bgColor)
            .border(1.dp, borderColor, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = SoftCharcoal
        )
    }
}
