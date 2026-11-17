// ============================================================================
// Solace+ Domain Models and Types
// ============================================================================

export type MoodId = 'happy' | 'calm' | 'neutral' | 'sad' | 'anxious' | 'angry' | 'overwhelmed';

export interface MoodConfig {
  id: MoodId;
  title: string;
  subtitle: string;
  emoji: string;
  defaultValence: number;
  primaryColor: string;
  bgTint: string;
  resonanceBadge: string;
  reflectionPromptTitle: string;
  reflectionPromptSubtitle: string;
  reflectionPlaceholder: string;
  contextualTags: string[];
}

export const MOOD_DEFINITIONS: Record<MoodId, MoodConfig> = {
  happy: {
    id: 'happy',
    title: 'Joyful',
    subtitle: 'Warm & radiant',
    emoji: '☀️',
    defaultValence: 8,
    primaryColor: '#D98E04',
    bgTint: '#FFF7E2',
    resonanceBadge: '✨ Joyful Resonance • Celebrating your light',
    reflectionPromptTitle: 'What is making you happy in this moment?',
    reflectionPromptSubtitle: 'Savor the light. Naming what brings you warmth and joy wires it deeper into your wellbeing.',
    reflectionPlaceholder: "Tell Solace what's making you smile, feel grateful, or spark joy today...",
    contextualTags: [
      'Joyful spark', 'Gratitude', 'Celebration', 'Progress', 'Good news',
      'Loving kindness', 'Sunlight & Warmth', 'Friendship', 'Music & Play', 'Accomplishment'
    ]
  },
  calm: {
    id: 'calm',
    title: 'Calm',
    subtitle: 'Peaceful & centered',
    emoji: '🌿',
    defaultValence: 8,
    primaryColor: '#4C7D5C',
    bgTint: '#EFF7F2',
    resonanceBadge: '🌿 Serene Sanctuary • Rooted in peace',
    reflectionPromptTitle: 'What is resting in your thoughts right now?',
    reflectionPromptSubtitle: 'Notice whatever quiet thoughts or sensations are present without rush or expectation.',
    reflectionPlaceholder: 'Record whatever thoughts, sensations, or quiet reflections are here...',
    contextualTags: [
      'Quiet morning', 'Peace of mind', 'Sleep & Rest', 'Nature & Walk',
      'Gentle breathing', 'Solitude', 'Creativity', 'Patience', 'Physical Health'
    ]
  },
  neutral: {
    id: 'neutral',
    title: 'Steady',
    subtitle: 'Grounded & quiet',
    emoji: '⚖️',
    defaultValence: 5,
    primaryColor: '#5E5854',
    bgTint: '#F4F1EB',
    resonanceBadge: '⚖️ Steady Baseline • Present in the moment',
    reflectionPromptTitle: 'What is present in your day today?',
    reflectionPromptSubtitle: 'Honoring an ordinary, steady baseline is its own quiet kind of peace.',
    reflectionPlaceholder: 'A few words on how the day is unfolding...',
    contextualTags: [
      'Ordinary rhythm', 'Routine', 'Work tasks', 'Quiet pause',
      'Neutral space', 'Observing', 'Transition', 'Just being'
    ]
  },
  sad: {
    id: 'sad',
    title: 'Tender',
    subtitle: 'Reflective & heavy',
    emoji: '💧',
    defaultValence: 3,
    primaryColor: '#4E6E8A',
    bgTint: '#EEF3F8',
    resonanceBadge: '💧 Tender Space • Holding sadness with gentle care',
    reflectionPromptTitle: 'What is hurting or feeling tender?',
    reflectionPromptSubtitle: 'Zero judgment here. Sadness is simply proof that you cared deeply. It is safe to unload here.',
    reflectionPlaceholder: 'What is hurting or feeling heavy? You are safe to let it be tender here...',
    contextualTags: [
      'Heavy heart', 'Grief & Loss', 'Lonely feeling', 'Disappointment',
      'Fatigue', 'Unspoken tears', 'Tender memory', 'Soft longing'
    ]
  },
  anxious: {
    id: 'anxious',
    title: 'Restless',
    subtitle: 'Fluttering & tense',
    emoji: '🌊',
    defaultValence: 3,
    primaryColor: '#3B7C91',
    bgTint: '#EAF4F7',
    resonanceBadge: '🌊 Restless Waves • Soothing tension within',
    reflectionPromptTitle: 'What is racing or worrying in your mind?',
    reflectionPromptSubtitle: 'Putting anxious loops into words helps separate your wise self from the alarm.',
    reflectionPlaceholder: "What's bothering or racing in your mind? Write down the worry to take its power away...",
    contextualTags: [
      'Overthinking', 'Chest flutter', 'Future worry', 'Deadlines & Work',
      'Social tension', 'Racing thoughts', 'Need for control', 'Restless energy'
    ]
  },
  angry: {
    id: 'angry',
    title: 'Stormy',
    subtitle: 'Heated & frustrated',
    emoji: '🔥',
    defaultValence: 2,
    primaryColor: '#9C5135',
    bgTint: '#F9EFEA',
    resonanceBadge: '🔥 Stormy Space • Safe place to let the heat out',
    reflectionPromptTitle: 'What made you feel angry or frustrated?',
    reflectionPromptSubtitle: 'Anger often points to a boundary that was crossed. Spill it safely without shame.',
    reflectionPlaceholder: 'Let out what bothered you... what made you feel angry, frustrated, or treated unfairly?',
    contextualTags: [
      'Frustration', 'Crossed boundary', 'Unfairness', 'Feeling unheard',
      'Conflict', 'Irritation', 'Built-up resentment', 'Need space'
    ]
  },
  overwhelmed: {
    id: 'overwhelmed',
    title: 'Overwhelmed',
    subtitle: 'Scattered & heavy',
    emoji: '☁️',
    defaultValence: 2,
    primaryColor: '#545A61',
    bgTint: '#EFF1F3',
    resonanceBadge: '☁️ Heavy Weight • Softly unloading your shoulders',
    reflectionPromptTitle: 'What is piling up on you right now?',
    reflectionPromptSubtitle: 'You do not have to carry or solve everything in this single hour. Drop the stones here.',
    reflectionPlaceholder: "Write down what's piling up so you don't have to carry it all in your head...",
    contextualTags: [
      'Too many demands', 'Decision fatigue', 'Sensory overload', 'Mental clutter',
      'Burnout', 'Exhaustion', 'Need to pause', 'Unrealistic pressure'
    ]
  }
};

export type RegulationModeId = 'listen' | 'understand' | 'calm' | 'reflect' | 'refocus' | 'gratitude';

export interface RegulationModeConfig {
  id: RegulationModeId;
  title: string;
  iconEmoji: string;
  description: string;
  promptPlaceholder: string;
  starterPrompts: string[];
}

export const REGULATION_MODES: Record<RegulationModeId, RegulationModeConfig> = {
  listen: {
    id: 'listen',
    title: 'Listen',
    iconEmoji: '👂',
    description: 'Silent holding space with unconditional validation and gentle presence.',
    promptPlaceholder: 'Speak whatever feels heavy right now...',
    starterPrompts: [
      "I don't know what I'm feeling",
      "I just want someone to listen",
      "Everything feels heavy today",
      "Can I just vent for a moment?"
    ]
  },
  understand: {
    id: 'understand',
    title: 'Understand',
    iconEmoji: '💡',
    description: 'Gentle exploration of emotional triggers, underlying roots, and thought patterns.',
    promptPlaceholder: 'What thoughts are circling your mind?',
    starterPrompts: [
      'Help me unpack this week',
      'Why did that interaction sting so much?',
      'I feel conflicted between two emotions',
      'Help me understand my anxiety today'
    ]
  },
  calm: {
    id: 'calm',
    title: 'Calm',
    iconEmoji: '🌿',
    description: 'Somatic de-escalation, paced breathwork guidance, and soothing down-regulation.',
    promptPlaceholder: 'Where do you feel physical tension?',
    starterPrompts: [
      "Let's do a quick grounding breath",
      'My chest feels tight and fluttery',
      'Guide me through physical relaxation',
      'Help me calm my racing thoughts'
    ]
  },
  reflect: {
    id: 'reflect',
    title: 'Reflect',
    iconEmoji: '🪞',
    description: 'Deep cognitive mirroring, compassionate reframing, and discovering wisdom.',
    promptPlaceholder: 'What perspective feels hard to see?',
    starterPrompts: [
      'What can this difficult feeling teach me?',
      'How can I treat myself with more grace?',
      'Reflect on my growth this past month',
      'Help me reframe this self-criticism'
    ]
  },
  refocus: {
    id: 'refocus',
    title: 'Refocus',
    iconEmoji: '🧭',
    description: 'Gentle micro-actions to break paralysis, overwhelm, and restore personal agency.',
    promptPlaceholder: 'What is one small thing you can control?',
    starterPrompts: [
      "I have so much to do I'm frozen",
      'Give me one tiny step for right now',
      'How do I unstick myself gently?',
      'Help me prioritize with kindness'
    ]
  },
  gratitude: {
    id: 'gratitude',
    title: 'Gratitude',
    iconEmoji: '☀️',
    description: 'Savoring quiet peace, gentle warmth, and anchoring small everyday mercies.',
    promptPlaceholder: 'What brought a moment of quiet relief?',
    starterPrompts: [
      'Three small comforts from this morning',
      'Someone whose presence warmed my spirit',
      'A gentle thing I appreciate about myself',
      'Finding light in a difficult week'
    ]
  }
};

export interface RegulationActivity {
  id: string;
  title: string;
  category: 'Breathing' | 'Grounding' | 'Cognitive' | 'Somatic' | 'Gratitude';
  durationSeconds: number;
  description: string;
  instructions: string[];
}

export const CURATED_REGULATION_ACTIVITIES: RegulationActivity[] = [
  {
    id: 'box_breathing',
    title: 'Box Breathing (4-4-4-4)',
    category: 'Breathing',
    durationSeconds: 120,
    description: 'A powerful autonomic stabilizer: inhale, hold, exhale, hold for equal 4-second counts.',
    instructions: [
      'Inhale deeply through your nose for 4 seconds',
      'Hold your breath gently with relaxed shoulders for 4 seconds',
      'Exhale smoothly through your mouth for 4 seconds',
      'Rest empty before your next breath for 4 seconds'
    ]
  },
  {
    id: 'physiological_sigh',
    title: 'Physiological Sigh (4-2-8)',
    category: 'Breathing',
    durationSeconds: 90,
    description: 'The fastest biological reset for high arousal: double inhale followed by a slow, long sigh.',
    instructions: [
      'Take a deep inhale through your nose for 4 seconds',
      'Sip a second top-off breath to completely expand your lungs for 2 seconds',
      'Release a slow, unhurried sigh out through your lips for 8 full seconds'
    ]
  },
  {
    id: 'grounding_54321',
    title: '5-4-3-2-1 Sensory Grounding',
    category: 'Grounding',
    durationSeconds: 180,
    description: 'Anchor your physical presence back into the here and now through sensory cues.',
    instructions: [
      '5 Things you can see around your immediate space',
      '4 Things you can physically touch or feel against your skin',
      '3 Things you can hear in your environment',
      '2 Things you can smell or subtle scents in the air',
      '1 Thing you can taste or an appreciative breath'
    ]
  },
  {
    id: 'body_scan',
    title: 'Somatic Release Scan',
    category: 'Somatic',
    durationSeconds: 180,
    description: 'Unclench hidden physical stress stored in jaw, temples, neck, and chest.',
    instructions: [
      'Unclench your teeth and let your tongue rest on the floor of your mouth',
      'Drop your shoulders 2 inches down away from your ears',
      'Open your palms and relax your belly completely',
      'Take one long, unhurried sigh out through your lips'
    ]
  },
  {
    id: 'cognitive_reframe',
    title: 'Gentle Cognitive Reframing',
    category: 'Cognitive',
    durationSeconds: 150,
    description: 'Notice rigid thoughts like passing clouds without letting them define your worth.',
    instructions: [
      'Identify the heavy thought or story your mind is telling you',
      'Ask: Is this 100% true, or is my nervous system tired?',
      'What would a compassionate friend say to you right now?',
      'Hold space for uncertainty without rushing to fix everything'
    ]
  },
  {
    id: 'gratitude_anchor',
    title: 'Quiet Gratitude Anchor',
    category: 'Gratitude',
    durationSeconds: 120,
    description: 'Anchor your spirit by noticing three small, unpretentious mercies of today.',
    instructions: [
      'Notice one sensory pleasure: warmth of a cup, light through a window',
      'Notice one act of quiet kindness you witnessed or received',
      'Give yourself credit for simply showing up and breathing today'
    ]
  }
];

export enum RiskLevel {
  LEVEL_0_NORMAL = 0,
  LEVEL_1_DISTRESS = 1,
  LEVEL_2_ELEVATED = 2,
  LEVEL_3_SUICIDAL_IDEATION = 3,
  LEVEL_4_CRITICAL = 4
}

export enum SafetyQuestionId {
  CURRENT_SUICIDAL_THOUGHTS = 'CURRENT_SUICIDAL_THOUGHTS',
  IMMEDIATE_INTENT = 'IMMEDIATE_INTENT',
  PLAN_AND_TIMEFRAME = 'PLAN_AND_TIMEFRAME',
  ACCESS_TO_MEANS = 'ACCESS_TO_MEANS',
  ACTION_ALREADY_TAKEN = 'ACTION_ALREADY_TAKEN',
  HUMAN_SUPPORT = 'HUMAN_SUPPORT'
}

export const SAFETY_QUESTIONS: Record<SafetyQuestionId, string> = {
  [SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS]: 'Are you having thoughts of ending your life at this moment?',
  [SafetyQuestionId.IMMEDIATE_INTENT]: 'Have you thought about acting on these thoughts?',
  [SafetyQuestionId.PLAN_AND_TIMEFRAME]: 'Have you thought about how or when you might act on these thoughts?',
  [SafetyQuestionId.ACCESS_TO_MEANS]: 'Do you have access to anything you might use to hurt yourself?',
  [SafetyQuestionId.ACTION_ALREADY_TAKEN]: 'Have you already taken any steps to hurt yourself?',
  [SafetyQuestionId.HUMAN_SUPPORT]: 'Is there someone you trust who could come and stay with you right now?'
};

export interface ChatMessage {
  id: string;
  role: 'user' | 'assistant';
  text: string;
  timestamp: number;
  riskLevel?: RiskLevel;
  showEmergencyCard?: boolean;
  showSafetyQuestion?: boolean;
  pendingQuestionId?: SafetyQuestionId;
  regulationMode?: RegulationModeId;
}

export interface MoodCheckIn {
  id: string;
  timestamp: number;
  mood: MoodId;
  valence: number;
  intensity: number;
  tags: string[];
  note: string;
}

export interface JournalEntry {
  id: string;
  timestamp: number;
  title: string;
  content: string;
  associatedMood?: MoodId;
  tags: string[];
  wordCount: number;
}

export interface RegulationRecord {
  id: string;
  activityId: string;
  activityTitle: string;
  timestamp: number;
  durationSeconds: number;
  rating: number; // 1-5
  note?: string;
}

export interface EmergencyResource {
  name: string;
  number: string;
  description: string;
  category: 'emergency' | 'mental_health' | 'crisis_text';
  country: string;
}

export type ThemeMode = 'system' | 'light' | 'dark';

export interface UserPreferences {
  sanctuaryName: string;
  theme: ThemeMode;
  pinnedMood: MoodId | null;
  geminiApiKey: string;
  geminiModel: 'gemini-2.5-flash' | 'gemini-2.0-flash' | 'gemini-1.5-flash';
  chatbotRole: 'companion' | 'somatic' | 'cbt' | 'wisdom';
  soundEnabled: boolean;
  memoryConsent: 'persisted' | 'ephemeral';
  isLoggedIn: boolean;
}
