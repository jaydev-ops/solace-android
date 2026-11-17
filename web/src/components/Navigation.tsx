import React from 'react';
import { useSanctuary, ScreenId } from '../context/SanctuaryContext';
import {
  Sparkles,
  Sun,
  BookOpen,
  BarChart2,
  Flower2
} from 'lucide-react';

export const Navigation: React.FC = () => {
  const { activeScreen, setActiveScreen } = useSanctuary();

  // Bottom navigation is hidden on sub-screens (Regulation and Settings) per SolaceApp.kt
  const isBottomNavVisible = activeScreen !== 'regulation' && activeScreen !== 'settings';

  if (!isBottomNavVisible) return null;

  return (
    <nav className="solace-bottom-nav-bar" aria-label="Solace Navigation">
      <div className="solace-bottom-nav-inner">
        {/* 1. Home */}
        <button
          onClick={() => setActiveScreen('home')}
          className={`solace-nav-btn ${activeScreen === 'home' ? 'active' : ''}`}
          aria-current={activeScreen === 'home' ? 'page' : undefined}
        >
          <Flower2 size={22} style={{ strokeWidth: activeScreen === 'home' ? 2.3 : 1.7 }} />
          <span>Home</span>
        </button>

        {/* 2. Check-In */}
        <button
          onClick={() => setActiveScreen('checkin')}
          className={`solace-nav-btn ${activeScreen === 'checkin' ? 'active' : ''}`}
          aria-current={activeScreen === 'checkin' ? 'page' : undefined}
        >
          <Sun size={22} style={{ strokeWidth: activeScreen === 'checkin' ? 2.3 : 1.7 }} />
          <span>Check-In</span>
        </button>

        {/* 3. Center Highlight: solace AI Floating Capsule */}
        <button
          onClick={() => setActiveScreen('companion')}
          className={`solace-ai-nav-capsule ${activeScreen === 'companion' ? 'active' : ''}`}
          aria-current={activeScreen === 'companion' ? 'page' : undefined}
          title="Solace AI Companion"
        >
          <Sparkles size={20} style={{ strokeWidth: 2.2 }} />
          <span>solace AI</span>
        </button>

        {/* 4. Journal */}
        <button
          onClick={() => setActiveScreen('journal')}
          className={`solace-nav-btn ${activeScreen === 'journal' ? 'active' : ''}`}
          aria-current={activeScreen === 'journal' ? 'page' : undefined}
        >
          <BookOpen size={22} style={{ strokeWidth: activeScreen === 'journal' ? 2.3 : 1.7 }} />
          <span>Journal</span>
        </button>

        {/* 5. Insights */}
        <button
          onClick={() => setActiveScreen('insights')}
          className={`solace-nav-btn ${activeScreen === 'insights' ? 'active' : ''}`}
          aria-current={activeScreen === 'insights' ? 'page' : undefined}
        >
          <BarChart2 size={22} style={{ strokeWidth: activeScreen === 'insights' ? 2.3 : 1.7 }} />
          <span>Insights</span>
        </button>
      </div>
    </nav>
  );
};
