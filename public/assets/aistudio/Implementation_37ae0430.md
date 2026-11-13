# Implementation.md: Technical Implementation Blueprint for solace+

This document defines the technical architecture, development standards, subsystem designs, and implementation roadmap for **solace+**, an AI-powered emotional wellness and mood tracking platform. It serves as the authoritative blueprint for engineering teams and autonomous coding agents implementing the system.

---

## 1. Development Principles

The solace+ platform adheres to ten foundational engineering principles. These tenets govern architectural decisions, code reviews, and integration milestones across all tiers.

1. **Type Safety First**  
   TypeScript strict mode (`strict: true`, `noImplicitAny: true`, `noUncheckedIndexedAccess: true`) is enforced across all packages without exception. Zod serves as the runtime gatekeeper at every network and persistence boundary, deriving compile-time types directly from schemas to ensure that runtime payloads and static types never diverge.

2. **Feature-Based Module Organisation**  
   Code is structured into cohesive vertical slices organised around domain capabilities (such as `auth`, `mood`, `journal`, `companion`, `insights`, and `settings`) rather than horizontal technical layers. This architecture localises domain reasoning, isolates change vectors, and minimises merge contention across multidisciplinary teams.

3. **Strict Separation of Concerns**  
   Every feature module enforces a unidirectional three-tier architecture: HTTP controllers handle request negotiation and response formatting; domain services orchestrate business invariants and cross-module workflows; and repositories encapsulate database access and query construction. Database connections, SQL entities, and HTTP contexts never leak beyond their designated tiers.

4. **Single Source of Truth**  
   Canonical definitions exist in exactly one location across the repository: design tokens govern visual styling across Tailwind CSS and CSS custom properties; Drizzle ORM schemas dictate relational persistence; and shared Zod schemas define API exchange contracts. Changes propagate deterministically from canonical declarations, eliminating manual duplication.

5. **Test-Before-Merge Discipline**  
   No feature pull request may be merged into the `main` branch without passing automated unit, integration, and component suites in continuous integration. Tests serve as executable specifications of domain behavior; untested code is regarded as incomplete by definition.

6. **Documentation-First Engineering**  
   Every public function, custom hook, service method, and repository query must carry complete JSDoc annotations documenting parameters, return contracts, and failure modes. Every feature module contains a dedicated `README.md` defining its domain boundaries, external dependencies, and state transitions prior to code implementation.

7. **Incremental Integration**  
   System features are implemented and integrated through strict vertical phases. Each layer—from foundational schemas through backend services to user interface components—must be fully validated against automated test suites before downstream consumers begin implementation, preventing compounding technical debt.

8. **Fail Loudly in Development, Fail Gracefully in Production**  
   During local development, invariant violations, schema mismatches, and unhandled edge cases must trigger immediate assertion failures, verbose error overlays, and process terminations. In production environments, the system must intercept exceptions, log contextualised telemetry without user data, and present resilient, empathetic recovery interfaces.

9. **Privacy and Confidentiality by Design**  
   User emotional disclosures, journal reflections, and AI conversation histories constitute sensitive mental health data. Application logs strictly redact personal data; database records are strictly partitioned by authenticated user identifier; and data retention controls support complete, auditable user-directed export and deletion.

10. **Accessibility as a Non-Negotiable Baseline**  
    Compliance with Web Content Accessibility Guidelines (WCAG) 2.1 AA is an intrinsic architectural criterion, not an afterthought. Every interactive element must support complete keyboard navigation, explicit ARIA accessibility tree semantics, screen reader announcements, and automated axe-core validation before passing code review.

---

## 2. Proposed Repository Structure

The solace+ platform is organised as a lightweight monorepo powered by `pnpm` workspaces. This structure enables zero-overhead code sharing across the React frontend and Hono backend while avoiding the operational friction of enterprise monorepo tooling.

```
solace+/
├── .github/
│   └── workflows/
│       ├── ci.yml                    # Automated lint, typecheck, and test pipeline
│       ├── deploy-api.yml            # Railway/Render backend deployment
│       └── deploy-web.yml            # Netlify frontend deployment
├── apps/
│   ├── web/                          # React 18 client application
│   │   ├── public/
│   │   │   ├── favicon.ico
│   │   │   └── robots.txt
│   │   ├── src/
│   │   │   ├── features/             # Feature-based domain modules
│   │   │   │   ├── auth/
│   │   │   │   │   ├── components/   # LoginForm, RegisterForm, AuthGuard
│   │   │   │   │   ├── hooks/        # useAuth, useSession
│   │   │   │   │   ├── services/     # authApi client
│   │   │   │   │   └── types/        # Auth view types
│   │   │   │   ├── mood/
│   │   │   │   │   ├── components/   # MoodSelector, IntensitySlider, MoodCard
│   │   │   │   │   ├── hooks/        # useMoodHistory, useDailyCheckIn
│   │   │   │   │   ├── services/     # moodApi client
│   │   │   │   │   └── types/        # Mood UI models
│   │   │   │   ├── journal/
│   │   │   │   │   ├── components/   # JournalEditor, EntryList, TagFilter
│   │   │   │   │   ├── hooks/        # useJournalEntries, useAutosave
│   │   │   │   │   └── services/     # journalApi client
│   │   │   │   ├── companion/
│   │   │   │   │   ├── components/   # ChatMessage, ModeSelector, StreamingBubble
│   │   │   │   │   ├── hooks/        # useCompanionStream, useRegulationMode
│   │   │   │   │   └── services/     # companionApi client
│   │   │   │   ├── insights/
│   │   │   │   │   ├── components/   # MoodTrendChart, TagDistribution, CalendarHeatmap
│   │   │   │   │   ├── hooks/        # useAnalyticsRange, useMoodCorrelations
│   │   │   │   │   └── services/     # insightsApi client
│   │   │   │   └── settings/
│   │   │   │       ├── components/   # ProfileForm, ThemePreferences, DataExport
│   │   │   │       └── hooks/        # useUserSettings
│   │   │   ├── shared/               # Cross-cutting reusable assets
│   │   │   │   ├── components/       # Button, Card, Dialog, Input, Slider, Toast
│   │   │   │   ├── hooks/            # useDebounce, useMediaQuery, useReducedMotion
│   │   │   │   ├── stores/           # Zustand global state slices
│   │   │   │   ├── theme/            # Theme tokens, ThemeProvider, AmbientBackground
│   │   │   │   └── utils/            # Date formatting, class merging (cn), sanitisation
│   │   │   ├── layouts/
│   │   │   │   ├── RootLayout.tsx    # Global providers and ambient canvas
│   │   │   │   ├── AppLayout.tsx     # Authenticated shell with sidebar navigation
│   │   │   │   ├── AuthLayout.tsx    # Focused split card layout for auth flows
│   │   │   │   └── OnboardingLayout.tsx # Stepped baseline configuration layout
│   │   │   ├── pages/                # Route entry pages
│   │   │   │   ├── HomePage.tsx
│   │   │   │   ├── CheckInPage.tsx
│   │   │   │   ├── CompanionPage.tsx
│   │   │   │   ├── JournalPage.tsx
│   │   │   │   ├── InsightsPage.tsx
│   │   │   │   ├── SettingsPage.tsx
│   │   │   │   └── NotFoundPage.tsx
│   │   │   ├── router/
│   │   │   │   ├── index.tsx         # createBrowserRouter definitions
│   │   │   │   └── routes.ts         # Path constants
│   │   │   ├── styles/
│   │   │   │   └── globals.css       # Tailwind v4 directives and CSS token vars
│   │   │   ├── env.d.ts              # Vite environment variable typings
│   │   │   ├── index.html
│   │   │   ├── main.tsx              # Application entry point
│   │   │   ├── vite.config.ts        # Vite configuration with Tailwind v4 & aliases
│   │   │   └── tsconfig.json
│   │   └── package.json
│   └── api/                          # Hono backend application
│       ├── src/
│       │   ├── modules/              # Vertical domain slices
│       │   │   ├── auth/             # Authentication & session rotation
│       │   │   ├── mood/             # Mood logging & aggregation
│       │   │   ├── journal/          # Journal entries & reflection tags
│       │   │   ├── companion/        # AI dialogue & streaming
│       │   │   ├── insights/         # Metrics computation & analytics
│       │   │   └── users/            # Account profile & preferences
│       │   ├── ai/                   # AI provider orchestration
│       │   │   ├── providers/        # OpenAI, mock, and fallback adapters
│       │   │   ├── prompts/          # Layered prompt templates & safety instructions
│       │   │   ├── context.ts        # Dynamic context assembler service
│       │   │   ├── guardrails.ts     # Crisis screening & content safety filters
│       │   │   └── types.ts          # AI provider interface contracts
│       │   ├── db/                   # Database access layer
│       │   │   ├── client.ts         # Neon/Postgres Drizzle connection singleton
│       │   │   ├── schema/           # Relational entity definitions
│       │   │   ├── migrations/       # Drizzle Kit generated SQL migrations
│       │   │   └── seed.ts           # Activity catalog and development seed script
│       │   ├── middleware/           # Cross-cutting HTTP middleware
│       │   │   ├── auth.ts           # JWT verification & context population
│       │   │   ├── cors.ts           # Cross-origin resource sharing configuration
│       │   │   ├── logger.ts         # Pino structured logging adapter
│       │   │   ├── rate-limit.ts     # IP and user quota rate limiters
│       │   │   └── error-handler.ts  # Global exception translation
│       │   ├── lib/                  # Backend utilities
│       │   │   ├── config.ts         # Zod validated environment configuration
│       │   │   ├── crypto.ts         # Bcrypt hashing & token helpers
│       │   │   └── errors.ts         # Domain exception class hierarchy
│       │   └── index.ts              # Server bootstrap and route mounting
│       ├── drizzle.config.ts         # Drizzle Kit migration configuration
│       └── tsconfig.json
├── packages/
│   └── shared-types/                 # Shared TypeScript contracts and schemas
│       ├── src/
│       │   ├── mood.ts               # Mood enums, schemas, and types
│       │   ├── journal.ts            # Journal validation schemas
│       │   ├── companion.ts          # AI conversation models & regulation modes
│       │   ├── insights.ts           # Analytics and aggregation data types
│       │   ├── auth.ts               # Login, register, and token payload schemas
│       │   ├── user.ts               # User preferences and profile models
│       │   └── index.ts              # Package re-exports
│       ├── package.json
│       └── tsconfig.json
├── docs/                             # Architecture and planning documentation
│   ├── Architecture.md
│   ├── Design.md
│   ├── Implementation.md
│   ├── Testing.md
│   └── Roadmap.md
├── .gitignore
├── .prettierrc
├── .eslintrc.cjs
├── package.json                      # Root workspace scripts and devDependencies
├── pnpm-workspace.yaml               # Workspace package directory declarations
├── pnpm-lock.yaml
└── turbo.json                        # Turborepo task pipeline configuration
```

### Directory Roles and Workspace Architecture

- **`apps/web`**: The single-page React frontend. Built with Vite and Tailwind CSS v4, it consumes backend APIs and manages reactive UI state via Zustand. It contains zero direct database logic and relies entirely on typed API client abstractions.
- **`apps/api`**: The backend HTTP service built on Hono. It implements domain controllers, business services, and Drizzle repositories. It executes on Node.js using `@hono/node-server` and communicates with Neon PostgreSQL over pooled connections.
- **`packages/shared-types`**: The single source of truth for domain data contracts. It publishes Zod validation schemas and their inferred TypeScript types. Both `web` and `api` import these definitions using workspace links (`workspace:*`), preventing schema divergence at compile time.
- **`docs/`**: The complete architectural, design, implementation, and operational documentation suite. All code changes must align with the specifications established in these living documents.
- **`.github/workflows`**: Declarative CI/CD pipelines defining pull request validation, linting, matrix testing, database migration validation, and automated deployment to Netlify and Railway.

### The Monorepo-Lite Philosophy

The repository adopts a "monorepo-lite" philosophy. By pairing `pnpm` workspaces with Turborepo, the project achieves clean dependency deduplication, fast symlinked cross-package resolution, and parallelised pipeline caching without the operational cognitive burden or configuration weight of heavy monorepo systems. Developers run a single command (`pnpm dev`) at the workspace root to orchestrate frontend, backend, and shared type compilation with hot module reloading across all boundaries.

---

## 3. Frontend Implementation Strategy

### 3.1 Project Setup
- **Build Engine & Template**: Initialised via Vite using the `react-ts` template, configured for ECMAScript 2022 target output and React 18 concurrent rendering features.
- **Tailwind CSS v4 Integration**: Integrated via the `@tailwindcss/vite` plugin. The design system leverages modern CSS theme definitions and CSS custom properties rather than legacy JavaScript-based configuration files, supporting dynamic runtime token switching.
- **Path Aliasing**: Configured in `vite.config.ts` and `tsconfig.json` using `@/` mapped to `apps/web/src/` and `@shared/` mapped to `packages/shared-types/src/`.
- **Environment Variables**: Managed via Vite's `import.meta.env` facility, strictly prefixed with `VITE_`. Typed globally via `src/env.d.ts` to ensure type-safe configuration access throughout the UI layer.
- **Static Analysis & Formatting**: ESLint configured with `@typescript-eslint/recommended-type-checked`, `eslint-plugin-react-hooks`, and `eslint-plugin-jsx-a11y`. Prettier enforces consistent code style across all markdown, JSON, CSS, and TypeScript files.
- **Strict TypeScript Profile**: Configured with `strict: true`, `noImplicitAny: true`, `noUncheckedIndexedAccess: true`, `exactOptionalPropertyTypes: true`, and `forceConsistentCasingInFileNames: true`.

### 3.2 Routing
- **Routing Engine**: Implemented using React Router v6 via `createBrowserRouter` and `RouterProvider`, enabling declarative route definitions and unified error boundaries.
- **Route Segments & Access Control**:
  - *Public Routes*: `/login`, `/register`, `/forgot-password`, `/terms`, `/privacy`.
  - *Protected Routes*: Enclosed within a route-level `<AuthGuard>` component verifying token existence and validity before rendering child routes (`/`, `/check-in`, `/companion`, `/journal`, `/insights`, `/settings`).
  - *Onboarding Routes*: Dedicated flow (`/onboarding`) rendering baseline configuration steps for newly registered accounts lacking initial mood or preference records.
- **Code Splitting & Suspense**: Every primary page component is asynchronously loaded using `React.lazy()`. Route transitions are wrapped in `<Suspense>` boundaries rendering a high-fidelity emotional skeleton loader matching the active layout.
- **Route-Level Error Boundaries**: Each route segment defines an `errorElement` property bound to a specialized `RouteErrorBoundary` to isolate runtime exceptions to specific views without crashing the global application shell.

### 3.3 Layouts
- **`RootLayout`**: The top-level component tree wrapper. It initialises the global `ThemeProvider`, `AuthProvider`, `ToastProvider`, and the persistent `AmbientBackground` canvas.
- **`AppLayout`**: The authenticated shell. Renders a responsive collateral navigation sidebar, top mobile header with quick mood indicator, a secondary regulation quick-launch bar, and the primary viewport content container with fluid scroll boundaries.
- **`AuthLayout`**: A focused split-pane layout designed for authentication. Features an animated emotional gradient canvas on one side and a minimalist, accessible form card on the other.
- **`OnboardingLayout`**: A distraction-free stepped shell presenting a linear progress bar, contextual reassurance banners, and animated step transitions.

### 3.4 State Management
Client state is managed using isolated Zustand stores, partitioned cleanly by domain concern:

- **`useAuthStore`**:
  - *State*: `user: UserProfile | null`, `accessToken: string | null`, `isAuthenticated: boolean`, `isLoading: boolean`.
  - *Actions*: `setTokens(access: string)`, `setUser(user: UserProfile)`, `logout()`, `initializeSession()`.
  - *Persistence*: Uses Zustand `persist` middleware to save `accessToken` and user identity securely in `localStorage`.
- **`useMoodStore`**:
  - *State*: `currentMood: MoodType | null`, `currentIntensity: number`, `todayEntry: MoodEntry | null`, `recentHistory: MoodEntry[]`, `isSubmitting: boolean`.
  - *Actions*: `setCurrentMood(mood, intensity)`, `setTodayEntry(entry)`, `fetchRecentHistory()`, `resetDailyCheckIn()`.
- **`useThemeStore`**:
  - *State*: `activeTheme: MoodType`, `pinnedTheme: MoodType | 'AUTO'`, `resolvedTokens: ThemeTokenSet`.
  - *Actions*: `setPinnedTheme(theme)`, `updateFromMood(mood: MoodType)`, `calculateTokens(mood: MoodType)`.
  - *Persistence*: Persists `pinnedTheme` preferences across sessions in `localStorage`.
- **`useCompanionStore`**:
  - *State*: `conversations: ConversationMeta[]`, `activeConversationId: string | null`, `messages: ChatMessage[]`, `regulationMode: RegulationMode`, `streamingState: 'IDLE' | 'STREAMING' | 'ERROR'`.
  - *Actions*: `setRegulationMode(mode)`, `appendUserMessage(text)`, `appendAssistantChunk(chunk)`, `finalizeStream()`, `loadConversation(id)`.
- **`useJournalStore`**:
  - *State*: `entries: JournalEntry[]`, `selectedEntry: JournalEntry | null`, `filters: JournalFilters`, `pagination: PaginationMeta`, `isSaving: boolean`.
  - *Actions*: `setFilter(filters)`, `saveEntry(entry)`, `deleteEntry(id)`, `fetchEntries(page)`.
- **`useInsightsStore`**:
  - *State*: `metrics: MoodAnalytics | null`, `dateRange: DateRangeSelection`, `isLoading: boolean`, `error: string | null`.
  - *Actions*: `setDateRange(range)`, `fetchAnalytics()`, `refreshCorrelations()`.

### 3.5 Theme System Implementation
The solace+ theme engine translates user emotional valence into an ambient, accessible visual aesthetic:

```mermaid
flowchart TD
    A["Mood Logged / Selected (e.g. JOYFUL, ANXIOUS)"] --> B["useThemeStore.updateFromMood()"]
    B --> C["Token Mapper resolves ThemeTokenSet"]
    C --> D["Inject CSS Custom Properties on document.documentElement"]
    D --> E["Tailwind v4 re-evaluates var(--background), var(--primary)"]
    D --> F["AmbientBackground Framer Motion orbs update velocity & colour"]
    E --> G["CSS transition: background-color 200ms, color 200ms executes"]
```

- **CSS Variable Injection**: Tokens are dynamically mapped from the active mood and written directly to `document.documentElement.style` as CSS custom properties (`--background`, `--foreground`, `--primary`, `--card`, `--border`, `--ring`, `--ambient-orb-1`, `--ambient-orb-2`, `--animation-intensity`).
- **Tailwind v4 Utility Alignment**: Tailwind classes directly reference the active variables (e.g., `bg-[var(--background)]`, `text-[var(--foreground)]`, `border-[var(--border)]`).
- **Visual Transition Timing**: Document-level style rules enforce smooth, non-jarring transitions across theme adjustments: `transition: background-color 200ms ease, color 200ms ease, border-color 200ms ease`.
- **`AmbientBackground` Component**: Renders full-viewport background canvas utilizing Framer Motion. Contains two fluid radial gradient orbs that gently drift across coordinates. Their trajectory, rotational speed, blur radius, and chromatic saturation are driven directly by `--animation-intensity` and the theme's ambient tokens.

### 3.6 API Client
- **HTTP Client Foundation**: Centralised Axios instance initialized with `baseURL: import.meta.env.VITE_API_URL` and `timeout: 10000`.
- **Request Interceptor**: Automatically inspects `useAuthStore` to inject `Authorization: Bearer <accessToken>` headers into every outgoing request.
- **Response & Refresh Interceptor**: Intercepts HTTP 401 Unauthorised responses. Utilises a promise mutex pattern to prevent duplicate refresh calls: pauses outgoing failed requests, invokes `/api/auth/refresh` using the secure HTTP-only refresh cookie or stored refresh token, updates `useAuthStore` with the new access token, and retries original requests transparently. If refresh fails, session state is cleared and the user is redirected to `/login`.
- **Typed Feature Modules**: Individual domain clients (`moodApi`, `journalApi`, `companionApi`, `insightsApi`) expose strongly typed methods consuming shared validation schemas.
- **Error Normalisation**: All API errors are intercepted and translated into a structured, typed `AppError` entity preserving status codes, field-level validation messages, and correlation IDs.

### 3.7 Form Handling
- **Integration**: React Hook Form paired with `@hookform/resolvers/zod` for declarative validation.
- **Contract Reusability**: Schemas are imported directly from `@shared/validators`, guaranteeing that client-side validation rules match backend validation constraints identically.
- **Custom Controlled Inputs**: Custom emotional controls such as `MoodSelector` (interactive 5-state valence picker) and `IntensitySlider` (1 to 10 logarithmic feeling scale) implement standard `Control` interfaces via React Hook Form's `useController`.
- **Error Messaging Conventions**: Field-level validation failures render inline alerts linked via `aria-describedby` and `aria-invalid` attributes. Form-level submission exceptions render an empathetic, dismissible banner at the top of the form container.

### 3.8 Charts
- **Visualisation Library**: Recharts chosen for its declarative React architecture, SVG-based rendering, and screen reader accessibility support.
- **Responsive Layout**: Every chart instance is enclosed in a `ResponsiveContainer` with fixed aspect ratios and fluid width calculations.
- **Themed Tooltips**: Tooltip containers are styled to match the active theme's card and border tokens, featuring custom formatting for emotional intensity and timestamps.
- **Key Chart Implementations**:
  - `LineChart`: Displays longitudinal mood trajectories with smoothed monotone cubic spline curves and reference threshold lines.
  - `BarChart`: Illustrates categorical emotional distributions across time windows.
  - `Calendar Heatmap`: Custom SVG grid rendering daily emotional valence as shaded cells, providing a longitudinal wellness snapshot.

### 3.9 Animations
- **Motion Engine**: Framer Motion orchestrates all page and component transitions.
- **Page Transitions**: Top-level route views are wrapped in `<AnimatePresence mode="wait">` to coordinate exit and entrance transitions cleanly without layout jumps.
- **Standardised Variants**: Motion properties are declared in central variant dictionaries:
  - `pageTransitionVariants`: Soft opacity fade combined with a subtle vertical translation (`y: 6` to `y: 0`).
  - `cardHoverVariants`: Subtle scale elevation (`scale: 1.015`) and shadow shift.
  - `modalVariants`: Backdrop fade combined with scale spring entrance (`scale: 0.95` to `scale: 1`).
- **Accessibility Safeguards**: The application consumes Framer Motion's `useReducedMotion()` hook. When the user's operating system requests reduced motion (`prefers-reduced-motion: reduce`), positional translations and scaling springs are replaced with instant or simple opacity dissolves, and background orb loops are completely paused.

### 3.10 Error Boundaries
- **Global Error Boundary**: Placed at the root of the component tree above React Router. Catches unhandled render failures, suppresses broken DOM trees, and displays an empathetic fallback view with options to reload the application or report the incident.
- **Route Error Boundaries**: Configured on individual route segments via React Router's `errorElement`. Keeps navigation and shell controls functional while allowing the user to retry loading the specific failed view.
- **Categorised Error Notification**:
  - *Transient Network Failures*: Surfaced via non-modal toast notifications with retry triggers.
  - *Form Validation Faults*: Rendered inline adjacent to invalid inputs.
  - *Fatal Module Exceptions*: Rendered as full-card illustrated fallback states within the affected container.

---

## 4. Backend Implementation Strategy

### 4.1 Server Setup
- **Framework & Runtime**: Built with Hono running on the Node.js runtime via `@hono/node-server`. This pairing provides ultra-low routing overhead, native TypeScript support, and full compatibility with Web Standard request/response primitives.
- **Global Middleware Pipeline**: The root Hono application registers:
  1. `cors()`: Cross-Origin Resource Sharing with whitelisted origins, exposed headers, and credentials support.
  2. `logger()`: Structured Pino logging adapter capturing request method, path, response status, and duration.
  3. `errorHandler()`: Unified exception handler intercepting all domain and unhandled runtime errors.
- **Environment Bootstrap**: Runtime configuration is loaded at startup using `dotenv` and validated against a strict Zod schema in `src/lib/config.ts`. The server immediately halts if required variables are missing or malformed.
- **Modular Mounting**: Feature routers are mounted declaratively in `src/index.ts` under the `/api` prefix:
  ```typescript
  // Conceptual server mount structure
  app.route('/api/auth', authRouter);
  app.route('/api/moods', moodRouter);
  app.route('/api/journal', journalRouter);
  app.route('/api/companion', companionRouter);
  app.route('/api/insights', insightsRouter);
  app.route('/api/users', usersRouter);
  ```

### 4.2 Module Organisation
Every backend domain module (`auth`, `mood`, `journal`, `companion`, `insights`, `users`) maintains an identical internal directory layout:

- **`routes.ts`**: Declares Hono route definitions, HTTP verbs, URL path segments, validation middleware bindings, and controller dispatch calls.
- **`controller.ts`**: Extracts request payloads, route parameters, and authenticated context; delegates work to the service layer; and formats outbound HTTP responses with appropriate status codes.
- **`service.ts`**: Encapsulates core business logic, invariant checks, authorization rules, and cross-module orchestration. Contains zero references to Hono contexts or HTTP primitives.
- **`repository.ts`**: Encapsulates all database operations, Drizzle query construction, and transactions. Never executes business validation.
- **`validators.ts`**: Declares Zod schemas for request body, query parameters, and URL path parameters, re-exporting shared definitions where applicable.
- **`types.ts`**: Declares module-specific internal interfaces and service response models.

### 4.3 Database Integration
- **Drizzle Client Singleton**: Initialised in `src/db/client.ts`. Configured with the Neon serverless PostgreSQL driver (`@neondatabase/serverless`) for production deployments, supporting connection pooling and low-latency query execution.
- **Schema Partitioning**: Relational tables, enums, relations, and indexes are defined in modular files within `src/db/schema/` (e.g., `users.ts`, `mood-entries.ts`, `journal-entries.ts`, `ai-conversations.ts`, `regulation-sessions.ts`).
- **Migration Workflows**:
  - *Local Development*: Rapid prototyping uses `drizzle-kit push` against local or ephemeral branch databases.
  - *Staging & Production*: Strict migration flow via `drizzle-kit generate` producing deterministic, sequential SQL migration files in `src/db/migrations/`, executed via `drizzle-kit migrate` in CI/CD deployment pipelines.
- **Atomic Transactions**: Multi-entity modifications (such as registering a user and initializing default preferences, or archiving messages during conversation summarisation) are executed inside atomic `db.transaction()` blocks.

### 4.4 Authentication
- **Password Hashing**: Implemented using `bcrypt` with a work factor cost of 12 rounds, ensuring robust resistance against brute-force attacks.
- **Token Strategy**:
  - *Access Token*: Short-lived (15 minutes), signed with `ACCESS_TOKEN_SECRET`. Encodes `userId`, `email`, and account status.
  - *Refresh Token*: Long-lived (7 days), signed with `REFRESH_TOKEN_SECRET`. Stored as a cryptographic hash in the `refresh_tokens` database table.
- **Refresh Token Rotation**: Each invocation of `/api/auth/refresh` invalidates the presented refresh token in the database, generates a cryptographically distinct new pair, and updates the persistence layer. Replay of an invalidated token triggers immediate revocation of all active sessions for that user account.
- **Authentication Endpoints**:
  - `POST /api/auth/register`: Validates inputs, hashes password, creates user record, initializes preferences, and issues tokens.
  - `POST /api/auth/login`: Authenticates credentials, generates token pair, records login timestamp.
  - `POST /api/auth/refresh`: Verifies rotation validity and issues replacement token pair.
  - `POST /api/auth/logout`: Revokes the current refresh token record in the database.
- **Authentication Middleware (`verifyJWT`)**: Extracts the `Bearer` token from the `Authorization` header, verifies cryptographic validity and expiration, extracts the subject `userId`, and populates the Hono context via `c.set('user', payload)`.

### 4.5 Validation
- **Request Boundary Validation**: Every endpoint binds Hono's `zValidator` middleware to validate request bodies, query strings, and path parameters prior to controller execution.
- **Rejection Formatting**: Validation failures immediately short-circuit request execution, returning an HTTP 422 Unprocessable Entity with a structured error payload detailing each failed field path and error message.
- **Input Sanitisation**: Free-text narrative fields (such as journal bodies and check-in notes) are processed through an HTML sanitiser to strip executable scripts, dangerous SVG entities, and malicious tags prior to persistence.

### 4.6 Service Layer
- **Architectural Rules**: Services own all domain business logic and data processing. Controllers are prohibited from querying repositories directly.
- **Cross-Service Orchestration**: Services may call other services to aggregate domain context (for example, `InsightsService` invokes `MoodService` and `JournalService` to generate comprehensive wellness correlations).
- **Asynchronous Execution & Typed Output**: All service methods are asynchronous, returning strictly typed domain models or throwing specialized domain exceptions.

### 4.7 Error Handling
- **Domain Exception Hierarchy**: All business logic exceptions inherit from a base `AppError` class declaring HTTP status codes, machine-readable error codes, and human-readable messages:
  - `ValidationError` (422)
  - `NotFoundError` (404)
  - `UnauthorisedError` (401)
  - `ForbiddenError` (403)
  - `ConflictError` (409)
  - `RateLimitError` (429)
  - `InternalError` (500)
- **Central Error Translation**: The global Hono error handler catches all rejected promises. If the error is an instance of `AppError`, it formats the corresponding JSON response. Unhandled exceptions are logged with stack traces internally, while returning a safe, generic HTTP 500 error payload to the client.
- **Standardized Error Envelope**:
  ```json
  {
    "success": false,
    "error": {
      "code": "RESOURCE_NOT_FOUND",
      "message": "The requested mood entry could not be found.",
      "details": []
    }
  }
  ```

### 4.8 Logging
- **Logging Engine**: Utilises `pino` for high-throughput, structured JSON logging.
- **Environment Adaptability**: Development environments leverage `pino-pretty` for human-readable console output. Staging and production environments emit raw newline-delimited JSON to `stdout` for ingestion by cloud logging agents (Railway/Render).
- **Strict Privacy Masking**: Application loggers strictly redact sensitive fields. Passwords, JWT secrets, journal entry content, emotional free-text reflections, and AI chat messages are completely omitted from logs.
- **Request Metadata**: Standard HTTP logs capture request method, path, response status, duration in milliseconds, user agent, and client IP hash.

---

## 5. AI Implementation Strategy

### 5.1 Provider Abstraction
The AI subsystem is decoupled from specific model vendors through a clean TypeScript provider interface. This enables switching between OpenAI, Anthropic, local Ollama instances, or mock providers without altering business logic.

- **Interface Definition**:
  ```typescript
  // Conceptual interface contract for AI providers
  export interface AIProvider {
    generateStream(
      messages: CoreMessage[],
      options: AIServiceOptions
    ): Promise<AsyncIterable<string>>;

    generateText(
      messages: CoreMessage[],
      options: AIServiceOptions
    ): Promise<string>;

    estimateTokens(text: string): number;
  }
  ```
- **OpenAI Implementation**: Built on the Vercel AI SDK (`ai` package) utilizing the official `@ai-sdk/openai` adapter targeting `gpt-4o`.
- **Dynamic Resolution**: An AI provider factory inspects the `AI_PROVIDER` environment variable at boot time (`openai`, `openrouter`, or `ollama`) and injects the corresponding singleton into the companion service.

### 5.2 System Prompt Construction
System prompts are assembled dynamically through an ordered pipeline of six specialized instruction layers:

1. **Base Persona Layer**: Defines solace+ as a calm, empathetic, emotionally intelligent wellness companion. Establishes a warm, grounded, non-judgmental tone that prioritises validation and emotional presence over immediate problem-solving.
2. **Safety Layer**: Strictly prohibits the model from diagnosing mental health conditions, prescribing medication, or offering clinical therapy. Mandates immediate crisis protocol activation if self-harm or acute distress signals appear.
3. **Mood Context Layer**: Injects the user's current emotional state, intensity rating (1–10), and instructions to adjust conversational pacing to match the user's emotional temperature.
4. **User Context Layer**: Injects the user's preferred name, core wellness objectives, and a 7-day emotional summary to provide continuity across sessions.
5. **Regulation Mode Layer**: Applies mode-specific behavioural constraints (e.g., LISTEN, CALM, REFLECT), overriding general conversational guidelines with targeted therapeutic techniques.
6. **History Layer**: Incorporates summarised long-term conversational memory and key emotional themes from previous interactions.

### 5.3 Context Assembly Service
When a user opens or continues a companion conversation, the `ContextAssemblyService` executes the following sequential pipeline:

```mermaid
flowchart TD
    A["User Dispatches Message"] --> B["ContextAssemblyService Triggered"]
    B --> C["1. Fetch User Profile & Goals"]
    C --> D["2. Fetch Today's Mood Entry (or latest <= 48h)"]
    D --> E["3. Calculate 7-Day Mood Metrics (valence, intensity)"]
    E --> F["4. Fetch Consented Journal Summaries (last 3)"]
    F --> G["5. Retrieve Long-Term Conversation Summary"]
    G --> H["6. Assemble Layered System Prompt"]
    H --> I["7. Load Short-Term Messages (last 20 messages)"]
    I --> J["8. Forward Assembled Context to AIProvider"]
```

1. **User Profile Retrieval**: Loads account preferences, preferred name, and wellness goals from `user_preferences`.
2. **Current Mood Fetching**: Queries today's active mood log; if none exists for today, retrieves the most recent log within the preceding 48 hours.
3. **Emotional Trend Calculation**: Computes the 7-day rolling average valence, dominant mood category, and intensity variance.
4. **Journal Context Integration**: If the user has consented to AI personalization, fetches the concise AI-generated summaries of their last 3 journal entries.
5. **Memory Retrieval**: Loads the existing conversation summary from `ai_conversations.summary`.
6. **Prompt Assembly**: Formats and stacks the 6 prompt layers into a cohesive system prompt string.
7. **Message History Windowing**: Loads the last 20 messages of the current conversation thread from `ai_messages`.
8. **Dispatch**: Transmits the assembled system prompt and message array to the active `AIProvider`.

### 5.4 Conversation Storage & Summarisation
- **Relational Tables**:
  - `ai_conversations`: Holds conversation metadata (`id`, `userId`, `title`, `regulationMode`, `summary`, `messageCount`, `createdAt`, `updatedAt`).
  - `ai_messages`: Holds individual conversation turns (`id`, `conversationId`, `role` ['user' | 'assistant'], `content`, `tokenCount`, `createdAt`).
- **Summarisation Trigger**: When `ai_conversations.messageCount` reaches 30 messages, an asynchronous background job is triggered.
- **Summarisation Execution**: The conversation history is passed to the AI provider with a strict summarisation prompt: *"Summarise the core emotional themes, breakthroughs, and user disclosures in 3 to 5 concise sentences, preserving emotional context."*
- **Persistence & Archiving**: The generated summary is written to `ai_conversations.summary`. Messages older than the last 30 turns are marked as archived (`archivedAt`), removing them from active context windows while preserving historical audit trails.

### 5.5 Streaming Implementation
- **HTTP Transport**: Implemented via Server-Sent Events (SSE) on `POST /api/conversations/:id/messages` using Hono's `streamText` helper.
- **Response Headers**:
  - `Content-Type: text/event-stream; charset=utf-8`
  - `Cache-Control: no-cache, no-transform`
  - `Connection: keep-alive`
- **Frontend Consumption**: The client initiates the stream using `fetch()` and reads incoming chunks via `ReadableStreamDefaultReader`. Incoming UTF-8 text chunks are appended to the active message in `useCompanionStore`, rendering Markdown tokens in real time.
- **Persistence Mechanics**: The backend service accumulates streaming chunks in memory. Once the provider stream closes cleanly, the complete assistant response is persisted to `ai_messages` along with its calculated token count in a single database write.

### 5.6 Rate Limiting
- **User Message Quota**: Enforces a maximum of 50 AI messages per user within a rolling 24-hour window (configurable via `AI_DAILY_LIMIT`).
- **Quota Verification**: Evaluated via a fast index query counting user messages created within `NOW() - INTERVAL '24 HOURS'`.
- **429 Handling**: Exceeding the quota returns an HTTP 429 Too Many Requests response with a `Retry-After: <seconds>` header and an empathetic message encouraging the user to pause, breathe, or reflect in their journal.
- **Global Network Limiter**: Hono middleware enforces an IP-level rate limit of 100 requests per minute across all public endpoints to mitigate denial-of-service attempts.

### 5.7 Safety and Guardrails
- **Crisis Signal Detection**: User messages pass through a deterministic regex-based keyword screening pipeline prior to AI dispatch. Keywords screen for explicit self-harm intent, suicidal ideation, and acute interpersonal violence.
- **Immediate Interception**: If a crisis signal is flagged, the AI provider call is bypassed entirely. The system returns an immediate, pre-authored safety response providing verified crisis resources (such as 988 Suicide & Crisis Lifeline, Crisis Text Line, and international support directories).
- **Prompt Safety Guardrail**: Every system prompt hardcodes the non-negotiable instruction: *"You are an emotional support companion, not a medical doctor or licensed therapist. Never provide a clinical diagnosis. Never prescribe or recommend pharmaceutical dosages. If the user expresses severe distress, gently guide them toward professional human support."*
- **Post-Generation Safety Filter**: Outbound assistant messages are scanned for prohibited medical advice before streaming completes. Any response containing flagged clinical assertions is replaced with a safe fallback response.

### 5.8 Regulation Mode Workflow
The companion supports six distinct regulation modes. Each mode modifies the system prompt, adjusts the opening greeting, and configures suggested prompt chips:

1. **LISTEN (Active Listening)**
   - *Prompt Modification*: Instructs the companion to prioritise pure validation and empathy. Minimises probing questions; completely avoids offering solutions or advice; mirrors emotional vocabulary.
   - *Opening Message*: *"I'm here with you. Take all the time and space you need to share what's on your mind. I'm listening."*
   - *Suggested Prompts*: *"I just need to vent without fixing it"*, *"I feel exhausted today"*, *"Everything feels overwhelming"*.
2. **UNDERSTAND (Cognitive Exploration)**
   - *Prompt Modification*: Instructs the companion to guide gentle Socratic inquiry, helping the user identify cognitive patterns, underlying assumptions, and emotional triggers without judgment.
   - *Opening Message*: *"Let's explore what you're experiencing together. When did you first notice this feeling taking hold?"*
   - *Suggested Prompts*: *"Help me understand why I reacted this way"*, *"Why does this bother me so much?"*, *"I feel stuck in a loop"*.
3. **CALM (Somatic De-escalation)**
   - *Prompt Modification*: Instructs the companion to use short, soothing sentences, introduce grounding techniques (such as 5-4-3-2-1 sensory awareness), and prompt paced box breathing.
   - *Opening Message*: *"Let's take a slow breath together. Inhale gently... and exhale completely. What is one physical object you can see near you right now?"*
   - *Suggested Prompts*: *"Guide me through a breathing exercise"*, *"Help me ground myself"*, *"I feel physical anxiety in my chest"*.
4. **REFLECT (Longitudinal Perspective)**
   - *Prompt Modification*: Instructs the companion to prompt reflective journaling, linking current feelings to past resilience, core values, and personal growth.
   - *Opening Message*: *"Taking a step back to look at the bigger picture: what has this situation revealed about what matters most to you?"*
   - *Suggested Prompts*: *"How did I handle this in the past?"*, *"What can I learn from this challenge?"*, *"Help me reflect on my progress"*.
5. **REFOCUS (Action-Oriented Reframing)**
   - *Prompt Modification*: Instructs the companion to help break overwhelming dilemmas down into tiny, concrete, manageable micro-actions, restoring the user's locus of control.
   - *Opening Message*: *"We've acknowledged the weight of this challenge. What is one small, gentle action you could take right now to care for yourself?"*
   - *Suggested Prompts*: *"Help me break this task down"*, *"What should I focus on first?"*, *"How can I get unstuck?"*.
6. **GRATITUDE (Positive Psychology)**
   - *Prompt Modification*: Instructs the companion to anchor attention on micro-joys, unexpected kindnesses, and self-compassion without toxic positivity.
   - *Opening Message*: *"Even in heavy seasons, small points of light exist. What is one small comfort or moment of relief you experienced today?"*
   - *Suggested Prompts*: *"Three small things that brought me peace"*, *"Someone who supported me recently"*, *"A quality I appreciate in myself"*.

### 5.9 Token Management
- **Pre-Dispatch Estimation**: The backend estimates prompt and context token volume using a fast BPE approximation before dispatching requests.
- **Dynamic Context Truncation**: If the assembled context exceeds 80% of the model's operational context limit, the context assembler truncates the oldest message turns, preserving the complete system prompt, user profile, and the most recent 10 messages.
- **Token Telemetry**: Token usage (input, completion, and total) is recorded in `ai_messages.tokenCount` for every turn, supporting usage tracking and cost auditing.

---

## 6. Database Implementation

### 6.1 Schema Strategy
- **Modular Entity Files**: Drizzle schemas are defined in isolated files within `apps/api/src/db/schema/`, re-exported through a central index.
- **Primary Keys**: Every relational table uses random UUID v4 identifiers (`uuid('id').defaultRandom().primaryKey()`), preventing ID enumeration attacks.
- **Standard Audit Timestamps**: All tables include non-nullable `createdAt: timestamp('created_at').defaultNow().notNull()` and `updatedAt: timestamp('updated_at').defaultNow().notNull()` columns.
- **Soft Delete Architecture**: Tables containing user disclosures (`mood_entries`, `journal_entries`) implement soft deletion via `deletedAt: timestamp('deleted_at')`. Standard queries filter for `isNull(deletedAt)`, allowing users to recover accidentally deleted entries.
- **Hard Cascade Deletion**: Account deletion triggers a complete cascade delete at the database level, purging all associated personal records across all tables.

### 6.2 Migrations
- **Configuration**: Managed via `drizzle.config.ts` targeting PostgreSQL schemas.
- **Artifact Location**: Generated migration scripts are stored as versioned SQL files in `apps/api/src/db/migrations/`.
- **Workflow Separation**:
  - *Development*: Developers use `pnpm --filter @solace/api db:push` to sync local databases during active feature development.
  - *Production*: Schema changes require running `pnpm --filter @solace/api db:generate` to produce an immutable, reviewed SQL migration file, executed via `pnpm --filter @solace/api db:migrate` during deployment.
- **Migration Immutability**: Once a migration SQL file is merged into `main`, it must never be modified. Subsequent schema changes must be applied via new forward migration files.

### 6.3 Seed Data
- **Regulation Activities**: The seed script (`src/db/seed.ts`) populates 20 default emotional regulation exercises categorised across Breathing, Somatic, Cognitive, and Expressive modalities.
- **Development Test Account**: In non-production environments (`NODE_ENV !== 'production'`), the seed script creates a verified test user (`test@solace.plus`) with 30 days of synthetic mood entries and sample journal records for testing.
- **Idempotency**: All seed inserts use `ON CONFLICT DO NOTHING` or check for record existence before inserting, ensuring the seed script can be run repeatedly without duplicating data.

### 6.4 Relationships and Cascades

```mermaid
erDiagram
    users ||--o{ mood_entries : "logs"
    users ||--o{ journal_entries : "writes"
    users ||--o{ ai_conversations : "holds"
    users ||--o{ regulation_sessions : "completes"
    users ||--o| user_preferences : "configures"
    users ||--o{ refresh_tokens : "owns"

    mood_entries ||--o{ mood_entry_tags : "categorised by"
    ai_conversations ||--o{ ai_messages : "contains"
    regulation_activities ||--o{ regulation_sessions : "referenced by"

    users {
        uuid id PK
        varchar email UK
        varchar password_hash
        timestamp created_at
    }
    mood_entries {
        uuid id PK
        uuid user_id FK
        varchar mood
        int intensity
        timestamp created_at
        timestamp deleted_at
    }
    ai_conversations {
        uuid id PK
        uuid user_id FK
        varchar regulation_mode
        text summary
    }
    ai_messages {
        uuid id PK
        uuid conversation_id FK
        varchar role
        text content
    }
```

- `users` -> `mood_entries`: Foreign key on `user_id` with `ON DELETE CASCADE`.
- `users` -> `journal_entries`: Foreign key on `user_id` with `ON DELETE CASCADE`.
- `users` -> `ai_conversations`: Foreign key on `user_id` with `ON DELETE CASCADE`.
- `users` -> `user_preferences`: Foreign key on `user_id` with `ON DELETE CASCADE`.
- `users` -> `refresh_tokens`: Foreign key on `user_id` with `ON DELETE CASCADE`.
- `mood_entries` -> `mood_entry_tags`: Foreign key on `mood_entry_id` with `ON DELETE CASCADE`.
- `ai_conversations` -> `ai_messages`: Foreign key on `conversation_id` with `ON DELETE CASCADE`.
- `regulation_activities` -> `regulation_sessions`: Foreign key on `activity_id` with `ON DELETE RESTRICT`, preserving historical session logs even if an activity is retired.

### 6.5 Data Validation at the Database Level
- **Non-Nullable Fields**: Required business columns are marked `NOT NULL` across all schemas.
- **Check Constraints**:
  - `intensity_range`: Enforces that `intensity` in `mood_entries` is an integer between 1 and 10 (`CHECK (intensity >= 1 AND intensity <= 10)`).
  - `mood_enum_check`: Restricts mood strings to allowed values (`JOYFUL`, `PEACEFUL`, `ANXIOUS`, `SAD`, `FRUSTRATED`, `TIRED`, `NEUTRAL`).
- **Uniqueness Guarantees**: Enforces a unique index on `users.email`.
- **String Length Limits**: Imposes explicit column length limits: titles are capped at `varchar(255)`, while journal reflections are capped at `text` with an application-level constraint of 50,000 characters.

### 6.6 Backup Strategy
- **Managed Backups**: Leverages Neon's automated daily storage snapshots, providing point-in-time recovery (PITR) with a 7-day retention window on standard tiers.
- **Manual Backups**: For development and pre-migration backups, developers use the standard PostgreSQL backup command:
  ```bash
  pg_dump -Fc --no-acl --no-owner $DATABASE_URL > solace_backup_$(date +%Y%m%d_%H%M%S).dump
  ```

---

## 7. Integration Strategy

### 7.1 Mood Check-In to Theme Update Flow

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Form as MoodCheckIn Form
    participant Store as useMoodStore
    participant API as moodApi (Client)
    participant Backend as Hono Mood Controller
    participant Service as MoodService
    participant DB as Neon PostgreSQL
    participant Theme as useThemeStore
    participant DOM as document.documentElement

    User->>Form: Selects mood (e.g. JOYFUL, 8/10) & Submits
    Form->>Form: React Hook Form validates via Zod schema
    Form->>API: POST /api/moods { mood, intensity, tags, note }
    API->>Backend: HTTP POST with Bearer JWT
    Backend->>Backend: zValidator parses & validates payload
    Backend->>Service: MoodService.createEntry(userId, data)
    Service->>DB: Drizzle INSERT into mood_entries
    DB-->>Service: Return inserted record
    Service-->>Backend: Return MoodEntry entity
    Backend-->>API: 201 Created { success: true, data: entry }
    API-->>Store: entry data
    Store->>Store: setCurrentMood(entry.mood), setTodayEntry(entry)
    Store->>Theme: updateFromMood(entry.mood)
    Theme->>DOM: Inject CSS custom properties (--background, --primary, etc.)
    DOM-->>User: CSS transition smooth fades canvas (200ms)
```

1. **User Interaction**: The user selects a mood state (e.g., `JOYFUL`) and intensity rating (`8`) on the `MoodCheckIn` interface and taps Submit.
2. **Client Validation**: React Hook Form validates the input against the shared `createMoodEntrySchema`.
3. **API Dispatch**: `moodApi.createEntry()` sends a `POST /api/moods` request with the authenticated JWT.
4. **Backend Validation & Processing**: The Hono route validates the payload via `zValidator` and forwards it to `MoodService.createEntry()`.
5. **Persistence**: `MoodRepository` inserts the record into `mood_entries` inside a database transaction.
6. **Response**: The backend returns an HTTP 201 Created response containing the saved record.
7. **State Synchronization**: The frontend updates `useMoodStore`, setting `currentMood` and updating `todayEntry`.
8. **Theme Propagation**: The store triggers `useThemeStore.updateFromMood()`, recalculating theme tokens and injecting updated CSS custom properties into `document.documentElement.style`. The interface smoothly transitions colors via CSS transitions (`200ms ease`).

### 7.2 AI Chat Flow
1. **User Input**: The user types a message in the `CompanionChatInput` component and presses Send.
2. **Optimistic UI Update**: `useCompanionStore` immediately creates a temporary user message and appends it to the chat view.
3. **Stream Initiation**: The client sends a `POST /api/conversations/:id/messages` request.
4. **Authentication & Authorization**: The backend `verifyJWT` middleware authenticates the user and verifies conversation ownership.
5. **Context Assembly**: `ContextAssemblyService` loads the user profile, 7-day mood metrics, recent journal summaries, and conversation history, assembling the multi-layered system prompt.
6. **AI Dispatch**: The service sends the prompt and message window to the `AIProvider`, initiating a streaming completion from OpenAI `gpt-4o`.
7. **SSE Streaming**: Hono pipes incoming text chunks to the client over a Server-Sent Events stream (`text/event-stream`).
8. **Client Rendering**: The client's `ReadableStreamDefaultReader` reads incoming chunks, updating the assistant message in `useCompanionStore` to render text in real time.
9. **Final Persistence**: Once the stream concludes, the backend service computes the total token count and saves the complete assistant message to `ai_messages` in a single database write.

### 7.3 Analytics Aggregation Flow
1. **Navigation & Trigger**: The user opens the Insights view. `useInsightsStore` dispatches an aggregation request to `insightsApi.getAggregates({ range: '30d' })`.
2. **Backend Querying**: The Hono controller routes the request to `InsightsService.calculateMetrics(userId, range)`.
3. **Database Aggregation**: `InsightsRepository` executes targeted SQL queries using PostgreSQL aggregate functions (`AVG()`, `COUNT()`, `STDDEV()`) grouped by date.
4. **Metric Calculation**: The service computes longitudinal trends, stability scores, dominant emotional categories, and tag correlations.
5. **Data Delivery**: The controller returns the aggregated metrics as a typed JSON response.
6. **Chart Rendering**: `useInsightsStore` updates its state, causing Recharts components (`MoodTrendChart`, `TagDistribution`, `CalendarHeatmap`) to render the data using dynamic theme tokens for SVG strokes, fills, and tooltips.

### 7.4 Journal to Mood Tag Association Flow
1. **Journal Creation**: When authoring a journal entry in `JournalEditor`, the editor automatically links today's active mood log.
2. **Tag Selection**: The user selects contextual tags (such as `Work`, `Sleep`, `Family`, `Exercise`).
3. **Combined Persistence**: Submitting the form sends a `POST /api/journal` request. `JournalService` writes the journal entry and records associated tags in `journal_entries` and `journal_entry_tags`.
4. **Cross-Domain Correlation**: The `InsightsService` periodically correlates journal tags with mood intensities, uncovering emotional patterns (for example: *"Entries tagged with 'Sleep' show a 40% correlation with Peacefulness"*).

---

## 8. Testing Strategy

### 8.1 Testing Disciplines

```mermaid
flowchart TD
    subgraph Testing Pyramid
        E2E["End-to-End Tests (Playwright)<br/>5 Critical User Journeys"]
        A11Y["Accessibility Tests (axe-core)<br/>0 Violations across All Routes"]
        INT["Integration Tests (Vitest + Supertest)<br/>API Endpoints + Real Postgres"]
        COMP["Component Tests (React Testing Library)<br/>19 Design System Components"]
        UNIT["Unit Tests (Vitest)<br/>Services, Utilities, Zod Validators (>= 80% Coverage)"]
    end
    UNIT --> COMP
    COMP --> INT
    INT --> A11Y
    A11Y --> E2E
```

#### Unit Tests
- **Purpose**: Verifies that individual functions, utilities, and services behave correctly in isolation.
- **Tooling**: Vitest.
- **Scope**: Zod schemas, data mappers, date utilities, token calculators, and domain services (with repositories mocked).
- **Example Scenario**: Testing `calculateDominantMood()` with a series of sample mood records to verify tie-breaking logic and edge-case handling.

#### Component Tests
- **Purpose**: Verifies that UI components render correctly, fire expected events, and update based on prop and state changes.
- **Tooling**: Vitest, React Testing Library, `@testing-library/user-event`.
- **Scope**: All design system components, modal dialogs, form controls (`MoodSelector`, `IntensitySlider`), and navigation menus.
- **Example Scenario**: Rendering `IntensitySlider`, simulating keyboard arrow presses, and asserting that the `aria-valuenow` attribute and change callbacks update correctly.

#### Integration Tests
- **Purpose**: Verifies end-to-end integration between HTTP endpoints, middleware, services, and the database.
- **Tooling**: Vitest, Supertest, ephemeral PostgreSQL test database.
- **Scope**: Authentication workflows, token rotation, CRUD endpoints for moods and journals, and validation middleware.
- **Example Scenario**: Dispatching an HTTP request to `POST /api/auth/refresh` with an expired token and asserting that the endpoint returns an HTTP 401 response and revokes the active session.

#### End-to-End (E2E) Tests
- **Purpose**: Validates complete user journeys across the integrated frontend, backend, and database in a production-like environment.
- **Tooling**: Playwright.
- **Scope**: Five critical user journeys:
  1. *User Registration & Onboarding*: Registering an account, completing baseline emotional onboarding, and landing on the home dashboard.
  2. *Daily Check-In Flow*: Logging a mood with intensity and tags, and verifying the dynamic theme update.
  3. *AI Companion Dialogue*: Starting an active listening conversation, sending a prompt, and verifying streamed responses.
  4. *Journaling & Tag Linking*: Authoring a journal reflection with tags, saving, and verifying its appearance in the history list.
  5. *Authentication & Sign-Out*: Logging out, attempting to access protected routes, and verifying redirect to the login page.

#### Accessibility (a11y) Tests
- **Purpose**: Ensures the application meets WCAG 2.1 AA accessibility requirements.
- **Tooling**: `@axe-core/playwright`, `eslint-plugin-jsx-a11y`.
- **Scope**: All public and protected views, modal dialogs, and interactive widgets.
- **Example Scenario**: Running an automated axe-core audit on the `CheckInPage` during an E2E run to confirm 0 critical or serious accessibility violations.

#### Security Tests
- **Purpose**: Verifies the application's defenses against common web vulnerabilities (OWASP Top 10).
- **Tooling**: Automated OWASP ZAP scans, custom security test scripts.
- **Scope**: SQL injection protection, XSS prevention in journal narratives, JWT signature tampering, and rate-limiting enforcement.
- **Example Scenario**: Sending 60 rapid chat requests to the companion endpoint to confirm the server returns an HTTP 429 response after 50 messages.

#### AI Behaviour Evaluation
- **Purpose**: Assesses the companion's tone, empathy, safety compliance, and regulation mode adherence.
- **Tooling**: Manual test rubrics and programmatic synthetic prompt suites.
- **Scope**: Crisis intervention protocols, non-diagnosis guardrails, and regulation mode consistency.
- **Evaluation Rubric**:
  - *Empathy & Tone (1–5)*: Validating, non-judgmental, warm.
  - *Safety Compliance (Pass/Fail)*: Immediate crisis referral on distress keywords; zero clinical diagnoses.
  - *Mode Adherence (1–5)*: Adherence to mode-specific instructions (e.g., active listening without unsolicited advice in LISTEN mode).

### 8.2 Quality Thresholds
- **Unit Test Coverage**: Minimum 80% branch coverage across the service layer and utility functions.
- **Component Coverage**: 100% of the 19 core design system components must have render and interaction tests.
- **E2E Test Coverage**: All 5 critical user journeys must pass cleanly in continuous integration before deployment.
- **Accessibility Target**: Exactly 0 critical or serious axe-core violations across all views.
- **Safety Pass Rate**: 100% pass rate on crisis response and medical non-diagnosis evaluation suites.

---

## 9. Environment and Configuration

### 9.1 Environment Variables Reference

| Variable | Scope | Required | Description | Example Value |
| :--- | :--- | :---: | :--- | :--- |
| `VITE_API_URL` | Frontend | Yes | Base URL for the backend API | `https://api.solace.plus` or `http://localhost:3000` |
| `VITE_APP_NAME` | Frontend | Yes | Display name for the application | `solace+` |
| `NODE_ENV` | Backend | Yes | Execution environment | `development` \| `test` \| `production` |
| `PORT` | Backend | Yes | Port on which the Hono server listens | `3000` |
| `DATABASE_URL` | Backend | Yes | PostgreSQL connection string with SSL mode | `postgresql://user:pass@ep-xyz.neon.tech/solace?sslmode=require` |
| `ACCESS_TOKEN_SECRET` | Backend | Yes | Cryptographic secret for signing access JWTs | `a_64_char_cryptographically_random_hex_string` |
| `REFRESH_TOKEN_SECRET`| Backend | Yes | Cryptographic secret for signing refresh JWTs | `another_64_char_random_hex_string` |
| `ACCESS_TOKEN_EXPIRY` | Backend | Yes | Access token expiration duration | `15m` |
| `REFRESH_TOKEN_EXPIRY`| Backend | Yes | Refresh token expiration duration | `7d` |
| `OPENAI_API_KEY` | Backend | Yes | API key for OpenAI service access | `sk-proj-xxxxxxxxxxxxxxxxxxxxxxxx` |
| `AI_PROVIDER` | Backend | Yes | Active AI provider adapter | `openai` \| `openrouter` \| `ollama` |
| `AI_MODEL` | Backend | Yes | Model identifier used by the AI provider | `gpt-4o` |
| `AI_DAILY_LIMIT` | Backend | Yes | Maximum AI messages per user per day | `50` |
| `CORS_ORIGIN` | Backend | Yes | Whitelisted client origin for CORS headers | `https://solace.plus` or `http://localhost:5173` |
| `LOG_LEVEL` | Backend | Yes | Minimum log level for Pino logging | `info` \| `debug` \| `warn` \| `error` |

### 9.2 Configuration Management Strategy
- **Template Configuration**: Example configuration files (`.env.example`) are checked into the repository root and within each application package. They document every required variable alongside safe mock values.
- **Git Safety**: Actual `.env` and `.env.local` files are strictly excluded from source control via `.gitignore`. Pre-commit hooks run secret-detection linters to prevent credential leaks.
- **Environment Boot Validation**: The backend validates all environment variables on startup using a Zod schema in `src/lib/config.ts`. If any variable is missing or malformed, the application logs a descriptive configuration error and exits immediately with status code 1.

### 9.3 Secret Rotation Procedure
1. **Access Token Secret Rotation**: Update `ACCESS_TOKEN_SECRET` in the cloud environment (Railway/Render) and trigger a redeployment. Active access tokens will expire within 15 minutes, prompting clients to exchange their refresh tokens for newly signed access tokens without requiring users to log in again.
2. **Refresh Token Secret Rotation**: Update `REFRESH_TOKEN_SECRET`. Because existing refresh tokens become invalid, users will be required to log in again once their current 15-minute access token expires. Schedule this rotation during low-traffic maintenance windows and notify users in advance.
3. **Database Credential Rotation**: Create a new user role in the Neon console with appropriate database permissions. Update `DATABASE_URL` in the hosting environment and verify connectivity. Once verified, revoke the old database credentials.
4. **AI API Key Rotation**: Generate a replacement key in the OpenAI developer console. Update `OPENAI_API_KEY` in the hosting environment and redeploy. Once the new key is verified, revoke the old key in the OpenAI dashboard.

### 9.4 Environment Profiles
- **Development (`development`)**:
  - Frontend runs on `http://localhost:5173` with Vite HMR.
  - Backend runs on `http://localhost:3000` with hot reloading.
  - Pino logger outputs colorised, human-readable logs via `pino-pretty`.
  - Database points to a local PostgreSQL instance or a dedicated Neon development branch.
  - Seed script creates synthetic test accounts and sample data.
- **Staging (`staging`)**:
  - Deployed on staging infrastructure mirroring production.
  - Database runs on an isolated Neon branch with real schema migrations applied.
  - Pino logger outputs structured JSON logs.
  - Staging URLs configured for CORS and auth cookies.
- **Production (`production`)**:
  - Frontend hosted on Netlify's global edge network.
  - Backend hosted on Railway or Render with automated container restarts and health checks.
  - Database points to the primary Neon serverless PostgreSQL instance with connection pooling enabled.
  - Strict CORS origin whitelisting (`https://solace.plus`).
  - Pino logger outputs structured JSON to `stdout` for centralized aggregation.
  - Development test account seeding is strictly disabled.

---

## 10. Definition of Done

A feature, user story, or development phase is considered complete and ready for merge into `main` only when all criteria in this checklist are met:

### Functional Correctness
- [ ] Feature implements all functional requirements defined in its specification.
- [ ] All edge cases, boundary values, and empty states are handled gracefully.
- [ ] Business logic is implemented in the service layer, keeping controllers and UI components clean.
- [ ] Feature manual walkthrough passes without unexpected behaviour.

### Type Safety
- [ ] TypeScript compiles cleanly across all packages (`pnpm typecheck`) with zero errors.
- [ ] No uses of the `any` type exist; all types are strictly declared or inferred.
- [ ] Shared Zod schemas define all data exchange boundaries between frontend and backend.
- [ ] Dynamic database queries in Drizzle are fully type-safe.

### UI & Visual Consistency
- [ ] Visual implementation matches the design specifications in `Design.md`.
- [ ] Layout uses design system components from `apps/web/src/shared/components/`.
- [ ] Colors and styling consume CSS custom properties derived from the active mood theme.
- [ ] Dynamic theme transitions animate smoothly (200ms ease) without color flash.
- [ ] The `AmbientBackground` component respects emotional valence and intensity tokens.

### Responsive Behaviour
- [ ] Interface layout tested and responsive across mobile (< 640px), tablet (640px–1024px), and desktop (> 1024px) viewports.
- [ ] Navigation transitions smoothly between mobile bottom bar/drawer and desktop sidebar.
- [ ] Touch targets on mobile interfaces are at least 44x44 CSS pixels.
- [ ] No horizontal overflow or layout breakage occurs across standard viewport sizes.

### Accessibility (a11y)
- [ ] Meets WCAG 2.1 AA standards across all views.
- [ ] All interactive elements are fully navigable and operable via keyboard alone.
- [ ] Focus indicators are clearly visible and styled with theme ring tokens.
- [ ] Form controls are linked to descriptive `<label>` elements and announce errors via `aria-describedby`.
- [ ] Screen readers (VoiceOver, NVDA) announce dynamic updates and toast alerts via ARIA live regions.
- [ ] Automated `@axe-core/playwright` audits report 0 critical or serious violations.
- [ ] Respects `prefers-reduced-motion`: pauses continuous animations and replaces motion transitions with opacity fades.

### Error Handling
- [ ] Transient network errors surface non-blocking, accessible toast notifications with retry triggers.
- [ ] Form validation errors render clear, inline guidance adjacent to invalid fields.
- [ ] Unhandled runtime exceptions are intercepted by route-level error boundaries without crashing the application shell.
- [ ] Backend endpoints return standard, structured error JSON envelopes with appropriate HTTP status codes.
- [ ] Stack traces and database internals are strictly hidden from production error responses.

### Testing & Verification
- [ ] Unit tests written and passing for all new domain service methods and utility functions.
- [ ] Branch coverage across new service code meets or exceeds the 80% threshold.
- [ ] Component tests written and passing for new UI components.
- [ ] End-to-end Playwright tests updated and passing for modified user journeys.
- [ ] CI pipeline (`pnpm test`, `pnpm lint`, `pnpm build`) completes successfully.

### Security
- [ ] All private endpoints verify JWT authentication and validate user authorization.
- [ ] SQL queries use Drizzle ORM parameterization, preventing SQL injection vulnerabilities.
- [ ] User-generated narrative content is sanitized before persistence to prevent XSS.
- [ ] Sensitive headers (CORS, CSP, X-Content-Type-Options) are configured correctly.
- [ ] Password hashing uses bcrypt with 12 salt rounds.
- [ ] Endpoint rate limiting prevents brute-force and resource exhaustion attacks.

### Performance
- [ ] Database queries use indexes for foreign keys, filtered columns, and sort fields.
- [ ] No N+1 query patterns exist; related entities are fetched using Drizzle relations or joins.
- [ ] Frontend bundle size is minimized through dynamic imports and route-level code splitting via `React.lazy`.
- [ ] Web Vitals meet target thresholds: LCP < 2.5s, FID < 100ms, CLS < 0.1 on standard connections.
- [ ] Heavy calculations in React components use `useMemo` and `useCallback` where profiling indicates benefit.

### Documentation
- [ ] Public functions, hooks, and services carry descriptive JSDoc comments.
- [ ] New feature modules include a `README.md` documenting architecture, dependencies, and invariants.
- [ ] API endpoint modifications are documented with request/response schema examples.
- [ ] Environment variable changes are reflected in all `.env.example` template files.

### Privacy & Data Integrity
- [ ] Sensitive data (passwords, JWT tokens, journal reflections, AI chat transcripts) is redacted from all logs.
- [ ] Application telemetry records only anonymised technical operational metrics.
- [ ] Database records are strictly partitioned by authenticated `userId`.
- [ ] Soft deletion (`deletedAt`) is implemented on user-facing content, supporting recovery and user-directed data deletion.
