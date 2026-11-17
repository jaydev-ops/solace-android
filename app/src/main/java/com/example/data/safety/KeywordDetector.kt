package com.example.data.safety

import java.util.Locale

/**
 * Dedicated Keyword Detection Engine for Solace+.
 * Monitored on every incoming user message to detect mental health crisis language
 * before further contextual interpretation and deterministic evaluation.
 */
object KeywordDetector {

    data class KeywordDetectionResult(
        val hasSuicideKeywords: Boolean,
        val hasSelfHarmKeywords: Boolean,
        val hasSevereDistressKeywords: Boolean,
        val requiresInterception: Boolean,
        val matchedKeywords: List<String>
    )

    // A. Suicide-related phrases and keywords (including variations and contractions)
    private val suicideKeywords = listOf(
        "suicide",
        "suicidal",
        "su1cide",
        "suicde",
        "kill myself",
        "killing myself",
        "end my life",
        "ending my life",
        "ended my life",
        "take my life",
        "taking my life",
        "end everything",
        "ending everything",
        "don't want to live",
        "dont want to live",
        "do not want to live",
        "not wanting to live",
        "don't want to exist",
        "dont want to exist",
        "do not want to exist",
        "not wanting to exist",
        "wish i was dead",
        "wish i were dead",
        "wish i had never been born",
        "rather be dead",
        "death",
        "dead",
        "die",
        "dying",
        "disappear forever",
        "disappearing forever",
        "never wake up",
        "not wake up",
        "better off without me",
        "better off dead",
        "life is not worth living",
        "life isn't worth living",
        "end it all",
        "ending it all",
        "kms",
        "want to die",
        "ready to die"
    )

    // B. Self-harm-related phrases and keywords
    private val selfHarmKeywords = listOf(
        "hurt myself",
        "hurting myself",
        "harm myself",
        "harming myself",
        "cut myself",
        "cutting myself",
        "self-harm",
        "self harm",
        "selfharm",
        "self-injury",
        "self injury",
        "overdose",
        "overdosed",
        "overdosing",
        "poison myself",
        "poisoning myself",
        "punish myself",
        "punishing myself",
        "burn myself",
        "burning myself"
    )

    // C. Severe emotional distress keywords
    private val severeDistressKeywords = listOf(
        "hopeless",
        "hopelessness",
        "worthless",
        "worthlessness",
        "useless",
        "burden",
        "nobody needs me",
        "no one needs me",
        "no reason to live",
        "no reason for living",
        "nothing matters",
        "nothing matters anymore",
        "cannot go on",
        "can't go on",
        "cant go on",
        "can't take it anymore",
        "cannot take it anymore",
        "cant take it anymore",
        "no future",
        "exhausted with life",
        "tired of living",
        "what's the point of living",
        "can't do this anymore",
        "cant do this anymore"
    )

    // Regex boundaries to avoid false substring matching on partial words (e.g. "undead", "diet")
    private fun compileRegex(keywords: List<String>): List<Pair<String, Regex>> {
        return keywords.map { kw ->
            val escaped = Regex.escape(kw)
            kw to Regex("""\b$escaped\b""", RegexOption.IGNORE_CASE)
        }
    }

    private val suicidePatterns = compileRegex(suicideKeywords)
    private val selfHarmPatterns = compileRegex(selfHarmKeywords)
    private val severeDistressPatterns = compileRegex(severeDistressKeywords)

    /**
     * Inspects the user's message for safety-critical keywords.
     */
    fun detect(text: String): KeywordDetectionResult {
        if (text.isBlank()) {
            return KeywordDetectionResult(
                hasSuicideKeywords = false,
                hasSelfHarmKeywords = false,
                hasSevereDistressKeywords = false,
                requiresInterception = false,
                matchedKeywords = emptyList()
            )
        }

        val normalized = text.lowercase(Locale.ROOT)
        val matched = mutableListOf<String>()

        var hasSuicide = false
        for ((kw, regex) in suicidePatterns) {
            if (regex.containsMatchIn(normalized)) {
                hasSuicide = true
                matched.add(kw)
            }
        }

        var hasSelfHarm = false
        for ((kw, regex) in selfHarmPatterns) {
            if (regex.containsMatchIn(normalized)) {
                hasSelfHarm = true
                matched.add(kw)
            }
        }

        var hasDistress = false
        for ((kw, regex) in severeDistressPatterns) {
            if (regex.containsMatchIn(normalized)) {
                hasDistress = true
                matched.add(kw)
            }
        }

        val requiresInterception = hasSuicide || hasSelfHarm

        return KeywordDetectionResult(
            hasSuicideKeywords = hasSuicide,
            hasSelfHarmKeywords = hasSelfHarm,
            hasSevereDistressKeywords = hasDistress,
            requiresInterception = requiresInterception,
            matchedKeywords = matched.distinct()
        )
    }
}

