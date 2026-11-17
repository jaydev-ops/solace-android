import { StorageService } from '../services/storageService.ts';

function assert(condition: boolean, msg: string) {
  if (!condition) {
    throw new Error(`❌ ASSERTION FAILED: ${msg}`);
  }
}

console.log('--- STARTING SOLACE+ LOGIN & CHAT HISTORY VERIFICATION ---');

// Mock localStorage for Node test environment if not present
if (typeof localStorage === 'undefined') {
  const store: Record<string, string> = {};
  (globalThis as any).localStorage = {
    getItem: (key: string) => store[key] || null,
    setItem: (key: string, val: string) => {
      store[key] = val;
    },
    removeItem: (key: string) => {
      delete store[key];
    },
    clear: () => {
      for (const k of Object.keys(store)) delete store[k];
    }
  };
}

localStorage.clear();

// 1. Initial State: not logged in, no sanctuary name
const initialPrefs = StorageService.getPreferences();
assert(!initialPrefs.isLoggedIn, 'Initially, user must not be logged in');
assert(!initialPrefs.sanctuaryName, 'Initially, sanctuary name must be empty');

// 2. Known Users Tracking
assert(StorageService.getKnownUsers().length === 0, 'Initially known users list is empty');
StorageService.addKnownUser('Maya');
StorageService.addKnownUser('Alex');
assert(StorageService.getKnownUsers().length === 2, 'Must have 2 known users');
assert(StorageService.getKnownUsers().includes('Maya'), 'Must include Maya');
assert(StorageService.getKnownUsers().includes('Alex'), 'Must include Alex');

// 3. User-Scoped Chat History for User 1: Maya
StorageService.saveChatMessages(
  [
    { id: '1', role: 'user', text: 'Hello Solace, I am Maya.', timestamp: 1000 },
    { id: '2', role: 'assistant', text: 'Welcome to your sanctuary, Maya.', timestamp: 2000 }
  ],
  'Maya'
);

const mayaMessages = StorageService.getChatMessages('Maya');
assert(mayaMessages.length === 2, 'Maya must have 2 messages');
assert(mayaMessages[0].text === 'Hello Solace, I am Maya.', 'Maya msg 1 matches');

// 4. User-Scoped Chat History for User 2: Alex (Separation of Histories)
const alexInitialMessages = StorageService.getChatMessages('Alex');
assert(alexInitialMessages.length === 0, 'Alex must start with empty history');

StorageService.saveChatMessages(
  [
    { id: '3', role: 'user', text: 'Hey, I am Alex.', timestamp: 3000 }
  ],
  'Alex'
);

const alexMessages = StorageService.getChatMessages('Alex');
assert(alexMessages.length === 1, 'Alex must have 1 message');
assert(alexMessages[0].text === 'Hey, I am Alex.', 'Alex msg matches');

// Ensure Maya still has her 2 messages intact!
const mayaMessagesAfter = StorageService.getChatMessages('Maya');
assert(mayaMessagesAfter.length === 2, 'Maya history must remain completely intact');

// 5. User Removal
StorageService.removeKnownUser('Alex');
assert(StorageService.getKnownUsers().length === 1, 'After removal, 1 known user remains');
assert(StorageService.getKnownUsers()[0] === 'Maya', 'Remaining user is Maya');

console.log('✅ ALL LOGIN & CHAT HISTORY VERIFICATIONS PASSED (5/5)!');
