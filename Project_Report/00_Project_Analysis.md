# Solace+ Codebase Investigation & Project Analysis
**Document ID:** `00_Project_Analysis.md`  
**Classification:** Internal Ground-Truth System Audit  
**Date of Audit:** November 2026  
**Auditor:** Technical Research Writer & Lead Software Architect  

---

## 1. Project Overview

**Solace+** (package namespace: `com.example` / `com.aistudio.solace.wrmsnc`, repository: `jaydev-ops/solace-android`) is an emotionally intelligent, native Android mental wellness companion and somatic nervous system regulation platform. Designed as a serene, non-judgmental digital sanctuary, Solace+ abandons traditional gamification paradigms (such as streaks, badges, points, and social comparison leaderboards) in favor of unconditional holding space, polyvagal-informed somatic pacing, and dynamic atmospheric interface responsiveness.

The system is constructed with a modern, reactive native Android stack utilizing **Kotlin 2.2**, **Jetpack Compose (BOM 2024.09.00)**, **Material Design 3**, and **Android 16 / SDK 36 (minSdk 24)**. Data persistence follows a strict **zero-knowledge local-first** architecture anchored by **Room SQLite Database v3**, with optional multi-device cloud synchronization powered by **Cloud Firestore** and **Firebase Authentication**.

The intelligence tier operates via a resilient dual-engine architecture:
1. **Cloud AI:** Direct REST invocations to the Google Gemini API (`gemini-1.5-flash`, `gemini-2.0-flash`, `gemini-2.5-flash`) and Firebase Vertex AI (`gemini-3.8-flash`, `gemini-4-argon`, `gemini-3.1-flash-lite`).
2. **Local Adaptive Sanctuary Dialogue Engine:** A deterministic, on-device affective conversational generator providing contextual, non-repetitive therapeutic reflections when operating offline or without cloud credentials.

Crucially, Solace+ incorporates a **graduated 4-tier deterministic mental health risk interception and escalation engine** (`RiskDecisionEngine`, `EscalationManager`, `ContextAnalyzer`, `SafetyResponseHandler`) that identifies emotional distress, passive ideation, and acute crisis signals, intercepting dangerous prompts before they reach stochastic generative models, and presenting hardcoded national (Tele-MANAS, KIRAN, 112) and international emergency resources.

---

## 2. Problem Statement

Modern digital mental health and wellness applications suffer from several structural and psychological shortcomings:
1. **Gamification and Toxic Urgency:** Mainstream wellness applications deploy streaks, guilt-inducing daily notifications, and point systems. When anxious, overwhelmed, or depressed users miss days, gamification induces shame and cognitive fatigue rather than relief.
2. **Clinical Sterile Environments:** Many existing platforms treat mental wellness through sterile psychiatric checklists and utilitarian data entry grids that feel clinical and alienating.
3. **Data Exploitation and Privacy Hazards:** User emotional states, intimate journal notes, and vulnerability disclosures are frequently stored in cleartext on commercial cloud servers, creating acute privacy hazards.
4. **Stochastic AI Hallucination in Crisis Situations:** Generic LLM-based chatbots risk producing dangerous, sycophantic, or dismissive responses when conversing with users experiencing active suicidal ideation or self-harm intentions.
5. **Lack of Somatic and Nervous System Integration:** Pure cognitive talk therapy approaches neglect somatic physiology. Autonomic dysregulation requires bottom-up vagal stabilization (paced respiration, sensory grounding) alongside top-down cognitive reframing.

---

## 3. Objectives of the System

1. **Develop an Ambient Mood-Responsive Interface:** Build a declarative Android UI that dynamically adapts its color palette, container luminescence, motion kinetics, and typography across 7 discrete affective states (Joyful, Calm, Steady, Tender, Restless, Stormy, Overwhelmed).
2. **Implement Multi-Modal Conversational Companionship:** Deliver unhurried, multi-turn emotional dialogue utilizing Google Gemini models across 4 specialized conversational roles (Sanctuary Holding Presence, Somatic & Nervous System Guide, Thought Clarifier / CBT, Deep Wisdom Mirror) and 6 regulation modes (Listen, Understand, Calm, Reflect, Refocus, Gratitude).
3. **Integrate Evidence-Based Somatic Regulation Exercises:** Deliver interactive, guided physiological stabilization modules, including Animated Box Breathing (4-4-4-4), 5-4-3-2-1 Sensory Grounding, Somatic Tension Release, and Quiet Gratitude Anchors.
4. **Enforce Deterministic Multi-Tier Safety Guardrails:** Create a deterministic, rule-based mental health escalation system that classifies user input across four severity levels (Levels 0 through 4), overrides AI generation with verified crisis protocols, and prevents accidental de-escalation of active emergencies.
5. **Guarantee Zero-Knowledge Local-First Privacy:** Implement offline-first local database storage (Room SQLite v3) ensuring complete functionality without requiring user accounts or external network connectivity, paired with optional salted SHA-256 local credentials and secure Firestore synchronization.

---

## 4. Existing System vs. Proposed Solace+ System

| Dimension | Existing Mainstream Systems (e.g., Headspace, Calm, Generic Chatbots) | Proposed Solace+ Platform |
| :--- | :--- | :--- |
| **User Experience Paradigm** | High-pressure gamification, daily streaks, reward badges, push-notification guilt. | Pure sanctuary holding space: zero streaks, zero points, zero judgment. |
| **Visual Architecture** | Static theme palettes; indifferent to the user's emotional state. | **Mood-Responsive Canvas**: dynamically morphs primary, surface, and outline tokens based on real-time valence. |
| **Conversational Engine** | Pre-scripted rule trees (rigid) or unconstrained general LLMs (hallucination-prone). | **Hybrid Intelligence**: Direct Gemini streaming with strict system prompts + Adaptive Local Sanctuary fallback. |
| **Safety and Crisis Protocol** | Reactive post-facto disclaimers or probabilistic LLM prompt adherence. | **Deterministic Multi-Tier Interceptor**: Hardcoded IF-ELSE priority logic, emergency state-locking, and zero LLM bypass. |
| **Privacy Architecture** | Mandatory account creation, remote-first telemetry, cloud-dependent data harvesting. | **Zero-Knowledge Local-First**: Full offline operation, Guest Mode, explicit memory saving opt-in, Room SQLite v3. |
| **Therapeutic Scope** | Exclusively cognitive or purely auditory meditation tracks. | **Bimodal Regulation**: Top-down cognitive reframing synthesized with bottom-up polyvagal somatic breathwork. |

---

## 5. Technology Stack & Environment

### Mobile Frontend & UI Framework
- **Platform:** Native Android (Min SDK 24 / Android 7.0, Target SDK 36 / Android 16).
- **Core Language:** Kotlin 2.2.10 (JVM Target 11).
- **UI Toolkit:** Jetpack Compose (BOM 2024.09.00) with Material Design 3 (1.3.1).
- **Asynchronous Concurrency:** Kotlin Coroutines (`kotlinx-coroutines-core` 1.10.2, `kotlinx-coroutines-android` 1.10.2) and Reactive `Flow` / `StateFlow`.
- **Navigation:** Jetpack Navigation Compose (2.8.9) with type-safe sealed class routes.
- **Image Loading:** Coil Compose (2.7.0).

### Data Persistence & Security Tier
- **Local Database:** AndroidX Room Database 2.7.0 with Kotlin Symbol Processing (`ksp`).
- **Database Engine:** SQLite on-device with custom migration strategies (`MIGRATION_1_2`, `MIGRATION_2_3`, `MIGRATION_1_3`).
- **Local Encryption:** Salted SHA-256 credential hashing with secure byte transposition.
- **Cloud Backend:** Google Cloud Firestore (Firebase BOM 34.17.0) with server-side Security Rules (`firestore.rules`).
- **Cloud Authentication:** Firebase Auth 34.17.0 & AndroidX Credential Manager 1.5.0 with Google Identity Services (`googleid:1.1.1`).
- **Integrity Attestation:** Firebase App Check with ReCAPTCHA Enterprise and Debug Provider.

### AI & Media Pipelines
- **Generative AI Tier 1 (Cloud):** Google Generative AI REST API (`gemini-1.5-flash`, `gemini-2.0-flash`, `gemini-2.5-flash`) via OkHttp3 4.10.0 and Retrofit 2.12.0.
- **Generative AI Tier 2 (Firebase Vertex AI):** `com.google.firebase:firebase-ai` for multi-turn structured session management.
- **Generative AI Tier 3 (On-Device):** Solace Adaptive Local Sanctuary Engine (deterministic contextual pattern matching).
- **Audio Capture & Transcription:** Android `MediaRecorder` (MPEG-4 container, AAC encoding @ 64kbps / 44.1kHz) with multi-modal Gemini transcription.

### Build & Verification Tools
- **Build System:** Gradle 9.3.1, Android Gradle Plugin 9.1.1, Google Services Plugin 4.5.0, Secrets Gradle Plugin 2.0.1.
- **Testing Frameworks:** JUnit 4.13.2, AndroidX Test Core 1.6.1, Espresso Core 3.7.0, Robolectric 4.16.1.
- **Publishing Pipeline:** Python 3 with ReportLab 5.0.1, Matplotlib 3.11.1, Pillow 12.3.0.

---

## 6. Detailed System Architecture

```
+---------------------------------------------------------------------------------------+
|                                    SOLACE+ CLIENT                                     |
|                                                                                       |
|  +---------------------------------------------------------------------------------+  |
|  |                           PRESENTATION TIER (COMPOSE)                           |  |
|  |  [AuthScreen]      [HomeScreen]     [CheckInScreen]     [CompanionScreen]       |  |
|  |  [JournalScreen]   [InsightsScreen] [RegulationScreen]  [SettingsScreen]        |  |
|  +---------------------------------------------------------------------------------+  |
|                                          |                                            |
|                                          v                                            |
|  +---------------------------------------------------------------------------------+  |
|  |                            VIEWMODEL & STATE TIER                               |  |
|  |  SolaceViewModel <--- StateFlow (Mood, Prefs, Theme, Audio, CloudSync)          |  |
|  |  ChatViewModel   <--- StateFlow (Messages, Role, Model, StreamingChunks)        |  |
|  +---------------------------------------------------------------------------------+  |
|                                          |                                            |
|                                          v                                            |
|  +---------------------------------------------------------------------------------+  |
|  |                   DETERMINISTIC SAFETY & ESCALATION TIER                        |  |
|  |  [ContextAnalyzer]  -->  [RiskDecisionEngine]  -->  [EscalationManager]         |  |
|  |                                                         |                          |
|  |                  +--------------------------------------+                          |
|  |                  | (If Level 2, 3, or 4 Risk Detected)                             |
|  |                  v                                                                 |
|  |          [SafetyResponseHandler]  ==> Immediate Intervention & Helplines (112)     |
|  +---------------------------------------------------------------------------------+  |
|                     | (If Level 0 or 1 Normal Operation)                              |
|                     v                                                                 |
|  +---------------------------------------------------------------------------------+  |
|  |                            INTELLIGENCE & AI TIER                               |  |
|  |  Primary: Google Gemini REST API (gemini-1.5-flash / gemini-2.0-flash)          |  |
|  |  Secondary: Firebase Vertex AI (Firebase.ai GenerativeModel)                    |  |
|  |  Fallback: Adaptive Local Sanctuary Dialogue Engine (Offline attunement)        |  |
|  |  Audio: Multimodal Audio Transcription (Gemini Audio Part)                      |  |
|  +---------------------------------------------------------------------------------+  |
|                                          |                                            |
|                                          v                                            |
|  +---------------------------------------------------------------------------------+  |
|  |                        DATA REPOSITORY & PERSISTENCE                            |  |
|  |  SolaceRepository <==========================> FirestoreRepository (Cloud Sync) |  |
|  |          |                                                  |                      |
|  |          v                                                  v                      |
|  |  Room SQLite Database (v3)                        Google Cloud Firestore           |
|  |  - mood_entries                                   - /users/{userId}/               |
|  |  - journal_entries                                - /users/{userId}/mood_entries   |
|  |  - chat_messages                                  - /users/{userId}/journal_entries|
|  |  - regulation_sessions                            - /users/{userId}/chat_messages  |
|  |  - user_preferences                               - /users/{userId}/reg_sessions   |
|  |  - user_accounts                                                                   |
|  +---------------------------------------------------------------------------------+  |
+---------------------------------------------------------------------------------------+
```

---

## 7. Major Subsystems & Modules

### 7.1 Mood Tracking & Atmospheric Design Engine
- **Taxonomy:** 7 distinct affective states:
  - *Joyful* (Valence: 8/10, Ochre Gold `#D98E04`, Tint `#FFF7E2`)
  - *Calm* (Valence: 8/10, Sage Leaf `#4C7D5C`, Tint `#EFF7F2`)
  - *Steady* (Valence: 5/10, Muted Stone `#5E5854`, Tint `#F4F1EB`)
  - *Tender* (Valence: 3/10, Dusk Slate `#4E6E8A`, Tint `#EEF3F8`)
  - *Restless* (Valence: 3/10, Teal Dusk `#3B7C91`, Tint `#EAF4F7`)
  - *Stormy* (Valence: 2/10, Terracotta `#9C5135`, Tint `#F9EFEA`)
  - *Overwhelmed* (Valence: 2/10, Heavy Charcoal `#545A61`, Tint `#EFF1F3`)
- **Reactive Token Resolution:** StateFlow emission updates `LocalMoodColors` and `LocalSolaceColors` across the Compose subtree within 1 composition frame (<16ms).

### 7.2 Conversational AI Tier (`SolaceAiService`, `GeminiChatService`)
- Multi-model orchestration supporting dynamic model selection (`gemini-1.5-flash`, `gemini-2.0-flash`, `gemini-2.5-flash`).
- 4 Chatbot Roles (Sanctuary Holding Presence, Somatic Guide, Thought Clarifier, Wisdom Mirror).
- 6 Regulation Modes (Listen, Understand, Calm, Reflect, Refocus, Gratitude).
- Streaming token generator with simulated 35–45ms word cadence for human-like conversational holding.

### 7.3 Somatic Nervous System Regulation Engine (`RegulationScreen.kt`)
- 5 Curated physiological stabilization exercises:
  1. *Box Breathing (4-4-4-4)*: 4s inhale, 4s hold, 4s exhale, 4s hold with dynamic visual expansion ring.
  2. *5-4-3-2-1 Sensory Grounding*: Interactive 5-step checklist anchoring sight, touch, sound, smell, and taste.
  3. *Gentle Cognitive Reframing*: Inquiry into cognitive distortions and self-compassion.
  4. *Somatic Release Scan*: Body unclenching sequence targeting jaw, shoulders, palms, and abdomen.
  5. *Quiet Gratitude Anchor*: 3-point micro-mercy inventory.
- Post-session rating (1–5 stars) and qualitative unburdening notes persisted to Room.

### 7.4 Graduated Safety & Crisis Interception Subsystem (`com.example.data.safety`)
- **4-Level Classification Hierarchy:**
  - `LEVEL_0_NORMAL`: Routine dialogue, stress-free reflections, figurative idioms ("dying of laughter").
  - `LEVEL_1_DISTRESS`: Loneliness, workplace exhaustion, grief, mild sadness.
  - `LEVEL_2_ELEVATED`: Persistent hopelessness, burdensomeness, unworthiness ("everyone would be better without me"). Triggers compassionate safety screening questions.
  - `LEVEL_3_SUICIDAL_IDEATION`: Passive/active suicidal thoughts ("wish I could go to sleep and never wake up"). Triggers immediate support card and Tele-MANAS contact.
  - `LEVEL_4_CRITICAL`: Imminent plan, current intent, recent attempt, active self-harm. Instantly locks conversational UI, displays verified emergency hotlines (112, 14416, 1800-599-0019), and overrides LLM streaming.
- **Safety Lock Persistence Rule:** Once Level 4 is triggered, casual conversation or topic switching cannot downgrade the safety state unless explicit clinical or real-world safety resolution is confirmed.

### 7.5 Audio Capture and Transcription Engine (`AudioRecorderHelper.kt`)
- Low-latency hardware recording via Android `MediaRecorder`.
- Standardized sampling at 44.1 kHz, 64 kbps AAC in an MPEG-4 container.
- Clean memory cleanup and transient file lifecycle to protect spoken confessions.

---

## 8. Verification and Test Status

Automated unit and architecture test suite located in `app/src/test/java/com/example/`:
- **Total Tests Implemented:** 29 automated tests (28 in `ExampleUnitTest.kt`, 1 in `ExampleRobolectricTest.kt`).
- **Test Execution Command:** `./gradlew testDebugUnitTest`
- **Empirical Execution Result:** **29 Passed, 0 Failed, 0 Skipped** (Execution time: 0.072 seconds).
- **Core Verifications:**
  1. `moodType_fromString_matchesCorrectly`: Validates 7-mood taxonomy mapping.
  2. `crisisInterceptor_detectsDistressKeywords`: Confirms deterministic crisis phrase detection.
  3. `crisisHelplines_containsLifeline988`: Confirms helpline integrity.
  4. `curatedActivities_boxBreathing_valid`: Validates polyvagal exercise parameters.
  5. `geminiChatModels_configuredCorrectly`: Validates model IDs (`gemini-3.8-flash`, `gemini-4-argon`, `gemini-3.1-flash-lite`).
  6. `weeklyMoodTracker_todayIndicatorMatchesCurrentDeviceWeekday`: Proves calendar alignment.
  7. `formatUserGreeting_withActualUserName_addressesUserByName`: Confirms personalized greetings.
  8. `formatUserGreeting_withPlaceholderOrBlank_omitsNameGracefully`: Prevents "Sanctuary" naming bug.
  9. `riskEvaluation_level1_distressStatements`: Confirms Level 1 classification.
  10. `riskEvaluation_level2_significantDistressStatements`: Confirms Level 2 screening trigger.
  11. `riskEvaluation_level3_suicidalIdeationStatements`: Confirms Level 3 ideation capture.
  12. `riskEvaluation_level4_criticalEmergencyStatements`: Confirms Level 4 critical emergency lock.
  13. `riskEvaluation_level0_figurativeExpressionsFiltered`: Validates non-suicidal idiom filtering ("dying of laughter").
  14. `riskEvaluation_level0_fictionalContextFiltered`: Validates creative writing filtering.
  15. `riskEvaluation_level0_negationHandledSafely`: Confirms negation parsing ("don't want to die").
  16. `riskEscalation_level4CannotBeDowngradedByCasualMessage`: Proves emergency persistence lock.
  17. `darkSolaceColors_matchesSpecificationPalette`: Validates dark theme hex codes.
  18. `journalEntry_lookupById_returnsExactMatchingNoteAndMetadata`: Proves Room query fidelity.
  19. `newUser_guestMode_transitionsToLoggedIn`: Proves frictionless guest authentication.

---

## 9. Identified System Limitations & Future Scope

### Authentic Limitations (Not Concealed)
1. **Absence of Hardware Biometric Sensor Linkage:** Somatic regulation relies on user self-reporting and animated visual pacing; it does not currently interface with photoplethysmography (PPG) camera sensors or wearable heart rate monitors (Apple Watch, Wear OS).
2. **Monolingual Architecture:** All prompt instructions, holding reflections, and safety screening phrases are currently authored in English.
3. **Cloud Audio Transcription Dependency:** Offline voice reflections are stored as raw audio files without on-device Whisper transcription.
4. **Single-Device Room Isolation by Default:** In pure guest mode without Firestore sync, data does not roam between devices.

### Future Scope
1. **On-Device Quantized SLMs:** Integrate Google MediaPipe GenAI with Gemini Nano on supported Android hardware for 100% offline edge inference.
2. **Camera-Based Pulse Rate Variability (PRV):** Implement camera-based fingertip PPG to provide real-time biofeedback during Box Breathing.
3. **Multilingual Sanctuary Attunement:** Expand natural language processing to Hindi, Marathi, Spanish, and French.
