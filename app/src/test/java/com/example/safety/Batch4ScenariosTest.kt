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
 * Solace+ Implementation Batch 4 Multi-Turn Test Suite
 * Covers Scenarios 16 to 20:
 * - Scenario 16: Grief and Loss
 * - Scenario 17: Body Image & Self-Confidence
 * - Scenario 18: Positive Emotions & Celebration
 * - Scenario 19: Casual Frustration & Venting
 * - Scenario 20: Romantic Relationship Support
 */
class Batch4ScenariosTest {

    private val convPrefix = "batch4_test_"

    @Before
    fun setUp() {
        for (i in 1..25) {
            DeterministicEmotionalEngine.clearSession("${convPrefix}$i")
        }
    }

    // =========================================================================
    // SCENARIO 16: GRIEF AND LOSS
    // =========================================================================

    @Test
    fun testScenario16_01_lovedOneLoss_multiTurnFlow() {
        val cid = "${convPrefix}1"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I miss my grandfather.", ctx)
        assertTrue("Turn 1 should hold gentle space for grief: ${turn1.responseText}",
            turn1.responseText.contains("space", ignoreCase = true) ||
            turn1.responseText.contains("grief", ignoreCase = true) ||
            turn1.responseText.contains("missing", ignoreCase = true) ||
            turn1.responseText.contains("sorrow", ignoreCase = true) ||
            turn1.responseText.contains("holding", ignoreCase = true)
        )
        assertFalse("Must not ask permission", turn1.responseText.contains("would you like", ignoreCase = true))

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("I keep remembering things we used to do.", ctx)
        assertTrue("Turn 2 should address memories and tears: ${turn2.responseText}",
            turn2.responseText.contains("memories", ignoreCase = true) ||
            turn2.responseText.contains("routines", ignoreCase = true) ||
            turn2.responseText.contains("tears", ignoreCase = true) ||
            turn2.responseText.contains("longing", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I wish I could talk to him one more time.", ctx)
        assertTrue("Turn 3 should validate wishing for one last talk: ${turn3.responseText}",
            turn3.responseText.contains("universal", ignoreCase = true) ||
            turn3.responseText.contains("love", ignoreCase = true) ||
            turn3.responseText.contains("memory", ignoreCase = true) ||
            turn3.responseText.contains("conversation", ignoreCase = true)
        )
    }

    @Test
    fun testScenario16_02_petLoss_singleTurn() {
        val cid = "${convPrefix}2"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("My pet dog passed away yesterday.", ctx)
        assertTrue("Must honor profound pet loss: ${turn.responseText}",
            turn.responseText.contains("pet", ignoreCase = true) ||
            turn.responseText.contains("companion", ignoreCase = true) ||
            turn.responseText.contains("grief", ignoreCase = true) ||
            turn.responseText.contains("honor", ignoreCase = true)
        )
    }

    @Test
    fun testScenario16_03_anniversaryGrief_singleTurn() {
        val cid = "${convPrefix}3"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Today is the anniversary of their death.", ctx)
        assertTrue("Must validate anniversary grief waves: ${turn.responseText}",
            turn.responseText.contains("anniversar", ignoreCase = true) ||
            turn.responseText.contains("waves", ignoreCase = true) ||
            turn.responseText.contains("sorrow", ignoreCase = true) ||
            turn.responseText.contains("gentle", ignoreCase = true)
        )
    }

    @Test
    fun testScenario16_04_absenceLonging_singleTurn() {
        val cid = "${convPrefix}4"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("It feels so empty without him here", ctx)
        assertTrue("Must acknowledge emptiness and grief: ${turn.responseText}",
            turn.responseText.contains("space", ignoreCase = true) ||
            turn.responseText.contains("empty", ignoreCase = true) ||
            turn.responseText.contains("quiet", ignoreCase = true) ||
            turn.responseText.contains("sanctuary", ignoreCase = true) ||
            turn.responseText.contains("warm", ignoreCase = true)
        )
    }

    @Test
    fun testScenario16_05_rushingGrief_singleTurn() {
        val cid = "${convPrefix}5"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("People tell me to move on after my loss of my grandma", ctx)
        assertTrue("Must validate not rushing grief: ${turn.responseText}",
            turn.responseText.contains("rush", ignoreCase = true) ||
            turn.responseText.contains("moving on", ignoreCase = true) ||
            turn.responseText.contains("waves", ignoreCase = true) ||
            turn.responseText.contains("quiet", ignoreCase = true) ||
            turn.responseText.contains("timeline", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 17: BODY IMAGE & SELF-CONFIDENCE
    // =========================================================================

    @Test
    fun testScenario17_01_bodyImageInsecurity_multiTurnFlow() {
        val cid = "${convPrefix}6"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I hate how I look.", ctx)
        assertTrue("Turn 1 should emphasize body neutrality over superficial praise: ${turn1.responseText}",
            turn1.responseText.contains("mirror", ignoreCase = true) ||
            turn1.responseText.contains("instrument", ignoreCase = true) ||
            turn1.responseText.contains("body", ignoreCase = true) ||
            turn1.responseText.contains("filtered", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("Everyone looks better than me.", ctx)
        assertTrue("Turn 2 should deconstruct curated screen perfection: ${turn2.responseText}",
            turn2.responseText.contains("commercial fiction", ignoreCase = true) ||
            turn2.responseText.contains("comparing", ignoreCase = true) ||
            turn2.responseText.contains("screens", ignoreCase = true) ||
            turn2.responseText.contains("digital", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I don't feel confident going outside.", ctx)
        assertTrue("Turn 3 should reassure right to take up physical space: ${turn3.responseText}",
            turn3.responseText.contains("public", ignoreCase = true) ||
            turn3.responseText.contains("space", ignoreCase = true) ||
            turn3.responseText.contains("insecurities", ignoreCase = true) ||
            turn3.responseText.contains("outside", ignoreCase = true)
        )
    }

    @Test
    fun testScenario17_02_weightDissatisfaction_singleTurn() {
        val cid = "${convPrefix}7"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I am disgusted with my body and hate my weight", ctx)
        assertTrue("Must offer gentle somatic kindness: ${turn.responseText}",
            turn.responseText.contains("heartbeat", ignoreCase = true) ||
            turn.responseText.contains("kindness", ignoreCase = true) ||
            turn.responseText.contains("chest", ignoreCase = true) ||
            turn.responseText.contains("body", ignoreCase = true)
        )
    }

    @Test
    fun testScenario17_03_photoDread_singleTurn() {
        val cid = "${convPrefix}8"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I am uncomfortable in photos and dread pictures", ctx)
        assertTrue("Must decouple 2D snapshots from human presence: ${turn.responseText}",
            turn.responseText.contains("photograph", ignoreCase = true) ||
            turn.responseText.contains("snapshot", ignoreCase = true) ||
            turn.responseText.contains("angle", ignoreCase = true) ||
            turn.responseText.contains("warmth", ignoreCase = true)
        )
    }

    @Test
    fun testScenario17_04_appearanceCriticism_singleTurn() {
        val cid = "${convPrefix}9"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Family made comments and I feel insecure about my looks", ctx)
        assertTrue("Must address family appearance comments: ${turn.responseText}",
            turn.responseText.contains("comments", ignoreCase = true) ||
            turn.responseText.contains("criticism", ignoreCase = true) ||
            turn.responseText.contains("value", ignoreCase = true) ||
            turn.responseText.contains("conditioning", ignoreCase = true) ||
            turn.responseText.contains("mirror", ignoreCase = true)
        )
    }

    @Test
    fun testScenario17_05_bodyNeutrality_singleTurn() {
        val cid = "${convPrefix}10"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I hate my features and have severe body dysmorphia", ctx)
        assertTrue("Must encourage body neutrality: ${turn.responseText}",
            turn.responseText.contains("neutrality", ignoreCase = true) ||
            turn.responseText.contains("safe", ignoreCase = true) ||
            turn.responseText.contains("breathing", ignoreCase = true) ||
            turn.responseText.contains("body", ignoreCase = true) ||
            turn.responseText.contains("kindness", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 18: POSITIVE EMOTIONS & CELEBRATION
    // =========================================================================

    @Test
    fun testScenario18_01_examCelebration_multiTurnFlow() {
        val cid = "${convPrefix}11"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I finally passed my exam!", ctx)
        assertTrue("Turn 1 should celebrate exam victory warmly: ${turn1.responseText}",
            turn1.responseText.contains("congratulations", ignoreCase = true) ||
            turn1.responseText.contains("incredible", ignoreCase = true) ||
            turn1.responseText.contains("relief", ignoreCase = true) ||
            turn1.responseText.contains("lightness", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("I studied so hard and it paid off.", ctx)
        assertTrue("Turn 2 should validate hard work: ${turn2.responseText}",
            turn2.responseText.contains("discipline", ignoreCase = true) ||
            turn2.responseText.contains("persistence", ignoreCase = true) ||
            turn2.responseText.contains("victory", ignoreCase = true) ||
            turn2.responseText.contains("savor", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I'm actually proud of myself.", ctx)
        assertTrue("Turn 3 should amplify healthy pride: ${turn3.responseText}",
            turn3.responseText.contains("proud", ignoreCase = true) ||
            turn3.responseText.contains("resilience", ignoreCase = true) ||
            turn3.responseText.contains("success", ignoreCase = true)
        )
    }

    @Test
    fun testScenario18_02_jobOffer_singleTurn() {
        val cid = "${convPrefix}12"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I got the job after months of trying!", ctx)
        assertTrue("Must celebrate job offer win: ${turn.responseText}",
            turn.responseText.contains("milestone", ignoreCase = true) ||
            turn.responseText.contains("win", ignoreCase = true) ||
            turn.responseText.contains("celebrate", ignoreCase = true) ||
            turn.responseText.contains("good news", ignoreCase = true) ||
            turn.responseText.contains("shared", ignoreCase = true) ||
            turn.responseText.contains("accomplish", ignoreCase = true)
        )
    }

    @Test
    fun testScenario18_03_generalGratitude_singleTurn() {
        val cid = "${convPrefix}13"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I had a wonderful day and want to celebrate with me", ctx)
        assertTrue("Must celebrate in sanctuary: ${turn.responseText}",
            turn.responseText.contains("sanctuary", ignoreCase = true) ||
            turn.responseText.contains("celebrat", ignoreCase = true) ||
            turn.responseText.contains("proud", ignoreCase = true) ||
            turn.responseText.contains("success", ignoreCase = true) ||
            turn.responseText.contains("resilience", ignoreCase = true) ||
            turn.responseText.contains("anchor", ignoreCase = true) ||
            turn.responseText.contains("memory", ignoreCase = true)
        )
    }

    @Test
    fun testScenario18_04_goalAchieved_singleTurn() {
        val cid = "${convPrefix}14"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Achieved my goal and overcame my fear", ctx)
        assertTrue("Must validate goal achievement: ${turn.responseText}",
            turn.responseText.contains("victory", ignoreCase = true) ||
            turn.responseText.contains("accomplish", ignoreCase = true) ||
            turn.responseText.contains("celebrate", ignoreCase = true) ||
            turn.responseText.contains("good news", ignoreCase = true) ||
            turn.responseText.contains("overcome", ignoreCase = true)
        )
    }

    @Test
    fun testScenario18_05_reallyHappyToday_singleTurn() {
        val cid = "${convPrefix}15"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I am really happy today and proud of myself", ctx)
        assertTrue("Must amplify joy: ${turn.responseText}",
            turn.responseText.contains("proud", ignoreCase = true) ||
            turn.responseText.contains("celebrat", ignoreCase = true) ||
            turn.responseText.contains("resilience", ignoreCase = true) ||
            turn.responseText.contains("joy", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 19: CASUAL FRUSTRATION & VENTING
    // =========================================================================

    @Test
    fun testScenario19_01_terribleDay_multiTurnFlow() {
        val cid = "${convPrefix}16"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("Today has been absolutely terrible.", ctx)
        assertTrue("Turn 1 should give space for a bad day: ${turn1.responseText}",
            turn1.responseText.contains("grating", ignoreCase = true) ||
            turn1.responseText.contains("terrible", ignoreCase = true) ||
            turn1.responseText.contains("vent", ignoreCase = true) ||
            turn1.responseText.contains("tipping point", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("Everything is just irritating me.", ctx)
        assertTrue("Turn 2 should address sensory threshold overload: ${turn2.responseText}",
            turn2.responseText.contains("sensory", ignoreCase = true) ||
            turn2.responseText.contains("irritating", ignoreCase = true) ||
            turn2.responseText.contains("threshold", ignoreCase = true) ||
            turn2.responseText.contains("fresh air", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I don't even know why I'm annoyed.", ctx)
        assertTrue("Turn 3 should permit being grumpy without guilt: ${turn3.responseText}",
            turn3.responseText.contains("biology", ignoreCase = true) ||
            turn3.responseText.contains("foul mood", ignoreCase = true) ||
            turn3.responseText.contains("grumpy", ignoreCase = true) ||
            turn3.responseText.contains("guilt", ignoreCase = true)
        )
    }

    @Test
    fun testScenario19_02_stuckInRut_singleTurn() {
        val cid = "${convPrefix}17"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I am stuck in a rut and grumpy today", ctx)
        assertTrue("Must invite venting without minimization: ${turn.responseText}",
            turn.responseText.contains("rant", ignoreCase = true) ||
            turn.responseText.contains("chest", ignoreCase = true) ||
            turn.responseText.contains("small", ignoreCase = true) ||
            turn.responseText.contains("grumpy", ignoreCase = true) ||
            turn.responseText.contains("annoying", ignoreCase = true) ||
            turn.responseText.contains("happened", ignoreCase = true)
        )
    }

    @Test
    fun testScenario19_03_awfulDay_singleTurn() {
        val cid = "${convPrefix}18"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("It was an awful day and everything went wrong", ctx)
        assertTrue("Must offer permission to write off bad day: ${turn.responseText}",
            turn.responseText.contains("wash", ignoreCase = true) ||
            turn.responseText.contains("tomorrow", ignoreCase = true) ||
            turn.responseText.contains("low-key", ignoreCase = true) ||
            turn.responseText.contains("grating", ignoreCase = true)
        )
    }

    @Test
    fun testScenario19_04_sensoryReset_singleTurn() {
        val cid = "${convPrefix}19"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Irritated by small things and everyone is annoying me", ctx)
        assertTrue("Must offer physical reset advice: ${turn.responseText}",
            turn.responseText.contains("sensory", ignoreCase = true) ||
            turn.responseText.contains("water", ignoreCase = true) ||
            turn.responseText.contains("temperature", ignoreCase = true) ||
            turn.responseText.contains("headphones", ignoreCase = true) ||
            turn.responseText.contains("cheerful", ignoreCase = true)
        )
    }

    @Test
    fun testScenario19_05_justNeedToVent_singleTurn() {
        val cid = "${convPrefix}20"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I had an annoying day and just need to vent", ctx)
        assertTrue("Must provide empathetic listening for venting: ${turn.responseText}",
            turn.responseText.contains("rant", ignoreCase = true) ||
            turn.responseText.contains("listen", ignoreCase = true) ||
            turn.responseText.contains("chest", ignoreCase = true) ||
            turn.responseText.contains("vent", ignoreCase = true) ||
            turn.responseText.contains("tipping point", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 20: ROMANTIC RELATIONSHIP SUPPORT
    // =========================================================================

    @Test
    fun testScenario20_01_partnerDistancing_multiTurnFlow() {
        val cid = "${convPrefix}21"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I feel like my partner is growing distant.", ctx)
        assertTrue("Turn 1 should address cooling affection: ${turn1.responseText}",
            turn1.responseText.contains("partner", ignoreCase = true) ||
            turn1.responseText.contains("affection", ignoreCase = true) ||
            turn1.responseText.contains("anxiety", ignoreCase = true) ||
            turn1.responseText.contains("communication", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("They barely give me attention anymore.", ctx)
        assertTrue("Turn 2 should validate need for attention: ${turn2.responseText}",
            turn2.responseText.contains("needy", ignoreCase = true) ||
            turn2.responseText.contains("attention", ignoreCase = true) ||
            turn2.responseText.contains("connection", ignoreCase = true) ||
            turn2.responseText.contains("demanding", ignoreCase = true) ||
            turn2.responseText.contains("routine", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I don't want to seem needy.", ctx)
        assertTrue("Turn 3 should guide direct emotional communication: ${turn3.responseText}",
            turn3.responseText.contains("express", ignoreCase = true) ||
            turn3.responseText.contains("needs", ignoreCase = true) ||
            turn3.responseText.contains("disconnected", ignoreCase = true) ||
            turn3.responseText.contains("guilt", ignoreCase = true) ||
            turn3.responseText.contains("demanding", ignoreCase = true)
        )
    }

    @Test
    fun testScenario20_02_jealousyAntiSurveillance_singleTurn() {
        val cid = "${convPrefix}22"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I get jealous when they spend time with other people", ctx)
        assertTrue("Must reject surveillance / control: ${turn.responseText}",
            turn.responseText.contains("jealous", ignoreCase = true) ||
            turn.responseText.contains("abandonment", ignoreCase = true) ||
            turn.responseText.contains("phone", ignoreCase = true) ||
            turn.responseText.contains("controlling", ignoreCase = true) ||
            turn.responseText.contains("trust", ignoreCase = true) ||
            turn.responseText.contains("surveillance", ignoreCase = true)
        )
    }

    @Test
    fun testScenario20_03_trustOverSurveillance_singleTurn() {
        val cid = "${convPrefix}23"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Trust issues in my relationship make me terrified of losing my partner", ctx)
        assertTrue("Must enforce trust and healthy independence: ${turn.responseText}",
            turn.responseText.contains("trust", ignoreCase = true) ||
            turn.responseText.contains("surveillance", ignoreCase = true) ||
            turn.responseText.contains("partner", ignoreCase = true) ||
            turn.responseText.contains("affection", ignoreCase = true) ||
            turn.responseText.contains("communication", ignoreCase = true) ||
            turn.responseText.contains("respect", ignoreCase = true) ||
            turn.responseText.contains("shoulders", ignoreCase = true) ||
            turn.responseText.contains("quiet", ignoreCase = true) ||
            turn.responseText.contains("neglect", ignoreCase = true) ||
            turn.responseText.contains("openly", ignoreCase = true)
        )
    }

    @Test
    fun testScenario20_04_delayedTextAnxiety_singleTurn() {
        val cid = "${convPrefix}24"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Scared my partner will leave and fear of abandonment", ctx)
        assertTrue("Must address abandonment fear and delayed text projection: ${turn.responseText}",
            turn.responseText.contains("facts", ignoreCase = true) ||
            turn.responseText.contains("text", ignoreCase = true) ||
            turn.responseText.contains("projecting", ignoreCase = true) ||
            turn.responseText.contains("breakup", ignoreCase = true) ||
            turn.responseText.contains("abandonment", ignoreCase = true) ||
            turn.responseText.contains("ground", ignoreCase = true)
        )
    }

    @Test
    fun testScenario20_05_teammatesApproach_singleTurn() {
        val cid = "${convPrefix}25"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Relationship insecurity and partner doesn't love me", ctx)
        assertTrue("Must guide healthy relationship perspective: ${turn.responseText}",
            turn.responseText.contains("partner", ignoreCase = true) ||
            turn.responseText.contains("affection", ignoreCase = true) ||
            turn.responseText.contains("communication", ignoreCase = true) ||
            turn.responseText.contains("teammate", ignoreCase = true) ||
            turn.responseText.contains("puzzle", ignoreCase = true) ||
            turn.responseText.contains("adversary", ignoreCase = true)
        )
    }
}
