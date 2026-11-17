import React from 'react';
import { useSanctuary, ScreenId } from '../context/SanctuaryContext';
import { ArrowLeft, ShieldAlert, Volume2, VolumeX } from 'lucide-react';

interface HeaderProps {
  subtitle?: string;
  onBack?: () => void;
  showBack?: boolean;
}

export const Header: React.FC<HeaderProps> = ({ subtitle, onBack, showBack }) => {
  const { preferences, activeScreen, setActiveScreen, setIsEmergencyModalOpen, soundEnabled, toggleSound } = useSanctuary();

  const getSubtitle = () => {
    if (subtitle) return subtitle;
    switch (activeScreen) {
      case 'home':
        return 'peaceful haven';
      case 'companion':
        return 'peaceful haven';
      case 'checkin':
        return 'emotional space';
      case 'journal':
        return 'private harbor';
      case 'insights':
        return 'emotional sanctuary analytics';
      case 'regulation':
        return 'somatic regulation';
      case 'settings':
        return 'sanctuary settings';
      default:
        return 'peaceful haven';
    }
  };

  const initial = preferences.sanctuaryName?.trim()
    ? preferences.sanctuaryName.trim().charAt(0).toUpperCase()
    : 'S';

  return (
    <header className="solace-top-bar" aria-label="Solace Top Bar">
      {/* Left: Back button (if on sub-screen) OR Wordmark */}
      {showBack ? (
        <button
          onClick={onBack || (() => setActiveScreen('home'))}
          className="btn-ghost"
          style={{ padding: '8px 12px', display: 'flex', alignItems: 'center', gap: '6px', color: 'var(--solace-primary-text)' }}
          aria-label="Navigate back to Sanctuary Home"
        >
          <ArrowLeft size={20} />
          <span style={{ fontWeight: 600, fontSize: '0.95rem' }}>Back</span>
        </button>
      ) : (
        <div
          className="solace-wordmark"
          onClick={() => setActiveScreen('home')}
          title="Solace+"
        >
          <span className="solace-wordmark-text">solace</span>
          <div className="solace-plus-badge">+</div>
        </div>
      )}

      {/* Right: Subtitle & Avatar */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
        <span
          style={{
            fontSize: '11px',
            color: 'var(--solace-secondary-text)',
            fontWeight: 500
          }}
        >
          {getSubtitle()}
        </span>

        {/* Audio Toggle */}
        <button
          onClick={toggleSound}
          className="btn-ghost"
          style={{ padding: '6px', borderRadius: '50%', color: 'var(--solace-secondary-text)' }}
          title={soundEnabled ? 'Soothing chimes active' : 'Chimes muted'}
          aria-label="Toggle sound"
        >
          {soundEnabled ? <Volume2 size={18} /> : <VolumeX size={18} />}
        </button>

        {/* Emergency Helplines Quick Icon */}
        <button
          onClick={() => setIsEmergencyModalOpen(true)}
          className="btn-ghost"
          style={{ padding: '6px', borderRadius: '50%', color: 'var(--solace-emergency)' }}
          title="Crisis Helplines (112 / 14416 / 988)"
          aria-label="Crisis support"
        >
          <ShieldAlert size={18} />
        </button>

        {/* Avatar */}
        <div
          className="solace-avatar"
          onClick={() => setActiveScreen('settings')}
          title="Open Settings"
          aria-label="Open Settings"
        >
          {initial}
        </div>
      </div>
    </header>
  );
};
