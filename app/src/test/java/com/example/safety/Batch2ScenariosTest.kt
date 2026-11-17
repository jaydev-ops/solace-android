package com.example.safety

import com.example.data.safety.ConversationContext
import com.example.data.safety.ConversationalStage
import com.example.data.safety.DeterministicEmotionalEngine
import com.example.data.safety.EmotionalClassificationEngine
import com.example.data.safety.ScenarioId
import com.example.data.safety.UserSituation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Solace+ Implementation Batch 2 Multi-Turn Test Suite
 * Covers Scenarios 6 to 10:
 * - Scenario 6: Misunderstood and Unheard
 * - Scenario 7: Regret and Guilt
 * - Scenario 8: Sleep Difficulties & Night Rumination
 * - Scenario 9: Social Anxiety & Fear of Judgement
 * - Scenario 10: Procrastination & Productivity Distress
 */
class Batch2ScenariosTest {

    private val convPrefix = "batch2_test_"

    @Before
    fun setUp() {
        for (i in 1..25) {
            DeterministicEmotionalEngine.clearSession("${convPrefix}$i")
        }
    }

    // =========================================================================
    // SCENARIO 6: MISUNDERSTOOD AND UNHEARD
    // =========================================================================

    @Test
    fun testScenario6_01_feelingsDismissed_multiTurnFlow() {
        val cid = "${convPrefix}6_1"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("Nobody understands what I'm trying to say.", ctx)
        assertTrue("Turn 1 should acknowledge misunderstood: ${turn1.responseText}",
            turn1.responseText.contains("misunderstood", ignoreCase = true) ||
            turn1.responseText.contains("language", ignoreCase = true) ||
            turn1.responseText.contains("hear", ignoreCase = true)
        )
        assertFalse("Must not ask permission", turn1.responseText.contains("would you like", ignoreCase = true))

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("Whenever I explain something, people interpret it differently and words get twisted.", ctx)
        assertTrue("Turn 2 should address twisted words / defending reality: ${turn2.responseText}",
            turn2.responseText.contains("twist", ignoreCase = true) ||
            turn2.responseText.contains("defend", ignoreCase = true) ||
            turn2.responseText.contains("explaining", ignoreCase = true) ||
            turn2.responseText.contains("observer", ignoreCase = true) ||
            turn2.responseText.contains("invisible", ignoreCase = true) ||
            turn2.responseText.contains("voice", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I just stopped explaining myself.", ctx)
        assertTrue("Turn 3 should validate healthy boundaries: ${turn3.responseText}",
            turn3.responseText.contains("boundary", ignoreCase = true) ||
            turn3.responseText.contains("explaining", ignoreCase = true) ||
            turn3.responseText.contains("preserve", ignoreCase = true) ||
            turn3.responseText.contains("protect", ignoreCase = true)
        )
    }

    @Test
    fun testScenario6_02_labeledOverlySensitive() {
        val cid = "${convPrefix}6_2"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("My family called me overly sensitive and said I'm overreacting.", ctx)
        assertTrue("Should reframe sensitivity: ${res.responseText}",
            res.responseText.contains("sensitive", ignoreCase = true) ||
            res.responseText.contains("depth", ignoreCase = true) ||
            res.responseText.contains("overreacting", ignoreCase = true)
        )
    }

    @Test
    fun testScenario6_03_invisibleInConversations() {
        val cid = "${convPrefix}6_3"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I feel invisible in conversations, like I'm talking to a brick wall.", ctx)
        assertTrue("Should acknowledge invisible feeling: ${res.responseText}",
            res.responseText.contains("invisible", ignoreCase = true) ||
            res.responseText.contains("observer", ignoreCase = true) ||
            res.responseText.contains("brick wall", ignoreCase = true) ||
            res.responseText.contains("misunderstood", ignoreCase = true)
        )
    }

    @Test
    fun testScenario6_04_energyProtectionReframing() {
        val cid = "${convPrefix}6_4"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("People constantly dismiss my feelings no matter what I say.", ctx)
        assertTrue("Should protect emotional energy: ${res.responseText}",
            res.responseText.contains("convincing", ignoreCase = true) ||
            res.responseText.contains("dismiss", ignoreCase = true) ||
            res.responseText.contains("energy", ignoreCase = true) ||
            res.responseText.contains("sanctuary", ignoreCase = true) ||
            res.responseText.contains("time", ignoreCase = true) ||
            res.responseText.contains("listening", ignoreCase = true) ||
            res.responseText.contains("interruption", ignoreCase = true)
        )
    }

    @Test
    fun testScenario6_05_sanctuaryQuietSpace() {
        val cid = "${convPrefix}6_5"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("Nobody cares how I feel, my feelings are always dismissed.", ctx)
        assertTrue("Should validate and offer presence: ${res.responseText}",
            res.responseText.contains("heard", ignoreCase = true) ||
            res.responseText.contains("sanctuary", ignoreCase = true) ||
            res.responseText.contains("dismiss", ignoreCase = true) ||
            res.responseText.contains("prove", ignoreCase = true) ||
            res.responseText.contains("energy", ignoreCase = true) ||
            res.responseText.contains("time", ignoreCase = true) ||
            res.responseText.contains("listening", ignoreCase = true) ||
            res.responseText.contains("interruption", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 7: REGRET AND GUILT
    // =========================================================================

    @Test
    fun testScenario7_01_regretAndGuilt_multiTurnFlow() {
        val cid = "${convPrefix}7_1"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I wish I could go back and change what I said.", ctx)
        assertTrue("Turn 1 should acknowledge regret and conscience: ${turn1.responseText}",
            turn1.responseText.contains("rewind", ignoreCase = true) ||
            turn1.responseText.contains("regret", ignoreCase = true) ||
            turn1.responseText.contains("undo", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("I hurt someone who genuinely cared about me.", ctx)
        assertTrue("Turn 2 should acknowledge hurting someone & accountability: ${turn2.responseText}",
            turn2.responseText.contains("hurt", ignoreCase = true) ||
            turn2.responseText.contains("accountability", ignoreCase = true) ||
            turn2.responseText.contains("apology", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I don't think I'll ever forgive myself.", ctx)
        assertTrue("Turn 3 should address self-forgiveness & guilt vs shame: ${turn3.responseText}",
            turn3.responseText.contains("shame", ignoreCase = true) ||
            turn3.responseText.contains("forgiving", ignoreCase = true) ||
            turn3.responseText.contains("mistake", ignoreCase = true) ||
            turn3.responseText.contains("weapon", ignoreCase = true) ||
            turn3.responseText.contains("apology", ignoreCase = true) ||
            turn3.responseText.contains("healing", ignoreCase = true) ||
            turn3.responseText.contains("honor", ignoreCase = true)
        )
    }

    @Test
    fun testScenario7_02_replayingPastMistakes() {
        val cid = "${convPrefix}7_2"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I keep replaying the mistake in my head every night.", ctx)
        assertTrue("Should address mental replay: ${res.responseText}",
            res.responseText.contains("replay", ignoreCase = true) ||
            res.responseText.contains("history", ignoreCase = true) ||
            res.responseText.contains("torture", ignoreCase = true) ||
            res.responseText.contains("mistake", ignoreCase = true)
        )
    }

    @Test
    fun testScenario7_03_guiltVsShame() {
        val cid = "${convPrefix}7_3"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("The guilt is eating me up inside and I feel so haunted by what I did.", ctx)
        assertTrue("Should address guilt eating away / shame: ${res.responseText}",
            res.responseText.contains("guilt", ignoreCase = true) ||
            res.responseText.contains("shame", ignoreCase = true) ||
            res.responseText.contains("forgive", ignoreCase = true) ||
            res.responseText.contains("replay", ignoreCase = true) ||
            res.responseText.contains("history", ignoreCase = true) ||
            res.responseText.contains("torture", ignoreCase = true)
        )
    }

    @Test
    fun testScenario7_04_pastSelfCompassion() {
        val cid = "${convPrefix}7_4"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I ruined everything and wish I could rewind time.", ctx)
        assertTrue("Should offer perspective on past decisions: ${res.responseText}",
            res.responseText.contains("moment", ignoreCase = true) ||
            res.responseText.contains("past", ignoreCase = true) ||
            res.responseText.contains("rewind", ignoreCase = true) ||
            res.responseText.contains("undo", ignoreCase = true)
        )
    }

    @Test
    fun testScenario7_05_carryingGuiltAlone() {
        val cid = "${convPrefix}7_5"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I can't forgive myself for how I treated them badly.", ctx)
        assertTrue("Should address self-forgiveness or unpacking guilt: ${res.responseText}",
            res.responseText.contains("forgiv", ignoreCase = true) ||
            res.responseText.contains("guilt", ignoreCase = true) ||
            res.responseText.contains("chest", ignoreCase = true) ||
            res.responseText.contains("weapon", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 8: SLEEP DIFFICULTIES & NIGHT RUMINATION
    // =========================================================================

    @Test
    fun testScenario8_01_sleepRumination_multiTurnFlow() {
        val cid = "${convPrefix}8_1"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("It's 3 AM and I still can't sleep.", ctx)
        assertTrue("Turn 1 should acknowledge 3 AM insomnia: ${turn1.responseText}",
            turn1.responseText.contains("3 AM", ignoreCase = true) ||
            turn1.responseText.contains("sleep", ignoreCase = true) ||
            turn1.responseText.contains("rest", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("My brain won't stop thinking about everything.", ctx)
        assertTrue("Turn 2 should address racing mind in quiet house: ${turn2.responseText}",
            turn2.responseText.contains("brain", ignoreCase = true) ||
            turn2.responseText.contains("quiet", ignoreCase = true) ||
            turn2.responseText.contains("racing", ignoreCase = true) ||
            turn2.responseText.contains("notes", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I'm thinking about everything that went wrong.", ctx)
        assertTrue("Turn 3 should address nighttime thoughts amplification: ${turn3.responseText}",
            turn3.responseText.contains("night", ignoreCase = true) ||
            turn3.responseText.contains("daylight", ignoreCase = true) ||
            turn3.responseText.contains("exhaustion", ignoreCase = true) ||
            turn3.responseText.contains("looping", ignoreCase = true)
        )
    }

    @Test
    fun testScenario8_02_somaticGroundingBreathing() {
        val cid = "${convPrefix}8_2"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I'm tossing and turning and can't fall asleep.", ctx)
        assertTrue("Should offer calming grounding or breathing: ${res.responseText}",
            res.responseText.contains("breath", ignoreCase = true) ||
            res.responseText.contains("mattress", ignoreCase = true) ||
            res.responseText.contains("rest", ignoreCase = true) ||
            res.responseText.contains("sleep", ignoreCase = true)
        )
    }

    @Test
    fun testScenario8_03_dreadingTomorrowMorning() {
        val cid = "${convPrefix}8_3"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I'm dreading waking up tomorrow, wide awake at night.", ctx)
        assertTrue("Should address dreading tomorrow: ${res.responseText}",
            res.responseText.contains("tomorrow", ignoreCase = true) ||
            res.responseText.contains("surrender", ignoreCase = true) ||
            res.responseText.contains("rest", ignoreCase = true) ||
            res.responseText.contains("night", ignoreCase = true)
        )
    }

    @Test
    fun testScenario8_04_lateNightLoneliness() {
        val cid = "${convPrefix}8_4"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("Staring at the ceiling feeling so alone at night.", ctx)
        assertTrue("Should acknowledge quiet night and presence: ${res.responseText}",
            res.responseText.contains("ceiling", ignoreCase = true) ||
            res.responseText.contains("night", ignoreCase = true) ||
            res.responseText.contains("alone", ignoreCase = true) ||
            res.responseText.contains("space", ignoreCase = true) ||
            res.responseText.contains("isolating", ignoreCase = true) ||
            res.responseText.contains("asleep", ignoreCase = true) ||
            res.responseText.contains("restorative", ignoreCase = true) ||
            res.responseText.contains("dark", ignoreCase = true)
        )
    }

    @Test
    fun testScenario8_05_resettingFrustration() {
        val cid = "${convPrefix}8_5"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I've been wide awake at night with trouble falling asleep.", ctx)
        assertTrue("Should suggest rest or getting up for water: ${res.responseText}",
            res.responseText.contains("water", ignoreCase = true) ||
            res.responseText.contains("chair", ignoreCase = true) ||
            res.responseText.contains("rest", ignoreCase = true) ||
            res.responseText.contains("sleep", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 9: SOCIAL ANXIETY & FEAR OF JUDGEMENT
    // =========================================================================

    @Test
    fun testScenario9_01_socialAnxiety_multiTurnFlow() {
        val cid = "${convPrefix}9_1"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I feel awkward around new people.", ctx)
        assertTrue("Turn 1 should acknowledge social nervousness: ${turn1.responseText}",
            turn1.responseText.contains("nervous", ignoreCase = true) ||
            turn1.responseText.contains("people", ignoreCase = true) ||
            turn1.responseText.contains("protect", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("I keep thinking they'll find me boring.", ctx)
        assertTrue("Turn 2 should address spotlight effect or fear of boring: ${turn2.responseText}",
            turn2.responseText.contains("spotlight", ignoreCase = true) ||
            turn2.responseText.contains("boring", ignoreCase = true) ||
            turn2.responseText.contains("critic", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I don't even know what to say.", ctx)
        assertTrue("Turn 3 should relieve pressure to perform: ${turn3.responseText}",
            turn3.responseText.contains("pressure", ignoreCase = true) ||
            turn3.responseText.contains("charismatic", ignoreCase = true) ||
            turn3.responseText.contains("question", ignoreCase = true) ||
            turn3.responseText.contains("pause", ignoreCase = true) ||
            turn3.responseText.contains("scorecard", ignoreCase = true) ||
            turn3.responseText.contains("anxiety", ignoreCase = true) ||
            turn3.responseText.contains("words", ignoreCase = true) ||
            turn3.responseText.contains("feeling", ignoreCase = true)
        )
    }

    @Test
    fun testScenario9_02_awkwardSilenceNormalization() {
        val cid = "${convPrefix}9_2"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I have so much social anxiety and fear of being judged.", ctx)
        assertTrue("Should normalize social anxiety: ${res.responseText}",
            res.responseText.contains("nervous", ignoreCase = true) ||
            res.responseText.contains("evaluating", ignoreCase = true) ||
            res.responseText.contains("pause", ignoreCase = true) ||
            res.responseText.contains("judgment", ignoreCase = true) ||
            res.responseText.contains("judged", ignoreCase = true)
        )
    }

    @Test
    fun testScenario9_03_postEventMentalScorecard() {
        val cid = "${convPrefix}9_3"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I'm replaying the conversation and dissecting every word I said.", ctx)
        assertTrue("Should dispel mental scorecard: ${res.responseText}",
            res.responseText.contains("scorecard", ignoreCase = true) ||
            res.responseText.contains("replaying", ignoreCase = true) ||
            res.responseText.contains("dissecting", ignoreCase = true) ||
            res.responseText.contains("critic", ignoreCase = true)
        )
    }

    @Test
    fun testScenario9_04_socialBatteryPermission() {
        val cid = "${convPrefix}9_4"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("My social anxiety makes me scared to speak up and freezing in groups.", ctx)
        assertTrue("Should grant permission to pause or leave: ${res.responseText}",
            res.responseText.contains("battery", ignoreCase = true) ||
            res.responseText.contains("air", ignoreCase = true) ||
            res.responseText.contains("pressure", ignoreCase = true) ||
            res.responseText.contains("pause", ignoreCase = true) ||
            res.responseText.contains("room", ignoreCase = true)
        )
    }

    @Test
    fun testScenario9_05_publicSpeakingVigilance() {
        val cid = "${convPrefix}9_5"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I have presentation anxiety and public speaking fear.", ctx)
        assertTrue("Should acknowledge public speaking vigilance: ${res.responseText}",
            res.responseText.contains("vigilance", ignoreCase = true) ||
            res.responseText.contains("public", ignoreCase = true) ||
            res.responseText.contains("energy", ignoreCase = true) ||
            res.responseText.contains("room", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 10: PROCRASTINATION & PRODUCTIVITY DISTRESS
    // =========================================================================

    @Test
    fun testScenario10_01_procrastination_multiTurnFlow() {
        val cid = "${convPrefix}10_1"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I've wasted the entire day scrolling reels.", ctx)
        assertTrue("Turn 1 should acknowledge scrolling as escape from overwhelm: ${turn1.responseText}",
            turn1.responseText.contains("scrolling", ignoreCase = true) ||
            turn1.responseText.contains("overwhelm", ignoreCase = true) ||
            turn1.responseText.contains("blame", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("I have so much work pending.", ctx)
        assertTrue("Turn 2 should address mountain of work / smallest piece: ${turn2.responseText}",
            turn2.responseText.contains("mountain", ignoreCase = true) ||
            turn2.responseText.contains("pending", ignoreCase = true) ||
            turn2.responseText.contains("smallest", ignoreCase = true) ||
            turn2.responseText.contains("piece", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I just can't get myself to start.", ctx)
        assertTrue("Turn 3 should encourage messy first draft: ${turn3.responseText}",
            turn3.responseText.contains("draft", ignoreCase = true) ||
            turn3.responseText.contains("line", ignoreCase = true) ||
            turn3.responseText.contains("motivation", ignoreCase = true) ||
            turn3.responseText.contains("timer", ignoreCase = true)
        )
    }

    @Test
    fun testScenario10_02_phoneDoomscrollingIntervention() {
        val cid = "${convPrefix}10_2"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I'm doomscrolling on my phone instead of doing assignments.", ctx)
        assertTrue("Should offer 20-minute timer or distance: ${res.responseText}",
            res.responseText.contains("room", ignoreCase = true) ||
            res.responseText.contains("timer", ignoreCase = true) ||
            res.responseText.contains("minutes", ignoreCase = true) ||
            res.responseText.contains("scrolling", ignoreCase = true)
        )
    }

    @Test
    fun testScenario10_03_perfectionismRoot() {
        val cid = "${convPrefix}10_3"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I'm procrastinating because I'm scared I'll do it poorly.", ctx)
        assertTrue("Should address perfectionism: ${res.responseText}",
            res.responseText.contains("perfectionism", ignoreCase = true) ||
            res.responseText.contains("draft", ignoreCase = true) ||
            res.responseText.contains("blank", ignoreCase = true) ||
            res.responseText.contains("starting", ignoreCase = true)
        )
    }

    @Test
    fun testScenario10_04_guiltFreeRest() {
        val cid = "${convPrefix}10_4"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("The constant buzz of work is giving me executive dysfunction and paralyzed by work.", ctx)
        assertTrue("Should address constant buzz or guilt-free rest: ${res.responseText}",
            res.responseText.contains("buzz", ignoreCase = true) ||
            res.responseText.contains("rest", ignoreCase = true) ||
            res.responseText.contains("mountain", ignoreCase = true) ||
            res.responseText.contains("heavy", ignoreCase = true)
        )
    }

    @Test
    fun testScenario10_05_resetMomentWater() {
        val cid = "${convPrefix}10_5"
        val ctx = ConversationContext(conversationId = cid)

        val res = DeterministicEmotionalEngine.processTurn("I feel so guilty about wasting time all afternoon.", ctx)
        assertTrue("Should offer grounding reset / today not ruined: ${res.responseText}",
            res.responseText.contains("water", ignoreCase = true) ||
            res.responseText.contains("ruined", ignoreCase = true) ||
            res.responseText.contains("hour", ignoreCase = true) ||
            res.responseText.contains("breath", ignoreCase = true) ||
            res.responseText.contains("draft", ignoreCase = true) ||
            res.responseText.contains("scrolling", ignoreCase = true)
        )
    }
}
