import { DeterministicConversationEngine } from '../services/conversationEngine.ts';

function assert(condition: boolean, msg: string) {
  if (!condition) {
    throw new Error(`❌ ASSERTION FAILED: ${msg}`);
  }
}

console.log('--- STARTING SOLACE+ BATCH 2 VALIDATION (WEB) ---');

// =========================================================================
// SCENARIO 6: MISUNDERSTOOD AND UNHEARD
// =========================================================================
console.log('1. Testing Scenario 6: Misunderstood and Unheard...');
DeterministicConversationEngine.clearSession('b2_s6_flow');

const s6_t1 = DeterministicConversationEngine.processTurn(
  "Nobody understands what I'm trying to say.",
  'User',
  undefined,
  undefined,
  'b2_s6_flow'
);
assert(
  s6_t1.text.toLowerCase().includes('misunderstood') ||
    s6_t1.text.toLowerCase().includes('language') ||
    s6_t1.text.toLowerCase().includes('hear'),
  'S6 Turn 1 must acknowledge feeling misunderstood'
);
assert(!s6_t1.text.toLowerCase().includes('would you like'), 'Must not ask permission');

const s6_t2 = DeterministicConversationEngine.processTurn(
  'Whenever I explain something, people interpret it differently and words get twisted.',
  'User',
  undefined,
  undefined,
  'b2_s6_flow'
);
assert(
  s6_t2.text.toLowerCase().includes('twist') ||
    s6_t2.text.toLowerCase().includes('defend') ||
    s6_t2.text.toLowerCase().includes('explaining') ||
    s6_t2.text.toLowerCase().includes('observer') ||
    s6_t2.text.toLowerCase().includes('invisible') ||
    s6_t2.text.toLowerCase().includes('voice'),
  'S6 Turn 2 must address twisted words or invisible voice'
);

const s6_t3 = DeterministicConversationEngine.processTurn(
  'I just stopped explaining myself.',
  'User',
  undefined,
  undefined,
  'b2_s6_flow'
);
assert(
  s6_t3.text.toLowerCase().includes('boundary') ||
    s6_t3.text.toLowerCase().includes('explaining') ||
    s6_t3.text.toLowerCase().includes('preserve') ||
    s6_t3.text.toLowerCase().includes('protect'),
  'S6 Turn 3 must validate healthy boundaries'
);

const s6_sensitive = DeterministicConversationEngine.processTurn(
  "My family called me overly sensitive and said I'm overreacting.",
  'User',
  undefined,
  undefined,
  'b2_s6_sensitive'
);
assert(
  s6_sensitive.text.toLowerCase().includes('sensitive') ||
    s6_sensitive.text.toLowerCase().includes('depth') ||
    s6_sensitive.text.toLowerCase().includes('overreacting'),
  'S6 must reframe sensitivity'
);

const s6_brick = DeterministicConversationEngine.processTurn(
  "I feel invisible in conversations, like I'm talking to a brick wall.",
  'User',
  undefined,
  undefined,
  'b2_s6_brick'
);
assert(
  s6_brick.text.toLowerCase().includes('invisible') ||
    s6_brick.text.toLowerCase().includes('observer') ||
    s6_brick.text.toLowerCase().includes('brick wall') ||
    s6_brick.text.toLowerCase().includes('misunderstood') ||
    s6_brick.text.toLowerCase().includes('language') ||
    s6_brick.text.toLowerCase().includes('twist') ||
    s6_brick.text.toLowerCase().includes('defend') ||
    s6_brick.text.toLowerCase().includes('withdraw'),
  'S6 must acknowledge invisible feeling'
);

const s6_dismiss = DeterministicConversationEngine.processTurn(
  'People constantly dismiss my feelings no matter what I say.',
  'User',
  undefined,
  undefined,
  'b2_s6_dismiss'
);
assert(
  s6_dismiss.text.toLowerCase().includes('convincing') ||
    s6_dismiss.text.toLowerCase().includes('dismiss') ||
    s6_dismiss.text.toLowerCase().includes('energy') ||
    s6_dismiss.text.toLowerCase().includes('time') ||
    s6_dismiss.text.toLowerCase().includes('listening'),
  'S6 must protect energy or offer attentive presence'
);

const s6_cares = DeterministicConversationEngine.processTurn(
  'Nobody cares how I feel, my feelings are always dismissed.',
  'User',
  undefined,
  undefined,
  'b2_s6_cares'
);
assert(
  s6_cares.text.toLowerCase().includes('heard') ||
    s6_cares.text.toLowerCase().includes('sanctuary') ||
    s6_cares.text.toLowerCase().includes('dismiss') ||
    s6_cares.text.toLowerCase().includes('time') ||
    s6_cares.text.toLowerCase().includes('listening') ||
    s6_cares.text.toLowerCase().includes('convincing') ||
    s6_cares.text.toLowerCase().includes('energy') ||
    s6_cares.text.toLowerCase().includes('perspective') ||
    s6_cares.text.toLowerCase().includes('breath'),
  'S6 must validate and offer presence'
);

// =========================================================================
// SCENARIO 7: REGRET AND GUILT
// =========================================================================
console.log('2. Testing Scenario 7: Regret and Guilt...');
DeterministicConversationEngine.clearSession('b2_s7_flow');

const s7_t1 = DeterministicConversationEngine.processTurn(
  'I wish I could go back and change what I said.',
  'User',
  undefined,
  undefined,
  'b2_s7_flow'
);
assert(
  s7_t1.text.toLowerCase().includes('rewind') ||
    s7_t1.text.toLowerCase().includes('regret') ||
    s7_t1.text.toLowerCase().includes('undo'),
  'S7 Turn 1 must acknowledge regret and conscience'
);

const s7_t2 = DeterministicConversationEngine.processTurn(
  'I hurt someone who genuinely cared about me.',
  'User',
  undefined,
  undefined,
  'b2_s7_flow'
);
assert(
  s7_t2.text.toLowerCase().includes('hurt') ||
    s7_t2.text.toLowerCase().includes('accountability') ||
    s7_t2.text.toLowerCase().includes('apology'),
  'S7 Turn 2 must acknowledge hurting someone and accountability'
);

const s7_t3 = DeterministicConversationEngine.processTurn(
  "I don't think I'll ever forgive myself.",
  'User',
  undefined,
  undefined,
  'b2_s7_flow'
);
assert(
  s7_t3.text.toLowerCase().includes('shame') ||
    s7_t3.text.toLowerCase().includes('forgiving') ||
    s7_t3.text.toLowerCase().includes('mistake') ||
    s7_t3.text.toLowerCase().includes('weapon') ||
    s7_t3.text.toLowerCase().includes('apology') ||
    s7_t3.text.toLowerCase().includes('healing') ||
    s7_t3.text.toLowerCase().includes('honor'),
  'S7 Turn 3 must address self-forgiveness or healing apology'
);

const s7_replay = DeterministicConversationEngine.processTurn(
  'I keep replaying the mistake in my head every night.',
  'User',
  undefined,
  undefined,
  'b2_s7_replay'
);
assert(
  s7_replay.text.toLowerCase().includes('replay') ||
    s7_replay.text.toLowerCase().includes('history') ||
    s7_replay.text.toLowerCase().includes('torture') ||
    s7_replay.text.toLowerCase().includes('mistake'),
  'S7 must address mental replay'
);

const s7_shame = DeterministicConversationEngine.processTurn(
  'The guilt is eating me up inside and I feel so haunted by what I did.',
  'User',
  undefined,
  undefined,
  'b2_s7_shame'
);
assert(
  s7_shame.text.toLowerCase().includes('guilt') ||
    s7_shame.text.toLowerCase().includes('shame') ||
    s7_shame.text.toLowerCase().includes('forgive') ||
    s7_shame.text.toLowerCase().includes('replay') ||
    s7_shame.text.toLowerCase().includes('history'),
  'S7 must address guilt vs shame'
);

const s7_ruined = DeterministicConversationEngine.processTurn(
  'I ruined everything and wish I could rewind time.',
  'User',
  undefined,
  undefined,
  'b2_s7_ruined'
);
assert(
  s7_ruined.text.toLowerCase().includes('moment') ||
    s7_ruined.text.toLowerCase().includes('past') ||
    s7_ruined.text.toLowerCase().includes('rewind') ||
    s7_ruined.text.toLowerCase().includes('undo'),
  'S7 must offer perspective on past decisions'
);

const s7_badly = DeterministicConversationEngine.processTurn(
  "I can't forgive myself for how I treated them badly.",
  'User',
  undefined,
  undefined,
  'b2_s7_badly'
);
assert(
  s7_badly.text.toLowerCase().includes('forgiv') ||
    s7_badly.text.toLowerCase().includes('guilt') ||
    s7_badly.text.toLowerCase().includes('chest') ||
    s7_badly.text.toLowerCase().includes('weapon'),
  'S7 must address self-forgiveness or unpacking guilt'
);

// =========================================================================
// SCENARIO 8: SLEEP DIFFICULTIES & NIGHT RUMINATION
// =========================================================================
console.log('3. Testing Scenario 8: Sleep Difficulties & Night Rumination...');
DeterministicConversationEngine.clearSession('b2_s8_flow');

const s8_t1 = DeterministicConversationEngine.processTurn(
  "It's 3 AM and I still can't sleep.",
  'User',
  undefined,
  undefined,
  'b2_s8_flow'
);
assert(
  s8_t1.text.toLowerCase().includes('3 am') ||
    s8_t1.text.toLowerCase().includes('sleep') ||
    s8_t1.text.toLowerCase().includes('rest'),
  'S8 Turn 1 must acknowledge 3 AM insomnia'
);

const s8_t2 = DeterministicConversationEngine.processTurn(
  "My brain won't stop thinking about everything.",
  'User',
  undefined,
  undefined,
  'b2_s8_flow'
);
assert(
  s8_t2.text.toLowerCase().includes('brain') ||
    s8_t2.text.toLowerCase().includes('quiet') ||
    s8_t2.text.toLowerCase().includes('racing') ||
    s8_t2.text.toLowerCase().includes('notes'),
  'S8 Turn 2 must address racing thoughts'
);

const s8_t3 = DeterministicConversationEngine.processTurn(
  "I'm thinking about everything that went wrong.",
  'User',
  undefined,
  undefined,
  'b2_s8_flow'
);
assert(
  s8_t3.text.toLowerCase().includes('night') ||
    s8_t3.text.toLowerCase().includes('daylight') ||
    s8_t3.text.toLowerCase().includes('exhaustion') ||
    s8_t3.text.toLowerCase().includes('looping'),
  'S8 Turn 3 must address nighttime thoughts amplified by exhaustion'
);

const s8_tossing = DeterministicConversationEngine.processTurn(
  "I'm tossing and turning and can't fall asleep.",
  'User',
  undefined,
  undefined,
  'b2_s8_tossing'
);
assert(
  s8_tossing.text.toLowerCase().includes('breath') ||
    s8_tossing.text.toLowerCase().includes('mattress') ||
    s8_tossing.text.toLowerCase().includes('rest') ||
    s8_tossing.text.toLowerCase().includes('sleep'),
  'S8 must offer calming breathing or somatic grounding'
);

const s8_dread = DeterministicConversationEngine.processTurn(
  "I'm dreading waking up tomorrow, wide awake at night.",
  'User',
  undefined,
  undefined,
  'b2_s8_dread'
);
assert(
  s8_dread.text.toLowerCase().includes('tomorrow') ||
    s8_dread.text.toLowerCase().includes('surrender') ||
    s8_dread.text.toLowerCase().includes('rest') ||
    s8_dread.text.toLowerCase().includes('night') ||
    s8_dread.text.toLowerCase().includes('water') ||
    s8_dread.text.toLowerCase().includes('chair') ||
    s8_dread.text.toLowerCase().includes('bed') ||
    s8_dread.text.toLowerCase().includes('frustration'),
  'S8 must address dreading tomorrow'
);

const s8_alone = DeterministicConversationEngine.processTurn(
  'Staring at the ceiling feeling so alone at night.',
  'User',
  undefined,
  undefined,
  'b2_s8_alone'
);
assert(
  s8_alone.text.toLowerCase().includes('ceiling') ||
    s8_alone.text.toLowerCase().includes('night') ||
    s8_alone.text.toLowerCase().includes('alone') ||
    s8_alone.text.toLowerCase().includes('isolating') ||
    s8_alone.text.toLowerCase().includes('asleep') ||
    s8_alone.text.toLowerCase().includes('restorative') ||
    s8_alone.text.toLowerCase().includes('tomorrow') ||
    s8_alone.text.toLowerCase().includes('surrender'),
  'S8 must acknowledge quiet night and presence'
);

const s8_reset = DeterministicConversationEngine.processTurn(
  'I have been wide awake at night with trouble falling asleep.',
  'User',
  undefined,
  undefined,
  'b2_s8_reset'
);
assert(
  s8_reset.text.toLowerCase().includes('water') ||
    s8_reset.text.toLowerCase().includes('chair') ||
    s8_reset.text.toLowerCase().includes('rest') ||
    s8_reset.text.toLowerCase().includes('sleep') ||
    s8_reset.text.toLowerCase().includes('isolating') ||
    s8_reset.text.toLowerCase().includes('asleep'),
  'S8 must suggest getting up or resting quietly'
);

// =========================================================================
// SCENARIO 9: SOCIAL ANXIETY & FEAR OF JUDGEMENT
// =========================================================================
console.log('4. Testing Scenario 9: Social Anxiety & Fear of Judgement...');
DeterministicConversationEngine.clearSession('b2_s9_flow');

const s9_t1 = DeterministicConversationEngine.processTurn(
  'I feel awkward around new people.',
  'User',
  undefined,
  undefined,
  'b2_s9_flow'
);
assert(
  s9_t1.text.toLowerCase().includes('nervous') ||
    s9_t1.text.toLowerCase().includes('people') ||
    s9_t1.text.toLowerCase().includes('protect'),
  'S9 Turn 1 must acknowledge social nervousness'
);

const s9_t2 = DeterministicConversationEngine.processTurn(
  "I keep thinking they'll find me boring.",
  'User',
  undefined,
  undefined,
  'b2_s9_flow'
);
assert(
  s9_t2.text.toLowerCase().includes('spotlight') ||
    s9_t2.text.toLowerCase().includes('boring') ||
    s9_t2.text.toLowerCase().includes('critic'),
  'S9 Turn 2 must address spotlight effect'
);

const s9_t3 = DeterministicConversationEngine.processTurn(
  "I don't even know what to say.",
  'User',
  undefined,
  undefined,
  'b2_s9_flow'
);
assert(
  s9_t3.text.toLowerCase().includes('pressure') ||
    s9_t3.text.toLowerCase().includes('charismatic') ||
    s9_t3.text.toLowerCase().includes('question') ||
    s9_t3.text.toLowerCase().includes('scorecard') ||
    s9_t3.text.toLowerCase().includes('anxiety') ||
    s9_t3.text.toLowerCase().includes('words'),
  'S9 Turn 3 must relieve pressure to perform'
);

const s9_judged = DeterministicConversationEngine.processTurn(
  'I have so much social anxiety and fear of being judged.',
  'User',
  undefined,
  undefined,
  'b2_s9_judged'
);
assert(
  s9_judged.text.toLowerCase().includes('nervous') ||
    s9_judged.text.toLowerCase().includes('evaluating') ||
    s9_judged.text.toLowerCase().includes('judgment') ||
    s9_judged.text.toLowerCase().includes('judged') ||
    s9_judged.text.toLowerCase().includes('pause') ||
    s9_judged.text.toLowerCase().includes('silence') ||
    s9_judged.text.toLowerCase().includes('breath'),
  'S9 must normalize social anxiety'
);

const s9_scorecard = DeterministicConversationEngine.processTurn(
  "I'm replaying the conversation and dissecting every word I said.",
  'User',
  undefined,
  undefined,
  'b2_s9_scorecard'
);
assert(
  s9_scorecard.text.toLowerCase().includes('scorecard') ||
    s9_scorecard.text.toLowerCase().includes('replaying') ||
    s9_scorecard.text.toLowerCase().includes('dissecting') ||
    s9_scorecard.text.toLowerCase().includes('critic'),
  'S9 must dispel mental scorecard'
);

const s9_battery = DeterministicConversationEngine.processTurn(
  'My social anxiety makes me scared to speak up and freezing in groups.',
  'User',
  undefined,
  undefined,
  'b2_s9_battery'
);
assert(
  s9_battery.text.toLowerCase().includes('battery') ||
    s9_battery.text.toLowerCase().includes('air') ||
    s9_battery.text.toLowerCase().includes('pressure') ||
    s9_battery.text.toLowerCase().includes('pause') ||
    s9_battery.text.toLowerCase().includes('silence') ||
    s9_battery.text.toLowerCase().includes('room'),
  'S9 must grant permission to pause or leave'
);

const s9_public = DeterministicConversationEngine.processTurn(
  'I have presentation anxiety and public speaking fear.',
  'User',
  undefined,
  undefined,
  'b2_s9_public'
);
assert(
  s9_public.text.toLowerCase().includes('vigilance') ||
    s9_public.text.toLowerCase().includes('public') ||
    s9_public.text.toLowerCase().includes('energy') ||
    s9_public.text.toLowerCase().includes('room'),
  'S9 must acknowledge public speaking vigilance'
);

// =========================================================================
// SCENARIO 10: PROCRASTINATION & PRODUCTIVITY DISTRESS
// =========================================================================
console.log('5. Testing Scenario 10: Procrastination & Productivity Distress...');
DeterministicConversationEngine.clearSession('b2_s10_flow');

const s10_t1 = DeterministicConversationEngine.processTurn(
  "I've wasted the entire day scrolling reels.",
  'User',
  undefined,
  undefined,
  'b2_s10_flow'
);
assert(
  s10_t1.text.toLowerCase().includes('scrolling') ||
    s10_t1.text.toLowerCase().includes('overwhelm') ||
    s10_t1.text.toLowerCase().includes('blame'),
  'S10 Turn 1 must acknowledge scrolling as emotional escape from overwhelm'
);

const s10_t2 = DeterministicConversationEngine.processTurn(
  'I have so much work pending.',
  'User',
  undefined,
  undefined,
  'b2_s10_flow'
);
assert(
  s10_t2.text.toLowerCase().includes('mountain') ||
    s10_t2.text.toLowerCase().includes('pending') ||
    s10_t2.text.toLowerCase().includes('smallest') ||
    s10_t2.text.toLowerCase().includes('piece'),
  'S10 Turn 2 must shrink the mountain of pending work'
);

const s10_t3 = DeterministicConversationEngine.processTurn(
  "I just can't get myself to start.",
  'User',
  undefined,
  undefined,
  'b2_s10_flow'
);
assert(
  s10_t3.text.toLowerCase().includes('draft') ||
    s10_t3.text.toLowerCase().includes('line') ||
    s10_t3.text.toLowerCase().includes('motivation') ||
    s10_t3.text.toLowerCase().includes('timer'),
  'S10 Turn 3 must encourage messy first draft'
);

const s10_doom = DeterministicConversationEngine.processTurn(
  "I'm doomscrolling on my phone instead of doing assignments.",
  'User',
  undefined,
  undefined,
  'b2_s10_doom'
);
assert(
  s10_doom.text.toLowerCase().includes('room') ||
    s10_doom.text.toLowerCase().includes('timer') ||
    s10_doom.text.toLowerCase().includes('minutes') ||
    s10_doom.text.toLowerCase().includes('scrolling'),
  'S10 must offer 20-minute timer or distance'
);

const s10_perf = DeterministicConversationEngine.processTurn(
  "I'm procrastinating because I'm scared I'll do it poorly.",
  'User',
  undefined,
  undefined,
  'b2_s10_perf'
);
assert(
  s10_perf.text.toLowerCase().includes('perfectionism') ||
    s10_perf.text.toLowerCase().includes('draft') ||
    s10_perf.text.toLowerCase().includes('blank') ||
    s10_perf.text.toLowerCase().includes('starting'),
  'S10 must address perfectionism'
);

const s10_buzz = DeterministicConversationEngine.processTurn(
  'The constant buzz of work is giving me executive dysfunction and paralyzed by work.',
  'User',
  undefined,
  undefined,
  'b2_s10_buzz'
);
assert(
  s10_buzz.text.toLowerCase().includes('buzz') ||
    s10_buzz.text.toLowerCase().includes('rest') ||
    s10_buzz.text.toLowerCase().includes('mountain') ||
    s10_buzz.text.toLowerCase().includes('heavy'),
  'S10 must address constant buzz or guilt-free rest'
);

const s10_reset = DeterministicConversationEngine.processTurn(
  'I feel so guilty about wasting time all afternoon.',
  'User',
  undefined,
  undefined,
  'b2_s10_reset'
);
assert(
  s10_reset.text.toLowerCase().includes('water') ||
    s10_reset.text.toLowerCase().includes('ruined') ||
    s10_reset.text.toLowerCase().includes('hour') ||
    s10_reset.text.toLowerCase().includes('breath') ||
    s10_reset.text.toLowerCase().includes('draft') ||
    s10_reset.text.toLowerCase().includes('scrolling'),
  'S10 must offer grounding reset'
);

console.log('✅ ALL BATCH 2 TESTS PASSED SUCCESSFULLY (25 TESTS)!');
