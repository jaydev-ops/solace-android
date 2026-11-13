# Technical Architecture Document: solace+

**Version:** 1.0.0  
**Status:** Approved for Implementation  
**Classification:** Core System Architecture  
**Target Platform:** Web (Desktop, Tablet, Mobile Responsive PWA-Ready)  
**Author:** Principal Software Architect & Technical Lead  
**Date:** November 2026  

---

## Executive Summary

**solace+** is an AI-powered personalised mood tracking and emotional wellness platform conceived as a private digital sanctuary. Unlike diagnostic clinical psychiatric tools, solace+ focuses on emotional self-awareness, mindful regulation, reflective journaling, and empathetic companionship. 

Its signature architectural innovation is a **Mood-Responsive Design System**: the visual atmosphere, ambient lighting, motion dynamics, and micro-interactions of the client interface morph dynamically in real time to match, validate, or gently stabilise the user's emotional state. Underpinning this ambient user experience is an edge-ready, stateless backend running on Hono and Node.js, backed by Neon Serverless PostgreSQL with Drizzle ORM, and an OpenAI-compatible model orchestration layer capable of low-latency token streaming and non-clinical emotional support.

---

## 1. Architectural Overview

### 1.1 Product Overview
solace+ bridges the chasm between utilitarian mood trackers (which rely on sterile graphs and transactional data entry) and generative AI chat tools (which lack persistent emotional context and spatial atmosphere). The product is designed around the psychological premise that digital environments influence emotional regulation. By dynamically adapting its aesthetic canvas to the user's logged affective state, solace+ provides visual confirmation of the user's feelings—creating an atmosphere of presence, psychological safety, and warmth.

The platform classifies emotional states into a foundational seven-mood taxonomy: **Happy, Calm, Neutral, Sad, Anxious, Angry, and Overwhelmed**. Each state anchors a discrete visual palette, ambient colour gradient, kinetic motion cadence, and tone-tailored conversational stance. Users record check-ins with quantitative valence, intensity scores, situational triggers, and contextual tags. Over time, these entries feed a deterministic analytics engine and an empathetic companion ("solace AI").

Crucially, solace+ is engineered as an introspective sanctuary rather than a medical device. It does not provide medical diagnoses, psychiatric evaluations, or clinical treatment plans. Instead, it offers cognitive grounding techniques, somatic breathwork pacing, reflective journaling prompts, and an attentive conversational companion trained to listen, validate, and guide self-reflection.

### 1.2 Architectural Objectives
The engineering architecture of solace+ is governed by the following core objectives:

*   **Dynamic Atmosphere Synchronisation:** Propagate mood changes through the design token hierarchy in sub-100ms, altering CSS custom properties and surface gradients without causing DOM repainting flashes or layout thrashing.
*   **Sub-500ms Streaming Time-to-First-Token (TTFB):** Deliver low-latency conversational responses via Server-Sent Events (SSE) and Vercel AI SDK to preserve the natural cadence of dialogue.
*   **Zero-Knowledge Architectural Privacy & Isolation:** Enforce absolute data isolation at the repository query boundary using strict tenant/user predicates, preventing cross-tenant leakage of intimate journal records or conversational histories.
*   **Lean, Edge-Ready Footprint:** Employ a lightweight, fast-booting HTTP runtime (Hono on Node.js) paired with an efficient ORM (Drizzle) to minimise memory overhead and cold-start latency.
*   **Deterministic State Hydration:** Ensure synchronisation between persistent database state, local caching stores (Zustand), and the ambient DOM theme with resilient offline recovery.
*   **Fail-Safe Non-Clinical Guardrails:** Implement multi-tier keyword filtering and prompt-level guardrails to instantly intercept crisis signals and present emergency resources without model hallucinations.

### 1.3 System Boundaries (MVP Scope)

```
+---------------------------------------------------------------------------------------+
|                                    IN SCOPE (MVP)                                     |
+---------------------------------------------------------------------------------------+
| * Self-hosted JWT authentication (Access + Refresh Token rotation, bcrypt)            |
| * Core 7-mood taxonomy check-in engine with situational tags and intensity sliders    |
| * Reactive CSS variable design system altering surface, border, and ambient tones     |
| * Streaming AI emotional companion ("solace AI") with 6 discrete regulation modes    |
| * Private markdown-capable emotional journal with mood-tag correlation               |
| * Interactive regulation sessions (box breathing, 5-4-3-2-1 grounding, reframing)    |
| * Mood analytics: 30-day trend lines, emotional distribution, and trigger breakdown   |
| * Personalised preferences (theme locking, reduced motion, AI memory opt-in)          |
| * Privacy tooling: Complete data export (JSON) and cascade account erasure            |
+---------------------------------------------------------------------------------------+

+---------------------------------------------------------------------------------------+
|                                  OUT OF SCOPE (MVP)                                   |
+---------------------------------------------------------------------------------------+
| * Third-party OAuth providers (Google, Apple, GitHub)                                 |
| * Clinical diagnostic tools, DSM-5 psychometric scales, or clinician portals         |
| * Real-time peer-to-peer social networking, public feeds, or community features      |
| * Biometric hardware integrations (Apple HealthKit, Google Fit, Whoop, Oura)          |
| * Native mobile compilation (React Native/Swift/Kotlin) - focus on mobile-first PWA   |
| * Real-time bidirectional voice streaming (WebRTC / duplex audio pipelines)           |
+---------------------------------------------------------------------------------------+
```

### 1.4 Major Subsystems
1.  **Frontend Single Page Application (SPA):** React 18, Vite, and Tailwind CSS v4 delivering a 60fps responsive user interface, featuring Framer Motion micro-animations and Zustand state management.
2.  **API Gateway & Application Server:** Node.js runtime executing Hono, exposing type-safe REST endpoints, validating payloads via Zod, and enforcing JWT authentication middleware.
3.  **Mood-Responsive Design Engine:** Dual-layer token resolver operating on the client, synchronising Zustand mood state with `:root` CSS custom properties to dynamically theme the layout.
4.  **AI Orchestration Subsystem:** Context-assembly pipeline combining the user's emotional baseline, recent check-ins, journal summaries, and selected regulation modes into an OpenAI-compatible streaming interface.
5.  **Persistence & Analytics Layer:** Neon Serverless PostgreSQL managed via Drizzle ORM, executing indexed relational queries, time-series aggregations, and cascade deletions.
6.  **Mood Regulation Engine:** State-machine-driven client-side guided exercises (e.g., visual breathwork pacing, sensory grounding, cognitive reflection).

### 1.5 Architectural Principles
1.  **Emotion-First UI Reactivity:** Visual state is an active participant in emotional regulation; changes in user mood must ripple through the theme architecture instantly and smoothly.
2.  **Defensive Privacy by Design:** Intimate psychological data requires rigorous isolation. Every database query must bind explicitly to an authenticated `userId`.
3.  **Stateless API Execution:** Application servers must remain completely stateless. User sessions, token rotations, and conversational checkpoints reside in persistent storage or signed tokens.
4.  **Vendor-Agnostic LLM Abstraction:** The conversational layer must never bind tightly to a single AI vendor. An OpenAI-compatible adapter permits swapping between OpenAI, OpenRouter, local Ollama, or Anthropic proxies via environment configuration.
5.  **Strict End-to-End Type Safety:** Schemas defined in Drizzle must map unambiguously to backend Zod contracts, which in turn generate TypeScript types consumed across the API and frontend clients.
6.  **Progressive Disclosure of Complexity:** The interface must avoid cognitive overload. Dashboards default to clean, minimalist views, expanding into deeper analytical views only on user demand.
7.  **Optimistic UI with Rollback Integrity:** Client-side interactions (e.g., mood logging, journal drafts) update local state optimistically, reconciling or reverting gracefully upon network failure.
8.  **Graceful Non-Clinical Boundary Enforcement:** When an interaction touches clinical or crisis boundaries, the system must deterministically divert from generative AI responses to verified, hardcoded safety messages.

### 1.6 High-Level Architecture Diagram

```mermaid
flowchart TD
    subgraph Client ["Frontend Client (React 18 + Vite + Tailwind CSS v4)"]
        UI["User Interface Components"]
        Zustand["Zustand Stores (Auth, Mood, Theme, AI)"]
        ThemeEngine["Mood-Responsive Theme Provider (:root CSS Tokens)"]
        ApiClient["Fetch API Client (JWT Interceptor & Stream Consumer)"]
        
        UI --> Zustand
        Zustand --> ThemeEngine
        UI --> ApiClient
    end

    subgraph Edge_Gateway ["API Layer (Hono on Node.js / Railway)"]
        Middleware["Middleware (CORS, Rate Limiting, Auth JWT, Zod Validator)"]
        
        subgraph Controllers ["Controllers & Route Handlers"]
            AuthCtrl["Auth Controller"]
            MoodCtrl["Mood & Analytics Controller"]
            JournalCtrl["Journal Controller"]
            AiCtrl["AI Conversation Controller (SSE)"]
            RegCtrl["Regulation Controller"]
        end
        
        Middleware --> Controllers
    end

    subgraph Core_Services ["Application Domain Services"]
        AuthSvc["Authentication Service"]
        MoodSvc["Mood & Theme Resolution Service"]
        AnalyticsSvc["Analytics Aggregation Service"]
        JournalSvc["Journal Service"]
        AiOrchSvc["AI Orchestration & Safety Service"]
        RegSvc["Regulation Service"]
    end

    subgraph Data_Layer ["Persistence & External Services"]
        Drizzle["Drizzle ORM Query Layer"]
        NeonDB[("Neon Serverless PostgreSQL")]
        LLMProvider["OpenAI-Compatible LLM (GPT-4o / OpenRouter / Ollama)"]
        CloudflareR2[("Cloudflare R2 Storage (Backups & Exports)")]
    end

    ApiClient -->|"HTTPS / REST"| Middleware
    ApiClient -->|"Server-Sent Events (SSE)"| AiCtrl

    AuthCtrl --> AuthSvc
    MoodCtrl --> MoodSvc
    MoodCtrl --> AnalyticsSvc
    JournalCtrl --> JournalSvc
    AiCtrl --> AiOrchSvc
    RegCtrl --> RegSvc

    AuthSvc --> Drizzle
    MoodSvc --> Drizzle
    AnalyticsSvc --> Drizzle
    JournalSvc --> Drizzle
    RegSvc --> Drizzle
    AiOrchSvc --> Drizzle
    AiOrchSvc -->|"Streaming Completion API"| LLMProvider
    
    Drizzle -->|"Postgres Wire Protocol"| NeonDB
    JournalSvc -.->|"Export Bundles"| CloudflareR2
```

---

## 2. Technology Stack

### 2.1 Frontend Layer
| Technology | Version / Spec | Purpose in solace+ | Alternatives Considered | Trade-Offs & Architectural Rationale |
| :--- | :--- | :--- | :--- | :--- |
| **React** | 18.3+ | Core declarative UI rendering | Vue 3, Svelte, SolidJS | Massive ecosystem, mature concurrent rendering, rich headless UI libraries, widespread agent familiarity. Slightly heavier bundle than Svelte, but mitigated by Vite tree-shaking. |
| **TypeScript** | 5.5+ | Static typing across components & hooks | Pure JavaScript | Eliminates runtime type errors, guarantees API contract fidelity between backend Zod schemas and UI models. |
| **Vite** | 5.4+ | Development server and bundle builder | Next.js, Webpack, Turbopack | Sub-second HMR, instant cold starts, lean static SPA output ideal for CDN deployment (Netlify) without SSR hosting overhead. |
| **Tailwind CSS** | v4.0 | Utility-first dynamic design styling | Vanilla CSS, Styled Components, Emotion | Tailwind v4 uses a lightning-fast Rust engine (`@tailwindcss/vite`), eliminating PostCSS config. Integrates seamlessly with dynamic CSS custom properties without runtime CSS-in-JS overhead. |
| **Zustand** | 4.5+ | Global state management | Redux Toolkit, MobX, React Context | Minimal boilerplate, zero-provider tree wrapping requirement, straightforward selector subscriptions, external access outside React components (e.g. within API interceptors). |
| **React Router** | v6.26+ | Client-side routing and navigation | TanStack Router | Mature, stable, battle-tested API for declarative nested routing, route guards, and code-split dynamic imports. |
| **Recharts** | 2.12+ | Mood trends and analytics visualisations | Chart.js, Tremor, Visx | Native SVG-based React component tree, highly customizable styling that inherits dynamic CSS variables cleanly. |
| **Framer Motion** | 11.3+ | Fluid atmospheric transitions & breathwork | CSS Animations, GSAP | Declarative springs and exit animations; critical for pacing guided somatic breathwork exercises and smoothing mood theme cross-fades. |
| **React Hook Form** | 7.52+ | Form state management & validation | Formik | Uncontrolled input architecture drastically reduces re-renders during high-frequency typing in journals and mood sliders. |
| **Zod** | 3.23+ | Client-side form & API schema validation | Yup, Valibot, Joi | TypeScript-first schema declaration sharing identical validation rules with the backend API. |

### 2.2 Backend Layer
| Technology | Version / Spec | Purpose in solace+ | Alternatives Considered | Trade-Offs & Architectural Rationale |
| :--- | :--- | :--- | :--- | :--- |
| **Node.js** | 20.x LTS | Server-side JavaScript runtime | Bun, Deno | Standard enterprise baseline. Predictable production stability on Railway/Render, native driver support for PostgreSQL, rich ecosystem. |
| **Hono** | 4.5+ | Lightweight REST & SSE API framework | Express.js, Fastify, NestJS | Ultra-low overhead, sub-millisecond route dispatch, built on Web Standards (`Request`/`Response`), native SSE helpers, seamless edge porting capability. |
| **TypeScript** | 5.5+ | Typed backend business logic | JavaScript | Guarantees complete end-to-end type safety from database schemas to route outputs. |
| **Zod** | 3.23+ | Request validation middleware | Joi, TypeBox | Validates incoming payloads at controller boundaries; directly derives output types for service calls. |
| **bcryptjs** | 2.4+ | Password hashing with salt | Argon2, PBKDF2 | Proven industry standard for non-reversible password derivation. Cost factor 12 provides robust protection with minimal CPU latency. |
| **jose / jsonwebtoken**| 9.0+ / 5.x | Cryptographic signing of JWT tokens | Session cookies with Redis | Stateless access token validation without database round-trips for every API hit; pairs with rotated refresh tokens in Postgres. |

### 2.3 Database Layer
| Technology | Version / Spec | Purpose in solace+ | Alternatives Considered | Trade-Offs & Architectural Rationale |
| :--- | :--- | :--- | :--- | :--- |
| **PostgreSQL** | 16+ | Relational data persistence | MongoDB, MySQL, SQLite | ACID compliance, robust indexing, native JSONB support, powerful analytical time-series window functions. |
| **Neon PostgreSQL** | Serverless | Managed cloud database instance | Supabase, AWS RDS | Serverless architecture with scale-to-zero capabilities (cost-effective MVP free tier), instant branching for migrations, connection pooling via PgBouncer built-in. |
| **Drizzle ORM** | 0.33+ | TypeScript SQL mapping & query builder | Prisma, TypeORM, Kysely | Zero-overhead, compiles directly to plain SQL strings, no heavy background binary engine, complete type safety matching PostgreSQL column definitions directly. |
| **Drizzle Kit** | 0.24+ | Schema migration and introspection CLI | Flyway, Prisma Migrate | Generates declarative SQL migration files directly from TypeScript schema declarations; zero drift. |

### 2.4 AI & Orchestration Layer
| Technology | Version / Spec | Purpose in solace+ | Alternatives Considered | Trade-Offs & Architectural Rationale |
| :--- | :--- | :--- | :--- | :--- |
| **OpenAI GPT-4o** | Snapshot / API | Default conversational companion | Anthropic Claude 3.5 Sonnet, Llama 3.1 | Exceptional empathetic nuance, low latency, structured JSON output mode, robust instruction-following under layered system constraints. |
| **Vercel AI SDK** | 3.3+ | Streaming orchestration & abstraction | LangChain, raw fetch/SSE | Stream handling via `streamText`, backpressure management, standardized tool calling, and unified protocol across varied LLM providers. |
| **OpenAI-Compatible Abstraction** | Custom Interface | Pluggable provider architecture | Native proprietary SDKs | Allows seamless switching between OpenAI, OpenRouter, self-hosted Ollama, or Groq without altering application service code. |

### 2.5 Infrastructure & Tooling Layer
| Technology | Version / Spec | Purpose in solace+ | Alternatives Considered | Trade-Offs & Architectural Rationale |
| :--- | :--- | :--- | :--- | :--- |
| **Netlify** | Free Tier | Frontend static asset hosting | Vercel, Cloudflare Pages | Global edge CDN, automatic branch previews, atomic deployments, zero-cost static hosting for Vite builds. |
| **Railway / Render** | Free / Hobby | Backend Node.js process hosting | Fly.io, AWS ECS, Heroku | Continuous deployment directly from GitHub, simple environment variable management, persistent logging, and native health check probes. |
| **Cloudflare R2** | S3-Compatible | Object storage for exports & backups | AWS S3, Google Cloud Storage | S3-compatible API, zero egress fees, generous 10 GB free monthly tier. |
| **GitHub Actions** | Hosted | CI/CD automation pipeline | GitLab CI, CircleCI | Integrated directly into the GitHub repository; automates linting, type-checking, automated testing, and deployment triggers. |

### 2.6 Technology Justification Summary
The chosen stack reflects a deliberate balance of **extreme developer velocity, zero-cost MVP operation, low-latency performance, and enterprise-grade type safety**. By pairing Vite and React 18 on the frontend with Hono and Drizzle ORM on the backend, the entire platform runs without bloated runtime frameworks. The database schema in Drizzle acts as the definitive source of truth, propagating types upward to Zod validation layers and downstream React components. The Vercel AI SDK coupled with an OpenAI-compatible abstraction layer prevents vendor lock-in while providing immediate access to leading conversational models with streaming responses.

---

## 3. High-Level System Architecture

### 3.1 Overall System Architecture
The overall topology comprises a client SPA communicating over TLS with a stateless Hono application server. The application server coordinates transactional persistence with Neon PostgreSQL and streams conversational tokens from an OpenAI-compatible LLM provider.

```mermaid
flowchart TD
    User([User / Browser])
    
    subgraph CDN ["Edge Distribution (Netlify)"]
        SPA["solace+ React Static Assets (HTML/JS/CSS)"]
    end

    subgraph Compute ["Compute Environment (Railway / Node.js)"]
        HonoApp["Hono Application Server (Port 3001)"]
        AuthModule["Auth & Session Module"]
        MoodModule["Mood & Analytics Module"]
        AiModule["AI Orchestrator Module"]
        JournalModule["Journal Module"]
        RegModule["Regulation Module"]
    end

    subgraph Database_Cluster ["Managed Data Store (Neon Serverless)"]
        PgPool["Connection Pooler (PgBouncer)"]
        PostgresCore[("PostgreSQL 16 Engine")]
    end

    subgraph AI_Ecosystem ["AI Service Providers"]
        OpenAI["OpenAI API (GPT-4o)"]
        OpenRouter["OpenRouter / Custom Endpoint (Fallback)"]
    end

    subgraph Storage ["Blob Storage (Cloudflare R2)"]
        R2Buckets[("User Data Exports & Backups")]
    end

    User -->|"1. Fetches Web Assets"| SPA
    User -->|"2. REST Calls (JSON / Bearer Token)"| HonoApp
    User -->|"3. Connects to SSE Stream"| HonoApp

    HonoApp --> AuthModule
    HonoApp --> MoodModule
    HonoApp --> AiModule
    HonoApp --> JournalModule
    HonoApp --> RegModule

    AuthModule --> PgPool
    MoodModule --> PgPool
    JournalModule --> PgPool
    RegModule --> PgPool
    AiModule --> PgPool
    
    PgPool --> PostgresCore

    AiModule -->|"4. Streaming Prompt (Tokens)"| OpenAI
    AiModule -.->|"5. Failover Streaming"| OpenRouter
    JournalModule -.->|"6. Asynchronous Export"| R2Buckets
```

### 3.2 Frontend-to-Backend Communication Flow
Client interactions follow two paradigms: traditional request-response REST transactions for structured entity operations, and reactive Server-Sent Events (SSE) for low-latency AI conversations.

```mermaid
sequenceDiagram
    autonumber
    actor User as User / Client UI
    participant Interceptor as Fetch / Axios Client
    participant Hono as Hono API Gateway
    participant AuthMW as Auth Middleware
    participant Service as Domain Service
    participant DB as Neon PostgreSQL

    Note over User, DB: Standard Synchronous Transaction (e.g. Log Mood)
    User->>Interceptor: Submit Mood Check-in Form
    Interceptor->>Interceptor: Inject Bearer Access Token
    Interceptor->>Hono: POST /api/moods (Payload JSON)
    Hono->>AuthMW: Verify Access Token (JWT)
    AuthMW-->>Hono: Token Validated (Extract userId)
    Hono->>Service: moodService.createEntry(userId, data)
    Service->>DB: INSERT INTO mood_entries RETURNING *
    DB-->>Service: Created Row
    Service-->>Hono: Domain Response Object
    Hono-->>Interceptor: 201 Created (JSON Payload)
    Interceptor-->>User: Update Zustand Store & Theme Context

    Note over User, DB: Token Expiry & Silent Refresh Cycle
    User->>Interceptor: GET /api/journal (Access Token Expired)
    Interceptor->>Hono: GET /api/journal
    Hono-->>Interceptor: 401 Unauthorized (Token Expired)
    Interceptor->>Hono: POST /api/auth/refresh (httpOnly Cookie)
    Hono->>DB: Validate Refresh Token & Rotate
    DB-->>Hono: New Token Pair Generated
    Hono-->>Interceptor: 200 OK (New Access Token in JSON)
    Interceptor->>Hono: Replay GET /api/journal (With New Token)
    Hono-->>Interceptor: 200 OK (Journal Data)
    Interceptor-->>User: Render View
```

### 3.3 AI Conversation Flow
The AI conversation pipeline constructs an empathetic prompt by assembling the user's recent emotional state, journal notes, and safety guidelines, streaming the generated response back via Server-Sent Events.

```mermaid
sequenceDiagram
    autonumber
    actor User as User
    participant ChatUI as Solace AI Chat View
    participant Hono as Hono SSE Handler
    participant Orchestrator as AI Orchestration Service
    participant Safety as Guardrail Inspector
    participant DB as Neon Database
    participant LLM as OpenAI GPT-4o

    User->>ChatUI: Send message: "I feel completely overwhelmed today"
    ChatUI->>ChatUI: Optimistically render user message bubble
    ChatUI->>Hono: POST /api/conversations/:id/messages (Stream Request)
    Hono->>Safety: Evaluate input against Crisis Keyword Lexicon
    alt Crisis Signal Detected
        Safety-->>Hono: Crisis Alert Triggered
        Hono-->>ChatUI: Stream Deterministic Crisis Escalation Protocol & Helplines
        ChatUI-->>User: Display Immediate Safety & Support Card
    else Input Cleared Safe
        Safety-->>Hono: Input Approved
        Hono->>Orchestrator: assembleContext(userId, conversationId, message)
        par Context Retrieval
            Orchestrator->>DB: Fetch User Preferences & Active Mood
            Orchestrator->>DB: Fetch 7-Day Mood History & Tag Frequency
            Orchestrator->>DB: Fetch Recent Journal Emotional Summaries
            Orchestrator->>DB: Fetch Last 10 Conversation Turns
        end
        DB-->>Orchestrator: Aggregate Context Payload
        Orchestrator->>Orchestrator: Construct Multi-Layer System Prompt + Guidance
        Orchestrator->>LLM: Dispatch Stream Completion Request (Tokens)
        LLM-->>Hono: Yield Text Chunk (Stream)
        loop Token Streaming
            Hono-->>ChatUI: Event: message (data: {"chunk": "I hear..."})
            ChatUI-->>User: Progressive Character Render (Typewriter Effect)
        end
        LLM-->>Hono: Stream Finalised [DONE]
        Hono->>DB: Asynchronously persist AI Message to ai_messages
        Hono-->>ChatUI: Event: end_of_stream (Persisted message metadata)
    end
```

### 3.4 Mood Check-In Data Flow
When a user logs their mood, the application records the quantitative entry, re-evaluates theme parameters, and dynamically updates the visual interface.

```mermaid
sequenceDiagram
    autonumber
    actor User as User
    participant CheckInUI as Mood Check-In Modal
    participant MoodStore as Zustand useMoodStore
    participant ThemeStore as Zustand useThemeStore
    participant API as Hono /api/moods
    participant DB as Neon PostgreSQL
    participant DOM as Document Root (:root)

    User->>CheckInUI: Selects Mood "Anxious" (Valence: 3, Intensity: 8, Tags: ["Work", "Deadlines"])
    CheckInUI->>API: POST /api/moods
    API->>DB: INSERT into mood_entries & mood_entry_tags
    DB-->>API: Persisted Record (id: uuid, mood: "Anxious", ...)
    API-->>CheckInUI: 201 Created (Mood Entry Object)
    CheckInUI->>MoodStore: setCurrentMood("Anxious", entryData)
    MoodStore->>ThemeStore: updateAtmosphereForMood("Anxious")
    ThemeStore->>ThemeStore: Resolve Anxious Theme Token Palette
    ThemeStore->>DOM: Inject CSS Variables (--background, --surface, --ambient-color)
    DOM-->>User: CSS Transition smoothly interpolates colours over 200ms
    CheckInUI-->>User: Display Affirmation & Suggest Grounding Exercise
```

### 3.5 Analytics Data Flow
The analytics subsystem processes historical check-ins into aggregated metrics, trend lines, and trigger correlations for visual rendering.

```mermaid
flowchart LR
    subgraph Database ["Neon PostgreSQL Store"]
        RawEntries[("mood_entries Table")]
        JunctionTags[("mood_entry_tags Table")]
    end

    subgraph Backend_Aggregation ["Hono Analytics Engine"]
        QueryRunner["Time-Window Bucket Query (Last 30 Days)"]
        GroupingEngine["Mathematical Grouping & Standard Deviation"]
        TriggerMapper["Trigger Frequency & Valence Correlation"]
    end

    subgraph API_Output ["Rest Endpoint"]
        Endpoint["GET /api/moods/analytics/trends"]
    end

    subgraph Client_Visualisation ["Frontend Analytics Dashboard"]
        Store["Zustand useAnalyticsStore"]
        AreaChart["Recharts Trend Area Chart (Smooth Valence Curve)"]
        PieChart["Recharts Mood Distribution Donut"]
        TagCloud["Trigger Impact Heatmap"]
    end

    RawEntries --> QueryRunner
    JunctionTags --> TriggerMapper
    QueryRunner --> GroupingEngine
    GroupingEngine --> Endpoint
    TriggerMapper --> Endpoint
    Endpoint -->|"JSON Payload"| Store
    Store --> AreaChart
    Store --> PieChart
    Store --> TagCloud
```

---

## 4. Frontend Architecture

### 4.1 Application Structure Explanation
The frontend client is architected as an offline-tolerant, modular Single Page Application (SPA) built using **React 18, Vite, and Tailwind CSS v4**. It follows a **Domain-Driven Feature Slice** pattern. Instead of grouping files by generic technical types (e.g. all components in one folder, all hooks in another), code is organised around self-contained business capabilities. This ensures high cohesion, loose coupling, and clean boundaries for scaling development.

### 4.2 Feature-Based Modular Folder Architecture
The codebase adheres to the following structural topology:

```
src/
├── app/                        # Application bootstrap, routing, and global providers
│   ├── App.tsx                 # Root application component
│   ├── routes.tsx              # React Router v6 tree configuration
│   └── providers.tsx           # Composed providers (Query, Theme, Auth)
├── assets/                     # Static icons, ambient audio, and visual SVGs
├── components/                 # Shared cross-cutting components
│   ├── feedback/               # Toasts, alert banners, crisis dialogs
│   ├── layout/                 # Shell, Header, Sidebar, BottomNav, SanctuaryCanvas
│   └── ui/                     # Design system atoms (Button, Slider, Card, Modal, Input)
├── features/                   # Self-contained domain modules
│   ├── analytics/              # Mood trends, calendar heatmaps, trigger analysis
│   │   ├── components/         # TrendChart, DistributionDonut, TriggerImpact
│   │   ├── hooks/              # useAnalyticsData, useMoodAggregations
│   │   ├── services/           # analyticsApi.ts
│   │   └── types/              # analytics.types.ts
│   ├── auth/                   # Registration, login, password recovery
│   ├── companion/              # solace AI conversation view, streaming text bubbles
│   ├── journal/                # Markdown journal editor, history, sentiment tags
│   ├── mood/                   # Mood check-in dial, intensity sliders, tag selector
│   ├── preferences/            # Sanctuary settings, theme lock, AI memory toggles
│   └── regulation/             # Grounding exercises, box breathing canvas, reframing
├── hooks/                      # Global cross-feature utility hooks (useDebounce, useMediaQuery)
├── lib/                        # Infrastructure singletons and configured instances
│   ├── api.ts                  # Fetch API wrapper with JWT interceptor & refresh queue
│   ├── constants.ts            # Fixed taxonomy definitions, default mood constants
│   └── utils.ts                # Tailwind class mergers (clsx, tailwind-merge)
├── stores/                     # Zustand global stores
│   ├── authStore.ts            # Session tokens, user profile, auth status
│   ├── moodStore.ts            # Active mood, check-in history, active triggers
│   ├── themeStore.ts           # Dynamic CSS token state, manual overrides, dark mode
│   ├── aiStore.ts              # Conversation list, streaming buffer, active mode
│   ├── journalStore.ts         # Active entry draft, historical list
│   └── analyticsStore.ts       # Cached aggregation metrics
├── styles/                     # CSS stylesheets
│   ├── globals.css             # Tailwind v4 imports, base typography
│   └── themes.css              # Dynamic CSS custom property root mappings
└── types/                      # Universal cross-domain TypeScript contracts
```

### 4.3 Shared Component Layers
The user interface is stratified into three distinct visual tiers:
1.  **Primitives / Atoms (`components/ui/`):** Purely presentational, accessible headless components styled with Tailwind CSS. Includes `Button`, `Slider`, `Dialog`, `Tooltip`, `Card`, and `Badge`. They are completely decoupled from domain business logic.
2.  **Composites / Molecules (`components/feedback/`, `features/*/components/`):** Domain-informed assemblies combining primitives with local state. Examples include `MoodDialSelector`, `BreathingCirclePacer`, `JournalEntryCard`, and `StreamingMessageBubble`.
3.  **Layouts & Sanctuary Canvases (`components/layout/`):** Structural components providing layout constraints, responsive navigational headers, and the ambient backdrop (`SanctuaryCanvas`) that applies radial gradient blends derived from the active theme tokens.

### 4.4 Design Token System Overview
The design token system binds semantic styling tokens to CSS Custom Properties injected into the document root (`:root`). Rather than applying fixed Tailwind utility classes (such as `bg-blue-500`), layout surfaces apply dynamic variable tokens (such as `bg-[var(--surface)]` or `text-[var(--text-primary)]`). When a mood transition occurs, updating these CSS variables dynamically updates the entire application's aesthetic atmosphere without triggering re-renders across the React virtual DOM tree.

### 4.5 ThemeProvider and MoodContextProvider Roles
*   **`MoodContextProvider`:** Manages the domain lifecycle of mood tracking. It encapsulates the user's latest recorded check-in, provides mutation methods to log new emotional states, and computes short-term emotional valence averages.
*   **`ThemeProvider`:** Listens directly to the `MoodContextProvider`. When the active mood updates, the `ThemeProvider` queries the central mood-to-theme mapping table, identifies whether the user has applied a manual theme lock, and imperatively injects the corresponding token values into `document.documentElement.style`. It also observes the `prefers-reduced-motion` media query to modulate transition durations.

### 4.6 Authentication State Management
Authentication state is managed by `useAuthStore` in Zustand. The short-lived JWT Access Token is held **exclusively in memory** within the store, protecting it against Cross-Site Scripting (XSS) extraction from `localStorage`. The long-lived Refresh Token resides in a secure, `httpOnly`, `SameSite=Strict` cookie managed exclusively by the browser and the `/api/auth` endpoints.

Upon initial client boot:
1.  The application invokes `authStore.initialize()`, issuing a silent `POST /api/auth/refresh` request.
2.  If the cookie is valid, the API returns a fresh Access Token and user profile, instantly hydrating the session.
3.  If the request fails, the store clears state and redirects the user to `/login`.

### 4.7 Global State (Zustand Stores)
The client maintains six distinct Zustand stores:
1.  **`useAuthStore`:** Manages `accessToken`, `user` object (`id`, `email`, `name`), `status` (`idle` | `authenticating` | `authenticated` | `unauthenticated`), and login/logout actions.
2.  **`useMoodStore`:** Manages `currentMood`, `recentEntries`, `activeCheckInDraft`, and optimistic mood logging actions.
3.  **`useThemeStore`:** Manages `activeTheme`, `manualOverrideMood`, `ambientColor`, `animationIntensity`, and token injection routines.
4.  **`useAiStore`:** Manages `activeConversationId`, `conversationsList`, `messageStreamBuffer`, `regulationMode` (`LISTEN`, `CALM`, etc.), and streaming controller abort signals.
5.  **`useJournalStore`:** Manages `entries`, `activeDraft`, `isSaving` debounce status, and filter parameters (e.g. filter by tag or mood).
6.  **`useAnalyticsStore`:** Manages `timeRange` (7d, 30d, 90d), aggregated metrics payload, distribution charts, and cache invalidation flags.

### 4.8 Local Component State Guidelines
Local state via `useState` or `useReducer` is reserved strictly for ephemeral UI concerns:
*   Uncontrolled text input buffers before saving.
*   Modal, drawer, and popover open/close visibility flags.
*   Hover, active, and local focus states.
*   Somatic exercise timers (e.g., breath-holding second counters in the regulation engine).
Any state that impacts two or more disparate features, or requires persistence across page transitions, must reside in a Zustand store.

### 4.9 API Client Architecture
The frontend leverages a custom `fetch` wrapper (`src/lib/api.ts`) featuring request and response interceptors:
*   **Request Interceptor:** Automatically retrieves the in-memory access token from `useAuthStore` and attaches the `Authorization: Bearer <token>` header to all outgoing requests.
*   **Response Interceptor & Refresh Concurrency Queue:** Intercepts `401 Unauthorized` responses. When an access token expires, outgoing requests are queued while a single refresh token request is executed. Once a new token is obtained, all queued requests are replayed with the new token. If the refresh request fails, the queue is cleared, user state is wiped, and the application redirects to `/login`.

### 4.10 Error Handling Strategy
*   **React Error Boundaries:** High-level boundaries wrap feature routes to catch unexpected rendering crashes, presenting a calming, non-technical recovery screen with a "Breathe & Reload" button.
*   **Normalised API Error Structure:** The API returns standard RFC 7807 error payloads (`{ status, code, message, details }`). The API client unpacks these into typed client-side exceptions displayed via gentle toast notifications.

### 4.11 Loading and Empty State Conventions
*   **Skeleton Loaders:** Content loading states use pulse-animated layout skeletons styled to match current theme tokens, avoiding disruptive white flashes or layout shifts.
*   **Compassionate Empty States:** When lists (e.g., journal entries or historical check-ins) are empty, the UI displays soothing, non-judgmental illustrations with gentle calls to action (e.g., *"Your sanctuary is quiet. Whenever you're ready, take your first check-in."*).

---

## 5. Backend Architecture

### 5.1 Architecture Overview
The backend application is structured as a layered, modular system running on **Node.js** using the **Hono** framework. It enforces a strict separation of concerns across Controllers, Services, Repositories, Middleware, and Validators.

```mermaid
flowchart TD
    Request([Incoming Client HTTP Request])
    
    subgraph MW ["Middleware Pipeline"]
        CORS["CORS Middleware"]
        RateLimit["Rate Limiter (Per-IP / Per-User)"]
        AuthGuard["JWT Verification Guard"]
        ZodValidator["Zod Schema Validator Middleware"]
    end

    subgraph Ctrls ["Controller Layer (Transport Adaptation)"]
        AuthController["Auth Controller"]
        MoodController["Mood Controller"]
        JournalController["Journal Controller"]
        AiController["AI Conversation Controller"]
        AnalyticsController["Analytics Controller"]
        RegController["Regulation Controller"]
    end

    subgraph Svcs ["Domain Service Layer (Business Logic Orchestration)"]
        AuthService["Auth Service"]
        MoodService["Mood & Theme Service"]
        JournalService["Journal Service"]
        AiOrchService["AI Orchestration & Safety Service"]
        AnalyticsService["Analytics Service"]
        RegService["Regulation Service"]
    end

    subgraph Repos ["Repository Layer (Data Access via Drizzle ORM)"]
        UserRepo["User Repository"]
        MoodRepo["Mood Repository"]
        JournalRepo["Journal Repository"]
        AiRepo["AI Conversation Repository"]
        AnalyticsRepo["Analytics Repository"]
    end

    subgraph StorageInfra ["Database Engine"]
        NeonDB[("Neon PostgreSQL Engine")]
    end

    Request --> CORS
    CORS --> RateLimit
    RateLimit --> AuthGuard
    AuthGuard --> ZodValidator
    
    ZodValidator --> AuthController
    ZodValidator --> MoodController
    ZodValidator --> JournalController
    ZodValidator --> AiController
    ZodValidator --> AnalyticsController
    ZodValidator --> RegController

    AuthController --> AuthService
    MoodController --> MoodService
    JournalController --> JournalService
    AiController --> AiOrchService
    AnalyticsController --> AnalyticsService
    RegController --> RegService

    AuthService --> UserRepo
    MoodService --> MoodRepo
    JournalService --> JournalRepo
    AiOrchService --> AiRepo
    AnalyticsService --> AnalyticsRepo
    RegService --> MoodRepo

    UserRepo --> NeonDB
    MoodRepo --> NeonDB
    JournalRepo --> NeonDB
    AiRepo --> NeonDB
    AnalyticsRepo --> NeonDB
```

### 5.2 Layer Responsibilities

#### Controllers
Controllers act as the transport layer. They are responsible for extracting HTTP parameters, query parameters, request bodies, and authenticated user credentials from the Hono context (`c: Context`). They delegate processing to domain services and format standard JSON or SSE responses. Controllers contain **zero direct SQL queries or complex business logic**.

#### Services
Services encapsulate the core business logic, domain invariants, and operational workflows. They manage database transactions, orchestrate calls between different repositories, and coordinate with external systems (such as LLM providers or object storage).

#### Repositories
Repositories isolate data access logic. Built using **Drizzle ORM**, they compose type-safe SQL queries targeting Neon PostgreSQL. Every repository query handling user-owned resources enforces an explicit tenant isolation predicate (`eq(table.userId, authenticatedUserId)`).

#### Middleware
The middleware pipeline inspects and mutates requests prior to controller execution:
*   **`corsMiddleware`:** Restricts Cross-Origin Resource Sharing to the authorized frontend domain.
*   **`rateLimitMiddleware`:** Tracks request frequencies in-memory using a sliding-window counter to prevent abuse.
*   **`authMiddleware`:** Cryptographically validates the incoming JWT Bearer token and binds the authenticated `userId` and `userEmail` to the Hono request context.
*   **`errorNormaliserMiddleware`:** Global catch-all handler that intercepts unhandled exceptions, logs diagnostic stacks to stderr, and formats consistent error responses for the client.

#### Validators
Built using **Zod**, validators define strict contracts for all incoming request bodies, route parameters, and query strings. The middleware rejects malformed payloads before they reach the controller layer.

### 5.3 Specialised Domain Services

#### AI Orchestration Service
Responsible for context assembly, dynamic system prompt synthesis, streaming response coordination, and safety guardrail enforcement. It interfaces directly with the OpenAI-compatible abstraction layer and manages token budgets to prevent model context overflow.

#### Mood Analysis Service
Evaluates incoming check-ins, tracks emotional shifts, computes valence scores, and maps entries to their corresponding theme configurations.

#### Analytics Service
Executes time-series aggregations, mood distribution computations, and trigger frequency correlations across 7-day, 30-day, and 90-day historical windows.

#### Journal Service
Manages rich markdown journal entries, handles soft-deletion flags, links mood check-in references, and prepares data bundles for user export.

---

## 6. Database Architecture

### 6.1 Database Engine & ORM Configuration
The storage tier runs on **PostgreSQL 16** hosted on **Neon Serverless PostgreSQL**. Neon provides auto-scaling compute, scale-to-zero efficiency during idle periods, and built-in connection pooling via PgBouncer. Schema declaration and type-safe query construction are handled by **Drizzle ORM**, with schema migrations managed declaratively through **Drizzle Kit**.

### 6.2 Entity Specifications

#### 1. `users`
**Purpose:** Core user account records, credentials, and verification state.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique user identifier |
| `email` | `varchar(255)` | `NOT NULL, UNIQUE` | Normalized lowercase user email address |
| `password_hash` | `text` | `NOT NULL` | bcrypt salted password hash (cost factor 12) |
| `display_name` | `varchar(100)` | `NOT NULL` | User's preferred self-reference name |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Account creation timestamp |
| `updated_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Last account modification timestamp |
| `deleted_at` | `timestamp with time zone`| `NULL` | Soft delete timestamp |

#### 2. `user_preferences`
**Purpose:** User-specific interface, accessibility, theme, and AI configuration settings.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique record identifier |
| `user_id` | `uuid` | `NOT NULL, UNIQUE, REFERENCES users(id) ON DELETE CASCADE`| Owning user relation |
| `pinned_theme` | `varchar(50)` | `NULL` | Optional manual theme lock override |
| `reduced_motion` | `boolean` | `NOT NULL, DEFAULT false` | Accessibility: minimize Framer Motion transitions |
| `dark_mode_preference`| `varchar(20)` | `NOT NULL, DEFAULT 'system'` | System, light, or dark preference |
| `ai_personalisation_opt_in`| `boolean` | `NOT NULL, DEFAULT true` | Permission to use journal/mood in AI context |
| `daily_checkin_reminder` | `time` | `NULL` | Preferred daily check-in prompt time |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Record creation timestamp |
| `updated_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Last update timestamp |

#### 3. `mood_entries`
**Purpose:** Quantitative and categorical records of user emotional check-ins.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique entry identifier |
| `user_id` | `uuid` | `NOT NULL, REFERENCES users(id) ON DELETE CASCADE` | Owning user relation |
| `mood` | `varchar(50)` | `NOT NULL` | Primary mood (Happy, Calm, Neutral, Sad, Anxious, Angry, Overwhelmed) |
| `valence` | `smallint` | `NOT NULL, CHECK (valence BETWEEN 1 AND 10)` | Affective valence score (1 = most negative, 10 = most positive) |
| `intensity` | `smallint` | `NOT NULL, CHECK (intensity BETWEEN 1 AND 10)` | Emotional arousal/energy level (1 = lowest, 10 = highest) |
| `note` | `text` | `NULL` | Optional short text note accompanying check-in |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Timestamp check-in occurred |

#### 4. `mood_tags`
**Purpose:** Normalized catalogue of situational triggers and emotional contexts.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique tag identifier |
| `user_id` | `uuid` | `NULL, REFERENCES users(id) ON DELETE CASCADE` | NULL for system defaults, user UUID for custom tags |
| `name` | `varchar(50)` | `NOT NULL` | Tag label (e.g. Work, Sleep, Family, Exercise) |
| `category` | `varchar(50)` | `NOT NULL, DEFAULT 'general'` | Contextual category (Environment, Social, Body) |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Creation timestamp |

#### 5. `mood_entry_tags`
**Purpose:** Many-to-many junction binding mood check-ins to situational tags.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `mood_entry_id` | `uuid` | `NOT NULL, REFERENCES mood_entries(id) ON DELETE CASCADE` | Associated mood check-in |
| `tag_id` | `uuid` | `NOT NULL, REFERENCES mood_tags(id) ON DELETE CASCADE` | Associated situational tag |

#### 6. `journal_entries`
**Purpose:** Private, reflective user journal writings with optional mood linkage.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique journal entry identifier |
| `user_id` | `uuid` | `NOT NULL, REFERENCES users(id) ON DELETE CASCADE` | Owning user relation |
| `associated_mood_entry_id`| `uuid` | `NULL, REFERENCES mood_entries(id) ON DELETE SET NULL` | Optional associated check-in |
| `title` | `varchar(255)` | `NOT NULL` | Entry title |
| `content` | `text` | `NOT NULL` | Markdown-formatted journal content |
| `word_count` | `integer` | `NOT NULL, DEFAULT 0` | Total word count |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Creation timestamp |
| `updated_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Last edit timestamp |
| `deleted_at` | `timestamp with time zone`| `NULL` | Soft-delete timestamp |

#### 7. `ai_conversations`
**Purpose:** Top-level conversational threads between the user and solace AI.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique conversation identifier |
| `user_id` | `uuid` | `NOT NULL, REFERENCES users(id) ON DELETE CASCADE` | Owning user relation |
| `title` | `varchar(255)` | `NOT NULL, DEFAULT 'New Sanctuary Dialogue'` | Auto-generated or custom conversation title |
| `active_mode` | `varchar(50)` | `NOT NULL, DEFAULT 'LISTEN'` | Current regulation mode (LISTEN, CALM, etc.) |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Thread initiation timestamp |
| `updated_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Timestamp of last message |

#### 8. `ai_messages`
**Purpose:** Individual message turns in an AI conversation thread.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique message identifier |
| `conversation_id`| `uuid` | `NOT NULL, REFERENCES ai_conversations(id) ON DELETE CASCADE`| Associated conversation thread |
| `sender_role` | `varchar(20)` | `NOT NULL, CHECK (sender_role IN ('user', 'assistant', 'system'))`| Role origin |
| `content` | `text` | `NOT NULL` | Message content text |
| `tokens_used` | `integer` | `NULL` | Estimated token count consumed |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Message timestamp |

#### 9. `regulation_activities`
**Purpose:** Static catalog of somatic, mindfulness, and cognitive grounding exercises.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique activity identifier |
| `title` | `varchar(100)` | `NOT NULL` | Activity title (e.g. 4-7-8 Breathing, 5-4-3-2-1 Sensory) |
| `category` | `varchar(50)` | `NOT NULL` | Somatic, Cognitive, Mindfulness, Gratitude |
| `duration_seconds`| `integer` | `NOT NULL` | Target duration of the exercise |
| `instructions` | `jsonb` | `NOT NULL` | Structured step-by-step guidance payload |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Record creation timestamp |

#### 10. `regulation_sessions`
**Purpose:** Historical records of completed user regulation exercises.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique session identifier |
| `user_id` | `uuid` | `NOT NULL, REFERENCES users(id) ON DELETE CASCADE` | Owning user relation |
| `activity_id` | `uuid` | `NOT NULL, REFERENCES regulation_activities(id) ON DELETE CASCADE`| Undertaken activity relation |
| `duration_spent_seconds`| `integer` | `NOT NULL` | Actual seconds user spent in exercise |
| `completed` | `boolean` | `NOT NULL, DEFAULT true` | Whether session was completed or aborted |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Session timestamp |

#### 11. `personal_insights`
**Purpose:** System-generated algorithmic observations linking moods, habits, and triggers.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique insight identifier |
| `user_id` | `uuid` | `NOT NULL, REFERENCES users(id) ON DELETE CASCADE` | Owning user relation |
| `insight_text` | `text` | `NOT NULL` | Descriptive observation (e.g., "Calmness increases after sleep") |
| `category` | `varchar(50)` | `NOT NULL` | Trend, Pattern, Milestone |
| `is_dismissed` | `boolean` | `NOT NULL, DEFAULT false` | User dismissal state |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Insight generation timestamp |

#### 12. `user_goals`
**Purpose:** Habit formation and emotional wellness targets tracked by the user.
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `uuid` | `PRIMARY KEY, DEFAULT gen_random_uuid()` | Unique goal identifier |
| `user_id` | `uuid` | `NOT NULL, REFERENCES users(id) ON DELETE CASCADE` | Owning user relation |
| `title` | `varchar(150)` | `NOT NULL` | Goal title (e.g., "Daily Evening Check-in") |
| `target_frequency_days`| `smallint` | `NOT NULL, DEFAULT 7` | Target days per week |
| `current_streak` | `integer` | `NOT NULL, DEFAULT 0` | Consecutive days achieved |
| `created_at` | `timestamp with time zone`| `NOT NULL, DEFAULT now()` | Goal creation timestamp |
| `archived_at` | `timestamp with time zone`| `NULL` | Goal retirement timestamp |

### 6.3 Indexing Strategy
To ensure low query latencies across high-volume time-series operations, the following indexes are applied:

*   `users(email)`: B-Tree index enforcing instant lookup during authentication.
*   `mood_entries(user_id, created_at DESC)`: Composite B-Tree index enabling rapid time-window slicing for dashboards and analytics without table scans.
*   `mood_entries(user_id, mood)`: Speeds up mood distribution calculations.
*   `mood_entry_tags(mood_entry_id, tag_id)`: Primary composite key index accelerating tag resolution during check-in retrieval.
*   `journal_entries(user_id, created_at DESC) WHERE deleted_at IS NULL`: Partial index facilitating rapid journal history loads while filtering soft-deleted entries.
*   `ai_messages(conversation_id, created_at ASC)`: Index accelerating sequential retrieval of conversation turns during AI context assembly.
*   `regulation_sessions(user_id, created_at DESC)`: Speeds up user activity history checks.

### 6.4 Data Deletion Strategy
*   **Soft Deletion:** Applied strictly to `users` and `journal_entries`. Soft-deleted rows have a non-null `deleted_at` timestamp. This preserves data recovery options during accidental deletion windows and maintains historical integrity. All default repository queries filter with `WHERE deleted_at IS NULL`.
*   **Hard Deletion & Cascading:** When a user invokes complete account deletion under GDPR compliance guidelines, a hard `DELETE FROM users WHERE id = :userId` is executed. Foreign keys targeting `users.id` enforce `ON DELETE CASCADE`, reliably purging preferences, mood logs, journal entries, conversation histories, and regulation sessions across the database.

### 6.5 Entity Relationship Diagram (ERD)

```mermaid
erDiagram
    USERS ||--|| USER_PREFERENCES : has
    USERS ||--o{ MOOD_ENTRIES : logs
    USERS ||--o{ MOOD_TAGS : creates
    USERS ||--o{ JOURNAL_ENTRIES : writes
    USERS ||--o{ AI_CONVERSATIONS : owns
    USERS ||--o{ REGULATION_SESSIONS : completes
    USERS ||--o{ PERSONAL_INSIGHTS : receives
    USERS ||--o{ USER_GOALS : tracks

    MOOD_ENTRIES ||--o{ MOOD_ENTRY_TAGS : tagged_with
    MOOD_TAGS ||--o{ MOOD_ENTRY_TAGS : categorises

    MOOD_ENTRIES ||--o| JOURNAL_ENTRIES : attaches_to

    AI_CONVERSATIONS ||--o{ AI_MESSAGES : contains

    REGULATION_ACTIVITIES ||--o{ REGULATION_SESSIONS : instances_of

    USERS {
        uuid id PK
        varchar email UK
        text password_hash
        varchar display_name
        timestamp created_at
        timestamp updated_at
        timestamp deleted_at
    }

    USER_PREFERENCES {
        uuid id PK
        uuid user_id FK
        varchar pinned_theme
        boolean reduced_motion
        varchar dark_mode_preference
        boolean ai_personalisation_opt_in
        time daily_checkin_reminder
        timestamp created_at
        timestamp updated_at
    }

    MOOD_ENTRIES {
        uuid id PK
        uuid user_id FK
        varchar mood
        smallint valence
        smallint intensity
        text note
        timestamp created_at
    }

    MOOD_TAGS {
        uuid id PK
        uuid user_id FK
        varchar name
        varchar category
        timestamp created_at
    }

    MOOD_ENTRY_TAGS {
        uuid mood_entry_id PK,FK
        uuid tag_id PK,FK
    }

    JOURNAL_ENTRIES {
        uuid id PK
        uuid user_id FK
        uuid associated_mood_entry_id FK
        varchar title
        text content
        integer word_count
        timestamp created_at
        timestamp updated_at
        timestamp deleted_at
    }

    AI_CONVERSATIONS {
        uuid id PK
        uuid user_id FK
        varchar title
        varchar active_mode
        timestamp created_at
        timestamp updated_at
    }

    AI_MESSAGES {
        uuid id PK
        uuid conversation_id FK
        varchar sender_role
        text content
        integer tokens_used
        timestamp created_at
    }

    REGULATION_ACTIVITIES {
        uuid id PK
        varchar title
        varchar category
        integer duration_seconds
        jsonb instructions
        timestamp created_at
    }

    REGULATION_SESSIONS {
        uuid id PK
        uuid user_id FK
        uuid activity_id FK
        integer duration_spent_seconds
        boolean completed
        timestamp created_at
    }

    PERSONAL_INSIGHTS {
        uuid id PK
        uuid user_id FK
        text insight_text
        varchar category
        boolean is_dismissed
        timestamp created_at
    }

    USER_GOALS {
        uuid id PK
        uuid user_id FK
        varchar title
        smallint target_frequency_days
        integer current_streak
        timestamp created_at
        timestamp archived_at
    }
```

---

## 7. AI Architecture

### 7.1 Context Assembly Pipeline
The AI Orchestration Service constructs context dynamically for each conversational turn, adhering to a strict token budget to prevent context window overflow and avoid degradation of model attention.

```mermaid
flowchart TD
    Trigger([User Dispatches Message])
    
    subgraph Assembly ["Context Assembly Pipeline"]
        Step1["1. User Profile & Preferences (Name, Opt-In Flags)"]
        Step2["2. Current Emotional State (Active Mood, Valence, Intensity)"]
        Step3["3. 7-Day Mood History & Situational Tag Frequencies"]
        Step4["4. Journal Summaries (High-Level Topics, No Raw PII)"]
        Step5["5. Recent Regulation Activities Completed"]
        Step6["6. Sliding Window Conversation History (Last 10 Turns)"]
    end

    subgraph Budgeting ["Token Budget Allocation (Max 4,000 Prompt Tokens)"]
        B1["System Persona & Safety Constraints: ~800 tokens"]
        B2["User Emotional Context & History: ~600 tokens"]
        B3["Recent Journal & Activity Summaries: ~600 tokens"]
        B4["Conversation History Window: ~1,500 tokens"]
        B5["Current User Input: ~500 tokens"]
    end

    subgraph PromptPayload ["Final Context Payload"]
        CompiledPrompt["Injected System Prompt + Formatted Message Tree"]
    end

    Trigger --> Step1
    Step1 --> Step2
    Step2 --> Step3
    Step3 --> Step4
    Step4 --> Step5
    Step5 --> Step6

    Step6 --> Budgeting
    Budgeting --> CompiledPrompt
```

### 7.2 System Prompt Structure
The system prompt is organized into layered, immutable instructions:
1.  **Core Persona Definition:** Establishes solace AI as a compassionate, non-judgmental, grounded emotional companion. Emphasises reflective listening over quick problem-solving.
2.  **Non-Clinical Boundary & Safety Guardrails:** Strict instructions prohibiting medical diagnoses, prescribing medications, or offering clinical psychiatric evaluations. Mandates immediate crisis protocol activation upon detection of self-harm signals.
3.  **Mood-Aware Tone Guidance:** Dynamically injected rules modulating conversational tone to match the user's active emotional state (e.g. gentle brevity for *Overwhelmed*, validating warmth for *Sad*, grounding pacing for *Anxious*).
4.  **User Context Injection:** Formatted emotional baseline and contextual tags.
5.  **Regulation Mode Directives:** Behavioral directives based on the conversation's active regulation mode (`LISTEN`, `CALM`, etc.).

### 7.3 Conversation Memory Management
*   **Short-Term Working Memory:** Managed through a sliding window of the last 10 messages within the active conversation thread, preserving conversational continuity.
*   **Long-Term Reflection Memory:** To protect privacy and prevent prompt bloat, solace+ does not store unbounded conversational logs in vector embeddings. Instead, if the user opts in, a lightweight summary of key emotional topics is synthesized periodically and stored in `personal_insights`. Users can inspect, modify, or purge these insights at any time.

### 7.4 Model Abstraction Interface
To prevent vendor lock-in, all model interactions are governed by an `AIProvider` TypeScript abstraction interface:

```typescript
export interface StreamChunk {
  text: string;
  isComplete: boolean;
  tokensUsed?: number;
}

export interface CompletionOptions {
  temperature?: number;
  maxTokens?: number;
  signal?: AbortSignal;
}

export interface AIProvider {
  readonly providerId: string;
  readonly modelName: string;

  /**
   * Generates a streaming text response using Server-Sent Events
   */
  generateStream(
    systemPrompt: string,
    messages: Array<{ role: 'user' | 'assistant' | 'system'; content: string }>,
    options?: CompletionOptions
  ): AsyncIterable<StreamChunk>;

  /**
   * Generates a non-streaming complete text response
   */
  generateText(
    systemPrompt: string,
    messages: Array<{ role: 'user' | 'assistant' | 'system'; content: string }>,
    options?: CompletionOptions
  ): Promise<{ text: string; tokensUsed: number }>;

  /**
   * Estimates token consumption for a given input string
   */
  estimateTokens(input: string): number;
}
```

Implementations include `OpenAIProvider`, `OpenRouterProvider`, and `OllamaProvider`. The active provider is resolved at runtime via environment variables (`AI_PROVIDER_DEFAULT=openai`).

### 7.5 Streaming Implementation (Hono + Vercel AI SDK)
Conversational endpoints stream responses using Server-Sent Events (SSE) via Hono's `streamText` and Web Streams API. This approach delivers response tokens to the client with sub-500ms initial latency, avoiding large HTTP response buffering.

```
[Client] ---- POST /api/conversations/:id/messages ----> [Hono Server]
[Client] <--- HTTP 200 (Content-Type: text/event-stream) [Hono Server]
[Client] <--- data: {"chunk": "I"} ---------------------- [Hono Server]
[Client] <--- data: {"chunk": " hear"} ------------------ [Hono Server]
[Client] <--- data: {"chunk": " you."} ------------------ [Hono Server]
[Client] <--- data: [DONE] ------------------------------ [Hono Server]
```

### 7.6 Rate Limiting & Token Budget Management
*   **Message Frequency Bounds:** Users are limited to 30 messages per rolling hour and 150 messages per day to ensure healthy usage boundaries and prevent runaway API expenses.
*   **Token Caps:** Responses are capped at a maximum of 400 completion tokens. solace AI is engineered for concise, conversational responses rather than generating expansive essays.

### 7.7 Safety and Guardrails
*   **Crisis Signal Interception:** Incoming text is evaluated against a pre-compiled, deterministic regular expression lexicon of crisis keywords (e.g., self-harm, suicide, severe violence). If a match is flagged, the LLM invocation is **aborted immediately**.
*   **Crisis Escalation Response Template:** When a crisis pattern is detected, the API returns a structured, non-generative safety card providing vetted emergency resources:
    *   *National Suicide Prevention Lifeline / 988 Suicide & Crisis Lifeline: Dial 988*
    *   *Crisis Text Line: Text HOME to 741741*
    *   *The Trevor Project: Call 866-488-7386*
    *   *International resources directory link (Befrienders Worldwide).*
*   **Diagnostic Refusal:** When asked for psychiatric diagnoses, pharmaceutical prescriptions, or clinical therapy advice, solace AI responds with a standardized boundary statement clarifying its non-clinical nature and encouraging the user to consult a licensed healthcare professional.

### 7.8 Fallback Behaviour
If the upstream AI provider is unavailable or rate-limited:
1.  The system attempts a retry against the configured secondary provider (e.g., OpenRouter).
2.  If the secondary provider fails, the stream yields a graceful degradation response:
    > *"I am having trouble connecting to my conversational thoughts right now, but I am still here with you. Would you like to try a calming grounding exercise or take a moment to journal while I reconnect?"*
3.  The UI automatically presents quick links to offline-capable regulation tools (Box Breathing and Grounding exercises).

### 7.9 Regulation Modes
Users can adjust solace AI's conversational mode. Each mode injects distinct behavioral instructions into the system prompt:

| Mode | Emotional Objective | Conversational Style & Prompt Modification |
| :--- | :--- | :--- |
| **LISTEN** | Pure validation and non-judgmental presence | Prioritizes reflective listening. Avoids offering solutions, advice, or reframing. Validates the user's expressed emotions and provides space for expression. |
| **UNDERSTAND** | Gentle exploration of emotional triggers | Employs gentle, open-ended inquiry. Helps the user unpack the roots of their feelings and connect current emotions to recent situations. |
| **CALM** | Somatic de-escalation and soothing | Delivers short, grounding sentences with calming pacing. Prompts the user to relax physical tension in their shoulders, jaw, and breath. |
| **REFLECT** | Cognitive reframing and perspective | Gently encourages looking at the situation from alternative perspectives, identifying cognitive distortions with warmth and compassion. |
| **REFOCUS** | Transitioning into small, achievable actions | Helps the user overcome paralysis or overwhelm by identifying a single, low-friction micro-step to regain agency. |
| **GRATITUDE** | Savoring positive experiences | Guides mindful attention toward small everyday appreciations, anchoring positive valence without toxic positivity. |

---

## 8. Mood-Responsive Theme Architecture

### 8.1 Central Mood-to-Theme Mapping
The table below specifies the complete design token system for each of the seven core moods. Every token represents a valid 6-character hex color code or defined intensity value.

| Token Identifier | Happy | Calm | Neutral | Sad | Anxious | Angry | Overwhelmed |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `--background` | `#fefdf5` | `#f4f9f7` | `#f8fafc` | `#f5f6fa` | `#faf7f5` | `#f8fafc` | `#fafafa` |
| `--surface` | `#fffbeb` | `#eaf4f0` | `#f1f5f9` | `#ebeef7` | `#f5ece6` | `#edf2f7` | `#f4f4f5` |
| `--surface-raised` | `#fef3c7` | `#dcfce7` | `#e2e8f0` | `#dbe2f3` | `#eddcd2` | `#e2e8f0` | `#e4e4e7` |
| `--primary` | `#d97706` | `#0d9488` | `#475569` | `#4f46e5` | `#c2410c` | `#b91c1c` | `#7c3aed` |
| `--primary-hover` | `#b45309` | `#0f766e` | `#334155` | `#4338ca` | `#9a3412` | `#991b1b` | `#6d28d9` |
| `--secondary` | `#f59e0b` | `#14b8a6` | `#64748b` | `#6366f1` | `#ea580c` | `#3b82f6` | `#8b5cf6` |
| `--accent` | `#fbbf24` | `#5eead4` | `#94a3b8` | `#a5b4fc` | `#fdba74` | `#f87171` | `#c4b5fd` |
| `--text-primary` | `#451a03` | `#134e4a` | `#0f172a` | `#1e1b4b` | `#431407` | `#1e293b` | `#18181b` |
| `--text-secondary` | `#78350f` | `#2d6a4f` | `#334155` | `#3730a3` | `#7c2d12` | `#334155` | `#3f3f46` |
| `--text-muted` | `#92400e` | `#52796f` | `#64748b` | `#6b7280` | `#9a3412` | `#64748b` | `#71717a` |
| `--border` | `#fde68a` | `#ccfbf1` | `#cbd5e1` | `#c7d2fe` | `#fed7aa` | `#cbd5e1` | `#d4d4d8` |
| `--gradient-start` | `#fef3c7` | `#e6f4ea` | `#f1f5f9` | `#ebeef7` | `#f5ece6` | `#edf2f7` | `#f4f4f5` |
| `--gradient-end` | `#fde68a` | `#d1fae5` | `#e2e8f0` | `#dbe2f3` | `#eddcd2` | `#e2e8f0` | `#e4e4e7` |
| `--ambient-color` | `#fef08a` | `#99f6e4` | `#cbd5e1` | `#c7d2fe` | `#ffedd5` | `#e2e8f0` | `#ede9fe` |
| `--animation-intensity`| `moderate`| `subtle` | `subtle` | `none` | `subtle` | `none` | `subtle` |

### 8.2 Application Mechanism
Tokens are injected at the `:root` pseudo-class level via JavaScript:

```typescript
export function applyThemeTokens(tokens: Record<string, string>): void {
  const root = document.documentElement;
  Object.entries(tokens).forEach(([property, value]) => {
    root.style.setProperty(property, value);
  });
}
```

Components reference these properties using Tailwind utility classes or inline variable assignments:
```html
<div class="bg-[var(--surface)] border border-[var(--border)] text-[var(--text-primary)]">
  <!-- Content dynamically harmonised to mood atmosphere -->
</div>
```

### 8.3 ThemeProvider Lifecycle
1.  **Mount:** The `ThemeProvider` reads the user's initial state from `useMoodStore` and checks for manual theme pins in `useThemeStore`.
2.  **Resolution:** If a pinned theme exists, it takes precedence. Otherwise, the store resolves the palette for the user's most recent mood check-in (falling back to `Neutral` if no check-ins exist).
3.  **Application:** Injects resolved CSS variables into `document.documentElement`.
4.  **Observer:** Listens for updates in `useMoodStore`. When a new check-in is logged, the provider reconciles new tokens and triggers the CSS transition sequence.

### 8.4 CSS Transition Strategy
To avoid jarring visual shifts, transitions between mood states are smoothed using CSS transitions defined on global surfaces:

```css
:root {
  transition: 
    background-color 200ms cubic-bezier(0.4, 0, 0.2, 1),
    color 200ms cubic-bezier(0.4, 0, 0.2, 1),
    border-color 200ms cubic-bezier(0.4, 0, 0.2, 1);
}

.sanctuary-ambient-canvas {
  transition: background 400ms cubic-bezier(0.4, 0, 0.2, 1);
}
```

Colors and surface borders interpolate over **200ms**, while ambient canvas gradients blend over **400ms**, delivering a subtle and calming atmospheric shift.

### 8.5 Manual Override Capability
Users who find dynamic theme shifts distracting can lock the interface to a fixed theme (e.g. permanent *Calm* or *Neutral*). This preference is stored in `user_preferences.pinned_theme`. When set, the `ThemeProvider` ignores active mood updates and retains the user's selected palette.

### 8.6 Accessibility & Contrast Compliance (WCAG AA)
*   **Contrast Ratio Verification:** Every color pairing between `--text-primary` and `--surface` meets or exceeds the **WCAG 2.1 AA** standard for normal text (minimum contrast ratio of 4.5:1).
*   **Reduced Motion Directives:** If the user enables `prefers-reduced-motion` in their operating system or toggles `reduced_motion` in `user_preferences`:
    *   Transition durations are forced to `0ms`.
    *   `--animation-intensity` falls back to `none`.
    *   Breathing exercise visualizers replace continuous scaling animations with static numerical step indicators.

### 8.7 Default State
Upon initial user registration—prior to their first mood check-in—the interface renders using the **Neutral** palette. This provides an objective, clean, and welcoming canvas that avoids presuming the user's emotional state.

---

## 9. Security and Privacy

### 9.1 Password Hashing
User passwords are encrypted using **bcrypt** with a work factor (salt rounds) of **12**. This configuration provides robust resistance against brute-force and rainbow-table attacks while keeping server verification latency under 250ms on standard cloud runtimes. Plaintext passwords are never logged, cached, or transmitted past the authentication service boundary.

### 9.2 JWT Authentication & Rotation Strategy
Authentication relies on a dual-token architecture:
*   **Access Token:** Signed using HMAC-SHA256 (`HS256`) with a short expiration of **15 minutes**. Carries minimal identity claims (`userId`, `email`). Stored **strictly in memory** within the client application; never written to `localStorage` or `sessionStorage`.
*   **Refresh Token:** Cryptographically random 256-bit token with a **7-day expiration**. Stored in an encrypted `httpOnly`, `Secure`, `SameSite=Strict` cookie path-restricted to `/api/auth`.
*   **Rotation on Use:** Each call to `/api/auth/refresh` invalidates the presented refresh token and issues a new token pair. If a previously invalidated refresh token is reused, the auth service revokes all active tokens for that user account.

### 9.3 Session Invalidation
Users can invalidate all active sessions by selecting "Log Out of All Devices" in their account settings. This action increments a `token_version` counter on the `users` table, which immediately invalidates all outstanding access and refresh tokens.

### 9.4 Input Sanitisation and Boundary Validation
Every incoming HTTP request body, path parameter, and query string is validated at the API boundary using **Zod** schemas. 
*   Unexpected or superfluous fields are stripped automatically.
*   Strings are trimmed and sanitized against SQL and shell injection patterns.
*   Zod validation errors return structured `422 Unprocessable Content` responses detailing specific field validation failures.

### 9.5 XSS (Cross-Site Scripting) Prevention
*   React's default JSX syntax escapes dynamic variables before rendering them to the DOM.
*   User-submitted markdown content within the Journal and AI companion views is parsed using `react-markdown` passed through **DOMPurify** with strict element whitelisting, preventing malicious script or iframe injection.
*   The API emits a strict `Content-Security-Policy` header:
    ```
    default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data: https:; connect-src 'self' https://api.openai.com;
    ```

### 9.6 CSRF (Cross-Site Request Forgery) Protection
Because the short-lived access token is stored in memory and attached via the `Authorization: Bearer` header, standard REST endpoints are immune to traditional browser-based CSRF attacks. The refresh endpoint (`/api/auth/refresh`) is protected by setting the cookie's `SameSite=Strict` flag, preventing cross-site transmission during third-party cross-origin requests.

### 9.7 Rate Limiting Strategy
The Hono backend implements an in-memory sliding-window rate limiter:
*   **Authentication Endpoints (`/api/auth/*`):** Capped at 5 requests per minute per IP to prevent brute-force attacks.
*   **AI Streaming Endpoints (`/api/conversations/*/messages`):** Capped at 30 requests per rolling hour per authenticated user.
*   **General API Endpoints:** Capped at 100 requests per minute per authenticated user.
Requests exceeding these thresholds receive a `429 Too Many Requests` response containing a `Retry-After` header.

### 9.8 API Key Protection
External LLM provider credentials (such as `OPENAI_API_KEY`) are stored strictly in server-side environment variables and are never bundled into frontend assets. All LLM calls are proxied through the backend AI Orchestration Service.

### 9.9 Row-Level Tenant Data Isolation
To ensure absolute data isolation, all database queries executed by Drizzle repositories must include an explicit user ID predicate:

```typescript
// Enforced repository pattern in solace+
export async function getJournalEntryById(db: Database, userId: string, entryId: string) {
  return await db.query.journalEntries.findFirst({
    where: and(
      eq(journalEntries.id, entryId),
      eq(journalEntries.userId, userId), // Mandatory tenant boundary check
      isNull(journalEntries.deletedAt)
    )
  });
}
```

Direct queries lacking a `userId` filter are rejected in code review and flagged by static analysis.

### 9.10 Encryption in Transit and at Rest
*   **In Transit:** All external and internal communications require **TLS 1.3** (or TLS 1.2 minimum). Plain HTTP connections are permanently redirected to HTTPS with HTTP Strict Transport Security (HSTS) headers enabled.
*   **At Rest:** Neon PostgreSQL databases are encrypted at rest using AES-256 storage volume encryption. Cloudflare R2 backup storage enforces server-side AES-256 encryption.

### 9.11 Sensitive Field Handling
Journal entries and companion chat records reflect intimate personal thoughts. These are stored in relational tables isolated by `user_id`. They are never passed to external third parties, used for ad targeting, or employed to train public machine learning models.

### 9.12 Data Export (GDPR Compliance)
Under GDPR and CCPA principles, users have the right to data portability. The endpoint `GET /api/users/export` assembles all records linked to the user's `userId` (user profile, preferences, mood history, journal entries, conversation transcripts, and regulation sessions) into a single, downloadable JSON archive.

### 9.13 Account Deletion (Right to Be Forgotten)
When a user selects "Delete Account", the system prompts for password re-authentication. Upon verification, the backend issues an explicit `DELETE FROM users WHERE id = :userId`. Database foreign keys enforce `ON DELETE CASCADE`, permanently deleting all associated mood logs, journal entries, conversations, and settings.

### 9.14 Consent Management
AI personalization features are **opt-in by default**. During onboarding, users are explicitly asked whether solace AI may reference recent mood entries and journal summaries to personalize conversational context. This setting is stored in `user_preferences.ai_personalisation_opt_in` and can be toggled off at any time, immediately clearing all contextual history injection from the prompt pipeline.

---

## 10. API Design

### 10.1 Endpoint Catalog Overview
All API routes are prefixed with `/api` and return standardized JSON responses or Server-Sent Event text streams.

| Group | Method | Path | Auth Required | Description |
| :--- | :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/api/auth/register` | No | Registers new account; returns user profile and sets refresh cookie |
| | `POST` | `/api/auth/login` | No | Authenticates credentials; returns access token & sets refresh cookie |
| | `POST` | `/api/auth/refresh` | No (Cookie) | Rotates refresh token cookie; returns fresh access token |
| | `POST` | `/api/auth/logout` | Yes | Clears refresh cookie and revokes session |
| **Users** | `GET` | `/api/users/profile` | Yes | Fetches authenticated user profile |
| | `PATCH`| `/api/users/profile` | Yes | Updates display name or profile details |
| | `DELETE`| `/api/users/account` | Yes | Permanently cascades deletion of user account and data |
| | `GET` | `/api/users/export` | Yes | Generates and downloads complete GDPR data export (JSON) |
| **Preferences**| `GET` | `/api/preferences` | Yes | Retrieves user interface and AI privacy preferences |
| | `PATCH`| `/api/preferences` | Yes | Updates preferences (pinned theme, reduced motion, AI opt-in) |
| **Moods** | `POST` | `/api/moods` | Yes | Creates new mood check-in with valence, intensity, and tags |
| | `GET` | `/api/moods` | Yes | Returns paginated mood history with filtering options |
| | `GET` | `/api/moods/:id` | Yes | Retrieves a specific mood check-in record |
| | `DELETE`| `/api/moods/:id` | Yes | Deletes an individual mood check-in record |
| **Analytics**| `GET` | `/api/moods/analytics/distribution`| Yes | Returns mood taxonomy percentage breakdown over time window |
| | `GET` | `/api/moods/analytics/trends` | Yes | Returns day-bucketed valence and intensity trend curve data |
| | `GET` | `/api/moods/analytics/calendar` | Yes | Returns daily aggregated mood data for calendar heatmaps |
| **Journal** | `GET` | `/api/journal` | Yes | Returns list of journal entries (excluding soft-deleted) |
| | `POST` | `/api/journal` | Yes | Creates a new journal entry |
| | `GET` | `/api/journal/:id` | Yes | Retrieves a specific journal entry |
| | `PATCH`| `/api/journal/:id` | Yes | Updates an existing journal entry |
| | `DELETE`| `/api/journal/:id` | Yes | Soft-deletes a journal entry |
| **Conversations**| `GET`| `/api/conversations` | Yes | Lists all active conversation threads |
| | `POST` | `/api/conversations` | Yes | Initiates a new conversation thread |
| | `GET` | `/api/conversations/:id` | Yes | Retrieves conversation metadata and message history |
| | `DELETE`| `/api/conversations/:id` | Yes | Deletes a conversation thread and all associated messages |
| **Messages** | `GET` | `/api/conversations/:id/messages` | Yes | Returns paginated message turns for a conversation |
| | `POST` | `/api/conversations/:id/messages` | Yes | Dispatches user message and opens an SSE stream for completion |
| **Regulation** | `GET` | `/api/regulation/activities` | Yes | Lists available grounding, mindfulness, and breathing exercises |
| | `POST` | `/api/regulation/sessions` | Yes | Records commencement of a regulation exercise session |
| | `PATCH`| `/api/regulation/sessions/:id/complete`| Yes | Marks an exercise session as completed with duration metrics |
| **Insights** | `GET` | `/api/insights` | Yes | Retrieves system-generated personal insights and patterns |

---

### 10.2 Detailed Request & Response Payloads

#### 1. POST `/api/moods` (Create Mood Entry)
Records an emotional check-in with valence, intensity, and associated situational tags.

**Request Payload:**
```json
{
  "mood": "Anxious",
  "valence": 3,
  "intensity": 7,
  "note": "Facing back-to-back engineering project deadlines and feel a tightening in my chest.",
  "tags": [
    "Work",
    "Deadlines",
    "Fatigue"
  ]
}
```

**Response Payload (`201 Created`):**
```json
{
  "success": true,
  "data": {
    "id": "e4b2d3c1-7f8a-4c22-b9e3-8a9d12345678",
    "userId": "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d",
    "mood": "Anxious",
    "valence": 3,
    "intensity": 7,
    "note": "Facing back-to-back engineering project deadlines and feel a tightening in my chest.",
    "tags": [
      { "id": "1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d", "name": "Work" },
      { "id": "2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e", "name": "Deadlines" },
      { "id": "3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f", "name": "Fatigue" }
    ],
    "suggestedTheme": {
      "mood": "Anxious",
      "ambientColor": "#ffedd5",
      "primary": "#c2410c"
    },
    "createdAt": "2026-11-04T08:30:00.000Z"
  }
}
```

---

#### 2. POST `/api/conversations/:id/messages` (Send Message & Stream Response)
Sends a user message into an existing conversation thread and opens a Server-Sent Events (SSE) stream returning generated tokens.

**Request Payload:**
```json
{
  "content": "I'm having a really hard time focusing today. My thoughts are racing about everything I need to get done.",
  "regulationMode": "CALM"
}
```

**Response Payload (`200 OK`, `Content-Type: text/event-stream`):**
```
event: open
data: {"conversationId":"f7a1b2c3-d4e5-4a6b-8c9d-0e1f2a3b4c5d","messageId":"8c9d0e1f-2a3b-4c5d-6e7f-8a9b0c1d2e3f"}

event: message
data: {"chunk":"I "}

event: message
data: {"chunk":"hear "}

event: message
data: {"chunk":"how "}

event: message
data: {"chunk":"loud "}

event: message
data: {"chunk":"everything "}

event: message
data: {"chunk":"feels "}

event: message
data: {"chunk":"right "}

event: message
data: {"chunk":"now. "}

event: message
data: {"chunk":"Let's "}

event: message
data: {"chunk":"pause "}

event: message
data: {"chunk":"for "}

event: message
data: {"chunk":"a "}

event: message
data: {"chunk":"moment "}

event: message
data: {"chunk":"together. "}

event: message
data: {"chunk":"Can "}

event: message
data: {"chunk":"you "}

event: message
data: {"chunk":"feel "}

event: message
data: {"chunk":"the "}

event: message
data: {"chunk":"ground "}

event: message
data: {"chunk":"beneath "}

event: message
data: {"chunk":"your "}

event: message
data: {"chunk":"feet?"}

event: done
data: {"tokensUsed":48,"completedAt":"2026-11-04T08:31:12.450Z"}
```

---

#### 3. GET `/api/moods/analytics/trends` (Historical Trend Data)
Returns time-series aggregations, moving averages, and emotional trajectories for a requested date range.

**Request Query Parameters:**
`GET /api/moods/analytics/trends?startDate=2026-10-01&endDate=2026-10-31&interval=day`

**Response Payload (`200 OK`):**
```json
{
  "success": true,
  "data": {
    "timeRange": {
      "startDate": "2026-10-01T00:00:00.000Z",
      "endDate": "2026-10-31T23:59:59.999Z",
      "totalCheckIns": 42
    },
    "summaryMetrics": {
      "averageValence": 6.4,
      "averageIntensity": 5.1,
      "dominantMood": "Calm",
      "stabilityScore": 7.8
    },
    "dataPoints": [
      {
        "date": "2026-10-01",
        "averageValence": 4.5,
        "averageIntensity": 7.0,
        "dominantMood": "Anxious",
        "checkInCount": 2
      },
      {
        "date": "2026-10-02",
        "averageValence": 7.0,
        "averageIntensity": 4.0,
        "dominantMood": "Calm",
        "checkInCount": 1
      },
      {
        "date": "2026-10-03",
        "averageValence": 8.5,
        "averageIntensity": 6.5,
        "dominantMood": "Happy",
        "checkInCount": 3
      },
      {
        "date": "2026-10-04",
        "averageValence": 3.0,
        "averageIntensity": 8.0,
        "dominantMood": "Overwhelmed",
        "checkInCount": 2
      }
    ]
  }
}
```

---

## 11. Deployment Architecture

### 11.1 Deployment Topology
The platform is deployed using an edge-optimized setup designed for high availability and zero operational overhead during the MVP phase.

```mermaid
flowchart TD
    subgraph VCS ["Source Control (GitHub)"]
        Repo["GitHub Repository (jayeshyadav/Solace+)"]
        Actions["GitHub Actions CI/CD Pipeline"]
    end

    subgraph StaticEdge ["Frontend Static Hosting (Netlify CDN)"]
        NetlifyEdge["Netlify Global CDN Edge"]
        SPABundle["React 18 SPA Production Bundle (/dist)"]
    end

    subgraph AppHost ["Backend Runtime (Railway / Render)"]
        ServiceContainer["Node.js LTS Container (Hono API Engine)"]
    end

    subgraph ServerlessDB ["Database Tier (Neon PostgreSQL)"]
        NeonCompute["Neon Serverless Compute (Postgres 16)"]
        StorageEngine[("Auto-scaling Distributed Storage")]
    end

    subgraph ExternalCloud ["Cloud Services"]
        OpenAI["OpenAI API Platform"]
        R2Storage[("Cloudflare R2 Object Storage")]
    end

    Repo -->|"Push to main"| Actions
    Actions -->|"Build & Deploy Static"| NetlifyEdge
    Actions -->|"Build & Deploy API"| ServiceContainer
    
    NetlifyEdge --- SPABundle
    SPABundle -->|"HTTPS Client Traffic"| ServiceContainer
    
    ServiceContainer -->|"Encrypted SQL Wire Protocol"| NeonCompute
    NeonCompute --- StorageEngine
    ServiceContainer -->|"Outbound HTTPS"| OpenAI
    ServiceContainer -->|"S3 API HTTPS"| R2Storage
```

### 11.2 Environment Configurations
*   **Development:** Vite development server runs on `http://localhost:5173`. Hono API server executes via `tsx watch` on `http://localhost:3001`. A local PostgreSQL container (or cloud Neon branch) handles persistence.
*   **Production:** The frontend is statically hosted on Netlify across global edge nodes. The backend runs as a continuous Node.js service on Railway. The database operates on Neon Serverless Postgres with automatic connection pooling.

### 11.3 Environment Variables Specification

| Variable Name | Execution Scope | Description | Canonical Example |
| :--- | :--- | :--- | :--- |
| `NODE_ENV` | Backend | Runtime execution environment | `production` |
| `PORT` | Backend | HTTP listening port | `3001` |
| `DATABASE_URL` | Backend | Pooled PostgreSQL connection string | `postgresql://user:pass@ep-xyz-pooler.neon.tech/solace?sslmode=require` |
| `DIRECT_DATABASE_URL`| Backend (Migrations)| Direct non-pooled PostgreSQL URL | `postgresql://user:pass@ep-xyz.neon.tech/solace?sslmode=require` |
| `JWT_ACCESS_SECRET` | Backend | Cryptographic secret for signing access tokens | `b3f81e...64_hex_chars_minimum...` |
| `JWT_REFRESH_SECRET`| Backend | Cryptographic secret for signing refresh tokens | `a9c40d...64_hex_chars_minimum...` |
| `CORS_ORIGIN` | Backend | Allowed frontend origin URL | `https://solaceplus.netlify.app` |
| `OPENAI_API_KEY` | Backend | OpenAI API authorization key | `sk-proj-xxxxxxxxxxxxxxxxxxxx` |
| `AI_PROVIDER_DEFAULT`| Backend | Active AI provider key | `openai` |
| `CLOUDFLARE_R2_ACCOUNT_ID`| Backend | Cloudflare account identifier | `4a8b1c2d3e4f5a6b7c8d9e0f` |
| `CLOUDFLARE_R2_ACCESS_KEY`| Backend | Cloudflare R2 S3 access key ID | `9f8e7d6c5b4a3a2b1c` |
| `CLOUDFLARE_R2_SECRET_KEY`| Backend | Cloudflare R2 S3 secret access key | `xyz123secretkeyabc456` |
| `CLOUDFLARE_R2_BUCKET_NAME`| Backend | Target bucket name for exports | `solace-user-data-exports` |
| `VITE_API_BASE_URL` | Frontend | Base URL targeting the backend API | `https://api-production.up.railway.app` |

### 11.4 Build Process
*   **Frontend Compilation:**
    1.  `npm run typecheck` (`tsc --noEmit`) validates type integrity across the codebase.
    2.  `vite build` processes TypeScript and JSX, applies the `@tailwindcss/vite` transformation engine, strips development assertions, and generates minified static assets in `dist/`.
*   **Backend Compilation:**
    1.  `tsc --noEmit` verifies strict type checking across all routes, services, and repositories.
    2.  `esbuild` bundles the Hono application and dependencies into a single, optimized Node.js script (`dist/index.js`), minimizing startup latency on cold boots.

### 11.5 GitHub Actions CI/CD Pipeline
Continuous integration and continuous deployment are orchestrated via a multi-stage GitHub Actions workflow defined in `.github/workflows/deploy.yml`:
1.  **Lint & Static Analysis:** Runs ESLint and Prettier across all workspaces to enforce code quality standards.
2.  **Typecheck:** Executes `tsc --noEmit` on both frontend and backend configurations.
3.  **Automated Unit & Integration Testing:** Executes test suites using Vitest, validating schema conversions, rate limiters, and theme calculations.
4.  **Database Migration Check:** Runs `drizzle-kit check` against migration files to prevent schema drift.
5.  **Build & Deployment:**
    *   Triggers Netlify CLI to deploy the frontend bundle to the production CDN.
    *   Triggers Railway CLI / Webhook to pull the latest image, execute database migrations via `drizzle-kit migrate`, and restart the backend application service cleanly.

---

## 12. Architecture Decision Records (ADRs)

---

### ADR-001: Hono over Express for Backend API Runtime
*   **Status:** Accepted
*   **Context:** Express.js has long been the default HTTP framework for Node.js. However, its architecture dates back to 2010. It lacks native TypeScript integration, does not rely on modern Web Standards (`Request`/`Response`), features poor built-in support for streaming responses, and carries significant legacy overhead. solace+ requires a lightweight, fast framework that supports Server-Sent Events natively for AI token streaming.
*   **Decision:** Adopt **Hono** as the core backend API framework running on Node.js.
*   **Alternatives Considered:** Express.js, Fastify, NestJS.
*   **Rationale:** Hono is built from the ground up on modern Web Standards. It provides sub-millisecond routing, native helper utilities for Server-Sent Events (`streamText`), first-class TypeScript support, and zero external runtime dependencies. Additionally, Hono is edge-ready: if traffic patterns warrant moving to Cloudflare Workers or AWS Lambda@Edge in the future, the code can be ported with minimal refactoring.
*   **Trade-offs:** Hono's middleware ecosystem is newer and smaller than Express's expansive plugin directory. Some community packages designed for Express require lightweight custom wrapper adapters.
*   **Consequences:** The codebase benefits from end-to-end type safety, lower memory consumption, and clean streaming abstractions for solace AI.

---

### ADR-002: Drizzle ORM over Prisma
*   **Status:** Accepted
*   **Context:** The application requires a type-safe relational data access layer targeting PostgreSQL on Neon. Prisma is a common default in the Node.js ecosystem, but it introduces significant architectural tradeoffs, including a heavy background binary engine, substantial memory overhead, and performance penalties during complex joins.
*   **Decision:** Adopt **Drizzle ORM** paired with **Drizzle Kit** for schema migrations and data access.
*   **Alternatives Considered:** Prisma, Kysely, TypeORM, Raw pg-promise.
*   **Rationale:** Drizzle operates as a zero-overhead TypeScript query builder that mirrors native SQL syntax. It does not run an external Rust engine binary, resulting in faster cold starts and lower memory footprints—an essential advantage in serverless environments. Schema declarations in Drizzle act as pure TypeScript definitions, allowing direct derivation of Zod validation schemas using `drizzle-zod`.
*   **Trade-offs:** Drizzle requires developers to write queries that mirror SQL rather than hiding database mechanics behind abstract object graphs. Migrations require manual inspection when handling complex column alterations.
*   **Consequences:** Faster build and boot times, zero binary-wrapper issues, and precise control over emitted SQL queries and indexes.

---

### ADR-003: Self-Hosted JWT Authentication over Third-Party Auth Services
*   **Status:** Accepted
*   **Context:** Third-party identity platforms (e.g. Auth0, Clerk, Supabase Auth) offer turnkey authentication workflows, but they introduce hard external dependencies, ongoing subscription costs, and potential privacy complications when handling sensitive mental wellness applications.
*   **Decision:** Implement a **self-hosted JWT authentication engine** utilizing in-memory access tokens, rotating `httpOnly` refresh tokens stored in PostgreSQL, and bcrypt password hashing (cost factor 12).
*   **Alternatives Considered:** Clerk, Auth0, Supabase Auth, Firebase Authentication.
*   **Rationale:** Self-hosting ensures all user authentication credentials remain completely within our Neon PostgreSQL database boundary. It eliminates per-user monthly SaaS costs, provides full control over data residency and deletion cascades under GDPR, and avoids vendor lock-in.
*   **Trade-offs:** The engineering team is directly responsible for maintaining cryptographic security, implementing refresh token rotation correctly, managing session invalidation, and mitigating brute-force attacks via rate limiting.
*   **Consequences:** Complete data sovereignty and zero external auth vendor costs, balanced by the need for disciplined security maintenance.

---

### ADR-004: Zustand over Redux Toolkit or React Context for Global State
*   **Status:** Accepted
*   **Context:** solace+ features complex, high-frequency state synchronisation across themes, mood check-ins, journal drafts, and streaming AI tokens. React Context causes cascading re-renders across consumers whenever any value in the context object updates. Redux Toolkit provides predictable state management but introduces heavy ceremony and boilerplate.
*   **Decision:** Adopt **Zustand** as the primary state management library across the frontend application.
*   **Alternatives Considered:** Redux Toolkit, React Context, MobX, Jotai.
*   **Rationale:** Zustand features a lightweight (~1.2 KB) footprint, requires zero provider tree wrapping, and supports granular selector subscriptions that isolate component re-renders to only the exact state slices consumed. Furthermore, Zustand stores can be inspected, read, and modified outside the React component lifecycle (such as within our Axios/Fetch interceptor pipeline).
*   **Trade-offs:** Lacks the structured action middleware ecosystem of Redux, requiring disciplined architectural conventions to keep store actions modular and maintainable.
*   **Consequences:** Minimal boilerplate, clean and readable state slices, and optimal rendering performance during real-time token streaming and dynamic theme adjustments.

---

### ADR-005: Neon Serverless PostgreSQL over Supabase
*   **Status:** Accepted
*   **Context:** The persistence tier requires a scalable, production-grade PostgreSQL database. Both Neon and Supabase provide managed cloud PostgreSQL instances with generous free tiers suitable for MVP deployment.
*   **Decision:** Select **Neon PostgreSQL** as the core database provider.
*   **Alternatives Considered:** Supabase, AWS RDS Aurora Serverless, Self-hosted Docker Postgres on VPS.
*   **Rationale:** Neon focuses purely on database excellence: instant serverless branching for non-destructive migration testing, true scale-to-zero compute auto-suspension during idle periods, and a built-in PgBouncer connection pool. In contrast, Supabase bundles multiple ancillary services (Auth, Realtime, Storage) that overlap with our custom architectural choices (self-hosted Hono auth and Cloudflare R2).
*   **Trade-offs:** Supabase offers an integrated GUI dashboard with built-in table editors that are slightly more mature than Neon's web console.
*   **Consequences:** A decoupled, lean persistence tier dedicated strictly to SQL data operations without lock-in to proprietary platform primitives.

---

### ADR-006: OpenAI-Compatible Abstraction Layer for AI Companion
*   **Status:** Accepted
*   **Context:** The conversational companion ("solace AI") is a core value proposition of the platform. Coupling the application directly to the proprietary OpenAI Node.js SDK risks vendor lock-in, exposing the platform to pricing changes, service degradation, or regional restrictions.
*   **Decision:** Build an **OpenAI-compatible abstraction interface** (`AIProvider`) leveraging the **Vercel AI SDK**, defaulting to OpenAI GPT-4o while supporting alternative providers (OpenRouter, Anthropic proxies, local Ollama) via configuration.
*   **Alternatives Considered:** Direct integration with the official OpenAI SDK, LangChain, proprietary Anthropic SDK.
*   **Rationale:** Standardizing on an OpenAI-compatible wire interface allows switching between model providers by simply updating environment variables (`AI_PROVIDER_DEFAULT` and `OPENAI_API_BASE_URL`). The Vercel AI SDK provides clean primitives for stream piping, backpressure handling, and unified error handling across vendors.
*   **Trade-offs:** Provider-specific prompt features (e.g., Anthropic prompt caching primitives) require translation layers or fallbacks within the abstraction.
*   **Consequences:** High operational resilience, zero vendor lock-in, and flexibility to transition to cost-effective open-weights models (e.g. Llama 3) as traffic scales.

---

### ADR-007: Framer Motion for Atmospheric Transitions and Guided Exercises
*   **Status:** Accepted
*   **Context:** solace+ relies on organic, calming visual feedback. The mood regulation module requires precise visual pacing for somatic exercises (such as 4-7-8 breathing cycles), while the sanctuary theme engine requires smooth color cross-fading.
*   **Decision:** Standardize on **Framer Motion** for all complex micro-interactions, layout morphs, and pacing animations across the frontend.
*   **Alternatives Considered:** Native CSS keyframe animations, GSAP, React Spring.
*   **Rationale:** Framer Motion provides a declarative API that integrates naturally with React component lifecycles. It offers physics-based spring models that avoid the mechanical stiffness of linear CSS transitions, supports layout exit animations via `AnimatePresence`, and honors the `prefers-reduced-motion` media query out of the box.
*   **Trade-offs:** Adds approximately 30 KB (gzipped) to the frontend bundle.
*   **Consequences:** High-fidelity micro-interactions and smooth kinetic pacing for regulation tools, with bundle impact mitigated through route-level code splitting.

---

### ADR-008: Monorepo-Lite Repository Structure
*   **Status:** Accepted
*   **Context:** Developing separate repositories for the frontend client and backend API introduces operational overhead, coordination challenges for cross-cutting features, and difficulty synchronizing shared TypeScript contracts. However, full-blown monorepo frameworks (such as Turborepo or Nx) introduce configuration complexity that can slow down MVP velocity.
*   **Decision:** Adopt a **Monorepo-Lite structure**: a single unified Git repository hosting `client/` and `server/` root directories, sharing TypeScript definitions directly without complex build orchestrators.
*   **Alternatives Considered:** Completely isolated repositories, Turborepo monorepo, Nx monorepo.
*   **Rationale:** A lightweight monorepo allows atomic commits across frontend views and backend endpoints, keeps pull requests cohesive, and simplifies local development using simple npm workspace scripts. It avoids the learning curve and caching configuration challenges of enterprise monorepo tooling during early-stage product evolution.
*   **Trade-offs:** Requires separate CI/CD trigger paths to ensure frontend asset changes do not trigger redundant backend deployments.
*   **Consequences:** Streamlined developer workflow, simplified schema synchronization, and lower operational complexity for the engineering team.

---

## 13. System Evolution & Roadmap

As solace+ transitions from initial MVP deployment to broad availability, the following architectural milestones are planned:

1.  **Phase 1 (Post-MVP Hardening):**
    *   Implement client-side encryption for private journal entries using Web Crypto API (AES-GCM-256 with user-derived keys), ensuring zero-knowledge privacy where even the database administrator cannot inspect personal journal text.
    *   Transition in-memory sliding-window rate limiters to a distributed Redis / Upstash instance to support multi-instance backend autoscaling.
2.  **Phase 2 (Expanded Ecosystem & Edge Migration):**
    *   Package the React frontend as an offline-first Progressive Web App (PWA) with Service Worker caching and background sync for offline mood logging.
    *   Evaluate migrating Hono API routing to Cloudflare Workers, moving compute closer to global users and reducing cold starts to under 10ms.
3.  **Phase 3 (Multimodal & Local Intelligence):**
    *   Deploy local browser-based sentiment and mood embedding models via WebAssembly / ONNX Runtime to perform on-device trigger analysis without sending journal drafts over the network.
    *   Introduce real-time ambient sound generation using the Web Audio API, synthetically rendering generative soundscapes tuned to the active mood palette.

---

*End of Architecture Document. Solace+ Architecture Specification v1.0.0.*
