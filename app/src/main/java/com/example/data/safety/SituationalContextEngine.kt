package com.example.data.safety

/**
 * Solace+ Situational Context & Trigger Recognition Engine.
 *
 * Dedicated recognizer for underlying situations and life triggers,
 * allowing the conversational engine to distinguish underlying life events
 * from surface-level emotional states.
 */
enum class UserSituation(val group: SituationGroup, val displayName: String) {
    // Relationships
    BREAKUP(SituationGroup.RELATIONSHIPS, "Breakup"),
    HEARTBREAK(SituationGroup.RELATIONSHIPS, "Heartbreak"),
    BEING_CHEATED_ON(SituationGroup.RELATIONSHIPS, "Being Cheated On"),
    RELATIONSHIP_ARGUMENTS(SituationGroup.RELATIONSHIPS, "Relationship Arguments"),
    BEING_IGNORED(SituationGroup.RELATIONSHIPS, "Being Ignored / Left on Read"),
    BEING_REJECTED(SituationGroup.RELATIONSHIPS, "Being Rejected"),
    LONG_DISTANCE(SituationGroup.RELATIONSHIPS, "Long-Distance Difficulties"),
    MISSING_EX(SituationGroup.RELATIONSHIPS, "Missing an Ex-Partner"),
    FEAR_OF_LOSING_SOMEONE(SituationGroup.RELATIONSHIPS, "Fear of Losing Someone"),
    FEELING_UNWANTED(SituationGroup.RELATIONSHIPS, "Feeling Unwanted"),
    EX_SOCIAL_MEDIA_CHECKING(SituationGroup.RELATIONSHIPS, "Checking Ex's Social Media"),

    // Academics
    EXAM_FAILURE(SituationGroup.ACADEMICS, "Exam Failure"),
    POOR_GRADES(SituationGroup.ACADEMICS, "Poor Grades"),
    ASSIGNMENT_PRESSURE(SituationGroup.ACADEMICS, "Assignment Pressure"),
    DISAPPOINTING_PARENTS(SituationGroup.ACADEMICS, "Fear of Disappointing Parents"),
    DIFFICULTY_CONCENTRATING(SituationGroup.ACADEMICS, "Difficulty Concentrating"),
    ACADEMIC_COMPARISON(SituationGroup.ACADEMICS, "Academic Comparison"),

    // Social
    LOSING_FRIENDS(SituationGroup.SOCIAL, "Losing Friends"),
    FEELING_EXCLUDED(SituationGroup.SOCIAL, "Feeling Excluded"),
    BEING_JUDGED(SituationGroup.SOCIAL, "Being Judged"),
    SOCIAL_REJECTION(SituationGroup.SOCIAL, "Social Rejection"),
    FRIENDSHIP_CONFLICTS(SituationGroup.SOCIAL, "Friendship Conflicts"),
    FRIENDSHIP_DRIFT(SituationGroup.SOCIAL, "Friendship Drift"),
    ONE_SIDED_FRIENDSHIP(SituationGroup.SOCIAL, "One-Sided Friendship"),
    THIRD_WHEEL(SituationGroup.SOCIAL, "Third Wheel / Left Out of Plans"),
    PEER_ACHIEVEMENT_ENVY(SituationGroup.SOCIAL, "Peer Achievement Comparison"),
    SOCIAL_MEDIA_ENVY(SituationGroup.SOCIAL, "Social Media Comparison"),

    // Personal
    FAMILY_CONFLICTS(SituationGroup.PERSONAL, "Family Conflicts"),
    PARENTAL_CAREER_PRESSURE(SituationGroup.PERSONAL, "Parental Career Pressure"),
    SIBLING_COMPARISON(SituationGroup.PERSONAL, "Sibling Comparison"),
    UNSUCCESSFUL_EFFORT(SituationGroup.ACADEMICS, "Unsuccessful Hard Work"),
    FEAR_WRONG_DEGREE(SituationGroup.PERSONAL, "Fear of Wrong Degree"),
    DIRECTIONLESS(SituationGroup.PERSONAL, "Feeling Directionless"),
    FINANCIAL_PRESSURE(SituationGroup.PERSONAL, "Financial Pressure"),
    CAREER_UNCERTAINTY(SituationGroup.PERSONAL, "Career Uncertainty"),
    LOSS_OF_LOVED_ONE(SituationGroup.PERSONAL, "Loss of a Loved One"),
    PERSONAL_DISAPPOINTMENT(SituationGroup.PERSONAL, "Personal Disappointment"),

    // Batch 2: Scenario 6 (Misunderstood and Unheard)
    PERSPECTIVE_DISMISSED(SituationGroup.PERSONAL, "Feelings Dismissed"),
    LABELED_OVERLY_SENSITIVE(SituationGroup.PERSONAL, "Labeled Overly Sensitive"),
    COMMUNICATION_BREAKDOWN(SituationGroup.PERSONAL, "Words Twisted / Communication Breakdown"),

    // Batch 2: Scenario 7 (Regret and Guilt)
    PAST_DECISION_REGRET(SituationGroup.PERSONAL, "Past Words / Actions Regret"),
    HURT_SOMEONE(SituationGroup.PERSONAL, "Hurt Someone Who Cared"),
    SELF_BLAME(SituationGroup.PERSONAL, "Unable to Forgive Self / Self-Blame"),

    // Batch 2: Scenario 8 (Sleep Difficulties & Night Rumination)
    CANNOT_SLEEP_INSOMNIA(SituationGroup.PERSONAL, "Late Night Insomnia"),
    NIGHT_RUMINATION(SituationGroup.PERSONAL, "Nighttime Mind Rumination"),
    LATE_NIGHT_LONELINESS(SituationGroup.PERSONAL, "Late Night Loneliness & Dread"),

    // Batch 2: Scenario 9 (Social Anxiety & Fear of Judgement)
    MEETING_NEW_PEOPLE(SituationGroup.SOCIAL, "Awkward Around New People"),
    POST_EVENT_RUMINATION(SituationGroup.SOCIAL, "Post-Event Social Rumination"),
    FEELING_AWKWARD(SituationGroup.SOCIAL, "Social Anxiety & Freezing Up"),
    PUBLIC_SPEAKING_FEAR(SituationGroup.SOCIAL, "Fear of Public Speaking"),

    // Batch 2: Scenario 10 (Procrastination & Productivity Distress)
    PHONE_DOOMSCROLLING(SituationGroup.PERSONAL, "Doomscrolling & Screen Addiction"),
    TASK_OVERWHELM(SituationGroup.ACADEMICS, "Overwhelmed by Pending Work"),
    AVOIDING_TASKS(SituationGroup.ACADEMICS, "Procrastination & Inability to Start"),

    // Batch 3: Scenario 11 (Emotional Exhaustion & Burnout)
    MENTAL_DRAIN(SituationGroup.PERSONAL, "Mentally Drained & Exhausted"),
    ANHEDONIA_LOSS_OF_INTEREST(SituationGroup.PERSONAL, "Loss of Interest in Activities"),
    RESPONSIBILITY_OVERWHELM(SituationGroup.PERSONAL, "Tired of Everything / Overwhelmed"),
    WITHDRAWAL_URGE(SituationGroup.PERSONAL, "Urge to Withdraw / Be Alone"),

    // Batch 3: Scenario 12 (Financial Stress & Dependence)
    EXPENSE_MANAGEMENT(SituationGroup.PERSONAL, "Financial Stress & Expenses"),
    PARENTAL_FINANCIAL_DEPENDENCE(SituationGroup.PERSONAL, "Guilt Over Parental Financial Dependence"),
    PEER_WEALTH_COMPARISON(SituationGroup.SOCIAL, "Comparing Earnings & Wealth to Peers"),

    // Batch 3: Scenario 13 (Life Transitions & Unfamiliarity)
    COLLEGE_START(SituationGroup.PERSONAL, "Starting College / New Job"),
    RELOCATING_NEW_CITY(SituationGroup.PERSONAL, "Relocating to New City / Unfamiliar Environment"),
    ROUTINE_LOSS(SituationGroup.PERSONAL, "Loss of Daily Routine"),
    NOSTALGIA_PAST(SituationGroup.PERSONAL, "Homesickness & Missing School Friends"),

    // Batch 3: Scenario 14 (Interpersonal Conflict & Boundaries)
    HEATED_ARGUMENT(SituationGroup.RELATIONSHIPS, "Heated Argument / Regretted Words"),
    DISRESPECT_FELT(SituationGroup.RELATIONSHIPS, "Disrespect in Conflict / Boundary Setting"),

    // Batch 3: Scenario 15 (Rejection & Unreciprocated Feelings)
    ROMANTIC_REJECTION(SituationGroup.RELATIONSHIPS, "Romantic Confession Rejection"),
    UNRECIPROCATED_FEELINGS(SituationGroup.RELATIONSHIPS, "Unreciprocated Feelings & Embarrassment"),
    FEELING_UNWANTED_AFTER_NO(SituationGroup.RELATIONSHIPS, "Feeling Not Good Enough After Rejection"),

    // Batch 4: Scenario 16 (Grief and Loss)
    PET_LOSS(SituationGroup.PERSONAL, "Loss of a Beloved Pet"),
    ANNIVERSARY_GRIEF(SituationGroup.PERSONAL, "Grief on Anniversaries & Holidays"),
    ABSENCE_LONGING(SituationGroup.PERSONAL, "Longing for One Last Conversation"),

    // Batch 4: Scenario 17 (Body Image & Self-Confidence)
    APPEARANCE_INSECURITY(SituationGroup.PERSONAL, "Hating Appearance & Insecurity"),
    WEIGHT_DISSATISFACTION(SituationGroup.PERSONAL, "Body & Weight Dissatisfaction"),
    PHOTO_ANXIETY(SituationGroup.PERSONAL, "Photo Dread & Outdoor Insecurity"),

    // Batch 4: Scenario 18 (Positive Emotions & Celebration)
    ACADEMIC_ACHIEVEMENT(SituationGroup.ACADEMICS, "Passed Exam / Academic Milestone"),
    PERSONAL_GOAL_REACHED(SituationGroup.PERSONAL, "Hard Work Paid Off / Goal Achieved"),
    GENERAL_GRATITUDE(SituationGroup.PERSONAL, "Proud of Myself / Joyful Celebration"),

    // Batch 4: Scenario 19 (Everyday Frustration & Casual Venting)
    BAD_DAY(SituationGroup.PERSONAL, "Terrible Day / Casual Venting"),
    IRRITATED_SMALL_THINGS(SituationGroup.PERSONAL, "Irritated by Small Things"),
    CASUAL_VENTING(SituationGroup.PERSONAL, "Unexplained Bad Mood & Grumpiness"),

    // Batch 4: Scenario 20 (Romantic Relationship Support)
    RELATIONSHIP_INSECURITY(SituationGroup.RELATIONSHIPS, "Partner Growing Distant / Insecurity"),
    PARTNER_DISTANCING(SituationGroup.RELATIONSHIPS, "Unmet Attention & Fearing Neediness"),
    FEAR_OF_ABANDONMENT(SituationGroup.RELATIONSHIPS, "Fear of Abandonment in Partnership"),
    JEALOUSY(SituationGroup.RELATIONSHIPS, "Jealousy When Partner Spends Time with Others")
}

enum class SituationGroup {
    RELATIONSHIPS,
    ACADEMICS,
    SOCIAL,
    PERSONAL
}

data class SituationRecognitionResult(
    val primarySituation: UserSituation?,
    val secondarySituations: List<UserSituation> = emptyList(),
    val matchedPhrase: String? = null,
    val confidence: Float = 0f
)

object SituationalContextEngine {

    // --- RELATIONSHIPS REGEXES ---
    private val breakupRegex = Regex(
        """\b((going\s+through\s+a\s+|gng\s+thru\s+(a\s+)?)?break\s*up|broke\s+up|broken\s+up|breaking\s+up|dumped(\s+me)?|ended\s+(things|our\s+relationship|it)|they\s+left\s+me|she\s+left\s+me|he\s+left\s+me|split\s+up|separated|parted\s+ways|called\s+it\s+off|cut\s+ties|ex[-\s]?(partner|boyfriend|girlfriend|gf|bf))\b""",
        RegexOption.IGNORE_CASE
    )

    private val missingExRegex = Regex(
        """\b(still\s+miss\s+(her|him|them|my\s+ex)|miss\s+(her|him|them|my\s+ex)\s+(so\s+much|terribly|a\s+lot)?|want\s+(her|him|them|my\s+ex)\s+back|wish\s+we\s+were\s+still\s+together|can'?t\s+get\s+(her|him|them)\s+out\s+of\s+my\s+head|reminisc(ing|e)\s+about\s+(her|him|us))\b""",
        RegexOption.IGNORE_CASE
    )

    private val exSocialMediaRegex = Regex(
        """\b(check(ing)?\s+(her|his|their|my\s+ex['\s]?s?)\s+(instagram|insta|profile|stories|story|posts|feed|account|whatsapp|snapchat|activity|status)|stalk(ing)?\s+(her|his|their|my\s+ex)|look(ing)?\s+at\s+(her|his|their|our)\s+(profile|photos|pictures|stories|old\s+texts|old\s+chats)|reread(ing)?\s+(our|old)\s+(texts|messages|conversations))\b""",
        RegexOption.IGNORE_CASE
    )

    private val cheatedOnRegex = Regex(
        """\b(cheat(ed|ing)?\s+(on\s+me)?|unfaithful|had\s+an\s+affair|betrayed\s+(me|my\s+trust)|slept\s+with\s+someone\s+else|saw\s+(him|her)\s+with\s+someone\s+else)\b""",
        RegexOption.IGNORE_CASE
    )

    private val relationshipArgumentsRegex = Regex(
        """\b(fight(ing)?\s+with\s+my\s+(partner|boyfriend|girlfriend|bf|gf|husband|wife)|argument\s+with\s+my\s+(partner|boyfriend|girlfriend)|we\s+keep\s+(fighting|arguing)|screaming\s+match|tense\s+between\s+us|toxic\s+relationship)\b""",
        RegexOption.IGNORE_CASE
    )

    private val beingIgnoredRegex = Regex(
        """\b(ignor(ed|ing)\s+me|left\s+(me\s+)?on\s+read|won'?t\s+(reply|answer|text\s+back)|stopped\s+(replying|texting|talking\s+to\s+me)|ghosted(\s+me)?|silent\s+treatment)\b""",
        RegexOption.IGNORE_CASE
    )

    private val beingRejectedRegex = Regex(
        """\b(reject(ed|ion)?(\s+by\s+(her|him|them))?|said\s+no|turned\s+(me\s+)?down|didn'?t\s+want\s+to\s+go\s+out|confessed\s+and\s+got\s+rejected|friendzoned)\b""",
        RegexOption.IGNORE_CASE
    )

    private val longDistanceRegex = Regex(
        """\b(long\s+distance|ldr|miles\s+apart|different\s+time\s+zones|different\s+cities|can'?t\s+see\s+each\s+other|living\s+far\s+away)\b""",
        RegexOption.IGNORE_CASE
    )

    private val fearOfLosingSomeoneRegex = Regex(
        """\b(scared\s+(of\s+)?los(ing|e)\s+(her|him|them|my\s+partner)|terrified\s+they'?ll\s+leave|afraid\s+(she|he|they)\s+will\s+walk\s+away|feel\s+like\s+i'?m\s+losing\s+(him|her))\b""",
        RegexOption.IGNORE_CASE
    )

    private val feelingUnwantedRegex = Regex(
        """\b(feel(ing)?\s+unwanted|nobody\s+wants\s+me|unlovable|not\s+good\s+enough\s+for\s+(anyone|her|him)|they\s+don'?t\s+care\s+about\s+me)\b""",
        RegexOption.IGNORE_CASE
    )

    // --- ACADEMICS REGEXES ---
    private val examFailureRegex = Regex(
        """\b(fail(ed|ing)?\s+(my\s+)?(exam|test|finals|midterms|paper|course|class)s?|flunk(ed|ing)?|got\s+an\s+f|couldn'?t\s+pass|messed\s+up\s+(my\s+)?(exam|test|finals)|made\s+terrible\s+mistakes)\b""",
        RegexOption.IGNORE_CASE
    )

    private val poorGradesRegex = Regex(
        """\b(poor\s+grades|bad\s+grades|grades\s+(are\s+)?(slipping|dropping|terrible|bad)|low\s+marks|ruined\s+my\s+gpa|cgpa\s+dropped|academic\s+probation)\b""",
        RegexOption.IGNORE_CASE
    )

    private val assignmentPressureRegex = Regex(
        """\b(assignment(s)?\s+(due|piling\s+up|pressure)|deadline\s+(is\s+)?tomorrow|so\s+much\s+homework|projects\s+due|behind\s+on\s+assignments)\b""",
        RegexOption.IGNORE_CASE
    )

    private val disappointingParentsRegex = Regex(
        """\b(disappoint(ing)?\s+(my\s+)?(parents|mom|dad|family)|parents\s+(will\s+be\s+mad|expect\s+so\s+much|disappointed)|pressure\s+from\s+parents\s+for\s+grades)\b""",
        RegexOption.IGNORE_CASE
    )

    private val difficultyConcentratingRegex = Regex(
        """\b(can'?t\s+(concentrate|focus|study)|mind\s+keeps\s+wandering|staring\s+at\s+the\s+page|brain\s+fog\s+while\s+studying|unable\s+to\s+retain)\b""",
        RegexOption.IGNORE_CASE
    )

    private val academicComparisonRegex = Regex(
        """\b(everyone\s+(is\s+)?smarter\s+than\s+me|comparing\s+my\s+grades|falling\s+behind\s+everyone\s+else|classmates\s+are\s+doing\s+better)\b""",
        RegexOption.IGNORE_CASE
    )

    // --- SOCIAL REGEXES ---
    private val losingFriendsRegex = Regex(
        """\b(los(ing|t)\s+(my\s+)?(friends|best\s+friend)|lost\s+touch\s+with\s+(my\s+)?(childhood\s+)?friends?|drifting\s+apart\s+from\s+friends|no\s+longer\s+close|friends\s+abandoned\s+me)\b""",
        RegexOption.IGNORE_CASE
    )

    private val feelingExcludedRegex = Regex(
        """\b(left\s+out|feel(ing)?\s+excluded|hung\s+out\s+without\s+me|not\s+invited|didn'?t\s+invite\s+me|made\s+plans\s+without\s+me)\b""",
        RegexOption.IGNORE_CASE
    )

    private val beingJudgedRegex = Regex(
        """\b(being\s+judged|people\s+judging\s+me|talking\s+behind\s+my\s+back|everyone\s+is\s+staring|judged\s+for\s+how\s+i\s+look)\b""",
        RegexOption.IGNORE_CASE
    )

    private val socialRejectionRegex = Regex(
        """\b(social\s+rejection|nobody\s+wants\s+to\s+sit\s+with\s+me|unwelcome|pushed\s+out\s+of\s+the\s+group|shunned)\b""",
        RegexOption.IGNORE_CASE
    )

    private val friendshipConflictsRegex = Regex(
        """\b(fight\s+with\s+my\s+(friend|best\s+friend)|friend\s+drama|friend\s+betrayed\s+me|arguing\s+with\s+my\s+friend)\b""",
        RegexOption.IGNORE_CASE
    )

    private val friendshipDriftRegex = Regex(
        """\b(best\s+friend\s+(distancing|ignoring|leaving|replaced\s+me)|drift(ing)?\s+apart\s+from\s+(my\s+)?friend|distancing\s+(themselves|themself)|friends\s+drifting)\b""",
        RegexOption.IGNORE_CASE
    )

    private val oneSidedFriendshipRegex = Regex(
        """\b(one[-\s]sided\s+friendship|only\s+one\s+(texting|reaching\s+out|trying|putting\s+effort)|don'?t\s+even\s+text\s+me\s+first|unequal\s+effort)\b""",
        RegexOption.IGNORE_CASE
    )

    private val thirdWheelRegex = Regex(
        """\b(third\s+wheel|friends\s+making\s+plans\s+without\s+me|left\s+me\s+out\s+of\s+plans)\b""",
        RegexOption.IGNORE_CASE
    )

    private val parentalCareerDisapprovalRegex = Regex(
        """\b(parents\s+(don'?t\s+understand|never\s+understand|hate|disapprove\s+of)\s+(my\s+)?(career|passion|degree|choices?)|parents\s+never\s+understand\s+me|want\s+me\s+to\s+do\s+something\s+i\s+don'?t\s+(even\s+)?like|pressure\s+to\s+become\s+a\s+(doctor|engineer|lawyer)|can'?t\s+even\s+explain\s+my\s+side)\b""",
        RegexOption.IGNORE_CASE
    )

    private val siblingComparisonRegex = Regex(
        """\b(compar(ed?|ing|es)?\s+(me\s+)?(to|with)\s+my\s+(brother|sister|sibling)s?|golden\s+child|always\s+comparing\s+us)\b""",
        RegexOption.IGNORE_CASE
    )

    private val unsuccessfulEffortRegex = Regex(
        """\b(worked\s+so\s+hard\s+and\s+still\s+failed|put\s+everything\s+into\s+it\s+and\s+failed|hard\s+work\s+produced\s+no\s+results|everyone\s+else\s+passed|failed\s+again|not\s+capable|maybe\s+i'?m\s+just\s+not\s+capable|rejected\s+from\s+(the\s+)?(program|job|university|college|internship|school)|worked\s+(for\s+)?(months|weeks|years)\s+for)\b""",
        RegexOption.IGNORE_CASE
    )

    private val fearWrongDegreeRegex = Regex(
        """\b(wrong\s+(degree|major|field\s+of\s+study|course)|picked\s+the\s+wrong\s+(degree|major)|regret(ting)?\s+my\s+(degree|major)|hate\s+my\s+(major|degree)|terrified\s+i\s+picked\s+the\s+wrong)\b""",
        RegexOption.IGNORE_CASE
    )

    private val careerDirectionlessRegex = Regex(
        """\b(no\s+idea\s+what\s+i'?m\s+doing\s+with\s+my\s+life|career\s+direction|directionless|fear\s+of\s+choosing\s+the\s+wrong|everyone\s+around\s+me\s+has\s+a\s+plan|scared\s+i'?ll\s+make\s+the\s+wrong\s+decision)\b""",
        RegexOption.IGNORE_CASE
    )

    private val socialMediaEnvyRegex = Regex(
        """\b(social\s+media(\s+envy|\s+makes\s+me)?|instagram|feed|scroll(ing)?\s+(on\s+)?(instagram|tiktok|social\s+media)|curated\s+highlight\s+reel|see(ing)?\s+people\s+travel(l)?ing(\s+and\s+earning)?|wasting\s+my\s+life)\b""",
        RegexOption.IGNORE_CASE
    )

    private val peerAchievementEnvyRegex = Regex(
        """\b(peers?\s+(earning|buying|succeeding|getting\s+promoted|achieving)|see(ing)?\s+people\s+(my\s+age\s+)?(earning|buying)|everyone\s+(my\s+age|around\s+me)\s+is\s+(doing\s+better|smarter|ahead|happier|doing\s+something\s+meaningful)|falling\s+behind\s+(everyone|in\s+life)|feel\s+so\s+inferior)\b""",
        RegexOption.IGNORE_CASE
    )

    // --- PERSONAL REGEXES ---
    private val familyConflictsRegex = Regex(
        """\b(family\s+(conflict|drama|problems)|parents\s+(are\s+)?fighting|screaming\s+at\s+home|toxic\s+household|dad\s+is\s+yelling|mom\s+is\s+screaming|family\s+dismiss(es|ed|ing)?|(family|parents)\s+dismiss(es|ed|ing)?(\s+my\s+feelings)?|unseen|controlled\s+by\s+my\s+parents|feeling\s+unseen)\b""",
        RegexOption.IGNORE_CASE
    )

    private val financialPressureRegex = Regex(
        """\b(financial\s+(stress|pressure|trouble)|money\s+problems|can'?t\s+afford|rent\s+is\s+due|debt|running\s+out\s+of\s+money|broke\s+and\s+stressed)\b""",
        RegexOption.IGNORE_CASE
    )

    private val careerUncertaintyRegex = Regex(
        """\b(career\s+(uncertainty|stress|anxiety|pressure)|don'?t\s+know\s+what\s+to\s+do\s+with\s+my\s+life|job\s+hunting|lost\s+my\s+job|laid\s+off|unemployed|future\s+career)\b""",
        RegexOption.IGNORE_CASE
    )

    private val lossOfLovedOneRegex = Regex(
        """\b(passed\s+away|funeral|lost\s+my\s+(grandmother|grandfather|grandma|grandpa|friend|dad|mom|father|mother|pet|dog|cat)|death\s+of\s+my)\b""",
        RegexOption.IGNORE_CASE
    )

    private val personalDisappointmentRegex = Regex(
        """\b(disappointed\s+in\s+myself|hate\s+who\s+i\s+am|let\s+myself\s+down|failed\s+myself|messed\s+up\s+my\s+life)\b""",
        RegexOption.IGNORE_CASE
    )

    // --- BATCH 2 REGEXES ---
    // Scenario 6: Misunderstood and Unheard
    private val perspectiveDismissedRegex = Regex(
        """\b(people\s+dismiss(\s+my\s+feelings)?|feelings?\s+(are\s+|get\s+)?dismissed|dismiss(ed|ing)?\s+(what\s+i\s+say|how\s+i\s+feel|my\s+feelings)|invalidat(ed|ing)|nobody\s+cares\s+how\s+i\s+feel)\b""",
        RegexOption.IGNORE_CASE
    )

    private val labeledOverlySensitiveRegex = Regex(
        """\b((label(l)?ed|called|told\s+i'?m)\s+(too|overly)\s+sensitive|overreacting|making\s+a\s+big\s+deal\s+out\s+of\s+nothing|too\s+emotional|drama\s+queen)\b""",
        RegexOption.IGNORE_CASE
    )

    private val communicationBreakdownRegex = Regex(
        """\b(nobody\s+understands\s+(what\s+i'?m\s+(trying\s+to\s+)?say(ing)?|me)|words\s+get\s+twisted|stopped\s+explaining\s+myself|invisible\s+in\s+conversations|talk(ing)?\s+to\s+a\s+brick\s+wall|don'?t\s+even\s+bother\s+explaining|interpret\s+it\s+differently|nobody\s+listens|misunderstood)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 7: Regret and Guilt
    private val pastDecisionRegretRegex = Regex(
        """\b(wish\s+i\s+could\s+(go\s+back|change\s+what\s+i\s+said|undo|rewind)|ruined\s+everything|so\s+much\s+regret|regret\s+(what\s+i\s+did|my\s+words|saying\s+that)|wish\s+i\s+never\s+said)\b""",
        RegexOption.IGNORE_CASE
    )

    private val hurtSomeoneRegex = Regex(
        """\b(hurt\s+someone(\s+who\s+(genuinely\s+)?cared)?|caused\s+(someone|them)\s+pain|broke\s+their\s+heart|made\s+(them|someone)\s+cry|damaged\s+(the\s+)?relationship|treated\s+them\s+badly)\b""",
        RegexOption.IGNORE_CASE
    )

    private val selfBlameRegex = Regex(
        """\b(never\s+forgive\s+myself|can'?t\s+forgive\s+myself|haunted\s+by\s+what\s+i\s+did|guilt\s+is\s+eating\s+me|replaying\s+(the\s+mistake|my\s+actions)|all\s+my\s+fault|hate\s+myself\s+for\s+it|eat(ing)?\s+me\s+up\s+inside)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 8: Sleep Difficulties & Night Rumination
    private val cannotSleepInsomniaRegex = Regex(
        """\b(3\s*am\s+and\s+(still\s+can'?t\s+sleep|wide\s+awake)|can'?t\s+(fall\s+asleep|sleep)|wide\s+awake\s+at\s+night|tossing\s+and\s+turning|insomnia|trouble\s+falling\s+asleep|staring\s+at\s+the\s+ceiling)\b""",
        RegexOption.IGNORE_CASE
    )

    private val nightRuminationRegex = Regex(
        """\b(brain\s+won'?t\s+(stop|shut\s+off)(\s+thinking)?|racing\s+thoughts\s+at\s+night|mind\s+won'?t\s+quiet\s+down|nighttime\s+(anxiety|overthinking)|replaying\s+everything\s+at\s+night|thinking\s+about\s+everything\s+that\s+went\s+wrong)\b""",
        RegexOption.IGNORE_CASE
    )

    private val lateNightLonelinessRegex = Regex(
        """\b(dreading\s+waking\s+up\s+tomorrow|dreading\s+tomorrow|late\s+night\s+loneliness|alone\s+at\s+night|middle\s+of\s+the\s+night)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 9: Social Anxiety & Fear of Judgement
    private val meetingNewPeopleRegex = Regex(
        """\b(feel\s+awkward\s+around\s+new\s+people|nervous\s+around\s+new\s+people|hard\s+to\s+talk\s+to\s+strangers|don'?t\s+know\s+what\s+to\s+say|awkward\s+in\s+conversations|small\s+talk\s+terrifies\s+me)\b""",
        RegexOption.IGNORE_CASE
    )

    private val postEventRuminationRegex = Regex(
        """\b(they'?ll\s+find\s+me\s+boring|think\s+i'?m\s+boring|replaying\s+the\s+conversation|dissecting\s+every\s+word|overthinking\s+what\s+i\s+said|sounding\s+stupid|embarrassed\s+myself\s+in\s+front\s+of)\b""",
        RegexOption.IGNORE_CASE
    )

    private val feelingAwkwardRegex = Regex(
        """\b(social\s+anxiety|afraid\s+of\s+being\s+judged|fear\s+of\s+(being\s+)?judged|scared\s+to\s+speak\s+up|freezing\s+(up\s+)?in\s+groups|spotlight\s+effect|people\s+evaluating\s+me|hyper[-\s]?aware\s+in\s+public)\b""",
        RegexOption.IGNORE_CASE
    )

    private val publicSpeakingFearRegex = Regex(
        """\b(public\s+speaking(\s+fear|\s+anxiety)?|presentation\s+anxiety|speaking\s+in\s+front\s+of\s+(people|class|crowd)|stage\s+fright)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 10: Procrastination & Productivity Distress
    private val phoneDoomscrollingRegex = Regex(
        """\b(wasted\s+(the\s+entire|my\s+whole|all)\s+day\s+scrolling|scrolling\s+(reels|tiktok|shorts|feed|phone)|doomscroll(ing)?|phone\s+addiction|spent\s+hours\s+on\s+my\s+phone)\b""",
        RegexOption.IGNORE_CASE
    )

    private val taskOverwhelmRegex = Regex(
        """\b(so\s+much\s+work\s+pending|mountain\s+of\s+work|pile\s+of\s+work|paralyzed\s+by\s+work|executive\s+dysfunction|too\s+many\s+tasks|work\s+is\s+piling\s+up)\b""",
        RegexOption.IGNORE_CASE
    )

    private val avoidingTasksRegex = Regex(
        """\b(can'?t\s+get\s+myself\s+to\s+start|procrastinat(ing|ion)|guilty\s+about\s+wasting\s+time|putting\s+off\s+(my\s+)?work|delaying\s+everything|unable\s+to\s+start|avoiding\s+(my\s+)?tasks)\b""",
        RegexOption.IGNORE_CASE
    )

    // --- BATCH 3 REGEXES ---
    // Scenario 11: Emotional Exhaustion & Burnout
    private val mentalDrainRegex = Regex(
        """\b(mentally\s+drained|running\s+on\s+empty|no\s+energy\s+left|chronic\s+fatigue|exhausted\s+to\s+my\s+bones)\b""",
        RegexOption.IGNORE_CASE
    )
    private val anhedoniaRegex = Regex(
        """\b(things\s+i\s+enjoy\s+feel\s+like\s+work|lost\s+interest\s+in\s+everything|nothing\s+brings\s+me\s+joy|hobbies\s+feel\s+like\s+chores|anhedonia)\b""",
        RegexOption.IGNORE_CASE
    )
    private val responsibilityOverwhelmRegex = Regex(
        """\b(tired\s+of\s+everything|tired\s+despite\s+sleeping|overwhelmed\s+by\s+responsibilities|carrying\s+so\s+much|burnout)\b""",
        RegexOption.IGNORE_CASE
    )
    private val withdrawalUrgeRegex = Regex(
        """\b(just\s+want\s+to\s+be\s+left\s+alone|want\s+to\s+withdraw|emotionally\s+unavailable|isolate\s+myself|don'?t\s+want\s+to\s+talk\s+to\s+anyone)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 12: Financial Stress & Dependence
    private val parentalFinancialDependenceRegex = Regex(
        """\b(feel\s+bad\s+asking\s+(my\s+)?parents\s+for\s+money|guilt\s+asking\s+parents|financial(ly)?\s+dependent|burden\s+on\s+my\s+parents|asking\s+for\s+financial\s+help)\b""",
        RegexOption.IGNORE_CASE
    )
    private val peerWealthComparisonRegex = Regex(
        """\b(everyone\s+around\s+me\s+earns|classmates\s+earning|peers\s+making\s+money|not\s+contributing\s+anything|compare\s+my\s+bank\s+account)\b""",
        RegexOption.IGNORE_CASE
    )
    private val expenseManagementRegex = Regex(
        """\b(can'?t\s+afford\s+anything|guilty\s+about\s+spending|debt\s+worries|financial\s+burden|every\s+expense\s+feels\s+like\s+a\s+threat)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 13: Life Transitions & Unfamiliarity
    private val collegeStartRegex = Regex(
        """\b(started\s+(college|university|a\s+new\s+job)|recently\s+started\s+(college|university|job)|don'?t\s+feel\s+like\s+i\s+belong\s+here|imposter\s+in\s+college|new\s+campus)\b""",
        RegexOption.IGNORE_CASE
    )
    private val nostalgiaPastRegex = Regex(
        """\b(miss\s+my\s+school\s+friends|miss\s+my\s+old\s+life|homesick(ness)?|longing\s+for\s+my\s+old\s+routine|miss\s+home)\b""",
        RegexOption.IGNORE_CASE
    )
    private val routineLossRegex = Regex(
        """\b(everything\s+feels\s+unfamiliar|lost\s+my\s+routine|disorienting\s+change|unfamiliar\s+environment)\b""",
        RegexOption.IGNORE_CASE
    )
    private val relocatingNewCityRegex = Regex(
        """\b(moved\s+to\s+a\s+new\s+city|relocated|living\s+in\s+a\s+new\s+place|new\s+city)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 14: Interpersonal Conflict & Boundaries
    private val heatedArgumentRegex = Regex(
        """\b(had\s+a\s+huge\s+argument(\s+with)?|said\s+things\s+i\s+shouldn'?t\s+have|screaming\s+match|we\s+had\s+a\s+fight|spoke\s+out\s+of\s+anger)\b""",
        RegexOption.IGNORE_CASE
    )
    private val disrespectFeltRegex = Regex(
        """\b(they\s+were\s+wrong\s+too|disrespected(\s+in\s+argument)?|both\s+sides\s+made\s+mistakes|boundary\s+was\s+crossed|need\s+boundaries)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 15: Rejection & Unreciprocated Feelings
    private val romanticRejectionRegex = Regex(
        """\b(told\s+someone\s+i\s+liked\s+them\s+and\s+they\s+rejected\s+me|confessed\s+and\s+got\s+rejected|confessed\s+my\s+feelings|rejected\s+after\s+confessing)\b""",
        RegexOption.IGNORE_CASE
    )
    private val unreciprocatedFeelingsRegex = Regex(
        """\b(feel\s+so\s+embarrassed(\s+after)?|unreciprocated\s+(feelings|love)|embarrassment\s+of\s+rejection|shared\s+my\s+feelings\s+and\s+got\s+turned\s+down)\b""",
        RegexOption.IGNORE_CASE
    )
    private val feelingUnwantedAfterNoRegex = Regex(
        """\b(maybe\s+i'?m\s+just\s+not\s+good\s+enough|they\s+said\s+no(\s+to\s+me)?|nobody\s+wants\s+me|friendzoned|unlovable\s+after\s+rejection)\b""",
        RegexOption.IGNORE_CASE
    )

    // --- BATCH 4 REGEXES ---
    // Scenario 16: Grief and Loss
    private val absenceLongingRegex = Regex(
        """\b(wish\s+i\s+could\s+talk\s+to\s+(him|her|them)\s+one\s+more\s+time|remembering\s+things\s+we\s+used\s+to\s+do|empty\s+without\s+(him|her|them)|miss\s+my\s+(grandfather|grandmother|grandpa|grandma))\b""",
        RegexOption.IGNORE_CASE
    )
    private val petLossRegex = Regex(
        """\b(lost\s+my\s+(pet|dog|cat)|(dog|cat|pet)\s+(passed\s+away|died)|put\s+my\s+(dog|cat|pet)\s+down)\b""",
        RegexOption.IGNORE_CASE
    )
    private val anniversaryGriefRegex = Regex(
        """\b(anniversary\s+of\s+(their\s+)?(death|passing)|first\s+(holiday|birthday|christmas|diwali)\s+without\s+(them|him|her))\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 17: Body Image & Self-Confidence
    private val appearanceInsecurityRegex = Regex(
        """\b(hate\s+how\s+i\s+look|feel\s+ugly|hate\s+my\s+(face|skin|features)|unattractive|insecure\s+about\s+my\s+looks)\b""",
        RegexOption.IGNORE_CASE
    )
    private val weightDissatisfactionRegex = Regex(
        """\b(disgusted\s+with\s+my\s+body|insecure\s+about\s+my\s+(weight|body|size)|hate\s+my\s+body|body\s+dysmorphia)\b""",
        RegexOption.IGNORE_CASE
    )
    private val photoAnxietyRegex = Regex(
        """\b(everyone\s+looks\s+better\s+than\s+me|uncomfortable\s+in\s+photos|hate\s+(taking|being\s+in)\s+photos|don'?t\s+feel\s+confident\s+going\s+outside|dread\s+pictures)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 18: Positive Emotions & Celebration
    private val academicAchievementRegex = Regex(
        """\b(finally\s+passed\s+(my\s+)?(exam|test|class|course|finals)|passed\s+my\s+(exam|test)|got\s+accepted(\s+into)?|cleared\s+the\s+exam)\b""",
        RegexOption.IGNORE_CASE
    )
    private val personalGoalReachedRegex = Regex(
        """\b(studied\s+so\s+hard\s+and\s+it\s+paid\s+off|got\s+the\s+job|achieved\s+my\s+goal|overcame\s+my\s+fear|hard\s+work\s+paid\s+off)\b""",
        RegexOption.IGNORE_CASE
    )
    private val generalGratitudeRegex = Regex(
        """\b(actually\s+proud\s+of\s+myself|proud\s+of\s+myself|celebrating|wonderful\s+day|really\s+happy\s+today|celebrate\s+with\s+me)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 19: Everyday Frustration & Casual Venting
    private val badDayRegex = Regex(
        """\b(today\s+has\s+been\s+(absolutely\s+)?terrible|horrible\s+day|awful\s+day|terrible\s+day|annoying\s+day)\b""",
        RegexOption.IGNORE_CASE
    )
    private val irritatedSmallThingsRegex = Regex(
        """\b(everything\s+is\s+just\s+irritating\s+me|everything\s+is\s+irritating|irritated\s+by\s+small\s+things|everyone\s+is\s+annoying\s+me|snapping\s+at\s+everyone)\b""",
        RegexOption.IGNORE_CASE
    )
    private val casualVentingRegex = Regex(
        """\b(don'?t\s+even\s+know\s+why\s+i'?m\s+annoyed|bad\s+mood|grumpy|stuck\s+in\s+a\s+rut|bored\s+and\s+frustrated|just\s+need\s+to\s+vent)\b""",
        RegexOption.IGNORE_CASE
    )

    // Scenario 20: Romantic Relationship Support
    private val relationshipInsecurityRegex = Regex(
        """\b(partner\s+(doesn'?t\s+love\s+me|(is\s+)?growing\s+distant|is\s+distant)|growing\s+distant(\s+from\s+partner)?|feel\s+like\s+my\s+partner\s+doesn'?t\s+love\s+me|trust\s+issues\s+in\s+my\s+relationship|relationship\s+insecurity)\b""",
        RegexOption.IGNORE_CASE
    )
    private val partnerDistancingRegex = Regex(
        """\b(partner\s+barely\s+gives?\s+me\s+attention|barely\s+gives?\s+me\s+attention|don'?t\s+want\s+to\s+seem\s+needy|unmet\s+emotional\s+connection)\b""",
        RegexOption.IGNORE_CASE
    )
    private val jealousyRegex = Regex(
        """\b(jealous\s+when\s+they\s+spend\s+time|jealous\s+of\s+their\s+friends|partner\s+spends\s+time\s+with\s+(others|other\s+people)|relationship\s+jealousy)\b""",
        RegexOption.IGNORE_CASE
    )
    private val fearOfAbandonmentRegex = Regex(
        """\b(scared\s+my\s+partner\s+will\s+leave|fear\s+of\s+abandonment|terrified\s+of\s+losing\s+my\s+partner)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Recognizes underlying user situations and life triggers from the message.
     */
    fun detectSituation(message: String): SituationRecognitionResult {
        val trimmed = message.trim()
        if (trimmed.isEmpty()) {
            return SituationRecognitionResult(null)
        }

        val matches = mutableListOf<Pair<UserSituation, Float>>()

        fun check(situation: UserSituation, regex: Regex, weight: Float) {
            if (regex.containsMatchIn(trimmed)) {
                matches.add(situation to weight)
            }
        }

        // Relationships (Check specific sub-actions first for priority)
        check(UserSituation.EX_SOCIAL_MEDIA_CHECKING, exSocialMediaRegex, 1.4f)
        check(UserSituation.MISSING_EX, missingExRegex, 1.35f)
        check(UserSituation.BEING_CHEATED_ON, cheatedOnRegex, 1.3f)
        check(UserSituation.BREAKUP, breakupRegex, 1.3f)
        check(UserSituation.RELATIONSHIP_ARGUMENTS, relationshipArgumentsRegex, 1.2f)
        check(UserSituation.BEING_IGNORED, beingIgnoredRegex, 1.2f)
        check(UserSituation.BEING_REJECTED, beingRejectedRegex, 1.2f)
        check(UserSituation.LONG_DISTANCE, longDistanceRegex, 1.1f)
        check(UserSituation.FEAR_OF_LOSING_SOMEONE, fearOfLosingSomeoneRegex, 1.15f)
        check(UserSituation.FEELING_UNWANTED, feelingUnwantedRegex, 1.1f)

        // Academics
        check(UserSituation.DISAPPOINTING_PARENTS, disappointingParentsRegex, 1.35f)
        check(UserSituation.UNSUCCESSFUL_EFFORT, unsuccessfulEffortRegex, 1.35f)
        check(UserSituation.EXAM_FAILURE, examFailureRegex, 1.3f)
        check(UserSituation.POOR_GRADES, poorGradesRegex, 1.25f)
        check(UserSituation.ASSIGNMENT_PRESSURE, assignmentPressureRegex, 1.2f)
        check(UserSituation.DIFFICULTY_CONCENTRATING, difficultyConcentratingRegex, 1.15f)
        check(UserSituation.ACADEMIC_COMPARISON, academicComparisonRegex, 1.1f)

        // Social
        check(UserSituation.FRIENDSHIP_DRIFT, friendshipDriftRegex, 1.35f)
        check(UserSituation.ONE_SIDED_FRIENDSHIP, oneSidedFriendshipRegex, 1.3f)
        check(UserSituation.THIRD_WHEEL, thirdWheelRegex, 1.25f)
        check(UserSituation.SOCIAL_MEDIA_ENVY, socialMediaEnvyRegex, 1.35f)
        check(UserSituation.PEER_ACHIEVEMENT_ENVY, peerAchievementEnvyRegex, 1.35f)
        check(UserSituation.LOSING_FRIENDS, losingFriendsRegex, 1.25f)
        check(UserSituation.FEELING_EXCLUDED, feelingExcludedRegex, 1.2f)
        check(UserSituation.FRIENDSHIP_CONFLICTS, friendshipConflictsRegex, 1.2f)
        check(UserSituation.BEING_JUDGED, beingJudgedRegex, 1.15f)
        check(UserSituation.SOCIAL_REJECTION, socialRejectionRegex, 1.15f)

        // Personal
        check(UserSituation.PARENTAL_CAREER_PRESSURE, parentalCareerDisapprovalRegex, 1.35f)
        check(UserSituation.SIBLING_COMPARISON, siblingComparisonRegex, 1.3f)
        check(UserSituation.FEAR_WRONG_DEGREE, fearWrongDegreeRegex, 1.35f)
        check(UserSituation.DIRECTIONLESS, careerDirectionlessRegex, 1.3f)
        check(UserSituation.LOSS_OF_LOVED_ONE, lossOfLovedOneRegex, 1.4f)
        check(UserSituation.FAMILY_CONFLICTS, familyConflictsRegex, 1.25f)
        check(UserSituation.FINANCIAL_PRESSURE, financialPressureRegex, 1.2f)
        check(UserSituation.CAREER_UNCERTAINTY, careerUncertaintyRegex, 1.2f)
        check(UserSituation.PERSONAL_DISAPPOINTMENT, personalDisappointmentRegex, 1.15f)

        // Batch 2: Scenario 6 (Misunderstood and Unheard)
        check(UserSituation.PERSPECTIVE_DISMISSED, perspectiveDismissedRegex, 1.35f)
        check(UserSituation.LABELED_OVERLY_SENSITIVE, labeledOverlySensitiveRegex, 1.35f)
        check(UserSituation.COMMUNICATION_BREAKDOWN, communicationBreakdownRegex, 1.35f)

        // Batch 2: Scenario 7 (Regret and Guilt)
        check(UserSituation.PAST_DECISION_REGRET, pastDecisionRegretRegex, 1.35f)
        check(UserSituation.HURT_SOMEONE, hurtSomeoneRegex, 1.35f)
        check(UserSituation.SELF_BLAME, selfBlameRegex, 1.35f)

        // Batch 2: Scenario 8 (Sleep Difficulties & Night Rumination)
        check(UserSituation.CANNOT_SLEEP_INSOMNIA, cannotSleepInsomniaRegex, 1.4f)
        check(UserSituation.NIGHT_RUMINATION, nightRuminationRegex, 1.35f)
        check(UserSituation.LATE_NIGHT_LONELINESS, lateNightLonelinessRegex, 1.3f)

        // Batch 2: Scenario 9 (Social Anxiety & Fear of Judgement)
        check(UserSituation.MEETING_NEW_PEOPLE, meetingNewPeopleRegex, 1.35f)
        check(UserSituation.POST_EVENT_RUMINATION, postEventRuminationRegex, 1.35f)
        check(UserSituation.FEELING_AWKWARD, feelingAwkwardRegex, 1.35f)
        check(UserSituation.PUBLIC_SPEAKING_FEAR, publicSpeakingFearRegex, 1.3f)

        // Batch 2: Scenario 10 (Procrastination & Productivity Distress)
        check(UserSituation.PHONE_DOOMSCROLLING, phoneDoomscrollingRegex, 1.4f)
        check(UserSituation.TASK_OVERWHELM, taskOverwhelmRegex, 1.35f)
        check(UserSituation.AVOIDING_TASKS, avoidingTasksRegex, 1.35f)

        // Batch 3: Scenario 11 (Emotional Exhaustion & Burnout)
        check(UserSituation.ANHEDONIA_LOSS_OF_INTEREST, anhedoniaRegex, 1.4f)
        check(UserSituation.MENTAL_DRAIN, mentalDrainRegex, 1.35f)
        check(UserSituation.WITHDRAWAL_URGE, withdrawalUrgeRegex, 1.35f)
        check(UserSituation.RESPONSIBILITY_OVERWHELM, responsibilityOverwhelmRegex, 1.3f)

        // Batch 3: Scenario 12 (Financial Stress & Dependence)
        check(UserSituation.EXPENSE_MANAGEMENT, expenseManagementRegex, 1.35f)
        check(UserSituation.PARENTAL_FINANCIAL_DEPENDENCE, parentalFinancialDependenceRegex, 1.35f)
        check(UserSituation.PEER_WEALTH_COMPARISON, peerWealthComparisonRegex, 1.35f)

        // Batch 3: Scenario 13 (Life Transitions & Unfamiliar Environments)
        check(UserSituation.RELOCATING_NEW_CITY, relocatingNewCityRegex, 1.4f)
        check(UserSituation.COLLEGE_START, collegeStartRegex, 1.35f)
        check(UserSituation.NOSTALGIA_PAST, nostalgiaPastRegex, 1.35f)
        check(UserSituation.ROUTINE_LOSS, routineLossRegex, 1.3f)

        // Batch 3: Scenario 14 (Interpersonal Conflict & Boundary Setting)
        check(UserSituation.HEATED_ARGUMENT, heatedArgumentRegex, 1.35f)
        check(UserSituation.DISRESPECT_FELT, disrespectFeltRegex, 1.35f)

        // Batch 3: Scenario 15 (Rejection & Unreciprocated Feelings)
        check(UserSituation.ROMANTIC_REJECTION, romanticRejectionRegex, 1.4f)
        check(UserSituation.UNRECIPROCATED_FEELINGS, unreciprocatedFeelingsRegex, 1.35f)
        check(UserSituation.FEELING_UNWANTED_AFTER_NO, feelingUnwantedAfterNoRegex, 1.35f)

        // Batch 4: Scenario 16 (Grief and Loss)
        check(UserSituation.PET_LOSS, petLossRegex, 1.4f)
        check(UserSituation.ANNIVERSARY_GRIEF, anniversaryGriefRegex, 1.4f)
        check(UserSituation.ABSENCE_LONGING, absenceLongingRegex, 1.35f)

        // Batch 4: Scenario 17 (Body Image & Self-Confidence)
        check(UserSituation.APPEARANCE_INSECURITY, appearanceInsecurityRegex, 1.35f)
        check(UserSituation.WEIGHT_DISSATISFACTION, weightDissatisfactionRegex, 1.35f)
        check(UserSituation.PHOTO_ANXIETY, photoAnxietyRegex, 1.3f)

        // Batch 4: Scenario 18 (Positive Emotions & Celebration)
        check(UserSituation.ACADEMIC_ACHIEVEMENT, academicAchievementRegex, 1.35f)
        check(UserSituation.PERSONAL_GOAL_REACHED, personalGoalReachedRegex, 1.35f)
        check(UserSituation.GENERAL_GRATITUDE, generalGratitudeRegex, 1.3f)

        // Batch 4: Scenario 19 (Everyday Frustration & Casual Venting)
        check(UserSituation.BAD_DAY, badDayRegex, 1.35f)
        check(UserSituation.IRRITATED_SMALL_THINGS, irritatedSmallThingsRegex, 1.3f)
        check(UserSituation.CASUAL_VENTING, casualVentingRegex, 1.35f)

        // Batch 4: Scenario 20 (Romantic Relationship Support)
        check(UserSituation.RELATIONSHIP_INSECURITY, relationshipInsecurityRegex, 1.35f)
        check(UserSituation.PARTNER_DISTANCING, partnerDistancingRegex, 1.35f)
        check(UserSituation.FEAR_OF_ABANDONMENT, fearOfAbandonmentRegex, 1.35f)
        check(UserSituation.JEALOUSY, jealousyRegex, 1.3f)

        if (matches.isEmpty()) {
            return SituationRecognitionResult(null)
        }

        matches.sortByDescending { it.second }
        val primary = matches.first().first
        val secondary = matches.drop(1).map { it.first }

        return SituationRecognitionResult(
            primarySituation = primary,
            secondarySituations = secondary,
            matchedPhrase = primary.displayName,
            confidence = 0.94f
        )
    }
}
