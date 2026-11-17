import React, { useState } from 'react';
import { useSanctuary } from '../context/SanctuaryContext';
import { MOOD_DEFINITIONS, MoodId, ThemeMode } from '../types/solace';
import { StorageService } from '../services/storageService';
import {
  Settings,
  User,
  Palette,
  Key,
  Bot,
  Volume2,
  VolumeX,
  ShieldCheck,
  Download,
  Trash2,
  Check,
  Eye,
  EyeOff,
  ExternalLink,
  ShieldAlert,
  Sparkles
} from 'lucide-react';

export const SettingsScreen: React.FC = () => {
  const {
    preferences,
    updatePreferences,
    setIsMemoryModalOpen,
    setIsEmergencyModalOpen
  } = useSanctuary();

  const [nameInput, setNameInput] = useState(preferences.sanctuaryName || '');
  const [apiKeyInput, setApiKeyInput] = useState(preferences.geminiApiKey || '');
  const [showApiKey, setShowApiKey] = useState(false);
  const [isSavedAlert, setIsSavedAlert] = useState(false);

  const handleSaveSettings = (e: React.FormEvent) => {
    e.preventDefault();
    updatePreferences({
      sanctuaryName: nameInput.trim(),
      geminiApiKey: apiKeyInput.trim()
    });

    setIsSavedAlert(true);
    setTimeout(() => setIsSavedAlert(false), 2200);
  };

  const handleExport = () => {
    StorageService.exportAllData();
  };

  const handleClear = () => {
    if (
      confirm(
        'Are you sure you want to completely erase all your mood history, journal entries, and sanctuary settings from this device? This cannot be undone.'
      )
    ) {
      StorageService.clearAllData();
      window.location.reload();
    }
  };

  return (
    <div
      style={{
        maxWidth: '680px',
        margin: '0 auto',
        width: '100%',
        padding: '8px 20px 100px',
        display: 'flex',
        flexDirection: 'column',
        gap: '20px'
      }}
    >
      {/* Title */}
      <div>
        <h1 style={{ fontSize: '1.85rem', fontWeight: 800, margin: 0 }}>Sanctuary Settings</h1>
        <p style={{ fontSize: '0.92rem', color: 'var(--solace-secondary-text)', marginTop: '4px' }}>
          Personalize your sanctuary atmosphere, AI keys, and local memory privacy.
        </p>
      </div>

      {isSavedAlert && (
        <div
          style={{
            backgroundColor: 'var(--solace-success-soft)',
            border: '1px solid var(--solace-success)',
            color: 'var(--solace-success)',
            padding: '12px 18px',
            borderRadius: 'var(--radius-lg)',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            fontWeight: 600,
            fontSize: '0.9rem'
          }}
        >
          <Check size={18} />
          <span>Sanctuary settings updated successfully.</span>
        </div>
      )}

      <form onSubmit={handleSaveSettings} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
        {/* Sanctuary Name & Identity */}
        <div className="solace-card" style={{ padding: '22px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '14px' }}>
            <User size={18} style={{ color: 'var(--active-mood-primary)' }} />
            <h3 style={{ fontSize: '1.05rem', fontWeight: 700, margin: 0 }}>Sanctuary Name</h3>
          </div>
          <p style={{ fontSize: '0.85rem', color: 'var(--solace-secondary-text)', margin: '0 0 10px', lineHeight: 1.5 }}>
            How Solace greets you when you enter the sanctuary.
          </p>
          <input
            type="text"
            value={nameInput}
            onChange={(e) => setNameInput(e.target.value)}
            placeholder="e.g. Jayesh"
            className="input-field"
            style={{ maxWidth: '380px' }}
          />
        </div>

        {/* Visual Appearance & Theme */}
        <div className="solace-card" style={{ padding: '22px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '14px' }}>
            <Palette size={18} style={{ color: 'var(--active-mood-primary)' }} />
            <h3 style={{ fontSize: '1.05rem', fontWeight: 700, margin: 0 }}>Theme & Visual Atmosphere</h3>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {/* Theme Mode Segmented Control */}
            <div>
              <span style={{ fontSize: '0.85rem', fontWeight: 600, display: 'block', marginBottom: '8px' }}>
                Color Palette
              </span>
              <div className="segmented-control" style={{ maxWidth: '440px' }}>
                <button
                  type="button"
                  onClick={() => updatePreferences({ theme: 'system' })}
                  className={`segmented-option ${preferences.theme === 'system' ? 'active' : ''}`}
                >
                  Follow System
                </button>
                <button
                  type="button"
                  onClick={() => updatePreferences({ theme: 'light' })}
                  className={`segmented-option ${preferences.theme === 'light' ? 'active' : ''}`}
                >
                  Warm Sanctuary (Light)
                </button>
                <button
                  type="button"
                  onClick={() => updatePreferences({ theme: 'dark' })}
                  className={`segmented-option ${preferences.theme === 'dark' ? 'active' : ''}`}
                >
                  Warm Obsidian (Dark)
                </button>
              </div>
            </div>

            {/* Pinned Mood Theme Override */}
            <div>
              <span style={{ fontSize: '0.85rem', fontWeight: 600, display: 'block', marginBottom: '8px' }}>
                Pinned Mood Palette Tint
              </span>
              <p style={{ fontSize: '0.8rem', color: 'var(--solace-secondary-text)', margin: '0 0 10px' }}>
                Lock your sanctuary's color accents to a specific emotion, or leave dynamic to match your latest check-in.
              </p>

              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
                <button
                  type="button"
                  onClick={() => updatePreferences({ pinnedMood: null })}
                  className={`tag-chip ${preferences.pinnedMood === null ? 'selected' : ''}`}
                >
                  <span>✨ Dynamic (Latest Mood)</span>
                </button>

                {(Object.keys(MOOD_DEFINITIONS) as MoodId[]).map((key) => {
                  const def = MOOD_DEFINITIONS[key];
                  const isSelected = preferences.pinnedMood === key;
                  return (
                    <button
                      key={key}
                      type="button"
                      onClick={() => updatePreferences({ pinnedMood: key })}
                      className={`tag-chip ${isSelected ? 'selected' : ''}`}
                    >
                      <span>{def.emoji}</span>
                      <span>{def.title}</span>
                    </button>
                  );
                })}
              </div>
            </div>
          </div>
        </div>

        {/* Gemini AI Engine Configuration */}
        <div className="solace-card" style={{ padding: '22px' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '14px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Key size={18} style={{ color: 'var(--active-mood-primary)' }} />
              <h3 style={{ fontSize: '1.05rem', fontWeight: 700, margin: 0 }}>
                Google Gemini AI Configuration
              </h3>
            </div>
            <a
              href="https://aistudio.google.com/app/apikey"
              target="_blank"
              rel="noopener noreferrer"
              style={{
                fontSize: '0.8rem',
                color: 'var(--active-mood-primary)',
                textDecoration: 'none',
                display: 'flex',
                alignItems: 'center',
                gap: '4px',
                fontWeight: 600
              }}
            >
              <span>Get Free Key</span>
              <ExternalLink size={13} />
            </a>
          </div>

          <p style={{ fontSize: '0.85rem', color: 'var(--solace-secondary-text)', margin: '0 0 12px', lineHeight: 1.5 }}>
            You can enter your personal Gemini API Key here for direct, private calls to Google Gemini AI models. If left blank, Solace automatically runs on the offline Adaptive Sanctuary Engine.
          </p>

          <div style={{ position: 'relative', marginBottom: '16px' }}>
            <input
              type={showApiKey ? 'text' : 'password'}
              value={apiKeyInput}
              onChange={(e) => setApiKeyInput(e.target.value)}
              placeholder="Paste AI Studio API key here (AIzaSy...)"
              className="input-field"
              style={{ paddingRight: '44px', fontFamily: 'monospace' }}
            />
            <button
              type="button"
              onClick={() => setShowApiKey(!showApiKey)}
              className="btn-ghost"
              style={{
                position: 'absolute',
                right: '8px',
                top: '50%',
                transform: 'translateY(-50%)',
                padding: '6px'
              }}
              aria-label={showApiKey ? 'Hide API key' : 'Show API key'}
            >
              {showApiKey ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '12px' }}>
            <div>
              <label style={{ fontSize: '0.82rem', fontWeight: 600, display: 'block', marginBottom: '6px' }}>
                AI Model Target
              </label>
              <select
                value={preferences.geminiModel}
                onChange={(e) => updatePreferences({ geminiModel: e.target.value as any })}
                className="input-field"
                style={{ fontSize: '0.88rem' }}
              >
                <option value="gemini-2.5-flash">Gemini 2.5 Flash (Ultra-responsive)</option>
                <option value="gemini-2.0-flash">Gemini 2.0 Flash (Balanced)</option>
                <option value="gemini-1.5-flash">Gemini 1.5 Flash (Reliable)</option>
              </select>
            </div>

            <div>
              <label style={{ fontSize: '0.82rem', fontWeight: 600, display: 'block', marginBottom: '6px' }}>
                Companion Persona
              </label>
              <select
                value={preferences.chatbotRole}
                onChange={(e) => updatePreferences({ chatbotRole: e.target.value as any })}
                className="input-field"
                style={{ fontSize: '0.88rem' }}
              >
                <option value="companion">Sanctuary Holding Presence</option>
                <option value="somatic">Somatic & Polyvagal Guide</option>
                <option value="cbt">Thought Clarifier (Gentle CBT)</option>
                <option value="wisdom">Deep Wisdom Mirror</option>
              </select>
            </div>
          </div>
        </div>

        {/* Auditory Atmosphere */}
        <div className="solace-card" style={{ padding: '22px' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              {preferences.soundEnabled ? (
                <Volume2 size={20} style={{ color: 'var(--active-mood-primary)' }} />
              ) : (
                <VolumeX size={20} style={{ color: 'var(--solace-muted-text)' }} />
              )}
              <div>
                <h3 style={{ fontSize: '1rem', fontWeight: 700, margin: 0 }}>
                  Somatic Singing Bowl Chimes
                </h3>
                <p style={{ fontSize: '0.8rem', color: 'var(--solace-secondary-text)', margin: '2px 0 0' }}>
                  Acoustic 432Hz sine sound cues during breath phases and check-ins.
                </p>
              </div>
            </div>

            <button
              type="button"
              onClick={() => updatePreferences({ soundEnabled: !preferences.soundEnabled })}
              className={`tag-chip ${preferences.soundEnabled ? 'selected' : ''}`}
              style={{ padding: '6px 14px' }}
            >
              {preferences.soundEnabled ? 'Sound Enabled' : 'Muted'}
            </button>
          </div>
        </div>

        {/* Save button */}
        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
          <button type="submit" className="btn-primary" style={{ minWidth: '170px' }}>
            <Check size={18} />
            <span>Save Preferences</span>
          </button>
        </div>
      </form>

      {/* Privacy, Storage & Sovereign Data */}
      <div className="solace-card" style={{ padding: '22px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '14px' }}>
          <ShieldCheck size={18} style={{ color: 'var(--solace-success)' }} />
          <h3 style={{ fontSize: '1.05rem', fontWeight: 700, margin: 0 }}>
            Data Sovereignty & Privacy
          </h3>
        </div>

        <p style={{ fontSize: '0.85rem', color: 'var(--solace-secondary-text)', margin: '0 0 16px', lineHeight: 1.5 }}>
          Current mode:{' '}
          <strong style={{ color: 'var(--solace-primary-text)' }}>
            {preferences.memoryConsent === 'persisted'
              ? 'Local Vault Saving (Persisted)'
              : 'Ephemeral (Session Only)'}
          </strong>
          . All data resides solely in your browser.
        </p>

        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '10px' }}>
          <button
            type="button"
            onClick={() => setIsMemoryModalOpen(true)}
            className="btn-secondary"
            style={{ fontSize: '0.85rem' }}
          >
            Change Memory Permissions
          </button>

          <button
            type="button"
            onClick={handleExport}
            className="btn-secondary"
            style={{ fontSize: '0.85rem' }}
          >
            <Download size={15} />
            <span>Export Data (JSON)</span>
          </button>

          <button
            type="button"
            onClick={handleClear}
            className="btn-ghost"
            style={{ color: 'var(--solace-emergency)', fontSize: '0.85rem' }}
          >
            <Trash2 size={15} />
            <span>Erase All Local Data</span>
          </button>
        </div>
      </div>

      {/* Emergency Crisis Resources Button */}
      <div
        className="solace-card"
        style={{
          padding: '20px',
          borderColor: 'rgba(198, 40, 40, 0.3)',
          backgroundColor: 'var(--solace-emergency-soft)'
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '10px' }}>
          <div>
            <h3 style={{ fontSize: '1rem', fontWeight: 700, color: 'var(--solace-emergency)', margin: 0 }}>
              Need immediate support?
            </h3>
            <p style={{ fontSize: '0.82rem', color: 'var(--solace-primary-text)', margin: '2px 0 0' }}>
              Free 24/7 helplines in India (112, 14416) and US/Canada (988).
            </p>
          </div>
          <button
            type="button"
            onClick={() => setIsEmergencyModalOpen(true)}
            className="btn-primary btn-emergency"
            style={{ minHeight: '40px', padding: '8px 16px', fontSize: '0.85rem' }}
          >
            <ShieldAlert size={16} />
            <span>Open Crisis Resources</span>
          </button>
        </div>
      </div>
    </div>
  );
};
