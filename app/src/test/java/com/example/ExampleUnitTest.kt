package com.example

import com.example.data.ai.CrisisHelplines
import com.example.data.ai.SolaceAiService
import com.example.data.local.entity.MoodEntryEntity
import com.example.data.model.CuratedRegulationActivities
import com.example.data.model.MoodType
import com.example.data.model.RegulationMode
import com.example.data.safety.ContextAnalyzer
import com.example.data.safety.EscalationManager
import com.example.data.safety.RiskDecisionEngine
import com.example.data.safety.RiskLevel
import com.example.data.safety.RiskState
import com.example.data.safety.SafetyResponseHandler
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

    // --- Graduated Mental Health Risk Detection & Escalation Tests ---

    @Test
    fun riskEvaluation_level1_distressStatements() {
        val result1 = EscalationManager.evaluate("I had a stressful day.")
        assertEquals(RiskLevel.LEVEL_1_DISTRESS, result1.level)

        val result2 = EscalationManager.evaluate("I feel lonely.")
        assertEquals(RiskLevel.LEVEL_1_DISTRESS, result2.level)
        assertFalse(result2.isEmergency)
    }

    @Test
    fun riskEvaluation_level2_significantDistressStatements() {
        val result1 = EscalationManager.evaluate("Nothing in my life is getting better.")
        assertEquals(RiskLevel.LEVEL_2_ELEVATED, result1.level)
        assertTrue(result1.showSupportCard)
        assertNotNull(result1.overrideResponse)

        val result2 = EscalationManager.evaluate("Everyone would be better without me.")
        assertEquals(RiskLevel.LEVEL_2_ELEVATED, result2.level)
        // Direct compassionate safety screening question must be asked
        assertTrue(result2.overrideResponse!!.contains("thoughts about hurting yourself or ending your life"))
    }

    @Test
    fun riskEvaluation_level3_suicidalIdeationStatements() {
        val result1 = EscalationManager.evaluate("I wish I could go to sleep and never wake up.")
        assertEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, result1.level)
        assertTrue(result1.showSupportCard)
        assertTrue(result1.overrideResponse!!.contains("Are you having thoughts of ending your life at this moment?"))

        val result2 = EscalationManager.evaluate("Sometimes I think about ending my life.")
        assertEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, result2.level)
    }

    @Test
    fun riskEvaluation_level4_criticalEmergencyStatements() {
        val result1 = EscalationManager.evaluate("I am planning to end my life tonight.")
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, result1.level)
        assertTrue(result1.isEmergency)
        assertTrue(result1.showEmergencyUI)
        assertTrue(result1.overrideResponse!!.contains("112"))

        val result2 = EscalationManager.evaluate("I have already attempted suicide.")
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, result2.level)
        assertTrue(result2.isEmergency)
    }

    @Test
    fun riskEvaluation_level0_figurativeExpressionsFiltered() {
        // "I'm dying of laughter" must NOT trigger suicidal ideation
        val result1 = EscalationManager.evaluate("I'm dying of laughter.")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result1.level)
        assertFalse(result1.isEmergency)

        val result2 = EscalationManager.evaluate("This homework is killing me.")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result2.level)

        val result3 = EscalationManager.evaluate("I'm dying to see that movie.")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result3.level)
    }

    @Test
    fun riskEvaluation_level0_fictionalContextFiltered() {
        // "My fictional character wants to die" must not cause false emergency
        val result = EscalationManager.evaluate("My fictional character wants to die in chapter 3.")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result.level)
        assertFalse(result.isEmergency)
    }

    @Test
    fun riskEvaluation_level0_negationHandledSafely() {
        // "I don't want to hurt myself" must not trigger suicide ideation
        val result = EscalationManager.evaluate("I don't want to hurt myself.")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result.level)
        assertFalse(result.isEmergency)
    }

    @Test
    fun riskEvaluation_selfHarmSafetyAssessment() {
        // "I want to hurt myself but don't want to die"
        val result = EscalationManager.evaluate("I want to hurt myself but don't want to die.")
        assertTrue(
            "Self-harm should trigger an elevated or critical safety assessment",
            result.level.isAtLeast(RiskLevel.LEVEL_2_ELEVATED)
        )
    }

    @Test
    fun riskEscalation_level4CannotBeDowngradedByCasualMessage() {
        val state0 = RiskState()
        // Turn 1: Emergency trigger
        val result1 = EscalationManager.evaluate(
            userMessage = "I have decided to end my life tonight",
            currentState = state0
        )
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, result1.level)
        assertTrue(result1.updatedState.emergencyTriggered)

        // Turn 2: User says something casual or switches topic
        val result2 = EscalationManager.evaluate(
            userMessage = "Haha anyway what's the weather today?",
            currentState = result1.updatedState
        )
        // Emergency lock: must stay LEVEL_4_CRITICAL
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, result2.level)
        assertTrue(result2.isEmergency)
    }

    @Test
    fun riskEscalation_screeningQuestionConfirmationEscalatesToIdeation() {
        val historyWithScreening = listOf(
            "assistant" to "Can I ask something important? Have you recently had thoughts about hurting yourself or ending your life?"
        )
        val result = EscalationManager.evaluate(
            userMessage = "Yes, honestly I have.",
            history = historyWithScreening
        )
        assertEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, result.level)
    }
}
