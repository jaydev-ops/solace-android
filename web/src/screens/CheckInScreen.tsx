import React, { useState } from 'react';
import { useSanctuary } from '../context/SanctuaryContext';
import { MOOD_DEFINITIONS, MoodId } from '../types/solace';
import {
  Heart,
  Sparkles,
  Check,
  Smile,
  ShieldCheck,
  Sliders,
  Tag
} from 'lucide-react';

export const CheckInScreen: React.FC = () => {
  const { activeMood, addMoodCheckIn, setActiveScreen } = useSanctuary();

  const [selectedMood, setSelectedMood] = useState<MoodId>(activeMood);
  const [intensity, setIntensity] = useState<number>(4);
  const [selectedTags, setSelectedTags] = useState<string[]>(['Morning quiet', 'Peace of mind']);
  const [noteText, setNoteText] = useState<string>('');
  const [isSaved, setIsSaved] = useState<boolean>(false);

  const moodConfig = MOOD_DEFINITIONS[selectedMood];

  const handleToggleTag = (tag: string) => {
    if (selectedTags.includes(tag)) {
      setSelectedTags(selectedTags.filter((t) => t !== tag));
    } else {
      setSelectedTags([...selectedTags, tag]);
    }
  };

  const handleSave = () => {
    addMoodCheckIn(
      selectedMood,
      moodConfig.defaultValence,
      intensity,
      selectedTags,
      noteText.trim()
    );

    setIsSaved(true);
    setTimeout(() => {
      setIsSaved(false);
      setActiveScreen('home');
    }, 1800);
  };

  const getIntensityLabel = (val: number) => {
    if (val <= 3) return 'Gentle • Subtle flutter';
    if (val <= 6) return 'Present • Noticeable space';
    if (val <= 8) return 'Strong • Colored heavily';
    return 'Intense • Taking up all room';
  };

  return (
    <div style={{ maxWidth: '680px', margin: '0 auto', width: '100%', padding: '8px 20px 100px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
      {/* Title */}
      <div>
        <h1 style={{ fontSize: '1.85rem', fontWeight: 800, margin: 0 }}>
          How is your heart feeling right now?
        </h1>
        <p style={{ fontSize: '0.92rem', color: 'var(--solace-secondary-text)', marginTop: '4px' }}>
          Validating whatever is present without judgment, scores, or streaks.
        </p>
      </div>

      {/* Mood Selector Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(130px, 1fr))', gap: '10px' }}>
        {(Object.keys(MOOD_DEFINITIONS) as MoodId[]).map((key) => {
          const item = MOOD_DEFINITIONS[key];
          const isSelected = selectedMood === key;
          return (
            <button
              key={key}
              type="button"
              onClick={() => {
                setSelectedMood(key);
                // Reset tags to mood default candidates
                setSelectedTags(item.contextualTags.slice(0, 2));
              }}
              style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                padding: '16px 8px',
                borderRadius: 'var(--radius-xl)',
                backgroundColor: isSelected ? 'var(--active-mood-bg)' : 'var(--solace-card-bg)',
                border: isSelected
                  ? '2px solid var(--active-mood-primary)'
                  : '1px solid var(--solace-border)',
                cursor: 'pointer',
                transition: 'all 0.2s cubic-bezier(0.16, 1, 0.3, 1)',
                boxShadow: isSelected ? '0 4px 16px var(--active-mood-glow)' : 'var(--solace-card-shadow)',
                transform: isSelected ? 'scale(1.02)' : 'none'
              }}
            >
              <span style={{ fontSize: '2rem', marginBottom: '6px' }}>{item.emoji}</span>
              <span style={{ fontSize: '0.95rem', fontWeight: 700, color: 'var(--solace-primary-text)' }}>
                {item.title}
              </span>
              <span style={{ fontSize: '0.72rem', color: 'var(--solace-secondary-text)', textAlign: 'center' }}>
                {item.subtitle}
              </span>
            </button>
          );
        })}
      </div>

      {/* Resonance Banner */}
      <div
        className="solace-card"
        style={{
          padding: '14px 18px',
          backgroundColor: 'var(--active-mood-bg)',
          borderColor: 'var(--active-mood-border)',
          display: 'flex',
          alignItems: 'center',
          gap: '10px'
        }}
      >
        <Sparkles size={18} style={{ color: 'var(--active-mood-primary)' }} />
        <span style={{ fontSize: '0.88rem', fontWeight: 600, color: 'var(--active-mood-primary)' }}>
          {moodConfig.resonanceBadge}
        </span>
      </div>

      {/* Intensity Slider */}
      <div className="solace-card" style={{ padding: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Sliders size={18} style={{ color: 'var(--active-mood-primary)' }} />
            <h3 style={{ fontSize: '1rem', fontWeight: 700, margin: 0 }}>Emotional Intensity</h3>
          </div>
          <span style={{ fontSize: '0.82rem', fontWeight: 600, color: 'var(--active-mood-primary)' }}>
            {intensity} / 10 • {getIntensityLabel(intensity)}
          </span>
        </div>

        <input
          type="range"
          min="1"
          max="10"
          value={intensity}
          onChange={(e) => setIntensity(Number(e.target.value))}
          style={{
            width: '100%',
            height: '8px',
            borderRadius: 'var(--radius-pill)',
            accentColor: 'var(--active-mood-primary)',
            cursor: 'pointer'
          }}
        />
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.72rem', color: 'var(--solace-secondary-text)', marginTop: '8px' }}>
          <span>Gentle whisper (1)</span>
          <span>Moderate presence (5)</span>
          <span>Deep surge (10)</span>
        </div>
      </div>

      {/* Contextual Situational Tags */}
      <div className="solace-card" style={{ padding: '20px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '12px' }}>
          <Tag size={18} style={{ color: 'var(--active-mood-primary)' }} />
          <h3 style={{ fontSize: '1rem', fontWeight: 700, margin: 0 }}>
            What is coloring your emotional space?
          </h3>
        </div>

        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
          {moodConfig.contextualTags.map((tag) => {
            const isChosen = selectedTags.includes(tag);
            return (
              <button
                key={tag}
                type="button"
                onClick={() => handleToggleTag(tag)}
                className={`tag-chip ${isChosen ? 'selected' : ''}`}
              >
                {isChosen && <Check size={13} />}
                <span>{tag}</span>
              </button>
            );
          })}
        </div>
      </div>

      {/* Dynamic Reflection Note Field */}
      <div className="solace-card" style={{ padding: '20px' }}>
        <h3 style={{ fontSize: '1rem', fontWeight: 700, margin: '0 0 4px' }}>
          {moodConfig.reflectionPromptTitle}
        </h3>
        <p style={{ fontSize: '0.82rem', color: 'var(--solace-secondary-text)', margin: '0 0 14px', lineHeight: 1.5 }}>
          {moodConfig.reflectionPromptSubtitle}
        </p>

        <textarea
          value={noteText}
          onChange={(e) => setNoteText(e.target.value)}
          placeholder={moodConfig.reflectionPlaceholder}
          className="input-field"
          rows={4}
        />
      </div>

      {/* Success Notification */}
      {isSaved && (
        <div
          style={{
            backgroundColor: 'var(--solace-success-soft)',
            border: '1px solid var(--solace-success)',
            color: 'var(--solace-success)',
            padding: '14px 20px',
            borderRadius: 'var(--radius-lg)',
            display: 'flex',
            alignItems: 'center',
            gap: '10px',
            fontWeight: 600,
            fontSize: '0.92rem'
          }}
        >
          <Check size={20} />
          <span>Your reflection has been safely anchored into your sanctuary.</span>
        </div>
      )}

      {/* Actions */}
      <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end', marginTop: '4px' }}>
        <button
          type="button"
          onClick={() => setActiveScreen('companion')}
          className="btn-secondary"
        >
          <Sparkles size={18} />
          <span>Unpack with Solace AI</span>
        </button>

        <button
          type="button"
          onClick={handleSave}
          className="btn-primary"
          style={{ minWidth: '160px' }}
        >
          <Heart size={18} />
          <span>Save Check-in</span>
        </button>
      </div>
    </div>
  );
};
