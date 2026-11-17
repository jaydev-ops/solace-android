import React from 'react';
import { SanctuaryProvider, useSanctuary } from './context/SanctuaryContext';
import { Header } from './components/Header';
import { Navigation } from './components/Navigation';
import { EmergencyModal } from './components/EmergencyModal';
import { MemoryPermissionModal } from './components/MemoryPermissionModal';

import { HomeScreen } from './screens/HomeScreen';
import { CheckInScreen } from './screens/CheckInScreen';
import { CompanionScreen } from './screens/CompanionScreen';
import { RegulationScreen } from './screens/RegulationScreen';
import { JournalScreen } from './screens/JournalScreen';
import { InsightsScreen } from './screens/InsightsScreen';
import { SettingsScreen } from './screens/SettingsScreen';
import { LoginScreen } from './screens/LoginScreen';

const MainShell: React.FC = () => {
  const { activeScreen, setActiveScreen, isLoggedIn } = useSanctuary();

  if (!isLoggedIn) {
    return <LoginScreen />;
  }

  const isSubScreen = activeScreen === 'regulation' || activeScreen === 'settings';

  const renderActiveScreen = () => {
    switch (activeScreen) {
      case 'home':
        return <HomeScreen />;
      case 'checkin':
        return <CheckInScreen />;
      case 'companion':
        return <CompanionScreen />;
      case 'regulation':
        return <RegulationScreen />;
      case 'journal':
        return <JournalScreen />;
      case 'insights':
        return <InsightsScreen />;
      case 'settings':
        return <SettingsScreen />;
      default:
        return <HomeScreen />;
    }
  };

  return (
    <div className="ambient-sanctuary-canvas">
      {/* Top Bar with Wordmark or Back navigation */}
      <Header
        showBack={isSubScreen}
        onBack={() => setActiveScreen('home')}
      />

      {/* Main Content Area */}
      <main style={{ flex: 1, display: 'flex', flexDirection: 'column', minHeight: 0 }}>
        {renderActiveScreen()}
      </main>

      {/* Bottom Navigation Bar */}
      <Navigation />

      {/* Global Dialogs */}
      <EmergencyModal />
      <MemoryPermissionModal />
    </div>
  );
};

export const App: React.FC = () => {
  return (
    <SanctuaryProvider>
      <MainShell />
    </SanctuaryProvider>
  );
};

export default App;
