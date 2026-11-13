package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mood: String, // Happy, Calm, Neutral, Sad, Anxious, Angry, Overwhelmed
    val valence: Int, // 1..10
    val intensity: Int, // 1..10
    val tags: String, // Comma-separated tags
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val associatedMood: String? = null,
    val tags: String = "",
    val wordCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderRole: String, // "user", "assistant", "system"
    val content: String,
    val regulationMode: String = "LISTEN",
    val contextAnchor: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "regulation_sessions")
data class RegulationSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val activityId: String,
    val activityTitle: String,
    val category: String,
    val durationSeconds: Int,
    val rating: Int = 5, // 1..5 stars
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: Int = 1,
    val userName: String = "Sanctuary",
    val userEmail: String = "",
    val isLoggedIn: Boolean = false,
    val pinnedTheme: String? = null, // null for dynamic mood response
    val isDarkMode: Boolean = false,
    val reducedMotion: Boolean = false,
    val aiPersonalisationConsent: Boolean = true,
    val memorySavingPermissionAsked: Boolean = false, // Has user been prompted for memory permission?
    val allowMemorySaving: Boolean = true, // User's choice on whether to save AI memories
    val customGeminiApiKey: String = "" // Optional custom Gemini API key entered by user in Settings
)

@Entity(tableName = "user_accounts")
data class UserAccountEntity(
    @PrimaryKey val email: String,
    val displayName: String,
    val passwordHash: String,
    val createdAt: Long = System.currentTimeMillis()
)
