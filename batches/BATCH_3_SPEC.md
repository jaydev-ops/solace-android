# Solace+ — Implementation Batch 3 Specification
## Scenarios 11 to 15: Stressors, Transitions & Rejection

> **Batch Number**: 3 of 4  
> **Status**: ✅ **Implemented and Verified** (Android Kotlin + Web TypeScript + 25 Unit Tests)  
> **Parent Spec**: [`CONVERSATIONAL_ENGINE_SPEC.md`](file:///Users/jayeshyadav/Projects/Solace--main/CONVERSATIONAL_ENGINE_SPEC.md)

---

## 1. Batch Overview & Scope

Batch 3 addresses physical and nervous-system depletion, acute financial insecurity, turbulent life changes, high-friction interpersonal disagreements, and the emotional sting of romantic or social rejection.

| Scenario ID | Name | Core Situations | Template Range | Response Count |
|---|---|---|---|:---:|
| `SCENARIO_11` | Emotional Exhaustion & Burnout | Mentally drained, anhedonia, responsibility overload, urge to withdraw, chronic fatigue | `SCN11_01`–`08` | 8 |
| `SCENARIO_12` | Financial Stress & Dependence | Guilt asking parents for money, debt anxiety, peer wealth comparison, financial worth | `SCN12_01`–`08` | 8 |
| `SCENARIO_13` | Life Transitions & Unfamiliarity | Starting college, moving cities, new job, routine loss, unfamiliar environment | `SCN13_01`–`08` | 8 |
| `SCENARIO_14` | Interpersonal Conflict & Boundaries | Huge arguments, saying things regretted, two-sided blame, boundary setting | `SCN14_01`–`08` | 8 |
| `SCENARIO_15` | Rejection & Unreciprocated Feelings | Confessed and rejected, feeling embarrassed, feeling unlovable, ghosted | `SCN15_01`–`08` | 8 |

**Total New Templates in Batch 3**: **40 distinct proactive templates**.

---

## 2. Detailed Scenario Specifications

### 2.1 Scenario 11: Emotional Exhaustion & Burnout
- **Identifier**: `SCENARIO_11_BURNOUT_EXHAUSTION`
- **Recognized Triggers**:
  - `\b(tired\s+of\s+everything|mentally\s+drained|running\s+on\s+empty|things\s+i\s+enjoy\s+feel\s+like\s+work|just\s+want\s+to\s+be\s+left\s+alone|no\s+energy\s+left|emotionally\s+unavailable|tired\s+despite\s+sleeping)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[MENTAL_DRAIN, ANHEDONIA_LOSS_OF_INTEREST, RESPONSIBILITY_OVERWHELM, WITHDRAWAL_URGE]`
  - Primary: `BURNOUT` or `EMOTIONAL_EXHAUSTION`; Secondary: `DEPRESSION_DISTRESS`, `EMOTIONAL_NUMBNESS`, `SADNESS`.
- **Response Templates (`SCN11_01` – `SCN11_08`)**:
  1. `SCN11_01` (`CALM` | `STAGE_1`): *"Hearing that you are 'tired of everything' tells me how heavily you've been carrying life's demands without a genuine break. When your emotional battery reaches zero, even small requests feel unbearable. Take a moment right now and let yourself put down the weight. You don't have to carry tomorrow tonight."*
  2. `SCN11_02` (`REFLECT` | `STAGE_2`): *"When activities and hobbies that used to bring you joy suddenly feel like exhausting chores, it is a clear sign your nervous system is deeply depleted. Don't force yourself to feel enthusiastic. Give yourself permission to do nothing for a little while."*
  3. `SCN11_03` (`LISTEN` | `STAGE_3`): *"Wanting to withdraw and be left completely alone is a natural instinct to protect what little energy you have left. I respect your need for quiet and space. I'm right here with you—no questions, no demands."*
  4. `SCN11_04` (`UNDERSTAND` | `STAGE_4`): *"Burnout doesn't heal with a single good night of sleep; it requires reducing the demands you place on yourself. Look at your schedule for tomorrow: what is one obligation that can be postponed, simplified, or dropped entirely?"*
  5. `SCN11_05` (`CALM` | `STAGE_5`): *"Unclench your jaw, lower your shoulders from your ears, and breathe out slowly. In this moment, you are safe, nothing is due, and nobody is evaluating you. Settle into this pause."*
  6. `SCN11_06` (`REFLECT` | `STAGE_3`): *"You have spent so long pushing through fatigue and being strong for others that your body is finally demanding the rest it was denied. Honor this fatigue instead of fighting it."*
  7. `SCN11_07` (`LISTEN` | `STAGE_1`): *"What part of your life has been pulling on your energy the most recently?"*
  8. `SCN11_08` (`CALM` | `STAGE_2`): *"Take things in ten-minute increments today. Lower your expectations of productivity to zero. Surviving this hour is enough."*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I'm tired of everything."* $\to$ Matches `SCN11_01`.
  - Turn 2: *"Even things I enjoy feel like work."* $\to$ Matches `SCN11_02`.
  - Turn 3: *"I just want to be left alone."* $\to$ Matches `SCN11_03`.

---

### 2.2 Scenario 12: Financial Stress & Dependence
- **Identifier**: `SCENARIO_12_FINANCIAL_STRESS`
- **Recognized Triggers**:
  - `\b(feel\s+bad\s+asking\s+my\s+parents\s+for\s+money|financial(ly)?\s+(dependent|stress|pressure|burden)|everyone\s+around\s+me\s+earns|can'?t\s+afford\s+anything|not\s+contributing\s+anything|guilty\s+about\s+spending|debt\s+worries)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[EXPENSE_MANAGEMENT, PARENTAL_FINANCIAL_DEPENDENCE, PEER_WEALTH_COMPARISON]`
  - Primary: `ANXIETY` or `BURNOUT`; Secondary: `GUILT`, `SHAME`, `FEAR`.
- **Response Templates (`SCN12_01` – `SCN12_08`)**:
  1. `SCN12_01` (`UNDERSTAND` | `STAGE_1`): *"Feeling guilty about asking your parents or family for financial support is an uncomfortable, suffocating ache. But when you are in school, transitioning between careers, or dealing with rising living costs, financial dependence is a temporary season, not a character flaw. What has been making money feel especially stressful lately?"*
  2. `SCN12_02` (`REFLECT` | `STAGE_2`): *"Looking at classmates or peers earning their own incomes and comparing your bank account to theirs creates an instant sense of inadequacy. But people start from vastly different privileges, generational supports, and timing. You cannot measure your capability by someone else's balance."*
  3. `SCN12_03` (`CALM` | `STAGE_3`): *"Your worth as a human being is not determined by how much money you bring in or how early you become financially self-sufficient. You contribute warmth, effort, presence, and thought to the people around you. Breathe into this truth."*
  4. `SCN12_04` (`UNDERSTAND` | `STAGE_4`): *"When financial anxiety spikes, the brain imagines worst-case ruin. Let's look at today: you have a place to sit, you have air in your lungs, and we can look at small, practical budgeting steps when your head is clear."*
  5. `SCN12_05` (`LISTEN` | `STAGE_1`): *"Living under constant financial strain forces your nervous system into chronic survival mode. It makes every small expense feel like a threat. I hear how exhausting that is."*
  6. `SCN12_06` (`REFLECT` | `STAGE_3`): *"Feeling like a burden to your family is a burden you place on yourself. Families support each other through different life seasons. Someday you will be in a position to give back, but right now it's okay to accept help."*
  7. `SCN12_07` (`CALM` | `STAGE_5`): *"Step away from the budget spreadsheets for tonight. Constant hyper-vigilance won't change your finances this evening; it will only rob you of sleep. Rest your mind."*
  8. `SCN12_08` (`LISTEN` | `STAGE_2`): *"What is the specific expense or worry that's keeping you up?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I feel bad asking my parents for money."* $\to$ Matches `SCN12_01`.
  - Turn 2: *"Everyone around me earns their own money."* $\to$ Matches `SCN12_02`.
  - Turn 3: *"I feel like I'm not contributing anything."* $\to$ Matches `SCN12_03`.

---

### 2.3 Scenario 13: Life Transitions & Unfamiliar Environments
- **Identifier**: `SCENARIO_13_LIFE_TRANSITIONS`
- **Recognized Triggers**:
  - `\b(started\s+(college|university|a\s+new\s+job)|don'?t\s+feel\s+like\s+i\s+belong\s+here|miss\s+my\s+school\s+friends|moved\s+to\s+a\s+new\s+city|everything\s+feels\s+unfamiliar|lost\s+my\s+routine|miss\s+my\s+old\s+life|homesick)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[COLLEGE_START, RELOCATING_NEW_CITY, ROUTINE_LOSS, NOSTALGIA_PAST]`
  - Primary: `ANXIETY` or `LONELINESS`; Secondary: `SADNESS`, `FEAR`, `OVERTHINKING`.
- **Response Templates (`SCN13_01` – `SCN13_08`)**:
  1. `SCN13_01` (`UNDERSTAND` | `STAGE_1`): *"Stepping into a new environment—whether it's starting college, moving cities, or taking a new job—shakes the foundation of your daily security. Feeling like you don't belong here is the most common early reaction to change. Give yourself time; belonging is built slowly over weeks, not granted on day one."*
  2. `SCN13_02` (`REFLECT` | `STAGE_2`): *"Missing your school friends and the comfort of your old life is completely natural. You aren't just missing people; you are missing the effortless familiarity of being known. Cherish those memories while giving this new chapter room to unfold."*
  3. `SCN13_03` (`CALM` | `STAGE_3`): *"When everything around you is unfamiliar, your brain works overtime to process new routes, faces, and expectations. It is normal to feel physically exhausted by 4 PM. Make your bedroom or personal corner a predictable, comforting haven tonight."*
  4. `SCN13_04` (`UNDERSTAND` | `STAGE_4`): *"You don't have to build a whole new friend group or master this environment all at once. Focus on establishing one tiny, comforting anchor routine—a favorite coffee spot, a quiet walk, or a nightly phone call home."*
  5. `SCN13_05` (`LISTEN` | `STAGE_1`): *"Homesickness is a physical ache in the center of your chest. It is proof of how deeply you loved the places and people you came from. I'm right here with you."*
  6. `SCN13_06` (`REFLECT` | `STAGE_3`): *"Almost everyone walking around your campus or office feels just as awkward and eager for connection as you do, even if they wear a confident mask. You are not alone in this adjustment."*
  7. `SCN13_07` (`CALM` | `STAGE_5`): *"Take a long, deep breath and let your feet press into the floor. You have survived every previous transition in your life, and you will adapt to this one too. Take it one day at a time."*
  8. `SCN13_08` (`LISTEN` | `STAGE_2`): *"What part of the new environment feels most jarring right now?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I recently started college and I don't feel like I belong here."* $\to$ Matches `SCN13_01`.
  - Turn 2: *"I miss my school friends."* $\to$ Matches `SCN13_02`.
  - Turn 3: *"Everything feels unfamiliar."* $\to$ Matches `SCN13_04`.

---

### 2.4 Scenario 14: Interpersonal Conflict & Boundary Setting
- **Identifier**: `SCENARIO_14_INTERPERSONAL_CONFLICT`
- **Recognized Triggers**:
  - `\b(had\s+a\s+huge\s+argument\s+with|said\s+things\s+i\s+shouldn'?t\s+have|screaming\s+match|they\s+were\s+wrong\s+too|conflict\s+with\s+(my\s+)?(friend|partner|coworker)|boundary|disrespected|both\s+sides\s+made\s+mistakes)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in` `[RELATIONSHIP_ARGUMENTS, HEATED_ARGUMENT, DISRESPECT_FELT]`
  - Primary: `ANGER` or `RELATIONSHIP_PROBLEMS`; Secondary: `GUILT`, `FRUSTRATION`, `ANXIETY`.
- **Response Templates (`SCN14_01` – `SCN14_08`)**:
  1. `SCN14_01` (`CALM` | `STAGE_1`): *"The emotional aftermath of a heated argument is filled with high adrenaline, shaking hands, and racing thoughts. Right now, your nervous system is in fight-or-flight. Don't rush to send another text or force a resolution this minute. Step back, drink some cold water, and let the storm in your body settle."*
  2. `SCN14_02` (`REFLECT` | `STAGE_2`): *"Recognizing that you spoke out of anger and said things you regret takes maturity. Accountability doesn't mean taking 100% of the blame for the entire fight; it simply means owning your own words with honesty and preparing a sincere apology for your part."*
  3. `SCN14_03` (`UNDERSTAND` | `STAGE_3`): *"Most intense interpersonal conflicts happen because both people feel hurt, misunderstood, or unheard. Acknowledging that they were wrong too is valid, but the healthiest conversations happen when you focus on expressing your boundary rather than winning the debate."*
  4. `SCN14_04` (`CALM` | `STAGE_4`): *"A boundary is not a punishment or a wall; it is simply clear communication about what you need to feel safe and respected. When you are ready to talk, use 'I feel' statements instead of pointing fingers."*
  5. `SCN14_05` (`LISTEN` | `STAGE_1`): *"Feeling disrespected in an argument leaves a lingering bitter taste. Your desire for basic respect is non-negotiable. What was the core issue that triggered the blowup?"*
  6. `SCN14_06` (`REFLECT` | `STAGE_3`): *"Never apologize for how you felt, but be willing to apologize for how you expressed it if you crossed lines. That balance preserves both your dignity and the relationship."*
  7. `SCN14_07` (`CALM` | `STAGE_5`): *"Give the interaction 24 hours of breathing room if possible. Time allows defensiveness to cool on both sides. Settle into your sanctuary tonight."*
  8. `SCN14_08` (`LISTEN` | `STAGE_2`): *"Tell me what happened—I am here to help you unpack the conflict without judgment."*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I had a huge argument with my friend."* $\to$ Matches `SCN14_01`.
  - Turn 2: *"I said some things I shouldn't have."* $\to$ Matches `SCN14_02`.
  - Turn 3: *"But they were wrong too."* $\to$ Matches `SCN14_03`.

---

### 2.5 Scenario 15: Rejection & Unreciprocated Feelings
- **Identifier**: `SCENARIO_15_REJECTION`
- **Recognized Triggers**:
  - `\b(told\s+someone\s+i\s+liked\s+them\s+and\s+they\s+rejected\s+me|confessed\s+and\s+got\s+rejected|feel\s+so\s+embarrassed|not\s+good\s+enough\s+for\s+anyone|unreciprocated|they\s+said\s+no|nobody\s+wants\s+me|friendzoned)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[ROMANTIC_REJECTION, UNRECIPROCATED_FEELINGS, FEELING_UNWANTED_AFTER_NO]`
  - Primary: `REJECTION` or `HEARTBREAK`; Secondary: `SHAME`, `SADNESS`, `LOW_SELF_ESTEEM`.
- **Response Templates (`SCN15_01` – `SCN15_08`)**:
  1. `SCN15_01` (`UNDERSTAND` | `STAGE_1`): *"Putting your heart on the line and being met with a 'no' is an intensely painful, vulnerable experience. It takes immense courage to confess how you feel, and facing rejection stings in the center of your chest. I'm really sorry you're hurting right now. How are you holding up?"*
  2. `SCN15_02` (`REFLECT` | `STAGE_2`): *"The embarrassment that follows romantic rejection can feel almost unbearable, making you want to disappear. But remember: having feelings and being brave enough to share them is a sign of human courage, not something to feel ashamed of. You took a risk that many never dare to take."*
  3. `SCN15_03` (`CALM` | `STAGE_3`): *"Rejection is not a grade on your worth, beauty, or lovability. It is simply a lack of mutual compatibility between two specific people at one specific moment in time. Please don't let this outcome become a weapon you turn against yourself."*
  4. `SCN15_04` (`UNDERSTAND` | `STAGE_4`): *"Give yourself physical and digital distance from this person right now. Don't try to immediately force an awkward friendship or analyze their every social post. Your heart needs quiet time to grieve and reset."*
  5. `SCN15_05` (`LISTEN` | `STAGE_1`): *"Being rejected makes your inner critic scream that you aren't 'enough'. But someone not seeing you as a romantic partner does not mean you lack immense value to the world. I'm right here with you."*
  6. `SCN15_06` (`REFLECT` | `STAGE_3`): *"Accepting their decision with grace preserves your self-respect. You did your part by being honest; the rest is about caring for your own healing today."*
  7. `SCN15_07` (`CALM` | `STAGE_5`): *"Take a gentle, deep breath. Cry if you need to, wrap yourself in something warm, and let today just be quiet. You will get through this."*
  8. `SCN15_08` (`LISTEN` | `STAGE_2`): *"What part of the conversation keeps replaying in your mind?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I told someone I liked them and they rejected me."* $\to$ Matches `SCN15_01`.
  - Turn 2: *"I feel so embarrassed."* $\to$ Matches `SCN15_02`.
  - Turn 3: *"Maybe I'm just not good enough."* $\to$ Matches `SCN15_03`.

---

## 3. Batch 3 Verification & Test Requirements

1. **Unit Test Suite**: 25 multi-turn automated tests (5 tests $\times$ 5 scenarios) in Kotlin (`Batch3ScenariosTest.kt`) and TypeScript (`batch3Validation.ts`).
2. **Key Checks**: Non-judgmental holding space for burnout, decoupling financial status from worth, transition adjustment pacing, cooling down interpersonal conflict, and re-framing romantic rejection without shame.
