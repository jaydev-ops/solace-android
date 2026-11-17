import { EmotionClassifier } from '../services/emotionClassifier.ts';
import { RESPONSE_TEMPLATES, CRISIS_RESPONSES } from '../services/responseLibrary.ts';
import { DeterministicConversationEngine } from '../services/conversationEngine.ts';
import { EMERGENCY_RESOURCES, SafetyService } from '../services/safetyService.ts';
import { SafetyQuestionId, RiskLevel } from '../types/solace.ts';

function assert(condition: boolean, msg: string) {
  if (!condition) {
    throw new Error(`❌ ASSERTION FAILED: ${msg}`);
  }
}

console.log('--- STARTING SOLACE+ DETERMINISTIC ENGINE VERIFICATION (WEB) ---');

// 1. CLASSIFICATION & DISAMBIGUATION
console.log('1. Testing classification & disambiguation...');
const anxiety = EmotionClassifier.classify('I feel so anxious and my thoughts are spinning');
assert(anxiety.primaryCategory === 'ANXIETY' || anxiety.primaryCategory === 'OVERTHINKING', 'Anxiety classification');

const panic = EmotionClassifier.classify('I am having a panic attack and heart is racing');
assert(panic.primaryCategory === 'PANIC', 'Panic classification');

const sadness = EmotionClassifier.classify('I feel so sad and cried all morning');
assert(sadness.primaryCategory === 'SADNESS', 'Sadness classification');

const academic = EmotionClassifier.classify('I am failing my exams and semester finals');
assert(academic.primaryCategory === 'ACADEMIC_STRESS', 'Academic stress classification');

// Negation
const negated = EmotionClassifier.classify("I'm exhausted but I don't want to die");
assert(negated.isNegated === true, 'Negation flag must be true');
assert(negated.primaryCategory !== 'SUICIDAL_IDEATION', 'Must not classify as affirmative suicide');

// Metaphor
const metaphor1 = EmotionClassifier.classify('I totally killed that exam today');
assert(metaphor1.isMetaphorical === true, 'Metaphor flag must be true');
assert(metaphor1.primaryCategory === 'POSITIVE_EMOTIONS', 'Killed exam must be positive');

const metaphor2 = EmotionClassifier.classify('I am dying to see the new show');
assert(metaphor2.isMetaphorical === true, 'Dying to see must be metaphorical');
assert(metaphor2.primaryCategory === 'POSITIVE_EMOTIONS', 'Dying to see must be positive');

// 2. RESPONSE LIBRARY VERIFICATION
console.log('2. Testing response library quotas and distinctness...');
assert(RESPONSE_TEMPLATES.length >= 100, `Must have >= 100 templates, got ${RESPONSE_TEMPLATES.length}`);

const categoryMap = new Map<string, number>();
for (const t of RESPONSE_TEMPLATES) {
  categoryMap.set(t.category, (categoryMap.get(t.category) || 0) + 1);
}

assert((categoryMap.get('ANXIETY') || 0) >= 8, 'Anxiety quota >= 8');
assert((categoryMap.get('SADNESS') || 0) >= 8, 'Sadness quota >= 8');
assert((categoryMap.get('LONELINESS') || 0) >= 7, 'Loneliness quota >= 7');
assert((categoryMap.get('OVERTHINKING') || 0) >= 7, 'Overthinking quota >= 7');
assert((categoryMap.get('ACADEMIC_STRESS') || 0) >= 7, 'Academic quota >= 7');
assert((categoryMap.get('RELATIONSHIP_PROBLEMS') || 0) >= 8, 'Relationship quota >= 8');
assert((categoryMap.get('ANGER') || 0) >= 5, 'Anger quota >= 5');
assert((categoryMap.get('LOW_SELF_ESTEEM') || 0) >= 6, 'Low self-esteem quota >= 6');
assert((categoryMap.get('BURNOUT') || 0) >= 6, 'Burnout quota >= 6');
assert((categoryMap.get('FEAR') || 0) >= 5, 'Fear quota >= 5');
assert((categoryMap.get('GUILT') || 0) >= 5, 'Guilt quota >= 5');
assert((categoryMap.get('GRIEF') || 0) >= 4, 'Grief quota >= 4');
assert((categoryMap.get('SLEEP_DIFFICULTIES') || 0) >= 4, 'Sleep quota >= 4');
assert((categoryMap.get('EMOTIONAL_NUMBNESS') || 0) >= 4, 'Numbness quota >= 4');
assert((categoryMap.get('SOCIAL_ANXIETY') || 0) >= 4, 'Social anxiety quota >= 4');
assert((categoryMap.get('FAMILY_CONFLICT') || 0) >= 4, 'Family quota >= 4');
assert((categoryMap.get('LACK_OF_MOTIVATION') || 0) >= 4, 'Motivation quota >= 4');
assert((categoryMap.get('POSITIVE_EMOTIONS') || 0) >= 4, 'Positive quota >= 4');

// Distinctness
const texts = new Set(RESPONSE_TEMPLATES.map((t) => t.text.toLowerCase().trim()));
assert(texts.size === RESPONSE_TEMPLATES.length, 'All response texts must be distinct');

// Forbidden toxic positivity
const forbidden = [
  'everything happens for a reason',
  'just be positive',
  'others have it worse',
  'stop overthinking',
  'everything will be fine',
  'you need to be stronger'
];
for (const t of RESPONSE_TEMPLATES) {
  const lower = t.text.toLowerCase();
  for (const f of forbidden) {
    assert(!lower.includes(f), `Template ${t.id} contains forbidden phrase: ${f}`);
  }
}

// 3. CRISIS SAFETY & PROGRESSION
console.log('3. Testing crisis safety and progression...');
const crisisTurn1 = DeterministicConversationEngine.processTurn('I want to kill myself');
assert(crisisTurn1.safety.riskLevel === RiskLevel.LEVEL_3_SUICIDAL_IDEATION, 'Risk level must be LEVEL_3');
assert(crisisTurn1.safety.requiresInterception === true, 'Must require interception');
assert(crisisTurn1.safety.questionId === SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS, 'Must ask CURRENT_SUICIDAL_THOUGHTS');

// User says YES to thoughts of ending life -> advances to IMMEDIATE_INTENT
const crisisTurn2 = DeterministicConversationEngine.processTurn('yes, I am having those thoughts', 'User', undefined, SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS);
assert(crisisTurn2.safety.questionId === SafetyQuestionId.IMMEDIATE_INTENT, 'Must advance to IMMEDIATE_INTENT');

// User says "i m. good", "good", "i am good", or "just overwhelmed" to thoughts of ending life -> resolves safety
const crisisTurnSafe1 = DeterministicConversationEngine.processTurn('i m. good', 'P', undefined, SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS);
assert(crisisTurnSafe1.safety.questionId === undefined, 'Must clear pending question on "i m. good"');
assert(crisisTurnSafe1.safety.riskLevel === RiskLevel.LEVEL_1_DISTRESS || crisisTurnSafe1.safety.riskLevel === RiskLevel.LEVEL_0_NORMAL, 'Risk level must de-escalate');
assert(crisisTurnSafe1.text.includes('relieved'), 'Must express relief on safe response');

const crisisTurnSafe2 = DeterministicConversationEngine.processTurn('i am good', 'P', undefined, SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS);
assert(crisisTurnSafe2.safety.questionId === undefined, 'Must clear pending question on "i am good"');

const crisisTurnSafe3 = DeterministicConversationEngine.processTurn('just overwhelmed', 'P', undefined, SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS);
assert(crisisTurnSafe3.safety.questionId === undefined, 'Must clear pending question on "just overwhelmed"');

// Verified Helplines
assert(EMERGENCY_RESOURCES.some((r) => r.number === '112'), 'Verified 112 present');
assert(EMERGENCY_RESOURCES.some((r) => r.number === '14416'), 'Verified 14416 present');
assert(EMERGENCY_RESOURCES.some((r) => r.number === '1800-599-0019'), 'Verified KIRAN present');
assert(EMERGENCY_RESOURCES.some((r) => r.number === '988'), 'Verified 988 present');
assert(EMERGENCY_RESOURCES.some((r) => r.number === '741741'), 'Verified 741741 present');

// 4. DEDUPLICATION
console.log('4. Testing sliding window deduplication...');
DeterministicConversationEngine.clearSession();
const used = new Set<string>();
for (let i = 0; i < 6; i++) {
  const turn = DeterministicConversationEngine.processTurn('I feel very anxious today');
  assert(turn.templateId !== undefined, 'Template ID must exist');
  assert(!used.has(turn.templateId!), `Template ${turn.templateId} was repeated consecutively`);
  used.add(turn.templateId!);
}
assert(used.size === 6, 'Must have selected 6 distinct templates');

// 5. NO PERMISSION-BASED QUESTIONS & DIRECT GUIDANCE
console.log('5. Testing removal of permission-based questions & proactive support...');
DeterministicConversationEngine.clearSession();
const permissionPhrases = [
  'would you like me to',
  'should i help you',
  'should i suggest',
  'do you want to try',
  'would you like some tips',
  'can i suggest',
  'would you like to talk about it',
  'would you like to talk it through',
  'would a short grounding exercise help',
  'would a 4-7-8 breathing sequence help'
];
for (const t of RESPONSE_TEMPLATES) {
  const lower = t.text.toLowerCase();
  for (const phrase of permissionPhrases) {
    assert(!lower.includes(phrase), `Template ${t.id} must not ask permission ('${phrase}'): ${t.text}`);
  }
}

// Proactive Guidance verification on sample triggers
const directAnx = DeterministicConversationEngine.processTurn('I feel really anxious');
assert(!directAnx.text.toLowerCase().includes('would you like me to'), 'Must not ask permission for anxiety');
assert(!directAnx.text.toLowerCase().includes('do you want to try'), 'Must not ask if user wants to try');
assert(directAnx.text.toLowerCase().includes('breath'), 'Must provide direct breathing/grounding guidance');

const directOverthink = DeterministicConversationEngine.processTurn("I can't stop overthinking");
assert(!directOverthink.text.toLowerCase().includes('can i suggest'), 'Must not ask permission for overthinking');
assert(directOverthink.text.toLowerCase().includes('thoughts') || directOverthink.text.toLowerCase().includes('focus'), 'Must provide direct cognitive/sensory guidance');

// 6. EXACT 4-TURN BREAKUP CONVERSATION FLOW
console.log('6. Testing exact 4-turn Breakup conversation flow...');
DeterministicConversationEngine.clearSession('breakup_test');

// Turn 1
const turn1 = DeterministicConversationEngine.processTurn(
  'I feel anxious.',
  'User',
  undefined,
  undefined,
  'breakup_test'
);
console.log('Turn 1 Output:', turn1.text);
assert(turn1.category === 'ANXIETY', 'Turn 1 category must be ANXIETY');
assert(turn1.text.toLowerCase().includes('anxiety') || turn1.text.toLowerCase().includes('exhausting'), 'Turn 1 acknowledges anxiety');
assert(
  turn1.text.toLowerCase().includes('weighing on you') || turn1.text.toLowerCase().includes('specific'),
  'Turn 1 asks if something specific is weighing today'
);

// Turn 2
const turn2 = DeterministicConversationEngine.processTurn(
  'Yeah, I am going through a breakup.',
  'User',
  undefined,
  undefined,
  'breakup_test'
);
console.log('Turn 2 Output:', turn2.text);
assert(turn2.templateId === 'SIT_BREAKUP_01', 'Turn 2 must select SIT_BREAKUP_01');
assert(turn2.text.toLowerCase().includes('breakup'), 'Turn 2 must explicitly address the breakup');
assert(
  !turn2.text.includes("I want to make sure I truly understand what you're going through. Could you tell me a little more about what's been happening?"),
  'Turn 2 must NOT return generic clarification fallback'
);

// Turn 3
const turn3 = DeterministicConversationEngine.processTurn(
  'I still miss her.',
  'User',
  undefined,
  undefined,
  'breakup_test'
);
console.log('Turn 3 Output:', turn3.text);
assert(turn3.templateId === 'SIT_MISSING_EX_01', 'Turn 3 must select SIT_MISSING_EX_01');
assert(
  turn3.text.toLowerCase().includes('miss') || turn3.text.toLowerCase().includes('absence'),
  'Turn 3 must acknowledge missing her'
);

// Turn 4
const turn4 = DeterministicConversationEngine.processTurn(
  'I keep checking her Instagram.',
  'User',
  undefined,
  undefined,
  'breakup_test'
);
console.log('Turn 4 Output:', turn4.text);
assert(turn4.templateId === 'SIT_EX_SOCIAL_MEDIA_01', 'Turn 4 must select SIT_EX_SOCIAL_MEDIA_01');
assert(
  turn4.text.toLowerCase().includes('instagram') ||
    turn4.text.toLowerCase().includes('profile') ||
    turn4.text.toLowerCase().includes('social media'),
  'Turn 4 must acknowledge checking social media'
);

// 7. REPEATED BREAKUP DISCLOSURE
console.log('7. Testing repeated breakup disclosure handling...');
DeterministicConversationEngine.clearSession('repeat_breakup_test');
const repTurn1 = DeterministicConversationEngine.processTurn(
  'I am going through a breakup.',
  'User',
  undefined,
  undefined,
  'repeat_breakup_test'
);
const repeatTurn = DeterministicConversationEngine.processTurn(
  'I am going through a breakup.',
  'User',
  undefined,
  undefined,
  'repeat_breakup_test'
);
console.log('Repeat Breakup Output:', repeatTurn.text);
assert(
  repeatTurn.text !== repTurn1.text,
  'Turn 2 must NOT repeat turn 1 identical text'
);
assert(
  !repeatTurn.text.includes("I want to make sure I truly understand"),
  'Turn 2 must NOT be generic clarification'
);
assert(
  repeatTurn.text.includes('I hear you. Going through a breakup is clearly weighing on you') ||
    repeatTurn.text.toLowerCase().includes('breakup'),
  'Must give supportive repeated disclosure message instead of generic fallback'
);

// 8. NEVER EMIT BANNED GENERIC CLARIFICATION STRING
console.log('8. Testing that banned generic clarification string is never emitted...');
const bannedClarification =
  "I want to make sure I truly understand what you're going through. Could you tell me a little more about what's been happening?";
for (const t of RESPONSE_TEMPLATES) {
  assert(!t.text.includes(bannedClarification), `Template ${t.id} must not contain banned string`);
}

// 9. ACADEMIC STRESS -> PARENTS DISAPPOINTMENT
console.log('9. Testing academic stress -> parents disappointment progression...');
DeterministicConversationEngine.clearSession('acad_test');
const acad1 = DeterministicConversationEngine.processTurn(
  'I am overwhelmed with my exams and finals',
  'User',
  undefined,
  undefined,
  'acad_test'
);
assert(acad1.category === 'ACADEMIC_STRESS', 'Acad 1 category must be ACADEMIC_STRESS');
const acad2 = DeterministicConversationEngine.processTurn(
  'I am terrified of disappointing my parents with my grades',
  'User',
  undefined,
  undefined,
  'acad_test'
);
assert(acad2.templateId === 'SIT_ACAD_PARENTS_01', 'Acad 2 must select SIT_ACAD_PARENTS_01');
assert(
  acad2.text.toLowerCase().includes('parents') || acad2.text.toLowerCase().includes('disappointed'),
  'Must address parent disappointment directly'
);

// 10. LONELINESS -> FRIENDSHIP EXCLUSION
console.log('10. Testing loneliness -> friendship exclusion progression...');
DeterministicConversationEngine.clearSession('social_test');
const soc1 = DeterministicConversationEngine.processTurn(
  'I feel completely alone and isolated',
  'User',
  undefined,
  undefined,
  'social_test'
);
assert(soc1.category === 'LONELINESS', 'Soc 1 category must be LONELINESS');
const soc2 = DeterministicConversationEngine.processTurn(
  'My friends made plans without me and left me out',
  'User',
  undefined,
  undefined,
  'social_test'
);
assert(soc2.templateId === 'SIT_SOCIAL_EXCLUDED_01', 'Soc 2 must select SIT_SOCIAL_EXCLUDED_01');
assert(
  soc2.text.toLowerCase().includes('left out') || soc2.text.toLowerCase().includes('excluded'),
  'Must address feeling excluded'
);

// 11. PERSONALIZATION SAFETY (NO "p." BUG)
console.log('11. Testing personalization safety...');
DeterministicConversationEngine.clearSession('name_test');
const turnSingleLetterName = DeterministicConversationEngine.processTurn(
  'I feel anxious',
  'p',
  undefined,
  undefined,
  'name_test'
);
assert(!turnSingleLetterName.text.includes(', p.'), 'Must not format single letter into ", p."');
assert(!turnSingleLetterName.text.includes('p,'), 'Must not format single letter into "p,"');

console.log('✅ ALL VERIFICATIONS PASSED SUCCESSFULLY!');
