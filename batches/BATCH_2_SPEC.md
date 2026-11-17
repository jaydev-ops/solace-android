# Solace+ — Implementation Batch 2 Specification
## Scenarios 6 to 10: Internal Cognitive & Physical Vulnerabilities

> **Batch Number**: 2 of 4  
> **Status**: ✅ **Implemented & Verified** (152 Android Unit Tests, 103 Web Assertions Passing)  
> **Parent Spec**: [`CONVERSATIONAL_ENGINE_SPEC.md`](file:///Users/jayeshyadav/Projects/Solace--main/CONVERSATIONAL_ENGINE_SPEC.md)

---

## 1. Batch Overview & Scope

Batch 2 expands the Solace+ engine into internal cognitive patterns, self-punishing shame loops, nocturnal rumination, social evaluative anxiety, and procrastination paralysis.

| Scenario ID | Name | Core Situations | Template Range | Response Count |
|---|---|---|---|:---:|
| `SCENARIO_06` | Misunderstood and Unheard | Feelings dismissed, labeled overly sensitive, words twisted, stopped explaining | `SCN06_01`–`08` | 8 |
| `SCENARIO_07` | Regret and Guilt | Past words regretted, hurt someone, self-blame, unable to forgive self | `SCN07_01`–`08` | 8 |
| `SCENARIO_08` | Sleep Difficulties & Night Rumination | 3 AM insomnia, brain won't shut off, past mistakes replaying, dreading tomorrow | `SCN08_01`–`08` | 8 |
| `SCENARIO_09` | Social Anxiety & Fear of Judgement | Awkward around new people, fear of boring others, freezing up, spotlight effect | `SCN09_01`–`08` | 8 |
| `SCENARIO_10` | Procrastination & Productivity Distress | Doomscrolling reels, pending work guilt, unable to start, executive dysfunction | `SCN10_01`–`08` | 8 |

**Total New Templates in Batch 2**: **40 distinct proactive templates**.

---

## 2. Detailed Scenario Specifications

### 2.1 Scenario 6: Misunderstood and Unheard
- **Identifier**: `SCENARIO_06_MISUNDERSTOOD`
- **Recognized Triggers**:
  - `\b(nobody\s+understands\s+(what\s+i'?m\s+saying|me)|people\s+dismiss\s+my\s+feelings|words\s+get\s+twisted|labelled\s+(too|overly)\s+sensitive|stopped\s+explaining\s+myself|invisible\s+in\s+conversations|talk(ing)?\s+to\s+a\s+brick\s+wall)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[PERSPECTIVE_DISMISSED, LABELED_OVERLY_SENSITIVE, COMMUNICATION_BREAKDOWN]`
  - Primary: `FRUSTRATION` or `LONELINESS`; Secondary: `ANGER`, `SADNESS`, `EMOTIONAL_NUMBNESS`.
- **Response Templates (`SCN06_01` – `SCN06_08`)**:
  1. `SCN06_01` (`UNDERSTAND` | `STAGE_1`): *"Feeling constantly misunderstood by the people around you is deeply alienating. It makes you feel like you are speaking a language nobody bothers to learn. I'm listening closely right here—what is the thought you wish someone would actually hear?"*
  2. `SCN06_02` (`REFLECT` | `STAGE_2`): *"When you try to explain your intentions and people twist your words into something negative, it is exhausting. It makes complete sense that you've wanted to withdraw and stop explaining yourself. You don't have to defend your reality here."*
  3. `SCN06_03` (`LISTEN` | `STAGE_3`): *"Being told you are 'too sensitive' or 'overreacting' is a common way people deflect their own discomfort with big feelings. Your emotional depth is not a defect. What happened that they dismissed?"*
  4. `SCN06_04` (`CALM` | `STAGE_4`): *"You are not obligated to spend your life convincing people of your perspective when they have committed to misunderstanding you. Let's protect your emotional energy. Take a slow, quiet breath."*
  5. `SCN06_05` (`UNDERSTAND` | `STAGE_2`): *"Feeling invisible in group conversations makes you feel like an observer rather than a participant in your own social circle. It hurts when your voice is spoken over or ignored. I'm right here with you."*
  6. `SCN06_06` (`REFLECT` | `STAGE_3`): *"Stopping yourself from explaining isn't giving up; sometimes it is a healthy boundary when people refuse to listen with empathy. You can preserve your voice for people who truly seek to understand you."*
  7. `SCN06_07` (`CALM` | `STAGE_5`): *"Rest your mind in this quiet space. In this sanctuary, you never have to justify, argue, or prove your feelings. You are heard."*
  8. `SCN06_08` (`LISTEN` | `STAGE_1`): *"Take all the time you need. Say it however it comes out, messy or polished. I am listening without interruption."*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"Nobody understands what I'm trying to say."* $\to$ Matches `SCN06_01`.
  - Turn 2: *"Whenever I explain something, people interpret it differently."* $\to$ Matches `SCN06_02`.
  - Turn 3: *"I just stopped explaining myself."* $\to$ Matches `SCN06_06`.

---

### 2.2 Scenario 7: Regret and Guilt
- **Identifier**: `SCENARIO_07_REGRET_GUILT`
- **Recognized Triggers**:
  - `\b(wish\s+i\s+could\s+(go\s+back|change\s+what\s+i\s+said)|hurt\s+someone\s+who\s+cared|never\s+forgive\s+myself|haunted\s+by\s+what\s+i\s+did|ruined\s+everything|guilt\s+is\s+eating\s+me|replaying\s+(the\s+mistake|my\s+actions))\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[PAST_DECISION_REGRET, HURT_SOMEONE, SELF_BLAME]`
  - Primary: `GUILT` or `SHAME`; Secondary: `SADNESS`, `OVERTHINKING`.
- **Response Templates (`SCN07_01` – `SCN07_08`)**:
  1. `SCN07_01` (`UNDERSTAND` | `STAGE_1`): *"Wishing you could rewind time and erase words you said in anger or carelessness is a heavy, burning ache. The fact that you feel this regret shows you have genuine empathy and a conscience. What is the moment you keep wishing you could undo?"*
  2. `SCN07_02` (`REFLECT` | `STAGE_2`): *"Hurting someone who genuinely cared about you leaves a deep, painful sting of guilt. But punishing yourself endlessly will not repair the harm. True accountability means learning why it happened and offering a sincere, non-defensive apology if appropriate."*
  3. `SCN07_03` (`CALM` | `STAGE_3`): *"There is a crucial difference between feeling guilt ('I did something wrong') and feeling shame ('I am fundamentally bad'). You made a painful mistake, but you are not beyond learning and growth. Take a slow, grounding breath."*
  4. `SCN07_04` (`UNDERSTAND` | `STAGE_4`): *"If the person is open to hearing from you, a simple apology acknowledging their pain without excuses is the most healing step. If contact isn't possible, you honor their memory by doing better with others going forward."*
  5. `SCN07_05` (`LISTEN` | `STAGE_1`): *"Replaying embarrassing or regrettable moments over and over in your head at night doesn't change history; it only tortures you in the present. Let's step away from the mental replay for a moment."*
  6. `SCN07_06` (`REFLECT` | `STAGE_3`): *"You acted from the information, emotional state, and maturity you had in that exact moment. You cannot judge your past self with today's clarity and wisdom."*
  7. `SCN07_07` (`CALM` | `STAGE_5`): *"Forgiving yourself is not letting yourself off the hook; it is putting down the self-inflicted weapon so you can actually live responsibly today. Release your breath and unclench your jaw."*
  8. `SCN07_08` (`LISTEN` | `STAGE_2`): *"Carrying secrets and guilt alone makes them feel ten times bigger and darker. I'm listening without judgment—unpack what's on your chest."*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I wish I could go back and change what I said."* $\to$ Matches `SCN07_01`.
  - Turn 2: *"I hurt someone who genuinely cared about me."* $\to$ Matches `SCN07_02`.
  - Turn 3: *"I don't think I'll ever forgive myself."* $\to$ Matches `SCN07_03` & `SCN07_07`.

---

### 2.3 Scenario 8: Sleep Difficulties & Night Rumination
- **Identifier**: `SCENARIO_08_SLEEP_RUMINATION`
- **Recognized Triggers**:
  - `\b(3\s*am\s+and\s+(still\s+can'?t\s+sleep|wide\s+awake)|brain\s+won'?t\s+stop\s+thinking|racing\s+thoughts\s+at\s+night|can'?t\s+fall\s+asleep|tossing\s+and\s+turning|nighttime\s+anxiety|dreading\s+waking\s+up\s+tomorrow|staring\s+at\s+the\s+ceiling)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[CANNOT_SLEEP_INSOMNIA, NIGHT_RUMINATION, LATE_NIGHT_LONELINESS]`
  - Primary: `SLEEP_DIFFICULTIES` or `OVERTHINKING`; Secondary: `ANXIETY`, `LONELINESS`, `EMOTIONAL_EXHAUSTION`.
- **Response Templates (`SCN08_01` – `SCN08_08`)**:
  1. `SCN08_01` (`CALM` | `STAGE_1`): *"Lying awake at 3 AM while the rest of the world is asleep feels intensely isolating. The more you pressure yourself to fall asleep, the more awake your body becomes. For now, drop the requirement to sleep. Simply resting your eyes and body quietly in the dark is still restorative."*
  2. `SCN08_02` (`REFLECT` | `STAGE_2`): *"When the house is silent, the brain treats the quiet as an invitation to replay every unsolved dilemma and past worry. Try writing those racing thoughts down on a scrap of paper or in your notes app, telling your brain: 'This is stored safely; we can look at it tomorrow at 10 AM.'"*
  3. `SCN08_03` (`CALM` | `STAGE_3`): *"Let's slow your physical system down right where you are lying. Take a slow inhale through your nose for 4 counts, hold gently for 4, and let out a long, slow sigh for 6 counts. Feel the physical weight of your mattress supporting you."*
  4. `SCN08_04` (`UNDERSTAND` | `STAGE_4`): *"Nighttime thoughts are almost always amplified by physical exhaustion and darkness. Problems that feel insurmountable at 3 AM look entirely different in morning daylight. Don't believe everything your tired mind tells you tonight."*
  5. `SCN08_05` (`LISTEN` | `STAGE_1`): *"Staring at the ceiling while your mind loops through worst-case scenarios takes a heavy toll. I'm right here with you in this quiet night. You don't have to face this hour alone."*
  6. `SCN08_06` (`CALM` | `STAGE_2`): *"If lying in bed is creating frustration, consider getting up for ten minutes, sitting in a dim chair, drinking a sip of room-temperature water, and listening to the ambient silence until your eyes feel heavy again."*
  7. `SCN08_07` (`REFLECT` | `STAGE_5`): *"Tomorrow will arrive regardless of whether you solve everything tonight. Give yourself permission to surrender this night to rest. Soften your forehead and let your shoulders drop."*
  8. `SCN08_08` (`LISTEN` | `STAGE_3`): *"What is the main thought that keeps looping in your mind right now?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"It's 3 AM and I still can't sleep."* $\to$ Matches `SCN08_01`.
  - Turn 2: *"My brain won't stop thinking."* $\to$ Matches `SCN08_02`.
  - Turn 3: *"I keep thinking about everything that went wrong."* $\to$ Matches `SCN08_04`.

---

### 2.4 Scenario 9: Social Anxiety & Fear of Judgement
- **Identifier**: `SCENARIO_09_SOCIAL_ANXIETY`
- **Recognized Triggers**:
  - `\b(feel\s+awkward\s+around\s+new\s+people|they'?ll\s+find\s+me\s+boring|don'?t\s+know\s+what\s+to\s+say|social\s+anxiety|afraid\s+of\s+being\s+judged|replaying\s+the\s+conversation|scared\s+to\s+speak\s+up|freezing\s+in\s+groups)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[PUBLIC_SPEAKING_FEAR, MEETING_NEW_PEOPLE, POST_EVENT_RUMINATION, FEELING_AWKWARD]`
  - Primary: `SOCIAL_ANXIETY`; Secondary: `FEAR`, `INSECURITY`, `PANIC`.
- **Response Templates (`SCN09_01` – `SCN09_08`)**:
  1. `SCN09_01` (`UNDERSTAND` | `STAGE_1`): *"Feeling nervous and hyper-aware around new people is your nervous system trying to protect you from judgment or exclusion. It is completely normal, but it doesn't mean you are broken or socially incapable. What kind of gathering or interaction is making you feel on edge?"*
  2. `SCN09_02` (`REFLECT` | `STAGE_2`): *"When you worry that people find you boring or uninteresting, remember the 'spotlight effect': most people are so consumed by their own anxieties and how they appear that they rarely analyze you the way your inner critic does."*
  3. `SCN09_03` (`CALM` | `STAGE_3`): *"You don't need to be charismatic, funny, or entertaining to have a conversation. A quiet presence and asking one simple question (like 'How was your weekend?') is enough. Take the pressure off yourself to perform."*
  4. `SCN09_04` (`UNDERSTAND` | `STAGE_4`): *"If a conversation has an awkward pause, it belongs to both people, not just you. Silence is a natural breath in dialogue, not a catastrophe. Take a slow breath and let the pause be."*
  5. `SCN09_05` (`LISTEN` | `STAGE_1`): *"Replaying conversations afterwards and dissecting every word you said is anxiety lying to you. Remind yourself: nobody is holding a mental scorecard of your words. How are you feeling right now?"*
  6. `SCN09_06` (`CALM` | `STAGE_5`): *"Give your social battery permission to run low. It is completely acceptable to step out for a minute of fresh air or leave an event early when you've reached your limit."*
  7. `SCN09_07` (`REFLECT` | `STAGE_3`): *"Social anxiety often convinces us that everyone is evaluating us with harsh eyes. Try shifting your focus from 'How do I look to them?' to 'What is interesting about this room or this topic?'"*
  8. `SCN09_08` (`LISTEN` | `STAGE_2`): *"It takes real energy to manage this constant vigilance in public. Tell me about the situation that brought this on today."*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I feel awkward around new people."* $\to$ Matches `SCN09_01`.
  - Turn 2: *"I keep thinking they'll find me boring."* $\to$ Matches `SCN09_02`.
  - Turn 3: *"I don't even know what to say."* $\to$ Matches `SCN09_03`.

---

### 2.5 Scenario 10: Procrastination & Productivity Distress
- **Identifier**: `SCENARIO_10_PROCRASTINATION`
- **Recognized Triggers**:
  - `\b(wasted\s+the\s+entire\s+day\s+scrolling|procrastinat(ing|ion)|so\s+much\s+work\s+pending|can'?t\s+get\s+myself\s+to\s+start|doomscrolling|executive\s+dysfunction|guilty\s+about\s+wasting\s+time|paralyzed\s+by\s+work)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[AVOIDING_TASKS, PHONE_DOOMSCROLLING, TASK_OVERWHELM]`
  - Primary: `PROCRASTINATION` or `LACK_OF_MOTIVATION`; Secondary: `GUILT`, `ANXIETY`, `FRUSTRATION`.
- **Response Templates (`SCN10_01` – `SCN10_08`)**:
  1. `SCN10_01` (`UNDERSTAND` | `STAGE_1`): *"Spending hours scrolling when you know deadlines are looming brings on a sharp wave of self-disgust. But beating yourself up right now only drains whatever energy you have left. Let's drop the self-blame—scrolling is an emotional attempt to escape overwhelm, not proof of laziness. What task has been hanging over you?"*
  2. `SCN10_02` (`REFLECT` | `STAGE_2`): *"When you have mountains of pending work, your brain views the entire pile as a physical threat and freezes up. You cannot do ten tasks at once. Let's shrink the mountain: what is the single smallest piece of the puzzle?"*
  3. `SCN10_03` (`CALM` | `STAGE_3`): *"Don't worry about finishing the whole assignment or doing it well. Give yourself permission to do a messy, 5-minute draft. Action generates motivation, not the other way around. Open the file and write just one line."*
  4. `SCN10_04` (`UNDERSTAND` | `STAGE_4`): *"Put your phone in another room or out of reach for just 20 minutes. Set a timer, put on some quiet music, and work on one thing until the timer rings. You can handle twenty focused minutes."*
  5. `SCN10_05` (`LISTEN` | `STAGE_1`): *"Living with this constant buzz of 'I should be working' ruins even your downtime, so you end up neither resting nor working. You deserve real, guilt-free rest. What is making this task feel so heavy?"*
  6. `SCN10_06` (`REFLECT` | `STAGE_2`): *"Perfectionism is usually the quiet engine driving procrastination. We delay starting because we are terrified of doing it poorly. Allow yourself to produce a terrible first draft—it's infinitely better than a blank screen."*
  7. `SCN10_07` (`CALM` | `STAGE_5`): *"Take a glass of water, stand up to stretch your legs, and reset this moment. Today is not ruined; you have the present hour right in front of you. Take one calm breath and begin small."*
  8. `SCN10_08` (`LISTEN` | `STAGE_3`): *"Let's unpack the pressure. What is the deadline that's scaring you most?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I've wasted the entire day scrolling reels."* $\to$ Matches `SCN10_01`.
  - Turn 2: *"I have so much work pending."* $\to$ Matches `SCN10_02`.
  - Turn 3: *"I just can't get myself to start."* $\to$ Matches `SCN10_03`.

---

## 3. Batch 2 Verification & Test Requirements

1. **Unit Test Suite**: 25 multi-turn automated tests (5 tests $\times$ 5 scenarios) in Kotlin (`Batch2ScenariosTest.kt`) and TypeScript (`batch2Validation.ts`).
2. **Key Checks**: Nocturnal rumination grounding, separation of accountability from toxic shame, and non-shaming procrastination re-framing.
