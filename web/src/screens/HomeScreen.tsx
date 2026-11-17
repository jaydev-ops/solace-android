import React, { useState } from 'react';
import { useSanctuary } from '../context/SanctuaryContext';
import {
  MOOD_DEFINITIONS,
  MoodId
} from '../types/solace';
import {
  Flower2,
  Sun,
  Meh,
  Frown,
  AlertCircle,
  Flame,
  Check,
  ArrowRight,
  Moon,
  Sparkles,
  Wind
} from 'lucide-react';

export const HomeScreen: React.FC = () => {
  const {
    preferences,
    activeMood,
    moods,
    addMoodCheckIn,
    setActiveScreen,
    startRegulationActivity
  } = useSanctuary();

  const [selectedQuickMood, setSelectedQuickMood] = useState<MoodId>(activeMood);
  const [justLogged, setJustLogged] = useState(false);

  // Time greeting matching HomeScreen.kt
  const currentHour = new Date().getHours();
  const timeGreeting =
    currentHour < 12 ? 'Good morning' : currentHour < 17 ? 'Good afternoon' : 'Good evening';

  const cleanName = preferences.sanctuaryName?.trim();
  const hasValidName =
    cleanName && !['sanctuary', 'user', 'guest', 'sanctuary guest'].includes(cleanName.toLowerCase());

  const fullGreeting = hasValidName ? `${timeGreeting}, ${cleanName}.` : `${timeGreeting}.`;

  // 6 Quick Mood Chips for 2x3 grid matching HomeScreen.kt
  const quickMoodList: Array<{ id: MoodId; title: string; icon: React.ComponentType<{ size?: number; color?: string; style?: React.CSSProperties }> }> = [
    { id: 'happy', title: 'Joyful', icon: Sun },
    { id: 'calm', title: 'Calm', icon: Flower2 },
    { id: 'neutral', title: 'Steady', icon: Meh },
    { id: 'sad', title: 'Tender', icon: Frown },
    { id: 'anxious', title: 'Restless', icon: AlertCircle },
    { id: 'angry', title: 'Stormy', icon: Flame }
  ];

  const handleQuickCheckIn = () => {
    const config = MOOD_DEFINITIONS[selectedQuickMood];
    addMoodCheckIn(
      selectedQuickMood,
      config.defaultValence,
      5,
      config.contextualTags.slice(0, 2),
      'Quick sanctuary check-in'
    );
    setJustLogged(true);
    setTimeout(() => setJustLogged(false), 2400);
  };

  // Weekly Rhythm calculation (M T W T F S S) matching HomeScreen.kt
  const getWeeklyDays = () => {
    const dayLabels = ['M', 'T', 'W', 'T', 'F', 'S', 'S'];
    const now = new Date();
    const currentDayOfWeek = (now.getDay() + 6) % 7; // 0 = Mon, 6 = Sun

    return dayLabels.map((label, idx) => {
      const isToday = idx === currentDayOfWeek;
      const diffDays = currentDayOfWeek - idx;
      const targetDate = new Date();
      targetDate.setDate(now.getDate() - diffDays);
      const targetDateStr = targetDate.toDateString();

      const matchedMood = moods.find(
        (m) => new Date(m.timestamp).toDateString() === targetDateStr
      );

      return {
        label,
        isToday,
        mood: matchedMood ? MOOD_DEFINITIONS[matchedMood.mood] : null
      };
    });
  };

  const weeklyDays = getWeeklyDays();
  const currentMoodDef = MOOD_DEFINITIONS[selectedQuickMood];

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
      {/* 1. Header Greeting Section */}
      <div>
        <h1
          style={{
            fontSize: '1.75rem',
            fontWeight: 800,
            letterSpacing: '-0.025em',
            color: 'var(--solace-primary-text)',
            margin: 0
          }}
        >
          {fullGreeting}
        </h1>
        <p
          style={{
            fontSize: '0.92rem',
            color: 'var(--solace-secondary-text)',
            marginTop: '4px'
          }}
        >
          Welcome to your emotional sanctuary
        </p>
      </div>

      {/* 2. Breath of Dawn Hero Card (uses native img_sanctuary_dawn.jpg) */}
      <div
        className="solace-card"
        style={{
          height: '180px',
          position: 'relative',
          overflow: 'hidden',
          border: 'none',
          boxShadow: '0 4px 20px rgba(0,0,0,0.08)'
        }}
      >
        <img
          src="/img_sanctuary_dawn.jpg"
          alt="Breath of Dawn Sanctuary"
          style={{
            position: 'absolute',
            inset: 0,
            width: '100%',
            height: '100%',
            objectFit: 'cover'
          }}
        />

        {/* Gradient overlay for contrast */}
        <div
          style={{
            position: 'absolute',
            inset: 0,
            background:
              'linear-gradient(90deg, rgba(48, 49, 45, 0.85) 0%, rgba(48, 49, 45, 0.45) 55%, transparent 100%)'
          }}
        />

        <div
          style={{
            position: 'relative',
            zIndex: 2,
            height: '100%',
            padding: '20px',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'space-between',
            boxSizing: 'border-box'
          }}
        >
          {/* Tag */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            <span
              style={{
                width: '6px',
                height: '6px',
                borderRadius: '50%',
                backgroundColor: 'var(--solace-accent)'
              }}
            />
            <span
              style={{
                fontSize: '11px',
                fontWeight: 800,
                letterSpacing: '1px',
                color: 'var(--solace-accent-soft)',
                textTransform: 'uppercase'
              }}
            >
              Breath of Dawn
            </span>
          </div>

          <div>
            <p
              style={{
                fontSize: '1.05rem',
                fontWeight: 600,
                color: '#FFFFFF',
                lineHeight: 1.4,
                margin: 0
              }}
            >
              "Peace is not the absence of storm, but the gentle room inside."
            </p>
            <p
              style={{
                fontSize: '12px',
                color: 'rgba(255, 255, 255, 0.85)',
                marginTop: '6px',
                margin: '6px 0 0'
              }}
            >
              Take one slow breath before starting your day.
            </p>
          </div>
        </div>
      </div>

      {/* 3. Intention Check (Quick Mood Check-In Card) */}
      <div className="solace-card" style={{ padding: '20px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
          <Flower2 size={16} style={{ color: 'var(--active-mood-primary)' }} />
          <span style={{ fontSize: '12px', fontWeight: 600, color: 'var(--solace-secondary-text)' }}>
            Intention Check
          </span>
        </div>

        <h2 style={{ fontSize: '1.25rem', fontWeight: 800, color: 'var(--solace-primary-text)', margin: '0 0 2px' }}>
          Take a moment for yourself
        </h2>
        <p style={{ fontSize: '0.82rem', color: 'var(--solace-secondary-text)', margin: '0 0 16px' }}>
          Pause, breathe, and notice how you're feeling today.
        </p>

        {/* 2x3 Quick Mood Chips Grid */}
        <div className="quick-mood-grid">
          {quickMoodList.map((m) => {
            const isSelected = selectedQuickMood === m.id;
            const def = MOOD_DEFINITIONS[m.id];
            const Icon = m.icon;

            return (
              <button
                key={m.id}
                type="button"
                onClick={() => setSelectedQuickMood(m.id)}
                className={`quick-mood-chip ${isSelected ? 'selected' : ''}`}
                style={
                  {
                    '--chip-accent': def.primaryColor,
                    '--chip-bg': def.bgTint
                  } as React.CSSProperties
                }
              >
                <div
                  className="quick-mood-chip-icon"
                  style={{
                    backgroundColor: `${def.primaryColor}22`
                  }}
                >
                  <Icon size={18} color={def.primaryColor} />
                </div>
                <span className="quick-mood-chip-label">{m.title}</span>
              </button>
            );
          })}
        </div>

        {/* Selected Mood Footer */}
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            marginTop: '14px',
            marginBottom: '14px'
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            <span
              style={{
                width: '8px',
                height: '8px',
                borderRadius: '50%',
                backgroundColor: currentMoodDef.primaryColor
              }}
            />
            <span style={{ fontSize: '11px', color: 'var(--solace-primary-text)', fontWeight: 500 }}>
              Selected: {currentMoodDef.title} • Glowing gently
            </span>
          </div>

          <span
            onClick={() => setActiveScreen('checkin')}
            style={{
              fontSize: '11px',
              fontWeight: 700,
              color: 'var(--active-mood-primary)',
              cursor: 'pointer'
            }}
          >
            Full check-in →
          </span>
        </div>

        {/* Primary Log Button */}
        <button
          onClick={handleQuickCheckIn}
          className="btn-primary-54"
          style={{
            backgroundColor: currentMoodDef.primaryColor
          }}
        >
          <Check size={18} />
          <span>
            {justLogged
              ? 'Check-In Anchored ✓'
              : `Log Check-In (${currentMoodDef.title})`}
          </span>
        </button>
      </div>

      {/* 4. Gentle Rhythm (Weekly Mood Overview) */}
      <div className="solace-card" style={{ padding: '20px' }}>
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            marginBottom: '16px'
          }}
        >
          <div>
            <h3 style={{ fontSize: '1.05rem', fontWeight: 800, margin: 0 }}>
              Gentle Rhythm
            </h3>
            <p style={{ fontSize: '0.8rem', color: 'var(--solace-secondary-text)', margin: '2px 0 0' }}>
              Validating your natural flow this week
            </p>
          </div>

          <span
            onClick={() => setActiveScreen('insights')}
            style={{
              fontSize: '12px',
              fontWeight: 600,
              color: 'var(--active-mood-primary)',
              cursor: 'pointer'
            }}
          >
            History &gt;
          </span>
        </div>

        {/* 7 Days Row */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          {weeklyDays.map((d, index) => (
            <div
              key={index}
              style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                gap: '6px'
              }}
            >
              <span
                style={{
                  fontSize: '11px',
                  fontWeight: d.isToday ? 700 : 500,
                  color: d.isToday ? 'var(--active-mood-primary)' : 'var(--solace-secondary-text)'
                }}
              >
                {d.label}
              </span>

              <div
                style={{
                  width: '28px',
                  height: '28px',
                  borderRadius: '50%',
                  backgroundColor: d.mood ? d.mood.bgTint : 'var(--solace-input-bg)',
                  border: d.isToday
                    ? '1.5px solid var(--active-mood-primary)'
                    : d.mood
                    ? `1px solid ${d.mood.primaryColor}88`
                    : '1px solid var(--solace-border)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
              >
                {d.mood ? (
                  <span
                    style={{
                      width: '9px',
                      height: '9px',
                      borderRadius: '50%',
                      backgroundColor: d.mood.primaryColor
                    }}
                  />
                ) : (
                  <span style={{ fontSize: '11px', color: 'var(--solace-secondary-text)' }}>—</span>
                )}
              </div>

              <span
                style={{
                  fontSize: '10px',
                  fontWeight: d.isToday ? 700 : 400,
                  color: d.isToday ? 'var(--active-mood-primary)' : 'var(--solace-secondary-text)'
                }}
              >
                {d.isToday ? 'Today' : d.mood ? d.mood.title : '—'}
              </span>
            </div>
          ))}
        </div>
      </div>

      {/* 5. Today's Gentle Reflection Card */}
      <div
        className="solace-card interactive"
        onClick={() => setActiveScreen('journal')}
        style={{ padding: '20px' }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
          <span
            style={{
              width: '7px',
              height: '7px',
              borderRadius: '50%',
              backgroundColor: 'var(--active-mood-primary)'
            }}
          />
          <span
            style={{
              fontSize: '11px',
              fontWeight: 800,
              letterSpacing: '0.5px',
              color: 'var(--solace-secondary-text)',
              textTransform: 'uppercase'
            }}
          >
            Today's Gentle Reflection
          </span>
        </div>

        <p
          style={{
            fontSize: '1.05rem',
            fontWeight: 600,
            color: 'var(--solace-primary-text)',
            lineHeight: 1.4,
            margin: '0 0 14px'
          }}
        >
          "What made today a little gentler on your spirit?"
        </p>

        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            padding: '12px 14px',
            borderRadius: '16px',
            backgroundColor: 'var(--solace-input-bg)',
            border: '1px solid var(--solace-border)'
          }}
        >
          <span style={{ fontSize: '0.85rem', color: 'var(--solace-secondary-text)' }}>
            Write your unhurried thoughts...
          </span>
          <ArrowRight size={16} style={{ color: 'var(--active-mood-primary)' }} />
        </div>
      </div>

      {/* 6. solace AI Companion Spotlight Card */}
      <div
        className="solace-card"
        style={{
          padding: '20px',
          backgroundColor: 'var(--solace-card-highlight)'
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '10px' }}>
          <div
            style={{
              width: '32px',
              height: '32px',
              borderRadius: '50%',
              backgroundColor: 'var(--solace-accent)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: 'var(--solace-on-accent)'
            }}
          >
            <Sparkles size={18} />
          </div>
          <div>
            <span
              style={{
                fontSize: '11px',
                fontWeight: 800,
                letterSpacing: '0.5px',
                color: 'var(--solace-secondary-text)',
                textTransform: 'uppercase',
                display: 'block'
              }}
            >
              Your Sanctuary Companion
            </span>
            <h3 style={{ fontSize: '1.05rem', fontWeight: 800, color: 'var(--solace-primary-text)', margin: 0 }}>
              Need someone to talk to?
            </h3>
          </div>
        </div>

        <p
          style={{
            fontSize: '0.85rem',
            color: 'var(--solace-secondary-text)',
            lineHeight: 1.5,
            margin: '0 0 16px'
          }}
        >
          solace AI is here whenever you need a peaceful, non-judgmental ear to unpack thoughts or untangle feelings.
        </p>

        <button
          onClick={() => setActiveScreen('companion')}
          className="btn-accent-52"
        >
          <span>Start a conversation</span>
          <ArrowRight size={16} />
        </button>
      </div>

      {/* 7. Gentle Pattern Noticed Card */}
      <div className="solace-card" style={{ padding: '18px' }}>
        <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start' }}>
          <div
            style={{
              width: '34px',
              height: '34px',
              borderRadius: '50%',
              backgroundColor: 'var(--active-mood-container)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: 'var(--active-mood-primary)',
              flexShrink: 0
            }}
          >
            <Moon size={18} />
          </div>

          <div>
            <h4 style={{ fontSize: '0.98rem', fontWeight: 800, margin: 0, color: 'var(--solace-primary-text)' }}>
              Gentle Pattern Noticed
            </h4>
            <p
              style={{
                fontSize: '0.82rem',
                color: 'var(--solace-secondary-text)',
                margin: '4px 0 6px',
                lineHeight: 1.5
              }}
            >
              You tend to report feeling calmer on evenings when you take a moment to unwind and jot down a gentle reflection.
            </p>
            <span style={{ fontSize: '11px', color: 'var(--solace-muted-text)' }}>
              Synthesized from past 14 days
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};
