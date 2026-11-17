package com.example.safety

import com.example.data.model.RegulationMode
import com.example.data.safety.ConversationContext
import com.example.data.safety.ConversationState
import com.example.data.safety.ConversationalStateTracker
import com.example.data.safety.DeterministicEmotionalEngine
import com.example.data.safety.EmergencyResources
import com.example.data.safety.EmotionalClassificationEngine
import com.example.data.safety.EmotionalClassificationEngine.EmotionCategory
import com.example.data.safety.EmotionalResponseLibrary
import com.example.data.safety.RiskLevel
import com.example.data.safety.RiskState
import com.example.data.safety.SafetyQuestionId
import com.example.data.safety.UserSituation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeterministicEmotionalEngineTest {

    @Before
    fun setUp() {
        DeterministicEmotionalEngine.clearSession("test_session")
        DeterministicEmotionalEngine.clearSession("active_session")
    }

    // =========================================================================
    // 1. CLASSIFICATION & DISAMBIGUATION TESTS
    // =========================================================================

    @Test
    fun testClassification_acrossStandardCategories() {
        val anxiety = EmotionalClassificationEngine.classify("I feel so anxious and worried about the future")
        assertEquals(EmotionCategory.ANXIETY, anxiety.primaryCategory)

        val panic = EmotionalClassificationEngine.classify("I am having a panic attack and my heart is racing")
        assertEquals(EmotionCategory.PANIC, panic.primaryCategory)

        val sadness = EmotionalClassificationEngine.classify("I feel so sad and I cried all morning")
        assertEquals(EmotionCategory.SADNESS, sadness.primaryCategory)

        val overthinking = EmotionalClassificationEngine.classify("My mind won't stop spiraling and overthinking everything")
        assertEquals(EmotionCategory.OVERTHINKING, overthinking.primaryCategory)

        val academic = EmotionalClassificationEngine.classify("I'm so stressed about failing my exam and finals week")
        assertEquals(EmotionCategory.ACADEMIC_STRESS, academic.primaryCategory)

        val relationship = EmotionalClassificationEngine.classify("I had a huge fight with my partner and we are drifting apart")
        assertEquals(EmotionCategory.RELATIONSHIP_PROBLEMS, relationship.primaryCategory)

        val loneliness = EmotionalClassificationEngine.classify("I feel completely lonely and isolated, nobody cares")
        assertEquals(EmotionCategory.LONELINESS, loneliness.primaryCategory)

        val burnout = EmotionalClassificationEngine.classify("I'm experiencing severe burnout and overworked to the limit")
        assertEquals(EmotionCategory.BURNOUT, burnout.primaryCategory)
    }

    @Test
    fun testClassification_negationDisambiguation() {
        // "I don't want to die" must NOT trigger affirmative suicidal ideation
        val negated = EmotionalClassificationEngine.classify("I'm hurting but I don't want to die")
        assertTrue("Must be marked as negated", negated.isNegated)
        assertNotEquals("Must not classify as active SUICIDAL_IDEATION", EmotionCategory.SUICIDAL_IDEATION, negated.primaryCategory)
        assertEquals(EmotionCategory.DEPRESSION_DISTRESS, negated.primaryCategory)

        // Affirmative "I want to die" MUST trigger suicidal ideation
        val affirmative = EmotionalClassificationEngine.classify("I want to die right now")
        assertFalse("Must not be negated", affirmative.isNegated)
        assertEquals(EmotionCategory.SUICIDAL_IDEATION, affirmative.primaryCategory)
    }

    @Test
    fun testClassification_metaphoricalIdiomDisambiguation() {
        // "I killed that exam" must NOT trigger self-harm or crisis
        val examMetaphor = EmotionalClassificationEngine.classify("I totally killed that exam today!")
        assertTrue("Must be identified as metaphorical", examMetaphor.isMetaphorical)
        assertEquals(EmotionCategory.POSITIVE_EMOTIONS, examMetaphor.primaryCategory)

        // "dying to see" must NOT trigger suicidal ideation
        val dyingMetaphor = EmotionalClassificationEngine.classify("I am dying to see the new concert")
        assertTrue("Must be identified as metaphorical", dyingMetaphor.isMetaphorical)
        assertEquals(EmotionCategory.POSITIVE_EMOTIONS, dyingMetaphor.primaryCategory)

        // "homework is killing me" must classify as academic stress
        val homeworkMetaphor = EmotionalClassificationEngine.classify("This homework is killing me")
        assertTrue("Must be identified as metaphorical", homeworkMetaphor.isMetaphorical)
        assertEquals(EmotionCategory.ACADEMIC_STRESS, homeworkMetaphor.primaryCategory)
    }

    @Test
    fun testClassification_thirdPersonAndExternalObjectDisambiguation() {
        // "That movie was depressing" must NOT trigger personal depression distress
        val movieRef = EmotionalClassificationEngine.classify("That movie was so depressing")
        assertTrue("Must be identified as external object", movieRef.isThirdPersonOrObject)
        assertNotEquals(EmotionCategory.DEPRESSION_DISTRESS, movieRef.primaryCategory)
    }

    // =========================================================================
    // 2. RESPONSE LIBRARY VERIFICATION TESTS (Quotas, Distinctness, Safety)
    // =========================================================================

    @Test
    fun testResponseLibrary_quotaVerification() {
        val templates = EmotionalResponseLibrary.templates

        // Minimum total allocation must be at least 100
        assertTrue("Response library must contain at least 100 templates, found ${templates.size}", templates.size >= 100)

        // Verify minimum category quotas
        val categoryCounts = templates.groupBy { it.category }
        val anxietyCount = (categoryCounts[EmotionCategory.ANXIETY]?.size ?: 0) + (categoryCounts[EmotionCategory.PANIC]?.size ?: 0)
        assertTrue("Anxiety count must be >= 8, was $anxietyCount", anxietyCount >= 8)

        val sadnessCount = categoryCounts[EmotionCategory.SADNESS]?.size ?: 0
        assertTrue("Sadness count must be >= 8, was $sadnessCount", sadnessCount >= 8)

        val lonelinessCount = categoryCounts[EmotionCategory.LONELINESS]?.size ?: 0
        assertTrue("Loneliness count must be >= 7, was $lonelinessCount", lonelinessCount >= 7)

        val overthinkingCount = categoryCounts[EmotionCategory.OVERTHINKING]?.size ?: 0
        assertTrue("Overthinking count must be >= 7, was $overthinkingCount", overthinkingCount >= 7)

        val academicCount = categoryCounts[EmotionCategory.ACADEMIC_STRESS]?.size ?: 0
        assertTrue("Academic stress count must be >= 7, was $academicCount", academicCount >= 7)

        val relationshipCount = categoryCounts[EmotionCategory.RELATIONSHIP_PROBLEMS]?.size ?: 0
        assertTrue("Relationship count must be >= 8, was $relationshipCount", relationshipCount >= 8)

        val angerCount = categoryCounts[EmotionCategory.ANGER]?.size ?: 0
        assertTrue("Anger count must be >= 5, was $angerCount", angerCount >= 5)

        val lowSelfEsteemCount = categoryCounts[EmotionCategory.LOW_SELF_ESTEEM]?.size ?: 0
        assertTrue("Low self-esteem count must be >= 6, was $lowSelfEsteemCount", lowSelfEsteemCount >= 6)

        val burnoutCount = categoryCounts[EmotionCategory.BURNOUT]?.size ?: 0
        assertTrue("Burnout count must be >= 6, was $burnoutCount", burnoutCount >= 6)

        val fearCount = categoryCounts[EmotionCategory.FEAR]?.size ?: 0
        assertTrue("Fear count must be >= 5, was $fearCount", fearCount >= 5)

        val guiltCount = categoryCounts[EmotionCategory.GUILT]?.size ?: 0
        assertTrue("Guilt count must be >= 5, was $guiltCount", guiltCount >= 5)

        val griefCount = categoryCounts[EmotionCategory.GRIEF]?.size ?: 0
        assertTrue("Grief count must be >= 4, was $griefCount", griefCount >= 4)

        val sleepCount = categoryCounts[EmotionCategory.SLEEP_DIFFICULTIES]?.size ?: 0
        assertTrue("Sleep difficulties count must be >= 4, was $sleepCount", sleepCount >= 4)

        val numbnessCount = categoryCounts[EmotionCategory.EMOTIONAL_NUMBNESS]?.size ?: 0
        assertTrue("Emotional numbness count must be >= 4, was $numbnessCount", numbnessCount >= 4)

        val socialCount = categoryCounts[EmotionCategory.SOCIAL_ANXIETY]?.size ?: 0
        assertTrue("Social anxiety count must be >= 4, was $socialCount", socialCount >= 4)

        val familyCount = categoryCounts[EmotionCategory.FAMILY_CONFLICT]?.size ?: 0
        assertTrue("Family conflict count must be >= 4, was $familyCount", familyCount >= 4)

        val motivationCount = categoryCounts[EmotionCategory.LACK_OF_MOTIVATION]?.size ?: 0
        assertTrue("Motivation count must be >= 4, was $motivationCount", motivationCount >= 4)

        val positiveCount = categoryCounts[EmotionCategory.POSITIVE_EMOTIONS]?.size ?: 0
        assertTrue("Positive emotions count must be >= 4, was $positiveCount", positiveCount >= 4)
    }

    @Test
    fun testResponseLibrary_allTemplatesUniqueAndNoToxicPositivity() {
        val templates = EmotionalResponseLibrary.templates
        val texts = templates.map { it.text.trim().lowercase() }
        val uniqueTexts = texts.toSet()

        assertEquals("Every response template must be distinct", texts.size, uniqueTexts.size)

        // Verify absence of toxic positivity and dismissing tropes
        val forbiddenPhrases = listOf(
            "everything happens for a reason",
            "just be positive",
            "others have it worse",
            "stop overthinking",
            "everything will be fine",
            "you need to be stronger",
            "cheer up"
        )

        for (t in templates) {
            val lower = t.text.lowercase()
            for (forbidden in forbiddenPhrases) {
                assertFalse(
                    "Template ${t.id} contains forbidden phrase '$forbidden': ${t.text}",
                    lower.contains(forbidden)
                )
            }
        }
    }

    // =========================================================================
    // 3. CRISIS SAFETY ENGINE & PROGRESSION TESTS
    // =========================================================================

    @Test
    fun testCrisisEngine_absolutePriorityOverride() {
        val context = ConversationContext(conversationId = "crisis_test", country = "IN")

        // Active ideation triggers safety screening with question
        val turn1 = DeterministicEmotionalEngine.processTurn("I want to end my life", context)
        assertEquals(RiskLevel.LEVEL_3_SUICIDAL_IDEATION, turn1.riskLevel)
        assertTrue(turn1.requiresEmergencyUI)
        assertEquals(SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS, turn1.pendingQuestionId)

        // Immediate danger triggers Level 4 emergency
        val turnDanger = DeterministicEmotionalEngine.processTurn("someone is hurting me in immediate danger", context)
        assertEquals(RiskLevel.LEVEL_4_CRITICAL, turnDanger.riskLevel)
        assertTrue(turnDanger.requiresEmergencyUI)
    }

    @Test
    fun testCrisisEngine_progressiveQuestionStateMachine() {
        val context = ConversationContext(conversationId = "crisis_prog", country = "IN")

        // 1. Initial suicidal message
        val turn1 = DeterministicEmotionalEngine.processTurn("I want to die", context)
        assertEquals(SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS, turn1.pendingQuestionId)

        // 2. User answers "yes" to thoughts of ending life -> advances to IMMEDIATE_INTENT
        val state1 = RiskState(
            conversationId = "crisis_prog",
            currentLevel = turn1.riskLevel,
            pendingQuestionId = turn1.pendingQuestionId?.name,
            unresolvedSafetyConcern = true
        )
        val turn2 = DeterministicEmotionalEngine.processTurn("yes, I do", context.copy(currentState = state1))
        assertEquals(SafetyQuestionId.IMMEDIATE_INTENT, turn2.pendingQuestionId)
        assertTrue(turn2.responseText.contains("acting on these thoughts", ignoreCase = true))

        // 3. User answers "no" to immediate intent -> advances to PLAN_AND_TIMEFRAME
        val state2 = state1.copy(pendingQuestionId = turn2.pendingQuestionId?.name)
        val turn3 = DeterministicEmotionalEngine.processTurn("no I haven't thought about acting", context.copy(currentState = state2))
        assertEquals(SafetyQuestionId.PLAN_AND_TIMEFRAME, turn3.pendingQuestionId)
        assertTrue(turn3.responseText.contains("how or when", ignoreCase = true))

        // 4. User answers "no" to plan -> advances to ACCESS_TO_MEANS or HUMAN_SUPPORT
        val state3 = state2.copy(pendingQuestionId = turn3.pendingQuestionId?.name)
        val turn4 = DeterministicEmotionalEngine.processTurn("no I don't have any plan", context.copy(currentState = state3))
        assertNotNull(turn4.pendingQuestionId)
        assertTrue(turn4.responseText.contains("access to anything", ignoreCase = true) || turn4.responseText.contains("relief", ignoreCase = true))
    }

    @Test
    fun testCrisisEngine_resolvesCleanlyOn_im_good_and_safe_denial_variants() {
        val pendingQuestionState = RiskState(
            conversationId = "safe_denial_test",
            currentLevel = RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
            pendingQuestionId = SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS.name,
            unresolvedSafetyConcern = true
        )
        val context = ConversationContext(
            conversationId = "safe_denial_test",
            currentState = pendingQuestionState,
            userName = "P",
            country = "IN"
        )

        // 1. "i m. good" (mobile keyboard punctuation bug reproduction)
        val turnPunctuation = DeterministicEmotionalEngine.processTurn("i m. good", context)
        assertNull(turnPunctuation.pendingQuestionId)
        assertEquals(RiskLevel.LEVEL_0_NORMAL, turnPunctuation.riskLevel)
        assertEquals(ConversationState.NORMAL, turnPunctuation.conversationState)
        assertFalse(turnPunctuation.responseText.contains("ending your life right now", ignoreCase = true))
        assertTrue(turnPunctuation.responseText.contains("relieved and glad to hear you're doing okay", ignoreCase = true))

        // 2. "i am good" (must NOT be treated as affirmative "I am")
        val turnIAmGood = DeterministicEmotionalEngine.processTurn("i am good", context)
        assertNull(turnIAmGood.pendingQuestionId)
        assertEquals(RiskLevel.LEVEL_0_NORMAL, turnIAmGood.riskLevel)
        assertFalse(turnIAmGood.responseText.contains("ending your life right now", ignoreCase = true))

        // 3. "good" / "fine" / "im okay" / "just overwhelmed"
        val variants = listOf("good", "fine", "ok", "okay", "i'm good", "im fine", "i am safe", "just overwhelmed")
        for (variant in variants) {
            val turn = DeterministicEmotionalEngine.processTurn(variant, context)
            assertNull("Variant '$variant' should clear pendingQuestionId", turn.pendingQuestionId)
            assertEquals("Variant '$variant' should resolve to LEVEL_0_NORMAL", RiskLevel.LEVEL_0_NORMAL, turn.riskLevel)
            assertFalse("Variant '$variant' should not ask about ending life", turn.responseText.contains("ending your life right now", ignoreCase = true))
        }
    }

    @Test
    fun testCrisisEngine_verifiedEmergencyHelplines() {
        assertEquals("112", EmergencyResources.getPrimaryEmergencyNumber("IN"))
        assertEquals("14416", EmergencyResources.getPrimaryMentalHealthNumber("IN"))
        assertEquals("911", EmergencyResources.getPrimaryEmergencyNumber("US"))
        assertEquals("988", EmergencyResources.getPrimaryMentalHealthNumber("US"))

        val inResources = EmergencyResources.IndiaResources
        assertTrue(inResources.any { it.number == "112" })
        assertTrue(inResources.any { it.number == "14416" })
        assertTrue(inResources.any { it.number == "1800-599-0019" })
    }

    // =========================================================================
    // 4. CONVERSATION MODES & DEDUPLICATION TESTS
    // =========================================================================

    @Test
    fun testConversationalModes_resolvedAppropriately() {
        val context = ConversationContext(conversationId = "mode_test")

        // Panic / Anxiety should resolve to CALM
        val panicTurn = DeterministicEmotionalEngine.processTurn("I am having a panic attack", context)
        assertEquals(RegulationMode.CALM, panicTurn.appliedMode)

        // Sadness should resolve to LISTEN
        val sadTurn = DeterministicEmotionalEngine.processTurn("I feel so sad today", context)
        assertEquals(RegulationMode.LISTEN, sadTurn.appliedMode)

        // Overthinking should resolve to UNDERSTAND
        val thinkTurn = DeterministicEmotionalEngine.processTurn("I am overthinking my entire future", context)
        assertEquals(RegulationMode.UNDERSTAND, thinkTurn.appliedMode)

        // Low self esteem should resolve to REFLECT
        val esteemTurn = DeterministicEmotionalEngine.processTurn("I hate myself and feel like a failure", context)
        assertEquals(RegulationMode.REFLECT, esteemTurn.appliedMode)
    }

    @Test
    fun testDeduplication_slidingWindowPreventsRepetition() {
        val context = ConversationContext(conversationId = "dedup_session")

        val usedTemplates = mutableListOf<String>()

        // Send 6 consecutive anxiety messages
        for (i in 1..6) {
            val turn = DeterministicEmotionalEngine.processTurn("I'm feeling very anxious right now", context)
            val templateId = turn.selectedTemplateId
            assertNotNull(templateId)
            assertFalse(
                "Template $templateId must not have been used in recent turns! Already used: $usedTemplates",
                usedTemplates.contains(templateId)
            )
            usedTemplates.add(templateId!!)
        }

        assertEquals(6, usedTemplates.toSet().size)
    }

    // =========================================================================
    // 5. PROACTIVE SUPPORT & NO PERMISSION-BASED QUESTIONS TESTS
    // =========================================================================

    @Test
    fun testResponseLibrary_noPermissionBasedPhrases() {
        val permissionPhrases = listOf(
            "would you like me to",
            "should i help you",
            "should i suggest",
            "do you want to try",
            "would you like some tips",
            "can i suggest",
            "would you like to talk about it",
            "would you like to talk it through",
            "would a short grounding exercise help",
            "would a 4-7-8 breathing sequence help"
        )

        for (template in EmotionalResponseLibrary.templates) {
            val lower = template.text.lowercase()
            for (phrase in permissionPhrases) {
                assertFalse(
                    "Template ${template.id} must not ask permission before providing support ('$phrase'): ${template.text}",
                    lower.contains(phrase)
                )
            }
        }
    }

    @Test
    fun testProactiveSupport_directGuidanceWithoutAskingPermission() {
        val context = ConversationContext(conversationId = "proactive_test")

        // 1. Anxiety: directly provides slow breathing, relaxing shoulders, unclenched jaw
        val anxietyTurn = DeterministicEmotionalEngine.processTurn("I feel really anxious", context)
        assertFalse(anxietyTurn.responseText.contains("Would you like me to", ignoreCase = true))
        assertFalse(anxietyTurn.responseText.contains("Do you want to try", ignoreCase = true))
        assertTrue(anxietyTurn.responseText.contains("breath", ignoreCase = true))

        // 2. Overthinking: directly provides sensory grounding (5 things, etc.)
        val overthinkTurn = DeterministicEmotionalEngine.processTurn("I can't stop overthinking", context)
        assertFalse(overthinkTurn.responseText.contains("Can I suggest", ignoreCase = true))
        assertTrue(overthinkTurn.responseText.contains("thoughts", ignoreCase = true))

        // 3. Sadness: directly validates and suggests comforting action
        val sadTurn = DeterministicEmotionalEngine.processTurn("I'm feeling really low today", context)
        assertFalse(sadTurn.responseText.contains("Should I help you", ignoreCase = true))
        assertTrue(sadTurn.responseText.contains("difficult", ignoreCase = true) || sadTurn.responseText.contains("gentle", ignoreCase = true))
    }

    // =========================================================================
    // 6. CONVERSATIONAL INTELLIGENCE & CONTEXTUAL PROGRESSION TESTS
    // =========================================================================

    @Test
    fun testExactBreakupConversationFlow() {
        val convId = "breakup_flow_session"
        DeterministicEmotionalEngine.clearSession(convId)
        val context = ConversationContext(conversationId = convId)

        // Turn 1: User: "I feel anxious."
        val turn1 = DeterministicEmotionalEngine.processTurn("I feel anxious.", context)
        assertTrue(turn1.responseText.contains("anxiety", ignoreCase = true))
        assertTrue(
            turn1.responseText.contains("exhausting", ignoreCase = true) ||
            turn1.responseText.contains("overwhelming", ignoreCase = true)
        )
        assertFalse(turn1.responseText.contains(", p.", ignoreCase = true))

        // Turn 2: User: "Yeah, I am going through a breakup."
        val turn2 = DeterministicEmotionalEngine.processTurn("Yeah, I am going through a breakup.", context)
        assertTrue("Must address the breakup directly: ${turn2.responseText}", turn2.responseText.contains("breakup", ignoreCase = true))
        assertFalse("Must NOT use generic clarification fallback!", turn2.isClarification)
        assertFalse("Must NOT return generic clarification prompt", turn2.responseText.contains("I want to make sure I truly understand", ignoreCase = true))

        // Verify contextual emotion transition
        val state = ConversationalStateTracker.getOrCreate(convId)
        assertEquals(EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS, state.primaryEmotion)
        assertTrue(state.secondaryEmotions.contains(EmotionalClassificationEngine.EmotionCategory.ANXIETY))
        assertTrue(state.disclosedSituations.contains(UserSituation.BREAKUP))

        // Turn 3: User: "I still miss her."
        val turn3 = DeterministicEmotionalEngine.processTurn("I still miss her.", context)
        assertTrue(
            "Must address missing her / routine / grief: ${turn3.responseText}",
            turn3.responseText.contains("miss", ignoreCase = true) || turn3.responseText.contains("routine", ignoreCase = true)
        )
        assertFalse("Must NOT use generic clarification fallback!", turn3.isClarification)

        // Turn 4: User: "I keep checking her Instagram."
        val turn4 = DeterministicEmotionalEngine.processTurn("I keep checking her Instagram.", context)
        assertTrue(
            "Must address social media checking / wound / muting: ${turn4.responseText}",
            turn4.responseText.contains("checking", ignoreCase = true) ||
            turn4.responseText.contains("profile", ignoreCase = true) ||
            turn4.responseText.contains("muting", ignoreCase = true) ||
            turn4.responseText.contains("wound", ignoreCase = true)
        )
        assertFalse("Must NOT use generic clarification fallback!", turn4.isClarification)
    }

    @Test
    fun testRepeatedBreakupDisclosure_providesContextualSupportInsteadOfGenericFallback() {
        val convId = "repeat_breakup_session"
        DeterministicEmotionalEngine.clearSession(convId)
        val context = ConversationContext(conversationId = convId)

        // Turn 1: user discloses breakup
        val turn1 = DeterministicEmotionalEngine.processTurn("I am going through a breakup.", context)
        assertTrue(turn1.responseText.contains("breakup", ignoreCase = true))

        // Turn 2: user repeats the exact same disclosure
        val turn2 = DeterministicEmotionalEngine.processTurn("I am going through a breakup.", context)
        assertFalse("Turn 2 must NOT repeat turn 1 identical text", turn2.responseText == turn1.responseText)
        assertFalse("Turn 2 must NOT be generic clarification", turn2.responseText.contains("I want to make sure I truly understand", ignoreCase = true))
        assertTrue(
            "Turn 2 must acknowledge the repeated breakup disclosure: ${turn2.responseText}",
            turn2.responseText.contains("breakup", ignoreCase = true) && turn2.responseText.contains("weighing", ignoreCase = true)
        )
    }

    @Test
    fun testNeverReturnsGenericClarificationInquiry() {
        val convId = "never_generic_session"
        DeterministicEmotionalEngine.clearSession(convId)
        val context = ConversationContext(conversationId = convId)

        // Even with ambiguous or casual input, never return the banned generic string
        val ambiguousInputs = listOf("hmm", "maybe", "idk", "what now", "just stuff")
        for (input in ambiguousInputs) {
            val turn = DeterministicEmotionalEngine.processTurn(input, context)
            assertFalse(
                "Must never return banned generic fallback string on input '$input'",
                turn.responseText.contains("I want to make sure I truly understand what you're going through", ignoreCase = true)
            )
        }
    }

    @Test
    fun testPersonalization_noSingleLetterPlaceholderBug() {
        val convId = "placeholder_name_session"
        DeterministicEmotionalEngine.clearSession(convId)

        // Single letter "p"
        val contextWithP = ConversationContext(conversationId = convId, userName = "p")
        val turnP = DeterministicEmotionalEngine.processTurn("I feel anxious.", contextWithP)
        assertFalse("Must never inject single-letter name as ', p.': ${turnP.responseText}", turnP.responseText.contains(", p.", ignoreCase = true))
        assertFalse("Must never inject standalone 'p' placeholder", turnP.responseText.contains(" p.", ignoreCase = true))

        // Real human name
        val contextWithRealName = ConversationContext(conversationId = convId, userName = "Jayesh")
        val turnGreeting = DeterministicEmotionalEngine.processTurn("Hello", contextWithRealName)
        // Greetings format cleanly
        if (turnGreeting.responseText.startsWith("Hello", ignoreCase = true)) {
            assertTrue(turnGreeting.responseText.contains("Jayesh"))
        }
    }

    @Test
    fun testAcademicStressFollowedByParentsDisappointment() {
        val convId = "acad_parents_session"
        DeterministicEmotionalEngine.clearSession(convId)
        val context = ConversationContext(conversationId = convId)

        val turn1 = DeterministicEmotionalEngine.processTurn("I'm failing my exams", context)
        assertTrue(turn1.responseText.contains("exam", ignoreCase = true) || turn1.responseText.contains("studying", ignoreCase = true) || turn1.responseText.contains("pressure", ignoreCase = true))

        val turn2 = DeterministicEmotionalEngine.processTurn("I am so afraid of disappointing my parents", context)
        assertTrue("Must address disappointing parents: ${turn2.responseText}", turn2.responseText.contains("parents", ignoreCase = true))
    }

    @Test
    fun testLonelinessFollowedByFriendshipExclusion() {
        val convId = "lonely_friends_session"
        DeterministicEmotionalEngine.clearSession(convId)
        val context = ConversationContext(conversationId = convId)

        val turn1 = DeterministicEmotionalEngine.processTurn("I feel so lonely", context)
        assertTrue(turn1.responseText.contains("lonely", ignoreCase = true) || turn1.responseText.contains("isolated", ignoreCase = true) || turn1.responseText.contains("alone", ignoreCase = true))

        val turn2 = DeterministicEmotionalEngine.processTurn("My friends hung out without me and left me out", context)
        assertTrue("Must address friend exclusion: ${turn2.responseText}", turn2.responseText.contains("left out", ignoreCase = true) || turn2.responseText.contains("friends", ignoreCase = true))
    }
}
