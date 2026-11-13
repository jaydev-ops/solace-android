package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.RegulationMode
import com.example.data.safety.ContextAnalyzer
import com.example.data.safety.EscalationManager
import com.example.data.safety.RiskDecisionEngine
import com.example.data.safety.RiskLevel
import com.example.data.safety.SafetyResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private const val TAG = "SolaceAiService"

class SolaceAiService {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(25, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .build()
    }

    // Deterministic Crisis Guardrails
    private val crisisKeywords = listOf(
        "suicide", "kill myself", "end my life", "want to die", "commit suicide",
        "self harm", "cut myself", "slit my wrist", "hurt myself", "take all my pills",
        "better off dead", "end it all"
    )

    fun checkCrisisSignal(userMessage: String): Boolean {
        val signals = ContextAnalyzer.extractSignals(userMessage)
        val level = RiskDecisionEngine.determineRisk(signals)
        return level == RiskLevel.LEVEL_3_SUICIDAL_IDEATION || level == RiskLevel.LEVEL_4_CRITICAL
    }

    fun getCrisisResponse(country: String = "IN"): String {
        return SafetyResponseHandler.generateResponse(
            level = RiskLevel.LEVEL_4_CRITICAL,
            signals = ContextAnalyzer.extractSignals("crisis"),
            country = country
        )
    }

    /**
     * Resolves API Key: checks custom user key first, then BuildConfig/env.
     */
    fun resolveApiKey(customKey: String?): String {
        if (!customKey.isNullOrBlank()) {
            return customKey.trim()
        }
        return try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            val key = field.get(null) as? String ?: ""
            if (key != "MY_GEMINI_API_KEY") key.trim() else ""
        } catch (_: Exception) {
            ""
        }
    }

    /**
     * Streams or delivers response as a Flow, with online Gemini API when key is available,
     * and rich adaptive local sanctuary dialogue when offline or key is unconfigured.
     */
    fun streamResponse(
        userName: String,
        customApiKey: String? = null,
        currentMood: String? = null,
        moodIntensity: Int? = null,
        recentJournalSnippet: String? = null,
        regulationMode: RegulationMode = RegulationMode.LISTEN,
        conversationHistory: List<Pair<String, String>> = emptyList(),
        latestUserMessage: String
    ): Flow<String> = flow {
        // 1. Graduated Mental Health Risk Evaluation
        val assessment = EscalationManager.evaluate(
            userMessage = latestUserMessage,
            history = conversationHistory,
            userName = userName
        )

        if (assessment.overrideResponse != null) {
            val response = assessment.overrideResponse
            val sentences = response.split("\n\n")
            for (sentence in sentences) {
                val words = sentence.split(" ")
                for (i in words.indices step 4) {
                    val chunk = words.subList(i, minOf(i + 4, words.size)).joinToString(" ") + " "
                    emit(chunk)
                    delay(35)
                }
                emit("\n\n")
                delay(100)
            }
            return@flow
        }

        val effectiveKey = resolveApiKey(customApiKey)

        // 2. If valid API key available, attempt direct Google Gemini REST call
        if (effectiveKey.isNotBlank()) {
            try {
                val fullText = withContext(Dispatchers.IO) {
                    callGeminiRestApi(
                        apiKey = effectiveKey,
                        userName = userName,
                        currentMood = currentMood,
                        moodIntensity = moodIntensity,
                        recentJournalSnippet = recentJournalSnippet,
                        regulationMode = regulationMode,
                        conversationHistory = conversationHistory,
                        latestUserMessage = latestUserMessage
                    )
                }

                if (fullText.isNotBlank()) {
                    // Stream in soft, natural word chunks for serene typing cadence
                    val words = fullText.split(" ")
                    val chunkSize = 3
                    for (i in words.indices step chunkSize) {
                        val chunk = words.subList(i, minOf(i + chunkSize, words.size)).joinToString(" ") + " "
                        emit(chunk)
                        delay(40)
                    }
                    return@flow
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gemini REST API failed, transitioning smoothly to Adaptive Sanctuary Engine: ${e.message}")
            }
        }

        // 3. Adaptive Local Sanctuary Dialogue Engine
        val localResponse = generateAdaptiveSanctuaryResponse(
            userName = userName,
            currentMood = currentMood,
            moodIntensity = moodIntensity,
            regulationMode = regulationMode,
            history = conversationHistory,
            latestUserMessage = latestUserMessage
        )

        // Stream local response with warm, calming cadence
        val sentences = localResponse.split("\n\n")
        for (sentence in sentences) {
            val words = sentence.split(" ")
            for (i in words.indices step 4) {
                val chunk = words.subList(i, minOf(i + 4, words.size)).joinToString(" ") + " "
                emit(chunk)
                delay(45)
            }
            emit("\n\n")
            delay(120)
        }
    }

    suspend fun generateResponse(
        userName: String,
        customApiKey: String? = null,
        currentMood: String?,
        moodIntensity: Int?,
        recentJournalSnippet: String?,
        regulationMode: RegulationMode,
        conversationHistory: List<Pair<String, String>>,
        latestUserMessage: String
    ): String = withContext(Dispatchers.IO) {
        val assessment = EscalationManager.evaluate(
            userMessage = latestUserMessage,
            history = conversationHistory,
            userName = userName
        )
        if (assessment.overrideResponse != null) {
            return@withContext assessment.overrideResponse
        }

        val key = resolveApiKey(customApiKey)
        if (key.isNotBlank()) {
            try {
                return@withContext callGeminiRestApi(
                    apiKey = key,
                    userName = userName,
                    currentMood = currentMood,
                    moodIntensity = moodIntensity,
                    recentJournalSnippet = recentJournalSnippet,
                    regulationMode = regulationMode,
                    conversationHistory = conversationHistory,
                    latestUserMessage = latestUserMessage
                )
            } catch (e: Exception) {
                Log.w(TAG, "Gemini call failed, using adaptive sanctuary engine", e)
            }
        }

        delay(400)
        return@withContext generateAdaptiveSanctuaryResponse(
            userName = userName,
            currentMood = currentMood,
            moodIntensity = moodIntensity,
            regulationMode = regulationMode,
            history = conversationHistory,
            latestUserMessage = latestUserMessage
        )
    }

    private fun callGeminiRestApi(
        apiKey: String,
        userName: String,
        currentMood: String?,
        moodIntensity: Int?,
        recentJournalSnippet: String?,
        regulationMode: RegulationMode,
        conversationHistory: List<Pair<String, String>>,
        latestUserMessage: String
    ): String {
        val systemPrompt = buildSystemPrompt(userName, currentMood, moodIntensity, recentJournalSnippet, regulationMode)

        val contentsArray = JSONArray()

        val recentHistory = conversationHistory.takeLast(6)
        for ((role, text) in recentHistory) {
            val partObj = JSONObject().put("text", text)
            val contentObj = JSONObject()
                .put("role", if (role.equals("user", ignoreCase = true)) "user" else "model")
                .put("parts", JSONArray().put(partObj))
            contentsArray.put(contentObj)
        }

        contentsArray.put(
            JSONObject()
                .put("role", "user")
                .put("parts", JSONArray().put(JSONObject().put("text", latestUserMessage)))
        )

        val rootRequest = JSONObject()
        rootRequest.put("contents", contentsArray)

        val systemInstruction = JSONObject()
            .put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
        rootRequest.put("systemInstruction", systemInstruction)

        val generationConfig = JSONObject()
            .put("temperature", 0.75)
            .put("maxOutputTokens", 500)
        rootRequest.put("generationConfig", generationConfig)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = rootRequest.toString().toRequestBody(mediaType)

        // Try gemini-1.5-flash first, fallback to gemini-2.0-flash
        val models = listOf("gemini-1.5-flash", "gemini-2.0-flash", "gemini-2.5-flash")
        var lastException: Exception? = null

        for (model in models) {
            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                httpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val bodyString = response.body?.string() ?: ""
                        val jsonResponse = JSONObject(bodyString)
                        val candidates = jsonResponse.optJSONArray("candidates")
                        if (candidates != null && candidates.length() > 0) {
                            val candidate = candidates.getJSONObject(0)
                            val content = candidate.optJSONObject("content")
                            val parts = content?.optJSONArray("parts")
                            if (parts != null && parts.length() > 0) {
                                val text = parts.getJSONObject(0).optString("text")
                                if (text.isNotBlank()) return text.trim()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                lastException = e
            }
        }

        throw lastException ?: IllegalStateException("Could not generate response from Gemini API")
    }

    private fun buildSystemPrompt(
        userName: String,
        currentMood: String?,
        moodIntensity: Int?,
        recentJournalSnippet: String?,
        regulationMode: RegulationMode
    ): String {
        val moodInfo = if (currentMood != null) "Current recorded mood: $currentMood (Intensity: ${moodIntensity ?: 5}/10)." else ""
        val journalInfo = if (!recentJournalSnippet.isNullOrBlank()) "Recent reflection: \"$recentJournalSnippet\"." else ""

        val cleanName = userName.trim()
        val hasValidName = cleanName.isNotBlank() &&
            !cleanName.equals("Sanctuary", ignoreCase = true) &&
            !cleanName.equals("User", ignoreCase = true) &&
            !cleanName.equals("Guest", ignoreCase = true) &&
            !cleanName.equals("Sanctuary Guest", ignoreCase = true)

        val userAddressing = if (hasValidName) {
            "You are conversing with $cleanName. Address them warmly by their name sparingly."
        } else {
            "Address the user warmly in the second person. Do not call the user 'Sanctuary', 'User', or make up names."
        }

        val modeGuidance = when (regulationMode) {
            RegulationMode.LISTEN -> "MODE: LISTEN. Provide pure unconditional holding space. Reflect what you hear with deep compassion. Never offer unsolicited solutions or hurry the user."
            RegulationMode.UNDERSTAND -> "MODE: UNDERSTAND. Help the user gently unlayer what is happening beneath the surface with warm, curious questions."
            RegulationMode.CALM -> "MODE: CALM. Ground the nervous system. Provide polyvagal cues: relaxing the shoulders, softening the jaw, slow unhurried breaths."
            RegulationMode.REFLECT -> "MODE: REFLECT. Offer gentle cognitive mirroring and philosophical reframing. Help discover wisdom without invalidating pain."
            RegulationMode.REFOCUS -> "MODE: REFOCUS. Help reduce overwhelm into one tiny, kind, zero-pressure micro-step for the next 10 minutes."
            RegulationMode.GRATITUDE -> "MODE: GRATITUDE. Gently illuminate small, quiet mercies from today without forced positivity."
        }

        return """
            You are solace, a private, serene emotional sanctuary companion.
            $userAddressing
            $moodInfo
            $journalInfo

            Guidelines:
            1. Warm, grounding, and empathetic. Unhurried cadence.
            2. Keep responses concise (1 to 3 gentle paragraphs).
            3. $modeGuidance
        """.trimIndent()
    }

    /**
     * Highly dynamic, emotionally attuned offline sanctuary dialogue engine.
     * Selects from rich contextual pools so it NEVER gives repetitive canned messages.
     */
    fun generateAdaptiveSanctuaryResponse(
        userName: String,
        currentMood: String?,
        moodIntensity: Int?,
        regulationMode: RegulationMode,
        history: List<Pair<String, String>>,
        latestUserMessage: String
    ): String {
        val cleanName = userName.trim()
        val hasValidName = cleanName.isNotBlank() &&
                !cleanName.equals("Sanctuary", ignoreCase = true) &&
                !cleanName.equals("User", ignoreCase = true) &&
                !cleanName.equals("Guest", ignoreCase = true) &&
                !cleanName.equals("Sanctuary Guest", ignoreCase = true)
        val nameComma = if (hasValidName) ", $cleanName" else ""

        val lower = latestUserMessage.lowercase()
        val wordCount = latestUserMessage.split("\\s+".toRegex()).size
        val isShort = wordCount < 6
        val turnCount = history.size / 2

        // Emotional categorization
        val isAnxious = lower.contains("anxi") || lower.contains("panic") || lower.contains("nervous") ||
                lower.contains("worry") || lower.contains("scared") || lower.contains("fear") || lower.contains("chest")
        val isOverwhelmed = lower.contains("overwhelm") || lower.contains("too much") || lower.contains("heavy") ||
                lower.contains("burden") || lower.contains("drowning") || lower.contains("stuck") || lower.contains("frozen")
        val isTired = lower.contains("tired") || lower.contains("exhaust") || lower.contains("sleep") ||
                lower.contains("drained") || lower.contains("burnout") || lower.contains("weary")
        val isSad = lower.contains("sad") || lower.contains("cry") || lower.contains("tear") ||
                lower.contains("grief") || lower.contains("heartbreak") || lower.contains("lonely") || lower.contains("alone")
        val isAngry = lower.contains("angry") || lower.contains("mad") || lower.contains("frustrat") ||
                lower.contains("hate") || lower.contains("annoy") || lower.contains("unfair")
        val isHappy = lower.contains("happy") || lower.contains("joy") || lower.contains("good") ||
                lower.contains("great") || lower.contains("peace") || lower.contains("grateful") || lower.contains("calm")
        val isSelfCritical = lower.contains("stupid") || lower.contains("failure") || lower.contains("hate myself") ||
                lower.contains("mess up") || lower.contains("guilt") || lower.contains("shame")

        // 1. ANXIETY & RESTLESSNESS
        if (isAnxious) {
            val responses = listOf(
                "I feel how fast everything is moving inside you right now$nameComma. When anxiety enters, our thoughts race to keep us safe, but your body needs quiet reassurance first.\n\nNotice where your feet are planted on the floor right now. Can you drop your shoulders half an inch down, unclench your teeth, and let your next breath sigh out softly? You are completely safe in this moment.",
                "It's okay that your nervous system is on high alert$nameComma. You don't have to fight this fluttery feeling or force yourself to calm down instantly.\n\nTake one slow breath in through your nose for 4 counts... and gently release it through parted lips for 6 counts. What part of your body feels this tension most vividly right now?",
                "That tight, restless energy is so exhausting to hold alone$nameComma. Remember: feelings are waves, and waves always crest and break.\n\nLet's keep the sanctuary very quiet right now. You don't need to make any big decisions today. Just be right here, breathing beside me."
            )
            return responses.random()
        }

        // 2. OVERWHELM & PARALYSIS
        if (isOverwhelmed) {
            val responses = listOf(
                "When everything lands at once$nameComma, our minds freeze because they try to solve twenty problems simultaneously. You don't need to carry tomorrow or next week right now.\n\nLet's shrink the universe down to just the chair or bed you're resting on. If we could put 90% of those tasks into a locked drawer for the next hour, what is the single kindest thing your body needs?",
                "I hear the weight of all those spinning plates$nameComma. Feeling overwhelmed isn't a sign that you are weak—it's a sign that you've been strong for too long without enough room to rest.\n\nYou have permission to pause. Right here, nothing is due, nobody is waiting, and there is nothing you need to prove.",
                "It makes total sense that you feel frozen$nameComma. When demands exceed our current energy, paralysis is simply our nervous system saying: 'Please pause.'\n\nLet's honor that pause. Take a long, unhurried exhale. What is one small expectation you can let go of for the rest of today?"
            )
            return responses.random()
        }

        // 3. EXHAUSTION & TIREDNESS
        if (isTired) {
            val responses = listOf(
                "Bone-deep tiredness is a holy signal$nameComma. It's your spirit asking for quiet restoration, not more productivity.\n\nYou don't have to untangle anything right now. Close your eyes for ten seconds, let your hands rest limp in your lap, and allow yourself to simply exist without performing.",
                "I hear how drained your battery is$nameComma. Sometimes showing up to take a breath is a victory in itself. Give yourself credit for carrying everything you've shouldered so far today.",
                "When you are this tired$nameComma, your mind will naturally paint the world in gray tones. Don't believe every pessimistic thought your brain whispers when your body is fatigued. Rest first, reflect later."
            )
            return responses.random()
        }

        // 4. SADNESS & LONELINESS
        if (isSad) {
            val responses = listOf(
                "I'm sitting right beside you in this quiet ache$nameComma. Tears and heavy hearts are not failures; they are proof that something mattered deeply to you.\n\nYou don't have to put on a brave face here. I am holding unhurried space for whatever feelings need to be felt.",
                "Loneliness and sadness have a hollow weight that feels impossible to explain to the busy world outside. But here in your sanctuary, you are seen and welcomed exactly as you are.\n\nWrap your arms around yourself or hold a warm cup. What does this tender feeling wish someone would understand?",
                "There is nothing you need to fix about how you feel today$nameComma. Softness is its own form of resilience. Take all the time you need—I'm not going anywhere."
            )
            return responses.random()
        }

        // 5. FRUSTRATION & ANGER
        if (isAngry) {
            val responses = listOf(
                "Your frustration makes total sense$nameComma. Anger is often the fierce guardian of a boundary that was crossed or an injustice that stung.\n\nYou have every right to feel this heat. You don't have to swallow it down or pretend it's fine. What part of this feels most unfair or dismissive to you?",
                "I hear the fire in that$nameComma. When things are unfair or people let us down, it's exhausting to hold in. Let it breathe here in the sanctuary—you don't have to apologize for feeling upset.",
                "Anger is energy asking to be acknowledged$nameComma. Before doing anything with it, take one long, fierce exhale. What is this feeling trying to protect in you?"
            )
            return responses.random()
        }

        // 6. JOY & GRATITUDE
        if (isHappy) {
            val responses = listOf(
                "What a gift to witness this lightness in your spirit${if (hasValidName) ", $cleanName!" else "!"} When moments of ease and warmth arrive, letting them soak into your chest helps build a gentle emotional reservoir.\n\nWhat brought this glimmer of peace or joy to your heart today?",
                "I love hearing that softness in your words$nameComma. Savor it without waiting for the other shoe to drop. You deserve every ounce of this warmth.",
                "Let that feeling expand through your breath$nameComma. Notice how your body feels right now when it's open and unburdened—anchor this feeling as your sanctuary baseline."
            )
            return responses.random()
        }

        // 7. SELF-CRITICISM & GUILT
        if (isSelfCritical) {
            val responses = listOf(
                "Please pause and hear me gently${if (hasValidName) ", $cleanName:" else ":"} you are speaking to yourself with a harshness you would never use on someone you love.\n\nYou are a human being navigating a complex, demanding life with limited energy. What would happen if you offered yourself the same forgiveness you so readily extend to others?",
                "Mistakes and imperfect days do not diminish your worth$nameComma. Self-criticism is often an old survival habit trying to keep us in control, but kindness is what actually heals.\n\nTake a slow breath. Can we hold what happened with gentle curiosity instead of shame?",
                "I see you striving so hard$nameComma. Give the harsh inner judge a seat on the bench. In this sanctuary, you are accepted completely, flaws and all."
            )
            return responses.random()
        }

        // 8. GENERAL / ROLE-DRIVEN RESPONSES
        return when (regulationMode) {
            RegulationMode.LISTEN -> {
                val pool = listOf(
                    "Thank you for entrusting me with that$nameComma. It takes vulnerability to put these quiet thoughts into words. I am listening with deep attention—what else feels close to the surface for you?",
                    "I hear you$nameComma. There is no rush to wrap this in a neat bow or find a lesson right now. If your heart could speak without any filter, what would it say next?",
                    "Sitting with you in this space$nameComma. You don't have to perform or keep things light here. What feels heaviest about what you just shared?",
                    "I'm here holding this space with you$nameComma. Even when feelings feel tangled, bringing them to light is the first gentle step. Take your time."
                )
                pool[turnCount % pool.size]
            }
            RegulationMode.CALM -> {
                val pool = listOf(
                    "Let's pause right here together$nameComma. Notice your breath without trying to force it.\n\nSoftly drop your shoulders away from your ears. Unclench your jaw. Feel the gentle support of the seat underneath you. Can you take one long, smooth exhale through your lips?",
                    "Before untangling those thoughts, let's bring your body home first$nameComma.\n\nFeel the air entering your nostrils, cool and quiet, and leaving warm. Inhale for 4... hold for 4... exhale for 6. Let that calm ripple through your chest.",
                    "Your mind is trying so hard to carry everything$nameComma. Let's give it a 60-second recess. Soften your gaze, uncurl your fingers, and feel the steady rhythm of your heartbeat."
                )
                pool[turnCount % pool.size]
            }
            RegulationMode.UNDERSTAND -> {
                val pool = listOf(
                    "It sounds like there are multiple subtle layers to what you're experiencing$nameComma. When did you first notice this sensation or thought pattern beginning to build today?",
                    "That is so insightful of you to notice$nameComma. Often beneath this feeling is an unvoiced boundary or a quiet longing. What do you feel you needed most in that moment?",
                    "Looking at this with compassionate curiosity: if this feeling had a voice of its own, what message is it trying to deliver to your attention$nameComma?"
                )
                pool[turnCount % pool.size]
            }
            RegulationMode.REFLECT -> {
                val pool = listOf(
                    "That reflects real self-awareness$nameComma. Even uncomfortable emotions are often protective guides. Looking back at this from a place of peace, what gentle wisdom is emerging for you?",
                    "Notice how much resilience you've already shown$nameComma. What would a compassionate mentor or loving friend reflect back to you about this situation?",
                    "Everything in life moves in seasons and waves$nameComma. What can you release control over right now, trusting that clarity will arrive in its own time?"
                )
                pool[turnCount % pool.size]
            }
            RegulationMode.REFOCUS -> {
                val pool = listOf(
                    "When everything feels large and loud, let's shrink the horizon down to the next ten minutes$nameComma. What is one tiny, zero-pressure micro-step you could take right now that requires no perfection?",
                    "You don't need to cross the whole mountain today$nameComma. Just the next single step. Is it drinking a glass of cold water, stepping outside for fresh air, or closing your eyes for five minutes?",
                    "Let's protect your energy$nameComma. Of everything on your mind, what is the ONE thing that genuinely belongs to today, while letting the rest rest until tomorrow?"
                )
                pool[turnCount % pool.size]
            }
            RegulationMode.GRATITUDE -> {
                val pool = listOf(
                    "Even in the middle of a heavy week, small mercies quiet our nervous system$nameComma. What is one small sensory comfort you enjoyed recently—the warmth of tea, soft light, or a quiet breath?",
                    "That touches on something very pure$nameComma. Let that small relief settle into your chest for three full breaths. Warmth and gratitude are quiet anchors.",
                    "Noticing beauty amidst imperfection is a profound strength$nameComma. What is something you appreciate about your own courage or kindness today?"
                )
                pool[turnCount % pool.size]
            }
        }
    }
}


data class CrisisHelpline(
    val name: String,
    val contact: String,
    val description: String
)

val CrisisHelplines = listOf(
    CrisisHelpline(
        name = "988 Suicide & Crisis Lifeline",
        contact = "Call or Text 988",
        description = "Free, confidential 24/7 support for anyone in emotional distress or suicidal crisis."
    ),
    CrisisHelpline(
        name = "Crisis Text Line",
        contact = "Text HOME to 741741",
        description = "Connect with a volunteer Crisis Counselor 24/7 over free text message."
    ),
    CrisisHelpline(
        name = "The Trevor Project",
        contact = "Call 1-866-488-7386 or Text START to 678-678",
        description = "24/7 confidential suicide prevention and crisis intervention for LGBTQ+ young people."
    ),
    CrisisHelpline(
        name = "Veterans Crisis Line",
        contact = "Dial 988, then press 1",
        description = "Caring, qualified responders with the Department of Veterans Affairs available 24/7."
    ),
    CrisisHelpline(
        name = "SAMHSA National Helpline",
        contact = "1-800-662-4357",
        description = "Treatment referral and information service for mental health and substance use concerns."
    )
)
