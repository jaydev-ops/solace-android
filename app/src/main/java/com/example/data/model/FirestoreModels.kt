package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreUserProfile(
    val userId: String = "",
    val displayName: String = "",
    val email: String? = null,
    val pinnedTheme: String? = null,
    val isDarkMode: Boolean = false,
    val aiPersonalisationConsent: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreMoodEntry(
    @DocumentId val id: String = "",
    val userId: String = "",
    val mood: String = "",
    val valence: Long = 5L,
    val intensity: Long = 4L,
    val tags: String = "",
    val note: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreJournalEntry(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val associatedMood: String? = null,
    val tags: String = "",
    val wordCount: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreChatMessage(
    @DocumentId val id: String = "",
    val userId: String = "",
    val senderRole: String = "user",
    val content: String = "",
    val modelName: String = "gemini-3.8-flash",
    val systemRole: String = "solace_companion",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreAudioTranscription(
    @DocumentId val id: String = "",
    val userId: String = "",
    val transcription: String = "",
    val durationSeconds: Long = 0L,
    val source: String = "microphone",
    @ServerTimestamp val createdAt: Timestamp? = null
)
