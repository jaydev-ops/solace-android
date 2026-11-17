# Solace+ Web Sanctuary 🕊️

> **A gentle emotional sanctuary for every feeling.**  
> Web application built with React 19, TypeScript, Vite, Google Gemini AI REST integration, Somatic Polyvagal Nervous System Regulation, and Private Local-First Storage.

---

## 🌟 Overview

**Solace+ Web** is the web edition of the Solace+ emotional companion. Designed around warmth, unhurried presence, non-judgmental holding space, and emotional reassurance, it mirrors the Android application with Apple HIG-inspired typography, warm sanctuary light and obsidian dark themes, and strict adherence to modern web standards.

---

## ✨ Features & Architecture

### 🧠 1. Solace AI Sanctuary Companion
- **Direct Google Gemini Engine:** Calls Google Gemini REST endpoints (`gemini-2.5-flash`, `gemini-2.0-flash`, `gemini-1.5-flash`) for multi-turn empathetic dialogue.
- **Custom API Key Support:** Configure your Gemini API key in Settings for immediate access without backend infrastructure.
- **Offline Adaptive Sanctuary Engine:** When offline or without an API key, dynamically constructs empathetic, non-repeating holding reflections tailored across **7 emotional states** (*Joyful, Calm, Steady, Tender, Restless, Stormy, Overwhelmed*) and **6 regulation modes** (*Listen, Understand, Calm, Reflect, Refocus, Gratitude*).
- **Personalized Sanctuary Greetings:** Dynamically greets you by time of day (*Good morning*, *Good afternoon*, *Good evening*) with your chosen Sanctuary Name.

### 🛡️ 2. Multi-Tier Clinical Crisis & Safety Interception
- **Keyword Detection Engine:** Continuous scanning for suicidal ideation, self-harm, and severe distress signals.
- **Deterministic Question Progression State Machine:** Empathetic acknowledgement before sequential screening questions (*Current Suicidal Thoughts -> Immediate Intent -> Plan/Timeframe -> Means -> Human Support*).
- **One-Click Verified Helplines:**
  - **India:** Emergency **112**, Tele-MANAS **14416** (24/7 free toll-free), KIRAN **1800-599-0019**.
  - **US & Global:** Suicide & Crisis Lifeline **988**, Crisis Text Line **741741**.

### 🌿 3. Somatic Nervous System Regulation
- **Interactive Box Breathing (4-4-4-4):** Animated breathing aura, cycle counter, and phase guidance.
- **Physiological Sigh (4-2-8):** Fast biological reset for autonomic down-regulation (4s inhale -> 2s top-up sip -> 8s slow sigh exhale).
- **5-4-3-2-1 Sensory Grounding:** Interactive sensory checklist anchoring your presence into the immediate moment.
- **Somatic Release Scan:** Step-by-step physical tension release for jaw, shoulders, palms, and belly.
- **Gentle Cognitive Reframing:** Examining rigid thoughts from a compassionate mentor's perspective.
- **Quiet Gratitude Anchor:** Grounding small, everyday mercies.
- **Acoustic Web Audio Chimes:** Synthesizes calming 432Hz sine harmonics during breath transitions and session completions.
- **Session Ratings:** 1–5 gentle stars with personal notes saved to your local history.

### 📝 4. Emotional Check-ins & Private Journal
- **7 Core Mood Spaces:** *Joyful (☀️), Calm (🌿), Steady (⚖️), Tender (💧), Restless (🌊), Stormy (🔥), Overwhelmed (☁️)* with dynamic contextual tags.
- **Intensity & Valence Slider (1–10):** From subtle flutters to deep surges.
- **Mood-Attuned Prompts:** Dynamically shifts questions based on valence (*"What is making you happy?"* vs. *"What is weighing on you?"*).
- **Private Journal:** Search entries, filter by mood tags, live word counter, and prompt inspiration cards.

### 🔐 5. Data Sovereignty & Memory Controls
- **Private Local Vault:** Check-ins, journal reflections, and settings are preserved strictly inside your browser's local storage. Zero trackers, zero ads, zero data mining.
- **Keep Ephemeral Mode:** Session-only mode where reflections exist purely in memory during the active tab and vanish permanently upon refresh.
- **One-Click JSON Data Export:** Full local backup download anytime.

---

## 🎨 Design System & Modern Web Standards

- **Color Tokens:**
  - *Light (Warm Ivory Sanctuary):* `#F9F6EE` background, `#FFFFFCF5` cards, `#F5D982` buttery accent, `#30312D` soft charcoal text.
  - *Dark (Warm Obsidian Sanctuary):* `#171714` background, `#292720` cards, `#D9B65D` dark accent, `#F2EDE2` primary text.
  - *7 Mood Palettes:* Unique primary, container, border, and ambient glow accents for each emotional space.
- **Modern Web Guidance Compliance:**
  - `<meta name="color-scheme" content="light dark" />` in HTML head.
  - Native accessible `<dialog>` modals with `dialog:modal { overscroll-behavior: contain; }` and `dialog::backdrop { overflow: hidden; overscroll-behavior: contain; }`.
  - Accessible focus outlines and WCAG 2.1 AA compliant contrast ratios.
  - Mobile touch targets (50–56px) and frosted bottom navigation bar.

---

## 🚀 Getting Started

### Prerequisites
- Node.js 18+ (tested on Node v26)
- npm or pnpm

### Development
1. Navigate to the `web` directory:
   ```bash
   cd web
   ```
2. Start the Vite development server:
   ```bash
   npm run dev
   ```
3. Open your browser at:
   ```
   http://127.0.0.1:5173
   ```

### Production Build
```bash
npm run build
```
The optimized production bundle is generated in `web/dist/`.
To preview the production build locally:
```bash
npm run preview
```
