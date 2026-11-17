# Solace+ — Deterministic Rule-Based Emotional Conversation Engine
## System Architecture & Technical Specifications

---

## 1. Executive Summary

Solace+ is an offline-first, sovereign emotional health companion designed to provide compassionate, context-aware, non-judgmental conversational support without relying on any external Large Language Model (LLM), cloud inference API, or paid AI service.

By replacing unpredictable, non-deterministic cloud generative models with a deterministic rule-based emotional conversation engine, Solace+ achieves:
1. **Absolute Mental Health Safety Guarantees**: Crisis signals cannot be hallucinated, diluted, or bypassed by adversarial phrasing or conversational drift.
2. **Deterministic Response Attribution**: Every emitted statement is traced back to a psychologically reviewed, human-written template.
3. **Zero Network Latency & Zero External Cost**: Instant (<10ms) local processing with zero API subscription fees or rate limits.
4. **Complete Privacy & Sovereign Data**: No user thoughts, journals, or emotional disclosures ever leave the user's device.

---

## 2. High-Level Pipeline Architecture

The system operates on a multi-stage sequential evaluation pipeline:

```mermaid
flowchart TD
    UserMsg([Incoming User Message]) --> Preprocess[Step 1: Input Normalization & Sanity Scrub]
    Preprocess --> StateCheck{Step 2: Active Safety Assessment Pending?}
    
    StateCheck -->|Yes: Awaiting Answer| SafetyProg[Step 3: Safety Question Progression State Machine]
    SafetyProg --> SafetyAck[Acknowledge Answer & Emit Next Assessment Question / Relief Response]
    
    StateCheck -->|No| MetaphorCheck[Step 4: Metaphorical Idiom & Negation Disambiguation]
    MetaphorCheck --> CrisisFilter{Step 5: Crisis Priority Override?\nLevel 3/4 or Active Danger}
    
    CrisisFilter -->|Triggered| CrisisIntervention[Emit Verified Crisis Response & Emergency Helplines\nIndia: 112, 14416, 1800-599-0019 | US: 988]
    
    CrisisFilter -->|Not Crisis| TopicLock{Step 6: Previous Turn in Unresolved Crisis?}
    TopicLock -->|Yes: Attempting Topic Switch| EnforceLock[Topic Lock: Acknowledge Topic & Reaffirm Safety First]
    
    TopicLock -->|No| EmotionClassifier[Step 7: 32-Category Deterministic Emotional Classifier]
    EmotionClassifier --> ConfCheck{Confidence >= 0.70?}
    
    ConfCheck -->|No: Low Confidence| Clarify[Emit Gentle Clarification Inquiry]
    ConfCheck -->|Yes| ModeSelector[Step 8: Conversational Mode Selector\nLISTEN | UNDERSTAND | REFLECT | CALM]
    
    ModeSelector --> DedupFilter[Step 9: Sliding-Window Deduplication\nExclude Templates Used in Last 10 Turns]
    DedupFilter --> TemplateScorer[Step 10: Multi-Factor Scoring & Selection]
    TemplateScorer --> Personalize[Step 11: Gentle Personalization with User Name]
    Personalize --> DeliveredMsg([Delivered Companion Response])
```

---

## 3. Core Engine Components

### 3.1 Input Normalizer & Lexical Analyzer
- Cleans whitespace, normalizes Unicode punctuation, contracts spaces, and strips conversational noise.
- Normalizes informal contractions (`"im"`, `"wanna"`, `"dont"`, `"can't"`), emotional elongations (`"exhausteddd"`, `"cryingggg"`), and common emotional misspellings.

### 3.2 Clinical Safety State Machine (`SafetyConversationManager` / `SafetyService`)
- Operates independently and with absolute priority over the normal dialogue system.
- Enforces a deterministic 5-stage clinical screening progression:
  1. `CURRENT_SUICIDAL_THOUGHTS`: Inquires gently about current ideation.
  2. `IMMEDIATE_INTENT`: Assesses whether the user has decided or thought about acting.
  3. `PLAN_AND_TIMEFRAME`: Screens for timeline, methods, and specific plans.
  4. `ACCESS_TO_MEANS`: Determines if means or hazardous objects are physically accessible.
  5. `HUMAN_SUPPORT`: Assesses whether trusted human support is nearby or can be reached.
- Topic-Lock Protocol: If an assessment is pending or unresolved, the user is prevented from casually switching topics until their safety is explicitly confirmed.

### 3.3 Contextual Disambiguation Engine
- **Metaphorical Idiom Filter**: Prevents non-suicidal expressions from falsely tripping crisis guardrails (e.g., *"I killed that exam"* $\to$ `POSITIVE_EMOTIONS`, *"This homework is killing me"* $\to$ `ACADEMIC_STRESS`, *"I'm dying to see the concert"* $\to$ `POSITIVE_EMOTIONS`, *"Dead tired"* $\to$ `EMOTIONAL_EXHAUSTION`).
- **Negation Analyzer**: Distinguishes affirmative crisis declarations (*"I want to die"*) from protective or distress-based negations (*"I don't want to die"*, *"I would never hurt myself"*), mapping them to `DEPRESSION_DISTRESS` with appropriate emotional holding space rather than false alarm emergency escalations.
- **Third-Person & External Object Filter**: Disambiguates external media references (*"That movie was depressing"*) and third-party narratives (*"My friend broke up"*) from personal emotional states.

### 3.4 32-Category Deterministic Emotional Classifier (`EmotionClassifier`)
Classifies incoming utterances across 32 clinically grounded and conversational emotional dimensions:
- High-Risk: `IMMEDIATE_PHYSICAL_DANGER`, `SUICIDAL_IDEATION`, `SELF_HARM`
- Core Emotional Distress: `PANIC`, `ANXIETY`, `OVERTHINKING`, `SADNESS`, `LONELINESS`, `DEPRESSION_DISTRESS`, `GRIEF`, `EMOTIONAL_NUMBNESS`, `EMOTIONAL_EXHAUSTION`, `BURNOUT`
- Situational Stressors: `ACADEMIC_STRESS`, `WORK_RELATED_STRESS`, `RELATIONSHIP_PROBLEMS`, `HEARTBREAK`, `REJECTION`, `FAMILY_CONFLICT`, `SOCIAL_ANXIETY`
- Reactive & Evaluative States: `ANGER`, `FRUSTRATION`, `LOW_SELF_ESTEEM`, `INSECURITY`, `FEAR`, `GUILT`, `SHAME`, `CONFUSION`
- Somatic & Behavioral States: `SLEEP_DIFFICULTIES`, `PROCRASTINATION`, `LACK_OF_MOTIVATION`
- Well-Being: `POSITIVE_EMOTIONS`

### 3.5 Conversational Strategy Selector (Phase 4)
Assigns one of four primary conversational modes:
- **`LISTEN`**: Pure unconditional holding space. Acknowledges and validates without offering unsolicited advice or rushing the user. Used for `SADNESS`, `LONELINESS`, `GRIEF`, and `POSITIVE_EMOTIONS`.
- **`UNDERSTAND`**: Curious, gentle context exploration. Uncovers triggers and underlying patterns. Used for `OVERTHINKING`, `ACADEMIC_STRESS`, `RELATIONSHIP_PROBLEMS`, `ANGER`, and `FRUSTRATION`.
- **`REFLECT`**: Cognitive mirroring and philosophical reframing. Validates the emotional reality while gently exploring self-compassion. Used for `LOW_SELF_ESTEEM`, `GUILT`, `SHAME`, and `INSECURITY`.
- **`CALM`**: Somatic nervous system regulation. Introduces physiological anchors (4-7-8 breathing, dropping the shoulders, 5-4-3-2-1 sensory grounding). Used for `ANXIETY`, `PANIC`, `BURNOUT`, and `SLEEP_DIFFICULTIES`.

### 3.6 Response Deduplication & Sliding Window Memory (Phase 6)
- Maintains an in-memory sliding window history of at least 10 conversation turns per session.
- Excludes recently used template IDs from candidate pools.
- Records all asked screening questions to guarantee that a question once answered is never repeated in an infinite loop.

---

## 4. Cross-Platform Parity

The deterministic emotional engine is implemented symmetrically across both native Android and Web targets:

| Architecture Layer | Android Implementation | Web Implementation |
|---|---|---|
| **Language** | Kotlin 2.0+ / Coroutines | TypeScript 5.8+ / ES Modules |
| **UI Framework** | Jetpack Compose Material3 | React 19 / Modern CSS Design Tokens |
| **Emotional Classifier** | `EmotionalClassificationEngine.kt` | `emotionClassifier.ts` |
| **Response Library** | `EmotionalResponseLibrary.kt` (105+ templates) | `responseLibrary.ts` (105+ templates) |
| **Conversation State** | `DeterministicEmotionalEngine.kt` | `conversationEngine.ts` |
| **Safety Manager** | `SafetyConversationManager.kt` / `EscalationManager.kt` | `safetyService.ts` |
| **Local Storage** | Room DB SQLite (`SafetyStateEntity`, `ChatMessageEntity`) | LocalStorage / IndexedDB (`solace_vault`) |
| **Latency** | < 5ms local execution | < 2ms local execution |
| **Network Reliance** | 0 bytes / 100% Offline | 0 bytes / 100% Offline |
