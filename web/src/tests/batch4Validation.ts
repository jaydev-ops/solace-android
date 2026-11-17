import { DeterministicConversationEngine } from '../services/conversationEngine.ts';

function assert(condition: boolean, msg: string) {
  if (!condition) {
    throw new Error(`❌ ASSERTION FAILED: ${msg}`);
  }
}

console.log('--- STARTING SOLACE+ BATCH 4 VALIDATION (WEB) ---');

// =========================================================================
// SCENARIO 16: GRIEF AND LOSS
// =========================================================================
console.log('1. Testing Scenario 16: Grief and Loss...');
DeterministicConversationEngine.clearSession('b4_s16_flow');

const s16_t1 = DeterministicConversationEngine.processTurn(
  'I miss my grandfather.',
  'User',
  undefined,
  undefined,
  'b4_s16_flow'
);
assert(
  s16_t1.text.toLowerCase().includes('space') ||
    s16_t1.text.toLowerCase().includes('grief') ||
    s16_t1.text.toLowerCase().includes('missing') ||
    s16_t1.text.toLowerCase().includes('sorrow') ||
    s16_t1.text.toLowerCase().includes('holding'),
  'S16 Turn 1 must hold gentle compassionate space for grief'
);
assert(!s16_t1.text.toLowerCase().includes('would you like'), 'Must not ask permission');

const s16_t2 = DeterministicConversationEngine.processTurn(
  'I keep remembering things we used to do.',
  'User',
  undefined,
  undefined,
  'b4_s16_flow'
);
assert(
  s16_t2.text.toLowerCase().includes('memories') ||
    s16_t2.text.toLowerCase().includes('routines') ||
    s16_t2.text.toLowerCase().includes('tears') ||
    s16_t2.text.toLowerCase().includes('longing'),
  'S16 Turn 2 must validate cherished memories and tears'
);

const s16_t3 = DeterministicConversationEngine.processTurn(
  'I wish I could talk to him one more time.',
  'User',
  undefined,
  undefined,
  'b4_s16_flow'
);
assert(
  s16_t3.text.toLowerCase().includes('universal') ||
    s16_t3.text.toLowerCase().includes('love') ||
    s16_t3.text.toLowerCase().includes('memory') ||
    s16_t3.text.toLowerCase().includes('conversation'),
  'S16 Turn 3 must validate wishing for one last talk'
);

// S16 Single turn checks
const s16_single1 = DeterministicConversationEngine.processTurn('My pet dog passed away yesterday.');
assert(
  s16_single1.text.toLowerCase().includes('pet') ||
    s16_single1.text.toLowerCase().includes('companion') ||
    s16_single1.text.toLowerCase().includes('grief') ||
    s16_single1.text.toLowerCase().includes('honor'),
  'S16 Single 1 must honor profound pet loss'
);

const s16_single2 = DeterministicConversationEngine.processTurn('Today is the anniversary of their death.');
assert(
  s16_single2.text.toLowerCase().includes('anniversar') ||
    s16_single2.text.toLowerCase().includes('waves') ||
    s16_single2.text.toLowerCase().includes('sorrow') ||
    s16_single2.text.toLowerCase().includes('gentle'),
  'S16 Single 2 must validate anniversary grief waves'
);

// =========================================================================
// SCENARIO 17: BODY IMAGE & SELF-CONFIDENCE
// =========================================================================
console.log('2. Testing Scenario 17: Body Image & Self-Confidence...');
DeterministicConversationEngine.clearSession('b4_s17_flow');

const s17_t1 = DeterministicConversationEngine.processTurn(
  'I hate how I look.',
  'User',
  undefined,
  undefined,
  'b4_s17_flow'
);
assert(
  s17_t1.text.toLowerCase().includes('mirror') ||
    s17_t1.text.toLowerCase().includes('instrument') ||
    s17_t1.text.toLowerCase().includes('body') ||
    s17_t1.text.toLowerCase().includes('filtered'),
  'S17 Turn 1 must emphasize body neutrality over superficial praise'
);

const s17_t2 = DeterministicConversationEngine.processTurn(
  'Everyone looks better than me.',
  'User',
  undefined,
  undefined,
  'b4_s17_flow'
);
assert(
  s17_t2.text.toLowerCase().includes('commercial fiction') ||
    s17_t2.text.toLowerCase().includes('comparing') ||
    s17_t2.text.toLowerCase().includes('screens') ||
    s17_t2.text.toLowerCase().includes('digital'),
  'S17 Turn 2 must deconstruct curated screen perfection'
);

const s17_t3 = DeterministicConversationEngine.processTurn(
  "I don't feel confident going outside.",
  'User',
  undefined,
  undefined,
  'b4_s17_flow'
);
assert(
  s17_t3.text.toLowerCase().includes('public') ||
    s17_t3.text.toLowerCase().includes('space') ||
    s17_t3.text.toLowerCase().includes('insecurities') ||
    s17_t3.text.toLowerCase().includes('outside'),
  'S17 Turn 3 must reassure right to take up physical space'
);

// S17 Single turn checks
const s17_single1 = DeterministicConversationEngine.processTurn('I am disgusted with my body and hate my weight');
assert(
  s17_single1.text.toLowerCase().includes('heartbeat') ||
    s17_single1.text.toLowerCase().includes('kindness') ||
    s17_single1.text.toLowerCase().includes('chest') ||
    s17_single1.text.toLowerCase().includes('body'),
  'S17 Single 1 must offer gentle somatic kindness'
);

const s17_single2 = DeterministicConversationEngine.processTurn('I am uncomfortable in photos and dread pictures');
assert(
  s17_single2.text.toLowerCase().includes('photograph') ||
    s17_single2.text.toLowerCase().includes('snapshot') ||
    s17_single2.text.toLowerCase().includes('angle') ||
    s17_single2.text.toLowerCase().includes('warmth'),
  'S17 Single 2 must decouple 2D snapshots from human presence'
);

// =========================================================================
// SCENARIO 18: POSITIVE EMOTIONS & CELEBRATION
// =========================================================================
console.log('3. Testing Scenario 18: Positive Emotions & Celebration...');
DeterministicConversationEngine.clearSession('b4_s18_flow');

const s18_t1 = DeterministicConversationEngine.processTurn(
  'I finally passed my exam!',
  'User',
  undefined,
  undefined,
  'b4_s18_flow'
);
assert(
  s18_t1.text.toLowerCase().includes('congratulations') ||
    s18_t1.text.toLowerCase().includes('incredible') ||
    s18_t1.text.toLowerCase().includes('relief') ||
    s18_t1.text.toLowerCase().includes('lightness'),
  'S18 Turn 1 must celebrate exam milestone with warmth'
);

const s18_t2 = DeterministicConversationEngine.processTurn(
  'I studied so hard and it paid off.',
  'User',
  undefined,
  undefined,
  'b4_s18_flow'
);
assert(
  s18_t2.text.toLowerCase().includes('discipline') ||
    s18_t2.text.toLowerCase().includes('persistence') ||
    s18_t2.text.toLowerCase().includes('victory') ||
    s18_t2.text.toLowerCase().includes('savor'),
  'S18 Turn 2 must validate hard work and discipline'
);

const s18_t3 = DeterministicConversationEngine.processTurn(
  "I'm actually proud of myself.",
  'User',
  undefined,
  undefined,
  'b4_s18_flow'
);
assert(
  s18_t3.text.toLowerCase().includes('proud') ||
    s18_t3.text.toLowerCase().includes('resilience') ||
    s18_t3.text.toLowerCase().includes('success'),
  'S18 Turn 3 must amplify healthy self-pride'
);

// S18 Single turn checks
const s18_single1 = DeterministicConversationEngine.processTurn('I got the job after months of trying!');
assert(
  s18_single1.text.toLowerCase().includes('milestone') ||
    s18_single1.text.toLowerCase().includes('win') ||
    s18_single1.text.toLowerCase().includes('celebrate') ||
    s18_single1.text.toLowerCase().includes('good news') ||
    s18_single1.text.toLowerCase().includes('shared') ||
    s18_single1.text.toLowerCase().includes('accomplish'),
  'S18 Single 1 must celebrate job offer victory'
);

const s18_single2 = DeterministicConversationEngine.processTurn('I had a wonderful day and want to celebrate with me');
assert(
  s18_single2.text.toLowerCase().includes('sanctuary') ||
    s18_single2.text.toLowerCase().includes('celebrat') ||
    s18_single2.text.toLowerCase().includes('anchor') ||
    s18_single2.text.toLowerCase().includes('memory'),
  'S18 Single 2 must celebrate in the sanctuary'
);

// =========================================================================
// SCENARIO 19: CASUAL FRUSTRATION & VENTING
// =========================================================================
console.log('4. Testing Scenario 19: Casual Frustration & Venting...');
DeterministicConversationEngine.clearSession('b4_s19_flow');

const s19_t1 = DeterministicConversationEngine.processTurn(
  'Today has been absolutely terrible.',
  'User',
  undefined,
  undefined,
  'b4_s19_flow'
);
assert(
  s19_t1.text.toLowerCase().includes('grating') ||
    s19_t1.text.toLowerCase().includes('terrible') ||
    s19_t1.text.toLowerCase().includes('vent') ||
    s19_t1.text.toLowerCase().includes('tipping point'),
  'S19 Turn 1 must provide non-judgmental space for a bad day'
);

const s19_t2 = DeterministicConversationEngine.processTurn(
  'Everything is just irritating me.',
  'User',
  undefined,
  undefined,
  'b4_s19_flow'
);
assert(
  s19_t2.text.toLowerCase().includes('sensory') ||
    s19_t2.text.toLowerCase().includes('irritating') ||
    s19_t2.text.toLowerCase().includes('threshold') ||
    s19_t2.text.toLowerCase().includes('fresh air'),
  'S19 Turn 2 must normalize sensory threshold overload'
);

const s19_t3 = DeterministicConversationEngine.processTurn(
  "I don't even know why I'm annoyed.",
  'User',
  undefined,
  undefined,
  'b4_s19_flow'
);
assert(
  s19_t3.text.toLowerCase().includes('biology') ||
    s19_t3.text.toLowerCase().includes('foul mood') ||
    s19_t3.text.toLowerCase().includes('grumpy') ||
    s19_t3.text.toLowerCase().includes('guilt'),
  'S19 Turn 3 must permit being grumpy without guilt'
);

// S19 Single turn checks
const s19_single1 = DeterministicConversationEngine.processTurn('I am stuck in a rut and grumpy today');
assert(
  s19_single1.text.toLowerCase().includes('rant') ||
    s19_single1.text.toLowerCase().includes('chest') ||
    s19_single1.text.toLowerCase().includes('small') ||
    s19_single1.text.toLowerCase().includes('grumpy') ||
    s19_single1.text.toLowerCase().includes('annoying') ||
    s19_single1.text.toLowerCase().includes('happened'),
  'S19 Single 1 must invite venting without minimization'
);

const s19_single2 = DeterministicConversationEngine.processTurn('It was an awful day and everything went wrong');
assert(
  s19_single2.text.toLowerCase().includes('wash') ||
    s19_single2.text.toLowerCase().includes('tomorrow') ||
    s19_single2.text.toLowerCase().includes('low-key') ||
    s19_single2.text.toLowerCase().includes('grating'),
  'S19 Single 2 must offer permission to write off bad days'
);

// =========================================================================
// SCENARIO 20: ROMANTIC RELATIONSHIP SUPPORT
// =========================================================================
console.log('5. Testing Scenario 20: Romantic Relationship Support...');
DeterministicConversationEngine.clearSession('b4_s20_flow');

const s20_t1 = DeterministicConversationEngine.processTurn(
  'I feel like my partner is growing distant.',
  'User',
  undefined,
  undefined,
  'b4_s20_flow'
);
assert(
  s20_t1.text.toLowerCase().includes('partner') ||
    s20_t1.text.toLowerCase().includes('affection') ||
    s20_t1.text.toLowerCase().includes('anxiety') ||
    s20_t1.text.toLowerCase().includes('communication'),
  'S20 Turn 1 must address partner cooling with gentle inquiry'
);

const s20_t2 = DeterministicConversationEngine.processTurn(
  'They barely give me attention anymore.',
  'User',
  undefined,
  undefined,
  'b4_s20_flow'
);
assert(
  s20_t2.text.toLowerCase().includes('needy') ||
    s20_t2.text.toLowerCase().includes('attention') ||
    s20_t2.text.toLowerCase().includes('connection') ||
    s20_t2.text.toLowerCase().includes('demanding'),
  'S20 Turn 2 must validate need for emotional connection as non-needy'
);

const s20_t3 = DeterministicConversationEngine.processTurn(
  "I don't want to seem needy.",
  'User',
  undefined,
  undefined,
  'b4_s20_flow'
);
assert(
  s20_t3.text.toLowerCase().includes('express') ||
    s20_t3.text.toLowerCase().includes('needs') ||
    s20_t3.text.toLowerCase().includes('disconnected') ||
    s20_t3.text.toLowerCase().includes('guilt'),
  'S20 Turn 3 must guide towards constructive direct communication'
);

// S20 Single turn checks
const s20_single1 = DeterministicConversationEngine.processTurn('I get jealous when they spend time with other people');
assert(
  s20_single1.text.toLowerCase().includes('jealous') ||
    s20_single1.text.toLowerCase().includes('abandonment') ||
    s20_single1.text.toLowerCase().includes('phone') ||
    s20_single1.text.toLowerCase().includes('controlling') ||
    s20_single1.text.toLowerCase().includes('trust') ||
    s20_single1.text.toLowerCase().includes('surveillance'),
  'S20 Single 1 must acknowledge jealousy while disavowing controlling behavior'
);

const s20_single2 = DeterministicConversationEngine.processTurn('Trust issues in my relationship make me terrified of losing my partner');
assert(
  s20_single2.text.toLowerCase().includes('trust') ||
    s20_single2.text.toLowerCase().includes('surveillance') ||
    s20_single2.text.toLowerCase().includes('respect') ||
    s20_single2.text.toLowerCase().includes('shoulders') ||
    s20_single2.text.toLowerCase().includes('quiet') ||
    s20_single2.text.toLowerCase().includes('neglect') ||
    s20_single2.text.toLowerCase().includes('openly'),
  'S20 Single 2 must address relationship insecurity'
);

console.log('✅ ALL BATCH 4 (SCENARIOS 16-20) TESTS PASSED SUCCESSFULLY (25/25)!');
