# Solace+ — Implementation Batch 1 Specification
## Scenarios 1 to 5: Interpersonal & Achievement Foundations

> **Batch Number**: 1 of 4  
> **Status**: Architecture Specification Ready — Awaiting User Approval to Implement  
> **Parent Spec**: [`CONVERSATIONAL_ENGINE_SPEC.md`](file:///Users/jayeshyadav/Projects/Solace--main/CONVERSATIONAL_ENGINE_SPEC.md)

---

## 1. Batch Overview & Scope

Batch 1 establishes the foundational communication scenarios spanning interpersonal relationships, family dynamics, achievement pressure, existential career uncertainty, and comparative social distress.

| Scenario ID | Name | Core Situations | Template Range | Response Count |
|---|---|---|---|:---:|
| `SCENARIO_01` | Friendship Conflicts & Drift | Falling out, feeling replaced, exclusion, childhood friend drift, one-sided effort | `SCN01_01`–`08` | 8 |
| `SCENARIO_02` | Family-Related Emotional Conversations | Career disapproval, sibling comparison, parental pressure, feeling controlled | `SCN02_01`–`08` | 8 |
| `SCENARIO_03` | Failure and Disappointment | Exam failure, opportunity rejection, unachieved goals, mistakes, wasted effort | `SCN03_01`–`08` | 8 |
| `SCENARIO_04` | Future Uncertainty & Career Stress | Career direction, degree doubt, feeling behind in life, decision paralysis | `SCN04_01`–`08` | 8 |
| `SCENARIO_05` | Social Comparison & Inferiority | Peer achievement envy, social media envy, financial comparison, timeline guilt | `SCN05_01`–`08` | 8 |

**Total New Templates in Batch 1**: **40 distinct proactive templates**.

---

## 2. Detailed Scenario Specifications

### 2.1 Scenario 1: Friendship Conflicts & Drift
- **Identifier**: `SCENARIO_01_FRIENDSHIP`
- **Recognized Triggers**:
  - `\b(best\s+friend\s+(distancing|ignoring|leaving|replaced\s+me)|drift(ing)?\s+apart\s+from\s+my\s+friend|friends\s+making\s+plans\s+without\s+me|third\s+wheel|lost\s+touch\s+with\s+(my\s+)?childhood\s+friends|one[-\s]sided\s+friendship|only\s+one\s+(texting|reaching\s+out|trying)|betrayed\s+by\s+(my\s+)?friend)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[LOSING_FRIENDS, FEELING_EXCLUDED, FRIENDSHIP_CONFLICTS]`
  - Prior: `ANXIETY` or `SADNESS` $\to$ Primary: `LONELINESS` or `RELATIONSHIP_PROBLEMS`; Secondary: `SADNESS`, `INSECURITY`.
- **Response Templates (`SCN01_01` – `SCN01_08`)**:
  1. `SCN01_01` (`UNDERSTAND` | `STAGE_1`): *"Noticing distance between yourself and a close friend is a disorienting, quiet ache. Before assuming the worst or concluding they no longer care, let's take a slow breath. Sometimes people get consumed by their own hidden burdens. Has this distance been growing slowly, or did it feel sudden?"*
  2. `SCN01_02` (`REFLECT` | `STAGE_2`): *"When you realize you're always the one reaching out first, it can make the whole friendship feel hollow and exhausting. It is completely reasonable to feel hurt when effort feels unequal. Give yourself permission to pause and step back for a few days to see if space brings clarity."*
  3. `SCN01_03` (`LISTEN` | `STAGE_3`): *"Feeling like the third wheel or finding out friends made plans without you cuts deep. Being left out does not mean you lack value or that you're unwanted. I'm right here with you—what happened that made you feel excluded?"*
  4. `SCN01_04` (`CALM` | `STAGE_4`): *"Friendships naturally shift as life circumstances, routines, and responsibilities change. Grieving the closeness you used to have is normal. You don't have to force an artificial conversation or burn bridges today. Take things one gentle step at a time."*
  5. `SCN01_05` (`UNDERSTAND` | `STAGE_2`): *"Having a close friend replace you or turn their attention elsewhere feels like an unspoken betrayal. Please remember your worth doesn't decrease because someone shifted their priorities. How long have you felt this shift?"*
  6. `SCN01_06` (`REFLECT` | `STAGE_3`): *"Losing touch with childhood friends is one of the quietest griefs of growing older. It's painful when shared history no longer bridges current distance. It's okay to cherish what you had without blaming yourself for where you both are now."*
  7. `SCN01_07` (`CALM` | `STAGE_5`): *"Before making a permanent decision to end a friendship in the heat of hurt, give yourself a day or two of emotional distance. Settle your thoughts, let your nervous system rest, and we can look at what healthy boundaries you need."*
  8. `SCN01_08` (`LISTEN` | `STAGE_1`): *"It takes courage to admit when a friendship feels painful or one-sided. You deserve connections where your energy and presence are met with reciprocal warmth. I'm listening—tell me more about what's been happening."*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I think my best friend is slowly distancing themselves from me."* $\to$ Matches `SCN01_01`.
  - Turn 2: *"They don't even text me first anymore."* $\to$ Matches `SCN01_02`.
  - Turn 3: *"I think I'm the only one trying."* $\to$ Matches `SCN01_07` (Suggests stepping back without burning bridges).

---

### 2.2 Scenario 2: Family-Related Emotional Conversations
- **Identifier**: `SCENARIO_02_FAMILY`
- **Recognized Triggers**:
  - `\b(parents\s+(never\s+understand|don'?t\s+get\s+me|compare\s+me|controlling|dismiss(ing)?\s+my\s+feelings)|pressure\s+from\s+parents|comparison\s+with\s+(my\s+)?(brother|sister|sibling)|family\s+doesn'?t\s+support\s+my\s+career|walking\s+on\s+eggshells\s+at\s+home|can'?t\s+talk\s+to\s+my\s+parents)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[FAMILY_CONFLICTS, DISAPPOINTING_PARENTS, PARENTAL_PRESSURE]`
  - Primary: `FAMILY_CONFLICT`; Secondary: `ANGER`, `GUILT`, `LOW_SELF_ESTEEM`.
- **Response Templates (`SCN02_01` – `SCN02_08`)**:
  1. `SCN02_01` (`UNDERSTAND` | `STAGE_1`): *"Living with the feeling that your parents never truly hear or understand you creates a heavy, everyday friction. It is exhausting when the people whose approval you care about seem to misunderstand your intentions. Take a breath—what happened at home today?"*
  2. `SCN02_02` (`REFLECT` | `STAGE_2`): *"When parents want you to follow a path or career you have no passion for, it feels like having to choose between their happiness and your own life. Wanting to live your own truth doesn't make you ungrateful or a bad child. What path are they pushing on you?"*
  3. `SCN02_03` (`CALM` | `STAGE_3`): *"Being constantly compared to siblings or relatives is profoundly invalidating. It treats love and recognition like a contest you never agreed to enter. Let's ground this moment: their comparisons do not define your worth or potential."*
  4. `SCN02_04` (`UNDERSTAND` | `STAGE_4`): *"When communication at home repeatedly dissolves into yelling or dismissal, staying silent feels like the only defense. Before attempting another exhausting conversation, give yourself permission to step back and write out your feelings in private."*
  5. `SCN02_05` (`LISTEN` | `STAGE_1`): *"Having your feelings dismissed as 'dramatic' or 'disrespectful' by family hurts deeply. Your emotional reality is valid, even if your parents lack the tools to acknowledge it. I'm right here with you."*
  6. `SCN02_06` (`REFLECT` | `STAGE_3`): *"Parents often project their own unhealed anxieties and generational fears onto their children under the banner of protection. Recognizing their fear doesn't mean you have to absorb it as your truth."*
  7. `SCN02_07` (`CALM` | `STAGE_5`): *"You don't have to resolve years of family communication patterns tonight. Protect your peace in your own space, take a slow grounding breath, and let yourself rest away from the tension."*
  8. `SCN02_08` (`LISTEN` | `STAGE_2`): *"Feeling unseen under your own roof is one of the loneliest feelings in the world. You have a sanctuary right here to speak openly without fear of judgment. What do you wish they could understand?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"My parents never understand me."* $\to$ Matches `SCN02_01`.
  - Turn 2: *"They want me to do something I don't even like."* $\to$ Matches `SCN02_02`.
  - Turn 3: *"I can't even explain my side."* $\to$ Matches `SCN02_04`.

---

### 2.3 Scenario 3: Failure and Disappointment
- **Identifier**: `SCENARIO_03_FAILURE`
- **Recognized Triggers**:
  - `\b(worked\s+so\s+hard\s+and\s+still\s+failed|failed\s+(my\s+)?(exam|test|interview|finals)|everyone\s+else\s+passed|not\s+capable|complete\s+failure|ruined\s+my\s+chance|flunked|rejected\s+from\s+(the\s+)?(job|college|university|program)|let\s+everyone\s+down)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[EXAM_FAILURE, OPPORTUNITY_REJECTION, MISTAKE_MADE]`
  - Primary: `LOW_SELF_ESTEEM` or `ACADEMIC_STRESS`; Secondary: `SADNESS`, `GUILT`, `SHAME`.
- **Response Templates (`SCN03_01` – `SCN03_08`)**:
  1. `SCN03_01` (`UNDERSTAND` | `STAGE_1`): *"Pouring your energy, sleep, and heart into something and still facing failure is devastating. I'm not going to give you clichés about failure being a lesson right now—it hurts, and you deserve space to feel that grief. What was the outcome you worked so hard for?"*
  2. `SCN03_02` (`REFLECT` | `STAGE_2`): *"Seeing everyone else pass or celebrate while you're holding a disappointing result stings with acute isolation. But another person's success does not mean your effort was meaningless. Take a slow, steady breath."*
  3. `SCN03_03` (`CALM` | `STAGE_3`): *"Failing at a task or exam does not make you a failure as a human being. Your brain wants to generalize one bad outcome into an entire identity. Let's gently decouple this result from your permanent intelligence."*
  4. `SCN03_04` (`UNDERSTAND` | `STAGE_4`): *"When the initial shock and sting begin to settle, we can look at practical options together—re-evaluations, re-takes, or different routes. But for tonight, your only job is to be gentle with yourself."*
  5. `SCN03_05` (`LISTEN` | `STAGE_1`): *"Feeling like you let down everyone who believed in you is a crushing weight. But genuine people care about you, not just your test scores or resume. I'm holding space with you."*
  6. `SCN03_06` (`REFLECT` | `STAGE_3`): *"Effort doesn't always guarantee immediate success, and that is one of the hardest truths of being human. Give yourself credit for showing up and fighting, even if the scoreboard didn't reflect it."*
  7. `SCN03_07` (`CALM` | `STAGE_5`): *"Do not make drastic declarations about your future while you are in the acute fog of disappointment. Drink some water, let your shoulders drop, and allow today to just be quiet."*
  8. `SCN03_08` (`LISTEN` | `STAGE_2`): *"Mistakes and missteps are part of the process, even when they carry real consequences. What part of the result feels heaviest right now?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I worked so hard and still failed."* $\to$ Matches `SCN03_01`.
  - Turn 2: *"Everyone else passed."* $\to$ Matches `SCN03_02`.
  - Turn 3: *"Maybe I'm just not capable."* $\to$ Matches `SCN03_03`.

---

### 2.4 Scenario 4: Future Uncertainty & Career Stress
- **Identifier**: `SCENARIO_04_CAREER_UNCERTAINTY`
- **Recognized Triggers**:
  - `\b(no\s+idea\s+what\s+i'?m\s+doing\s+with\s+my\s+life|career\s+(path|uncertainty|stress)|wrong\s+degree|directionless|wasting\s+my\s+time|pressure\s+to\s+be\s+successful|terrified\s+of\s+the\s+future|everyone\s+has\s+a\s+plan)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[CAREER_UNCERTAINTY, FEAR_WRONG_DEGREE, DIRECTIONLESS]`
  - Primary: `ANXIETY` or `OVERTHINKING`; Secondary: `FEAR`, `CONFUSION`.
- **Response Templates (`SCN04_01` – `SCN04_08`)**:
  1. `SCN04_01` (`UNDERSTAND` | `STAGE_1`): *"Having no idea what you're doing with your life is terrifying, but it is also far more normal than anyone admits out loud. You don't need a ten-year master plan today. What decision is pressing on you most right now?"*
  2. `SCN04_02` (`REFLECT` | `STAGE_2`): *"When you look around and it seems like everyone has an organized roadmap, remember that most people are figuring things out step by step behind closed doors. You are not behind; you are simply in the exploratory phase of your own life."*
  3. `SCN04_03` (`CALM` | `STAGE_3`): *"Fearing that you'll make an irreversible choice traps your mind in analysis paralysis. Most career decisions are pivots, not prison sentences. Take a slow breath—we can break the uncertainty down into smaller steps."*
  4. `SCN04_04` (`UNDERSTAND` | `STAGE_4`): *"Instead of trying to discover your 'one true passion' under immense pressure, ask yourself: what is one subject, skill, or project you'd be curious to explore for just the next two weeks?"*
  5. `SCN04_05` (`LISTEN` | `STAGE_1`): *"The societal pressure to have your life, income, and career figured out in your twenties is overwhelming. Your worth as a person is not measured by how fast you climb a career ladder. I'm right here with you."*
  6. `SCN04_06` (`REFLECT` | `STAGE_2`): *"Questioning your degree or chosen major is scary when you've invested time and tuition. But recognizing that something isn't fitting is self-awareness, not failure. What feels misaligned?"*
  7. `SCN04_07` (`CALM` | `STAGE_5`): *"Bring your attention back to where your feet are right now. The future will be built day by day, not solved in a single anxious evening. Settle your mind and take one restful breath."*
  8. `SCN04_08` (`LISTEN` | `STAGE_3`): *"Feeling directionless often means old goals no longer fit who you are becoming. Let's make space for that evolution without panic. What used to feel meaningful to you?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I have no idea what I'm doing with my life."* $\to$ Matches `SCN04_01`.
  - Turn 2: *"Everyone around me has a plan."* $\to$ Matches `SCN04_02`.
  - Turn 3: *"I'm scared I'll make the wrong decision."* $\to$ Matches `SCN04_03`.

---

### 2.5 Scenario 5: Social Comparison & Inferiority
- **Identifier**: `SCENARIO_05_SOCIAL_COMPARISON`
- **Recognized Triggers**:
  - `\b(everyone\s+(my\s+age|around\s+me)\s+is\s+(doing\s+better|smarter|ahead|happier)|comparing\s+myself\s+to|social\s+media\s+makes\s+me\s+feel|seeing\s+people\s+travel(l)?ing|feel\s+inferior|falling\s+behind\s+in\s+life|wasting\s+my\s+life)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[ACADEMIC_COMPARISON, PEER_ACHIEVEMENT_ENVY, SOCIAL_MEDIA_ENVY]`
  - Primary: `LOW_SELF_ESTEEM` or `INSECURITY`; Secondary: `DEPRESSION_DISTRESS`, `FRUSTRATION`.
- **Response Templates (`SCN05_01` – `SCN05_08`)**:
  1. `SCN05_01` (`UNDERSTAND` | `STAGE_1`): *"Feeling like everyone your age is living meaningful, exciting lives while you are standing still is an excruciating form of self-torture. It is completely understandable that you're feeling left behind. What triggered this comparison today?"*
  2. `SCN05_02` (`REFLECT` | `STAGE_2`): *"Social media presents a curated highlight reel of other people's promotions, travels, and celebrations. It never shows their panic attacks, private arguments, or feelings of inadequacy. Comparing your behind-the-scenes to their polished stage will always feel unfair."*
  3. `SCN05_03` (`CALM` | `STAGE_3`): *"Take a gentle, full breath and step away from the feed for the rest of tonight. Your journey has its own unique obstacles, timing, and seasons. You cannot run someone else's race."*
  4. `SCN05_04` (`UNDERSTAND` | `STAGE_4`): *"When feelings of inferiority flare up, ask yourself: what is one small thing in your own life that you genuinely care about, completely outside of whether anyone else approves of it?"*
  5. `SCN05_05` (`LISTEN` | `STAGE_1`): *"Hearing about peers earning more money or achieving milestones can sting, even when you want to be happy for them. You don't have to feel guilty for that sting—it's just your own unmet needs speaking up. I'm listening."*
  6. `SCN05_06` (`REFLECT` | `STAGE_2`): *"Believing that everyone else has it all figured out is an illusion. We are all improvising in different ways. Anchor back into what you can control right now: your own rest, your own next step, your own peace."*
  7. `SCN05_07` (`CALM` | `STAGE_5`): *"Close the apps, put your phone face down, and let your shoulders release. Your life is not a competition against your peers. Settle into this quiet moment."*
  8. `SCN05_08` (`LISTEN` | `STAGE_3`): *"You are not wasting your life simply because you are moving at a different pace or facing heavier burdens. Your experiences and perseverance matter. What feels hardest right now?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"Everyone my age is doing something meaningful except me."* $\to$ Matches `SCN05_01`.
  - Turn 2: *"I see people travelling, earning and enjoying life."* $\to$ Matches `SCN05_02`.
  - Turn 3: *"I feel like I'm wasting my life."* $\to$ Matches `SCN05_08` & `SCN05_03`.

---

## 3. Batch 1 Verification & Test Requirements

1. **Unit Test Suite**: Minimum of 25 multi-turn automated tests (5 tests per scenario $\times$ 5 scenarios) implemented in Kotlin (`app/src/test/java/com/example/safety/Batch1ScenariosTest.kt`) and TypeScript (`web/src/tests/batch1Validation.ts`).
2. **Regression Invariant**: All existing 102 Android unit tests and 53 Web tests must pass with zero regressions.
3. **Safety Priority**: Verification that crisis screening and immediate emergency interception (Levels 1–4) override any Batch 1 scenario trigger immediately.
