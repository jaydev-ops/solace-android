// ============================================================================
// Solace+ Situational Context & Trigger Recognition Engine (Web / TypeScript)
// Dedicated recognizer for underlying situations and life triggers.
// ============================================================================

export type SituationGroup = 'RELATIONSHIPS' | 'ACADEMICS' | 'SOCIAL' | 'PERSONAL';

export type UserSituation =
  // Relationships
  | 'BREAKUP'
  | 'HEARTBREAK'
  | 'BEING_CHEATED_ON'
  | 'RELATIONSHIP_ARGUMENTS'
  | 'BEING_IGNORED'
  | 'BEING_REJECTED'
  | 'LONG_DISTANCE'
  | 'MISSING_EX'
  | 'FEAR_OF_LOSING_SOMEONE'
  | 'FEELING_UNWANTED'
  | 'EX_SOCIAL_MEDIA_CHECKING'
  // Academics
  | 'EXAM_FAILURE'
  | 'POOR_GRADES'
  | 'ASSIGNMENT_PRESSURE'
  | 'DISAPPOINTING_PARENTS'
  | 'DIFFICULTY_CONCENTRATING'
  | 'ACADEMIC_COMPARISON'
  // Social
  | 'LOSING_FRIENDS'
  | 'FEELING_EXCLUDED'
  | 'BEING_JUDGED'
  | 'SOCIAL_REJECTION'
  | 'FRIENDSHIP_CONFLICTS'
  | 'FRIENDSHIP_DRIFT'
  | 'ONE_SIDED_FRIENDSHIP'
  | 'THIRD_WHEEL'
  | 'PEER_ACHIEVEMENT_ENVY'
  | 'SOCIAL_MEDIA_ENVY'
  // Personal
  | 'FAMILY_CONFLICTS'
  | 'PARENTAL_CAREER_PRESSURE'
  | 'SIBLING_COMPARISON'
  | 'UNSUCCESSFUL_EFFORT'
  | 'FEAR_WRONG_DEGREE'
  | 'DIRECTIONLESS'
  | 'FINANCIAL_PRESSURE'
  | 'CAREER_UNCERTAINTY'
  | 'LOSS_OF_LOVED_ONE'
  | 'PERSONAL_DISAPPOINTMENT'
  // Batch 2: Scenario 6 (Misunderstood and Unheard)
  | 'PERSPECTIVE_DISMISSED'
  | 'LABELED_OVERLY_SENSITIVE'
  | 'COMMUNICATION_BREAKDOWN'
  // Batch 2: Scenario 7 (Regret and Guilt)
  | 'PAST_DECISION_REGRET'
  | 'HURT_SOMEONE'
  | 'SELF_BLAME'
  // Batch 2: Scenario 8 (Sleep Difficulties & Night Rumination)
  | 'CANNOT_SLEEP_INSOMNIA'
  | 'NIGHT_RUMINATION'
  | 'LATE_NIGHT_LONELINESS'
  // Batch 2: Scenario 9 (Social Anxiety & Fear of Judgement)
  | 'MEETING_NEW_PEOPLE'
  | 'POST_EVENT_RUMINATION'
  | 'FEELING_AWKWARD'
  | 'PUBLIC_SPEAKING_FEAR'
  // Batch 2: Scenario 10 (Procrastination & Productivity Distress)
  | 'PHONE_DOOMSCROLLING'
  | 'TASK_OVERWHELM'
  | 'AVOIDING_TASKS'
  // Batch 3: Scenario 11 (Emotional Exhaustion & Burnout)
  | 'MENTAL_DRAIN'
  | 'ANHEDONIA_LOSS_OF_INTEREST'
  | 'RESPONSIBILITY_OVERWHELM'
  | 'WITHDRAWAL_URGE'
  // Batch 3: Scenario 12 (Financial Stress & Dependence)
  | 'EXPENSE_MANAGEMENT'
  | 'PARENTAL_FINANCIAL_DEPENDENCE'
  | 'PEER_WEALTH_COMPARISON'
  // Batch 3: Scenario 13 (Life Transitions & Unfamiliar Environments)
  | 'COLLEGE_START'
  | 'RELOCATING_NEW_CITY'
  | 'ROUTINE_LOSS'
  | 'NOSTALGIA_PAST'
  // Batch 3: Scenario 14 (Interpersonal Conflict & Boundary Setting)
  | 'HEATED_ARGUMENT'
  | 'DISRESPECT_FELT'
  // Batch 3: Scenario 15 (Rejection & Unreciprocated Feelings)
  | 'ROMANTIC_REJECTION'
  | 'UNRECIPROCATED_FEELINGS'
  | 'FEELING_UNWANTED_AFTER_NO'
  // Batch 4: Scenario 16 (Grief and Loss)
  | 'PET_LOSS'
  | 'ANNIVERSARY_GRIEF'
  | 'ABSENCE_LONGING'
  // Batch 4: Scenario 17 (Body Image & Self-Confidence)
  | 'APPEARANCE_INSECURITY'
  | 'WEIGHT_DISSATISFACTION'
  | 'PHOTO_ANXIETY'
  // Batch 4: Scenario 18 (Positive Emotions & Celebration)
  | 'ACADEMIC_ACHIEVEMENT'
  | 'PERSONAL_GOAL_REACHED'
  | 'GENERAL_GRATITUDE'
  // Batch 4: Scenario 19 (Everyday Frustration & Casual Venting)
  | 'BAD_DAY'
  | 'IRRITATED_SMALL_THINGS'
  | 'CASUAL_VENTING'
  // Batch 4: Scenario 20 (Romantic Relationship Support)
  | 'RELATIONSHIP_INSECURITY'
  | 'PARTNER_DISTANCING'
  | 'FEAR_OF_ABANDONMENT'
  | 'JEALOUSY';

export interface SituationRecognitionResult {
  primarySituation: UserSituation | null;
  secondarySituations: UserSituation[];
  matchedPhrase?: string;
  confidence: number;
}

const BREAKUP_REGEX =
  /\b((going\s+through\s+a\s+|gng\s+thru\s+(a\s+)?)?break\s*up|broke\s+up|broken\s+up|breaking\s+up|dumped(\s+me)?|ended\s+(things|our\s+relationship|it)|they\s+left\s+me|she\s+left\s+me|he\s+left\s+me|split\s+up|separated|parted\s+ways|called\s+it\s+off|cut\s+ties|ex[-\s]?(partner|boyfriend|girlfriend|gf|bf))\b/i;

const MISSING_EX_REGEX =
  /\b(still\s+miss\s+(her|him|them|my\s+ex)|miss\s+(her|him|them|my\s+ex)\s+(so\s+much|terribly|a\s+lot)?|want\s+(her|him|them|my\s+ex)\s+back|wish\s+we\s+were\s+still\s+together|can'?t\s+get\s+(her|him|them)\s+out\s+of\s+my\s+head|reminisc(ing|e)\s+about\s+(her|him|us))\b/i;

const EX_SOCIAL_MEDIA_REGEX =
  /\b(check(ing)?\s+(her|his|their|my\s+ex['\s]?s?)\s+(instagram|insta|profile|stories|story|posts|feed|account|whatsapp|snapchat|activity|status)|stalk(ing)?\s+(her|his|their|my\s+ex)|look(ing)?\s+at\s+(her|his|their|our)\s+(profile|photos|pictures|stories|old\s+texts|old\s+chats)|reread(ing)?\s+(our|old)\s+(texts|messages|conversations))\b/i;

const CHEATED_ON_REGEX =
  /\b(cheat(ed|ing)?\s+(on\s+me)?|unfaithful|had\s+an\s+affair|betrayed\s+(me|my\s+trust)|slept\s+with\s+someone\s+else|saw\s+(him|her)\s+with\s+someone\s+else)\b/i;

const RELATIONSHIP_ARGUMENTS_REGEX =
  /\b(fight(ing)?\s+with\s+my\s+(partner|boyfriend|girlfriend|bf|gf|husband|wife)|argument\s+with\s+my\s+(partner|boyfriend|girlfriend)|we\s+keep\s+(fighting|arguing)|screaming\s+match|tense\s+between\s+us|toxic\s+relationship)\b/i;

const BEING_IGNORED_REGEX =
  /\b(ignor(ed|ing)\s+me|left\s+(me\s+)?on\s+read|won'?t\s+(reply|answer|text\s+back)|stopped\s+(replying|texting|talking\s+to\s+me)|ghosted(\s+me)?|silent\s+treatment)\b/i;

const BEING_REJECTED_REGEX =
  /\b(reject(ed|ion)?(\s+by\s+(her|him|them))?|said\s+no|turned\s+(me\s+)?down|didn'?t\s+want\s+to\s+go\s+out|confessed\s+and\s+got\s+rejected|friendzoned)\b/i;

const LONG_DISTANCE_REGEX =
  /\b(long\s+distance|ldr|miles\s+apart|different\s+time\s+zones|different\s+cities|can'?t\s+see\s+each\s+other|living\s+far\s+away)\b/i;

const FEAR_OF_LOSING_REGEX =
  /\b(scared\s+(of\s+)?los(ing|e)\s+(her|him|them|my\s+partner)|terrified\s+they'?ll\s+leave|afraid\s+(she|he|they)\s+will\s+walk\s+away|feel\s+like\s+i'?m\s+losing\s+(him|her))\b/i;

const FEELING_UNWANTED_REGEX =
  /\b(feel(ing)?\s+unwanted|nobody\s+wants\s+me|unlovable|not\s+good\s+enough\s+for\s+(anyone|her|him)|they\s+don'?t\s+care\s+about\s+me)\b/i;

// Academics
const EXAM_FAILURE_REGEX =
  /\b(fail(ed|ing)?\s+(my\s+)?(exam|test|finals|midterms|paper|course|class)s?|flunk(ed|ing)?|got\s+an\s+f|couldn'?t\s+pass|messed\s+up\s+(my\s+)?(exam|test|finals)|made\s+terrible\s+mistakes)\b/i;

const POOR_GRADES_REGEX =
  /\b(poor\s+grades|bad\s+grades|grades\s+(are\s+)?(slipping|dropping|terrible|bad)|low\s+marks|ruined\s+my\s+gpa|cgpa\s+dropped|academic\s+probation)\b/i;

const ASSIGNMENT_PRESSURE_REGEX =
  /\b(assignment(s)?\s+(due|piling\s+up|pressure)|deadline\s+(is\s+)?tomorrow|so\s+much\s+homework|projects\s+due|behind\s+on\s+assignments)\b/i;

const DISAPPOINTING_PARENTS_REGEX =
  /\b(disappoint(ing)?\s+(my\s+)?(parents|mom|dad|family)|parents\s+(will\s+be\s+mad|expect\s+so\s+much|disappointed)|pressure\s+from\s+parents\s+for\s+grades)\b/i;

const DIFFICULTY_CONCENTRATING_REGEX =
  /\b(can'?t\s+(concentrate|focus|study)|mind\s+keeps\s+wandering|staring\s+at\s+the\s+page|brain\s+fog\s+while\s+studying|unable\s+to\s+retain)\b/i;

const ACADEMIC_COMPARISON_REGEX =
  /\b(everyone\s+(is\s+)?smarter\s+than\s+me|comparing\s+my\s+grades|falling\s+behind\s+everyone\s+else|classmates\s+are\s+doing\s+better)\b/i;

// Social
const LOSING_FRIENDS_REGEX =
  /\b(los(ing|t)\s+(my\s+)?(friends|best\s+friend)|lost\s+touch\s+with\s+(my\s+)?(childhood\s+)?friends?|drifting\s+apart\s+from\s+friends|no\s+longer\s+close|friends\s+abandoned\s+me)\b/i;

const FEELING_EXCLUDED_REGEX =
  /\b(left\s+out|feel(ing)?\s+excluded|hung\s+out\s+without\s+me|not\s+invited|didn'?t\s+invite\s+me|made\s+plans\s+without\s+me)\b/i;

const BEING_JUDGED_REGEX =
  /\b(being\s+judged|people\s+judging\s+me|talking\s+behind\s+my\s+back|everyone\s+is\s+staring|judged\s+for\s+how\s+i\s+look)\b/i;

const SOCIAL_REJECTION_REGEX =
  /\b(social\s+rejection|nobody\s+wants\s+to\s+sit\s+with\s+me|unwelcome|pushed\s+out\s+of\s+the\s+group|shunned)\b/i;

const FRIENDSHIP_CONFLICTS_REGEX =
  /\b(fight\s+with\s+my\s+(friend|best\s+friend)|friend\s+drama|friend\s+betrayed\s+me|arguing\s+with\s+my\s+friend)\b/i;

const FRIENDSHIP_DRIFT_REGEX =
  /\b(best\s+friend\s+(distancing|ignoring|leaving|replaced\s+me)|drift(ing)?\s+apart\s+from\s+(my\s+)?friend|distancing\s+(themselves|themself)|friends\s+drifting)\b/i;

const ONE_SIDED_FRIENDSHIP_REGEX =
  /\b(one[-\s]sided\s+friendship|only\s+one\s+(texting|reaching\s+out|trying|putting\s+effort)|don'?t\s+even\s+text\s+me\s+first|unequal\s+effort)\b/i;

const THIRD_WHEEL_REGEX =
  /\b(third\s+wheel|friends\s+making\s+plans\s+without\s+me|left\s+me\s+out\s+of\s+plans)\b/i;

const PARENTAL_CAREER_PRESSURE_REGEX =
  /\b(parents\s+(don'?t\s+understand|never\s+understand|hate|disapprove\s+of)\s+(my\s+)?(career|passion|degree|choices?)|parents\s+never\s+understand\s+me|want\s+me\s+to\s+do\s+something\s+i\s+don'?t\s+(even\s+)?like|pressure\s+to\s+become\s+a\s+(doctor|engineer|lawyer)|can'?t\s+even\s+explain\s+my\s+side)\b/i;

const SIBLING_COMPARISON_REGEX =
  /\b(compar(ed?|ing|es)?\s+(me\s+)?(to|with)\s+my\s+(brother|sister|sibling)s?|golden\s+child|always\s+comparing\s+us)\b/i;

const UNSUCCESSFUL_EFFORT_REGEX =
  /\b(worked\s+so\s+hard\s+and\s+still\s+failed|put\s+everything\s+into\s+it\s+and\s+failed|hard\s+work\s+produced\s+no\s+results|everyone\s+else\s+passed|failed\s+again|not\s+capable|maybe\s+i'?m\s+just\s+not\s+capable|rejected\s+from\s+(the\s+)?(program|job|university|college|internship|school)|worked\s+(for\s+)?(months|weeks|years)\s+for)\b/i;

const FEAR_WRONG_DEGREE_REGEX =
  /\b(wrong\s+(degree|major|field\s+of\s+study|course)|picked\s+the\s+wrong\s+(degree|major)|regret(ting)?\s+my\s+(degree|major)|hate\s+my\s+(major|degree)|terrified\s+i\s+picked\s+the\s+wrong)\b/i;

const CAREER_DIRECTIONLESS_REGEX =
  /\b(no\s+idea\s+what\s+i'?m\s+doing\s+with\s+my\s+life|career\s+direction|directionless|fear\s+of\s+choosing\s+the\s+wrong|everyone\s+around\s+me\s+has\s+a\s+plan|scared\s+i'?ll\s+make\s+the\s+wrong\s+decision)\b/i;

const SOCIAL_MEDIA_ENVY_REGEX =
  /\b(social\s+media(\s+envy|\s+makes\s+me)?|instagram|feed|scroll(ing)?\s+(on\s+)?(instagram|tiktok|social\s+media)|curated\s+highlight\s+reel|see(ing)?\s+people\s+travel(l)?ing(\s+and\s+earning)?|wasting\s+my\s+life)\b/i;

const PEER_ACHIEVEMENT_ENVY_REGEX =
  /\b(peers?\s+(earning|buying|succeeding|getting\s+promoted|achieving)|see(ing)?\s+people\s+(my\s+age\s+)?(earning|buying)|everyone\s+(my\s+age|around\s+me)\s+is\s+(doing\s+better|smarter|ahead|happier|doing\s+something\s+meaningful)|falling\s+behind\s+(everyone|in\s+life)|feel\s+so\s+inferior)\b/i;

// Personal
const FAMILY_CONFLICTS_REGEX =
  /\b(family\s+(conflict|drama|problems)|parents\s+(are\s+)?fighting|screaming\s+at\s+home|toxic\s+household|dad\s+is\s+yelling|mom\s+is\s+screaming|family\s+dismiss(es|ed|ing)?|(family|parents)\s+dismiss(es|ed|ing)?(\s+my\s+feelings)?|unseen|controlled\s+by\s+my\s+parents|feeling\s+unseen)\b/i;

const FINANCIAL_PRESSURE_REGEX =
  /\b(financial\s+(stress|pressure|trouble)|money\s+problems|can'?t\s+afford|rent\s+is\s+due|debt|running\s+out\s+of\s+money|broke\s+and\s+stressed)\b/i;

const CAREER_UNCERTAINTY_REGEX =
  /\b(career\s+(uncertainty|stress|anxiety|pressure)|don'?t\s+know\s+what\s+to\s+do\s+with\s+my\s+life|job\s+hunting|lost\s+my\s+job|laid\s+off|unemployed|future\s+career)\b/i;

const LOSS_OF_LOVED_ONE_REGEX =
  /\b(passed\s+away|funeral|lost\s+my\s+(grandmother|grandfather|grandma|grandpa|friend|dad|mom|father|mother|pet|dog|cat)|death\s+of\s+my)\b/i;

const PERSONAL_DISAPPOINTMENT_REGEX =
  /\b(disappointed\s+in\s+myself|hate\s+who\s+i\s+am|let\s+myself\s+down|failed\s+myself|messed\s+up\s+my\s+life)\b/i;

// --- BATCH 2 REGEXES ---
// Scenario 6: Misunderstood and Unheard
const PERSPECTIVE_DISMISSED_REGEX =
  /\b(people\s+dismiss(\s+my\s+feelings)?|feelings?\s+(are\s+|get\s+)?dismissed|dismiss(ed|ing)?\s+(what\s+i\s+say|how\s+i\s+feel|my\s+feelings)|invalidat(ed|ing)|nobody\s+cares\s+how\s+i\s+feel)\b/i;

const LABELED_OVERLY_SENSITIVE_REGEX =
  /\b((label(l)?ed|called|told\s+i'?m)\s+(too|overly)\s+sensitive|overreacting|making\s+a\s+big\s+deal\s+out\s+of\s+nothing|too\s+emotional|drama\s+queen)\b/i;

const COMMUNICATION_BREAKDOWN_REGEX =
  /\b(nobody\s+understands\s+(what\s+i'?m\s+(trying\s+to\s+)?say(ing)?|me)|words\s+get\s+twisted|stopped\s+explaining\s+myself|invisible\s+in\s+conversations|talk(ing)?\s+to\s+a\s+brick\s+wall|don'?t\s+even\s+bother\s+explaining|interpret\s+it\s+differently|nobody\s+listens|misunderstood)\b/i;

// Scenario 7: Regret and Guilt
const PAST_DECISION_REGRET_REGEX =
  /\b(wish\s+i\s+could\s+(go\s+back|change\s+what\s+i\s+said|undo|rewind)|ruined\s+everything|so\s+much\s+regret|regret\s+(what\s+i\s+did|my\s+words|saying\s+that)|wish\s+i\s+never\s+said)\b/i;

const HURT_SOMEONE_REGEX =
  /\b(hurt\s+someone(\s+who\s+(genuinely\s+)?cared)?|caused\s+(someone|them)\s+pain|broke\s+their\s+heart|made\s+(them|someone)\s+cry|damaged\s+(the\s+)?relationship|treated\s+them\s+badly)\b/i;

const SELF_BLAME_REGEX =
  /\b(never\s+forgive\s+myself|can'?t\s+forgive\s+myself|haunted\s+by\s+what\s+i\s+did|guilt\s+is\s+eating\s+me|replaying\s+(the\s+mistake|my\s+actions)|all\s+my\s+fault|hate\s+myself\s+for\s+it|eat(ing)?\s+me\s+up\s+inside)\b/i;

// Scenario 8: Sleep Difficulties & Night Rumination
const CANNOT_SLEEP_INSOMNIA_REGEX =
  /\b(3\s*am\s+and\s+(still\s+can'?t\s+sleep|wide\s+awake)|can'?t\s+(fall\s+asleep|sleep)|wide\s+awake\s+at\s+night|tossing\s+and\s+turning|insomnia|trouble\s+falling\s+asleep|staring\s+at\s+the\s+ceiling)\b/i;

const NIGHT_RUMINATION_REGEX =
  /\b(brain\s+won'?t\s+(stop|shut\s+off)(\s+thinking)?|racing\s+thoughts\s+at\s+night|mind\s+won'?t\s+quiet\s+down|nighttime\s+(anxiety|overthinking)|replaying\s+everything\s+at\s+night|thinking\s+about\s+everything\s+that\s+went\s+wrong)\b/i;

const LATE_NIGHT_LONELINESS_REGEX =
  /\b(dreading\s+waking\s+up\s+tomorrow|dreading\s+tomorrow|late\s+night\s+loneliness|alone\s+at\s+night|middle\s+of\s+the\s+night)\b/i;

// Scenario 9: Social Anxiety & Fear of Judgement
const MEETING_NEW_PEOPLE_REGEX =
  /\b(feel\s+awkward\s+around\s+new\s+people|nervous\s+around\s+new\s+people|hard\s+to\s+talk\s+to\s+strangers|don'?t\s+know\s+what\s+to\s+say|awkward\s+in\s+conversations|small\s+talk\s+terrifies\s+me)\b/i;

const POST_EVENT_RUMINATION_REGEX =
  /\b(they'?ll\s+find\s+me\s+boring|think\s+i'?m\s+boring|replaying\s+the\s+conversation|dissecting\s+every\s+word|overthinking\s+what\s+i\s+said|sounding\s+stupid|embarrassed\s+myself\s+in\s+front\s+of)\b/i;

const FEELING_AWKWARD_REGEX =
  /\b(social\s+anxiety|afraid\s+of\s+being\s+judged|fear\s+of\s+(being\s+)?judged|scared\s+to\s+speak\s+up|freezing\s+(up\s+)?in\s+groups|spotlight\s+effect|people\s+evaluating\s+me|hyper[-\s]?aware\s+in\s+public)\b/i;

const PUBLIC_SPEAKING_FEAR_REGEX =
  /\b(public\s+speaking(\s+fear|\s+anxiety)?|presentation\s+anxiety|speaking\s+in\s+front\s+of\s+(people|class|crowd)|stage\s+fright)\b/i;

// Scenario 10: Procrastination & Productivity Distress
const PHONE_DOOMSCROLLING_REGEX =
  /\b(wasted\s+(the\s+entire|my\s+whole|all)\s+day\s+scrolling|scrolling\s+(reels|tiktok|shorts|feed|phone)|doomscroll(ing)?|phone\s+addiction|spent\s+hours\s+on\s+my\s+phone)\b/i;

const TASK_OVERWHELM_REGEX =
  /\b(so\s+much\s+work\s+pending|mountain\s+of\s+work|pile\s+of\s+work|paralyzed\s+by\s+work|executive\s+dysfunction|too\s+many\s+tasks|work\s+is\s+piling\s+up)\b/i;

const AVOIDING_TASKS_REGEX =
  /\b(can'?t\s+get\s+myself\s+to\s+start|procrastinat(ing|ion)|guilty\s+about\s+wasting\s+time|putting\s+off\s+(my\s+)?work|delaying\s+everything|unable\s+to\s+start|avoiding\s+(my\s+)?tasks)\b/i;

// --- BATCH 3 REGEXES ---
// Scenario 11: Emotional Exhaustion & Burnout
const MENTAL_DRAIN_REGEX =
  /\b(mentally\s+drained|running\s+on\s+empty|no\s+energy\s+left|chronic\s+fatigue|exhausted\s+to\s+my\s+bones)\b/i;
const ANHEDONIA_REGEX =
  /\b(things\s+i\s+enjoy\s+feel\s+like\s+work|lost\s+interest\s+in\s+everything|nothing\s+brings\s+me\s+joy|hobbies\s+feel\s+like\s+chores|anhedonia)\b/i;
const RESPONSIBILITY_OVERWHELM_REGEX =
  /\b(tired\s+of\s+everything|tired\s+despite\s+sleeping|overwhelmed\s+by\s+responsibilities|carrying\s+so\s+much|burnout)\b/i;
const WITHDRAWAL_URGE_REGEX =
  /\b(just\s+want\s+to\s+be\s+left\s+alone|want\s+to\s+withdraw|emotionally\s+unavailable|isolate\s+myself|don'?t\s+want\s+to\s+talk\s+to\s+anyone)\b/i;

// Scenario 12: Financial Stress & Dependence
const PARENTAL_FINANCIAL_DEPENDENCE_REGEX =
  /\b(feel\s+bad\s+asking\s+(my\s+)?parents\s+for\s+money|guilt\s+asking\s+parents|financial(ly)?\s+dependent|burden\s+on\s+my\s+parents|asking\s+for\s+financial\s+help)\b/i;
const PEER_WEALTH_COMPARISON_REGEX =
  /\b(everyone\s+around\s+me\s+earns|classmates\s+earning|peers\s+making\s+money|not\s+contributing\s+anything|compare\s+my\s+bank\s+account)\b/i;
const EXPENSE_MANAGEMENT_REGEX =
  /\b(can'?t\s+afford\s+anything|guilty\s+about\s+spending|debt\s+worries|financial\s+burden|every\s+expense\s+feels\s+like\s+a\s+threat)\b/i;

// Scenario 13: Life Transitions & Unfamiliarity
const COLLEGE_START_REGEX =
  /\b(started\s+(college|university|a\s+new\s+job)|recently\s+started\s+(college|university|job)|don'?t\s+feel\s+like\s+i\s+belong\s+here|imposter\s+in\s+college|new\s+campus)\b/i;
const NOSTALGIA_PAST_REGEX =
  /\b(miss\s+my\s+school\s+friends|miss\s+my\s+old\s+life|homesick(ness)?|longing\s+for\s+my\s+old\s+routine|miss\s+home)\b/i;
const ROUTINE_LOSS_REGEX =
  /\b(everything\s+feels\s+unfamiliar|lost\s+my\s+routine|disorienting\s+change|unfamiliar\s+environment)\b/i;
const RELOCATING_NEW_CITY_REGEX =
  /\b(moved\s+to\s+a\s+new\s+city|relocated|living\s+in\s+a\s+new\s+place|new\s+city)\b/i;

// Scenario 14: Interpersonal Conflict & Boundaries
const HEATED_ARGUMENT_REGEX =
  /\b(had\s+a\s+huge\s+argument(\s+with)?|said\s+things\s+i\s+shouldn'?t\s+have|screaming\s+match|we\s+had\s+a\s+fight|spoke\s+out\s+of\s+anger)\b/i;
const DISRESPECT_FELT_REGEX =
  /\b(they\s+were\s+wrong\s+too|disrespected(\s+in\s+argument)?|both\s+sides\s+made\s+mistakes|boundary\s+was\s+crossed|need\s+boundaries)\b/i;

// Scenario 15: Rejection & Unreciprocated Feelings
const ROMANTIC_REJECTION_REGEX =
  /\b(told\s+someone\s+i\s+liked\s+them\s+and\s+they\s+rejected\s+me|confessed\s+and\s+got\s+rejected|confessed\s+my\s+feelings|rejected\s+after\s+confessing)\b/i;
const UNRECIPROCATED_FEELINGS_REGEX =
  /\b(feel\s+so\s+embarrassed(\s+after)?|unreciprocated\s+(feelings|love)|embarrassment\s+of\s+rejection|shared\s+my\s+feelings\s+and\s+got\s+turned\s+down)\b/i;
const FEELING_UNWANTED_AFTER_NO_REGEX =
  /\b(maybe\s+i'?m\s+just\s+not\s+good\s+enough|they\s+said\s+no(\s+to\s+me)?|nobody\s+wants\s+me|friendzoned|unlovable\s+after\s+rejection)\b/i;

// --- BATCH 4 REGEXES ---
// Scenario 16: Grief and Loss
const ABSENCE_LONGING_REGEX =
  /\b(wish\s+i\s+could\s+talk\s+to\s+(him|her|them)\s+one\s+more\s+time|remembering\s+things\s+we\s+used\s+to\s+do|empty\s+without\s+(him|her|them)|miss\s+my\s+(grandfather|grandmother|grandpa|grandma))\b/i;
const PET_LOSS_REGEX =
  /\b(lost\s+my\s+(pet|dog|cat)|(dog|cat|pet)\s+(passed\s+away|died)|put\s+my\s+(dog|cat|pet)\s+down)\b/i;
const ANNIVERSARY_GRIEF_REGEX =
  /\b(anniversary\s+of\s+(their\s+)?(death|passing)|first\s+(holiday|birthday|christmas|diwali)\s+without\s+(them|him|her))\b/i;

// Scenario 17: Body Image & Self-Confidence
const APPEARANCE_INSECURITY_REGEX =
  /\b(hate\s+how\s+i\s+look|feel\s+ugly|hate\s+my\s+(face|skin|features)|unattractive|insecure\s+about\s+my\s+looks)\b/i;
const WEIGHT_DISSATISFACTION_REGEX =
  /\b(disgusted\s+with\s+my\s+body|insecure\s+about\s+my\s+(weight|body|size)|hate\s+my\s+body|body\s+dysmorphia)\b/i;
const PHOTO_ANXIETY_REGEX =
  /\b(everyone\s+looks\s+better\s+than\s+me|uncomfortable\s+in\s+photos|hate\s+(taking|being\s+in)\s+photos|don'?t\s+feel\s+confident\s+going\s+outside|dread\s+pictures)\b/i;

// Scenario 18: Positive Emotions & Celebration
const ACADEMIC_ACHIEVEMENT_REGEX =
  /\b(finally\s+passed\s+(my\s+)?(exam|test|class|course|finals)|passed\s+my\s+(exam|test)|got\s+accepted(\s+into)?|cleared\s+the\s+exam)\b/i;
const PERSONAL_GOAL_REACHED_REGEX =
  /\b(studied\s+so\s+hard\s+and\s+it\s+paid\s+off|got\s+the\s+job|achieved\s+my\s+goal|overcame\s+my\s+fear|hard\s+work\s+paid\s+off)\b/i;
const GENERAL_GRATITUDE_REGEX =
  /\b(actually\s+proud\s+of\s+myself|proud\s+of\s+myself|celebrating|wonderful\s+day|really\s+happy\s+today|celebrate\s+with\s+me)\b/i;

// Scenario 19: Everyday Frustration & Casual Venting
const BAD_DAY_REGEX =
  /\b(today\s+has\s+been\s+(absolutely\s+)?terrible|horrible\s+day|awful\s+day|terrible\s+day|annoying\s+day)\b/i;
const IRRITATED_SMALL_THINGS_REGEX =
  /\b(everything\s+is\s+just\s+irritating\s+me|everything\s+is\s+irritating|irritated\s+by\s+small\s+things|everyone\s+is\s+annoying\s+me|snapping\s+at\s+everyone)\b/i;
const CASUAL_VENTING_REGEX =
  /\b(don'?t\s+even\s+know\s+why\s+i'?m\s+annoyed|bad\s+mood|grumpy|stuck\s+in\s+a\s+rut|bored\s+and\s+frustrated|just\s+need\s+to\s+vent)\b/i;

// Scenario 20: Romantic Relationship Support
const RELATIONSHIP_INSECURITY_REGEX =
  /\b(partner\s+(doesn'?t\s+love\s+me|(is\s+)?growing\s+distant|is\s+distant)|growing\s+distant(\s+from\s+partner)?|feel\s+like\s+my\s+partner\s+doesn'?t\s+love\s+me|trust\s+issues\s+in\s+my\s+relationship|relationship\s+insecurity)\b/i;
const PARTNER_DISTANCING_REGEX =
  /\b(partner\s+barely\s+gives?\s+me\s+attention|barely\s+gives?\s+me\s+attention|don'?t\s+want\s+to\s+seem\s+needy|unmet\s+emotional\s+connection)\b/i;
const JEALOUSY_REGEX =
  /\b(jealous\s+when\s+they\s+spend\s+time|jealous\s+of\s+their\s+friends|partner\s+spends\s+time\s+with\s+(others|other\s+people)|relationship\s+jealousy)\b/i;
const FEAR_OF_ABANDONMENT_REGEX =
  /\b(scared\s+my\s+partner\s+will\s+leave|fear\s+of\s+abandonment|terrified\s+of\s+losing\s+my\s+partner)\b/i;

export class SituationalContextEngine {
  static detectSituation(message: string): SituationRecognitionResult {
    const trimmed = message.trim();
    if (!trimmed) {
      return { primarySituation: null, secondarySituations: [], confidence: 0 };
    }

    const matches: { situation: UserSituation; weight: number }[] = [];

    const check = (situation: UserSituation, regex: RegExp, weight: number) => {
      if (regex.test(trimmed)) {
        matches.push({ situation, weight });
      }
    };

    // Relationships
    check('EX_SOCIAL_MEDIA_CHECKING', EX_SOCIAL_MEDIA_REGEX, 1.4);
    check('MISSING_EX', MISSING_EX_REGEX, 1.35);
    check('BEING_CHEATED_ON', CHEATED_ON_REGEX, 1.3);
    check('BREAKUP', BREAKUP_REGEX, 1.3);
    check('RELATIONSHIP_ARGUMENTS', RELATIONSHIP_ARGUMENTS_REGEX, 1.2);
    check('BEING_IGNORED', BEING_IGNORED_REGEX, 1.2);
    check('BEING_REJECTED', BEING_REJECTED_REGEX, 1.2);
    check('LONG_DISTANCE', LONG_DISTANCE_REGEX, 1.1);
    check('FEAR_OF_LOSING_SOMEONE', FEAR_OF_LOSING_REGEX, 1.15);
    check('FEELING_UNWANTED', FEELING_UNWANTED_REGEX, 1.1);

    // Academics
    check('DISAPPOINTING_PARENTS', DISAPPOINTING_PARENTS_REGEX, 1.35);
    check('UNSUCCESSFUL_EFFORT', UNSUCCESSFUL_EFFORT_REGEX, 1.35);
    check('EXAM_FAILURE', EXAM_FAILURE_REGEX, 1.3);
    check('POOR_GRADES', POOR_GRADES_REGEX, 1.25);
    check('ASSIGNMENT_PRESSURE', ASSIGNMENT_PRESSURE_REGEX, 1.2);
    check('DIFFICULTY_CONCENTRATING', DIFFICULTY_CONCENTRATING_REGEX, 1.15);
    check('ACADEMIC_COMPARISON', ACADEMIC_COMPARISON_REGEX, 1.1);

    // Social
    check('FRIENDSHIP_DRIFT', FRIENDSHIP_DRIFT_REGEX, 1.35);
    check('ONE_SIDED_FRIENDSHIP', ONE_SIDED_FRIENDSHIP_REGEX, 1.3);
    check('THIRD_WHEEL', THIRD_WHEEL_REGEX, 1.25);
    check('SOCIAL_MEDIA_ENVY', SOCIAL_MEDIA_ENVY_REGEX, 1.35);
    check('PEER_ACHIEVEMENT_ENVY', PEER_ACHIEVEMENT_ENVY_REGEX, 1.35);
    check('LOSING_FRIENDS', LOSING_FRIENDS_REGEX, 1.25);
    check('FEELING_EXCLUDED', FEELING_EXCLUDED_REGEX, 1.2);
    check('FRIENDSHIP_CONFLICTS', FRIENDSHIP_CONFLICTS_REGEX, 1.2);
    check('BEING_JUDGED', BEING_JUDGED_REGEX, 1.15);
    check('SOCIAL_REJECTION', SOCIAL_REJECTION_REGEX, 1.15);

    // Personal
    check('PARENTAL_CAREER_PRESSURE', PARENTAL_CAREER_PRESSURE_REGEX, 1.35);
    check('SIBLING_COMPARISON', SIBLING_COMPARISON_REGEX, 1.3);
    check('FEAR_WRONG_DEGREE', FEAR_WRONG_DEGREE_REGEX, 1.35);
    check('DIRECTIONLESS', CAREER_DIRECTIONLESS_REGEX, 1.3);
    check('LOSS_OF_LOVED_ONE', LOSS_OF_LOVED_ONE_REGEX, 1.4);
    check('FAMILY_CONFLICTS', FAMILY_CONFLICTS_REGEX, 1.25);
    check('FINANCIAL_PRESSURE', FINANCIAL_PRESSURE_REGEX, 1.2);
    check('CAREER_UNCERTAINTY', CAREER_UNCERTAINTY_REGEX, 1.2);
    check('PERSONAL_DISAPPOINTMENT', PERSONAL_DISAPPOINTMENT_REGEX, 1.15);

    // Batch 2: Scenario 6 (Misunderstood and Unheard)
    check('PERSPECTIVE_DISMISSED', PERSPECTIVE_DISMISSED_REGEX, 1.35);
    check('LABELED_OVERLY_SENSITIVE', LABELED_OVERLY_SENSITIVE_REGEX, 1.35);
    check('COMMUNICATION_BREAKDOWN', COMMUNICATION_BREAKDOWN_REGEX, 1.35);

    // Batch 2: Scenario 7 (Regret and Guilt)
    check('PAST_DECISION_REGRET', PAST_DECISION_REGRET_REGEX, 1.35);
    check('HURT_SOMEONE', HURT_SOMEONE_REGEX, 1.35);
    check('SELF_BLAME', SELF_BLAME_REGEX, 1.35);

    // Batch 2: Scenario 8 (Sleep Difficulties & Night Rumination)
    check('CANNOT_SLEEP_INSOMNIA', CANNOT_SLEEP_INSOMNIA_REGEX, 1.4);
    check('NIGHT_RUMINATION', NIGHT_RUMINATION_REGEX, 1.35);
    check('LATE_NIGHT_LONELINESS', LATE_NIGHT_LONELINESS_REGEX, 1.3);

    // Batch 2: Scenario 9 (Social Anxiety & Fear of Judgement)
    check('MEETING_NEW_PEOPLE', MEETING_NEW_PEOPLE_REGEX, 1.35);
    check('POST_EVENT_RUMINATION', POST_EVENT_RUMINATION_REGEX, 1.35);
    check('FEELING_AWKWARD', FEELING_AWKWARD_REGEX, 1.35);
    check('PUBLIC_SPEAKING_FEAR', PUBLIC_SPEAKING_FEAR_REGEX, 1.3);

    // Batch 2: Scenario 10 (Procrastination & Productivity Distress)
    check('PHONE_DOOMSCROLLING', PHONE_DOOMSCROLLING_REGEX, 1.4);
    check('TASK_OVERWHELM', TASK_OVERWHELM_REGEX, 1.35);
    check('AVOIDING_TASKS', AVOIDING_TASKS_REGEX, 1.35);

    // Batch 3: Scenario 11 (Emotional Exhaustion & Burnout)
    check('ANHEDONIA_LOSS_OF_INTEREST', ANHEDONIA_REGEX, 1.4);
    check('MENTAL_DRAIN', MENTAL_DRAIN_REGEX, 1.35);
    check('WITHDRAWAL_URGE', WITHDRAWAL_URGE_REGEX, 1.35);
    check('RESPONSIBILITY_OVERWHELM', RESPONSIBILITY_OVERWHELM_REGEX, 1.3);

    // Batch 3: Scenario 12 (Financial Stress & Dependence)
    check('EXPENSE_MANAGEMENT', EXPENSE_MANAGEMENT_REGEX, 1.35);
    check('PARENTAL_FINANCIAL_DEPENDENCE', PARENTAL_FINANCIAL_DEPENDENCE_REGEX, 1.35);
    check('PEER_WEALTH_COMPARISON', PEER_WEALTH_COMPARISON_REGEX, 1.35);

    // Batch 3: Scenario 13 (Life Transitions & Unfamiliar Environments)
    check('RELOCATING_NEW_CITY', RELOCATING_NEW_CITY_REGEX, 1.4);
    check('COLLEGE_START', COLLEGE_START_REGEX, 1.35);
    check('NOSTALGIA_PAST', NOSTALGIA_PAST_REGEX, 1.35);
    check('ROUTINE_LOSS', ROUTINE_LOSS_REGEX, 1.3);

    // Batch 3: Scenario 14 (Interpersonal Conflict & Boundary Setting)
    check('HEATED_ARGUMENT', HEATED_ARGUMENT_REGEX, 1.35);
    check('DISRESPECT_FELT', DISRESPECT_FELT_REGEX, 1.35);

    // Batch 3: Scenario 15 (Rejection & Unreciprocated Feelings)
    check('ROMANTIC_REJECTION', ROMANTIC_REJECTION_REGEX, 1.4);
    check('UNRECIPROCATED_FEELINGS', UNRECIPROCATED_FEELINGS_REGEX, 1.35);
    check('FEELING_UNWANTED_AFTER_NO', FEELING_UNWANTED_AFTER_NO_REGEX, 1.35);

    // Batch 4: Scenario 16 (Grief and Loss)
    check('PET_LOSS', PET_LOSS_REGEX, 1.4);
    check('ANNIVERSARY_GRIEF', ANNIVERSARY_GRIEF_REGEX, 1.4);
    check('ABSENCE_LONGING', ABSENCE_LONGING_REGEX, 1.35);

    // Batch 4: Scenario 17 (Body Image & Self-Confidence)
    check('APPEARANCE_INSECURITY', APPEARANCE_INSECURITY_REGEX, 1.35);
    check('WEIGHT_DISSATISFACTION', WEIGHT_DISSATISFACTION_REGEX, 1.35);
    check('PHOTO_ANXIETY', PHOTO_ANXIETY_REGEX, 1.3);

    // Batch 4: Scenario 18 (Positive Emotions & Celebration)
    check('ACADEMIC_ACHIEVEMENT', ACADEMIC_ACHIEVEMENT_REGEX, 1.35);
    check('PERSONAL_GOAL_REACHED', PERSONAL_GOAL_REACHED_REGEX, 1.35);
    check('GENERAL_GRATITUDE', GENERAL_GRATITUDE_REGEX, 1.3);

    // Batch 4: Scenario 19 (Everyday Frustration & Casual Venting)
    check('BAD_DAY', BAD_DAY_REGEX, 1.35);
    check('IRRITATED_SMALL_THINGS', IRRITATED_SMALL_THINGS_REGEX, 1.3);
    check('CASUAL_VENTING', CASUAL_VENTING_REGEX, 1.35);

    // Batch 4: Scenario 20 (Romantic Relationship Support)
    check('RELATIONSHIP_INSECURITY', RELATIONSHIP_INSECURITY_REGEX, 1.35);
    check('PARTNER_DISTANCING', PARTNER_DISTANCING_REGEX, 1.35);
    check('FEAR_OF_ABANDONMENT', FEAR_OF_ABANDONMENT_REGEX, 1.35);
    check('JEALOUSY', JEALOUSY_REGEX, 1.3);

    if (matches.length === 0) {
      return { primarySituation: null, secondarySituations: [], confidence: 0 };
    }

    matches.sort((a, b) => b.weight - a.weight);

    return {
      primarySituation: matches[0].situation,
      secondarySituations: matches.slice(1).map((m) => m.situation),
      matchedPhrase: matches[0].situation,
      confidence: 0.94
    };
  }
}
