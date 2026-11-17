package com.example.data.safety

import com.example.data.model.RegulationMode
import java.util.concurrent.atomic.AtomicInteger

/**
 * Deterministic Rule-Based Conversation Engine for Solace+.
 *
 * Implements 100% offline, local, rule-based conversational responses based on detected
 * keywords and emotional expressions. Eliminates all external LLM / Gemini dependencies
 * while ensuring high-quality, non-repetitive, compassionate human-like dialogue.
 */
object RuleBasedConversationEngine {

    private val counter = AtomicInteger(0)

    enum class RuleCategory {
        BAD_DAY,
        LONELY,
        OVERWHELMED,
        CANNOT_CONCENTRATE,
        EMOTIONALLY_EXHAUSTED,
        ANXIOUS,
        DONT_KNOW_FEELING,
        JUST_WANT_LISTENING,
        FEELING_GOOD_OR_SAFE,
        NOBODY_UNDERSTANDS,
        NEED_HELP,
        SAD_AND_HURTING,
        ANGRY_AND_FRUSTRATED,
        SELF_CRITICAL_AND_GUILT,
        GREETING,
        GRATITUDE,
        CHECK_IN_QUESTION,
        GENERAL_HOLDING_SPACE
    }

    // --- REGEX PATTERNS FOR KEYWORD DETECTION ---

    // 1. FEELING GOOD / SAFE / HAPPY / RELIEF
    private val feelingGoodRegex = Regex(
        """\b(i\s*['a]?\s*m\s+(good|fine|okay|ok|doing\s+good|doing\s+fine|doing\s+well|safe|better|alright|happy|great|awesome|blessed|peaceful|calm|content|relaxed)|i\s+am\s+(good|fine|okay|ok|doing\s+good|doing\s+fine|doing\s+well|safe|better|alright|happy|great|awesome|blessed|peaceful|calm|content|relaxed)|im\s+(good|fine|okay|ok|safe|better|alright|happy|great|awesome|blessed|peaceful|calm|content|relaxed)|feeling\s+(good|fine|okay|ok|better|happy|great|peaceful|calm|content|relaxed|awesome|blessed)|doing\s+(good|fine|well|great|okay|ok)|all\s+good|no\s+(i['a]?m\s+good|im\s+good|i\s+am\s+good)|feel\s+(so\s+)?(good|happy|peaceful|calm|relieved|great|light)|much\s+better|so\s+happy|feeling\s+wonderful|feeling\s+fantastic)\b""",
        RegexOption.IGNORE_CASE
    )
    private val singleWordGoodRegex = Regex(
        """^(good|fine|okay|ok|all good|doing good|doing well|doing fine|i m good|im good|i'm good|great|awesome|happy|better|peaceful|calm|relaxed|relieved)$""",
        RegexOption.IGNORE_CASE
    )
    private val positiveEmotionsRegex = Regex(
        """\b(happiness|joy|joyful|delighted|excited|excitement|proud\s+of\s+myself|accomplished|smiling|smiled\s+today|wonderful\s+day|great\s+news|blessed|grateful\s+today|relieved|huge\s+relief|feeling\s+alive|feeling\s+lighter)\b""",
        RegexOption.IGNORE_CASE
    )

    // 2. EXPLICIT REQUESTS FOR HELP
    private val needHelpRegex = Regex(
        """\b(help\s+me|i\s+need\s+help|can\s+you\s+help|please\s+help|need\s+some\s+help|give\s+me\s+advice|what\s+should\s+i\s+do|what\s+can\s+i\s+do|i['a]?\s*m\s+struggling|struggling\s+a\s+lot|don['o]t\s+know\s+what\s+to\s+do|need\s+support|help\s+please)\b""",
        RegexOption.IGNORE_CASE
    )

    // 3. ANXIETY & PANIC
    private val anxiousRegex = Regex(
        """\b(feel\s+anxious|feeling\s+anxious|having\s+anxiety|anxious|anxiety|panic|panicking|panic\s+attack|racing\s+heart|heart\s+is\s+pounding|can['o]t\s+breathe|short\s+of\s+breath|scared|terrified|nervous|shaking|sense\s+of\s+dread|chest\s+feels\s+tight|freaking\s+out)\b""",
        RegexOption.IGNORE_CASE
    )

    // 4. SADNESS & HEARTBREAK & PAIN
    private val sadRegex = Regex(
        """\b(feel\s+sad|feeling\s+sad|so\s+sad|crying|cried\s+today|in\s+tears|tears|heartbroken|heartbreak|grief|grieving|hurting\s+inside|emotional\s+pain|depressed|feeling\s+low|feeling\s+down|miserable|broken|devastated)\b""",
        RegexOption.IGNORE_CASE
    )

    // 5. OVERWHELM & STRESS
    private val overwhelmedRegex = Regex(
        """\b(am\s+overwhelmed|feeling\s+overwhelmed|so\s+overwhelmed|overwhelm|too\s+much\s+to\s+handle|drowning\s+in|stressed\s+out|so\s+stressed|stress|burning\s+out|burnout|pressure\s+is\s+too\s+much|chaos|paralyzed\s+with\s+work)\b""",
        RegexOption.IGNORE_CASE
    )

    // 6. LONELINESS & ISOLATION
    private val lonelyRegex = Regex(
        """\b(feel\s+(so\s+)?lonely|feeling\s+lonely|loneliness|all\s+alone|nobody\s+cares|isolated|isolation|feel\s+abandoned|empty\s+inside|have\s+no\s+one\s+to\s+talk\s+to)\b""",
        RegexOption.IGNORE_CASE
    )

    // 7. BAD DAY
    private val badDayRegex = Regex(
        """\b(had\s+a\s+(bad|rough|terrible|horrible|hard|awful|miserable)\s+day|rough\s+day|bad\s+day|terrible\s+day|awful\s+day|horrible\s+day|today\s+sucked|worst\s+day)\b""",
        RegexOption.IGNORE_CASE
    )

    // 8. EMOTIONAL EXHAUSTION / TIREDNESS
    private val exhaustedRegex = Regex(
        """\b(emotionally\s+exhausted|feeling\s+drained|utterly\s+exhausted|no\s+energy\s+left|so\s+tired|exhausted|depleted|weary|fatigued|tired\s+of\s+everything|can['o]t\s+keep\s+my\s+eyes\s+open)\b""",
        RegexOption.IGNORE_CASE
    )

    // 9. ANGER & FRUSTRATION
    private val angryRegex = Regex(
        """\b(so\s+angry|feeling\s+angry|mad|furious|frustrated|frustration|irritated|annoyed|unfair|screaming\s+inside|hate\s+everything|pissed\s+off|lost\s+my\s+temper)\b""",
        RegexOption.IGNORE_CASE
    )

    // 10. GUILT & SELF-CRITICISM
    private val selfCriticalRegex = Regex(
        """\b(feel\s+guilty|guilt|ashamed|shame|worthless|i['a]?m\s+a\s+failure|failed|i\s+messed\s+up|hate\s+myself|stupid\s+mistake|not\s+good\s+enough|disappointed\s+in\s+myself|blame\s+myself)\b""",
        RegexOption.IGNORE_CASE
    )

    // 11. CONCENTRATION & FOCUS
    private val concentrationRegex = Regex(
        """\b(cannot|can['o]t|unable\s+to)\s+(concentrate|focus)|mind\s+is\s+wandering|too\s+distracted|brain\s+fog|cannot\s+think\s+straight\b""",
        RegexOption.IGNORE_CASE
    )

    // 12. DONT KNOW FEELING / CONFUSED / NUMB
    private val dontKnowRegex = Regex(
        """\b(don['o]t\s+know\s+what\s+i\s+feel|confused\s+about\s+my\s+feelings|don['o]t\s+know\s+how\s+i\s+feel|numb|feeling\s+numb|empty|lost|blank|confused)\b""",
        RegexOption.IGNORE_CASE
    )

    // 13. JUST WANT LISTENING
    private val justListenRegex = Regex(
        """\b(just\s+want\s+someone\s+to\s+listen|just\s+listen\s+to\s+me|need\s+to\s+vent|listen\s+without\s+giving\s+advice|don['o]t\s+need\s+solutions|just\s+hear\s+me\s+out)\b""",
        RegexOption.IGNORE_CASE
    )

    // 14. NOBODY UNDERSTANDS
    private val nobodyUnderstandsRegex = Regex(
        """\b(nobody\s+(really\s+)?understands\s+me|no\s+one\s+understands\s+me|feel\s+misunderstood|nobody\s+gets\s+me|no\s+one\s+gets\s+me)\b""",
        RegexOption.IGNORE_CASE
    )

    // 15. GREETINGS
    private val greetingRegex = Regex(
        """^(hi|hello|hey|hey\s+there|good\s+morning|good\s+afternoon|good\s+evening|greetings|howdy)$""",
        RegexOption.IGNORE_CASE
    )

    // 16. GRATITUDE
    private val gratitudeRegex = Regex(
        """\b(thank\s+you|thanks|thank\s+u|appreciate\s+it|appreciate\s+you|that\s+helped|feeling\s+grateful|thanks\s+a\s+lot)\b""",
        RegexOption.IGNORE_CASE
    )

    // 17. CHECK IN / IDENTITY QUESTIONS
    private val checkInQuestionRegex = Regex(
        """\b(how\s+are\s+you|who\s+are\s+you|what\s+are\s+you|what\s+is\s+solace|what\s+can\s+you\s+do|tell\s+me\s+about\s+yourself)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Matches the incoming message against deterministic rule categories.
     */
    fun matches(userMessage: String): RuleCategory? {
        val trimmed = userMessage.trim()
        if (trimmed.isEmpty()) return null

        // Check for feeling good / safe first if explicit, or single-word positive affirmations
        if (feelingGoodRegex.containsMatchIn(trimmed) || singleWordGoodRegex.matches(trimmed) || positiveEmotionsRegex.containsMatchIn(trimmed)) {
            return RuleCategory.FEELING_GOOD_OR_SAFE
        }

        return when {
            // Priority 1: Explicit requests for help
            needHelpRegex.containsMatchIn(trimmed) -> RuleCategory.NEED_HELP

            // Priority 2: High emotional distress
            anxiousRegex.containsMatchIn(trimmed) -> RuleCategory.ANXIOUS
            sadRegex.containsMatchIn(trimmed) -> RuleCategory.SAD_AND_HURTING
            overwhelmedRegex.containsMatchIn(trimmed) -> RuleCategory.OVERWHELMED
            lonelyRegex.containsMatchIn(trimmed) -> RuleCategory.LONELY
            exhaustedRegex.containsMatchIn(trimmed) -> RuleCategory.EMOTIONALLY_EXHAUSTED
            angryRegex.containsMatchIn(trimmed) -> RuleCategory.ANGRY_AND_FRUSTRATED
            selfCriticalRegex.containsMatchIn(trimmed) -> RuleCategory.SELF_CRITICAL_AND_GUILT
            badDayRegex.containsMatchIn(trimmed) -> RuleCategory.BAD_DAY
            nobodyUnderstandsRegex.containsMatchIn(trimmed) -> RuleCategory.NOBODY_UNDERSTANDS
            justListenRegex.containsMatchIn(trimmed) -> RuleCategory.JUST_WANT_LISTENING
            concentrationRegex.containsMatchIn(trimmed) -> RuleCategory.CANNOT_CONCENTRATE
            dontKnowRegex.containsMatchIn(trimmed) -> RuleCategory.DONT_KNOW_FEELING

            // Priority 3: Social / conversational rituals
            gratitudeRegex.containsMatchIn(trimmed) -> RuleCategory.GRATITUDE
            greetingRegex.matches(trimmed) -> RuleCategory.GREETING
            checkInQuestionRegex.containsMatchIn(trimmed) -> RuleCategory.CHECK_IN_QUESTION

            // Fallback for any other user words
            else -> RuleCategory.GENERAL_HOLDING_SPACE
        }
    }

    /**
     * Generates a compassionate, non-repetitive, deterministic rule-based response.
     */
    fun generateRuleResponse(
        category: RuleCategory,
        userName: String? = null,
        history: List<Pair<String, String>> = emptyList(),
        regulationMode: RegulationMode = RegulationMode.LISTEN,
        latestUserMessage: String = ""
    ): String {
        val cleanName = userName?.trim()
        val hasName = !cleanName.isNullOrBlank() &&
            !cleanName.equals("Sanctuary", ignoreCase = true) &&
            !cleanName.equals("User", ignoreCase = true) &&
            !cleanName.equals("Guest", ignoreCase = true) &&
            !cleanName.equals("Sanctuary Guest", ignoreCase = true)
        val nameComma = if (hasName) ", $cleanName" else ""

        val index = (counter.getAndIncrement() + history.size)

        return when (category) {
            // --- 1. FEELING GOOD / LIGHT / SAFE ---
            RuleCategory.FEELING_GOOD_OR_SAFE -> {
                val lastAssistant = history.lastOrNull { 
                    it.first.equals("model", ignoreCase = true) || it.first.equals("assistant", ignoreCase = true) 
                }?.second

                val wasAnsweringScreening = lastAssistant != null && (
                    lastAssistant.contains("thoughts about hurting yourself", ignoreCase = true) ||
                    lastAssistant.contains("thoughts of ending your life", ignoreCase = true) ||
                    lastAssistant.contains("hurting yourself or ending your life", ignoreCase = true)
                )

                val templates = if (wasAnsweringScreening) {
                    listOf(
                        "I'm so relieved and glad to hear you're doing okay$nameComma. Thank you for being open and letting me know.\n\nI'm right here in your sanctuary whenever you need a listening ear or a quiet pause. What would you like to talk about today?",
                        "Thank you for letting me know$nameComma, that is such a relief to hear. You're never alone here, and there's no pressure to feel any particular way. How is the rest of your day shaping up?",
                        "That is so good to hear$nameComma. I'm always here holding a safe, quiet space for you. What would feel most comforting or interesting to explore right now?"
                    )
                } else {
                    listOf(
                        "I'm so glad to hear you're feeling good$nameComma! It's always a gentle relief when things feel lighter.\n\nI'm right here beside you in the sanctuary—what has brought a little warmth or peace to your day?",
                        "That is wonderful to hear$nameComma. Savor that feeling of ease in your body.\n\nWhether you'd like to share what went well today, explore a thought, or just take an unhurried pause, I'm here listening.",
                        "Hearing that you're doing well brings a smile to my heart$nameComma. Take a calm breath and let that goodness settle in. What's on your mind right now?",
                        "What a lovely moment to witness$nameComma! When ease and contentment arrive, taking thirty seconds to truly feel them in your chest creates an emotional anchor. What is feeling best right now?",
                        "That is wonderful news$nameComma! Celebrate this lightness. You deserve every bit of this peace and comfort. I'm right here with you—tell me more about what's going well!"
                    )
                }
                templates[index % templates.size]
            }

            // --- 2. EXPLICIT REQUESTS FOR HELP ---
            RuleCategory.NEED_HELP -> {
                val templates = listOf(
                    "I am right here with you$nameComma, and I'm ready to help however you need. Take a slow breath and let your shoulders drop.\n\nTell me what's feeling hardest right now. Are you looking for a quiet ear to listen, some gentle grounding, or help sorting through a situation?",
                    "You don't have to carry this alone$nameComma. I'm here to support you in whatever way feels most comforting right now.\n\nTake your time. What is on your mind that you need help with?",
                    "I hear you, and I'm glad you reached out$nameComma. Let's take things one small step at a time.\n\nWhat is the most immediate thing causing you distress or needing attention today?"
                )
                templates[index % templates.size]
            }

            // --- 3. ANXIETY & PANIC ---
            RuleCategory.ANXIOUS -> {
                val templates = listOf(
                    "I hear the fluttery, tense energy in your words$nameComma. When anxiety peaks, our body is preparing to fight a danger that is often just overwhelming thoughts.\n\nLet's do a gentle box breath together: breathe in softly through your nose for 4... hold for 4... exhale smoothly through your mouth for 4... and pause for 4. You are safe in this moment.",
                    "Anxiety feels so urgent, but feelings are like waves that crest and naturally subside$nameComma.\n\nLet's ground your body right now. Feel the solid chair beneath you supporting your weight, take a slow grounding breath, and notice three things in your room you can see. What does your body need most to feel even 5% calmer right now?",
                    "It's completely okay that your nervous system is on high alert$nameComma. Don't fight the feeling or judge yourself for it.\n\nDrop your shoulders down from your ears. Unclench your hands. Take an unhurried, slow grounding breath sighing out. I am right here with you."
                )
                templates[index % templates.size]
            }

            // --- 4. SADNESS & HEARTBREAK & PAIN ---
            RuleCategory.SAD_AND_HURTING -> {
                val templates = listOf(
                    "I'm sitting right beside you in this quiet ache$nameComma. Heavy hearts and tears are not failures—they are proof of how deeply you feel.\n\nYou don't have to put on a brave face here. I am holding unhurried space for whatever feelings need to be felt. What hurts most right now?",
                    "Loneliness and sadness have a hollow weight that feels impossible to explain to the busy world outside. But here in your sanctuary, you are seen and welcomed exactly as you are.\n\nTake a gentle breath. What does this tender feeling wish someone would understand?",
                    "There is nothing you need to fix about how you feel today$nameComma. Softness is its own form of resilience. Take all the time you need—I'm not going anywhere."
                )
                templates[index % templates.size]
            }

            // --- 5. OVERWHELM & STRESS ---
            RuleCategory.OVERWHELMED -> {
                val templates = listOf(
                    "When everything demands your attention at once$nameComma, your nervous system naturally goes into overload. You don't have to solve everything today.\n\nLet's pause right here. Notice your feet touching the ground, and let your jaw loosen. What is the ONE thing we can gently set aside for the next hour?",
                    "I hear how loud and heavy everything feels$nameComma. When we are overwhelmed, our mind tries to run twenty marathons at the same time.\n\nTake one long exhale through your mouth. Can we shrink the world down to just this single minute?",
                    "You've been holding too much for too long$nameComma. Overwhelm is your body's kind way of asking for a boundary.\n\nLet's take a 60-second break from problem-solving. No decisions are required right now. Just breathe with me."
                )
                templates[index % templates.size]
            }

            // --- 6. LONELINESS & ISOLATION ---
            RuleCategory.LONELY -> {
                val templates = listOf(
                    "Loneliness has a quiet, heavy ache that is so hard to put into words$nameComma. Even in a crowded world, feeling unseen hurts deeply.\n\nI want you to know you are truly welcome and valued here. What is that lonely feeling wishing someone would notice about you right now?",
                    "I am right here with you$nameComma. You don't have to pretend you are fine or entertain anyone here.\n\nLoneliness often whispers that we don't matter, but that isn't the truth—it's just the ache of missing connection. How long have you been carrying this feeling?",
                    "Sitting quietly beside you in this moment$nameComma. It takes courage to admit feeling lonely.\n\nTake a slow, grounding breath and let your shoulders drop. What has felt most distant or disconnected lately?"
                )
                templates[index % templates.size]
            }

            // --- 7. BAD DAY ---
            RuleCategory.BAD_DAY -> {
                val templates = listOf(
                    "I'm sorry you had such a difficult day$nameComma. You don't have to carry that weight silently in this sanctuary.\n\nWas there a specific moment that felt heaviest, or was it just one relentless thing after another?",
                    "Some days take everything out of us$nameComma. I hear you, and it's completely valid to feel worn down right now.\n\nTake a slow breath and let your shoulders drop. What part of today is still lingering on your mind?",
                    "Thank you for sharing that with me$nameComma. Days like that can leave us feeling raw and depleted.\n\nIf you'd like to unpack what happened, I'm here. Or if you simply need to rest and not think about it for a while, that is completely okay too."
                )
                templates[index % templates.size]
            }

            // --- 8. EMOTIONALLY EXHAUSTED ---
            RuleCategory.EMOTIONALLY_EXHAUSTED -> {
                val templates = listOf(
                    "Emotional exhaustion is bone-deep$nameComma, and you cannot pour from an empty cup. I won't give you a to-do list or tell you to 'look on the bright side.'\n\nI am simply holding this quiet space for you. You are allowed to rest completely.",
                    "I hear how tired your heart and spirit are$nameComma. You don't have to figure anything out or explain yourself.\n\nClose your eyes for ten seconds, let your hands rest open on your lap, and allow yourself to simply be without performing.",
                    "Carrying emotional burdens without pause drains our deepest reserves$nameComma. There is no advice you need to follow right now.\n\nWhat is one small pressure or expectation you can take off your shoulders for the rest of tonight?"
                )
                templates[index % templates.size]
            }

            // --- 9. ANGER & FRUSTRATION ---
            RuleCategory.ANGRY_AND_FRUSTRATED -> {
                val templates = listOf(
                    "Your frustration makes total sense$nameComma. Anger is often the fierce guardian of a boundary that was crossed or an injustice that stung.\n\nYou have every right to feel this heat. You don't have to swallow it down or pretend it's fine. What part of this feels most unfair or dismissive to you?",
                    "I hear the fire in that$nameComma. When things are unfair or people let us down, it's exhausting to hold in. Let it breathe here in the sanctuary—you don't have to apologize for feeling upset.",
                    "Anger is energy asking to be acknowledged$nameComma. Before doing anything with it, take one long, fierce exhale. What is this feeling trying to protect in you?"
                )
                templates[index % templates.size]
            }

            // --- 10. GUILT & SELF-CRITICISM ---
            RuleCategory.SELF_CRITICAL_AND_GUILT -> {
                val templates = listOf(
                    "Please pause and hear me gently$nameComma: you are speaking to yourself with a harshness you would never use on someone you love.\n\nYou are a human being navigating a complex, demanding life with limited energy. What would happen if you offered yourself the same forgiveness you so readily extend to others?",
                    "Mistakes and imperfect days do not diminish your worth$nameComma. Self-criticism is often an old survival habit trying to keep us in control, but kindness is what actually heals.\n\nTake a slow breath. Can we hold what happened with gentle curiosity instead of shame?",
                    "I see you striving so hard$nameComma. Give the harsh inner judge a seat on the bench. In this sanctuary, you are accepted completely, flaws and all."
                )
                templates[index % templates.size]
            }

            // --- 11. CANNOT CONCENTRATE ---
            RuleCategory.CANNOT_CONCENTRATE -> {
                val templates = listOf(
                    "It's so frustrating when your mind feels scattered and refuses to settle$nameComma. Often, inability to concentrate isn't lack of discipline—it's an emotional backlog asking for attention.\n\nIs your mind racing with worries, or does your brain just feel foggy and fatigued?",
                    "When focus slips away$nameComma, pushing harder usually only increases friction. Let's step back with gentle curiosity.\n\nWhat has been occupying the background of your thoughts today before you sat down to focus?",
                    "A wandering mind is often a tired mind$nameComma. Give yourself permission to step away from the screen or task for five minutes.\n\nDrink a sip of water, stretch your arms, and let your thoughts drift without judging them. What feels most distracting right now?"
                )
                templates[index % templates.size]
            }

            // --- 12. DONT KNOW FEELING ---
            RuleCategory.DONT_KNOW_FEELING -> {
                val templates = listOf(
                    "That is completely okay$nameComma. You don't need a neat label for what you're experiencing. Sometimes emotions arrive as a tangled knot.\n\nIf you check in with your physical body right now, where do you feel the most sensation—in your chest, your stomach, your throat, or your shoulders?",
                    "Not knowing what you feel is a very honest place to start$nameComma. Sometimes our minds go numb when things are too complex to sort out quickly.\n\nDoes it feel more like a quiet numbness, a restless fog, or a heavy stillness?",
                    "We don't have to rush to figure it out$nameComma. Just allowing yourself to feel uncertain without judgment is healing.\n\nTake your time. If that feeling had a texture or a color today, what would come to mind first?"
                )
                templates[index % templates.size]
            }

            // --- 13. JUST WANT LISTENING ---
            RuleCategory.JUST_WANT_LISTENING -> {
                val templates = listOf(
                    "I hear you completely$nameComma. Zero advice, zero solutions, zero pressure. My ears and heart are completely open.\n\nSay whatever you need to say. I am here listening.",
                    "No problem-solving here$nameComma. You have full permission to vent, to be angry, to be sad, or to let out whatever is bottled up.\n\nTake all the space you need. What else is on your chest?",
                    "I am right here, quietly holding space for you$nameComma. You don't need anyone to fix you or tell you what to do. Let it all out."
                )
                templates[index % templates.size]
            }

            // --- 14. NOBODY UNDERSTANDS ---
            RuleCategory.NOBODY_UNDERSTANDS -> {
                val templates = listOf(
                    "That can feel incredibly isolating$nameComma, especially when you've tried explaining yourself and still feel misunderstood. Has something happened recently that's made you feel this way?",
                    "Feeling like nobody truly gets what you're experiencing is such a lonely place to be$nameComma. I am right here listening without judging or trying to fix it. What has felt hardest to explain to people?",
                    "It is deeply exhausting when you feel unseen by the people around you$nameComma. You don't have to put on a mask here. What part of what you're experiencing has felt most misunderstood?"
                )
                templates[index % templates.size]
            }

            // --- 15. GREETING ---
            RuleCategory.GREETING -> {
                val templates = listOf(
                    "Hello$nameComma. Welcome into your sanctuary. Take a gentle breath—there is no rush here.\n\nHow are you feeling in this moment?",
                    "Hi$nameComma. I'm glad you stopped by today. This is your safe space to unpack, reflect, or simply rest.\n\nWhat is on your mind right now?",
                    "Warm greetings$nameComma. Settle into your seat and let the day's noise fade for a moment. How can I support you today?"
                )
                templates[index % templates.size]
            }

            // --- 16. GRATITUDE ---
            RuleCategory.GRATITUDE -> {
                val templates = listOf(
                    "You are so very welcome$nameComma. It is an honor to share this space with you. Remember that taking time for yourself is a gift.\n\nIs there anything else you'd like to share or reflect on?",
                    "Thank you for sharing your thoughts with me$nameComma. Your openness makes this sanctuary meaningful. I'm always here whenever you need a pause.",
                    "I'm glad that brought a moment of ease$nameComma. You deserve care and compassion. How are you feeling now?"
                )
                templates[index % templates.size]
            }

            // --- 17. CHECK-IN QUESTION ---
            RuleCategory.CHECK_IN_QUESTION -> {
                listOf(
                    "I am Solace$nameComma—your private, rule-based emotional sanctuary companion.\n\nI'm designed to offer a calm, non-judgmental holding space: validating your feelings, helping you regulate during stressful moments, and celebrating the good times when things feel lighter. How can I be of comfort to you right now?",
                    "I'm right here beside you, fully present and attentive$nameComma. As your Solace companion, I listen without judgment, offer somatic grounding cues, and help you find steady ground. What's on your mind today?"
                )[index % 2]
            }

            // --- 18. GENERAL HOLDING SPACE (FALLBACK) ---
            RuleCategory.GENERAL_HOLDING_SPACE -> {
                val templates = listOf(
                    "I hear you$nameComma. I am listening with an open heart.\n\nCould you tell me a little more about what's behind that, or what you're experiencing right now?",
                    "Thank you for sharing that with me$nameComma. In this sanctuary, every thought and feeling is welcome without judgment.\n\nWhat feels most important about this for you today?",
                    "I am right here beside you$nameComma. Take all the time and space you need to unpack that.\n\nHow does that make you feel when you sit with it?",
                    "I'm tuned in and listening closely$nameComma. Tell me more about what's going on—I'm here with you."
                )
                templates[index % templates.size]
            }
        }
    }
}
