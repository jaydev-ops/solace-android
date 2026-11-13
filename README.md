# Solace+ 🕊️
> **A gentle emotional sanctuary for every feeling.**  
> Native Android application built with Jetpack Compose, Google Gemini AI, polyvagal somatic regulation, and private local-first storage.

---

## 🌟 Overview

**Solace+** is a personal mental wellness and nervous system regulation companion designed around warmth, non-judgmental holding space, and emotional reassurance. The app adapts its visual presence, dialogue cadence, and regulation tools to the user's emotional state without streaks, scores, or gamification.

---

## ✨ Features

### 🧠 1. Dynamic Solace AI Companion
- **Direct Google Gemini Engine:** Calls Google Gemini REST endpoints (`gemini-1.5-flash`, `gemini-2.0-flash`, `gemini-2.5-flash`) for multi-turn emotional conversations.
- **Custom API Key Support:** Users can input their own Gemini API key in Settings for immediate access without Firebase backend setup.
- **Offline Adaptive Sanctuary Engine:** When offline or without an API key, dynamically constructs empathetic, non-repeating holding reflections tailored to 7 emotional states (*Anxiety, Overwhelm, Fatigue, Grief, Anger, Joy, Self-Criticism*) and 6 regulation modes (*Listen, Calm, Understand, Reflect, Refocus, Gratitude*).
- **Personalized Sanctuary Greetings:** Converses with the user using their chosen Sanctuary Name, greeting them by time of day (*Good morning*, *Good afternoon*, *Good evening*).

### 🔐 2. Privacy & Password Authentication
- **Local Secure Account Store:** Email and password authentication backed by salted SHA-256 encryption in Room SQLite database.
- **Guest Mode:** Zero-friction access for completely offline, anonymous emotional sanctuary use.
- **Memory Permissions (Ask to Save):** Explicit consent prompt allowing users to choose between:
  - **Allow Memory Saving:** Persists check-ins and reflections locally on device.
  - **Keep Ephemeral:** Session-only mode; messages vanish when closed and are never written to disk.

### 🎨 3. Apple HIG-Inspired Mobile Design
- **Optical Typography Scale:** Calibrated font sizing with proportional line-heights and negative optical tracking for crisp legibility without mobile clutter.
- **Design Elements:**
  - Standardized corner radii hierarchy (`16dp`, `20dp`, `24dp`, capsule `CircleShape`).
  - iOS-style segmented controls for authentication and timeframe filters.
  - Frosted translucent bottom navigation bar with a `0.5dp` hairline divider.
  - Dual-coded status badges (pulsing indicator dots + plain-language labels).
  - 52–56dp thumb-first touch targets for primary action buttons.

### 🌿 4. Somatic Nervous System Regulation
- **Curated Polyvagal Exercises:**
  - Animated Box Breathing (4-4-4-4) with expanding visual aura.
  - 5-4-3-2-1 Sensory Grounding interactive checklist.
  - Somatic Unclenching and Tension Release.
  - Physiological Sigh (4s Inhale, 2s Top-off, 8s Exhale).
  - Gentle Self-Compassion Holding.
- **Completion Tracking:** Gentle star ratings and optional notes to notice which activities soothe your nervous system best.

### 📝 5. Emotional Check-ins & Private Journal
- **Mood Taxonomy:** 7 core emotional spaces with contextual situational tags that dynamically shift based on what is coloring your space.
- **Time-Based Home Greetings:** Dynamically adjusts to device clock (*Good morning, Good afternoon, Good evening* followed by your Sanctuary Name).
- **Private Journal:** Reflection prompts, keyword search, mood filter chips, and word counter.

---

## 🛠️ Tech Stack

- **Platform:** Android (Min SDK 26, Target SDK 36)
- **Language:** Kotlin 2.0+
- **UI Framework:** Jetpack Compose & Material Design 3
- **Local Database:** Room Database v2 (SQLite) with Coroutines Flow
- **AI Integration:** Google Gemini REST API + Local Adaptive Dialogue Engine
- **Build System:** Gradle 9.3.1 with Android Gradle Plugin 9.1.1

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug / Meerkat or later
- JDK 17 or later
- Android SDK 36

### Build & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/jaydev-ops/solace-android.git
   cd solace-android
   ```
2. Build the Debug APK:
   ```bash
   ./gradlew assembleDebug
   ```
3. Run tests:
   ```bash
   ./gradlew testDebugUnitTest
   ```
4. Output APK is generated at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 🛡️ License & Privacy

All personal thoughts, emotional check-ins, journal reflections, and audio recordings remain strictly on your local device. Solace+ requires zero mandatory cloud connectivity to deliver complete emotional sanctuary support.
