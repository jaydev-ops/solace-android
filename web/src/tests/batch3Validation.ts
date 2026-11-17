import { DeterministicConversationEngine } from '../services/conversationEngine.ts';

function assert(condition: boolean, msg: string) {
  if (!condition) {
    throw new Error(`❌ ASSERTION FAILED: ${msg}`);
  }
}

console.log('--- STARTING SOLACE+ BATCH 3 VALIDATION (WEB) ---');

// =========================================================================
// SCENARIO 11: EMOTIONAL EXHAUSTION & BURNOUT
// =========================================================================
console.log('1. Testing Scenario 11: Emotional Exhaustion & Burnout...');
DeterministicConversationEngine.clearSession('b3_s11_flow');

const s11_t1 = DeterministicConversationEngine.processTurn(
  "I'm tired of everything.",
  'User',
  undefined,
  undefined,
  'b3_s11_flow'
);
assert(
  s11_t1.text.toLowerCase().includes('tired') ||
    s11_t1.text.toLowerCase().includes('weight') ||
    s11_t1.text.toLowerCase().includes('battery') ||
    s11_t1.text.toLowerCase().includes('energy') ||
    s11_t1.text.toLowerCase().includes('exhausted'),
  'S11 Turn 1 must address being tired or carrying heavy weight'
);
assert(!s11_t1.text.toLowerCase().includes('would you like'), 'Must not ask permission');

const s11_t2 = DeterministicConversationEngine.processTurn(
  'Even things I enjoy feel like work and chores now.',
  'User',
  undefined,
  undefined,
  'b3_s11_flow'
);
assert(
  s11_t2.text.toLowerCase().includes('joy') ||
    s11_t2.text.toLowerCase().includes('chores') ||
    s11_t2.text.toLowerCase().includes('depleted') ||
    s11_t2.text.toLowerCase().includes('hobbies'),
  'S11 Turn 2 must validate loss of joy / anhedonia'
);

const s11_t3 = DeterministicConversationEngine.processTurn(
  'I just want to be left alone by everyone.',
  'User',
  undefined,
  undefined,
  'b3_s11_flow'
);
assert(
  s11_t3.text.toLowerCase().includes('withdraw') ||
    s11_t3.text.toLowerCase().includes('alone') ||
    s11_t3.text.toLowerCase().includes('quiet') ||
    s11_t3.text.toLowerCase().includes('space'),
  'S11 Turn 3 must honor need to withdraw'
);

// S11 Single turn checks
const s11_single1 = DeterministicConversationEngine.processTurn("I am mentally drained and running on empty");
assert(s11_single1.text.toLowerCase().includes('energy') || s11_single1.text.toLowerCase().includes('weight') || s11_single1.text.toLowerCase().includes('fatigue'), 'S11 Single 1 must acknowledge mental drain');

const s11_single2 = DeterministicConversationEngine.processTurn("I am tired despite sleeping");
assert(s11_single2.text.toLowerCase().includes('burnout') || s11_single2.text.toLowerCase().includes('sleep') || s11_single2.text.toLowerCase().includes('tired'), 'S11 Single 2 must acknowledge chronic fatigue');

// =========================================================================
// SCENARIO 12: FINANCIAL STRESS & DEPENDENCE
// =========================================================================
console.log('2. Testing Scenario 12: Financial Stress & Dependence...');
DeterministicConversationEngine.clearSession('b3_s12_flow');

const s12_t1 = DeterministicConversationEngine.processTurn(
  'I feel bad asking my parents for money.',
  'User',
  undefined,
  undefined,
  'b3_s12_flow'
);
assert(
  s12_t1.text.toLowerCase().includes('parents') ||
    s12_t1.text.toLowerCase().includes('financial') ||
    s12_t1.text.toLowerCase().includes('money') ||
    s12_t1.text.toLowerCase().includes('dependence'),
  'S12 Turn 1 must address guilt asking parents for money'
);

const s12_t2 = DeterministicConversationEngine.processTurn(
  'Everyone around me earns their own money and contributes.',
  'User',
  undefined,
  undefined,
  'b3_s12_flow'
);
assert(
  s12_t2.text.toLowerCase().includes('bank account') ||
    s12_t2.text.toLowerCase().includes('earning') ||
    s12_t2.text.toLowerCase().includes('inadequacy') ||
    s12_t2.text.toLowerCase().includes('peers'),
  'S12 Turn 2 must address peer wealth comparison'
);

const s12_t3 = DeterministicConversationEngine.processTurn(
  "I feel like I'm not contributing anything.",
  'User',
  undefined,
  undefined,
  'b3_s12_flow'
);
assert(
  s12_t3.text.toLowerCase().includes('worth') ||
    s12_t3.text.toLowerCase().includes('human') ||
    s12_t3.text.toLowerCase().includes('contribute') ||
    s12_t3.text.toLowerCase().includes('money'),
  'S12 Turn 3 must decouple self-worth from income'
);

// S12 Single turn checks
const s12_single1 = DeterministicConversationEngine.processTurn("I can't afford anything and have debt worries");
assert(s12_single1.text.toLowerCase().includes('financial') || s12_single1.text.toLowerCase().includes('budget') || s12_single1.text.toLowerCase().includes('strain'), 'S12 Single 1 must address expense strain');

const s12_single2 = DeterministicConversationEngine.processTurn("I am a financial burden on my parents");
assert(
  s12_single2.text.toLowerCase().includes('burden') ||
    s12_single2.text.toLowerCase().includes('family') ||
    s12_single2.text.toLowerCase().includes('parents') ||
    s12_single2.text.toLowerCase().includes('financial') ||
    s12_single2.text.toLowerCase().includes('expense') ||
    s12_single2.text.toLowerCase().includes('strain'),
  'S12 Single 2 must reassure against feeling like a burden or financial strain'
);

// =========================================================================
// SCENARIO 13: LIFE TRANSITIONS & UNFAMILIAR ENVIRONMENTS
// =========================================================================
console.log('3. Testing Scenario 13: Life Transitions & Unfamiliar Environments...');
DeterministicConversationEngine.clearSession('b3_s13_flow');

const s13_t1 = DeterministicConversationEngine.processTurn(
  "I recently started college and I don't feel like I belong here.",
  'User',
  undefined,
  undefined,
  'b3_s13_flow'
);
assert(
  s13_t1.text.toLowerCase().includes('college') ||
    s13_t1.text.toLowerCase().includes('belong') ||
    s13_t1.text.toLowerCase().includes('environment') ||
    s13_t1.text.toLowerCase().includes('transition'),
  'S13 Turn 1 must address college transition & belonging'
);

const s13_t2 = DeterministicConversationEngine.processTurn(
  'I miss my school friends and my old life.',
  'User',
  undefined,
  undefined,
  'b3_s13_flow'
);
assert(
  s13_t2.text.toLowerCase().includes('school friends') ||
    s13_t2.text.toLowerCase().includes('missing') ||
    s13_t2.text.toLowerCase().includes('familiarity') ||
    s13_t2.text.toLowerCase().includes('memories'),
  'S13 Turn 2 must validate nostalgia for old friends'
);

const s13_t3 = DeterministicConversationEngine.processTurn(
  'Everything feels unfamiliar and I lost my routine.',
  'User',
  undefined,
  undefined,
  'b3_s13_flow'
);
assert(
  s13_t3.text.toLowerCase().includes('unfamiliar') ||
    s13_t3.text.toLowerCase().includes('routine') ||
    s13_t3.text.toLowerCase().includes('anchor') ||
    s13_t3.text.toLowerCase().includes('brain'),
  'S13 Turn 3 must normalize routine loss'
);

// S13 Single turn checks
const s13_single1 = DeterministicConversationEngine.processTurn("I moved to a new city and feel homesick");
assert(
  s13_single1.text.toLowerCase().includes('homesick') ||
    s13_single1.text.toLowerCase().includes('city') ||
    s13_single1.text.toLowerCase().includes('transition') ||
    s13_single1.text.toLowerCase().includes('adapt') ||
    s13_single1.text.toLowerCase().includes('environment'),
  'S13 Single 1 must acknowledge homesickness / life transition'
);

const s13_single2 = DeterministicConversationEngine.processTurn("Everything feels unfamiliar in this new campus");
assert(
  s13_single2.text.toLowerCase().includes('unfamiliar') ||
    s13_single2.text.toLowerCase().includes('environment') ||
    s13_single2.text.toLowerCase().includes('campus') ||
    s13_single2.text.toLowerCase().includes('adjustment') ||
    s13_single2.text.toLowerCase().includes('anchor'),
  'S13 Single 2 must acknowledge disorientation or campus adjustment'
);

// =========================================================================
// SCENARIO 14: INTERPERSONAL CONFLICT & BOUNDARY SETTING
// =========================================================================
console.log('4. Testing Scenario 14: Interpersonal Conflict & Boundary Setting...');
DeterministicConversationEngine.clearSession('b3_s14_flow');

const s14_t1 = DeterministicConversationEngine.processTurn(
  'I had a huge argument with my friend.',
  'User',
  undefined,
  undefined,
  'b3_s14_flow'
);
assert(
  s14_t1.text.toLowerCase().includes('argument') ||
    s14_t1.text.toLowerCase().includes('fight') ||
    s14_t1.text.toLowerCase().includes('nervous system') ||
    s14_t1.text.toLowerCase().includes('settle'),
  'S14 Turn 1 must acknowledge post-argument adrenaline'
);

const s14_t2 = DeterministicConversationEngine.processTurn(
  "I said things I shouldn't have out of anger.",
  'User',
  undefined,
  undefined,
  'b3_s14_flow'
);
assert(
  s14_t2.text.toLowerCase().includes('anger') ||
    s14_t2.text.toLowerCase().includes('regret') ||
    s14_t2.text.toLowerCase().includes('accountability') ||
    s14_t2.text.toLowerCase().includes('words'),
  'S14 Turn 2 must address regretted words and accountability'
);

const s14_t3 = DeterministicConversationEngine.processTurn(
  'They were wrong too and disrespected me.',
  'User',
  undefined,
  undefined,
  'b3_s14_flow'
);
assert(
  s14_t3.text.toLowerCase().includes('disrespect') ||
    s14_t3.text.toLowerCase().includes('boundary') ||
    s14_t3.text.toLowerCase().includes('wrong') ||
    s14_t3.text.toLowerCase().includes('conflict'),
  'S14 Turn 3 must validate boundaries and feeling disrespected'
);

// S14 Single turn checks
const s14_single1 = DeterministicConversationEngine.processTurn("We had a screaming match and said things we regret");
assert(s14_single1.text.toLowerCase().includes('argument') || s14_single1.text.toLowerCase().includes('anger') || s14_single1.text.toLowerCase().includes('settle'), 'S14 Single 1 must acknowledge screaming match');

const s14_single2 = DeterministicConversationEngine.processTurn("A boundary was crossed in our relationship");
assert(s14_single2.text.toLowerCase().includes('boundary') || s14_single2.text.toLowerCase().includes('respect') || s14_single2.text.toLowerCase().includes('safe'), 'S14 Single 2 must address boundary crossing');

// =========================================================================
// SCENARIO 15: REJECTION & UNRECIPROCATED FEELINGS
// =========================================================================
console.log('5. Testing Scenario 15: Rejection & Unreciprocated Feelings...');
DeterministicConversationEngine.clearSession('b3_s15_flow');

const s15_t1 = DeterministicConversationEngine.processTurn(
  'I told someone I liked them and they rejected me.',
  'User',
  undefined,
  undefined,
  'b3_s15_flow'
);
assert(
  s15_t1.text.toLowerCase().includes('heart') ||
    s15_t1.text.toLowerCase().includes('courage') ||
    s15_t1.text.toLowerCase().includes('rejection') ||
    s15_t1.text.toLowerCase().includes('rejected'),
  'S15 Turn 1 must acknowledge courage and sting of rejection'
);

const s15_t2 = DeterministicConversationEngine.processTurn(
  'I feel so embarrassed right now.',
  'User',
  undefined,
  undefined,
  'b3_s15_flow'
);
assert(
  s15_t2.text.toLowerCase().includes('embarrass') ||
    s15_t2.text.toLowerCase().includes('ashamed') ||
    s15_t2.text.toLowerCase().includes('courage') ||
    s15_t2.text.toLowerCase().includes('brave'),
  'S15 Turn 2 must normalize post-rejection embarrassment'
);

const s15_t3 = DeterministicConversationEngine.processTurn(
  "Maybe I'm just not good enough for anyone.",
  'User',
  undefined,
  undefined,
  'b3_s15_flow'
);
assert(
  s15_t3.text.toLowerCase().includes('worth') ||
    s15_t3.text.toLowerCase().includes('compatibility') ||
    s15_t3.text.toLowerCase().includes('lovability') ||
    s15_t3.text.toLowerCase().includes('grade'),
  'S15 Turn 3 must decouple rejection from human lovability'
);

// S15 Single turn checks
const s15_single1 = DeterministicConversationEngine.processTurn("I confessed my feelings and they said no");
assert(s15_single1.text.toLowerCase().includes('rejection') || s15_single1.text.toLowerCase().includes('courage') || s15_single1.text.toLowerCase().includes('heart'), 'S15 Single 1 must acknowledge confession rejection');

const s15_single2 = DeterministicConversationEngine.processTurn("I got friendzoned and feel unlovable");
assert(
  s15_single2.text.toLowerCase().includes('worth') ||
    s15_single2.text.toLowerCase().includes('compatibility') ||
    s15_single2.text.toLowerCase().includes('rejection') ||
    s15_single2.text.toLowerCase().includes('rejected') ||
    s15_single2.text.toLowerCase().includes('value') ||
    s15_single2.text.toLowerCase().includes('enough'),
  'S15 Single 2 must address feeling unlovable'
);

console.log('✅ ALL BATCH 3 TESTS PASSED SUCCESSFULLY (25 TESTS)!');
