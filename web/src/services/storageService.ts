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

export const DEFAULT_PREFERENCES: UserPreferences = {
  sanctuaryName: 'Jayesh',
  theme: 'system',
  pinnedMood: null,
  geminiApiKey: '',
  geminiModel: 'gemini-2.5-flash',
  chatbotRole: 'companion',
  soundEnabled: true,
  memoryConsent: 'persisted'
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

  static getChatMessages(): ChatMessage[] {
    const prefs = this.getPreferences();
    if (prefs.memoryConsent === 'ephemeral') {
      return ephemeralChatHistory;
    }

    try {
      const data = localStorage.getItem(CHAT_KEY);
      if (data) return JSON.parse(data);
    } catch (e) {
      console.warn('Could not read chat messages:', e);
    }

    return [];
  }

  static saveChatMessages(messages: ChatMessage[]): void {
    const prefs = this.getPreferences();
    if (prefs.memoryConsent === 'ephemeral') {
      ephemeralChatHistory = messages;
      return;
    }

    try {
      localStorage.setItem(CHAT_KEY, JSON.stringify(messages));
    } catch (e) {
      console.warn('Could not save chat messages:', e);
    }
  }

  static clearChatHistory(): void {
    ephemeralChatHistory = [];
    try {
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
