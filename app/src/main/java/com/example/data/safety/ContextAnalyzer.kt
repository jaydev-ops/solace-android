package com.example.data.safety

import java.util.Locale

/**
 * Robust Contextual Language Understanding for Solace+ Mental Health Safety.
 * Handles negation, figurative idioms, fictional/third-party/historical framing,
 * multi-turn screening responses, and ambiguous distress expressions.
 */
object ContextAnalyzer {

    // --- 1. Figurative Idioms & Common Expressions (Non-suicidal) ---
    private val figurativePatterns = listOf(
        Regex("""\bdying\s+of\s+laughter\b""", RegexOption.IGNORE_CASE),
        Regex("""\bdying\s+to\s+(see|know|hear|go|meet|try|find|watch|eat)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bdying\s+for\s+(a\s+)?(coffee|tea|drink|food|nap|rest|break|vacation)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(homework|work|job|boss|exam|test|traffic|heat|cold|day|shift)\s+is\s+killing\s+me\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(workout|run|gym|training|leg\s+day)\s+killed\s+me\b""", RegexOption.IGNORE_CASE),
        Regex("""\bkilled\s+it\s+(at|today|yesterday|on|in)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bstarved?\s+to\s+death\b""", RegexOption.IGNORE_CASE),
        Regex("""\bbored\s+to\s+death\b""", RegexOption.IGNORE_CASE),
        Regex("""\bscared\s+to\s+death\b""", RegexOption.IGNORE_CASE),
        Regex("""\bkill\s+two\s+birds\b""", RegexOption.IGNORE_CASE),
        Regex("""\blaughing\s+my\s+head\s+off\b""", RegexOption.IGNORE_CASE),
        Regex("""\bdead\s+tired\b""", RegexOption.IGNORE_CASE)
    )

    // --- 2. Fictional, Script, Story, or Third-Party Contexts ---
    private val fictionalPatterns = listOf(
        Regex("""\b(my\s+)?(fictional\s+character|character\s+in|story|novel|book|script|plot|protagonist|roleplay|rpg)\b.*?\b(wants?\s+to\s+die|dies?|suicide|ends?\s+life)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bin\s+the\s+(movie|show|series|anime|game|book|film)\b.*?\b(character|someone|he|she|actor)\b.*?\b(suicide|died|killed)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bwriting\s+a\s+(story|scene|script|poem|novel)\s+about\b""", RegexOption.IGNORE_CASE),
        Regex("""\bfor\s+(a\s+)?(class|play|theater|screenplay|assignment)\b.*?\b(character|suicide|death)\b""", RegexOption.IGNORE_CASE)
    )

    private val thirdPartyPatterns = listOf(
        Regex("""\b(my\s+friend|my\s+cousin|my\s+brother|my\s+sister|someone\s+i\s+know|a\s+person)\s+(wants?\s+to|is\s+thinking\s+of)\s+(die|commit\s+suicide|kill\s+themselves|hurt\s+themselves)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(he|she|they)\s+(said|told\s+me)\s+(he|she|they)\s+(wants?\s+to\s+die|is\s+suicidal)\b""", RegexOption.IGNORE_CASE)
    )

    // --- 3. Historical Reflection (Past vs Current Intent) ---
    private val historicalPatterns = listOf(
        Regex("""\b(years?|months?|a\s+long\s+time)\s+ago\s+(i\s+felt|i\s+wanted|i\s+was\s+suicidal|i\s+thought\s+about\s+dying)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bi\s+used\s+to\s+(think\s+about\s+suicide|want\s+to\s+die|hurt\s+myself)\s+in\s+the\s+past\b""", RegexOption.IGNORE_CASE),
        Regex("""\bused\s+to\s+struggle\s+with\s+(suicide|self[- ]harm)\s+years\s+ago\b""", RegexOption.IGNORE_CASE)
    )

    // --- 4. Negation Patterns ---
    private val negationPatterns = listOf(
        Regex("""\b(do\s*n['o]t|dont|did\s*n['o]t|will\s*n['o]t|wont|am\s+not|never)\s+(want|intend|planning?|thinking?)\s+to\s+(kill\s+myself|die|end\s+my\s+life|hurt\s+myself|commit\s+suicide)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(not|never)\s+(suicidal|thinking\s+about\s+suicide|going\s+to\s+kill\s+myself|going\s+to\s+hurt\s+myself)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bwould\s+never\s+(hurt\s+myself|kill\s+myself|commit\s+suicide|end\s+my\s+life)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bno\s+intention\s+of\s+(dying|killing\s+myself|suicide)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bdo\s*n['o]t\s+want\s+to\s+(die|end\s+it)\b""", RegexOption.IGNORE_CASE)
    )

    // --- 5. Level 4: Immediate Danger / Emergency Patterns ---
    private val criticalImmediateIntent = listOf(
        Regex("""\b(want|planning|decided|going)\s+to\s+(end\s+my\s+life|kill\s+myself)\s+(tonight|today|now|soon|right\s+now)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(will|shall)\s+(end\s+my\s+life|kill\s+myself)(\s+(tonight|today|now|soon|right\s+now))?\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(have\s+decided|i\s+am\s+going|i\s+will)\s+to\s+(kill\s+myself|end\s+my\s+life)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(end|ending|kill)\s+my\s+life\s+(tonight|today|now|soon)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(goodbye\s+everyone|goodbye\s+world|this\s+is\s+my\s+last\s+day|goodbye\s+forever)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(have\s+the\s+pills\s+ready|have\s+the\s+rope|about\s+to\s+jump|standing\s+on\s+the\s+ledge|standing\s+on\s+the\s+balcony)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(wrote|written)\s+(my\s+)?suicide\s+note\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(giving|gave)\s+away\s+(all\s+my\s+things|my\s+belongings|my\s+possessions)\b""", RegexOption.IGNORE_CASE)
    )

    private val criticalRecentAttempt = listOf(
        Regex("""\b(already|just)\s+attempted\s+suicide\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(already|just)\s+(swallowed|took)\s+(all\s+the|a\s+bottle\s+of|excessive)\s+(pills|medication|tablets)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(already\s+tried|attempted)\s+to\s+(kill\s+myself|end\s+my\s+life)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bbleeding\s+(heavily|a\s+lot)\s+from\s+(cutting|hurting\s+myself)\b""", RegexOption.IGNORE_CASE)
    )

    // --- 6. Level 3: Suicidal Ideation Patterns ---
    private val suicidalIdeationPatterns = listOf(
        Regex("""\b(wish\s+i\s+could\s+go\s+to\s+sleep\s+and\s+never\s+wake\s+up|sleep\s+and\s+never\s+wake\s+up|wish\s+i\s+never\s+woke\s+up)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(wish\s+i\s+was\s+dead|wish\s+i\s+were\s+dead)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(wish\s+i\s+had\s+never\s+been\s+born|wish\s+i\s+didn['o]t\s+exist)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(not\s+wanting\s+to\s+exist|don['o]t\s+want\s+to\s+exist)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(sometimes\s+i\s+think\s+about\s+(suicide|ending\s+my\s+life)|thinking\s+about\s+ending\s+my\s+life)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(thinking\s+about\s+suicide|thoughts\s+of\s+suicide|suicidal\s+thoughts?)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(want\s+to\s+hurt\s+myself|going\s+to\s+hurt\s+myself)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(want\s+to\s+die|feel\s+like\s+dying|tired\s+of\s+living)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bwish\s+i\s+could\s+disappear\s+forever\b""", RegexOption.IGNORE_CASE),
        Regex("""\bbetter\s+off\s+dead\b""", RegexOption.IGNORE_CASE)
    )

    // --- 7. Level 2: Significant Distress & Clinical Warning Patterns ---
    private val burdensomenessPatterns = listOf(
        Regex("""\beveryone\s+would\s+be\s+better\s+(off\s+)?without\s+me\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(i['a]m\s+just\s+a\s+burden|i\s+am\s+a\s+burden)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bthey\s+would\s+be\s+happier\s+if\s+i\s+was\s+gone\b""", RegexOption.IGNORE_CASE),
        Regex("""\bi\s+drain\s+everyone\s+around\s+me\b""", RegexOption.IGNORE_CASE)
    )

    private val hopelessnessPatterns = listOf(
        Regex("""\bnothing\s+(in\s+my\s+life\s+)?is\s+getting\s+better\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(nothing\s+will\s+ever\s+change|nothing\s+will\s+ever\s+improve)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(completely\s+hopeless|no\s+hope\s+left|no\s+way\s+out)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(no\s+reason\s+to\s+live|cant\s+see\s+any\s+future|no\s+future)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(i\s+am\s+worthless|feeling\s+worthless|worthless\s+person|utterly\s+worthless)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(lost\s+all\s+purpose|no\s+purpose\s+in\s+life|feel\s+completely\s+numb)\b""", RegexOption.IGNORE_CASE)
    )

    // Separate Non-Suicidal Self-Injury (NSSI) Assessment
    private val selfHarmWithoutSuicidePatterns = listOf(
        Regex("""\b(want|feel\s+like)\s+to\s+hurt\s+myself\s+but\s+(not\s+die|don['o]t\s+want\s+to\s+die|do\s+not\s+want\s+to\s+die)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bhurt\s+myself\s+(but\s+not\s+die|without\s+dying)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bcut\s+myself\s+but\s+not\s+suicidal\b""", RegexOption.IGNORE_CASE)
    )

    // --- 8. Level 1: General Emotional Distress Patterns ---
    private val distressPatterns = listOf(
        Regex("""\b(stressful\s+day|stressed\s+out|so\s+stressed|stress\s+at\s+work)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(feel\s+lonely|so\s+lonely|loneliness|all\s+alone)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(feel\s+sad|feeling\s+sad|so\s+sad|heartbroken|crying\s+all\s+day)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(feeling\s+overwhelmed|overwhelmed\s+by|too\s+much\s+to\s+handle)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(emotionally\s+exhausted|exhausted\s+today|drained|burnout)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(frustrated\s+with|feeling\s+down|rough\s+day|bad\s+day)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(anxious|nervous|anxiety|having\s+anxiety)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(cannot\s+concentrate|can't\s+focus|unable\s+to\s+concentrate)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(don't\s+know\s+what\s+i\s+feel|confused\s+about\s+my\s+feelings)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(just\s+want\s+someone\s+to\s+listen|need\s+to\s+vent)\b""", RegexOption.IGNORE_CASE)
    )

    /**
     * Extract structured risk signals from a user message with full conversational awareness.
     */
    fun extractSignals(
        message: String,
        history: List<Pair<String, String>> = emptyList(),
        currentState: RiskState? = null
    ): RiskSignals {
        val trimmed = message.trim()
        val lower = trimmed.lowercase(Locale.ROOT)
        val evidenceList = mutableListOf<String>()

        // 0. Keyword Detection (Highest Priority Interception Layer)
        val keywordResult = KeywordDetector.detect(trimmed)
        if (keywordResult.requiresInterception) {
            evidenceList.add("Safety keyword interception triggered: [${keywordResult.matchedKeywords.joinToString(", ")}]")
        }

        // 1. Check Figurative Idioms (e.g. "I'm dying of laughter", "work is killing me")
        val isFigurative = figurativePatterns.any { it.containsMatchIn(trimmed) }
        if (isFigurative) {
            evidenceList.add("Figurative expression detected (non-suicidal idiom)")
            return RiskSignals(
                hasKeywordTrigger = keywordResult.requiresInterception,
                matchedKeywords = keywordResult.matchedKeywords,
                isFigurative = true,
                evidence = evidenceList
            )
        }

        // 2. Check Fictional or Script Contexts (e.g. "My fictional character died", "writing a story")
        val isFictional = fictionalPatterns.any { it.containsMatchIn(trimmed) }
        if (isFictional) {
            evidenceList.add("Fictional/character context detected")
            return RiskSignals(
                hasKeywordTrigger = keywordResult.requiresInterception,
                matchedKeywords = keywordResult.matchedKeywords,
                isFictionalOrHypothetical = true,
                evidence = evidenceList
            )
        }

        // 3. Check Third-party or Historical Statements
        val isThirdParty = thirdPartyPatterns.any { it.containsMatchIn(trimmed) }
        val isHistorical = historicalPatterns.any { it.containsMatchIn(trimmed) }
        if (isThirdParty) {
            evidenceList.add("Third-party distress statement")
        }
        if (isHistorical) {
            evidenceList.add("Historical past distress statement")
        }

        // 4. Check Negation (e.g. "I don't want to kill myself")
        val isNegated = negationPatterns.any { it.containsMatchIn(trimmed) }
        if (isNegated) {
            evidenceList.add("Negation detected: user explicitly denies self-harm or suicide")
        }

        // 5. Analyze Safety Follow-up Confirmation
        val confirmationType = SafetyConversationManager.analyzeSafetyConfirmation(trimmed, currentState ?: RiskState())
        if (confirmationType != null) {
            evidenceList.add("Safety follow-up signal detected: $confirmationType")
        }

        // 5. Multi-turn Screening Question Evaluation
        val lastAssistantMessage = history.lastOrNull { 
            it.first.equals("model", ignoreCase = true) || it.first.equals("assistant", ignoreCase = true) 
        }?.second

        val wasSafetyQuestionAsked = (currentState?.awaitingScreeningAnswer == true) || (
            lastAssistantMessage != null && (
                lastAssistantMessage.contains("thoughts about hurting yourself", ignoreCase = true) ||
                lastAssistantMessage.contains("thoughts of ending your life", ignoreCase = true) ||
                lastAssistantMessage.contains("safe right now", ignoreCase = true) ||
                lastAssistantMessage.contains("ending your life at this moment", ignoreCase = true) ||
                lastAssistantMessage.contains("hurting yourself or ending your life", ignoreCase = true)
            )
        )

        val safeOrDenialPhrases = listOf(
            Regex("""\b(i\s*['a]?\s*m\s+(good|fine|okay|ok|doing\s+good|doing\s+fine|doing\s+well|safe|better|alright))\b""", RegexOption.IGNORE_CASE),
            Regex("""\b(i\s+am\s+(good|fine|okay|ok|doing\s+good|doing\s+fine|doing\s+well|safe|better|alright))\b""", RegexOption.IGNORE_CASE),
            Regex("""\b(im\s+(good|fine|okay|ok|safe|better|alright))\b""", RegexOption.IGNORE_CASE),
            Regex("""\b(feel\s+(good|fine|okay|ok|better|alright))\b""", RegexOption.IGNORE_CASE),
            Regex("""\b(doing\s+(good|fine|well|okay|ok))\b""", RegexOption.IGNORE_CASE),
            Regex("""\b(all\s+good|all\s+fine|all\s+okay)\b""", RegexOption.IGNORE_CASE),
            Regex("""\b(no|nope|nah|never|not\s+really|not\s+at\s+all|no\s+way|nothing\s+like\s+that)\b""", RegexOption.IGNORE_CASE),
            Regex("""\b(no\s+i\s+do\s*n['o]t|i\s+do\s*n['o]t|no\s+thoughts?|never\s+had\s+thoughts?)\b""", RegexOption.IGNORE_CASE),
            Regex("""\b(no\s+never|definitely\s+not|absolutely\s+not)\b""", RegexOption.IGNORE_CASE),
            Regex("""^(good|fine|okay|ok|all good|doing good|doing well|doing fine)$""", RegexOption.IGNORE_CASE)
        )

        var confirmedViaScreening = false
        var deniedViaScreening = false

        if (wasSafetyQuestionAsked) {
            val cleanLower = lower.replace("""[.,!?;:]""".toRegex(), " ").trim()
            val tokens = cleanLower.split("\\s+".toRegex()).filter { it.isNotBlank() }

            val hasAffirmative = tokens.contains("yes") || tokens.contains("yeah") || tokens.contains("yep") ||
                cleanLower.contains("i have") || cleanLower.contains("i do") || cleanLower.contains("honestly yes") ||
                cleanLower.contains("all the time") || cleanLower.contains("frequently") ||
                cleanLower.contains("sometimes i do")
            val hasNegative = !hasAffirmative && (
                tokens.contains("no") || tokens.contains("nope") || tokens.contains("never") ||
                tokens.contains("good") || tokens.contains("fine") || tokens.contains("okay") || tokens.contains("ok") || tokens.contains("safe") ||
                safeOrDenialPhrases.any { it.containsMatchIn(trimmed) }
            )

            if (hasAffirmative) {
                confirmedViaScreening = true
                evidenceList.add("Affirmative answer to previous safety screening question")
            } else if (hasNegative) {
                deniedViaScreening = true
                evidenceList.add("Negative/safe answer to previous safety screening question ('$trimmed')")
            }
        }

        // 6. Level 4 Critical Matches
        val hasImmediateIntent = criticalImmediateIntent.any { pattern ->
            val m = pattern.find(trimmed)
            if (m != null) evidenceList.add("Immediate intent: '${m.value}'")
            m != null
        }

        val hasRecentAttempt = criticalRecentAttempt.any { pattern ->
            val m = pattern.find(trimmed)
            if (m != null) evidenceList.add("Recent attempt/harm: '${m.value}'")
            m != null
        }

        // 7. Level 3 Suicidal Ideation Matches
        val hasIdeation = (suicidalIdeationPatterns.any { pattern ->
            val m = pattern.find(trimmed)
            if (m != null) evidenceList.add("Ideation pattern: '${m.value}'")
            m != null
        } || confirmedViaScreening) && !isHistorical

        val hasPassiveDeathWish = (trimmed.contains("sleep and never wake up", ignoreCase = true) ||
            trimmed.contains("never woke up", ignoreCase = true) ||
            trimmed.contains("not wanting to exist", ignoreCase = true) ||
            trimmed.contains("wish i was dead", ignoreCase = true) ||
            trimmed.contains("wish i didn't exist", ignoreCase = true)) && !isHistorical

        // 8. Level 2 Significant Psychological Distress Matches
        val hasBurdensomeness = burdensomenessPatterns.any { pattern ->
            val m = pattern.find(trimmed)
            if (m != null) evidenceList.add("Burdensomeness: '${m.value}'")
            m != null
        }

        val hasHopelessness = hopelessnessPatterns.any { pattern ->
            val m = pattern.find(trimmed)
            if (m != null) evidenceList.add("Hopelessness: '${m.value}'")
            m != null
        }

        val hasSelfHarmOnly = selfHarmWithoutSuicidePatterns.any { pattern ->
            val m = pattern.find(trimmed)
            if (m != null) evidenceList.add("Self-harm statement: '${m.value}'")
            m != null
        }

        // 9. Level 1 General Distress Matches
        val hasDistress = distressPatterns.any { pattern ->
            val m = pattern.find(trimmed)
            if (m != null) evidenceList.add("Emotional distress: '${m.value}'")
            m != null
        }

        // 10. Ambiguous statements requiring clarification
        val isAmbiguous = (lower == "i wish i could disappear" ||
            lower == "i just want it all to stop" ||
            lower == "i can't do this anymore" ||
            lower == "i'm done") && !hasImmediateIntent && !hasIdeation

        return RiskSignals(
            emotionalDistress = hasDistress,
            sadness = lower.contains("sad") || lower.contains("cry") || lower.contains("heartbroken"),
            loneliness = lower.contains("lone") || lower.contains("alone"),
            stress = lower.contains("stress"),
            emotionalExhaustion = lower.contains("exhaust") || lower.contains("drain"),
            frustration = lower.contains("frustrat") || lower.contains("bad day") || lower.contains("rough day"),
            temporaryHopelessness = lower.contains("feel hopeless today"),

            persistentHopelessness = hasHopelessness,
            worthlessness = lower.contains("worthless"),
            perceivedBurdensomeness = hasBurdensomeness,
            persistentDespair = hasHopelessness,
            futurelessness = lower.contains("no future") || lower.contains("no reason to live") || lower.contains("can't see any future"),
            lossOfPurpose = lower.contains("lost all purpose") || lower.contains("no purpose"),
            persistentEmotionalNumbness = lower.contains("feel completely numb"),
            beliefNothingWillImprove = hasHopelessness,

            passiveDeathWish = hasPassiveDeathWish && !isNegated,
            activeSuicidalThoughts = hasIdeation && !isNegated && !hasSelfHarmOnly,
            recurrentDeathThoughts = lower.contains("thinking about death") && !isNegated,
            desireToDisappear = lower.contains("disappear forever") && !isNegated,
            notWantingToExist = (lower.contains("don't want to exist") || lower.contains("not wanting to exist")) && !isNegated,

            currentSuicidalIntent = hasImmediateIntent && !isNegated,
            specificPlan = hasImmediateIntent && (lower.contains("tonight") || lower.contains("today") || lower.contains("pills") || lower.contains("rope")),
            accessToMeans = lower.contains("pills") || lower.contains("rope") || lower.contains("ledge"),
            imminentPlan = hasImmediateIntent && (lower.contains("tonight") || lower.contains("now") || lower.contains("today")),
            preparatoryBehaviour = lower.contains("suicide note") || lower.contains("gave away all my"),
            recentAttempt = hasRecentAttempt,
            seriousCurrentSelfHarm = (lower.contains("bleeding heavily") || lower.contains("swallowed all the pills")) && !hasSelfHarmOnly,
            selfHarmOnly = hasSelfHarmOnly,
            hasKeywordTrigger = keywordResult.requiresInterception,
            matchedKeywords = keywordResult.matchedKeywords,
            confirmationType = confirmationType,

            negated = isNegated || deniedViaScreening,
            ambiguous = isAmbiguous,
            needsClarification = (isAmbiguous || (hasBurdensomeness && !hasIdeation)) && !deniedViaScreening,
            isFigurative = false,
            isFictionalOrHypothetical = isFictional || isThirdParty || isHistorical,
            isQuotedOrThirdParty = isThirdParty,
            isSarcastic = false,

            confidence = 1.0f,
            evidence = evidenceList
        )
    }
}
