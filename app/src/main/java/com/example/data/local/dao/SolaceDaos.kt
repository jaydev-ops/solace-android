package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.JournalEntryEntity
import com.example.data.local.entity.MoodEntryEntity
import com.example.data.local.entity.RegulationSessionEntity
import com.example.data.local.entity.UserAccountEntity
import com.example.data.local.entity.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoodEntries(): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC LIMIT 1")
    fun getLatestMoodEntry(): Flow<MoodEntryEntity?>

    @Query("SELECT * FROM mood_entries WHERE timestamp >= :sinceTimestamp ORDER BY timestamp ASC")
    fun getMoodEntriesSince(sinceTimestamp: Long): Flow<List<MoodEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoodEntry(entry: MoodEntryEntity): Long

    @Query("DELETE FROM mood_entries WHERE id = :id")
    suspend fun deleteMoodEntryById(id: Long)

    @Query("DELETE FROM mood_entries")
    suspend fun deleteAllMoodEntries()
}

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAllActiveEntries(): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM journal_entries WHERE id = :id LIMIT 1")
    fun getEntryById(id: Long): Flow<JournalEntryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntryEntity): Long

    @Update
    suspend fun updateEntry(entry: JournalEntryEntity)

    @Query("UPDATE journal_entries SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun softDelete(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun hardDelete(id: Long)

    @Query("DELETE FROM journal_entries")
    suspend fun deleteAllEntries()
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    suspend fun getAllMessagesList(): List<ChatMessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Update
    suspend fun updateMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()
}

@Dao
interface RegulationDao {
    @Query("SELECT * FROM regulation_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<RegulationSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: RegulationSessionEntity): Long

    @Query("DELETE FROM regulation_sessions")
    suspend fun deleteAllSessions()
}

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1 LIMIT 1")
    fun getPreferences(): Flow<UserPreferencesEntity?>

    @Query("SELECT * FROM user_preferences WHERE id = 1 LIMIT 1")
    suspend fun getPreferencesSync(): UserPreferencesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPreferences(preferences: UserPreferencesEntity)
}

@Dao
interface UserAccountDao {
    @Query("SELECT * FROM user_accounts WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getAccountByEmail(email: String): UserAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: UserAccountEntity)

    @Query("SELECT * FROM user_accounts ORDER BY createdAt DESC")
    fun getAllAccounts(): Flow<List<UserAccountEntity>>
}
