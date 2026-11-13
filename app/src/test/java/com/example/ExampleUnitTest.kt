package com.example

import com.example.data.ai.CrisisHelplines
import com.example.data.ai.SolaceAiService
import com.example.data.model.CuratedRegulationActivities
import com.example.data.model.MoodType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

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
}
