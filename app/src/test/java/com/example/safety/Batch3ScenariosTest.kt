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
 * Solace+ Implementation Batch 3 Multi-Turn Test Suite
 * Covers Scenarios 11 to 15:
 * - Scenario 11: Emotional Exhaustion & Burnout
 * - Scenario 12: Financial Stress & Dependence
 * - Scenario 13: Life Transitions & Unfamiliar Environments
 * - Scenario 14: Interpersonal Conflict & Boundary Setting
 * - Scenario 15: Rejection & Unreciprocated Feelings
 */
class Batch3ScenariosTest {

    private val convPrefix = "batch3_test_"

    @Before
    fun setUp() {
        for (i in 1..25) {
            DeterministicEmotionalEngine.clearSession("${convPrefix}$i")
        }
    }

    // =========================================================================
    // SCENARIO 11: EMOTIONAL EXHAUSTION & BURNOUT
    // =========================================================================

    @Test
    fun testScenario11_01_mentalDrain_multiTurnFlow() {
        val cid = "${convPrefix}1"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I'm tired of everything.", ctx)
        assertTrue("Turn 1 should acknowledge being tired: ${turn1.responseText}",
            turn1.responseText.contains("tired", ignoreCase = true) ||
            turn1.responseText.contains("weight", ignoreCase = true) ||
            turn1.responseText.contains("battery", ignoreCase = true) ||
            turn1.responseText.contains("energy", ignoreCase = true) ||
            turn1.responseText.contains("exhausted", ignoreCase = true)
        )
        assertFalse("Must not ask permission", turn1.responseText.contains("would you like", ignoreCase = true))

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("Even things I enjoy feel like work and chores now.", ctx)
        assertTrue("Turn 2 should address anhedonia: ${turn2.responseText}",
            turn2.responseText.contains("joy", ignoreCase = true) ||
            turn2.responseText.contains("chores", ignoreCase = true) ||
            turn2.responseText.contains("depleted", ignoreCase = true) ||
            turn2.responseText.contains("hobbies", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I just want to be left alone by everyone.", ctx)
        assertTrue("Turn 3 should honor withdrawal urge: ${turn3.responseText}",
            turn3.responseText.contains("withdraw", ignoreCase = true) ||
            turn3.responseText.contains("alone", ignoreCase = true) ||
            turn3.responseText.contains("quiet", ignoreCase = true) ||
            turn3.responseText.contains("space", ignoreCase = true)
        )
    }

    @Test
    fun testScenario11_02_runningOnEmpty_singleTurn() {
        val cid = "${convPrefix}2"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I am mentally drained and running on empty", ctx)
        assertTrue("Must acknowledge running on empty: ${turn.responseText}",
            turn.responseText.contains("energy", ignoreCase = true) ||
            turn.responseText.contains("weight", ignoreCase = true) ||
            turn.responseText.contains("battery", ignoreCase = true) ||
            turn.responseText.contains("fatigue", ignoreCase = true)
        )
    }

    @Test
    fun testScenario11_03_chronicFatigue_singleTurn() {
        val cid = "${convPrefix}3"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I am tired despite sleeping", ctx)
        assertTrue("Must acknowledge chronic fatigue: ${turn.responseText}",
            turn.responseText.contains("burnout", ignoreCase = true) ||
            turn.responseText.contains("sleep", ignoreCase = true) ||
            turn.responseText.contains("tired", ignoreCase = true)
        )
    }

    @Test
    fun testScenario11_04_anhedoniaLossOfInterest_singleTurn() {
        val cid = "${convPrefix}4"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Nothing brings me joy and hobbies feel like chores", ctx)
        assertTrue("Must address loss of interest: ${turn.responseText}",
            turn.responseText.contains("joy", ignoreCase = true) ||
            turn.responseText.contains("chores", ignoreCase = true) ||
            turn.responseText.contains("hobbies", ignoreCase = true)
        )
    }

    @Test
    fun testScenario11_05_responsibilityOverwhelm_singleTurn() {
        val cid = "${convPrefix}5"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Overwhelmed by responsibilities and burnout", ctx)
        assertTrue("Must address responsibility burnout: ${turn.responseText}",
            turn.responseText.contains("burnout", ignoreCase = true) ||
            turn.responseText.contains("carrying", ignoreCase = true) ||
            turn.responseText.contains("responsibilities", ignoreCase = true) ||
            turn.responseText.contains("weight", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 12: FINANCIAL STRESS & DEPENDENCE
    // =========================================================================

    @Test
    fun testScenario12_01_parentalDependence_multiTurnFlow() {
        val cid = "${convPrefix}6"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I feel bad asking my parents for money.", ctx)
        assertTrue("Turn 1 should address parental dependence: ${turn1.responseText}",
            turn1.responseText.contains("parents", ignoreCase = true) ||
            turn1.responseText.contains("financial", ignoreCase = true) ||
            turn1.responseText.contains("money", ignoreCase = true) ||
            turn1.responseText.contains("dependence", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("Everyone around me earns their own money and contributes.", ctx)
        assertTrue("Turn 2 should address peer wealth comparison: ${turn2.responseText}",
            turn2.responseText.contains("bank account", ignoreCase = true) ||
            turn2.responseText.contains("earning", ignoreCase = true) ||
            turn2.responseText.contains("inadequacy", ignoreCase = true) ||
            turn2.responseText.contains("peers", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("I feel like I'm not contributing anything.", ctx)
        assertTrue("Turn 3 should decouple self-worth from income: ${turn3.responseText}",
            turn3.responseText.contains("worth", ignoreCase = true) ||
            turn3.responseText.contains("human", ignoreCase = true) ||
            turn3.responseText.contains("contribute", ignoreCase = true) ||
            turn3.responseText.contains("money", ignoreCase = true)
        )
    }

    @Test
    fun testScenario12_02_expenseStrain_singleTurn() {
        val cid = "${convPrefix}7"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I can't afford anything and have debt worries", ctx)
        assertTrue("Must address expense strain: ${turn.responseText}",
            turn.responseText.contains("financial", ignoreCase = true) ||
            turn.responseText.contains("budget", ignoreCase = true) ||
            turn.responseText.contains("strain", ignoreCase = true) ||
            turn.responseText.contains("debt", ignoreCase = true)
        )
    }

    @Test
    fun testScenario12_03_financialBurden_singleTurn() {
        val cid = "${convPrefix}8"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I am a financial burden on my parents", ctx)
        assertTrue("Must address financial burden: ${turn.responseText}",
            turn.responseText.contains("burden", ignoreCase = true) ||
            turn.responseText.contains("family", ignoreCase = true) ||
            turn.responseText.contains("parents", ignoreCase = true) ||
            turn.responseText.contains("financial", ignoreCase = true) ||
            turn.responseText.contains("strain", ignoreCase = true)
        )
    }

    @Test
    fun testScenario12_04_peerWealthComparison_singleTurn() {
        val cid = "${convPrefix}9"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("My classmates earning while I have no income", ctx)
        assertTrue("Must address peer wealth comparison: ${turn.responseText}",
            turn.responseText.contains("peers", ignoreCase = true) ||
            turn.responseText.contains("earning", ignoreCase = true) ||
            turn.responseText.contains("compare", ignoreCase = true) ||
            turn.responseText.contains("money", ignoreCase = true) ||
            turn.responseText.contains("worth", ignoreCase = true) ||
            turn.responseText.contains("contribute", ignoreCase = true) ||
            turn.responseText.contains("bank account", ignoreCase = true)
        )
    }

    @Test
    fun testScenario12_05_spendingGuilt_singleTurn() {
        val cid = "${convPrefix}10"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Every expense feels like a threat and I feel guilty about spending", ctx)
        assertTrue("Must address spending threat: ${turn.responseText}",
            turn.responseText.contains("expense", ignoreCase = true) ||
            turn.responseText.contains("threat", ignoreCase = true) ||
            turn.responseText.contains("strain", ignoreCase = true) ||
            turn.responseText.contains("financial", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 13: LIFE TRANSITIONS & UNFAMILIAR ENVIRONMENTS
    // =========================================================================

    @Test
    fun testScenario13_01_collegeTransition_multiTurnFlow() {
        val cid = "${convPrefix}11"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I recently started college and I don't feel like I belong here.", ctx)
        assertTrue("Turn 1 should address college belonging: ${turn1.responseText}",
            turn1.responseText.contains("college", ignoreCase = true) ||
            turn1.responseText.contains("belong", ignoreCase = true) ||
            turn1.responseText.contains("environment", ignoreCase = true) ||
            turn1.responseText.contains("transition", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("I miss my school friends and my old life.", ctx)
        assertTrue("Turn 2 should address missing school friends: ${turn2.responseText}",
            turn2.responseText.contains("school friends", ignoreCase = true) ||
            turn2.responseText.contains("missing", ignoreCase = true) ||
            turn2.responseText.contains("familiarity", ignoreCase = true) ||
            turn2.responseText.contains("memories", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("Everything feels unfamiliar and I lost my routine.", ctx)
        assertTrue("Turn 3 should normalize routine loss: ${turn3.responseText}",
            turn3.responseText.contains("unfamiliar", ignoreCase = true) ||
            turn3.responseText.contains("routine", ignoreCase = true) ||
            turn3.responseText.contains("anchor", ignoreCase = true) ||
            turn3.responseText.contains("brain", ignoreCase = true)
        )
    }

    @Test
    fun testScenario13_02_newCityRelocation_singleTurn() {
        val cid = "${convPrefix}12"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I moved to a new city and feel homesick", ctx)
        assertTrue("Must address relocating to new city: ${turn.responseText}",
            turn.responseText.contains("city", ignoreCase = true) ||
            turn.responseText.contains("homesick", ignoreCase = true) ||
            turn.responseText.contains("transition", ignoreCase = true) ||
            turn.responseText.contains("adapt", ignoreCase = true) ||
            turn.responseText.contains("environment", ignoreCase = true)
        )
    }

    @Test
    fun testScenario13_03_unfamiliarCampus_singleTurn() {
        val cid = "${convPrefix}13"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Everything feels unfamiliar in this new campus", ctx)
        assertTrue("Must address unfamiliar campus: ${turn.responseText}",
            turn.responseText.contains("campus", ignoreCase = true) ||
            turn.responseText.contains("environment", ignoreCase = true) ||
            turn.responseText.contains("unfamiliar", ignoreCase = true) ||
            turn.responseText.contains("belong", ignoreCase = true)
        )
    }

    @Test
    fun testScenario13_04_homesicknessOldRoutine_singleTurn() {
        val cid = "${convPrefix}14"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I have severe homesickness and miss my old routine", ctx)
        assertTrue("Must address homesickness: ${turn.responseText}",
            turn.responseText.contains("home", ignoreCase = true) ||
            turn.responseText.contains("routine", ignoreCase = true) ||
            turn.responseText.contains("miss", ignoreCase = true) ||
            turn.responseText.contains("familiar", ignoreCase = true)
        )
    }

    @Test
    fun testScenario13_05_imposterInCollege_singleTurn() {
        val cid = "${convPrefix}15"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Feeling like an imposter in college and new campus", ctx)
        assertTrue("Must address college imposter feeling: ${turn.responseText}",
            turn.responseText.contains("belong", ignoreCase = true) ||
            turn.responseText.contains("college", ignoreCase = true) ||
            turn.responseText.contains("environment", ignoreCase = true) ||
            turn.responseText.contains("campus", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 14: INTERPERSONAL CONFLICT & BOUNDARIES
    // =========================================================================

    @Test
    fun testScenario14_01_argumentAndWords_multiTurnFlow() {
        val cid = "${convPrefix}16"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I had a huge argument with my friend.", ctx)
        assertTrue("Turn 1 should address argument adrenaline: ${turn1.responseText}",
            turn1.responseText.contains("argument", ignoreCase = true) ||
            turn1.responseText.contains("fight", ignoreCase = true) ||
            turn1.responseText.contains("nervous system", ignoreCase = true) ||
            turn1.responseText.contains("settle", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("I said things I shouldn't have out of anger.", ctx)
        assertTrue("Turn 2 should address regretted words: ${turn2.responseText}",
            turn2.responseText.contains("anger", ignoreCase = true) ||
            turn2.responseText.contains("regret", ignoreCase = true) ||
            turn2.responseText.contains("accountability", ignoreCase = true) ||
            turn2.responseText.contains("words", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("They were wrong too and disrespected me.", ctx)
        assertTrue("Turn 3 should validate disrespect and boundaries: ${turn3.responseText}",
            turn3.responseText.contains("disrespect", ignoreCase = true) ||
            turn3.responseText.contains("boundary", ignoreCase = true) ||
            turn3.responseText.contains("wrong", ignoreCase = true) ||
            turn3.responseText.contains("conflict", ignoreCase = true)
        )
    }

    @Test
    fun testScenario14_02_screamingMatch_singleTurn() {
        val cid = "${convPrefix}17"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("We had a screaming match and said things we regret", ctx)
        assertTrue("Must acknowledge screaming match: ${turn.responseText}",
            turn.responseText.contains("argument", ignoreCase = true) ||
            turn.responseText.contains("anger", ignoreCase = true) ||
            turn.responseText.contains("words", ignoreCase = true) ||
            turn.responseText.contains("settle", ignoreCase = true)
        )
    }

    @Test
    fun testScenario14_03_boundaryCrossed_singleTurn() {
        val cid = "${convPrefix}18"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("A boundary was crossed in our relationship", ctx)
        assertTrue("Must address boundary crossing: ${turn.responseText}",
            turn.responseText.contains("boundary", ignoreCase = true) ||
            turn.responseText.contains("respect", ignoreCase = true) ||
            turn.responseText.contains("safe", ignoreCase = true)
        )
    }

    @Test
    fun testScenario14_04_spokeOutOfAnger_singleTurn() {
        val cid = "${convPrefix}19"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I spoke out of anger and damaged the relationship", ctx)
        assertTrue("Must address speaking out of anger: ${turn.responseText}",
            turn.responseText.contains("anger", ignoreCase = true) ||
            turn.responseText.contains("words", ignoreCase = true) ||
            turn.responseText.contains("repair", ignoreCase = true) ||
            turn.responseText.contains("regret", ignoreCase = true)
        )
    }

    @Test
    fun testScenario14_05_disrespectedInFight_singleTurn() {
        val cid = "${convPrefix}20"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Both sides made mistakes and I felt disrespected in argument", ctx)
        assertTrue("Must address mutual mistakes and disrespect: ${turn.responseText}",
            turn.responseText.contains("disrespect", ignoreCase = true) ||
            turn.responseText.contains("mistakes", ignoreCase = true) ||
            turn.responseText.contains("boundary", ignoreCase = true) ||
            turn.responseText.contains("wrong", ignoreCase = true)
        )
    }

    // =========================================================================
    // SCENARIO 15: REJECTION & UNRECIPROCATED FEELINGS
    // =========================================================================

    @Test
    fun testScenario15_01_romanticRejection_multiTurnFlow() {
        val cid = "${convPrefix}21"
        val ctx = ConversationContext(conversationId = cid)

        // Turn 1
        val turn1 = DeterministicEmotionalEngine.processTurn("I told someone I liked them and they rejected me.", ctx)
        assertTrue("Turn 1 should acknowledge rejection courage: ${turn1.responseText}",
            turn1.responseText.contains("heart", ignoreCase = true) ||
            turn1.responseText.contains("courage", ignoreCase = true) ||
            turn1.responseText.contains("rejection", ignoreCase = true) ||
            turn1.responseText.contains("rejected", ignoreCase = true)
        )

        // Turn 2
        val turn2 = DeterministicEmotionalEngine.processTurn("I feel so embarrassed right now.", ctx)
        assertTrue("Turn 2 should normalize post-rejection embarrassment: ${turn2.responseText}",
            turn2.responseText.contains("embarrass", ignoreCase = true) ||
            turn2.responseText.contains("ashamed", ignoreCase = true) ||
            turn2.responseText.contains("courage", ignoreCase = true) ||
            turn2.responseText.contains("brave", ignoreCase = true)
        )

        // Turn 3
        val turn3 = DeterministicEmotionalEngine.processTurn("Maybe I'm just not good enough for anyone.", ctx)
        assertTrue("Turn 3 should decouple rejection from lovability: ${turn3.responseText}",
            turn3.responseText.contains("worth", ignoreCase = true) ||
            turn3.responseText.contains("compatibility", ignoreCase = true) ||
            turn3.responseText.contains("lovability", ignoreCase = true) ||
            turn3.responseText.contains("grade", ignoreCase = true)
        )
    }

    @Test
    fun testScenario15_02_confessionRejection_singleTurn() {
        val cid = "${convPrefix}22"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I confessed my feelings and they said no", ctx)
        assertTrue("Must acknowledge confession rejection: ${turn.responseText}",
            turn.responseText.contains("rejection", ignoreCase = true) ||
            turn.responseText.contains("rejected", ignoreCase = true) ||
            turn.responseText.contains("courage", ignoreCase = true) ||
            turn.responseText.contains("heart", ignoreCase = true)
        )
    }

    @Test
    fun testScenario15_03_unreciprocatedLove_singleTurn() {
        val cid = "${convPrefix}23"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("I got friendzoned and feel unlovable", ctx)
        assertTrue("Must address feeling unlovable: ${turn.responseText}",
            turn.responseText.contains("worth", ignoreCase = true) ||
            turn.responseText.contains("compatibility", ignoreCase = true) ||
            turn.responseText.contains("lovability", ignoreCase = true) ||
            turn.responseText.contains("rejected", ignoreCase = true) ||
            turn.responseText.contains("value", ignoreCase = true)
        )
    }

    @Test
    fun testScenario15_04_embarrassmentOfRejection_singleTurn() {
        val cid = "${convPrefix}24"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("Shared my feelings and got turned down feeling so embarrassed", ctx)
        assertTrue("Must address embarrassment after being turned down: ${turn.responseText}",
            turn.responseText.contains("embarrass", ignoreCase = true) ||
            turn.responseText.contains("vulnerab", ignoreCase = true) ||
            turn.responseText.contains("courage", ignoreCase = true) ||
            turn.responseText.contains("rejection", ignoreCase = true) ||
            turn.responseText.contains("rejected", ignoreCase = true)
        )
    }

    @Test
    fun testScenario15_05_nobodyWantsMe_singleTurn() {
        val cid = "${convPrefix}25"
        val ctx = ConversationContext(conversationId = cid)
        val turn = DeterministicEmotionalEngine.processTurn("They said no to me and nobody wants me", ctx)
        assertTrue("Must address feeling nobody wants me: ${turn.responseText}",
            turn.responseText.contains("worth", ignoreCase = true) ||
            turn.responseText.contains("partner", ignoreCase = true) ||
            turn.responseText.contains("value", ignoreCase = true) ||
            turn.responseText.contains("compatibility", ignoreCase = true) ||
            turn.responseText.contains("enough", ignoreCase = true)
        )
    }
}
