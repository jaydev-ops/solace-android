package com.example

import com.example.data.ai.CrisisHelplines
import com.example.data.ai.SolaceAiService
import com.example.data.local.entity.MoodEntryEntity
import com.example.data.model.CuratedRegulationActivities
import com.example.data.model.MoodType
import com.example.data.model.RegulationMode
import com.example.ui.screens.home.calculateCurrentWeekMoods
import com.example.ui.screens.home.formatUserGreeting
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ExampleUnitTest {

    @Test
    fun moodType_fromString_matchesCorrectly() {
        assertEquals(MoodType.HAPPY, MoodType.fromString("Joyful"))
        assertEquals(MoodType.CALM, MoodType.fromString("calm"))
        assertEquals(MoodType.SAD, MoodType.fromString("Tender"))
        assertEquals(MoodType.ANXIOUS, MoodType.fromString("Restless"))
        assertEquals(MoodType.ANGRY, MoodType.fromString("Stormy"))
        assertEquals(MoodType.OVERWHELMED, MoodType.fromString("overwhelmed"))
    }

    @Test
    fun crisisInterceptor_detectsDistressKeywords() {
        val aiService = SolaceAiService()
        assertTrue(aiService.checkCrisisSignal("I feel like I want to die tonight"))
        assertTrue(aiService.checkCrisisSignal("I want to hurt myself"))
        assertFalse(aiService.checkCrisisSignal("I had a stressful day at work"))
    }

    @Test
    fun crisisHelplines_containsLifeline988() {
        val has988 = CrisisHelplines.any { it.contact.contains("988") }
        assertTrue("988 Suicide & Crisis Lifeline should be included", has988)
    }

    @Test
    fun curatedActivities_boxBreathing_valid() {
        val boxBreathing = CuratedRegulationActivities.find { it.id == "box_breathing" }
        assertNotNull(boxBreathing)
        assertTrue(boxBreathing!!.durationSeconds > 0)
        assertTrue(boxBreathing.instructions.isNotEmpty())
    }

    @Test
    fun geminiChatModels_configuredCorrectly() {
        val flash = com.example.data.ai.GeminiChatModel.FLASH
        val argon = com.example.data.ai.GeminiChatModel.ARGON
        val lite = com.example.data.ai.GeminiChatModel.FLASH_LITE

        assertEquals("gemini-3.8-flash", flash.modelId)
        assertEquals("gemini-4-argon", argon.modelId)
        assertEquals("gemini-3.1-flash-lite", lite.modelId)
    }

    @Test
    fun chatbotRoles_haveSystemInstructions() {
        com.example.data.ai.ChatbotRole.entries.forEach { role ->
            assertTrue("Role ${role.id} should have system instructions", role.systemInstruction.isNotBlank())
            assertTrue("Role ${role.id} should have non-empty title", role.title.isNotBlank())
        }
    }

    // --- Issue 1 Tests: Weekly Mood Day Alignment ---

    @Test
    fun weeklyMoodTracker_todayIndicatorMatchesCurrentDeviceWeekday() {
        val week = calculateCurrentWeekMoods(emptyList())
        assertEquals(7, week.size)

        val cal = Calendar.getInstance().apply { firstDayOfWeek = Calendar.MONDAY }
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val expectedTodayIndex = (dayOfWeek - Calendar.MONDAY + 7) % 7

        week.forEachIndexed { index, item ->
            if (index == expectedTodayIndex) {
                assertTrue("Day at index $index must be marked as Today", item.isToday)
            } else {
                assertFalse("Day at index $index must NOT be marked as Today", item.isToday)
            }
        }
    }

    @Test
    fun weeklyMoodTracker_moodLoggedOnSpecificDayAlignsAccurately() {
        val cal = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
        }
        val currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val todayIndex = (currentDayOfWeek - Calendar.MONDAY + 7) % 7

        // Create a mood entry for today
        val todayMood = MoodEntryEntity(
            id = 1,
            mood = "calm",
            valence = 8,
            intensity = 4,
            tags = "quiet",
            note = "peaceful hour",
            timestamp = cal.timeInMillis
        )

        val week = calculateCurrentWeekMoods(listOf(todayMood))
        assertEquals(MoodType.CALM, week[todayIndex].mood)
        assertTrue(week[todayIndex].isToday)

        // Verify other days without entries have null mood
        week.forEachIndexed { index, item ->
            if (index != todayIndex) {
                assertNull("Day at index $index should have no mood", item.mood)
            }
        }
    }

    // --- Issue 3 Tests: User Greeting & Name Personalization ---

    @Test
    fun formatUserGreeting_withActualUserName_addressesUserByName() {
        val greeting = formatUserGreeting("Good morning", "Jayesh")
        assertEquals("Good morning, Jayesh.", greeting)

        val afternoonGreeting = formatUserGreeting("Good afternoon", "Sarah")
        assertEquals("Good afternoon, Sarah.", afternoonGreeting)
    }

    @Test
    fun formatUserGreeting_withPlaceholderOrBlank_omitsNameGracefully() {
        // Never call the user 'Sanctuary' or 'User' or 'Guest'
        assertEquals("Good morning.", formatUserGreeting("Good morning", "Sanctuary"))
        assertEquals("Good morning.", formatUserGreeting("Good morning", "sanctuary"))
        assertEquals("Good morning.", formatUserGreeting("Good morning", "User"))
        assertEquals("Good morning.", formatUserGreeting("Good morning", "Guest"))
        assertEquals("Good morning.", formatUserGreeting("Good morning", "Sanctuary Guest"))
        assertEquals("Good morning.", formatUserGreeting("Good morning", ""))
        assertEquals("Good morning.", formatUserGreeting("Good morning", "   "))
        assertEquals("Good morning.", formatUserGreeting("Good morning", null))
    }

    @Test
    fun adaptiveSanctuaryEngine_doesNotInsertSanctuaryAsUserName() {
        val aiService = SolaceAiService()
        val responseSanctuary = aiService.generateAdaptiveSanctuaryResponse(
            userName = "Sanctuary",
            currentMood = "calm",
            moodIntensity = 5,
            regulationMode = RegulationMode.LISTEN,
            history = emptyList(),
            latestUserMessage = "I feel so anxious and restless today"
        )
        assertFalse("AI response should NEVER call the user 'Sanctuary'", responseSanctuary.contains("Sanctuary"))

        val responseRealUser = aiService.generateAdaptiveSanctuaryResponse(
            userName = "Jayesh",
            currentMood = "calm",
            moodIntensity = 5,
            regulationMode = RegulationMode.LISTEN,
            history = emptyList(),
            latestUserMessage = "I feel so anxious and restless today"
        )
        assertTrue("AI response should warmly address the actual user by name", responseRealUser.contains("Jayesh"))
    }
}
