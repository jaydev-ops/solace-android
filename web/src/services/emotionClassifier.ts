// ============================================================================
// Solace+ Deterministic Emotional Classification Engine (Web / TypeScript)
// 32 Emotional Categories with Idiom, Negation, and Entity Disambiguation
// ============================================================================

import { SituationalContextEngine } from './situationalContext.ts';

export type EmotionCategory =
  | 'IMMEDIATE_PHYSICAL_DANGER'
  | 'SUICIDAL_IDEATION'
  | 'SELF_HARM'
  | 'PANIC'
  | 'ANXIETY'
  | 'OVERTHINKING'
  | 'SADNESS'
  | 'LONELINESS'
  | 'DEPRESSION_DISTRESS'
  | 'GRIEF'
  | 'EMOTIONAL_NUMBNESS'
  | 'EMOTIONAL_EXHAUSTION'
  | 'BURNOUT'
  | 'ACADEMIC_STRESS'
  | 'WORK_RELATED_STRESS'
  | 'RELATIONSHIP_PROBLEMS'
  | 'HEARTBREAK'
  | 'REJECTION'
  | 'FAMILY_CONFLICT'
  | 'SOCIAL_ANXIETY'
  | 'ANGER'
  | 'FRUSTRATION'
  | 'LOW_SELF_ESTEEM'
  | 'INSECURITY'
  | 'FEAR'
  | 'GUILT'
  | 'SHAME'
  | 'CONFUSION'
  | 'SLEEP_DIFFICULTIES'
  | 'PROCRASTINATION'
  | 'LACK_OF_MOTIVATION'
  | 'POSITIVE_EMOTIONS';

export type EmotionIntensity = 'MILD' | 'MODERATE' | 'HIGH' | 'CRITICAL';

export interface ClassificationResult {
  primaryCategory: EmotionCategory | null;
  secondaryCategories: EmotionCategory[];
  intensity: EmotionIntensity;
  confidence: number;
  isNegated: boolean;
  isMetaphorical: boolean;
  isThirdPersonOrObject: boolean;
  requiresClarification: boolean;
  matchedKeywords: string[];
  rawInput: string;
}

// 1. Metaphorical Idiom Patterns (Disambiguated before safety)
const METAPHOR_PATTERNS: Array<{ regex: RegExp; category: EmotionCategory }> = [
  { regex: /\b(killed\s+(that|the|my|this)\s+(exam|test|interview|presentation|audition|workout|set|game))\b/i, category: 'POSITIVE_EMOTIONS' },
  { regex: /\b(killing\s+it|crushing\s+it|slaying\s+it|nailed\s+it)\b/i, category: 'POSITIVE_EMOTIONS' },
  { regex: /\b(dying\s+(to\s+see|to\s+know|to\s+hear|to\s+meet|to\s+go|to\s+watch|to\s+read|to\s+try|of\s+laughter|laughing))\b/i, category: 'POSITIVE_EMOTIONS' },
  { regex: /\b(to\s+die\s+for)\b/i, category: 'POSITIVE_EMOTIONS' },
  { regex: /\b(dead\s+tired|dead\s+exhausted|half\s+dead\s+from\s+work)\b/i, category: 'EMOTIONAL_EXHAUSTION' },
  { regex: /\b(this\s+(exam|homework|assignment|project|job|work|shift|code|bug)\s+is\s+killing\s+me)\b/i, category: 'ACADEMIC_STRESS' },
  { regex: /\b(my\s+(feet|back|head|neck|shoulder|body)\s+(is|are)\s+killing\s+me)\b/i, category: 'EMOTIONAL_EXHAUSTION' }
];

// 2. Third-person & External Object References
const EXTERNAL_OBJECT_PATTERN = /\b(that|this|the)\s+(movie|film|book|novel|show|episode|series|story|documentary|weather|song|album|news|article)\s+(is|was|feels|seemed)\s+(so\s+)?(depressing|sad|heartbreaking|awful|dark)\b/i;
const THIRD_PERSON_SUBJECT_PATTERN = /\b(he|she|they|my\s+friend|my\s+brother|my\s+sister|my\s+mom|my\s+dad|my\s+cousin|my\s+coworker|my\s+boss|someone|people)\s+(is|was|feels|felt|were|are)\s+(so\s+)?(sad|depressed|angry|crying|anxious|hurting)\b/i;

// 3. Negation Patterns
const NEGATED_DEATH_IDEATION = /\b(i\s+)?(don'?t|do\s+not|never)\s+(want\s+to|wanna|plan\s+to)\s+(die|end\s+my\s+life|kill\s+myself|hurt\s+myself)\b/i;

// 4. Intensity Modifiers
const HIGH_INTENSITY_PATTERN = /\b(unbearable|extremely|completely|utterly|severely|intensely|cannot\s+take\s+it|can'?t\s+take\s+this|desperately|overwhelmingly|falling\s+apart|paralyzed|shaking|heart\s+pounding|can'?t\s+breathe)\b/i;
const MILD_INTENSITY_PATTERN = /\b(a\s+little|a\s+bit|slightly|somewhat|mildly|a\s+tiny\s+bit|kind\s+of|sort\s+of)\b/i;

// 5. Category Regex Dictionaries
const REGEX_MAP: Record<EmotionCategory, RegExp> = {
  IMMEDIATE_PHYSICAL_DANGER: /\b(someone\s+is\s+(hurting|attacking|following)\s+me|in\s+immediate\s+danger|imminent\s+danger|someone\s+has\s+a\s+weapon|abusing\s+me|threatened\s+my\s+life|call\s+the\s+police)\b/i,
  SUICIDAL_IDEATION: /\b(want\s+to\s+die|wanna\s+die|kill\s+myself|killing\s+myself|end\s+my\s+life|ending\s+my\s+life|take\s+my\s+own\s+life|commit\s+suicide|suicidal|wish\s+i\s+(was|were)\s+dead|rather\s+be\s+dead|better\s+off\s+dead|don'?t\s+want\s+to\s+wake\s+up|never\s+wake\s+up|ready\s+to\s+die|no\s+reason\s+to\s+live|end\s+it\s+all|ending\s+it\s+all|su1cide|kms)\b/i,
  SELF_HARM: /\b(cut\s+myself|cutting\s+myself|harm\s+myself|harming\s+myself|hurt\s+myself|hurting\s+myself|self[-\s]?harm|burn\s+myself|slit\s+my\s+wrist|overdose|take\s+all\s+my\s+pills)\b/i,

  PANIC: /\b(panic\s+attack|panicking|panicked|having\s+a\s+panic|hyperventilat(ing|ion)|heart\s+racing|heart\s+is\s+pounding|chest\s+tightness|can'?t\s+catch\s+my\s+breath|freaking\s+out|terrified)\b/i,
  ANXIETY: /\b(anxious|anxiety|nervous|dread|uneasy|on\s+edge|worrying|worried|restless|butterflies\s+in\s+my\s+stomach|fearful|shaking\s+inside|jittery)\b/i,
  OVERTHINKING: /\b(overthinking|overthink|mind\s+won'?t\s+stop|racing\s+thoughts|thoughts\s+racing|can'?t\s+stop\s+thinking|analyzing\s+everything|replaying\s+(everything|it|thoughts)|spiraling|head\s+is\s+spinning|mental\s+loop)\b/i,
  SADNESS: /\b(feel(ing)?\s+sad|so\s+sad|sadness|crying|cried|in\s+tears|weeping|heavy\s+heart|feeling\s+blue|gloomy|unhappy|downhearted|sorrowful|feel(ing)?\s+(really\s+|so\s+)?(low|down)|bad\s+day|rough\s+day|terrible\s+day|awful\s+day|horrible\s+day|today\s+sucked|worst\s+day)\b/i,
  LONELINESS: /\b(lonely|loneliness|alone|all\s+alone|feeling\s+isolated|isolated|isolation|nobody\s+cares|no\s+one\s+cares|have\s+no\s+friends|no\s+one\s+to\s+talk\s+to|left\s+out|abandoned|by\s+myself|unwanted)\b/i,
  DEPRESSION_DISTRESS: /\b(depressed|depression|hopeless|hopelessness|empty\s+inside|feel(ing)?\s+empty|what'?s\s+the\s+point|nothing\s+matters|feel\s+like\s+nothing|dark\s+place|deep\s+dark\s+hole|miserable|despair)\b/i,
  GRIEF: /\b(grief|grieving|mourning|loss\s+of\s+my|passed\s+away|died|loss\s+of\s+a\s+loved\s+one|bereavement|miss\s+(him|her|them)\s+so\s+much)\b/i,
  EMOTIONAL_NUMBNESS: /\b(feel(ing)?\s+numb|emotional(ly)?\s+numb|blank\s+inside|can'?t\s+feel\s+anything|detached|dissociated|dissociating|robotic|feel\s+like\s+a\s+ghost|hollow)\b/i,
  EMOTIONAL_EXHAUSTION: /\b(emotionally\s+exhausted|emotionally\s+drained|no\s+energy\s+left|tired\s+of\s+everything|tired\s+of\s+trying|worn\s+out|spent|completely\s+drained|running\s+on\s+empty)\b/i,
  BURNOUT: /\b(burnout|burning\s+out|burned\s+out|overworked|pushed\s+to\s+the\s+limit|reached\s+my\s+limit|can'?t\s+keep\s+doing\s+this|exhaustion\s+from\s+work|work\s+overload)\b/i,

  ACADEMIC_STRESS: /\b(fail(ing)?\s+(my\s+)?(exam|test|class|semester|course|school|college)s?|exam\s+stress|finals\s+week|grades\s+are\s+slipping|studying\s+all\s+night|syllabus|assignment\s+due|homework|gpa|flunking|(overwhelmed|stressed|anxious|worried)\s+(with|by|about)\s+(my\s+)?(exams?|finals|tests?)|(exam|finals)\s+pressure)\b/i,
  WORK_RELATED_STRESS: /\b(boss\s+is|toxic\s+workplace|work\s+stress|job\s+stress|fired|getting\s+fired|layoff|laid\s+off|deadline\s+is\s+tomorrow|workload|working\s+12\s+hours|career\s+pressure)\b/i,
  RELATIONSHIP_PROBLEMS: /\b(fight\s+with\s+my\s+(partner|boyfriend|girlfriend|husband|wife)|relationship\s+problems|drifting\s+apart|we\s+keep\s+fighting|partner\s+doesn'?t\s+understand|cheating|trust\s+issues|toxic\s+relationship)\b/i,
  HEARTBREAK: /\b((going\s+through\s+a\s+|gng\s+thru\s+(a\s+)?)?break\s*up|broke\s+up|broken\s+up|breaking\s+up|heartbroken|heartbreak|dumped(\s+me)?|ended\s+(things|our\s+relationship|it)|they\s+left\s+me|she\s+left\s+me|he\s+left\s+me|shattered\s+my\s+heart|still\s+love\s+my\s+ex|miss\s+(my\s+ex|her|him|them)|still\s+miss(\s+(her|him|them|my\s+ex))?|want\s+(her|him|them)\s+back|split\s+up|separated|parted\s+ways|called\s+it\s+off|cut\s+ties)\b/i,
  REJECTION: /\b(rejected|rejection|got\s+rejected|they\s+said\s+no|nobody\s+wants\s+me|turned\s+down|didn'?t\s+get\s+picked|ghosted|they\s+ghosted\s+me)\b/i,
  FAMILY_CONFLICT:
    /\b(parents\s+(are\s+fighting|yelling|screaming)|family\s+drama|fight\s+with\s+my\s+(parents|mom|dad|mother|father|family)|strict\s+parents|family\s+pressure|unsupportive\s+family|family\s+dismiss(es|ed|ing)?|(parents|family)\s+dismiss(es|ed|ing)?(\s+my\s+feelings)?)\b/i,
  SOCIAL_ANXIETY: /\b(social\s+anxiety|talking\s+to\s+people|scared\s+of\s+crowds|judged\s+by\s+others|people\s+staring\s+at\s+me|awkward\s+in\s+public|embarrassed\s+myself|fear\s+of\s+speaking|introvert\s+panic)\b/i,

  ANGER: /\b(angry|rage|furious|mad|pissed\s+off|livid|boiling\s+with\s+anger|want\s+to\s+scream|lost\s+my\s+temper|screaming\s+inside|so\s+mad)\b/i,
  FRUSTRATION: /\b(frustrated|frustration|irritated|annoyed|aggravated|sick\s+and\s+tired|nothing\s+works|fed\s+up|at\s+my\s+wit'?s\s+end)\b/i,
  LOW_SELF_ESTEEM: /\b(worthless|hate\s+myself|disgusted\s+with\s+myself|feel\s+like\s+a\s+failure|i'?m\s+a\s+loser|i'?m\s+so\s+stupid|never\s+good\s+enough|useless|pathetic|deserve\s+bad\s+things)\b/i,
  INSECURITY: /\b(insecure|insecurity|comparing\s+myself|not\s+attractive|ugly|feel\s+inadequate|imposter\s+syndrome|everyone\s+is\s+better\s+than\s+me|doubt\s+myself)\b/i,
  FEAR: /\b(scared|terrified|fearful|fear\s+of\s+the\s+future|dreading|horrified|afraid\s+of\s+what'?s\s+next|scared\s+of\s+failing|paralyzed\s+with\s+fear)\b/i,
  GUILT: /\b(guilty|guilt|feel\s+so\s+guilty|my\s+fault|all\s+my\s+fault|blame\s+myself|i\s+ruined\s+everything|i\s+messed\s+everything\s+up|should\s+have\s+done\s+better)\b/i,
  SHAME: /\b(ashamed|shame|humiliated|so\s+embarrassed|can'?t\s+look\s+at\s+myself|deep\s+shame|mortified|hide\s+my\s+face)\b/i,
  CONFUSION: /\b(confused|don'?t\s+know\s+what\s+to\s+do|lost|don'?t\s+understand\s+myself|mixed\s+feelings|torn\s+between|head\s+is\s+clouded|indecisive|can'?t\s+make\s+up\s+my\s+mind)\b/i,

  SLEEP_DIFFICULTIES: /\b(can'?t\s+sleep|cannot\s+sleep|insomnia|sleepless|waking\s+up\s+at\s+night|tossing\s+and\s+turning|haven'?t\s+slept|nightmares|stayed\s+awake\s+all\s+night|sleep\s+deprived)\b/i,
  PROCRASTINATION: /\b(procrastinat(ing|ion)|putting\s+it\s+off|avoiding\s+my\s+work|delaying\s+everything|wasting\s+time|can'?t\s+get\s+myself\s+to\s+start|doomscrolling)\b/i,
  LACK_OF_MOTIVATION: /\b(no\s+motivation|lack\s+of\s+motivation|unmotivated|can'?t\s+bring\s+myself\s+to|don'?t\s+care\s+anymore|no\s+drive|apathy|apathetic|lost\s+my\s+spark)\b/i,

  POSITIVE_EMOTIONS: /\b(happy|joyful|peaceful|grateful|relieved|doing\s+well|feeling\s+good|proud\s+of\s+myself|hopeful|excited|wonderful|content|blessed|optimistic|celebrating)\b/i
};

export class EmotionClassifier {
  static classify(message: string): ClassificationResult {
    const trimmed = message.trim();
    if (!trimmed) {
      return {
        primaryCategory: null,
        secondaryCategories: [],
        intensity: 'MODERATE',
        confidence: 0,
        isNegated: false,
        isMetaphorical: false,
        isThirdPersonOrObject: false,
        requiresClarification: false,
        matchedKeywords: [],
        rawInput: message
      };
    }

    // Step 1: Metaphors & Idioms
    for (const item of METAPHOR_PATTERNS) {
      if (item.regex.test(trimmed)) {
        return {
          primaryCategory: item.category,
          secondaryCategories: [],
          intensity: 'MODERATE',
          confidence: 0.95,
          isNegated: false,
          isMetaphorical: true,
          isThirdPersonOrObject: false,
          requiresClarification: false,
          matchedKeywords: [item.regex.source],
          rawInput: message
        };
      }
    }

    // Step 2: Third person / media object
    if (EXTERNAL_OBJECT_PATTERN.test(trimmed)) {
      return {
        primaryCategory: 'CONFUSION',
        secondaryCategories: [],
        intensity: 'MILD',
        confidence: 0.85,
        isNegated: false,
        isMetaphorical: false,
        isThirdPersonOrObject: true,
        requiresClarification: false,
        matchedKeywords: ['external_object_ref'],
        rawInput: message
      };
    }

    // Step 3: Negated Death Ideation
    if (NEGATED_DEATH_IDEATION.test(trimmed)) {
      return {
        primaryCategory: 'DEPRESSION_DISTRESS',
        secondaryCategories: [],
        intensity: 'MODERATE',
        confidence: 0.88,
        isNegated: true,
        isMetaphorical: false,
        isThirdPersonOrObject: false,
        requiresClarification: false,
        matchedKeywords: ['negated_death_statement'],
        rawInput: message
      };
    }

    // Step 4: Intensity
    const intensity: EmotionIntensity = HIGH_INTENSITY_PATTERN.test(trimmed)
      ? 'HIGH'
      : MILD_INTENSITY_PATTERN.test(trimmed)
      ? 'MILD'
      : 'MODERATE';

    // Step 5: Critical Safety Override
    if (REGEX_MAP.IMMEDIATE_PHYSICAL_DANGER.test(trimmed)) {
      return {
        primaryCategory: 'IMMEDIATE_PHYSICAL_DANGER',
        secondaryCategories: [],
        intensity: 'CRITICAL',
        confidence: 0.99,
        isNegated: false,
        isMetaphorical: false,
        isThirdPersonOrObject: false,
        requiresClarification: false,
        matchedKeywords: ['danger'],
        rawInput: message
      };
    }
    if (REGEX_MAP.SUICIDAL_IDEATION.test(trimmed)) {
      return {
        primaryCategory: 'SUICIDAL_IDEATION',
        secondaryCategories: [],
        intensity: 'CRITICAL',
        confidence: 0.99,
        isNegated: false,
        isMetaphorical: false,
        isThirdPersonOrObject: false,
        requiresClarification: false,
        matchedKeywords: ['suicide'],
        rawInput: message
      };
    }
    if (REGEX_MAP.SELF_HARM.test(trimmed)) {
      return {
        primaryCategory: 'SELF_HARM',
        secondaryCategories: [],
        intensity: 'CRITICAL',
        confidence: 0.98,
        isNegated: false,
        isMetaphorical: false,
        isThirdPersonOrObject: false,
        requiresClarification: false,
        matchedKeywords: ['self_harm'],
        rawInput: message
      };
    }

    // Step 6: Multi-category match & weighting
    const matches: Array<{ category: EmotionCategory; weight: number }> = [];

    const weights: Partial<Record<EmotionCategory, number>> = {
      PANIC: 1.2,
      GRIEF: 1.3,
      ACADEMIC_STRESS: 1.2,
      WORK_RELATED_STRESS: 1.2,
      FAMILY_CONFLICT: 1.2,
      SLEEP_DIFFICULTIES: 1.2,
      LOW_SELF_ESTEEM: 1.2
    };

    for (const [catKey, regex] of Object.entries(REGEX_MAP) as Array<[EmotionCategory, RegExp]>) {
      if (regex.test(trimmed)) {
        matches.push({ category: catKey, weight: weights[catKey] || 1.0 });
      }
    }

    if (matches.length === 0) {
      const sitResult = SituationalContextEngine.detectSituation(trimmed);
      if (sitResult.primarySituation) {
        let inferred: EmotionCategory = 'RELATIONSHIP_PROBLEMS';
        switch (sitResult.primarySituation) {
          case 'BREAKUP':
          case 'HEARTBREAK':
          case 'BEING_CHEATED_ON':
          case 'RELATIONSHIP_ARGUMENTS':
          case 'BEING_IGNORED':
          case 'BEING_REJECTED':
          case 'LONG_DISTANCE':
          case 'FEAR_OF_LOSING_SOMEONE':
          case 'FEELING_UNWANTED':
            inferred = 'RELATIONSHIP_PROBLEMS';
            break;
          case 'MISSING_EX':
            inferred = 'SADNESS';
            break;
          case 'EX_SOCIAL_MEDIA_CHECKING':
            inferred = 'OVERTHINKING';
            break;
          case 'EXAM_FAILURE':
          case 'POOR_GRADES':
          case 'ASSIGNMENT_PRESSURE':
          case 'DISAPPOINTING_PARENTS':
          case 'DIFFICULTY_CONCENTRATING':
          case 'ACADEMIC_COMPARISON':
            inferred = 'ACADEMIC_STRESS';
            break;
          case 'LOSING_FRIENDS':
          case 'FEELING_EXCLUDED':
          case 'SOCIAL_REJECTION':
            inferred = 'LONELINESS';
            break;
          case 'BEING_JUDGED':
          case 'FRIENDSHIP_CONFLICTS':
            inferred = 'SOCIAL_ANXIETY';
            break;
          case 'FAMILY_CONFLICTS':
            inferred = 'FAMILY_CONFLICT';
            break;
          case 'FINANCIAL_PRESSURE':
          case 'CAREER_UNCERTAINTY':
            inferred = 'ANXIETY';
            break;
          case 'LOSS_OF_LOVED_ONE':
            inferred = 'GRIEF';
            break;
          case 'PERSONAL_DISAPPOINTMENT':
          case 'PEER_ACHIEVEMENT_ENVY':
          case 'SOCIAL_MEDIA_ENVY':
            inferred = 'LOW_SELF_ESTEEM';
            break;
          // Batch 2: Scenario 6 (Misunderstood and Unheard)
          case 'PERSPECTIVE_DISMISSED':
          case 'LABELED_OVERLY_SENSITIVE':
          case 'COMMUNICATION_BREAKDOWN':
            inferred = 'FRUSTRATION';
            break;
          // Batch 2: Scenario 7 (Regret and Guilt)
          case 'PAST_DECISION_REGRET':
          case 'HURT_SOMEONE':
          case 'SELF_BLAME':
            inferred = 'GUILT';
            break;
          // Batch 2: Scenario 8 (Sleep Difficulties & Night Rumination)
          case 'CANNOT_SLEEP_INSOMNIA':
          case 'NIGHT_RUMINATION':
          case 'LATE_NIGHT_LONELINESS':
            inferred = 'SLEEP_DIFFICULTIES';
            break;
          // Batch 2: Scenario 9 (Social Anxiety & Fear of Judgement)
          case 'MEETING_NEW_PEOPLE':
          case 'POST_EVENT_RUMINATION':
          case 'FEELING_AWKWARD':
          case 'PUBLIC_SPEAKING_FEAR':
            inferred = 'SOCIAL_ANXIETY';
            break;
          // Batch 2: Scenario 10 (Procrastination & Productivity Distress)
          case 'PHONE_DOOMSCROLLING':
          case 'TASK_OVERWHELM':
          case 'AVOIDING_TASKS':
            inferred = 'PROCRASTINATION';
            break;
          // Batch 3: Scenario 11 (Emotional Exhaustion & Burnout)
          case 'MENTAL_DRAIN':
          case 'ANHEDONIA_LOSS_OF_INTEREST':
          case 'RESPONSIBILITY_OVERWHELM':
          case 'WITHDRAWAL_URGE':
            inferred = 'BURNOUT';
            break;
          // Batch 3: Scenario 12 (Financial Stress & Dependence)
          case 'EXPENSE_MANAGEMENT':
          case 'PARENTAL_FINANCIAL_DEPENDENCE':
          case 'PEER_WEALTH_COMPARISON':
            inferred = 'ANXIETY';
            break;
          // Batch 3: Scenario 13 (Life Transitions & Unfamiliar Environments)
          case 'COLLEGE_START':
          case 'RELOCATING_NEW_CITY':
          case 'ROUTINE_LOSS':
          case 'NOSTALGIA_PAST':
            inferred = 'ANXIETY';
            break;
          // Batch 3: Scenario 14 (Interpersonal Conflict & Boundary Setting)
          case 'HEATED_ARGUMENT':
          case 'DISRESPECT_FELT':
            inferred = 'ANGER';
            break;
          // Batch 3: Scenario 15 (Rejection & Unreciprocated Feelings)
          case 'ROMANTIC_REJECTION':
          case 'UNRECIPROCATED_FEELINGS':
          case 'FEELING_UNWANTED_AFTER_NO':
            inferred = 'REJECTION';
            break;
          // Batch 4: Scenario 16 (Grief and Loss)
          case 'PET_LOSS':
          case 'ANNIVERSARY_GRIEF':
          case 'ABSENCE_LONGING':
            inferred = 'GRIEF';
            break;
          // Batch 4: Scenario 17 (Body Image & Self-Confidence)
          case 'APPEARANCE_INSECURITY':
          case 'WEIGHT_DISSATISFACTION':
          case 'PHOTO_ANXIETY':
            inferred = 'INSECURITY';
            break;
          // Batch 4: Scenario 18 (Positive Emotions & Celebration)
          case 'ACADEMIC_ACHIEVEMENT':
          case 'PERSONAL_GOAL_REACHED':
          case 'GENERAL_GRATITUDE':
            inferred = 'POSITIVE_EMOTIONS';
            break;
          // Batch 4: Scenario 19 (Everyday Frustration & Casual Venting)
          case 'BAD_DAY':
          case 'IRRITATED_SMALL_THINGS':
          case 'CASUAL_VENTING':
            inferred = 'FRUSTRATION';
            break;
          // Batch 4: Scenario 20 (Romantic Relationship Support)
          case 'RELATIONSHIP_INSECURITY':
          case 'PARTNER_DISTANCING':
          case 'FEAR_OF_ABANDONMENT':
          case 'JEALOUSY':
            inferred = 'RELATIONSHIP_PROBLEMS';
            break;
        }
        return {
          primaryCategory: inferred,
          secondaryCategories: [],
          intensity,
          confidence: 0.93,
          isNegated: false,
          isMetaphorical: false,
          isThirdPersonOrObject: false,
          requiresClarification: false,
          matchedKeywords: [sitResult.primarySituation],
          rawInput: message
        };
      }

      const isThirdPerson = THIRD_PERSON_SUBJECT_PATTERN.test(trimmed);
      return {
        primaryCategory: null,
        secondaryCategories: [],
        intensity,
        confidence: 0.3,
        isNegated: false,
        isMetaphorical: false,
        isThirdPersonOrObject: isThirdPerson,
        requiresClarification: true,
        matchedKeywords: [],
        rawInput: message
      };
    }

    matches.sort((a, b) => b.weight - a.weight);

    return {
      primaryCategory: matches[0].category,
      secondaryCategories: matches.slice(1).map((m) => m.category),
      intensity,
      confidence: 0.92,
      isNegated: false,
      isMetaphorical: false,
      isThirdPersonOrObject: false,
      requiresClarification: false,
      matchedKeywords: matches.map((m) => m.category),
      rawInput: message
    };
  }
}
