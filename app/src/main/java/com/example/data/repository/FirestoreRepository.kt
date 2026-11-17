package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.firebase.OperationType
import com.example.data.firebase.handleFirestoreError
import com.example.data.model.FirestoreChatMessage
import com.example.data.model.FirestoreJournalEntry
import com.example.data.model.FirestoreMoodEntry
import com.example.data.model.FirestoreUserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreRepository(private val db: FirebaseFirestore? = null) {

    constructor(context: Context) : this(
        try {
            try {
                FirebaseFirestore.getInstance(
                    context.applicationContext.getString(R.string.firestore_database_id)
                )
            } catch (e: Exception) {
                android.util.Log.w("FirestoreRepository", "Named database not found, falling back to default instance: ${e.message}")
                FirebaseFirestore.getInstance()
            }
        } catch (e: Exception) {
            android.util.Log.w("FirestoreRepository", "Firestore unavailable, offline sanctuary mode active: ${e.message}")
            null
        }
    )

    private val auth = try {
        Firebase.auth
    } catch (_: Exception) {
        null
    }

    private fun getUserId(): String? {
        return auth?.currentUser?.uid
    }

    // Profile
    fun observeUserProfile(userId: String): Flow<FirestoreUserProfile?> = flow {
        val database = db ?: run {
            emit(null)
            return@flow
        }
        val docRef = database.collection("users").document(userId)
        docRef.snapshots()
            .map { it.toObject(FirestoreUserProfile::class.java) }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.GET, docRef.path)
                emit(null)
            }
            .collect { emit(it) }
    }

    suspend fun saveUserProfile(profile: FirestoreUserProfile) {
        val database = db ?: return
        val uid = getUserId() ?: return
        val docRef = database.collection("users").document(uid)
        try {
            val payload = hashMapOf<String, Any>(
                "userId" to uid,
                "displayName" to profile.displayName,
                "isDarkMode" to profile.isDarkMode,
                "appearanceMode" to profile.appearanceMode,
                "aiPersonalisationConsent" to profile.aiPersonalisationConsent,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            profile.email?.let { payload["email"] = it }
            profile.pinnedTheme?.let { payload["pinnedTheme"] = it }

            val existing = docRef.get().await()
            if (!existing.exists()) {
                payload["createdAt"] = FieldValue.serverTimestamp()
            }
            docRef.set(payload, com.google.firebase.firestore.SetOptions.merge()).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
        }
    }

    // Mood Entries
    fun observeMoodEntries(userId: String): Flow<List<FirestoreMoodEntry>> = flow {
        val database = db ?: run {
            emit(emptyList())
            return@flow
        }
        val colRef = database.collection("users").document(userId).collection("mood_entries")
        colRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot -> snapshot.toObjects(FirestoreMoodEntry::class.java) }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, colRef.path)
                emit(emptyList())
            }
            .collect { emit(it) }
    }

    suspend fun saveMoodEntry(mood: String, valence: Int, intensity: Int, tags: String, note: String) {
        val database = db ?: return
        val uid = getUserId() ?: return
        val entryId = UUID.randomUUID().toString()
        val docRef = database.collection("users").document(uid).collection("mood_entries").document(entryId)
        try {
            val payload = hashMapOf<String, Any>(
                "id" to entryId,
                "userId" to uid,
                "mood" to mood,
                "valence" to valence.toLong(),
                "intensity" to intensity.toLong(),
                "tags" to tags,
                "note" to note,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
        }
    }

    // Journal Entries
    fun observeJournalEntries(userId: String): Flow<List<FirestoreJournalEntry>> = flow {
        val database = db ?: run {
            emit(emptyList())
            return@flow
        }
        val colRef = database.collection("users").document(userId).collection("journal_entries")
        colRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot -> snapshot.toObjects(FirestoreJournalEntry::class.java) }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, colRef.path)
                emit(emptyList())
            }
            .collect { emit(it) }
    }

    suspend fun saveJournalEntry(title: String, content: String, associatedMood: String?, tags: String) {
        val database = db ?: return
        val uid = getUserId() ?: return
        val journalId = UUID.randomUUID().toString()
        val docRef = database.collection("users").document(uid).collection("journal_entries").document(journalId)
        val wordCount = content.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }.size.toLong()
        try {
            val payload = hashMapOf<String, Any>(
                "id" to journalId,
                "userId" to uid,
                "title" to title,
                "content" to content,
                "tags" to tags,
                "wordCount" to wordCount,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            associatedMood?.let { payload["associatedMood"] = it }
            docRef.set(payload).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
        }
    }

    suspend fun deleteJournalEntry(id: String) {
        val database = db ?: return
        val uid = getUserId() ?: return
        val docRef = database.collection("users").document(uid).collection("journal_entries").document(id)
        try {
            docRef.delete().await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
        }
    }

    // Chat Messages (Multi-turn)
    fun observeChatMessages(userId: String): Flow<List<FirestoreChatMessage>> = flow {
        val database = db ?: run {
            emit(emptyList())
            return@flow
        }
        val colRef = database.collection("users").document(userId).collection("chat_messages")
        colRef.orderBy("createdAt", Query.Direction.ASCENDING)
            .snapshots()
            .map { snapshot -> snapshot.toObjects(FirestoreChatMessage::class.java) }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, colRef.path)
                emit(emptyList())
            }
            .collect { emit(it) }
    }

    suspend fun saveChatMessage(senderRole: String, content: String, modelName: String, systemRole: String) {
        val database = db ?: return
        val uid = getUserId() ?: return
        val messageId = UUID.randomUUID().toString()
        val docRef = database.collection("users").document(uid).collection("chat_messages").document(messageId)
        try {
            val payload = hashMapOf<String, Any>(
                "id" to messageId,
                "userId" to uid,
                "senderRole" to senderRole,
                "content" to content,
                "modelName" to modelName,
                "systemRole" to systemRole,
                "createdAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
        }
    }

    suspend fun clearChatMessages(userId: String) {
        val database = db ?: return
        val colRef = database.collection("users").document(userId).collection("chat_messages")
        try {
            val snapshots = colRef.get().await()
            for (doc in snapshots.documents) {
                doc.reference.delete().await()
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, colRef.path)
        }
    }

    // --- Direct Restoration Fetches (for account recovery after re-install) ---

    suspend fun fetchUserProfile(userId: String): FirestoreUserProfile? {
        val database = db ?: return null
        val docRef = database.collection("users").document(userId)
        return try {
            val snapshot = docRef.get().await()
            if (snapshot.exists()) snapshot.toObject(FirestoreUserProfile::class.java) else null
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, docRef.path)
            null
        }
    }

    suspend fun fetchMoodEntries(userId: String): List<FirestoreMoodEntry> {
        val database = db ?: return emptyList()
        val colRef = database.collection("users").document(userId).collection("mood_entries")
        return try {
            val snapshot = colRef.orderBy("createdAt", Query.Direction.DESCENDING).get().await()
            snapshot.toObjects(FirestoreMoodEntry::class.java)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, colRef.path)
            emptyList()
        }
    }

    suspend fun fetchJournalEntries(userId: String): List<FirestoreJournalEntry> {
        val database = db ?: return emptyList()
        val colRef = database.collection("users").document(userId).collection("journal_entries")
        return try {
            val snapshot = colRef.orderBy("createdAt", Query.Direction.DESCENDING).get().await()
            snapshot.toObjects(FirestoreJournalEntry::class.java)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, colRef.path)
            emptyList()
        }
    }

    suspend fun fetchChatMessages(userId: String): List<FirestoreChatMessage> {
        val database = db ?: return emptyList()
        val colRef = database.collection("users").document(userId).collection("chat_messages")
        return try {
            val snapshot = colRef.orderBy("createdAt", Query.Direction.ASCENDING).get().await()
            snapshot.toObjects(FirestoreChatMessage::class.java)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, colRef.path)
            emptyList()
        }
    }

    suspend fun deleteUserAccountAndData(userId: String) {
        val database = db ?: return
        val userDoc = database.collection("users").document(userId)
        try {
            // Delete subcollections
            val moods = userDoc.collection("mood_entries").get().await()
            for (d in moods.documents) d.reference.delete().await()

            val journals = userDoc.collection("journal_entries").get().await()
            for (d in journals.documents) d.reference.delete().await()

            val chats = userDoc.collection("chat_messages").get().await()
            for (d in chats.documents) d.reference.delete().await()

            try {
                val audio = userDoc.collection("audio_transcriptions").get().await()
                for (d in audio.documents) d.reference.delete().await()
            } catch (_: Exception) {}

            // Delete user doc
            userDoc.delete().await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, userDoc.path)
        }
    }
}
