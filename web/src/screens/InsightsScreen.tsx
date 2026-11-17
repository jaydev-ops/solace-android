import React, { useState } from 'react';
import { useSanctuary } from '../context/SanctuaryContext';
import { MOOD_DEFINITIONS, MoodId, CURATED_REGULATION_ACTIVITIES } from '../types/solace';
import {
  BarChart2,
  Calendar,
  Sparkles,
  Wind,
  Smile,
  Heart,
  TrendingUp,
  Activity,
  ArrowRight
} from 'lucide-react';

export const InsightsScreen: React.FC = () => {
  const { moods, regulationRecords, startRegulationActivity, setActiveScreen } = useSanctuary();
  const [timeframe, setTimeframe] = useState<'7d' | '30d' | 'all'>('7d');

  const now = Date.now();
  const cutoff =
    timeframe === '7d'
      ? now - 7 * 86400000
      : timeframe === '30d'
      ? now - 30 * 86400000
      : 0;

  const filteredMoods = moods.filter((m) => m.timestamp >= cutoff);
  const totalEntries = filteredMoods.length;

  // Calculate mood distribution
  const counts: Record<MoodId, number> = {
    happy: 0,
    calm: 0,
    neutral: 0,
    sad: 0,
    anxious: 0,
    angry: 0,
    overwhelmed: 0
  };

  filteredMoods.forEach((m) => {
    if (counts[m.mood] !== undefined) {
      counts[m.mood] += 1;
    }
  });

  // Calculate average valence
  const avgValence =
    totalEntries > 0
      ? (filteredMoods.reduce((acc, m) => acc + (m.valence || 5), 0) / totalEntries).toFixed(1)
      : '5.0';

  // Total regulation minutes
  const totalRegSeconds = regulationRecords.reduce((acc, r) => acc + r.durationSeconds, 0);
  const totalRegMinutes = Math.round(totalRegSeconds / 60);

  // Emotional Weather summary
  const getEmotionalWeather = () => {
    if (totalEntries === 0) return { title: 'Quiet Clear Skies', desc: 'A blank slate waiting for your first check-ins.' };
    const maxMood = (Object.keys(counts) as MoodId[]).reduce((a, b) =>
      counts[a] > counts[b] ? a : b
    );
    switch (maxMood) {
      case 'happy':
        return { title: 'Radiant Sunlight ☀️', desc: 'Your space has been filled with warmth, gratitude, and upward momentum.' };
      case 'calm':
        return { title: 'Gentle Harbor 🌿', desc: 'Centered, peaceful, and grounded baseline with balanced nervous system rhythm.' };
      case 'neutral':
        return { title: 'Steady Equinox ⚖️', desc: 'Grounded and quiet. Neither turbulent nor high arousal—a solid foundation.' };
      case 'sad':
        return { title: 'Tender Mist 💧', desc: 'Holding space for grief or soft reflection. Give yourself extra patience.' };
      case 'anxious':
        return { title: 'Restless Ocean Waves 🌊', desc: 'High mental arousal. Slow exhales and somatic unclenching will soothe the tides.' };
      case 'angry':
        return { title: 'Passionate Thunder 🔥', desc: 'Fiery boundary signaling. Healthy release and physical grounding recommended.' };
      case 'overwhelmed':
        return { title: 'Heavy Cloud Cover ☁️', desc: 'Scattered cognitive load. Time to drop half the stones and rest.' };
    }
  };

  const weather = getEmotionalWeather();

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
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '12px' }}>
        <div>
          <h1 style={{ fontSize: '1.85rem', fontWeight: 800, margin: 0 }}>Emotional Insights</h1>
          <p style={{ fontSize: '0.92rem', color: 'var(--solace-secondary-text)', marginTop: '4px' }}>
            Validating your natural flow without judgment, streaks, or scores.
          </p>
        </div>

        {/* Timeframe Selector */}
        <div className="segmented-control" style={{ width: '280px' }}>
          <button
            onClick={() => setTimeframe('7d')}
            className={`segmented-option ${timeframe === '7d' ? 'active' : ''}`}
          >
            Last 7 Days
          </button>
          <button
            onClick={() => setTimeframe('30d')}
            className={`segmented-option ${timeframe === '30d' ? 'active' : ''}`}
          >
            Last 30 Days
          </button>
          <button
            onClick={() => setTimeframe('all')}
            className={`segmented-option ${timeframe === 'all' ? 'active' : ''}`}
          >
            All Time
          </button>
        </div>
      </div>

      {/* Weather Summary Card */}
      <div
        className="solace-card"
        style={{
          background: 'linear-gradient(135deg, var(--active-mood-bg) 0%, var(--solace-card-bg) 100%)',
          borderColor: 'var(--active-mood-border)',
          padding: '24px'
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
          <Sparkles size={18} style={{ color: 'var(--active-mood-primary)' }} />
          <span style={{ fontSize: '0.78rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.05em', color: 'var(--active-mood-primary)' }}>
            Nervous System Atmospheric Weather
          </span>
        </div>
        <h2 style={{ fontSize: '1.4rem', fontWeight: 800, margin: 0 }}>{weather.title}</h2>
        <p style={{ fontSize: '0.92rem', color: 'var(--solace-secondary-text)', margin: '4px 0 0', lineHeight: 1.5 }}>
          {weather.desc}
        </p>
      </div>

      {/* Metrics Row */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '14px' }}>
        <div className="solace-card" style={{ padding: '18px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--solace-secondary-text)', marginBottom: '6px' }}>
            <Activity size={16} />
            <span style={{ fontSize: '0.8rem', fontWeight: 600 }}>Total Check-ins</span>
          </div>
          <span style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--solace-primary-text)' }}>
            {totalEntries}
          </span>
          <p style={{ fontSize: '0.75rem', color: 'var(--solace-secondary-text)', margin: '4px 0 0' }}>
            Moments of honest presence
          </p>
        </div>

        <div className="solace-card" style={{ padding: '18px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--solace-secondary-text)', marginBottom: '6px' }}>
            <TrendingUp size={16} />
            <span style={{ fontSize: '0.8rem', fontWeight: 600 }}>Average Valence</span>
          </div>
          <span style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--active-mood-primary)' }}>
            {avgValence} <span style={{ fontSize: '1rem', fontWeight: 600 }}>/ 10</span>
          </span>
          <p style={{ fontSize: '0.75rem', color: 'var(--solace-secondary-text)', margin: '4px 0 0' }}>
            Comfortable baseline equilibrium
          </p>
        </div>

        <div className="solace-card" style={{ padding: '18px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--solace-secondary-text)', marginBottom: '6px' }}>
            <Wind size={16} />
            <span style={{ fontSize: '0.8rem', fontWeight: 600 }}>Somatic Regulation</span>
          </div>
          <span style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--solace-success)' }}>
            {totalRegMinutes} <span style={{ fontSize: '1rem', fontWeight: 600 }}>mins</span>
          </span>
          <p style={{ fontSize: '0.75rem', color: 'var(--solace-secondary-text)', margin: '4px 0 0' }}>
            {regulationRecords.length} completed practices
          </p>
        </div>
      </div>

      {/* Mood Distribution Breakdown */}
      <div className="solace-card" style={{ padding: '24px' }}>
        <h3 style={{ fontSize: '1.1rem', fontWeight: 700, margin: '0 0 16px' }}>
          Emotional Space Distribution
        </h3>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
          {(Object.keys(MOOD_DEFINITIONS) as MoodId[]).map((key) => {
            const def = MOOD_DEFINITIONS[key];
            const count = counts[key];
            const pct = totalEntries > 0 ? Math.round((count / totalEntries) * 100) : 0;

            return (
              <div key={key} style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <span>{def.emoji}</span>
                    <strong style={{ fontWeight: 600 }}>{def.title}</strong>
                  </div>
                  <span style={{ color: 'var(--solace-secondary-text)' }}>
                    {count} {count === 1 ? 'time' : 'times'} ({pct}%)
                  </span>
                </div>

                <div
                  style={{
                    width: '100%',
                    height: '8px',
                    backgroundColor: 'var(--solace-surface-raised)',
                    borderRadius: 'var(--radius-pill)',
                    overflow: 'hidden'
                  }}
                >
                  <div
                    style={{
                      height: '100%',
                      width: `${pct}%`,
                      backgroundColor: def.primaryColor,
                      borderRadius: 'var(--radius-pill)',
                      transition: 'width 0.6s cubic-bezier(0.16, 1, 0.3, 1)'
                    }}
                  />
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Recommended Somatic Regulation */}
      <div className="solace-card" style={{ padding: '24px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
          <Wind size={20} style={{ color: 'var(--active-mood-primary)' }} />
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700, margin: 0 }}>
            Recommended Practices for Your Nervous System
          </h3>
        </div>
        <p style={{ fontSize: '0.88rem', color: 'var(--solace-secondary-text)', lineHeight: 1.5, marginBottom: '16px' }}>
          Based on your recent rhythm, these exercises offer gentle autonomic down-regulation:
        </p>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '12px' }}>
          {CURATED_REGULATION_ACTIVITIES.slice(0, 2).map((act) => (
            <div
              key={act.id}
              onClick={() => startRegulationActivity(act)}
              className="solace-card interactive"
              style={{
                padding: '16px',
                backgroundColor: 'var(--solace-surface-raised)',
                border: '1px solid var(--solace-border)'
              }}
            >
              <h4 style={{ fontSize: '0.98rem', fontWeight: 700, margin: 0 }}>{act.title}</h4>
              <p style={{ fontSize: '0.8rem', color: 'var(--solace-secondary-text)', margin: '4px 0 10px', lineHeight: 1.4 }}>
                {act.description}
              </p>
              <div style={{ display: 'flex', alignItems: 'center', gap: '4px', color: 'var(--active-mood-primary)', fontSize: '0.82rem', fontWeight: 600 }}>
                <span>Start {act.durationSeconds}s</span>
                <ArrowRight size={14} />
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
