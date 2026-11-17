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
 * Solace+ Implementation Batch 1 Multi-Turn Test Suite
 * Covers Scenarios 1 to 5:
 * - Scenario 1: Friendship Conflicts & Drift
 * - Scenario 2: Family-Related Emotional Conversations
 * - Scenario 3: Failure and Disappointment
 * - Scenario 4: Future Uncertainty & Career Stress
 * - Scenario 5: Social Comparison & Inferiority
 */
class Batch1ScenariosTest {

    private val convPrefix = "batch1_test_"

    @Before
    fun setUp() {
        for (i in 1..25) {
            DeterministicEmotionalEngine.clearSession("${convPrefix}$i")
        }
    }

    // =========================================================================
    // SCENARIO 1: FRIENDSHIP CONFLICTS & DRIFT
    // =========================================================================

    @Test
    fun testScenario1_01_friendshipDrift_full3TurnFlow() {
        val cid = "${convPrefix}1_1"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I think my best friend is slowly distancing themselves from me.", ctx)
        assertTrue("Turn 1 should acknowledge distance: ${turn1.responseText}", turn1.responseText.contains("distance", ignoreCase = true) || turn1.responseText.contains("friend", ignoreCase = true))
        assertFalse("Must not ask permission", turn1.responseText.contains("would you like", ignoreCase = true))

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("They don't even text me first anymore.", ctx)
        assertTrue("Turn 2 should recognize one-sided effort: ${turn2.responseText}", turn2.responseText.contains("effort", ignoreCase = true) || turn2.responseText.contains("reaching out", ignoreCase = true) || turn2.responseText.contains("friendship", ignoreCase = true))

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I think I'm the only one trying.", ctx)
        assertTrue("Turn 3 should suggest healthy space: ${turn3.responseText}", turn3.responseText.contains("distance", ignoreCase = true) || turn3.responseText.contains("boundaries", ignoreCase = true) || turn3.responseText.contains("reciprocal", ignoreCase = true) || turn3.responseText.contains("pause", ignoreCase = true))
    }

    @Test
    fun testScenario1_02_thirdWheelExclusion() {
        val cid = "${convPrefix}1_2"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I feel like the third wheel whenever my friends make plans without me.", ctx)
        assertTrue("Should acknowledge third wheel/plans: ${turn.responseText}", turn.responseText.contains("third wheel", ignoreCase = true) || turn.responseText.contains("left out", ignoreCase = true))
    }

    @Test
    fun testScenario1_03_childhoodFriendsDrifting() {
        val cid = "${convPrefix}1_3"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I've completely lost touch with my childhood friends.", ctx)
        assertTrue("Should address childhood friends: ${turn.responseText}", turn.responseText.contains("childhood friends", ignoreCase = true) || turn.responseText.contains("drift", ignoreCase = true) || turn.responseText.contains("history", ignoreCase = true))
    }

    @Test
    fun testScenario1_04_friendReplacedUser() {
        val cid = "${convPrefix}1_4"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("My best friend replaced me with someone else.", ctx)
        assertTrue("Should address being replaced: ${turn.responseText}", turn.responseText.contains("replaced", ignoreCase = true) || turn.responseText.contains("friend", ignoreCase = true))
    }

    @Test
    fun testScenario1_05_friendshipConflictsNoBurningBridges() {
        val cid = "${convPrefix}1_5"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I had a fight with my best friend and feel so betrayed.", ctx)
        assertTrue("Must address friend conflict: ${turn.responseText}", turn.responseText.contains("friend", ignoreCase = true))
        assertFalse("Must never advise automatically ending friendship", turn.responseText.contains("cut them off", ignoreCase = true))
    }

    // =========================================================================
    // SCENARIO 2: FAMILY-RELATED CONVERSATIONS
    // =========================================================================

    @Test
    fun testScenario2_01_parentalCareerConflict_full3TurnFlow() {
        val cid = "${convPrefix}2_1"
        val ctx = ConversationContext(conversationId = cid)

        val turn1 = DeterministicEmotionalEngine.processTurn("My parents never understand me.", ctx)
        assertTrue("Turn 1 acknowledges parents: ${turn1.responseText}", turn1.responseText.contains("parents", ignoreCase = true) || turn1.responseText.contains("understand", ignoreCase = true))

        val turn2 = DeterministicEmotionalEngine.processTurn("They want me to do something I don't even like.", ctx)
        assertTrue("Turn 2 acknowledges career expectations: ${turn2.responseText}", turn2.responseText.contains("path", ignoreCase = true) || turn2.responseText.contains("career", ignoreCase = true) || turn2.responseText.contains("truth", ignoreCase = true))

        val turn3 = DeterministicEmotionalEngine.processTurn("I can't even explain my side.", ctx)
        assertTrue("Turn 3 suggests organizing thoughts/space: ${turn3.responseText}", turn3.responseText.contains("feelings", ignoreCase = true) || turn3.responseText.contains("private", ignoreCase = true) || turn3.responseText.contains("defense", ignoreCase = true) || turn3.responseText.contains("peace", ignoreCase = true))
    }

    @Test
    fun testScenario2_02_siblingComparison() {
        val cid = "${convPrefix}2_2"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("My parents are always comparing me to my brother.", ctx)
        assertTrue("Should address sibling comparison: ${turn.responseText}", turn.responseText.contains("compared", ignoreCase = true) || turn.responseText.contains("siblings", ignoreCase = true))
    }

    @Test
    fun testScenario2_03_familyDismissingFeelings() {
        val cid = "${convPrefix}2_3"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("My family dismisses my feelings whenever I try to speak.", ctx)
        assertTrue("Should address feelings dismissed: ${turn.responseText}", turn.responseText.contains("dismissed", ignoreCase = true) || turn.responseText.contains("family", ignoreCase = true))
    }

    @Test
    fun testScenario2_04_walkingOnEggshellsAtHome() {
        val cid = "${convPrefix}2_4"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I'm constantly walking on eggshells at home because of family drama.", ctx)
        assertTrue("Should address family tension: ${turn.responseText}", turn.responseText.contains("family", ignoreCase = true) || turn.responseText.contains("tension", ignoreCase = true) || turn.responseText.contains("peace", ignoreCase = true))
    }

    @Test
    fun testScenario2_05_feelingUnseenUnderOwnRoof() {
        val cid = "${convPrefix}2_5"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I feel so unseen and controlled by my parents.", ctx)
        assertTrue("Should address feeling unseen: ${turn.responseText}", turn.responseText.contains("unseen", ignoreCase = true) || turn.responseText.contains("parents", ignoreCase = true) || turn.responseText.contains("sanctuary", ignoreCase = true))
    }

    // =========================================================================
    // SCENARIO 3: FAILURE AND DISAPPOINTMENT
    // =========================================================================

    @Test
    fun testScenario3_01_workedHardAndFailed_full3TurnFlow() {
        val cid = "${convPrefix}3_1"
        val ctx = ConversationContext(conversationId = cid)

        val turn1 = DeterministicEmotionalEngine.processTurn("I worked so hard and still failed.", ctx)
        assertTrue("Turn 1 validates failure sting: ${turn1.responseText}", turn1.responseText.contains("failure", ignoreCase = true) || turn1.responseText.contains("outcome", ignoreCase = true) || turn1.responseText.contains("hard", ignoreCase = true))
        assertFalse("Must not say failure is a stepping stone", turn1.responseText.contains("stepping stone", ignoreCase = true))

        val turn2 = DeterministicEmotionalEngine.processTurn("Everyone else passed.", ctx)
        assertTrue("Turn 2 addresses comparison: ${turn2.responseText}", turn2.responseText.contains("passed", ignoreCase = true) || turn2.responseText.contains("effort", ignoreCase = true) || turn2.responseText.contains("success", ignoreCase = true))

        val turn3 = DeterministicEmotionalEngine.processTurn("Maybe I'm just not capable.", ctx)
        assertTrue("Turn 3 distinguishes outcome from identity: ${turn3.responseText}", turn3.responseText.contains("failure as a human", ignoreCase = true) || turn3.responseText.contains("decouple", ignoreCase = true) || turn3.responseText.contains("intelligence", ignoreCase = true))
    }

    @Test
    fun testScenario3_02_opportunityRejection() {
        val cid = "${convPrefix}3_2"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I was rejected from the program I worked months for.", ctx)
        assertTrue("Should address program/opportunity rejection: ${turn.responseText}", turn.responseText.contains("effort", ignoreCase = true) || turn.responseText.contains("outcome", ignoreCase = true) || turn.responseText.contains("grief", ignoreCase = true) || turn.responseText.contains("failure", ignoreCase = true))
    }

    @Test
    fun testScenario3_03_lettingEveryoneDown() {
        val cid = "${convPrefix}3_3"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I failed my finals and feel like I let everyone down.", ctx)
        assertTrue("Should address letting people down: ${turn.responseText}", turn.responseText.contains("let down", ignoreCase = true) || turn.responseText.contains("scores", ignoreCase = true) || turn.responseText.contains("believed in you", ignoreCase = true))
    }

    @Test
    fun testScenario3_04_hardWorkProducedNoResults() {
        val cid = "${convPrefix}3_4"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("My hard work produced no results and I feel like giving up.", ctx)
        assertTrue("Should address effort without immediate return: ${turn.responseText}", turn.responseText.contains("effort", ignoreCase = true) || turn.responseText.contains("worked so hard", ignoreCase = true) || turn.responseText.contains("success", ignoreCase = true) || turn.responseText.contains("fighting", ignoreCase = true) || turn.responseText.contains("energy", ignoreCase = true))
    }

    @Test
    fun testScenario3_05_makingMistakeGentlySupported() {
        val cid = "${convPrefix}3_5"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I messed up my exam and made terrible mistakes.", ctx)
        assertTrue("Should address mistakes as part of process: ${turn.responseText}", turn.responseText.contains("mistakes", ignoreCase = true) || turn.responseText.contains("process", ignoreCase = true) || turn.responseText.contains("practical options", ignoreCase = true))
    }

    // =========================================================================
    // SCENARIO 4: FUTURE UNCERTAINTY & CAREER STRESS
    // =========================================================================

    @Test
    fun testScenario4_01_futureUncertainty_full3TurnFlow() {
        val cid = "${convPrefix}4_1"
        val ctx = ConversationContext(conversationId = cid)

        val turn1 = DeterministicEmotionalEngine.processTurn("I have no idea what I'm doing with my life.", ctx)
        assertTrue("Turn 1 normalizes uncertainty: ${turn1.responseText}", turn1.responseText.contains("life", ignoreCase = true) && (turn1.responseText.contains("normal", ignoreCase = true) || turn1.responseText.contains("master plan", ignoreCase = true)))

        val turn2 = DeterministicEmotionalEngine.processTurn("Everyone around me has a plan.", ctx)
        assertTrue("Turn 2 addresses social comparison: ${turn2.responseText}", turn2.responseText.contains("roadmap", ignoreCase = true) || turn2.responseText.contains("behind closed doors", ignoreCase = true) || turn2.responseText.contains("exploratory", ignoreCase = true))

        val turn3 = DeterministicEmotionalEngine.processTurn("I'm scared I'll make the wrong decision.", ctx)
        assertTrue("Turn 3 addresses fear of irreversible choice: ${turn3.responseText}", turn3.responseText.contains("irreversible", ignoreCase = true) || turn3.responseText.contains("pivots", ignoreCase = true) || turn3.responseText.contains("steps", ignoreCase = true))
    }

    @Test
    fun testScenario4_02_wrongDegreeDoubt() {
        val cid = "${convPrefix}4_2"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I'm terrified I picked the wrong degree.", ctx)
        assertTrue("Should address degree questioning: ${turn.responseText}", turn.responseText.contains("degree", ignoreCase = true) || turn.responseText.contains("major", ignoreCase = true) || turn.responseText.contains("self-awareness", ignoreCase = true))
    }

    @Test
    fun testScenario4_03_feelingDirectionless() {
        val cid = "${convPrefix}4_3"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I feel so directionless and lost right now.", ctx)
        assertTrue("Should address feeling directionless: ${turn.responseText}", turn.responseText.contains("directionless", ignoreCase = true) || turn.responseText.contains("evolution", ignoreCase = true) || turn.responseText.contains("meaningful", ignoreCase = true))
    }

    @Test
    fun testScenario4_04_pressureToSucceed() {
        val cid = "${convPrefix}4_4"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("The career pressure to be successful is suffocating me.", ctx)
        assertTrue("Should address societal career pressure: ${turn.responseText}", turn.responseText.contains("pressure", ignoreCase = true) || turn.responseText.contains("career", ignoreCase = true))
    }

    @Test
    fun testScenario4_05_exploringMicroExperiment() {
        val cid = "${convPrefix}4_5"
        val ctx = ConversationContext(conversationId = cid)

        val turn1 = DeterministicEmotionalEngine.processTurn("I have major career uncertainty.", ctx)
        val turn2 = DeterministicEmotionalEngine.processTurn("I don't know what my passion is.", ctx)
        assertTrue("Should suggest exploring without huge pressure: ${turn2.responseText}", turn2.responseText.contains("passion", ignoreCase = true) || turn2.responseText.contains("explore", ignoreCase = true) || turn2.responseText.contains("curious", ignoreCase = true))
    }

    // =========================================================================
    // SCENARIO 5: SOCIAL COMPARISON & INFERIORITY
    // =========================================================================

    @Test
    fun testScenario5_01_socialComparison_full3TurnFlow() {
        val cid = "${convPrefix}5_1"
        val ctx = ConversationContext(conversationId = cid)

        val turn1 = DeterministicEmotionalEngine.processTurn("Everyone my age is doing something meaningful except me.", ctx)
        assertTrue("Turn 1 validates feeling left behind: ${turn1.responseText}", turn1.responseText.contains("left behind", ignoreCase = true) || turn1.responseText.contains("comparison", ignoreCase = true))

        val turn2 = DeterministicEmotionalEngine.processTurn("I see people travelling, earning and enjoying life.", ctx)
        assertTrue("Turn 2 addresses social media highlight reel: ${turn2.responseText}", turn2.responseText.contains("social media", ignoreCase = true) || turn2.responseText.contains("highlight reel", ignoreCase = true) || turn2.responseText.contains("curated", ignoreCase = true))

        val turn3 = DeterministicEmotionalEngine.processTurn("I feel like I'm wasting my life.", ctx)
        assertTrue("Turn 3 grounds user pace and worth: ${turn3.responseText}", turn3.responseText.contains("wasting your life", ignoreCase = true) || turn3.responseText.contains("pace", ignoreCase = true) || turn3.responseText.contains("perseverance", ignoreCase = true) || turn3.responseText.contains("feed", ignoreCase = true))
    }

    @Test
    fun testScenario5_02_socialMediaEnvy() {
        val cid = "${convPrefix}5_2"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("Social media makes me feel like a complete failure.", ctx)
        assertTrue("Should address social media comparison: ${turn.responseText}", turn.responseText.contains("social media", ignoreCase = true) || turn.responseText.contains("highlight reel", ignoreCase = true) || turn.responseText.contains("feed", ignoreCase = true))
    }

    @Test
    fun testScenario5_03_peerEarningsComparison() {
        val cid = "${convPrefix}5_3"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("I see peers earning and buying things and feel so inferior.", ctx)
        assertTrue("Should address financial and achievement envy gently: ${turn.responseText}", turn.responseText.contains("earning", ignoreCase = true) || turn.responseText.contains("peers", ignoreCase = true) || turn.responseText.contains("milestones", ignoreCase = true) || turn.responseText.contains("comparison", ignoreCase = true) || turn.responseText.contains("left behind", ignoreCase = true))
    }

    @Test
    fun testScenario5_04_academicComparisonWithClassmates() {
        val cid = "${convPrefix}5_4"
        val ctx = ConversationContext(conversationId = cid)

        val turn = DeterministicEmotionalEngine.processTurn("Everyone is smarter than me and I'm falling behind everyone else.", ctx)
        assertTrue("Should address academic comparison: ${turn.responseText}", turn.responseText.contains("figured out", ignoreCase = true) || turn.responseText.contains("control", ignoreCase = true) || turn.responseText.contains("peace", ignoreCase = true) || turn.responseText.contains("comparison", ignoreCase = true))
    }

    @Test
    fun testScenario5_05_phoneDownGrounding() {
        val cid = "${convPrefix}5_5"
        val ctx = ConversationContext(conversationId = cid)

        val turn1 = DeterministicEmotionalEngine.processTurn("I have major social media envy today.", ctx)
        val turn2 = DeterministicEmotionalEngine.processTurn("I can't stop comparing myself to influencers.", ctx)
        assertTrue("Should offer grounding and stepping away: ${turn2.responseText}", turn2.responseText.contains("apps", ignoreCase = true) || turn2.responseText.contains("feed", ignoreCase = true) || turn2.responseText.contains("phone", ignoreCase = true) || turn2.responseText.contains("race", ignoreCase = true))
    }
}
