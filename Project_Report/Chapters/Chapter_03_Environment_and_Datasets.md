# Chapter 3

# Project Environment and Datasets

The implementation and verification of **Solace+** require a robust, modern native mobile engineering toolchain. This chapter documents the hardware and software development environments, target runtime specifications, external service integrations, and the foundational clinical data resources and taxonomies utilized throughout the platform.

---

## 3.1 Software Requirements & Development Toolchain

The development of Solace+ utilizes the latest Android native engineering ecosystem, anchored by Kotlin 2.2 and Jetpack Compose. All tools were selected based on compile-time safety, memory efficiency, asynchronous performance, and open-source availability.

### 3.1.1 Core Development Environment
- **Operating System:** macOS Sequoia (Darwin ARM64 architecture) / Linux / Windows 11.
- **Integrated Development Environment (IDE):** Android Studio Ladybug (2024.2.1) / Meerkat (2024.3.1) with Android Gradle Plugin support.
- **Java Development Kit (JDK):** OpenJDK 24 / JDK 17 (LTS) with JVM Target 11 bytecode compatibility.
- **Build System:** Gradle 9.3.1 with Android Gradle Plugin (AGP) 9.1.1 and Kotlin Gradle Plugin 2.2.10.
- **Symbol Processing Engine:** Google KSP (`com.google.devtools.ksp:2.3.5`) for compile-time Room DAO generation and Moshi serialization code generation.

### 3.1.2 Target Platform & Android Runtime Specifications
- **Compile SDK:** Android API Level 36 (Android 16 Vanilla Ice Cream / Preview, Minor Level 1).
- **Target SDK:** Android API Level 36 (enforcing edge-to-edge window insets, predictive back gestures, and granular media permissions).
- **Minimum SDK:** Android API Level 24 (Android 7.0 Nougat), providing backward compatibility across over 94% of active worldwide Android devices.
- **Application Namespace:** `com.example` (Application ID: `com.aistudio.solace.wrmsnc`).

### 3.1.3 Software Libraries and Frameworks
The dependency hierarchy is managed via Gradle Version Catalogs (`gradle/libs.versions.toml`):

| Library / Framework | Version | Purpose in Solace+ |
| :--- | :--- | :--- |
| `androidx.compose.bom` | 2024.09.00 | Bill of Materials guaranteeing version consistency across all Compose UI modules. |
| `androidx.compose.material3` | 1.3.1 | Material Design 3 components, dynamic color theming, typography scales, surface containers. |
| `androidx.activity.compose` | 1.10.1 | Edge-to-edge window handling and Activity-Compose integration. |
| `androidx.navigation.compose` | 2.8.9 | Single-Activity declarative navigation across 8 top-level application destinations. |
| `androidx.room:room-runtime` | 2.7.0 | Local-first SQLite Object-Relational Mapping (ORM) database engine. |
| `androidx.room:room-ktx` | 2.7.0 | Kotlin Coroutines extensions and reactive `Flow` query emissions for Room. |
| `com.google.firebase:firebase-bom`| 34.17.0 | Managed cloud dependency platform for authentication, database, and vertex AI. |
| `com.google.firebase:firebase-firestore` | Managed | Multi-device cloud document database with granular server-side security rules. |
| `com.google.firebase:firebase-auth` | Managed | Salted Google Identity and email authentication. |
| `androidx.credentials` | 1.5.0 | Modern Android Credential Manager API for seamless Google Sign-In. |
| `com.squareup.okhttp3:okhttp` | 4.10.0 | High-performance HTTP client for direct Google Gemini REST API streaming calls. |
| `com.squareup.retrofit2:retrofit` | 2.12.0 | Type-safe REST client for backend endpoints. |
| `io.coil-kt:coil-compose` | 2.7.0 | Asynchronous image loading and caching pipeline. |
| `junit:junit` | 4.13.2 | Core unit test runner for business logic and algorithm validation. |
| `org.robolectric:robolectric` | 4.16.1 | Headless Android framework simulator for fast, JVM-based component testing. |

*Table 3.1.1: Software Development Requirements & Toolchain Specifications*

---

## 3.2 Hardware Environment Specifications

The development, automated compilation, and local testing of Solace+ were conducted on high-performance UNIX workstations and real Android hardware to verify sub-16ms UI frame rates and battery efficiency.

| Hardware Component | Development Workstation Specification | Minimum Target Mobile Device Specification |
| :--- | :--- | :--- |
| **Processor (CPU)** | Apple Silicon M-Series (8-Core CPU, 4 Performance + 4 Efficiency cores) | 64-bit Quad-Core ARM Cortex-A53 @ 1.8 GHz |
| **Graphics (GPU)** | 8-Core integrated Apple Metal GPU | Adreno 506 / Mali-G52 or higher with OpenGL ES 3.1 / Vulkan |
| **System Memory (RAM)**| 8.0 GB Unified LPDDR5 Memory | 3.0 GB RAM (4.0 GB recommended for smooth Compose rendering) |
| **Storage Capacity** | 256 GB High-Speed NVMe SSD | 64 MB available on-device flash storage for APK + Room SQLite |
| **Display Matrix** | 13.6-inch Liquid Retina Display ($2560 \times 1664$) | 5.5-inch Multi-touch IPS LCD / AMOLED ($1080 \times 2400$, 60Hz) |
| **Audio Hardware** | Built-in studio quality microphone array | Hardware microphone supporting 44.1 kHz, 16-bit PCM capture |
| **Network Interface** | Wi-Fi 6 (802.11ax) + Bluetooth 5.3 | 4G LTE / Wi-Fi (optional: app remains 100% functional offline) |

*Table 3.2.1: Hardware Environment & Minimum Target Specifications*

---

## 3.3 Clinical Data Resources, Taxonomies & Seed Datasets

Rather than relying on uncurated internet scraping, Solace+ incorporates verified, structured data resources across emotional modeling, somatic regulation, crisis intervention, and baseline database seeding.

### 3.3.1 The Seven-State Affective Taxonomy Dataset
Derived from Russell’s Circumplex Model of Affect [11], Solace+ models emotional experience through a structured taxonomy defining valence, intensity, palette codes, and situational contexts:

```
[Affective State] --> [Valence (1..10)] --> [Default Intensity (1..10)] --> [Hex Codes]
- Joyful              8/10                  6/10                           Accent: #D98E04, Tint: #FFF7E2
- Calm                8/10                  4/10                           Accent: #4C7D5C, Tint: #EFF7F2
- Steady              5/10                  3/10                           Accent: #5E5854, Tint: #F4F1EB
- Tender              3/10                  5/10                           Accent: #4E6E8A, Tint: #EEF3F8
- Restless            3/10                  7/10                           Accent: #3B7C91, Tint: #EAF4F7
- Stormy              2/10                  8/10                           Accent: #9C5135, Tint: #F9EFEA
- Overwhelmed         2/10                  8/10                           Accent: #545A61, Tint: #EFF1F3
```

Contextual situational tags are organized into five life domains:
1. *Productivity & Focus:* Work, Deadlines, Studies, Creative Flow.
2. *Social & Interpersonal:* Relationships, Family, Conflict, Solitude.
3. *Physical & Somatic:* Sleep & Rest, Physical Tension, Fatigue, Movement.
4. *Environment & Surroundings:* Morning Quiet, Weather & Light, Commute.
5. *Internal Cognition:* Self-Criticism, Gratitude, Overthinking, Grief.

### 3.3.2 Curated Polyvagal Regulation Protocols Dataset
Grounding interventions in autonomic neuroscience [3], [4], the platform embeds five evidence-based physiological protocols:
- `box_breathing`: 120-second autonomic stabilizer based on 4-second equalized phases (Inhale, Hold, Exhale, Hold) to optimize heart rate variability.
- `grounding_54321`: 180-second sensory orientation checklist (5 sights, 4 physical touches, 3 environmental sounds, 2 scents, 1 taste) to break dissociative freeze.
- `cognitive_reframe`: 150-second gentle CBT inquiry evaluating cognitive distortions ("Is this 100% true, or is my nervous system tired?").
- `body_scan`: 180-second somatic tension release sequence targeting the jaw, temples, shoulders, palms, and abdomen.
- `gratitude_anchor`: 120-second quiet inventory of three unpretentious daily mercies.

### 3.3.3 Emergency Helplines & Crisis Triage Dataset
For deterministic safety interception, Solace+ embeds verified, zero-latency emergency resource directories:
- **India (Primary Deployment Context):**
  - **112:** National Unified Emergency Services (Police, Ambulance, Fire).
  - **14416 (Tele-MANAS):** Ministry of Health and Family Welfare 24/7 free toll-free mental health helpline [15].
  - **1800-599-0019 (KIRAN):** Ministry of Social Justice 24/7 mental rehabilitation helpline.
- **Global & International Fallback:**
  - **988:** Suicide & Crisis Lifeline (United States & Canada).
  - **741741:** Crisis Text Line (Text HOME for free 24/7 crisis counseling).

### 3.3.4 Realistic Baseline Seed Dataset (`SolaceDatabase.kt`)
To guarantee an immediate, authentic sanctuary experience upon first launch without cold-start emptiness, Solace+'s Room database automatically seeds historical baseline records:
- **6 Historical Mood Check-Ins:** Pre-populated entries across the preceding 6 days (Calm, Joyful, Tender, Restless, Calm, Joyful) demonstrating trends, valence ratings, and notes in the analytics dashboard.
- **2 Reflective Journal Entries:** Real-world reflections ("Morning Serenity & The Breath of Dawn", "Unpacking the Weight of Deadlines") providing immediate context for search and filter testing.
- **Initial Welcoming Companion Message:** Contextualized time-of-day greeting acknowledging the user's latest check-in.
- **Baseline Regulation Session:** Initial completed Box Breathing session with a 5-star rating.

---

## 3.4 Ethical Compliance & Privacy Guarantees

Because Solace+ handles intimate psychological disclosures, the system was designed in strict compliance with medical data ethics:
1. **Zero Mandatory Personal Identifiable Information (PII):** Guest mode enables full use without email, phone number, or name.
2. **Local Salted SHA-256 Hashing:** User passwords are never stored in plaintext; they are hashed locally with unique salt bytes before insertion into Room SQLite.
3. **Explicit Memory Opt-In:** Users maintain complete control over whether conversational check-ins are saved to disk or kept strictly ephemeral in RAM.
4. **Zero Third-Party Advertising Trackers:** The APK contains no advertising identifiers, tracking pixels, or commercial telemetry SDKs, directly addressing the vulnerabilities exposed by Huckvale et al. [14].
