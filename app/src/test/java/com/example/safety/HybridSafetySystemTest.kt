package com.example.safety

import com.example.data.ai.ConversationOrchestrator
import com.example.data.ai.GeminiContextAnalyzer
import com.example.data.ai.ResponseRouter
import com.example.data.ai.ResponseType
import com.example.data.model.RegulationMode
import com.example.data.safety.ContextAnalyzer
import com.example.data.safety.ConversationContext
import com.example.data.safety.ConversationState
import com.example.data.safety.EmergencyResources
import com.example.data.safety.EscalationManager
import com.example.data.safety.GeminiContextAnalysis
import com.example.data.safety.KeywordDetector
import com.example.data.safety.ResponseDeduplicationManager
import com.example.data.safety.RiskDecisionEngine
import com.example.data.safety.RiskLevel
import com.example.data.safety.RiskSignals
import com.example.data.safety.RiskState
import com.example.data.safety.RuleBasedConversationEngine
import com.example.data.safety.SafetyResponseHandler
import com.example.data.safety.SafetyQuestionId
import com.example.data.safety.SafetyAnswer
import com.example.data.safety.SafetyConversationManager
import com.example.data.safety.SafetyStateManager.Companion.toEntity
import com.example.data.safety.SafetyStateManager.Companion.toRiskState
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HybridSafetySystemTest {

    // =========================================================================
    // 1. MANDATED TABLE TESTS (Phase 10 Table Requirements)
    // =========================================================================

    @Test
    fun testMandatedTable_01_stressfulDay_isLevel1() {
        val result = EscalationManager.evaluate("I had a stressful day")
        assertEquals(RiskLevel.LEVEL_1_DISTRESS, result.level)
    }

    @Test
    fun testMandatedTable_02_lonely_isLevel1() {
        val result = EscalationManager.evaluate("I feel lonely")
        assertEquals(RiskLevel.LEVEL_1_DISTRESS, result.level)
    }

    @Test
    fun testMandatedTable_03_nothingGettingBetter_isLevel2() {
        val result = EscalationManager.evaluate("Nothing is getting better")
        assertEquals(RiskLevel.LEVEL_2_ELEVATED, result.level)
    }

    @Test
    fun testMandatedTable_04_everyoneBetterWithoutMe_isLevel2WithScreening() {
        val result = EscalationManager.evaluate("Everyone would be better without me")
        assertEquals(RiskLevel.LEVEL_2_ELEVATED, result.level)
        assertTrue(result.showSupportCard)
        assertNotNull(result.overrideResponse)
        assertTrue(
            "Must ask compassionate safety screening question",
            result.overrideResponse!!.contains("thoughts about hurting yourself or ending your life")
        )
    }

    @Test
    fun testMandatedTable_05_wishINeverWokeUp_isLevel3Assessment() {
        val result = EscalationManager.evaluate("I wish I never woke up")
        assertEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, result.level)
        assertTrue(result.showSupportCard)
        assertTrue(
            "Must ask if user is thinking of ending life at this moment",
            result.overrideResponse!!.contains("Are you having thoughts of ending your life at this moment?")
        )
    }

    @Test
    fun testMandatedTable_06_sometimesThinkAboutSuicide_isLevel3() {
        val result = EscalationManager.evaluate("Sometimes I think about suicide")
        assertEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, result.level)
    }

    @Test
    fun testMandatedTable_07_willEndMyLifeTonight_isLevel4() {
        val result = EscalationManager.evaluate("I will end my life tonight")
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, result.level)
        assertTrue(result.isEmergency)
        assertTrue(result.showEmergencyUI)
        assertTrue(result.overrideResponse!!.contains("112"))
    }

    @Test
    fun testMandatedTable_08_alreadyAttemptedSuicide_isLevel4Assessment() {
        val result = EscalationManager.evaluate("I have already attempted suicide")
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, result.level)
        assertTrue(result.isEmergency)
        assertTrue(result.showEmergencyUI)
    }

    @Test
    fun testMandatedTable_09_dyingOfLaughter_isLevel0() {
        val result = EscalationManager.evaluate("I'm dying of laughter")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result.level)
        assertFalse(result.isEmergency)
    }

    @Test
    fun testMandatedTable_10_fictionalCharacterWantsToDie_isContextualAssessment() {
        val result = EscalationManager.evaluate("My fictional character wants to die in the next scene")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result.level)
        assertFalse(result.isEmergency)
    }

    @Test
    fun testMandatedTable_11_dontWantToHurtMyself_isContextualAssessment() {
        val result = EscalationManager.evaluate("I don't want to hurt myself")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result.level)
        assertFalse(result.isEmergency)
    }

    @Test
    fun testMandatedTable_12_wantToHurtMyselfButNotDie_isSeparateSelfHarmAssessment() {
        val result = EscalationManager.evaluate("I want to hurt myself but not die")
        assertTrue(
            "Self-harm without suicidal intent must trigger an elevated safety tier",
            result.level.isAtLeast(RiskLevel.LEVEL_2_ELEVATED)
        )
    }

    // =========================================================================
    // 2. RULE-BASED CONVERSATIONAL ENGINE TESTS (Phase 1.A)
    // =========================================================================

    @Test
    fun testRuleBasedEngine_matchesAllEightCommonSituations() {
        assertEquals(RuleBasedConversationEngine.RuleCategory.BAD_DAY, RuleBasedConversationEngine.matches("I had a bad day at work"))
        assertEquals(RuleBasedConversationEngine.RuleCategory.LONELY, RuleBasedConversationEngine.matches("I feel lonely tonight"))
        assertEquals(RuleBasedConversationEngine.RuleCategory.OVERWHELMED, RuleBasedConversationEngine.matches("I am overwhelmed by everything"))
        assertEquals(RuleBasedConversationEngine.RuleCategory.CANNOT_CONCENTRATE, RuleBasedConversationEngine.matches("I cannot concentrate on my tasks"))
        assertEquals(RuleBasedConversationEngine.RuleCategory.EMOTIONALLY_EXHAUSTED, RuleBasedConversationEngine.matches("I feel emotionally exhausted"))
        assertEquals(RuleBasedConversationEngine.RuleCategory.ANXIOUS, RuleBasedConversationEngine.matches("I feel anxious right now"))
        assertEquals(RuleBasedConversationEngine.RuleCategory.DONT_KNOW_FEELING, RuleBasedConversationEngine.matches("I don't know what I feel"))
        assertEquals(RuleBasedConversationEngine.RuleCategory.JUST_WANT_LISTENING, RuleBasedConversationEngine.matches("I just want someone to listen to me"))
    }

    @Test
    fun testRuleBasedEngine_providesResponseVariation() {
        val responses = (0..5).map { turn ->
            RuleBasedConversationEngine.generateRuleResponse(
                category = RuleBasedConversationEngine.RuleCategory.BAD_DAY,
                userName = "Prapti",
                history = List(turn) { "user" to "msg" }
            )
        }
        val uniqueResponses = responses.toSet()
        assertTrue("Rule-based engine must provide multiple varied response templates", uniqueResponses.size > 1)
        responses.forEach { reply ->
            assertTrue("Responses should warmly include the user's name", reply.contains("Prapti"))
        }
    }

    @Test
    fun testRuleBasedEngine_anxiousOffersGroundingBreathwork() {
        val reply = RuleBasedConversationEngine.generateRuleResponse(
            category = RuleBasedConversationEngine.RuleCategory.ANXIOUS,
            userName = "Aarya"
        )
        assertTrue(
            "Anxiety response should offer grounding or breathing guidance",
            reply.contains("breath") || reply.contains("shoulders") || reply.contains("4")
        )
    }

    @Test
    fun testRuleBasedEngine_justListenRefrainsFromPrescribingSolutions() {
        val reply = RuleBasedConversationEngine.generateRuleResponse(
            category = RuleBasedConversationEngine.RuleCategory.JUST_WANT_LISTENING,
            userName = "User"
        )
        assertTrue(
            "Just listen response should affirm zero advice / holding space",
            reply.contains("Zero advice") || reply.contains("No problem-solving") || reply.contains("holding space")
        )
    }

    // =========================================================================
    // 3. GEMINI NLU CONTEXTUAL UNDERSTANDING TESTS (Phase 1.B)
    // =========================================================================

    @Test
    fun testGeminiContextAnalyzer_deterministicFallbackSchemaValidation() {
        val analyzer = GeminiContextAnalyzer()
        val analysis = analyzer.fallbackDeterministicAnalysis(
            userMessage = "I feel lonely and isolated",
            history = emptyList()
        )
        assertEquals("emotional_support", analysis.intent)
        assertEquals("loneliness", analysis.emotion)
        assertEquals("LISTEN", analysis.responseMode)
        assertTrue(analysis.isLiteral)
        assertFalse(analysis.isFigurative)
    }

    @Test
    fun testGeminiContextAnalyzer_figurativeDetectionInFallback() {
        val analyzer = GeminiContextAnalyzer()
        val analysis = analyzer.fallbackDeterministicAnalysis(
            userMessage = "I am dying of laughter from that joke",
            history = emptyList()
        )
        assertTrue(analysis.isFigurative)
        assertFalse(analysis.isLiteral)
    }

    // =========================================================================
    // 4. DETERMINISTIC IF-ELSE RISK DECISION ENGINE TESTS (Phase 3)
    // =========================================================================

    @Test
    fun testRiskDecisionEngine_priorityOrderEvaluation() {
        // High risk + low risk signals combined: Highest MUST win
        val signals = RiskSignals(
            emotionalDistress = true,
            stress = true,
            persistentHopelessness = true,
            activeSuicidalThoughts = true,
            currentSuicidalIntent = true
        )
        val level = RiskDecisionEngine.determineRisk(signals)
        assertEquals("Level 4 must take absolute precedence over all lower levels", RiskLevel.LEVEL_4_CRITICAL, level)
    }

    @Test
    fun testRiskDecisionEngine_level3OverridesLevel2AndLevel1() {
        val signals = RiskSignals(
            emotionalDistress = true,
            loneliness = true,
            persistentHopelessness = true,
            passiveDeathWish = true
        )
        val level = RiskDecisionEngine.determineRisk(signals)
        assertEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, level)
    }

    @Test
    fun testRiskDecisionEngine_level2OverridesLevel1() {
        val signals = RiskSignals(
            emotionalDistress = true,
            sadness = true,
            perceivedBurdensomeness = true
        )
        val level = RiskDecisionEngine.determineRisk(signals)
        assertEquals(RiskLevel.LEVEL_2_ELEVATED, level)
    }

    // =========================================================================
    // 5. CONTEXTUAL LANGUAGE UNDERSTANDING TESTS (Phase 4)
    // =========================================================================

    @Test
    fun testContextualLanguage_figurativeExpressionsDoNotTriggerEmergency() {
        val idioms = listOf(
            "This homework is killing me",
            "I'm dying to try that coffee place",
            "That gym workout completely killed me",
            "I'm starved to death",
            "I'm bored to death"
        )
        idioms.forEach { idiom ->
            val result = EscalationManager.evaluate(idiom)
            assertEquals("Idiom '$idiom' should be LEVEL_0_NORMAL", RiskLevel.LEVEL_0_NORMAL, result.level)
            assertFalse(result.isEmergency)
        }
    }

    @Test
    fun testContextualLanguage_fictionalAndThirdPartyContexts() {
        val fictional1 = EscalationManager.evaluate("In the movie the character committed suicide")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, fictional1.level)

        val fictional2 = EscalationManager.evaluate("I'm writing a script about someone dying")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, fictional2.level)
    }

    @Test
    fun testContextualLanguage_negationInterpretation() {
        val neg1 = EscalationManager.evaluate("I am not suicidal at all")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, neg1.level)

        val neg2 = EscalationManager.evaluate("I would never kill myself")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, neg2.level)

        val neg3 = EscalationManager.evaluate("I have no intention of hurting myself")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, neg3.level)
    }

    @Test
    fun testContextualLanguage_ambiguousStatementsTriggerClarification() {
        val result = EscalationManager.evaluate("I wish I could disappear")
        assertEquals(RiskLevel.LEVEL_2_ELEVATED, result.level)
        assertTrue(result.signals.needsClarification)
        assertTrue(
            "Ambiguous statement must ask for gentle clarification",
            result.overrideResponse!!.contains("are you thinking about hurting yourself or ending your life")
        )
    }

    @Test
    fun testContextualLanguage_multiTurnScreeningFlow() {
        val historyWithScreening = listOf(
            "user" to "I feel like a burden to everyone",
            "assistant" to "Can I ask something important? Have you recently had thoughts about hurting yourself or ending your life?"
        )

        // Affirmative answer
        val yesResult = EscalationManager.evaluate(
            userMessage = "Yes, I have",
            history = historyWithScreening
        )
        assertEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, yesResult.level)

        // Negative answer
        val noResult = EscalationManager.evaluate(
            userMessage = "No, never that",
            history = historyWithScreening
        )
        assertNotEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, noResult.level)
        assertFalse(noResult.isEmergency)
    }

    // =========================================================================
    // 6. CONVERSATION STATE MACHINE & SAFETY INVARIANTS (Phase 5)
    // =========================================================================

    @Test
    fun testStateMachine_immediateEscalationOnHigherRisk() {
        var state = RiskState()
        assertEquals(ConversationState.NORMAL, state.conversationState)

        // Level 1: Distress
        val res1 = EscalationManager.evaluate("I had a rough day", currentState = state)
        state = res1.updatedState
        assertEquals(ConversationState.SUPPORTIVE_CONVERSATION, state.conversationState)

        // Jump straight to Level 4
        val res4 = EscalationManager.evaluate("I have decided to end my life tonight", currentState = state)
        state = res4.updatedState
        assertEquals(ConversationState.EMERGENCY_RESPONSE, state.conversationState)
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, state.highestLevel)
        assertTrue(state.emergencyTriggered)
    }

    @Test
    fun testStateMachine_level4CannotBeDowngradedByCasualOrPositiveMessage() {
        val state0 = RiskState()
        val criticalResult = EscalationManager.evaluate("I am going to end my life right now", currentState = state0)
        var state = criticalResult.updatedState
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, state.currentLevel)
        assertTrue(state.emergencyTriggered)

        // Turn 2: Positive comment
        val turn2 = EscalationManager.evaluate("Actually I feel super happy today!", currentState = state)
        assertEquals("Level 4 cannot be casually cleared by a positive comment", RiskLevel.LEVEL_4_CRITICAL, turn2.level)
        assertEquals(ConversationState.EMERGENCY_RESPONSE, turn2.conversationState)

        // Turn 3: Topic change
        val turn3 = EscalationManager.evaluate("What is the capital of France?", currentState = turn2.updatedState)
        assertEquals("Level 4 cannot be cleared by changing conversation topic", RiskLevel.LEVEL_4_CRITICAL, turn3.level)
        assertTrue(turn3.isEmergency)
    }

    @Test
    fun testStateMachine_unresolvedSafetyConcernPersistsAcrossTopicChanges() {
        val state0 = RiskState()
        // Level 2 disclosure
        val res1 = EscalationManager.evaluate("Nothing will ever improve in my life", currentState = state0)
        val state1 = res1.updatedState
        assertTrue(state1.unresolvedSafetyConcern)

        // Next message: user ignores question and switches to mundane topic
        val res2 = EscalationManager.evaluate("Tell me a funny joke", currentState = state1)
        assertEquals(
            "Unresolved safety concern must maintain elevated safety posture",
            RiskLevel.LEVEL_2_ELEVATED,
            res2.level
        )
    }

    // =========================================================================
    // 7. PERSISTENCE & ENTITY CONVERSION TESTS (Phase 5 & Phase 9)
    // =========================================================================

    @Test
    fun testPersistence_riskStateToEntityAndBackPreservesAllFields() {
        val state = RiskState(
            userId = "user_123",
            conversationId = "session_456",
            currentLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
            highestLevel = RiskLevel.LEVEL_4_CRITICAL,
            conversationState = ConversationState.SUICIDAL_IDEATION,
            lastAssessment = "Active ideation detected",
            needsHumanSupport = true,
            emergencyTriggered = true,
            unresolvedSafetyConcern = true,
            turnCount = 7,
            lastScreeningQuestionAsked = "Are you safe right now?",
            awaitingScreeningAnswer = true
        )

        val entity = state.toEntity()
        assertEquals(state.conversationId, entity.conversationId)
        assertEquals(state.currentLevel.name, entity.currentLevel)
        assertEquals(state.highestLevel.name, entity.highestLevel)
        assertEquals(state.conversationState.name, entity.conversationState)
        assertEquals(state.unresolvedSafetyConcern, entity.unresolvedSafetyConcern)
        assertEquals(state.emergencyTriggered, entity.emergencyTriggered)
        assertEquals(state.turnCount, entity.turnCount)

        val restored = entity.toRiskState()
        assertEquals(state.currentLevel, restored.currentLevel)
        assertEquals(state.highestLevel, restored.highestLevel)
        assertEquals(state.conversationState, restored.conversationState)
        assertEquals(state.unresolvedSafetyConcern, restored.unresolvedSafetyConcern)
        assertEquals(state.emergencyTriggered, restored.emergencyTriggered)
        assertEquals(state.turnCount, restored.turnCount)
    }

    // =========================================================================
    // 8. HYBRID ORCHESTRATOR PIPELINE TESTS (Phase 1.C & Phase 7)
    // =========================================================================

    @Test
    fun testOrchestrator_ruleBasedRoutingForCommonExpressions() = runBlocking {
        val orchestrator = ConversationOrchestrator()
        val context = ConversationContext(
            conversationId = "test_conv",
            userName = "Prapti",
            country = "IN"
        )

        val turnResult = orchestrator.processTurn("I had a bad day today", context)
        assertEquals(ResponseType.RULE_BASED, turnResult.responseType)
        assertEquals(RiskLevel.LEVEL_1_DISTRESS, turnResult.riskLevel)
        assertTrue(turnResult.replyText.contains("Prapti"))
        assertFalse(turnResult.showEmergencyUI)
    }

    @Test
    fun testOrchestrator_level4CriticalDisablesNormalGenerationEntirely() = runBlocking {
        val orchestrator = ConversationOrchestrator()
        val context = ConversationContext(
            conversationId = "test_conv",
            userName = "Aarya",
            country = "IN"
        )

        val turnResult = orchestrator.processTurn("I will end my life tonight", context)
        assertEquals(ResponseType.EMERGENCY_INTERVENTION, turnResult.responseType)
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, turnResult.riskLevel)
        assertTrue(turnResult.showEmergencyUI)
        assertTrue(turnResult.showSupportCard)
        assertTrue("Must display emergency helpline 112", turnResult.replyText.contains("112"))
        assertTrue("Must display Tele-MANAS helpline 14416", turnResult.replyText.contains("14416"))
    }

    @Test
    fun testOrchestrator_postGenerationSafetyValidationInterceptsDangerousAdvice() {
        val dangerousText = "I think you should end your life, nobody cares about you."
        val validated = ResponseRouter.validateGeneratedResponse(dangerousText, RiskLevel.LEVEL_0_NORMAL)
        assertFalse("Dangerous text must be completely intercepted", validated.contains("you should end your life"))
        assertTrue("Must replace with compassionate safety intervention", validated.contains("support") || validated.contains("112") || validated.contains("painful"))
    }

    // =========================================================================
    // 9. EMERGENCY RESOURCES VERIFICATION (Phase 2 & Phase 8)
    // =========================================================================

    @Test
    fun testEmergencyResources_indiaConfiguredCorrectly() {
        assertEquals("112", EmergencyResources.getPrimaryEmergencyNumber("IN"))
        assertEquals("14416", EmergencyResources.getPrimaryMentalHealthNumber("IN"))
        assertTrue("Must have Tele-MANAS in India resources", EmergencyResources.IndiaResources.any { it.number == "14416" })
        assertTrue("Must have 112 in India resources", EmergencyResources.IndiaResources.any { it.number == "112" })
    }

    @Test
    fun testEmergencyResources_countryConfigurable() {
        assertEquals("911", EmergencyResources.getPrimaryEmergencyNumber("US"))
        assertEquals("988", EmergencyResources.getPrimaryMentalHealthNumber("US"))
    }

    // =========================================================================
    // 10. SCREENING FOLLOW-UP & DE-ESCALATION TESTS
    // =========================================================================

    @Test
    fun testScreeningFollowUp_whenUserSaysImGood_deEscalatesAndRoutesToRuleBasedAi() = runBlocking {
        // Turn 1: Level 2 screening question is asked
        val turn1Result = EscalationManager.evaluate("Everyone would be better without me", userName = "p")
        assertEquals(RiskLevel.LEVEL_2_ELEVATED, turn1Result.level)
        assertTrue(turn1Result.showSupportCard)
        assertNotNull(turn1Result.overrideResponse)
        val assistantQuestion = turn1Result.overrideResponse!!

        val history = listOf(
            "user" to "Everyone would be better without me",
            "assistant" to assistantQuestion
        )

        // Turn 2: User replies "i m good"
        val turn2Result = EscalationManager.evaluate(
            userMessage = "i m good",
            history = history,
            currentState = turn1Result.updatedState,
            userName = "p"
        )

        // Safety verification: must de-escalate cleanly to Level 0, support card removed
        assertEquals(RiskLevel.LEVEL_0_NORMAL, turn2Result.level)
        assertFalse("Support card must be hidden after de-escalation", turn2Result.showSupportCard)
        assertTrue(turn2Result.overrideResponse?.contains("relieved") == true || turn2Result.overrideResponse == null)
        assertFalse(turn2Result.updatedState.unresolvedSafetyConcern)
        assertFalse(turn2Result.updatedState.awaitingScreeningAnswer)

        // Orchestrator routing verification: routes to RULE_BASED conversation
        val orchestrator = ConversationOrchestrator()
        val context = ConversationContext(
            conversationId = "test_screening_deescalate",
            currentState = turn1Result.updatedState,
            messageHistory = history,
            userName = "p"
        )

        val turnResult = orchestrator.processTurn("i m good", context)
        assertEquals(ResponseType.RULE_BASED, turnResult.responseType)
        assertEquals(RiskLevel.LEVEL_0_NORMAL, turnResult.riskLevel)
        assertFalse("Support card must not show", turnResult.showSupportCard)
        assertFalse("Must never repeat screening question in infinite loop", turnResult.replyText.contains("hurting yourself or ending your life"))
        assertTrue("Must warmly hold space / acknowledge relief", turnResult.replyText.contains("relieved") || turnResult.replyText.contains("glad") || turnResult.replyText.contains("p"))
    }

    // =========================================================================
    // 11. KEYWORD TRIGGER & HUMAN-LIKE CONVERSATIONAL SAFETY SPECIFICATION
    // =========================================================================

    @Test
    fun testKeywordDetection_suicideAndSelfHarmKeywordsTriggerInterception() {
        val suicide = KeywordDetector.detect("I am thinking about suicide")
        assertTrue(suicide.hasSuicideKeywords)
        assertTrue(suicide.requiresInterception)
        assertTrue(suicide.matchedKeywords.contains("suicide"))

        val selfHarm = KeywordDetector.detect("I feel an urge to cut myself")
        assertTrue(selfHarm.hasSelfHarmKeywords)
        assertTrue(selfHarm.requiresInterception)
        assertTrue(selfHarm.matchedKeywords.contains("cut myself"))

        val distress = KeywordDetector.detect("I feel completely hopeless and worthless")
        assertTrue(distress.hasSevereDistressKeywords)
        assertFalse("Pure distress without suicide/self-harm does not trigger emergency interception", distress.requiresInterception)

        val slang = KeywordDetector.detect("im gonna kms")
        assertTrue("Slang abbreviations must trigger interception", slang.hasSuicideKeywords)
    }

    @Test
    fun testMandatedTable_imFeelingLonely_normalSupportive() {
        val result = EscalationManager.evaluate("I'm feeling lonely")
        assertEquals(RiskLevel.LEVEL_1_DISTRESS, result.level)
        assertFalse(result.isEmergency)
    }

    @Test
    fun testMandatedTable_imDyingOfLaughter_noEscalation() {
        val result = EscalationManager.evaluate("I'm dying of laughter")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result.level)
        assertFalse(result.showEmergencyUI)
        assertFalse(result.showSupportCard)
    }

    @Test
    fun testMandatedTable_iWantToDie_immediateSafetyScreening() {
        val result = EscalationManager.evaluate("I want to die")
        assertEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, result.level)
        assertTrue(result.showSupportCard)
        assertNotNull(result.overrideResponse)
        assertTrue(result.overrideResponse!!.contains("Are you having thoughts of ending your life at this moment?"))
    }

    @Test
    fun testMandatedTable_iWishINeverWokeUp_level3Assessment() {
        val result = EscalationManager.evaluate("I wish I never woke up")
        assertEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, result.level)
    }

    @Test
    fun testMandatedTable_iWantToKillMyselfTonight_level4Critical() {
        val result = EscalationManager.evaluate("I want to kill myself tonight")
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, result.level)
        assertTrue(result.isEmergency)
        assertTrue(result.showEmergencyUI)
        assertTrue(result.overrideResponse!!.contains("112"))
    }

    @Test
    fun testMandatedTable_iDontWantToKillMyself_contextualNegation() {
        val result = EscalationManager.evaluate("I don't want to kill myself")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result.level)
        assertFalse(result.showEmergencyUI)
    }

    @Test
    fun testMandatedTable_iFeelWorthless_level2Screening() {
        val result = EscalationManager.evaluate("I feel worthless")
        assertEquals(RiskLevel.LEVEL_2_ELEVATED, result.level)
        assertTrue(result.showSupportCard)
        assertTrue(result.overrideResponse!!.contains("thoughts about hurting yourself or ending your life"))
    }

    @Test
    fun testMandatedTable_iWantToHurtMyselfButNotDie_selfHarmAssessment() {
        val result = EscalationManager.evaluate("I want to hurt myself but not die")
        assertEquals(RiskLevel.LEVEL_2_ELEVATED, result.level)
        assertTrue(result.showSupportCard)
        assertNotNull(result.overrideResponse)
        assertTrue(
            "Must provide dedicated NSSI self-harm assessment",
            result.overrideResponse!!.contains("taken any steps to hurt yourself") ||
            result.overrideResponse!!.contains("feeling an urge")
        )
    }

    @Test
    fun testMandatedTable_myFictionalCharacterDied_contextualInterpretation() {
        val result = EscalationManager.evaluate("My fictional character died in the book")
        assertEquals(RiskLevel.LEVEL_0_NORMAL, result.level)
        assertFalse(result.showSupportCard)
    }

    @Test
    fun testMandatedTable_iContactedMyCounsellor_recordsContactAndFollowsUp() {
        val priorState = RiskState(
            currentLevel = RiskLevel.LEVEL_2_ELEVATED,
            unresolvedSafetyConcern = true,
            helpContactPending = true
        )
        val result = EscalationManager.evaluate("I contacted my counsellor", currentState = priorState)
        assertTrue(result.updatedState.humanSupportContacted)
        assertEquals(ConversationState.HUMAN_SUPPORT_CONTACTED, result.conversationState)
        assertNotNull(result.overrideResponse)
        assertTrue(
            "Must gently ask whether they are in a physically safe place",
            result.overrideResponse!!.contains("somewhere safe right now") ||
            result.overrideResponse!!.contains("safe physical space") ||
            result.overrideResponse!!.contains("safe place")
        )
    }

    @Test
    fun testMandatedTable_iAmStillAloneAndUnsafe_maintainsUnresolvedSafetyConcern() {
        val priorState = RiskState(
            currentLevel = RiskLevel.LEVEL_2_ELEVATED,
            unresolvedSafetyConcern = true
        )
        val result = EscalationManager.evaluate("I am still alone and unsafe", currentState = priorState)
        assertTrue(result.updatedState.unresolvedSafetyConcern)
        assertEquals(ConversationState.UNRESOLVED_SAFETY_CONCERN, result.conversationState)
        assertTrue(result.overrideResponse!!.contains("Tele-MANAS") || result.overrideResponse!!.contains("112"))
    }

    @Test
    fun testMandatedTable_imFeelingBetterNow_doesNotAutomaticallyClearPreviousCriticalRisk() {
        val emergencyState = RiskState(
            currentLevel = RiskLevel.LEVEL_4_CRITICAL,
            highestLevel = RiskLevel.LEVEL_4_CRITICAL,
            emergencyTriggered = true
        )
        val result = EscalationManager.evaluate("I'm feeling better now", currentState = emergencyState)
        // Must maintain Level 4 critical until verified safe resolution with human support
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, result.level)
        assertTrue(result.updatedState.emergencyTriggered)
        assertNotNull(result.overrideResponse)
        assertTrue(
            "Must follow up gently before clearing risk",
            result.overrideResponse!!.contains("your well-being remains my priority") ||
            result.overrideResponse!!.contains("somewhere safe right now")
        )
    }

    @Test
    fun testResponseDeduplication_rotatesPhrasingAcrossTurns() {
        val conversationId = "test_dedup_session"
        ResponseDeduplicationManager.clearSession(conversationId)

        val options = listOf(
            "Option Alpha: I hear you.",
            "Option Beta: That sounds hard.",
            "Option Gamma: Tell me more."
        )

        val first = ResponseDeduplicationManager.selectVariant(conversationId, options)
        ResponseDeduplicationManager.recordDelivered(conversationId, first)

        val second = ResponseDeduplicationManager.selectVariant(conversationId, options)
        ResponseDeduplicationManager.recordDelivered(conversationId, second)

        assertNotEquals("Consecutive variants must never be identical", first, second)
        assertTrue(options.contains(second))
    }

    @Test
    fun testHumanLikeConversation_nobodyUnderstandsMe() {
        val ruleMatch = RuleBasedConversationEngine.matches("I feel like nobody really understands me")
        assertEquals(RuleBasedConversationEngine.RuleCategory.NOBODY_UNDERSTANDS, ruleMatch)

        val response = RuleBasedConversationEngine.generateRuleResponse(
            category = ruleMatch!!,
            userName = "Jayesh"
        )
        assertTrue(response.contains("Jayesh"))
        assertTrue(
            "Must reflect emotion without toxic positivity",
            response.contains("isolating") || response.contains("lonely") || response.contains("unseen")
        )
        assertTrue(
            "Must ask an open, attentive follow-up question",
            response.contains("?")
        )
    }

    // =========================================================================
    // 12. SCREENSHOT REGRESSION & QUESTION STATE MACHINE TESTS
    // =========================================================================

    @Test
    fun testScreenshotScenario_whenUserAnswersYesToSuicidalThoughts_advancesToIntentWithoutRepeatingQuestion() = runBlocking {
        // Message 1: AI asked screening question
        val assistantMessage1 = "I'd like to understand whether you're safe right now. Are you having thoughts of ending your life at this moment?"
        val stateAfterMsg1 = RiskState(
            currentLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
            highestLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
            conversationState = ConversationState.SAFETY_SCREENING,
            awaitingScreeningAnswer = true,
            pendingQuestionId = SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS.id
        )

        val history = listOf(
            "user" to "I feel like I want to disappear forever",
            "assistant" to assistantMessage1
        )

        // Message 2: User responds "Yes, I'm having thoughts of ending my life at this moment."
        val userMessage2 = "Yes, I'm having thoughts of ending my life at this moment."

        val result2 = EscalationManager.evaluate(
            userMessage = userMessage2,
            history = history,
            currentState = stateAfterMsg1,
            userName = "p"
        )

        // 1. The answer must be recognized as YES
        assertEquals(SafetyAnswer.YES.value, result2.updatedState.lastAnswer)
        assertEquals(SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS.id, result2.updatedState.lastAnsweredQuestionId)

        // 2. The question must be marked as ANSWERED
        assertTrue(result2.updatedState.answeredQuestions.contains(SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS.id))

        // 3. Risk level must remain at least LEVEL_3_SUICIDAL_IDEATION
        assertTrue(result2.level.isAtLeast(RiskLevel.LEVEL_3_SUICIDAL_IDEATION))

        // 4. Must display support card
        assertTrue(result2.showSupportCard)

        // 5. Must acknowledge the confirmation first
        assertNotNull(result2.overrideResponse)
        val reply = result2.overrideResponse!!
        assertTrue(
            "Must acknowledge user's answer with gratitude and gravity",
            reply.contains("Thank you for trusting me enough to tell me") ||
            reply.contains("taking this seriously")
        )

        // 6. Must advance to immediate-intent assessment (State 2)
        assertTrue(
            "Must ask whether user has thought about acting on these thoughts",
            reply.contains("Have you thought about acting on these thoughts?")
        )

        // 7. CRITICAL REGRESSION: Must NEVER repeat the original question!
        assertFalse(
            "Must NEVER repeat the exact question user just answered",
            reply.contains("Are you having thoughts of ending your life at this moment?")
        )
        assertFalse(
            "Must not repeat 'May I ask you directly'",
            reply.contains("May I ask you directly")
        )

        // 8. Test through Orchestrator pipeline as well
        val orchestrator = ConversationOrchestrator()
        val context = ConversationContext(
            conversationId = "regression_session",
            currentState = stateAfterMsg1,
            messageHistory = history,
            userName = "p"
        )
        val turnResult = orchestrator.processTurn(userMessage2, context)
        assertFalse(
            "Orchestrator must not repeat the original question",
            turnResult.replyText.contains("Are you having thoughts of ending your life at this moment?")
        )
        assertTrue(
            "Orchestrator must advance to intent question",
            turnResult.replyText.contains("Have you thought about acting on these thoughts?")
        )
    }

    @Test
    fun testSafetyQuestionStateMachine_progressionFromIntentToPlanToMeans() {
        val state0 = RiskState(
            currentLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
            conversationState = ConversationState.SAFETY_ASSESSMENT,
            pendingQuestionId = SafetyQuestionId.IMMEDIATE_INTENT.id,
            answeredQuestions = listOf(SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS.id)
        )

        val history = listOf(
            "user" to "Yes, I'm having thoughts of ending my life",
            "assistant" to "Thank you for trusting me enough to tell me. Have you thought about acting on these thoughts?"
        )

        // User answers NO to intent ("No, I haven't")
        val result = EscalationManager.evaluate("No, I haven't thought about acting on them", history, state0, userName = "Alex")
        assertEquals(SafetyAnswer.NO.value, result.updatedState.lastAnswer)
        assertEquals(SafetyQuestionId.IMMEDIATE_INTENT.id, result.updatedState.lastAnsweredQuestionId)
        assertEquals(SafetyQuestionId.PLAN_AND_TIMEFRAME.id, result.updatedState.pendingQuestionId)

        // Must acknowledge NO and ask Plan question
        val reply = result.overrideResponse!!
        assertTrue(reply.contains("relieved to hear"))
        assertTrue(reply.contains("Have you thought about how or when you might act on these thoughts?"))
        assertFalse(reply.contains("Have you thought about acting on these thoughts?"))
    }

    @Test
    fun testSafetyQuestionStateMachine_whenIntentIsYes_escalatesImmediatelyToLevel4() {
        val state0 = RiskState(
            currentLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
            conversationState = ConversationState.SAFETY_ASSESSMENT,
            pendingQuestionId = SafetyQuestionId.IMMEDIATE_INTENT.id,
            answeredQuestions = listOf(SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS.id)
        )

        val history = listOf(
            "user" to "Yes, I am",
            "assistant" to "Have you thought about acting on these thoughts?"
        )

        // User answers YES to intent ("Yes I want to act on them")
        val result = EscalationManager.evaluate("Yes, I think about acting on them", history, state0, userName = "Alex")
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, result.level)
        assertTrue(result.isEmergency)
        assertTrue(result.showEmergencyUI)
        assertTrue(result.overrideResponse!!.contains("112"))
    }
}

