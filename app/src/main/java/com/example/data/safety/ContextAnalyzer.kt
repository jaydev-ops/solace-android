package com.example.data.safety

import java.util.Locale

object ContextAnalyzer {

    // --- 1. Figurative Idioms & Common Expressions (Non-suicidal) ---
    private val figurativePatterns = listOf(
        Regex("""\bdying\s+of\s+laughter\b""", RegexOption.IGNORE_CASE),
        Regex("""\bdying\s+to\s+(see|know|hear|go|meet|try|find|watch)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bdying\s+for\s+(a\s+)?(coffee|tea|drink|food|nap|rest|break)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(homework|work|job|boss|exam|test|traffic|heat|cold|day)\s+is\s+killing\s+me\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(workout|run|gym|training)\s+killed\s+me\b""", RegexOption.IGNORE_CASE),
        Regex("""\bkilled\s+it\s+(at|today|yesterday|on)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bstarved?\s+to\s+death\b""", RegexOption.IGNORE_CASE),
        Regex("""\bbored\s+to\s+death\b""", RegexOption.IGNORE_CASE),
        Regex("""\bscared\s+to\s+death\b""", RegexOption.IGNORE_CASE),
        Regex("""\bkill\s+two\s+birds\b""", RegexOption.IGNORE_CASE)
    )

    // --- 2. Fictional, Quoted, or Third-Party Contexts ---
    private val fictionalPatterns = listOf(
        Regex("""\b(my\s+)?(fictional\s+character|character\s+in|story|novel|book|script|plot)\b.*?\b(wants?\s+to\s+die|dies?|suicide)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bin\s+the\s+(movie|show|series|anime|game|book)\b.*?\b(character|someone|he|she)\b.*?\b(suicide|died|killed)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bwriting\s+a\s+(story|scene|script)\s+about\b""", RegexOption.IGNORE_CASE)
    )

    // --- 3. Negation Patterns ---
    private val negationPatterns = listOf(
        Regex("""\b(do\s*n['o]t|dont|did\s*n['o]t|will\s*n['o]t|wont|am\s+not|never)\s+(want|intend|planning?|thinking?)\s+to\s+(kill\s+myself|die|end\s+my\s+life|hurt\s+myself|commit\s+suicide)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(not|never)\s+(suicidal|thinking\s+about\s+suicide|going\s+to\s+kill\s+myself|going\s+to\s+hurt\s+myself)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bwould\s+never\s+(hurt\s+myself|kill\s+myself|commit\s+suicide|end\s+my\s+life)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bno\s+intention\s+of\s+(dying|killing\s+myself|suicide)\b""", RegexOption.IGNORE_CASE)
    )

    // --- 4. Level 4: Immediate Danger / Emergency Patterns ---
    private val criticalImmediateIntent = listOf(
        Regex("""\b(planning|decided|going)\s+to\s+end\s+my\s+life\s+(tonight|today|now|soon)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(have\s+decided|i\s+am\s+going)\s+to\s+kill\s+myself\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(goodbye\s+everyone|goodbye\s+world|this\s+is\s+my\s+last\s+day)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(have\s+the\s+pills\s+ready|have\s+the\s+rope|about\s+to\s+jump|standing\s+on\s+the\s+ledge)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(wrote|written)\s+(my\s+)?suicide\s+note\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(giving|gave)\s+away\s+(all\s+my\s+things|my\s+belongings|my\s+possessions)\b""", RegexOption.IGNORE_CASE)
    )

    private val criticalRecentAttempt = listOf(
        Regex("""\b(already|just)\s+attempted\s+suicide\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(already|just)\s+(swallowed|took)\s+(all\s+the|a\s+bottle\s+of|excessive)\s+(pills|medication|tablets)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(already\s+tried|attempted)\s+to\s+(kill\s+myself|end\s+my\s+life)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bbleeding\s+(heavily|a\s+lot)\s+from\s+(cutting|hurting\s+myself)\b""", RegexOption.IGNORE_CASE)
    )

    // --- 5. Level 3: Suicidal Ideation Patterns ---
    private val suicidalIdeationPatterns = listOf(
        Regex("""\b(wish\s+i\s+could\s+go\s+to\s+sleep\s+and\s+never\s+wake\s+up|sleep\s+and\s+never\s+wake\s+up)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(wish\s+i\s+was\s+dead|wish\s+i\s+were\s+dead)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(wish\s+i\s+had\s+never\s+been\s+born|wish\s+i\s+didn['o]t\s+exist)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(not\s+wanting\s+to\s+exist|don['o]t\s+want\s+to\s+exist)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(sometimes\s+i\s+think\s+about\s+ending\s+my\s+life|thinking\s+about\s+ending\s+my\s+life)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(thinking\s+about\s+suicide|thoughts\s+of\s+suicide|suicidal\s+thoughts?)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(want\s+to\s+hurt\s+myself|going\s+to\s+hurt\s+myself)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(want\s+to\s+die|feel\s+like\s+dying|tired\s+of\s+living)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bwish\s+i\s+could\s+disappear\s+forever\b""", RegexOption.IGNORE_CASE),
        Regex("""\bbetter\s+off\s+dead\b""", RegexOption.IGNORE_CASE)
    )

    // --- 6. Level 2: Significant Distress & Warning Patterns ---
    private val burdensomenessPatterns = listOf(
        Regex("""\beveryone\s+would\s+be\s+better\s+(off\s+)?without\s+me\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(i['a]m\s+just\s+a\s+burden|i\s+am\s+a\s+burden)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bthey\s+would\s+be\s+happier\s+if\s+i\s+was\s+gone\b""", RegexOption.IGNORE_CASE)
    )

    private val hopelessnessPatterns = listOf(
        Regex("""\bnothing\s+(in\s+my\s+life\s+)?is\s+getting\s+better\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(nothing\s+will\s+ever\s+change|nothing\s+will\s+ever\s+improve)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(completely\s+hopeless|no\s+hope\s+left|no\s+way\s+out)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(no\s+reason\s+to\s+live|cant\s+see\s+any\s+future|no\s+future)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(i\s+am\s+worthless|feeling\s+worthless|worthless\s+person)\b""", RegexOption.IGNORE_CASE)
    )

    private val selfHarmWithoutSuicidePatterns = listOf(
        Regex("""\b(want|feel\s+like)\s+to\s+hurt\s+myself\s+but\s+(don['o]t|do\s+not)\s+want\s+to\s+die\b""", RegexOption.IGNORE_CASE),
        Regex("""\bhurt\s+myself\s+without\s+dying\b""", RegexOption.IGNORE_CASE)
    )

    // --- 7. Level 1: General Emotional Distress Patterns ---
    private val distressPatterns = listOf(
        Regex("""\b(stressful\s+day|stressed\s+out|so\s+stressed)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(feel\s+lonely|so\s+lonely|loneliness)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(feel\s+sad|feeling\s+sad|so\s+sad|heartbroken)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(feeling\s+overwhelmed|overwhelmed\s+by|too\s+much\s+to\s+handle)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(emotionally\s+exhausted|exhausted\s+today|drained)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(frustrated\s+with|feeling\s+down|rough\s+day)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(anxious|nervous|anxiety)\b""", RegexOption.IGNORE_CASE)
    )

    /**
     * Analyze user message and conversation history to extract structured risk signals.
     */
    fun extractSignals(
        message: String,
        history: List<Pair<String, String>> = emptyList(),
        currentState: RiskState? = null
    ): RiskSignals {
        val trimmed = message.trim()
        val lower = trimmed.lowercase(Locale.ROOT)
        val evidenceList = mutableListOf<String>()

        // 1. Check Figurative Idioms
        val isFigurative = figurativePatterns.any { it.containsMatchIn(trimmed) }
        if (isFigurative) {
            evidenceList.add("Figurative expression detected")
            return RiskSignals(
                isFigurative = true,
                evidence = evidenceList
            )
        }

        // 2. Check Fictional or Third-Party Contexts
        val isFictional = fictionalPatterns.any { it.containsMatchIn(trimmed) }
        if (isFictional) {
            evidenceList.add("Fictional/third-party context detected")
            return RiskSignals(
                isFictionalOrHypothetical = true,
                evidence = evidenceList
            )
        }

        // 3. Check Negation
        val isNegated = negationPatterns.any { it.containsMatchIn(trimmed) }
        if (isNegated) {
            evidenceList.add("Negation detected: User denies self-harm or suicide")
        }

        // 4. Screening Question Follow-up Context
        // If the AI previously asked a safety screening question:
        val lastAssistantMessage = history.lastOrNull { it.first.equals("model", ignoreCase = true) || it.first.equals("assistant", ignoreCase = true) }?.second
        val wasSafetyQuestionAsked = lastAssistantMessage != null && (
            lastAssistantMessage.contains("thoughts about hurting yourself", ignoreCase = true) ||
            lastAssistantMessage.contains("thoughts of ending your life", ignoreCase = true) ||
            lastAssistantMessage.contains("safe right now", ignoreCase = true)
        )

        var confirmedViaScreening = false
        var deniedViaScreening = false

        if (wasSafetyQuestionAsked) {
            val cleanLower = lower.replace("""[.,!?;:]""".toRegex(), " ").trim()
            val tokens = cleanLower.split("\\s+".toRegex()).filter { it.isNotBlank() }

            val hasAffirmative = tokens.contains("yes") || tokens.contains("yeah") || tokens.contains("yep") ||
                cleanLower.contains("i have") || cleanLower.contains("i do") || cleanLower.contains("honestly yes") ||
                cleanLower.contains("all the time")
            val hasNegative = (tokens.contains("no") || tokens.contains("nope") || cleanLower.contains("not at all") ||
                tokens.contains("never") || cleanLower.contains("i don't") || cleanLower.contains("no never")) && !hasAffirmative

            if (hasAffirmative) {
                confirmedViaScreening = true
                evidenceList.add("Affirmative answer to previous safety screening question")
            } else if (hasNegative) {
                deniedViaScreening = true
                evidenceList.add("Negative answer to previous safety screening question")
            }
        }

        // 5. Level 4 Critical Matches
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

        // 6. Level 3 Suicidal Ideation Matches
        val hasIdeation = suicidalIdeationPatterns.any { pattern ->
            val m = pattern.find(trimmed)
            if (m != null) evidenceList.add("Ideation pattern: '${m.value}'")
            m != null
        } || confirmedViaScreening

        val hasPassiveDeathWish = trimmed.contains("sleep and never wake up", ignoreCase = true) ||
            trimmed.contains("not wanting to exist", ignoreCase = true) ||
            trimmed.contains("wish i was dead", ignoreCase = true) ||
            trimmed.contains("wish i didn't exist", ignoreCase = true)

        // 7. Level 2 Significant Psychological Distress Matches
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

        // 8. Level 1 General Distress Matches
        val hasDistress = distressPatterns.any { pattern ->
            val m = pattern.find(trimmed)
            if (m != null) evidenceList.add("Emotional distress: '${m.value}'")
            m != null
        }

        // 9. Ambiguous statements that require clarification
        val isAmbiguous = (lower == "i wish i could disappear" ||
            lower == "i just want it all to stop" ||
            lower == "i can't do this anymore") && !hasImmediateIntent && !hasIdeation

        return RiskSignals(
            emotionalDistress = hasDistress,
            sadness = lower.contains("sad") || lower.contains("cry"),
            loneliness = lower.contains("lone"),
            stress = lower.contains("stress"),
            emotionalExhaustion = lower.contains("exhaust") || lower.contains("drain"),

            persistentHopelessness = hasHopelessness,
            worthlessness = lower.contains("worthless"),
            perceivedBurdensomeness = hasBurdensomeness,
            persistentDespair = hasHopelessness,
            futurelessness = lower.contains("no future") || lower.contains("no reason to live"),

            passiveDeathWish = hasPassiveDeathWish && !isNegated,
            activeSuicidalThoughts = hasIdeation && !isNegated,
            recurrentDeathThoughts = lower.contains("thinking about death"),
            desireToDisappear = lower.contains("disappear"),

            currentSuicidalIntent = hasImmediateIntent && !isNegated,
            specificPlan = hasImmediateIntent && (lower.contains("tonight") || lower.contains("today") || lower.contains("pills") || lower.contains("rope")),
            accessToMeans = lower.contains("pills") || lower.contains("rope") || lower.contains("ledge"),
            imminentPlan = hasImmediateIntent && (lower.contains("tonight") || lower.contains("now")),
            preparatoryBehaviour = lower.contains("suicide note") || lower.contains("gave away"),
            recentAttempt = hasRecentAttempt,
            seriousCurrentSelfHarm = hasSelfHarmOnly || lower.contains("bleeding"),

            negated = isNegated || deniedViaScreening,
            ambiguous = isAmbiguous,
            needsClarification = isAmbiguous || (hasBurdensomeness && !hasIdeation),
            isFigurative = false,
            isFictionalOrHypothetical = false,

            confidence = 1.0f,
            evidence = evidenceList
        )
    }
}
