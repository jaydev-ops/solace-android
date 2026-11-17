import { DeterministicConversationEngine } from '../services/conversationEngine.ts';

function assert(condition: boolean, msg: string) {
  if (!condition) {
    throw new Error(`❌ ASSERTION FAILED: ${msg}`);
  }
}

console.log('--- STARTING SOLACE+ BATCH 1 VALIDATION (WEB) ---');

// =========================================================================
// SCENARIO 1: FRIENDSHIP CONFLICTS & DRIFT
// =========================================================================
console.log('1. Testing Scenario 1: Friendship Conflicts & Drift...');
DeterministicConversationEngine.clearSession('b1_s1_flow');

const s1_t1 = DeterministicConversationEngine.processTurn(
  'I think my best friend is slowly distancing themselves from me.',
  'User',
  undefined,
  undefined,
  'b1_s1_flow'
);
assert(
  s1_t1.text.toLowerCase().includes('distance') || s1_t1.text.toLowerCase().includes('friend'),
  'S1 Turn 1 must acknowledge distance'
);
assert(!s1_t1.text.toLowerCase().includes('would you like'), 'Must not ask permission');

const s1_t2 = DeterministicConversationEngine.processTurn(
  "They don't even text me first anymore.",
  'User',
  undefined,
  undefined,
  'b1_s1_flow'
);
assert(
  s1_t2.text.toLowerCase().includes('effort') ||
    s1_t2.text.toLowerCase().includes('reaching out') ||
    s1_t2.text.toLowerCase().includes('friendship'),
  'S1 Turn 2 must address unequal effort'
);

const s1_t3 = DeterministicConversationEngine.processTurn(
  "I think I'm the only one trying.",
  'User',
  undefined,
  undefined,
  'b1_s1_flow'
);
assert(
  s1_t3.text.toLowerCase().includes('distance') ||
    s1_t3.text.toLowerCase().includes('boundaries') ||
    s1_t3.text.toLowerCase().includes('pause') ||
    s1_t3.text.toLowerCase().includes('reciprocal'),
  'S1 Turn 3 must suggest healthy reflection and space'
);

// Third wheel
const s1_third = DeterministicConversationEngine.processTurn(
  'I feel like the third wheel when friends make plans without me.',
  'User',
  undefined,
  undefined,
  'b1_s1_third'
);
assert(
  s1_third.text.toLowerCase().includes('third wheel') || s1_third.text.toLowerCase().includes('left out'),
  'Must acknowledge third wheel'
);

// Childhood friends
const s1_child = DeterministicConversationEngine.processTurn(
  'I lost touch with my childhood friends.',
  'User',
  undefined,
  undefined,
  'b1_s1_child'
);
assert(
  s1_child.text.toLowerCase().includes('childhood friends') || s1_child.text.toLowerCase().includes('drift'),
  'Must address childhood friends'
);

// =========================================================================
// SCENARIO 2: FAMILY-RELATED CONVERSATIONS
// =========================================================================
console.log('2. Testing Scenario 2: Family-Related Conversations...');
DeterministicConversationEngine.clearSession('b1_s2_flow');

const s2_t1 = DeterministicConversationEngine.processTurn(
  'My parents never understand me.',
  'User',
  undefined,
  undefined,
  'b1_s2_flow'
);
assert(
  s2_t1.text.toLowerCase().includes('parents') || s2_t1.text.toLowerCase().includes('understand'),
  'S2 Turn 1 must acknowledge parents'
);

const s2_t2 = DeterministicConversationEngine.processTurn(
  "They want me to do something I don't even like.",
  'User',
  undefined,
  undefined,
  'b1_s2_flow'
);
assert(
  s2_t2.text.toLowerCase().includes('path') ||
    s2_t2.text.toLowerCase().includes('career') ||
    s2_t2.text.toLowerCase().includes('truth'),
  'S2 Turn 2 must acknowledge career pressure'
);

const s2_t3 = DeterministicConversationEngine.processTurn(
  "I can't even explain my side.",
  'User',
  undefined,
  undefined,
  'b1_s2_flow'
);
assert(
  s2_t3.text.toLowerCase().includes('feelings') ||
    s2_t3.text.toLowerCase().includes('private') ||
    s2_t3.text.toLowerCase().includes('defense') ||
    s2_t3.text.toLowerCase().includes('peace'),
  'S2 Turn 3 must suggest private organization of thoughts'
);

// Sibling comparison
const s2_sib = DeterministicConversationEngine.processTurn(
  'My parents are always comparing me to my brother.',
  'User',
  undefined,
  undefined,
  'b1_s2_sib'
);
assert(
  s2_sib.text.toLowerCase().includes('compared') || s2_sib.text.toLowerCase().includes('siblings'),
  'Must address sibling comparison'
);

// Dismissing feelings
const s2_dism = DeterministicConversationEngine.processTurn(
  'My family dismisses my feelings constantly.',
  'User',
  undefined,
  undefined,
  'b1_s2_dism'
);
assert(
  s2_dism.text.toLowerCase().includes('dismissed') || s2_dism.text.toLowerCase().includes('family'),
  'Must address family dismissing feelings'
);

// =========================================================================
// SCENARIO 3: FAILURE AND DISAPPOINTMENT
// =========================================================================
console.log('3. Testing Scenario 3: Failure and Disappointment...');
DeterministicConversationEngine.clearSession('b1_s3_flow');

const s3_t1 = DeterministicConversationEngine.processTurn(
  'I worked so hard and still failed.',
  'User',
  undefined,
  undefined,
  'b1_s3_flow'
);
assert(
  s3_t1.text.toLowerCase().includes('failure') ||
    s3_t1.text.toLowerCase().includes('outcome') ||
    s3_t1.text.toLowerCase().includes('hard'),
  'S3 Turn 1 must acknowledge failure'
);
assert(!s3_t1.text.toLowerCase().includes('stepping stone'), 'Must not use cliches');

const s3_t2 = DeterministicConversationEngine.processTurn(
  'Everyone else passed.',
  'User',
  undefined,
  undefined,
  'b1_s3_flow'
);
assert(
  s3_t2.text.toLowerCase().includes('passed') ||
    s3_t2.text.toLowerCase().includes('effort') ||
    s3_t2.text.toLowerCase().includes('success'),
  'S3 Turn 2 must address comparison with peers'
);

const s3_t3 = DeterministicConversationEngine.processTurn(
  "Maybe I'm just not capable.",
  'User',
  undefined,
  undefined,
  'b1_s3_flow'
);
assert(
  s3_t3.text.toLowerCase().includes('failure as a human') ||
    s3_t3.text.toLowerCase().includes('decouple') ||
    s3_t3.text.toLowerCase().includes('intelligence'),
  'S3 Turn 3 must distinguish outcome from overall capability'
);

// Opportunity rejection
const s3_opp = DeterministicConversationEngine.processTurn(
  'I was rejected from the program I worked months for.',
  'User',
  undefined,
  undefined,
  'b1_s3_opp'
);
assert(
  s3_opp.text.toLowerCase().includes('effort') || s3_opp.text.toLowerCase().includes('outcome') || s3_opp.text.toLowerCase().includes('failure'),
  'Must address opportunity rejection'
);

// Let everyone down
const s3_down = DeterministicConversationEngine.processTurn(
  'I failed my finals and feel like I let everyone down.',
  'User',
  undefined,
  undefined,
  'b1_s3_down'
);
assert(
  s3_down.text.toLowerCase().includes('let down') || s3_down.text.toLowerCase().includes('believed in you'),
  'Must address letting people down'
);

// =========================================================================
// SCENARIO 4: FUTURE UNCERTAINTY & CAREER STRESS
// =========================================================================
console.log('4. Testing Scenario 4: Future Uncertainty & Career Stress...');
DeterministicConversationEngine.clearSession('b1_s4_flow');

const s4_t1 = DeterministicConversationEngine.processTurn(
  "I have no idea what I'm doing with my life.",
  'User',
  undefined,
  undefined,
  'b1_s4_flow'
);
assert(
  s4_t1.text.toLowerCase().includes('life') &&
    (s4_t1.text.toLowerCase().includes('normal') || s4_t1.text.toLowerCase().includes('master plan')),
  'S4 Turn 1 must normalize uncertainty'
);

const s4_t2 = DeterministicConversationEngine.processTurn(
  'Everyone around me has a plan.',
  'User',
  undefined,
  undefined,
  'b1_s4_flow'
);
assert(
  s4_t2.text.toLowerCase().includes('roadmap') ||
    s4_t2.text.toLowerCase().includes('behind closed doors') ||
    s4_t2.text.toLowerCase().includes('exploratory'),
  'S4 Turn 2 must address everyone having a plan'
);

const s4_t3 = DeterministicConversationEngine.processTurn(
  "I'm scared I'll make the wrong decision.",
  'User',
  undefined,
  undefined,
  'b1_s4_flow'
);
assert(
  s4_t3.text.toLowerCase().includes('irreversible') ||
    s4_t3.text.toLowerCase().includes('pivots') ||
    s4_t3.text.toLowerCase().includes('steps'),
  'S4 Turn 3 must address fear of irreversible decisions'
);

// Wrong degree
const s4_deg = DeterministicConversationEngine.processTurn(
  "I'm terrified I picked the wrong degree.",
  'User',
  undefined,
  undefined,
  'b1_s4_deg'
);
assert(
  s4_deg.text.toLowerCase().includes('degree') || s4_deg.text.toLowerCase().includes('major'),
  'Must address degree anxiety'
);

// Directionless
const s4_dir = DeterministicConversationEngine.processTurn(
  'I feel so directionless and lost right now.',
  'User',
  undefined,
  undefined,
  'b1_s4_dir'
);
assert(
  s4_dir.text.toLowerCase().includes('directionless') || s4_dir.text.toLowerCase().includes('evolution'),
  'Must address feeling directionless'
);

// =========================================================================
// SCENARIO 5: SOCIAL COMPARISON & INFERIORITY
// =========================================================================
console.log('5. Testing Scenario 5: Social Comparison & Inferiority...');
DeterministicConversationEngine.clearSession('b1_s5_flow');

const s5_t1 = DeterministicConversationEngine.processTurn(
  'Everyone my age is doing something meaningful except me.',
  'User',
  undefined,
  undefined,
  'b1_s5_flow'
);
assert(
  s5_t1.text.toLowerCase().includes('left behind') || s5_t1.text.toLowerCase().includes('comparison'),
  'S5 Turn 1 must validate feeling left behind'
);

const s5_t2 = DeterministicConversationEngine.processTurn(
  'I see people travelling, earning and enjoying life.',
  'User',
  undefined,
  undefined,
  'b1_s5_flow'
);
assert(
  s5_t2.text.toLowerCase().includes('social media') ||
    s5_t2.text.toLowerCase().includes('highlight reel') ||
    s5_t2.text.toLowerCase().includes('curated'),
  'S5 Turn 2 must address social media curation'
);

const s5_t3 = DeterministicConversationEngine.processTurn(
  "I feel like I'm wasting my life.",
  'User',
  undefined,
  undefined,
  'b1_s5_flow'
);
assert(
  s5_t3.text.toLowerCase().includes('wasting your life') ||
    s5_t3.text.toLowerCase().includes('pace') ||
    s5_t3.text.toLowerCase().includes('perseverance') ||
    s5_t3.text.toLowerCase().includes('feed'),
  'S5 Turn 3 must anchor in user pace and worth'
);

// Social media envy
const s5_envy = DeterministicConversationEngine.processTurn(
  'Social media makes me feel like a complete failure.',
  'User',
  undefined,
  undefined,
  'b1_s5_envy'
);
assert(
  s5_envy.text.toLowerCase().includes('social media') ||
    s5_envy.text.toLowerCase().includes('highlight reel') ||
    s5_envy.text.toLowerCase().includes('feed') ||
    s5_envy.text.toLowerCase().includes('apps'),
  'Must address social media envy'
);

// Peer earnings
const s5_earn = DeterministicConversationEngine.processTurn(
  'I see peers earning and buying things and feel so inferior.',
  'User',
  undefined,
  undefined,
  'b1_s5_earn'
);
assert(
  s5_earn.text.toLowerCase().includes('earning') || s5_earn.text.toLowerCase().includes('peers'),
  'Must address peer earnings comparison'
);

console.log('✅ ALL BATCH 1 TESTS PASSED SUCCESSFULLY (25 TESTS)!');
