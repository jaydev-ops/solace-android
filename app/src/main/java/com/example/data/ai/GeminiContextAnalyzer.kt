package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import com.example.data.safety.ContextAnalyzer
import com.example.data.safety.GeminiContextAnalysis
import com.example.data.safety.RiskLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val TAG = "GeminiContextAnalyzer"

/**
 * Gemini Contextual NLU Engine for Solace+.
 * Queries Gemini for deep intent and emotion extraction returning structured JSON.
 *
 * CRITICAL SAFETY BOUNDARY:
 * Gemini's contextual output is advisory only for conversational tone and mode.
 * It NEVER independently determines or overrides mental health risk or safety escalation.
 */
class GeminiContextAnalyzer(
    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()
) {

    /**
     * Analyze context returning structured GeminiContextAnalysis.
     * Falls back seamlessly to deterministic rule-based analysis if offline or if API returns invalid JSON.
     */
    suspend fun analyze(
        userMessage: String,
        history: List<Pair<String, String>> = emptyList(),
        customApiKey: String? = null
    ): GeminiContextAnalysis = withContext(Dispatchers.IO) {
        // Purely deterministic offline rule-based analysis (No Gemini API calls)
        fallbackDeterministicAnalysis(userMessage, history)
    }

    private fun resolveApiKey(customKey: String?): String {
        if (!customKey.isNullOrBlank()) return customKey.trim()
        return try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            val key = field.get(null) as? String ?: ""
            if (key != "MY_GEMINI_API_KEY") key.trim() else ""
        } catch (_: Exception) {
            ""
        }
    }

    private fun callGeminiForJson(
        apiKey: String,
        userMessage: String,
        history: List<Pair<String, String>>
    ): String {
        val systemInstruction = """
            You are the Natural Language Understanding component for Solace+, an emotional wellness sanctuary.
            Analyze the user's message and history for emotional intent and conversational context.
            You MUST reply with ONLY a single valid JSON object with the following schema:
            {
              "intent": "emotional_support | venting | seeking_advice | reflection | grounding | casual",
              "emotion": "loneliness | anxiety | overwhelm | sadness | exhaustion | anger | joy | neutral",
              "responseMode": "LISTEN | UNDERSTAND | CALM | REFLECT | REFOCUS | GRATITUDE",
              "suggestedAction": "explore_feelings | offer_grounding | reflect_wisdom | listening_space",
              "riskIndicators": ["string"],
              "requiresClarification": boolean,
              "contextSummary": "Brief 1-sentence summary",
              "isLiteral": boolean,
              "isFigurative": boolean,
              "isFictionalOrHypothetical": boolean
            }
            Do not include Markdown backticks (e.g. ```json). Do not add any text before or after the JSON.
        """.trimIndent()

        val contentsArray = JSONArray()
        for ((role, text) in history.takeLast(4)) {
            val part = JSONObject().put("text", text)
            contentsArray.put(
                JSONObject()
                    .put("role", if (role.equals("user", ignoreCase = true)) "user" else "model")
                    .put("parts", JSONArray().put(part))
            )
        }

        contentsArray.put(
            JSONObject()
                .put("role", "user")
                .put("parts", JSONArray().put(JSONObject().put("text", userMessage)))
        )

        val root = JSONObject().apply {
            put("contents", contentsArray)
            put("systemInstruction", JSONObject().put("parts", JSONArray().put(JSONObject().put("text", systemInstruction))))
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.1) // Low temperature for deterministic JSON output
                put("maxOutputTokens", 300)
                put("responseMimeType", "application/json")
            })
        }

        val requestBody = root.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

        val request = Request.Builder().url(url).post(requestBody).build()
        httpClient.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val body = response.body?.string() ?: ""
                val json = JSONObject(body)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return parts.getJSONObject(0).optString("text")
                    }
                }
            }
        }
        throw IllegalStateException("Failed to retrieve valid JSON from Gemini")
    }

    private fun parseAndValidate(rawJson: String): GeminiContextAnalysis? {
        return try {
            val clean = rawJson.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            val obj = JSONObject(clean)

            val riskIndicators = mutableListOf<String>()
            val riskArr = obj.optJSONArray("riskIndicators")
            if (riskArr != null) {
                for (i in 0 until riskArr.length()) {
                    riskIndicators.add(riskArr.optString(i))
                }
            }

            GeminiContextAnalysis(
                intent = obj.optString("intent", "emotional_support"),
                emotion = obj.optString("emotion", "neutral"),
                responseMode = obj.optString("responseMode", "LISTEN"),
                suggestedAction = obj.optString("suggestedAction", "explore_feelings"),
                riskIndicators = riskIndicators,
                requiresClarification = obj.optBoolean("requiresClarification", false),
                contextSummary = obj.optString("contextSummary", ""),
                isLiteral = obj.optBoolean("isLiteral", true),
                isFigurative = obj.optBoolean("isFigurative", false),
                isFictionalOrHypothetical = obj.optBoolean("isFictionalOrHypothetical", false)
            )
        } catch (e: Exception) {
            Log.w(TAG, "JSON parsing error on Gemini output: ${e.message}")
            null
        }
    }

    /**
     * Fallback deterministic analysis using ContextAnalyzer when Gemini is offline or fails.
     */
    fun fallbackDeterministicAnalysis(
        userMessage: String,
        history: List<Pair<String, String>>
    ): GeminiContextAnalysis {
        val signals = ContextAnalyzer.extractSignals(userMessage, history)
        val lower = userMessage.lowercase()

        val emotion = when {
            signals.loneliness -> "loneliness"
            signals.stress -> "stress"
            signals.emotionalExhaustion -> "exhaustion"
            signals.sadness -> "sadness"
            lower.contains("anxi") -> "anxiety"
            lower.contains("overwhelm") -> "overwhelm"
            lower.contains("frustrat") || lower.contains("angry") -> "frustration"
            lower.contains("happy") || lower.contains("good") -> "joy"
            else -> "neutral"
        }

        val mode = when {
            signals.emotionalExhaustion || signals.loneliness -> "LISTEN"
            lower.contains("anxi") || lower.contains("overwhelm") -> "CALM"
            lower.contains("why") || lower.contains("understand") -> "UNDERSTAND"
            lower.contains("focus") || lower.contains("concentrate") -> "REFOCUS"
            lower.contains("thank") || lower.contains("grateful") -> "GRATITUDE"
            else -> "LISTEN"
        }

        val intent = when {
            signals.desireToDisappear || signals.passiveDeathWish -> "distress_disclosure"
            lower.contains("just listen") || lower.contains("vent") -> "venting"
            lower.contains("what should i do") || lower.contains("advice") -> "seeking_advice"
            else -> "emotional_support"
        }

        return GeminiContextAnalysis(
            intent = intent,
            emotion = emotion,
            responseMode = mode,
            suggestedAction = if (mode == "CALM") "offer_grounding" else "explore_feelings",
            riskIndicators = signals.evidence,
            requiresClarification = signals.needsClarification,
            contextSummary = "Deterministic extraction: user expressing $emotion",
            isLiteral = !signals.isFigurative && !signals.isFictionalOrHypothetical,
            isFigurative = signals.isFigurative,
            isFictionalOrHypothetical = signals.isFictionalOrHypothetical
        )
    }
}

