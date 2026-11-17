package com.example.data.safety

import java.util.Locale

/**
 * Solace+ Deterministic Emotional Classification Engine.
 *
 * Classifies incoming messages across 32 emotional categories using
 * deterministic regular expressions, synonym lexicons, contextual negation analysis,
 * metaphorical idiom disambiguation, entity/subject tracking, and intensity scoring.
 *
 * Guarantees 100% offline, local sovereign execution with zero cloud dependency.
 */
object EmotionalClassificationEngine {

    enum class EmotionCategory(val displayName: String, val isCrisis: Boolean = false) {
        // High Risk / Safety Categories
        IMMEDIATE_PHYSICAL_DANGER("Immediate Physical Danger", true),
        SUICIDAL_IDEATION("Suicidal Ideation", true),
        SELF_HARM("Self-Harm References", true),

        // Core Emotional Distress
        PANIC("Panic"),
        ANXIETY("Anxiety"),
        OVERTHINKING("Overthinking"),
        SADNESS("Sadness"),
        LONELINESS("Loneliness"),
        DEPRESSION_DISTRESS("Depression-Related Distress"),
        GRIEF("Grief"),
        EMOTIONAL_NUMBNESS("Emotional Numbness"),
        EMOTIONAL_EXHAUSTION("Emotional Exhaustion"),
        BURNOUT("Burnout"),

        // Situational & Life Stress
        ACADEMIC_STRESS("Academic Stress"),
        WORK_RELATED_STRESS("Work-Related Stress"),
        RELATIONSHIP_PROBLEMS("Relationship Problems"),
        HEARTBREAK("Heartbreak"),
        REJECTION("Rejection"),
        FAMILY_CONFLICT("Family Conflict"),
        SOCIAL_ANXIETY("Social Anxiety"),

        // Reactive & Self-Evaluative Emotions
        ANGER("Anger"),
        FRUSTRATION("Frustration"),
        LOW_SELF_ESTEEM("Low Self-Esteem"),
        INSECURITY("Insecurity"),
        FEAR("Fear"),
        GUILT("Guilt"),
        SHAME("ShAME"),
        CONFUSION("Confusion"),

        // Behavioral & Somatic States
        SLEEP_DIFFICULTIES("Sleep Difficulties"),
        PROCRASTINATION("Procrastination"),
        LACK_OF_MOTIVATION("Lack of Motivation"),

        // Well-Being & Positive
        POSITIVE_EMOTIONS("Positive Emotions")
    }

    enum class EmotionIntensity {
        MILD,
        MODERATE,
        HIGH,
        CRITICAL
    }

    data class ClassificationResult(
        val primaryCategory: EmotionCategory?,
        val secondaryCategories: List<EmotionCategory> = emptyList(),
        val intensity: EmotionIntensity = EmotionIntensity.MODERATE,
        val confidence: Float = 0f,
        val isNegated: Boolean = false,
        val isMetaphorical: Boolean = false,
        val isThirdPersonOrObject: Boolean = false,
        val requiresClarification: Boolean = false,
        val matchedKeywords: List<String> = emptyList(),
        val rawInput: String = ""
    )

    // =========================================================================
    // 1. METAPHORICAL & IDIOMATIC PATTERNS (Must be disambiguated before safety)
    // =========================================================================
    private val metaphoricalPatterns = listOf(
        Regex("""\b(killed\s+(that|the|my|this)\s+(exam|test|interview|presentation|audition|workout|set|game))\b""", RegexOption.IGNORE_CASE) to EmotionCategory.POSITIVE_EMOTIONS,
        Regex("""\b(killing\s+it|crushing\s+it|slaying\s+it|nailed\s+it)\b""", RegexOption.IGNORE_CASE) to EmotionCategory.POSITIVE_EMOTIONS,
        Regex("""\b(dying\s+(to\s+see|to\s+know|to\s+hear|to\s+meet|to\s+go|to\s+watch|to\s+read|to\s+try|of\s+laughter|laughing))\b""", RegexOption.IGNORE_CASE) to EmotionCategory.POSITIVE_EMOTIONS,
        Regex("""\b(to\s+die\s+for)\b""", RegexOption.IGNORE_CASE) to EmotionCategory.POSITIVE_EMOTIONS,
        Regex("""\b(dead\s+tired|dead\s+exhausted|half\s+dead\s+from\s+work)\b""", RegexOption.IGNORE_CASE) to EmotionCategory.EMOTIONAL_EXHAUSTION,
        Regex("""\b(this\s+(exam|homework|assignment|project|job|work|shift|code|bug)\s+is\s+killing\s+me)\b""", RegexOption.IGNORE_CASE) to EmotionCategory.ACADEMIC_STRESS,
        Regex("""\b(my\s+(feet|back|head|neck|shoulder|body)\s+(is|are)\s+killing\s+me)\b""", RegexOption.IGNORE_CASE) to EmotionCategory.EMOTIONAL_EXHAUSTION
    )

    // =========================================================================
    // 2. THIRD-PERSON & EXTERNAL OBJECT DISAMBIGUATION
    // =========================================================================
    private val externalObjectPattern = Regex(
        """\b(that|this|the)\s+(movie|film|book|novel|show|episode|series|story|documentary|weather|song|album|news|article)\s+(is|was|feels|seemed)\s+(so\s+)?(depressing|sad|heartbreaking|awful|dark)\b""",
        RegexOption.IGNORE_CASE
    )

    private val thirdPersonSubjectPattern = Regex(
        """\b(he|she|they|my\s+friend|my\s+brother|my\s+sister|my\s+mom|my\s+dad|my\s+cousin|my\s+coworker|my\s+boss|someone|people)\s+(is|was|feels|felt|were|are)\s+(so\s+)?(sad|depressed|angry|crying|anxious|hurting)\b""",
        RegexOption.IGNORE_CASE
    )

    // =========================================================================
    // 3. NEGATION PATTERNS
    // =========================================================================
    private val negationPrefixPattern = Regex(
        """\b(don'?t|do\s+not|not|never|didn'?t|did\s+not|won'?t|will\s+not|cannot|can'?t|ain'?t|no\s+longer|haven'?t|have\s+not|without)\s+([a-z\s]{1,25})""",
        RegexOption.IGNORE_CASE
    )

    private val negatedDeathIdeation = Regex(
        """\b(i\s+)?(don'?t|do\s+not|never)\s+(want\s+to|wanna|plan\s+to)\s+(die|end\s+my\s+life|kill\s+myself|hurt\s+myself)\b""",
        RegexOption.IGNORE_CASE
    )

    // =========================================================================
    // 4. INTENSITY MODIFIERS
    // =========================================================================
    private val highIntensityPattern = Regex(
        """\b(unbearable|extremely|completely|utterly|severely|intensely|cannot\s+take\s+it|can'?t\s+take\s+this|desperately|overwhelmingly|falling\s+apart|paralyzed|shaking|heart\s+pounding|can'?t\s+breathe)\b""",
        RegexOption.IGNORE_CASE
    )

    private val mildIntensityPattern = Regex(
        """\b(a\s+little|a\s+bit|slightly|somewhat|mildly|a\s+tiny\s+bit|kind\s+of|sort\s+of)\b""",
        RegexOption.IGNORE_CASE
    )

    // =========================================================================
    // 5. DETERMINISTIC CATEGORY REGEX LEXICONS (32 Categories)
    // =========================================================================

    // High Risk
    private val dangerRegex = Regex(
        """\b(someone\s+is\s+(hurting|attacking|following)\s+me|in\s+immediate\s+danger|imminent\s+danger|someone\s+has\s+a\s+weapon|abusing\s+me|threatened\s+my\s+life|call\s+the\s+police)\b""",
        RegexOption.IGNORE_CASE
    )

    private val suicideIdeationRegex = Regex(
        """\b(want\s+to\s+die|wanna\s+die|kill\s+myself|killing\s+myself|end\s+my\s+life|ending\s+my\s+life|take\s+my\s+own\s+life|commit\s+suicide|suicidal|wish\s+i\s+(was|were)\s+dead|rather\s+be\s+dead|better\s+off\s+dead|don'?t\s+want\s+to\s+wake\s+up|never\s+wake\s+up|ready\s+to\s+die|no\s+reason\s+to\s+live|end\s+it\s+all|ending\s+it\s+all|su1cide|kms)\b""",
        RegexOption.IGNORE_CASE
    )

    private val selfHarmRegex = Regex(
        """\b(cut\s+myself|cutting\s+myself|harm\s+myself|harming\s+myself|hurt\s+myself|hurting\s+myself|self[-\s]?harm|burn\s+myself|slit\s+my\s+wrist|overdose|take\s+all\s+my\s+pills)\b""",
        RegexOption.IGNORE_CASE
    )

    // Core Emotional Distress
    private val panicRegex = Regex(
        """\b(panic\s+attack|panicking|panicked|having\s+a\s+panic|hyperventilat(ing|ion)|heart\s+racing|heart\s+is\s+pounding|chest\s+tightness|can'?t\s+catch\s+my\s+breath|freaking\s+out|terrified)\b""",
        RegexOption.IGNORE_CASE
    )

    private val anxietyRegex = Regex(
        """\b(anxious|anxiety|nervous|dread|uneasy|on\s+edge|worrying|worried|restless|butterflies\s+in\s+my\s+stomach|fearful|shaking\s+inside|jittery)\b""",
        RegexOption.IGNORE_CASE
    )

    private val overthinkingRegex = Regex(
        """\b(overthinking|overthink|mind\s+won'?t\s+stop|racing\s+thoughts|thoughts\s+racing|can'?t\s+stop\s+thinking|analyzing\s+everything|replaying\s+(everything|it|thoughts)|spiraling|head\s+is\s+spinning|mental\s+loop)\b""",
        RegexOption.IGNORE_CASE
    )

    private val sadnessRegex = Regex(
        """\b(feel(ing)?\s+sad|so\s+sad|sadness|crying|cried|in\s+tears|weeping|heavy\s+heart|feeling\s+blue|gloomy|unhappy|downhearted|sorrowful|feel(ing)?\s+(really\s+|so\s+)?(low|down)|bad\s+day|rough\s+day|terrible\s+day|awful\s+day|horrible\s+day|today\s+sucked|worst\s+day)\b""",
        RegexOption.IGNORE_CASE
    )

    private val lonelinessRegex = Regex(
        """\b(lonely|loneliness|alone|all\s+alone|feeling\s+isolated|isolated|isolation|nobody\s+cares|no\s+one\s+cares|have\s+no\s+friends|no\s+one\s+to\s+talk\s+to|left\s+out|abandoned|by\s+myself|unwanted)\b""",
        RegexOption.IGNORE_CASE
    )

    private val depressionDistressRegex = Regex(
        """\b(depressed|depression|hopeless|hopelessness|empty\s+inside|feel(ing)?\s+empty|what'?s\s+the\s+point|nothing\s+matters|feel\s+like\s+nothing|dark\s+place|deep\s+dark\s+hole|miserable|despair)\b""",
        RegexOption.IGNORE_CASE
    )

    private val griefRegex = Regex(
        """\b(grief|grieving|mourning|loss\s+of\s+my|passed\s+away|died|loss\s+of\s+a\s+loved\s+one|bereavement|miss\s+(him|her|them)\s+so\s+much)\b""",
        RegexOption.IGNORE_CASE
    )

    private val emotionalNumbnessRegex = Regex(
        """\b(feel(ing)?\s+numb|emotional(ly)?\s+numb|blank\s+inside|can'?t\s+feel\s+anything|detached|dissociated|dissociating|robotic|feel\s+like\s+a\s+ghost|hollow)\b""",
        RegexOption.IGNORE_CASE
    )

    private val emotionalExhaustionRegex = Regex(
        """\b(emotionally\s+exhausted|emotionally\s+drained|no\s+energy\s+left|tired\s+of\s+everything|tired\s+of\s+trying|worn\s+out|spent|completely\s+drained|running\s+on\s+empty)\b""",
        RegexOption.IGNORE_CASE
    )

    private val burnoutRegex = Regex(
        """\b(burnout|burning\s+out|burned\s+out|overworked|pushed\s+to\s+the\s+limit|reached\s+my\s+limit|can'?t\s+keep\s+doing\s+this|exhaustion\s+from\s+work|work\s+overload)\b""",
        RegexOption.IGNORE_CASE
    )

    // Situational Stress
    private val academicStressRegex = Regex(
        """\b(fail(ing)?\s+(my\s+)?(exam|test|class|semester|course|school|college)s?|exam\s+stress|finals\s+week|grades\s+are\s+slipping|studying\s+all\s+night|syllabus|assignment\s+due|homework|gpa|flunking|(overwhelmed|stressed|anxious|worried)\s+(with|by|about)\s+(my\s+)?(exams?|finals|tests?)|(exam|finals)\s+pressure)\b""",
        RegexOption.IGNORE_CASE
    )

    private val workStressRegex = Regex(
        """\b(boss\s+is|toxic\s+workplace|work\s+stress|job\s+stress|fired|getting\s+fired|layoff|laid\s+off|deadline\s+is\s+tomorrow|workload|working\s+12\s+hours|career\s+pressure)\b""",
        RegexOption.IGNORE_CASE
    )

    private val relationshipProblemsRegex = Regex(
        """\b(fight\s+with\s+my\s+(partner|boyfriend|girlfriend|husband|wife)|relationship\s+problems|drifting\s+apart|we\s+keep\s+fighting|partner\s+doesn'?t\s+understand|cheating|trust\s+issues|toxic\s+relationship)\b""",
        RegexOption.IGNORE_CASE
    )

    private val heartbreakRegex = Regex(
        """\b((going\s+through\s+a\s+|gng\s+thru\s+(a\s+)?)?break\s*up|broke\s+up|broken\s+up|breaking\s+up|heartbroken|heartbreak|dumped(\s+me)?|ended\s+(things|our\s+relationship|it)|they\s+left\s+me|she\s+left\s+me|he\s+left\s+me|shattered\s+my\s+heart|still\s+love\s+my\s+ex|miss\s+(my\s+ex|her|him|them)|still\s+miss(\s+(her|him|them|my\s+ex))?|want\s+(her|him|them)\s+back|split\s+up|separated|parted\s+ways|called\s+it\s+off|cut\s+ties)\b""",
        RegexOption.IGNORE_CASE
    )

    private val exSocialMediaRegex = Regex(
        """\b(check(ing)?\s+(her|his|their|my\s+ex['\s]?s?)\s+(instagram|insta|profile|stories|story|posts|feed|account|activity)|stalk(ing)?\s+(her|his|their|my\s+ex)|look(ing)?\s+at\s+(her|his|their|our)\s+(profile|photos|pictures|stories|old\s+texts|old\s+chats)|reread(ing)?\s+(our|old)\s+(texts|messages|conversations))\b""",
        RegexOption.IGNORE_CASE
    )

    private val rejectionRegex = Regex(
        """\b(rejected|rejection|got\s+rejected|they\s+said\s+no|nobody\s+wants\s+me|turned\s+down|didn'?t\s+get\s+picked|ghosted|they\s+ghosted\s+me)\b""",
        RegexOption.IGNORE_CASE
    )

    private val familyConflictRegex = Regex(
        """\b(parents\s+(are\s+fighting|yelling|screaming)|family\s+drama|fight\s+with\s+my\s+(parents|mom|dad|mother|father|family)|strict\s+parents|family\s+pressure|unsupportive\s+family|family\s+dismiss(es|ed|ing)?|(parents|family)\s+dismiss(es|ed|ing)?(\s+my\s+feelings)?|unseen|controlled\s+by\s+my\s+parents|feeling\s+unseen)\b""",
        RegexOption.IGNORE_CASE
    )

    private val socialAnxietyRegex = Regex(
        """\b(social\s+anxiety|talking\s+to\s+people|scared\s+of\s+crowds|judged\s+by\s+others|people\s+staring\s+at\s+me|awkward\s+in\s+public|embarrassed\s+myself|fear\s+of\s+speaking|introvert\s+panic)\b""",
        RegexOption.IGNORE_CASE
    )

    // Reactive & Self-Evaluative
    private val angerRegex = Regex(
        """\b(angry|rage|furious|mad|pissed\s+off|livid|boiling\s+with\s+anger|want\s+to\s+scream|lost\s+my\s+temper|screaming\s+inside|so\s+mad)\b""",
        RegexOption.IGNORE_CASE
    )

    private val frustrationRegex = Regex(
        """\b(frustrated|frustration|irritated|annoyed|aggravated|sick\s+and\s+tired|nothing\s+works|fed\s+up|at\s+my\s+wit'?s\s+end)\b""",
        RegexOption.IGNORE_CASE
    )

    private val lowSelfEsteemRegex = Regex(
        """\b(worthless|hate\s+myself|disgusted\s+with\s+myself|feel\s+like\s+a\s+failure|i'?m\s+a\s+loser|i'?m\s+so\s+stupid|never\s+good\s+enough|useless|pathetic|deserve\s+bad\s+things)\b""",
        RegexOption.IGNORE_CASE
    )

    private val insecurityRegex = Regex(
        """\b(insecure|insecurity|comparing\s+myself|not\s+attractive|ugly|feel\s+inadequate|imposter\s+syndrome|everyone\s+is\s+better\s+than\s+me|doubt\s+myself)\b""",
        RegexOption.IGNORE_CASE
    )

    private val fearRegex = Regex(
        """\b(scared|terrified|fearful|fear\s+of\s+the\s+future|dreading|horrified|afraid\s+of\s+what'?s\s+next|scared\s+of\s+failing|paralyzed\s+with\s+fear)\b""",
        RegexOption.IGNORE_CASE
    )

    private val guiltRegex = Regex(
        """\b(guilty|guilt|feel\s+so\s+guilty|my\s+fault|all\s+my\s+fault|blame\s+myself|i\s+ruined\s+everything|i\s+messed\s+everything\s+up|should\s+have\s+done\s+better)\b""",
        RegexOption.IGNORE_CASE
    )

    private val shameRegex = Regex(
        """\b(ashamed|shame|humiliated|so\s+embarrassed|can'?t\s+look\s+at\s+myself|deep\s+shame|mortified|hide\s+my\s+face)\b""",
        RegexOption.IGNORE_CASE
    )

    private val confusionRegex = Regex(
        """\b(confused|don'?t\s+know\s+what\s+to\s+do|lost|don'?t\s+understand\s+myself|mixed\s+feelings|torn\s+between|head\s+is\s+clouded|indecisive|can'?t\s+make\s+up\s+my\s+mind)\b""",
        RegexOption.IGNORE_CASE
    )

    // Somatic & Behavioral
    private val sleepDifficultiesRegex = Regex(
        """\b(can'?t\s+sleep|cannot\s+sleep|insomnia|sleepless|waking\s+up\s+at\s+night|tossing\s+and\s+turning|haven'?t\s+slept|nightmares|stayed\s+awake\s+all\s+night|sleep\s+deprived)\b""",
        RegexOption.IGNORE_CASE
    )

    private val procrastinationRegex = Regex(
        """\b(procrastinat(ing|ion)|putting\s+it\s+off|avoiding\s+my\s+work|delaying\s+everything|wasting\s+time|can'?t\s+get\s+myself\s+to\s+start|doomscrolling)\b""",
        RegexOption.IGNORE_CASE
    )

    private val lackOfMotivationRegex = Regex(
        """\b(no\s+motivation|lack\s+of\s+motivation|unmotivated|can'?t\s+bring\s+myself\s+to|don'?t\s+care\s+anymore|no\s+drive|apathy|apathetic|lost\s+my\s+spark)\b""",
        RegexOption.IGNORE_CASE
    )

    // Positive Emotions
    private val positiveEmotionsRegex = Regex(
        """\b(happy|joyful|peaceful|grateful|relieved|doing\s+well|feeling\s+good|proud\s+of\s+myself|hopeful|excited|wonderful|content|blessed|optimistic|celebrating)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Primary Classifier Entry Point.
     * Evaluates incoming message through the deterministic NLP disambiguation pipeline.
     */
    fun classify(
        message: String,
        history: List<Pair<String, String>> = emptyList()
    ): ClassificationResult {
        val trimmed = message.trim()
        if (trimmed.isEmpty()) {
            return ClassificationResult(
                primaryCategory = null,
                confidence = 0f,
                rawInput = message
            )
        }

        // --- STEP 1: METAPHOR & IDIOM DETECTION ---
        for ((pattern, category) in metaphoricalPatterns) {
            if (pattern.containsMatchIn(trimmed)) {
                return ClassificationResult(
                    primaryCategory = category,
                    confidence = 0.95f,
                    isMetaphorical = true,
                    matchedKeywords = listOf(pattern.pattern),
                    rawInput = message
                )
            }
        }

        // --- STEP 2: EXTERNAL OBJECT / MEDIA / 3RD PERSON DISAMBIGUATION ---
        if (externalObjectPattern.containsMatchIn(trimmed)) {
            return ClassificationResult(
                primaryCategory = EmotionCategory.CONFUSION,
                confidence = 0.85f,
                isThirdPersonOrObject = true,
                rawInput = message
            )
        }

        // --- STEP 3: NEGATION ANALYSIS ---
        val hasNegatedDeath = negatedDeathIdeation.containsMatchIn(trimmed)
        if (hasNegatedDeath) {
            // User explicitly stated "I don't want to die" or "I don't want to kill myself"
            // This is NOT affirmative suicidal ideation, but reflects emotional distress or relief.
            return ClassificationResult(
                primaryCategory = EmotionCategory.DEPRESSION_DISTRESS,
                intensity = EmotionIntensity.MODERATE,
                confidence = 0.88f,
                isNegated = true,
                matchedKeywords = listOf("negated_death_statement"),
                rawInput = message
            )
        }

        // --- STEP 4: INTENSITY ESTIMATION ---
        val intensity = when {
            highIntensityPattern.containsMatchIn(trimmed) -> EmotionIntensity.HIGH
            mildIntensityPattern.containsMatchIn(trimmed) -> EmotionIntensity.MILD
            else -> EmotionIntensity.MODERATE
        }

        // --- STEP 5: SAFETY CRISIS OVERRIDE CHECK ---
        if (dangerRegex.containsMatchIn(trimmed)) {
            return ClassificationResult(
                primaryCategory = EmotionCategory.IMMEDIATE_PHYSICAL_DANGER,
                intensity = EmotionIntensity.CRITICAL,
                confidence = 0.99f,
                matchedKeywords = listOf("immediate_physical_danger"),
                rawInput = message
            )
        }
        if (suicideIdeationRegex.containsMatchIn(trimmed)) {
            return ClassificationResult(
                primaryCategory = EmotionCategory.SUICIDAL_IDEATION,
                intensity = EmotionIntensity.CRITICAL,
                confidence = 0.99f,
                matchedKeywords = listOf("suicidal_ideation"),
                rawInput = message
            )
        }
        if (selfHarmRegex.containsMatchIn(trimmed)) {
            return ClassificationResult(
                primaryCategory = EmotionCategory.SELF_HARM,
                intensity = EmotionIntensity.CRITICAL,
                confidence = 0.98f,
                matchedKeywords = listOf("self_harm"),
                rawInput = message
            )
        }

        // --- STEP 6: DETERMINISTIC MULTI-CATEGORY SCORING ---
        val matchedCategories = mutableListOf<Pair<EmotionCategory, Float>>()

        fun test(category: EmotionCategory, regex: Regex, weight: Float = 1.0f) {
            if (regex.containsMatchIn(trimmed)) {
                matchedCategories.add(category to weight)
            }
        }

        test(EmotionCategory.PANIC, panicRegex, 1.2f)
        test(EmotionCategory.ANXIETY, anxietyRegex, 1.0f)
        test(EmotionCategory.OVERTHINKING, overthinkingRegex, 1.1f)
        test(EmotionCategory.SADNESS, sadnessRegex, 1.0f)
        test(EmotionCategory.LONELINESS, lonelinessRegex, 1.1f)
        test(EmotionCategory.DEPRESSION_DISTRESS, depressionDistressRegex, 1.1f)
        test(EmotionCategory.GRIEF, griefRegex, 1.3f)
        test(EmotionCategory.EMOTIONAL_NUMBNESS, emotionalNumbnessRegex, 1.1f)
        test(EmotionCategory.EMOTIONAL_EXHAUSTION, emotionalExhaustionRegex, 1.0f)
        test(EmotionCategory.BURNOUT, burnoutRegex, 1.1f)
        test(EmotionCategory.ACADEMIC_STRESS, academicStressRegex, 1.2f)
        test(EmotionCategory.WORK_RELATED_STRESS, workStressRegex, 1.2f)
        test(EmotionCategory.RELATIONSHIP_PROBLEMS, relationshipProblemsRegex, 1.1f)
        test(EmotionCategory.HEARTBREAK, heartbreakRegex, 1.2f)
        test(EmotionCategory.REJECTION, rejectionRegex, 1.1f)
        test(EmotionCategory.FAMILY_CONFLICT, familyConflictRegex, 1.2f)
        test(EmotionCategory.SOCIAL_ANXIETY, socialAnxietyRegex, 1.1f)
        test(EmotionCategory.ANGER, angerRegex, 1.0f)
        test(EmotionCategory.FRUSTRATION, frustrationRegex, 1.0f)
        test(EmotionCategory.LOW_SELF_ESTEEM, lowSelfEsteemRegex, 1.2f)
        test(EmotionCategory.INSECURITY, insecurityRegex, 1.0f)
        test(EmotionCategory.FEAR, fearRegex, 1.0f)
        test(EmotionCategory.GUILT, guiltRegex, 1.1f)
        test(EmotionCategory.SHAME, shameRegex, 1.1f)
        test(EmotionCategory.CONFUSION, confusionRegex, 0.9f)
        test(EmotionCategory.SLEEP_DIFFICULTIES, sleepDifficultiesRegex, 1.2f)
        test(EmotionCategory.PROCRASTINATION, procrastinationRegex, 1.1f)
        test(EmotionCategory.LACK_OF_MOTIVATION, lackOfMotivationRegex, 1.1f)
        test(EmotionCategory.OVERTHINKING, exSocialMediaRegex, 1.35f)
        test(EmotionCategory.POSITIVE_EMOTIONS, positiveEmotionsRegex, 0.95f)

        if (matchedCategories.isEmpty()) {
            // Check situational context recognizer before treating as unknown
            val situationResult = SituationalContextEngine.detectSituation(trimmed)
            if (situationResult.primarySituation != null) {
                val inferredCategory = when (situationResult.primarySituation) {
                    UserSituation.BREAKUP,
                    UserSituation.HEARTBREAK,
                    UserSituation.BEING_CHEATED_ON,
                    UserSituation.RELATIONSHIP_ARGUMENTS,
                    UserSituation.BEING_IGNORED,
                    UserSituation.BEING_REJECTED,
                    UserSituation.LONG_DISTANCE,
                    UserSituation.FEAR_OF_LOSING_SOMEONE,
                    UserSituation.FEELING_UNWANTED,
                    UserSituation.FRIENDSHIP_DRIFT,
                    UserSituation.ONE_SIDED_FRIENDSHIP -> EmotionCategory.RELATIONSHIP_PROBLEMS

                    UserSituation.MISSING_EX -> EmotionCategory.SADNESS
                    UserSituation.EX_SOCIAL_MEDIA_CHECKING -> EmotionCategory.OVERTHINKING

                    UserSituation.EXAM_FAILURE,
                    UserSituation.POOR_GRADES,
                    UserSituation.ASSIGNMENT_PRESSURE,
                    UserSituation.DISAPPOINTING_PARENTS,
                    UserSituation.DIFFICULTY_CONCENTRATING,
                    UserSituation.ACADEMIC_COMPARISON,
                    UserSituation.UNSUCCESSFUL_EFFORT -> EmotionCategory.ACADEMIC_STRESS

                    UserSituation.LOSING_FRIENDS,
                    UserSituation.FEELING_EXCLUDED,
                    UserSituation.SOCIAL_REJECTION,
                    UserSituation.THIRD_WHEEL -> EmotionCategory.LONELINESS

                    UserSituation.BEING_JUDGED,
                    UserSituation.FRIENDSHIP_CONFLICTS -> EmotionCategory.SOCIAL_ANXIETY

                    UserSituation.FAMILY_CONFLICTS,
                    UserSituation.PARENTAL_CAREER_PRESSURE,
                    UserSituation.SIBLING_COMPARISON -> EmotionCategory.FAMILY_CONFLICT

                    UserSituation.FINANCIAL_PRESSURE,
                    UserSituation.CAREER_UNCERTAINTY,
                    UserSituation.DIRECTIONLESS,
                    UserSituation.FEAR_WRONG_DEGREE -> EmotionCategory.ANXIETY

                    UserSituation.LOSS_OF_LOVED_ONE -> EmotionCategory.GRIEF

                    UserSituation.PERSONAL_DISAPPOINTMENT,
                    UserSituation.PEER_ACHIEVEMENT_ENVY,
                    UserSituation.SOCIAL_MEDIA_ENVY -> EmotionCategory.LOW_SELF_ESTEEM

                    // Batch 2: Scenario 6 (Misunderstood and Unheard)
                    UserSituation.PERSPECTIVE_DISMISSED,
                    UserSituation.LABELED_OVERLY_SENSITIVE,
                    UserSituation.COMMUNICATION_BREAKDOWN -> EmotionCategory.FRUSTRATION

                    // Batch 2: Scenario 7 (Regret and Guilt)
                    UserSituation.PAST_DECISION_REGRET,
                    UserSituation.HURT_SOMEONE,
                    UserSituation.SELF_BLAME -> EmotionCategory.GUILT

                    // Batch 2: Scenario 8 (Sleep Difficulties & Night Rumination)
                    UserSituation.CANNOT_SLEEP_INSOMNIA,
                    UserSituation.NIGHT_RUMINATION,
                    UserSituation.LATE_NIGHT_LONELINESS -> EmotionCategory.SLEEP_DIFFICULTIES

                    // Batch 2: Scenario 9 (Social Anxiety & Fear of Judgement)
                    UserSituation.MEETING_NEW_PEOPLE,
                    UserSituation.POST_EVENT_RUMINATION,
                    UserSituation.FEELING_AWKWARD,
                    UserSituation.PUBLIC_SPEAKING_FEAR -> EmotionCategory.SOCIAL_ANXIETY

                    // Batch 2: Scenario 10 (Procrastination & Productivity Distress)
                    UserSituation.PHONE_DOOMSCROLLING,
                    UserSituation.TASK_OVERWHELM,
                    UserSituation.AVOIDING_TASKS -> EmotionCategory.PROCRASTINATION

                    // Batch 3: Scenario 11 (Emotional Exhaustion & Burnout)
                    UserSituation.MENTAL_DRAIN,
                    UserSituation.ANHEDONIA_LOSS_OF_INTEREST,
                    UserSituation.RESPONSIBILITY_OVERWHELM,
                    UserSituation.WITHDRAWAL_URGE -> EmotionCategory.BURNOUT

                    // Batch 3: Scenario 12 (Financial Stress & Dependence)
                    UserSituation.EXPENSE_MANAGEMENT,
                    UserSituation.PARENTAL_FINANCIAL_DEPENDENCE,
                    UserSituation.PEER_WEALTH_COMPARISON -> EmotionCategory.ANXIETY

                    // Batch 3: Scenario 13 (Life Transitions & Unfamiliar Environments)
                    UserSituation.COLLEGE_START,
                    UserSituation.RELOCATING_NEW_CITY,
                    UserSituation.ROUTINE_LOSS,
                    UserSituation.NOSTALGIA_PAST -> EmotionCategory.ANXIETY

                    // Batch 3: Scenario 14 (Interpersonal Conflict & Boundary Setting)
                    UserSituation.HEATED_ARGUMENT,
                    UserSituation.DISRESPECT_FELT -> EmotionCategory.ANGER

                    // Batch 3: Scenario 15 (Rejection & Unreciprocated Feelings)
                    UserSituation.ROMANTIC_REJECTION,
                    UserSituation.UNRECIPROCATED_FEELINGS,
                    UserSituation.FEELING_UNWANTED_AFTER_NO -> EmotionCategory.REJECTION

                    // Batch 4: Scenario 16 (Grief and Loss)
                    UserSituation.PET_LOSS,
                    UserSituation.ANNIVERSARY_GRIEF,
                    UserSituation.ABSENCE_LONGING -> EmotionCategory.GRIEF

                    // Batch 4: Scenario 17 (Body Image & Self-Confidence)
                    UserSituation.APPEARANCE_INSECURITY,
                    UserSituation.WEIGHT_DISSATISFACTION,
                    UserSituation.PHOTO_ANXIETY -> EmotionCategory.INSECURITY

                    // Batch 4: Scenario 18 (Positive Emotions & Celebration)
                    UserSituation.ACADEMIC_ACHIEVEMENT,
                    UserSituation.PERSONAL_GOAL_REACHED,
                    UserSituation.GENERAL_GRATITUDE -> EmotionCategory.POSITIVE_EMOTIONS

                    // Batch 4: Scenario 19 (Everyday Frustration & Casual Venting)
                    UserSituation.BAD_DAY,
                    UserSituation.IRRITATED_SMALL_THINGS,
                    UserSituation.CASUAL_VENTING -> EmotionCategory.FRUSTRATION

                    // Batch 4: Scenario 20 (Romantic Relationship Support)
                    UserSituation.RELATIONSHIP_INSECURITY,
                    UserSituation.PARTNER_DISTANCING,
                    UserSituation.FEAR_OF_ABANDONMENT,
                    UserSituation.JEALOUSY -> EmotionCategory.RELATIONSHIP_PROBLEMS
                }
                return ClassificationResult(
                    primaryCategory = inferredCategory,
                    intensity = intensity,
                    confidence = 0.93f,
                    matchedKeywords = listOf(situationResult.primarySituation.name),
                    rawInput = message
                )
            }

            // Check if 3rd person was talking about someone else
            val isThirdPerson = thirdPersonSubjectPattern.containsMatchIn(trimmed)
            return ClassificationResult(
                primaryCategory = null,
                intensity = intensity,
                confidence = 0.3f,
                isThirdPersonOrObject = isThirdPerson,
                requiresClarification = true,
                rawInput = message
            )
        }

        // Sort matches by weight descending
        matchedCategories.sortByDescending { it.second }
        val primary = matchedCategories.first().first
        val secondary = matchedCategories.drop(1).map { it.first }

        return ClassificationResult(
            primaryCategory = primary,
            secondaryCategories = secondary,
            intensity = intensity,
            confidence = 0.92f,
            matchedKeywords = matchedCategories.map { it.first.name },
            rawInput = message
        )
    }
}
