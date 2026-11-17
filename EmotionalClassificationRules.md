# Solace+ — Emotional Classification Rules & Lexicon Specification

---

## 1. Overview
The Solace+ Emotional Classification Engine deterministic NLP pipeline classifies free-form conversational messages across 32 distinct emotional, behavioral, and clinical safety categories. It utilizes a prioritized sequence of regular expressions, synonym lexicons, idiomatic metaphor filters, negation analyzers, and intensity modulators.

---

## 2. The 32 Emotional Categories

| Category Index | Category ID | Clinical / Emotional Focus | Sample User Triggers |
|---|---|---|---|
| 1 | `IMMEDIATE_PHYSICAL_DANGER` | Physical violence, assault, stalking | *"Someone is following me", "Call the police", "He has a weapon"* |
| 2 | `SUICIDAL_IDEATION` | Active or passive suicide declarations | *"I want to die", "Kill myself", "Better off dead", "End it all"* |
| 3 | `SELF_HARM` | Non-suicidal self-injury, cutting, overdose | *"Cut myself", "Hurting myself", "Burn myself", "Slit my wrist"* |
| 4 | `PANIC` | Acute somatic panic, hyperventilation | *"Panic attack", "Can't catch my breath", "Heart racing", "Freaking out"* |
| 5 | `ANXIETY` | Generalized worry, nervousness, apprehension | *"Feeling anxious", "On edge", "Restless", "Butterflies in stomach"* |
| 6 | `OVERTHINKING` | Cognitive rumination, thought looping | *"Mind won't stop", "Overthinking everything", "Racing thoughts", "Spiraling"* |
| 7 | `SADNESS` | Dysphoria, sorrow, acute crying | *"Feeling sad", "Crying all day", "Heavy heart", "Bad day", "Downhearted"* |
| 8 | `LONELINESS` | Social isolation, feeling forgotten | *"All alone", "Nobody cares", "No one to talk to", "Feeling abandoned"* |
| 9 | `DEPRESSION_DISTRESS` | Anhedonia, hopelessness, existential emptiness | *"Hopeless", "Empty inside", "Nothing matters", "Dark place", "Despair"* |
| 10 | `GRIEF` | Bereavement, loss of loved ones | *"Passed away", "Grieving", "Miss them so much", "Loss of my grandmother"* |
| 11 | `EMOTIONAL_NUMBNESS` | Dissociation, emotional blunting | *"Feeling numb", "Blank inside", "Can't feel anything", "Detached", "Hollow"* |
| 12 | `EMOTIONAL_EXHAUSTION` | Compassion fatigue, depletion | *"Emotionally drained", "No energy left", "Tired of trying", "Running on empty"* |
| 13 | `BURNOUT` | Chronic occupational or life exhaustion | *"Burned out", "Overworked", "Pushed to my limit", "Can't keep doing this"* |
| 14 | `ACADEMIC_STRESS` | Exam anxiety, syllabus overload, GPA panic | *"Failing my exams", "Finals week", "Studying all night", "Homework stress"* |
| 15 | `WORK_RELATED_STRESS` | Boss conflict, toxic workplace, deadlines | *"Toxic boss", "Deadline tomorrow", "Laid off", "Working 12 hours"* |
| 16 | `RELATIONSHIP_PROBLEMS` | Interpersonal discord, mistrust | *"Fight with my partner", "Drifting apart", "Trust issues", "Toxic relationship"* |
| 17 | `HEARTBREAK` | Romantic breakup, abandonment | *"Heartbroken", "We broke up", "Dumped me", "Miss my ex", "Shattered heart"* |
| 18 | `REJECTION` | Social or romantic dismissal | *"Got rejected", "They turned me down", "Nobody wants me", "Ghosted"* |
| 19 | `FAMILY_CONFLICT` | Parental conflict, home hostility | *"Parents yelling", "Family drama", "Fight with my mom", "Strict parents"* |
| 20 | `SOCIAL_ANXIETY` | Evaluative social fear, post-event replay | *"Social anxiety", "Judged by others", "Awkward in public", "Embarrassed myself"* |
| 21 | `ANGER` | Acute wrath, betrayal, boundary violation | *"So angry", "Furious", "Pissed off", "Want to scream", "Boiling with rage"* |
| 22 | `FRUSTRATION` | Blocked progress, exasperation | *"Frustrated", "Nothing works", "Fed up", "At my wit's end", "Annoyed"* |
| 23 | `LOW_SELF_ESTEEM` | Self-hatred, feelings of inadequacy | *"Hate myself", "I'm a failure", "I'm a loser", "Never good enough", "Worthless"* |
| 24 | `INSECURITY` | Unfavorable comparison, body doubt | *"Insecure", "Comparing myself", "Imposter syndrome", "Feel inadequate"* |
| 25 | `FEAR` | Future apprehension, acute terror | *"Terrified", "Scared of failing", "Dreading the future", "Fearful"* |
| 26 | `GUILT` | Moral regret, feeling responsible for ruin | *"Feel so guilty", "My fault", "I ruined everything", "Should have done better"* |
| 27 | `SHAME` | Identity-level self-condemnation | *"Ashamed", "Deep shame", "Humiliated", "Mortified", "Can't look at myself"* |
| 28 | `CONFUSION` | Ambivalence, decision paralysis | *"Confused", "Don't know what to do", "Mixed feelings", "Head is clouded"* |
| 29 | `SLEEP_DIFFICULTIES` | Insomnia, sleep disruption | *"Can't sleep", "Insomnia", "Tossing and turning", "Stayed awake all night"* |
| 30 | `PROCRASTINATION` | Task avoidance, doomscrolling | *"Procrastinating", "Putting it off", "Avoiding work", "Doomscrolling"* |
| 31 | `LACK_OF_MOTIVATION` | Apathy, lost momentum | *"No motivation", "Unmotivated", "No drive", "Apathetic", "Lost my spark"* |
| 32 | `POSITIVE_EMOTIONS` | Joy, gratitude, relief, peace | *"Feeling good", "Peaceful", "Grateful", "Doing well", "Proud of myself"* |

---

## 3. Disambiguation Engine Specification

### 3.1 Metaphor & Idiom Disambiguation
Conventional keyword matching falsely flags common conversational expressions containing words like *"kill"*, *"die"*, or *"dead"*. The engine evaluates these expressions first and routes them away from safety escalations:

```
Pattern 1: \b(killed\s+(that|the|my|this)\s+(exam|test|interview|presentation))\b
Action:    Classify as POSITIVE_EMOTIONS (Confidence: 0.95)

Pattern 2: \b(killing\s+it|crushing\s+it|slaying\s+it|nailed\s+it)\b
Action:    Classify as POSITIVE_EMOTIONS (Confidence: 0.95)

Pattern 3: \b(dying\s+(to\s+see|to\s+know|to\s+hear|to\s+go|of\s+laughter))\b
Action:    Classify as POSITIVE_EMOTIONS (Confidence: 0.95)

Pattern 4: \b(dead\s+tired|dead\s+exhausted|my\s+feet\s+are\s+killing\s+me)\b
Action:    Classify as EMOTIONAL_EXHAUSTION (Confidence: 0.90)

Pattern 5: \b(this\s+(exam|homework|project|bug)\s+is\s+killing\s+me)\b
Action:    Classify as ACADEMIC_STRESS / WORK_RELATED_STRESS (Confidence: 0.90)
```

### 3.2 Negation Analysis
Negated statements must not be interpreted as affirmative declarations.
- Trigger Regex: `\b(i\s+)?(don'?t|do\s+not|never)\s+(want\s+to|wanna|plan\s+to)\s+(die|end\s+my\s+life|kill\s+myself|hurt\s+myself)\b`
- Evaluated State: `isNegated = true`
- Handling: When a user states *"I'm in a lot of pain but I don't want to die"*, the engine classifies the input as `DEPRESSION_DISTRESS` (or general emotional distress) with `isNegated = true`, preventing false Level 3 suicidal ideation alarms while providing warm, compassionate accompaniment.

### 3.3 Third-Person & External Media Disambiguation
The engine distinguishes the user's personal emotional state from discussions about media, weather, or third parties:
- Media Object Regex: `\b(that|this|the)\s+(movie|film|book|novel|show|story|song)\s+(is|was|feels)\s+(so\s+)?(depressing|sad|awful)\b`
- Outcome: Marked as `isThirdPersonOrObject = true`, classified as `CONFUSION` or holding space, rather than personal clinical depression.
- Third-Person Regex: `\b(he|she|they|my\s+friend|my\s+sister)\s+(is|was|feels)\s+(sad|depressed|angry)\b`
- Outcome: Marked as external subject, asking the user how they are holding up while supporting their friend or family member.

---

## 4. Emotional Intensity Scoring

Intensity is evaluated by analyzing linguistic amplifiers:
- **`CRITICAL`**: Present in affirmative suicidal declarations, imminent self-harm, or active physical danger.
- **`HIGH`**: Triggered by modifiers such as *"unbearable"*, *"extremely"*, *"completely"*, *"cannot take it"*, *"falling apart"*, *"paralyzed"*, *"heart pounding"*. Triggers suggested somatic grounding exercises.
- **`MODERATE`**: Default emotional baseline for expressed feelings without extreme distress tokens.
- **`MILD`**: Triggered by softeners such as *"a little"*, *"a bit"*, *"slightly"*, *"mildly"*, *"kind of"*.

---

## 5. Low-Confidence & Clarification Protocol

When an input does not contain identifiable emotional triggers, or is too ambiguous:
1. `primaryCategory` is set to `null` and `requiresClarification` is set to `true`.
2. The engine deterministically selects a non-invasive, open-ended inquiry from `CLARIFICATION_INQUIRIES` (e.g., *"I want to make sure I truly understand what you're going through. Could you tell me a little more about what's been happening?"*).
3. The system never makes wild assumptions or jumps to inaccurate diagnoses.
