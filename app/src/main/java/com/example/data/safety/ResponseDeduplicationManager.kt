package com.example.data.safety

import java.util.concurrent.ConcurrentHashMap

/**
 * Manages response variation and deduplication across conversation turns.
 * Ensures the system never sends identical responses consecutively, does not repeat
 * answered screening questions, and rotates phrasing naturally.
 */
object ResponseDeduplicationManager {

    private val lastDeliveredResponsePerSession = ConcurrentHashMap<String, String>()
    private val deliveredHashesPerSession = ConcurrentHashMap<String, MutableSet<String>>()
    private val askedQuestionsPerSession = ConcurrentHashMap<String, MutableSet<String>>()

    /**
     * Checks if a proposed text was sent in the immediate preceding turn.
     */
    fun isConsecutiveDuplicate(conversationId: String, proposedText: String): Boolean {
        val last = lastDeliveredResponsePerSession[conversationId] ?: return false
        return normalizeForComparison(last) == normalizeForComparison(proposedText)
    }

    /**
     * Checks if a specific safety or screening question has already been asked and answered.
     */
    fun hasQuestionBeenAnswered(conversationId: String, questionKey: String, state: RiskState): Boolean {
        if (state.answeredQuestions.contains(questionKey)) return true
        val sessionQuestions = askedQuestionsPerSession[conversationId]
        return sessionQuestions?.contains(questionKey) == true
    }

    /**
     * Selects an unrepeated variant from a list of options.
     */
    fun selectVariant(
        conversationId: String,
        options: List<String>,
        state: RiskState? = null
    ): String {
        if (options.isEmpty()) return ""
        if (options.size == 1) return options[0]

        val delivered = (state?.deliveredResponseHashes ?: emptyList()).toSet() +
            (deliveredHashesPerSession[conversationId] ?: emptySet())
        val lastDelivered = lastDeliveredResponsePerSession[conversationId]

        // 1. Try to find an option not yet delivered in this conversation
        val unused = options.filter { opt ->
            val hash = hashText(opt)
            !delivered.contains(hash) && (lastDelivered == null || normalizeForComparison(lastDelivered) != normalizeForComparison(opt))
        }

        if (unused.isNotEmpty()) {
            return unused.first()
        }

        // 2. If all have been used, pick the one least recently used that differs from the immediate last message
        val nonConsecutive = options.filter { opt ->
            lastDelivered == null || normalizeForComparison(lastDelivered) != normalizeForComparison(opt)
        }

        return nonConsecutive.firstOrNull() ?: options.first()
    }

    /**
     * Records a delivered response for deduplication tracking.
     */
    fun recordDelivered(
        conversationId: String,
        deliveredText: String,
        questionKey: String? = null
    ): Pair<String, String?> {
        val hash = hashText(deliveredText)
        lastDeliveredResponsePerSession[conversationId] = deliveredText

        val hashes = deliveredHashesPerSession.getOrPut(conversationId) { mutableSetOf() }
        hashes.add(hash)

        if (questionKey != null) {
            val questions = askedQuestionsPerSession.getOrPut(conversationId) { mutableSetOf() }
            questions.add(questionKey)
        }

        return hash to questionKey
    }

    fun clearSession(conversationId: String) {
        lastDeliveredResponsePerSession.remove(conversationId)
        deliveredHashesPerSession.remove(conversationId)
        askedQuestionsPerSession.remove(conversationId)
    }

    private fun normalizeForComparison(text: String): String {
        return text.trim().lowercase().replace("""\s+""".toRegex(), " ")
    }

    fun hashText(text: String): String {
        return text.trim().hashCode().toString()
    }
}

