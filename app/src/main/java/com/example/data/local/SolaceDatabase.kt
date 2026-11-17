package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.ChatDao
import com.example.data.local.dao.JournalDao
import com.example.data.local.dao.MoodDao
import com.example.data.local.dao.RegulationDao
import com.example.data.local.dao.SafetyDao
import com.example.data.local.dao.UserAccountDao
import com.example.data.local.dao.UserPreferencesDao
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.JournalEntryEntity
import com.example.data.local.entity.MoodEntryEntity
import com.example.data.local.entity.RegulationSessionEntity
import com.example.data.local.entity.SafetyStateEntity
import com.example.data.local.entity.UserAccountEntity
import com.example.data.local.entity.UserPreferencesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MoodEntryEntity::class,
        JournalEntryEntity::class,
        ChatMessageEntity::class,
        SafetyStateEntity::class,
        RegulationSessionEntity::class,
        UserPreferencesEntity::class,
        UserAccountEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class SolaceDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
    abstract fun journalDao(): JournalDao
    abstract fun chatDao(): ChatDao
    abstract fun safetyDao(): SafetyDao
    abstract fun regulationDao(): RegulationDao
    abstract fun userPreferencesDao(): UserPreferencesDao
    abstract fun userAccountDao(): UserAccountDao

    companion object {
        val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                try {
                    db.execSQL("CREATE TABLE IF NOT EXISTS `user_accounts` (`email` TEXT NOT NULL, `displayName` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`email`))")
                } catch (e: Exception) {
                    android.util.Log.w("SolaceDatabase", "MIGRATION_1_2 warning: ${e.message}")
                }
            }
        }

        val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                try {
                    db.execSQL("ALTER TABLE user_preferences ADD COLUMN appearanceMode TEXT NOT NULL DEFAULT 'system'")
                } catch (e: Exception) {
                    android.util.Log.w("SolaceDatabase", "MIGRATION_2_3 warning: ${e.message}")
                }
            }
        }

        val MIGRATION_1_3 = object : androidx.room.migration.Migration(1, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                try {
                    db.execSQL("CREATE TABLE IF NOT EXISTS `user_accounts` (`email` TEXT NOT NULL, `displayName` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`email`))")
                } catch (_: Exception) {}
                try {
                    db.execSQL("ALTER TABLE user_preferences ADD COLUMN appearanceMode TEXT NOT NULL DEFAULT 'system'")
                } catch (_: Exception) {}
            }
        }

        val MIGRATION_3_4 = object : androidx.room.migration.Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                try {
                    db.execSQL("CREATE TABLE IF NOT EXISTS `safety_states` (`conversationId` TEXT NOT NULL, `currentLevel` TEXT NOT NULL, `highestLevel` TEXT NOT NULL, `conversationState` TEXT NOT NULL, `unresolvedSafetyConcern` INTEGER NOT NULL, `emergencyTriggered` INTEGER NOT NULL, `lastAssessment` TEXT NOT NULL, `lastScreeningQuestionAsked` TEXT, `awaitingScreeningAnswer` INTEGER NOT NULL, `turnCount` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`conversationId`))")
                } catch (e: Exception) {
                    android.util.Log.w("SolaceDatabase", "MIGRATION_3_4 safety_states: ${e.message}")
                }
                try {
                    db.execSQL("ALTER TABLE chat_messages ADD COLUMN riskLevel TEXT DEFAULT NULL")
                } catch (e: Exception) {
                    android.util.Log.w("SolaceDatabase", "MIGRATION_3_4 chat_messages: ${e.message}")
                }
            }
        }

        val MIGRATION_4_5 = object : androidx.room.migration.Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                try {
                    db.execSQL("ALTER TABLE safety_states ADD COLUMN helpRecommended INTEGER NOT NULL DEFAULT 0")
                } catch (_: Exception) {}
                try {
                    db.execSQL("ALTER TABLE safety_states ADD COLUMN humanSupportContacted INTEGER NOT NULL DEFAULT 0")
                } catch (_: Exception) {}
                try {
                    db.execSQL("ALTER TABLE safety_states ADD COLUMN safetyConfirmed INTEGER NOT NULL DEFAULT 0")
                } catch (_: Exception) {}
                try {
                    db.execSQL("ALTER TABLE safety_states ADD COLUMN lastConfirmationType TEXT DEFAULT NULL")
                } catch (_: Exception) {}
            }
        }

        val MIGRATION_5_6 = object : androidx.room.migration.Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                try {
                    db.execSQL("ALTER TABLE safety_states ADD COLUMN pendingQuestionId TEXT DEFAULT NULL")
                } catch (_: Exception) {}
                try {
                    db.execSQL("ALTER TABLE safety_states ADD COLUMN lastAnsweredQuestionId TEXT DEFAULT NULL")
                } catch (_: Exception) {}
                try {
                    db.execSQL("ALTER TABLE safety_states ADD COLUMN lastAnswer TEXT DEFAULT NULL")
                } catch (_: Exception) {}
                try {
                    db.execSQL("ALTER TABLE safety_states ADD COLUMN questionAnswersJson TEXT NOT NULL DEFAULT ''")
                } catch (_: Exception) {}
            }
        }

        val MIGRATION_4_6 = object : androidx.room.migration.Migration(4, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                MIGRATION_4_5.migrate(db)
                MIGRATION_5_6.migrate(db)
            }
        }

        @Volatile
        private var INSTANCE: SolaceDatabase? = null

        fun getDatabase(context: Context): SolaceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SolaceDatabase::class.java,
                    "solace_sanctuary.db"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_1_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_4_6)
                .fallbackToDestructiveMigration(dropAllTables = true)
                .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                INSTANCE?.let { database ->
                                    populateInitialData(database)
                                }
                            } catch (e: Exception) {
                                android.util.Log.e("SolaceDatabase", "Failed to populate initial data: ${e.message}")
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(db: SolaceDatabase) {
            val now = System.currentTimeMillis()
            val dayMillis = 24 * 60 * 60 * 1000L

            // Initial Preferences
            db.userPreferencesDao().upsertPreferences(
                UserPreferencesEntity(
                    id = 1,
                    userName = "Sanctuary",
                    pinnedTheme = null,
                    isDarkMode = false,
                    appearanceMode = "system",
                    reducedMotion = false,
                    aiPersonalisationConsent = true
                )
            )

            // Initial historical mood check-ins (past 6 days leading to today)
            db.moodDao().insertMoodEntry(
                MoodEntryEntity(
                    mood = "Calm",
                    valence = 8,
                    intensity = 4,
                    tags = "Morning quiet, Solitude",
                    note = "Soft rain outside, cup of chamomile tea. Feeling grounded.",
                    timestamp = now - (5 * dayMillis)
                )
            )
            db.moodDao().insertMoodEntry(
                MoodEntryEntity(
                    mood = "Happy",
                    valence = 9,
                    intensity = 6,
                    tags = "Creativity, Weather & Light",
                    note = "Finished reading a gentle book. Heart feels light and open.",
                    timestamp = now - (4 * dayMillis)
                )
            )
            db.moodDao().insertMoodEntry(
                MoodEntryEntity(
                    mood = "Sad",
                    valence = 4,
                    intensity = 5,
                    tags = "Relationships, Solitude",
                    note = "Quiet melancholy in the evening. Allowed the tears to visit.",
                    timestamp = now - (3 * dayMillis)
                )
            )
            db.moodDao().insertMoodEntry(
                MoodEntryEntity(
                    mood = "Anxious",
                    valence = 3,
                    intensity = 7,
                    tags = "Work, Deadlines",
                    note = "Fluttering chest over project milestones. Need grounding.",
                    timestamp = now - (2 * dayMillis)
                )
            )
            db.moodDao().insertMoodEntry(
                MoodEntryEntity(
                    mood = "Calm",
                    valence = 8,
                    intensity = 4,
                    tags = "Morning quiet, Sleep & Rest",
                    note = "Slow awakening. Practiced 4-4-4 box breathing.",
                    timestamp = now - dayMillis
                )
            )
            db.moodDao().insertMoodEntry(
                MoodEntryEntity(
                    mood = "Happy",
                    valence = 8,
                    intensity = 4,
                    tags = "Morning quiet, Creativity",
                    note = "Gentle morning sun on the curtains. Grateful for this quiet sanctuary.",
                    timestamp = now - 3600_000L
                )
            )

            // Initial Journal Reflection
            db.journalDao().insertEntry(
                JournalEntryEntity(
                    title = "Morning Serenity & The Breath of Dawn",
                    content = "Today I woke up before the alarms. The world was still wrapped in soft blue shadows. Sitting by the wooden windowsill with steam rising from herbal tea, I noticed how quickly peace arrives when I stop rushing to defend my time. Peace is not the absence of storm, but the presence of gentle room inside.",
                    associatedMood = "Calm",
                    tags = "Morning quiet, Gratitude",
                    wordCount = 68,
                    createdAt = now - (2 * 3600_000L),
                    updatedAt = now - (2 * 3600_000L)
                )
            )
            db.journalDao().insertEntry(
                JournalEntryEntity(
                    title = "Unpacking the Weight of Deadlines",
                    content = "Work felt relentless yesterday afternoon. When five notifications pinged at once, my shoulders crept up to my ears. Writing this down helps me see that my worth is not measured by the speed of my responses. I can take one slow breath and do one thing at a time.",
                    associatedMood = "Anxious",
                    tags = "Work, Boundaries",
                    wordCount = 57,
                    createdAt = now - (2 * dayMillis),
                    updatedAt = now - (2 * dayMillis)
                )
            )

            // Initial Welcome AI Message
            val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            val timeGreeting = when {
                currentHour in 4..11 -> "Good morning"
                currentHour in 12..16 -> "Good afternoon"
                else -> "Good evening"
            }
            db.chatDao().insertMessage(
                ChatMessageEntity(
                    senderRole = "assistant",
                    content = "$timeGreeting. I noticed you logged feeling calm today with a quiet morning. I'm here with you in your sanctuary. What's on your mind right now?",
                    regulationMode = "LISTEN",
                    contextAnchor = "Sanctuary Context • Space is held with quiet reverence.",
                    timestamp = now - 1800_000L
                )
            )

            // Initial Regulation Session Record
            db.regulationDao().insertSession(
                RegulationSessionEntity(
                    activityId = "box_breathing",
                    activityTitle = "Box Breathing (4-4-4-4)",
                    category = "Breathing",
                    durationSeconds = 120,
                    rating = 5,
                    note = "Felt immediate settling in my chest and solar plexus.",
                    timestamp = now - dayMillis
                )
            )
        }
    }
}
