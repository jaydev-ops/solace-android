package com.example.data.repository

import com.example.data.local.SolaceDatabase
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.JournalEntryEntity
import com.example.data.local.entity.MoodEntryEntity
import com.example.data.local.entity.RegulationSessionEntity
import com.example.data.local.entity.UserPreferencesEntity
import com.example.data.model.FirestoreChatMessage
import com.example.data.model.FirestoreJournalEntry
import com.example.data.model.FirestoreMoodEntry
import com.example.data.model.FirestoreUserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class SolaceRepository(private val db: SolaceDatabase) {

    // --- Mood ---
    val allMoodEntries: Flow<List<MoodEntryEntity>> = db.moodDao().getAllMoodEntries()
    val latestMoodEntry: Flow<MoodEntryEntity?> = db.moodDao().getLatestMoodEntry()

    suspend fun saveMoodEntry(
        mood: String,
        valence: Int,
        intensity: Int,
        tags: String,
        note: String
    ): Long {
        val entry = MoodEntryEntity(
            mood = mood,
            valence = valence,
            intensity = intensity,
            tags = tags,
            note = note,
            timestamp = System.currentTimeMillis()
        )
        return db.moodDao().insertMoodEntry(entry)
    }

    suspend fun deleteMoodEntry(id: Long) {
        db.moodDao().deleteMoodEntryById(id)
    }

    // --- Journal ---
    val allJournalEntries: Flow<List<JournalEntryEntity>> = db.journalDao().getAllActiveEntries()

    fun getJournalEntryById(id: Long): Flow<JournalEntryEntity?> {
        return db.journalDao().getEntryById(id)
    }

    suspend fun saveJournalEntry(
        id: Long = 0,
        title: String,
        content: String,
        associatedMood: String? = null,
        tags: String = ""
    ): Long {
        val wordCount = content.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }.size
        val entry = JournalEntryEntity(
            id = id,
            title = title.ifBlank { "Gentle Reflection" },
            content = content,
            associatedMood = associatedMood,
            tags = tags,
            wordCount = wordCount,
            updatedAt = System.currentTimeMillis()
        )
        return db.journalDao().insertEntry(entry)
    }

    suspend fun deleteJournalEntry(id: Long) {
        db.journalDao().softDelete(id)
    }

    // --- Chat ---
    val allChatMessages: Flow<List<ChatMessageEntity>> = db.chatDao().getAllMessages()

    suspend fun saveChatMessage(
        senderRole: String,
        content: String,
        regulationMode: String = "LISTEN",
        contextAnchor: String? = null
    ): Long {
        val message = ChatMessageEntity(
            senderRole = senderRole,
            content = content,
            regulationMode = regulationMode,
            contextAnchor = contextAnchor,
            timestamp = System.currentTimeMillis()
        )
        return db.chatDao().insertMessage(message)
    }

    suspend fun clearChatHistory() {
        db.chatDao().clearChatHistory()
    }

    // --- Regulation ---
    val allRegulationSessions: Flow<List<RegulationSessionEntity>> = db.regulationDao().getAllSessions()

    suspend fun saveRegulationSession(
        activityId: String,
        activityTitle: String,
        category: String,
        durationSeconds: Int,
        rating: Int,
        note: String
    ): Long {
        val session = RegulationSessionEntity(
            activityId = activityId,
            activityTitle = activityTitle,
            category = category,
            durationSeconds = durationSeconds,
            rating = rating,
            note = note,
            timestamp = System.currentTimeMillis()
        )
        return db.regulationDao().insertSession(session)
    }

    // --- User Preferences ---
    val userPreferences: Flow<UserPreferencesEntity?> = db.userPreferencesDao().getPreferences()

    suspend fun updatePreferences(preferences: UserPreferencesEntity) {
        db.userPreferencesDao().upsertPreferences(preferences)
    }

    suspend fun updateUserName(name: String) {
        val current = userPreferences.firstOrNull() ?: UserPreferencesEntity()
        db.userPreferencesDao().upsertPreferences(current.copy(userName = name))
    }

    suspend fun setPinnedTheme(pinnedTheme: String?) {
        val current = userPreferences.firstOrNull() ?: UserPreferencesEntity()
        db.userPreferencesDao().upsertPreferences(current.copy(pinnedTheme = pinnedTheme))
    }

    suspend fun toggleDarkMode(isDark: Boolean) {
        val current = userPreferences.firstOrNull() ?: UserPreferencesEntity()
        db.userPreferencesDao().upsertPreferences(current.copy(isDarkMode = isDark))
    }

    suspend fun toggleReducedMotion(reduced: Boolean) {
        val current = userPreferences.firstOrNull() ?: UserPreferencesEntity()
        db.userPreferencesDao().upsertPreferences(current.copy(reducedMotion = reduced))
    }

    suspend fun toggleAiConsent(consent: Boolean) {
        val current = userPreferences.firstOrNull() ?: UserPreferencesEntity()
        db.userPreferencesDao().upsertPreferences(current.copy(aiPersonalisationConsent = consent))
    }

    suspend fun setMemoryPermission(allowed: Boolean) {
        val current = userPreferences.firstOrNull() ?: UserPreferencesEntity()
        db.userPreferencesDao().upsertPreferences(
            current.copy(
                memorySavingPermissionAsked = true,
                allowMemorySaving = allowed
            )
        )
    }

    suspend fun setCustomGeminiApiKey(apiKey: String) {
        val current = userPreferences.firstOrNull() ?: UserPreferencesEntity()
        db.userPreferencesDao().upsertPreferences(current.copy(customGeminiApiKey = apiKey.trim()))
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean, email: String, name: String) {
        val current = userPreferences.firstOrNull() ?: UserPreferencesEntity()
        db.userPreferencesDao().upsertPreferences(
            current.copy(
                isLoggedIn = isLoggedIn,
                userEmail = email,
                userName = name.ifBlank { current.userName }
            )
        )
    }

    // --- Accounts ---
    suspend fun getAccountByEmail(email: String): com.example.data.local.entity.UserAccountEntity? {
        return db.userAccountDao().getAccountByEmail(email)
    }

    suspend fun createAccount(email: String, name: String, passwordHash: String) {
        val account = com.example.data.local.entity.UserAccountEntity(
            email = email.trim().lowercase(),
            displayName = name.trim(),
            passwordHash = passwordHash
        )
        db.userAccountDao().insertAccount(account)
    }

    // --- Export & Wipe ---
    suspend fun exportAllDataAsJson(): String {
        val root = JSONObject()
        val prefs = userPreferences.firstOrNull() ?: UserPreferencesEntity()
        root.put("exportedAt", System.currentTimeMillis())
        root.put("sanctuaryUser", prefs.userName)

        val moods = allMoodEntries.firstOrNull() ?: emptyList()
        val moodsArray = JSONArray()
        for (m in moods) {
            val mo = JSONObject()
            mo.put("mood", m.mood)
            mo.put("valence", m.valence)
            mo.put("intensity", m.intensity)
            mo.put("tags", m.tags)
            mo.put("note", m.note)
            mo.put("timestamp", m.timestamp)
            moodsArray.put(mo)
        }
        root.put("moodEntries", moodsArray)

        val journals = allJournalEntries.firstOrNull() ?: emptyList()
        val journalsArray = JSONArray()
        for (j in journals) {
            val jo = JSONObject()
            jo.put("title", j.title)
            jo.put("content", j.content)
            jo.put("associatedMood", j.associatedMood)
            jo.put("tags", j.tags)
            jo.put("createdAt", j.createdAt)
            journalsArray.put(jo)
        }
        root.put("journalEntries", journalsArray)

        val sessions = allRegulationSessions.firstOrNull() ?: emptyList()
        val sessionsArray = JSONArray()
        for (s in sessions) {
            val so = JSONObject()
            so.put("activity", s.activityTitle)
            so.put("category", s.category)
            so.put("durationSeconds", s.durationSeconds)
            so.put("rating", s.rating)
            so.put("note", s.note)
            so.put("timestamp", s.timestamp)
            sessionsArray.put(so)
        }
        root.put("regulationSessions", sessionsArray)

        return root.toString(2)
    }

    suspend fun clearAllSanctuaryData() {
        db.moodDao().deleteAllMoodEntries()
        db.journalDao().deleteAllEntries()
        db.chatDao().clearChatHistory()
        db.regulationDao().deleteAllSessions()
    }

    suspend fun restoreUserData(
        profile: FirestoreUserProfile?,
        moods: List<FirestoreMoodEntry>,
        journals: List<FirestoreJournalEntry>,
        chats: List<FirestoreChatMessage>
    ) = withContext(Dispatchers.IO) {
        // 1. Preferences & Profile
        if (profile != null) {
            val current = db.userPreferencesDao().getPreferencesSync() ?: UserPreferencesEntity()
            val restoredName = profile.displayName.trim().ifBlank { current.userName }
            db.userPreferencesDao().upsertPreferences(
                current.copy(
                    userName = restoredName,
                    pinnedTheme = profile.pinnedTheme ?: current.pinnedTheme,
                    isDarkMode = profile.isDarkMode,
                    aiPersonalisationConsent = profile.aiPersonalisationConsent,
                    isLoggedIn = true
                )
            )
        }

        // 2. Moods
        if (moods.isNotEmpty()) {
            for (m in moods) {
                val timestamp = m.createdAt?.toDate()?.time ?: System.currentTimeMillis()
                db.moodDao().insertMoodEntry(
                    MoodEntryEntity(
                        id = 0,
                        mood = m.mood,
                        valence = m.valence.toInt(),
                        intensity = m.intensity.toInt(),
                        tags = m.tags,
                        note = m.note,
                        timestamp = timestamp
                    )
                )
            }
        }

        // 3. Journals
        if (journals.isNotEmpty()) {
            for (j in journals) {
                val createdAt = j.createdAt?.toDate()?.time ?: System.currentTimeMillis()
                val updatedAt = j.updatedAt?.toDate()?.time ?: createdAt
                db.journalDao().insertEntry(
                    JournalEntryEntity(
                        id = 0,
                        title = j.title,
                        content = j.content,
                        associatedMood = j.associatedMood,
                        tags = j.tags,
                        wordCount = j.wordCount.toInt(),
                        isDeleted = false,
                        createdAt = createdAt,
                        updatedAt = updatedAt
                    )
                )
            }
        }

        // 4. Chats
        if (chats.isNotEmpty()) {
            for (c in chats) {
                val timestamp = c.createdAt?.toDate()?.time ?: System.currentTimeMillis()
                db.chatDao().insertMessage(
                    ChatMessageEntity(
                        senderRole = c.senderRole,
                        content = c.content,
                        regulationMode = c.systemRole,
                        contextAnchor = c.modelName,
                        timestamp = timestamp
                    )
                )
            }
        }
    }

    suspend fun migrateLegacyDefaultName(legacyName: String = "Sarah", replacementName: String = "") = withContext(Dispatchers.IO) {
        val prefs = db.userPreferencesDao().getPreferencesSync()
        if (prefs != null && (prefs.userName.equals(legacyName, ignoreCase = true) || prefs.userName.isBlank())) {
            db.userPreferencesDao().upsertPreferences(prefs.copy(userName = replacementName))
        }
        val messages = db.chatDao().getAllMessagesList()
        messages.forEach { msg ->
            if (msg.content.contains(legacyName, ignoreCase = true)) {
                val updated = msg.content.replace(Regex(legacyName, RegexOption.IGNORE_CASE), replacementName)
                db.chatDao().updateMessage(msg.copy(content = updated))
            }
        }
    }
}
