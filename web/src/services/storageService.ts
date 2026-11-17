import {
  ChatMessage,
  JournalEntry,
  MoodCheckIn,
  RegulationRecord,
  UserPreferences
} from '../types/solace';

// ============================================================================
// Solace+ Local-First Private Storage Service
// Zero mandatory cloud connectivity. Respects Memory Saving vs Ephemeral Mode.
// ============================================================================

const PREFS_KEY = 'solace_preferences';
const MOODS_KEY = 'solace_mood_entries';
const JOURNAL_KEY = 'solace_journal_entries';
const REGULATION_KEY = 'solace_regulation_records';
const CHAT_KEY = 'solace_chat_history';
const KNOWN_USERS_KEY = 'solace_known_users';

export const DEFAULT_PREFERENCES: UserPreferences = {
  sanctuaryName: '',
  theme: 'system',
  pinnedMood: null,
  geminiApiKey: '',
  geminiModel: 'gemini-2.5-flash',
  chatbotRole: 'companion',
  soundEnabled: true,
  memoryConsent: 'persisted',
  isLoggedIn: false
};

// Seed initial mood check-ins for rich insights right out of the box
const INITIAL_MOODS: MoodCheckIn[] = [
  {
    id: 'seed-1',
    timestamp: Date.now() - 86400000 * 4,
    mood: 'calm',
    valence: 8,
    intensity: 4,
    tags: ['Quiet morning', 'Gentle breathing', 'Peace of mind'],
    note: 'Woke up early and listened to the rain. Felt centered and quiet.'
  },
  {
    id: 'seed-2',
    timestamp: Date.now() - 86400000 * 3,
    mood: 'anxious',
    valence: 3,
    intensity: 6,
    tags: ['Deadlines & Work', 'Overthinking'],
    note: 'Felt tension in the chest before the team review, but did 2 minutes of box breathing.'
  },
  {
    id: 'seed-3',
    timestamp: Date.now() - 86400000 * 2,
    mood: 'happy',
    valence: 9,
    intensity: 5,
    tags: ['Joyful spark', 'Progress', 'Good news'],
    note: 'Finished major milestone. Celebrated with warm tea and good conversation.'
  },
  {
    id: 'seed-4',
    timestamp: Date.now() - 86400000 * 1,
    mood: 'calm',
    valence: 7,
    intensity: 3,
    tags: ['Nature & Walk', 'Solitude'],
    note: 'Evening walk without screens. The cool breeze cleared out the day.'
  }
];

const INITIAL_JOURNAL: JournalEntry[] = [
  {
    id: 'seed-journal-1',
    timestamp: Date.now() - 86400000 * 2,
    title: 'The Sanctuary Within',
    content:
      'I am realizing that peace is not the absence of busyness or thoughts, but the presence of kindness toward myself when life is busy. Giving myself permission to exhale.',
    associatedMood: 'calm',
    tags: ['Peace of mind', 'Self-compassion'],
    wordCount: 33
  }
];

// Ephemeral in-memory caches for when user selects 'ephemeral' mode
let ephemeralChatHistory: ChatMessage[] = [];

export class StorageService {
  static getPreferences(): UserPreferences {
    try {
      const data = localStorage.getItem(PREFS_KEY);
      if (data) {
        return { ...DEFAULT_PREFERENCES, ...JSON.parse(data) };
      }
    } catch (e) {
      console.warn('Could not read user preferences from localStorage:', e);
    }
    return DEFAULT_PREFERENCES;
  }

  static savePreferences(prefs: UserPreferences): void {
    try {
      localStorage.setItem(PREFS_KEY, JSON.stringify(prefs));
    } catch (e) {
      console.warn('Could not save user preferences:', e);
    }
  }

  static getMoods(): MoodCheckIn[] {
    try {
      const data = localStorage.getItem(MOODS_KEY);
      if (data) {
        return JSON.parse(data);
      }
    } catch (e) {
      console.warn('Could not read moods from localStorage:', e);
    }
    return INITIAL_MOODS;
  }

  static saveMood(entry: MoodCheckIn): void {
    const prefs = this.getPreferences();
    if (prefs.memoryConsent === 'ephemeral') return;

    try {
      const current = this.getMoods();
      const updated = [entry, ...current];
      localStorage.setItem(MOODS_KEY, JSON.stringify(updated));
    } catch (e) {
      console.warn('Could not save mood check-in:', e);
    }
  }

  static getJournalEntries(): JournalEntry[] {
    try {
      const data = localStorage.getItem(JOURNAL_KEY);
      if (data) {
        return JSON.parse(data);
      }
    } catch (e) {
      console.warn('Could not read journal entries from localStorage:', e);
    }
    return INITIAL_JOURNAL;
  }

  static saveJournalEntry(entry: JournalEntry): void {
    const prefs = this.getPreferences();
    if (prefs.memoryConsent === 'ephemeral') return;

    try {
      const current = this.getJournalEntries();
      const filtered = current.filter((e) => e.id !== entry.id);
      const updated = [entry, ...filtered];
      localStorage.setItem(JOURNAL_KEY, JSON.stringify(updated));
    } catch (e) {
      console.warn('Could not save journal entry:', e);
    }
  }

  static deleteJournalEntry(id: string): void {
    try {
      const current = this.getJournalEntries();
      const updated = current.filter((e) => e.id !== id);
      localStorage.setItem(JOURNAL_KEY, JSON.stringify(updated));
    } catch (e) {
      console.warn('Could not delete journal entry:', e);
    }
  }

  static getRegulationRecords(): RegulationRecord[] {
    try {
      const data = localStorage.getItem(REGULATION_KEY);
      if (data) return JSON.parse(data);
    } catch (e) {
      console.warn('Could not read regulation records:', e);
    }
    return [];
  }

  static saveRegulationRecord(record: RegulationRecord): void {
    const prefs = this.getPreferences();
    if (prefs.memoryConsent === 'ephemeral') return;

    try {
      const current = this.getRegulationRecords();
      const updated = [record, ...current];
      localStorage.setItem(REGULATION_KEY, JSON.stringify(updated));
    } catch (e) {
      console.warn('Could not save regulation record:', e);
    }
  }

  static getKnownUsers(): string[] {
    try {
      const raw = localStorage.getItem(KNOWN_USERS_KEY);
      if (raw) return JSON.parse(raw);
    } catch (e) {
      console.warn('Could not read known users:', e);
    }
    return [];
  }

  static addKnownUser(name: string): void {
    const clean = name.trim();
    if (!clean) return;
    try {
      const users = this.getKnownUsers();
      if (!users.some((u) => u.toLowerCase() === clean.toLowerCase())) {
        users.push(clean);
        localStorage.setItem(KNOWN_USERS_KEY, JSON.stringify(users));
      }
    } catch (e) {
      console.warn('Could not save known user:', e);
    }
  }

  static removeKnownUser(name: string): void {
    const clean = name.trim();
    try {
      const users = this.getKnownUsers().filter((u) => u.toLowerCase() !== clean.toLowerCase());
      localStorage.setItem(KNOWN_USERS_KEY, JSON.stringify(users));
    } catch (e) {
      console.warn('Could not remove known user:', e);
    }
  }

  private static getUserChatKey(userName?: string): string {
    const clean = userName?.trim().toLowerCase();
    if (clean && clean !== 'guest' && clean !== 'user' && clean !== 'sanctuary') {
      return `${CHAT_KEY}_${clean}`;
    }
    return CHAT_KEY;
  }

  static getChatMessages(userName?: string): ChatMessage[] {
    const prefs = this.getPreferences();
    if (prefs.memoryConsent === 'ephemeral') {
      return ephemeralChatHistory;
    }

    const effectiveUser = (userName ?? prefs.sanctuaryName)?.trim();
    const userKey = this.getUserChatKey(effectiveUser);

    try {
      const data = localStorage.getItem(userKey);
      if (data) return JSON.parse(data);

      // Only check legacy un-scoped key if there is no user specified (guest/legacy)
      if (!effectiveUser) {
        const legacy = localStorage.getItem(CHAT_KEY);
        if (legacy) return JSON.parse(legacy);
      }
    } catch (e) {
      console.warn('Could not read chat messages:', e);
    }

    return [];
  }

  static saveChatMessages(messages: ChatMessage[], userName?: string): void {
    const prefs = this.getPreferences();
    if (prefs.memoryConsent === 'ephemeral') {
      ephemeralChatHistory = messages;
      return;
    }

    const effectiveUser = (userName ?? prefs.sanctuaryName)?.trim();
    const userKey = this.getUserChatKey(effectiveUser);

    try {
      const payload = JSON.stringify(messages);
      localStorage.setItem(userKey, payload);
      if (!effectiveUser) {
        localStorage.setItem(CHAT_KEY, payload);
      }
    } catch (e) {
      console.warn('Could not persist chat messages:', e);
    }
  }

  static clearChatHistory(userName?: string): void {
    ephemeralChatHistory = [];
    const prefs = this.getPreferences();
    const effectiveUser = userName || prefs.sanctuaryName;
    const userKey = this.getUserChatKey(effectiveUser);

    try {
      localStorage.removeItem(userKey);
      localStorage.removeItem(CHAT_KEY);
    } catch (e) {
      console.warn('Could not clear chat messages:', e);
    }
  }

  /**
   * Complete Data Export (GDPR / Sovereign local export)
   */
  static exportAllData(): void {
    const data = {
      exportedAt: new Date().toISOString(),
      platform: 'Solace+ Web Sanctuary',
      preferences: this.getPreferences(),
      moodHistory: this.getMoods(),
      journalEntries: this.getJournalEntries(),
      regulationRecords: this.getRegulationRecords()
    };

    const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `solace-sanctuary-backup-${new Date().toISOString().slice(0, 10)}.json`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  }

  /**
   * Wipes all persisted data
   */
  static clearAllData(): void {
    try {
      localStorage.clear();
      ephemeralChatHistory = [];
    } catch (e) {
      console.warn('Error clearing localStorage:', e);
    }
  }
}
