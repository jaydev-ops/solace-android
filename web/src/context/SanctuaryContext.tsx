import React, { createContext, useContext, useEffect, useState } from 'react';
import {
  ChatMessage,
  CURATED_REGULATION_ACTIVITIES,
  JournalEntry,
  MoodCheckIn,
  MoodId,
  RegulationActivity,
  RegulationRecord,
  ThemeMode,
  UserPreferences
} from '../types/solace';
import { StorageService } from '../services/storageService';
import { soundManager } from '../services/audioService';

export type ScreenId = 'home' | 'checkin' | 'companion' | 'regulation' | 'journal' | 'insights' | 'settings';

interface SanctuaryContextType {
  preferences: UserPreferences;
  activeScreen: ScreenId;
  setActiveScreen: (screen: ScreenId) => void;
  activeMood: MoodId;
  effectiveTheme: 'light' | 'dark';
  soundEnabled: boolean;
  toggleSound: () => void;
  updatePreferences: (updates: Partial<UserPreferences>) => void;
  
  // Moods
  moods: MoodCheckIn[];
  addMoodCheckIn: (mood: MoodId, valence: number, intensity: number, tags: string[], note: string) => void;
  
  // Journal
  journalEntries: JournalEntry[];
  addJournalEntry: (title: string, content: string, associatedMood?: MoodId, tags?: string[]) => void;
  deleteJournalEntry: (id: string) => void;
  
  // Somatic Regulation
  regulationRecords: RegulationRecord[];
  addRegulationRecord: (activity: RegulationActivity, rating: number, note?: string) => void;
  activeRegulationActivity: RegulationActivity | null;
  startRegulationActivity: (activity: RegulationActivity) => void;
  stopRegulationActivity: () => void;

  // Companion Chat
  chatMessages: ChatMessage[];
  setChatMessages: React.Dispatch<React.SetStateAction<ChatMessage[]>>;
  clearChat: () => void;

  // Authentication & Identity
  isLoggedIn: boolean;
  login: (name: string) => void;
  logout: () => void;
  knownUsers: string[];
  removeKnownUser: (name: string) => void;

  // Dialogs
  isEmergencyModalOpen: boolean;
  setIsEmergencyModalOpen: (open: boolean) => void;
  isMemoryModalOpen: boolean;
  setIsMemoryModalOpen: (open: boolean) => void;
}

const SanctuaryContext = createContext<SanctuaryContextType | null>(null);

export const SanctuaryProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [preferences, setPreferences] = useState<UserPreferences>(() => StorageService.getPreferences());
  const [activeScreen, setActiveScreen] = useState<ScreenId>('home');
  const [moods, setMoods] = useState<MoodCheckIn[]>(() => StorageService.getMoods());
  const [journalEntries, setJournalEntries] = useState<JournalEntry[]>(() => StorageService.getJournalEntries());
  const [regulationRecords, setRegulationRecords] = useState<RegulationRecord[]>(() => StorageService.getRegulationRecords());
  const [chatMessages, setChatMessages] = useState<ChatMessage[]>(() => StorageService.getChatMessages(preferences.sanctuaryName));
  const [knownUsers, setKnownUsers] = useState<string[]>(() => StorageService.getKnownUsers());
  const [activeRegulationActivity, setActiveRegulationActivity] = useState<RegulationActivity | null>(null);

  const [isEmergencyModalOpen, setIsEmergencyModalOpen] = useState(false);
  const [isMemoryModalOpen, setIsMemoryModalOpen] = useState(false);

  // System theme detection
  const [systemIsDark, setSystemIsDark] = useState(() => {
    if (typeof window !== 'undefined') {
      return window.matchMedia('(prefers-color-scheme: dark)').matches;
    }
    return false;
  });

  useEffect(() => {
    if (typeof window === 'undefined') return;
    const media = window.matchMedia('(prefers-color-scheme: dark)');
    const handler = (e: MediaQueryListEvent) => setSystemIsDark(e.matches);
    media.addEventListener('change', handler);
    return () => media.removeEventListener('change', handler);
  }, []);

  const effectiveTheme: 'light' | 'dark' =
    preferences.theme === 'system'
      ? systemIsDark
        ? 'dark'
        : 'light'
      : preferences.theme;

  // Active mood: use pinnedMood if set, otherwise use the latest check-in, or fallback to 'calm'
  const activeMood: MoodId =
    preferences.pinnedMood || (moods.length > 0 ? moods[0].mood : 'calm');

  // Apply data-theme and data-mood to :root
  useEffect(() => {
    const root = document.documentElement;
    root.setAttribute('data-theme', effectiveTheme);
    root.setAttribute('data-mood', activeMood);
  }, [effectiveTheme, activeMood]);

  // Sync sound manager
  useEffect(() => {
    soundManager.setEnabled(preferences.soundEnabled);
  }, [preferences.soundEnabled]);

  // Sync chat messages to storage
  useEffect(() => {
    StorageService.saveChatMessages(chatMessages, preferences.sanctuaryName);
  }, [chatMessages, preferences.sanctuaryName]);

  const updatePreferences = (updates: Partial<UserPreferences>) => {
    setPreferences((prev) => {
      const next = { ...prev, ...updates };
      StorageService.savePreferences(next);
      return next;
    });
  };

  const toggleSound = () => {
    updatePreferences({ soundEnabled: !preferences.soundEnabled });
  };

  const addMoodCheckIn = (
    mood: MoodId,
    valence: number,
    intensity: number,
    tags: string[],
    note: string
  ) => {
    const newEntry: MoodCheckIn = {
      id: 'mood-' + Date.now(),
      timestamp: Date.now(),
      mood,
      valence,
      intensity,
      tags,
      note
    };

    StorageService.saveMood(newEntry);
    setMoods((prev) => [newEntry, ...prev]);

    // Provide gentle auditory chime on check-in
    soundManager.playSingingBowlChime(432, 2.5);
  };

  const addJournalEntry = (
    title: string,
    content: string,
    associatedMood?: MoodId,
    tags: string[] = []
  ) => {
    const words = content.trim().split(/\s+/).filter(Boolean).length;
    const newEntry: JournalEntry = {
      id: 'journal-' + Date.now(),
      timestamp: Date.now(),
      title: title.trim() || 'Untitled Reflection',
      content,
      associatedMood: associatedMood || activeMood,
      tags,
      wordCount: words
    };

    StorageService.saveJournalEntry(newEntry);
    setJournalEntries((prev) => [newEntry, ...prev]);
    soundManager.playSingingBowlChime(528, 2.0);
  };

  const deleteJournalEntry = (id: string) => {
    StorageService.deleteJournalEntry(id);
    setJournalEntries((prev) => prev.filter((e) => e.id !== id));
  };

  const addRegulationRecord = (activity: RegulationActivity, rating: number, note?: string) => {
    const newRecord: RegulationRecord = {
      id: 'reg-' + Date.now(),
      activityId: activity.id,
      activityTitle: activity.title,
      timestamp: Date.now(),
      durationSeconds: activity.durationSeconds,
      rating,
      note
    };

    StorageService.saveRegulationRecord(newRecord);
    setRegulationRecords((prev) => [newRecord, ...prev]);
    soundManager.playSingingBowlChime(432, 3.0);
  };

  const startRegulationActivity = (activity: RegulationActivity) => {
    setActiveRegulationActivity(activity);
    setActiveScreen('regulation');
  };

  const stopRegulationActivity = () => {
    setActiveRegulationActivity(null);
  };

  const login = (name: string) => {
    const clean = name.trim();
    if (!clean) return;
    StorageService.addKnownUser(clean);
    setKnownUsers(StorageService.getKnownUsers());

    updatePreferences({
      sanctuaryName: clean,
      isLoggedIn: true
    });

    const userHistory = StorageService.getChatMessages(clean);
    if (userHistory.length === 0) {
      const welcomeMessage: ChatMessage = {
        id: 'msg-welcome-' + Date.now(),
        role: 'assistant',
        text: `Welcome to your emotional sanctuary, ${clean}. I am here to hold whatever is in your thoughts without judgment or rush.\n\nTake an unhurried breath. You can choose a regulation mode or speak whatever feels heavy today.`,
        timestamp: Date.now(),
        regulationMode: 'listen'
      };
      setChatMessages([welcomeMessage]);
      StorageService.saveChatMessages([welcomeMessage], clean);
    } else {
      setChatMessages(userHistory);
    }

    setActiveScreen('home');
    soundManager.playSingingBowlChime(432, 2.5);
  };

  const logout = () => {
    updatePreferences({
      isLoggedIn: false
    });
    setActiveScreen('home');
  };

  const removeKnownUser = (name: string) => {
    StorageService.removeKnownUser(name);
    setKnownUsers(StorageService.getKnownUsers());
  };

  const clearChat = () => {
    StorageService.clearChatHistory(preferences.sanctuaryName);
    const welcomeMessage: ChatMessage = {
      id: 'msg-welcome-' + Date.now(),
      role: 'assistant',
      text: `Welcome to your emotional sanctuary, ${preferences.sanctuaryName || 'friend'}. I am here to hold whatever is in your thoughts without judgment or rush.\n\nTake an unhurried breath. You can choose a regulation mode or speak whatever feels heavy today.`,
      timestamp: Date.now(),
      regulationMode: 'listen'
    };
    setChatMessages([welcomeMessage]);
    StorageService.saveChatMessages([welcomeMessage], preferences.sanctuaryName);
  };

  return (
    <SanctuaryContext.Provider
      value={{
        preferences,
        activeScreen,
        setActiveScreen,
        activeMood,
        effectiveTheme,
        soundEnabled: preferences.soundEnabled,
        toggleSound,
        updatePreferences,
        moods,
        addMoodCheckIn,
        journalEntries,
        addJournalEntry,
        deleteJournalEntry,
        regulationRecords,
        addRegulationRecord,
        activeRegulationActivity,
        startRegulationActivity,
        stopRegulationActivity,
        chatMessages,
        setChatMessages,
        clearChat,
        isLoggedIn: Boolean(preferences.isLoggedIn && preferences.sanctuaryName?.trim()),
        login,
        logout,
        knownUsers,
        removeKnownUser,
        isEmergencyModalOpen,
        setIsEmergencyModalOpen,
        isMemoryModalOpen,
        setIsMemoryModalOpen
      }}
    >
      {children}
    </SanctuaryContext.Provider>
  );
};

export const useSanctuary = (): SanctuaryContextType => {
  const context = useContext(SanctuaryContext);
  if (!context) {
    throw new Error('useSanctuary must be used within a SanctuaryProvider');
  }
  return context;
};
