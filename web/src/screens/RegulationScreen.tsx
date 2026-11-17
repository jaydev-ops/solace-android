import React, { useState, useEffect } from 'react';
import { useSanctuary } from '../context/SanctuaryContext';
import {
  CURATED_REGULATION_ACTIVITIES,
  RegulationActivity
} from '../types/solace';
import { soundManager } from '../services/audioService';
import {
  Play,
  Pause,
  RotateCcw,
  CheckCircle,
  Star,
  Wind,
  Check,
  Sparkles,
  ArrowLeft
} from 'lucide-react';

export const RegulationScreen: React.FC = () => {
  const {
    activeRegulationActivity,
    startRegulationActivity,
    stopRegulationActivity,
    addRegulationRecord,
    setActiveScreen
  } = useSanctuary();

  const [selectedActivity, setSelectedActivity] = useState<RegulationActivity>(
    activeRegulationActivity || CURATED_REGULATION_ACTIVITIES[0]
  );

  // Exercise Player States
  const [isRunning, setIsRunning] = useState<boolean>(false);
  const [elapsedSeconds, setElapsedSeconds] = useState<number>(0);
  const [breathPhase, setBreathPhase] = useState<'inhale' | 'hold' | 'exhale' | 'rest'>('inhale');
  const [phaseSecondsLeft, setPhaseSecondsLeft] = useState<number>(4);
  const [completedCycles, setCompletedCycles] = useState<number>(0);

  // 5-4-3-2-1 Interactive checklist checked state
  const [groundingChecks, setGroundingChecks] = useState<Record<number, boolean>>({});

  // Completion dialog
  const [showCompletionModal, setShowCompletionModal] = useState<boolean>(false);
  const [rating, setRating] = useState<number>(5);
  const [completionNote, setCompletionNote] = useState<string>('');

  // Sync selected activity when prop changes
  useEffect(() => {
    if (activeRegulationActivity) {
      setSelectedActivity(activeRegulationActivity);
      resetPlayer();
    }
  }, [activeRegulationActivity]);

  const resetPlayer = () => {
    setIsRunning(false);
    setElapsedSeconds(0);
    setBreathPhase('inhale');
    setPhaseSecondsLeft(4);
    setCompletedCycles(0);
    setGroundingChecks({});
  };

  // Timer loop for breathing exercises
  useEffect(() => {
    let interval: ReturnType<typeof setInterval> | null = null;

    if (isRunning) {
      interval = setInterval(() => {
        setElapsedSeconds((prev) => prev + 1);

        // Box Breathing 4-4-4-4 logic
        if (selectedActivity.id === 'box_breathing') {
          setPhaseSecondsLeft((current) => {
            if (current > 1) {
              return current - 1;
            } else {
              // Transition to next phase
              let nextPhase: 'inhale' | 'hold' | 'exhale' | 'rest' = 'inhale';
              if (breathPhase === 'inhale') nextPhase = 'hold';
              else if (breathPhase === 'hold') nextPhase = 'exhale';
              else if (breathPhase === 'exhale') nextPhase = 'rest';
              else if (breathPhase === 'rest') {
                nextPhase = 'inhale';
                setCompletedCycles((c) => c + 1);
              }

              setBreathPhase(nextPhase);
              soundManager.playBreathCue(nextPhase);
              return 4;
            }
          });
        }

        // Physiological Sigh 4-2-8 logic
        if (selectedActivity.id === 'physiological_sigh') {
          setPhaseSecondsLeft((current) => {
            if (current > 1) {
              return current - 1;
            } else {
              if (breathPhase === 'inhale') {
                setBreathPhase('hold'); // Top-off sip 2s
                soundManager.playBreathCue('hold');
                return 2;
              } else if (breathPhase === 'hold') {
                setBreathPhase('exhale'); // Slow sigh 8s
                soundManager.playBreathCue('exhale');
                return 8;
              } else {
                setBreathPhase('inhale');
                setCompletedCycles((c) => c + 1);
                soundManager.playBreathCue('inhale');
                return 4;
              }
            }
          });
        }
      }, 1000);
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isRunning, breathPhase, selectedActivity.id]);

  const togglePlay = () => {
    if (!isRunning) {
      // Starting
      soundManager.playBreathCue(breathPhase);
    }
    setIsRunning(!isRunning);
  };

  const handleFinishSession = () => {
    setIsRunning(false);
    setShowCompletionModal(true);
    soundManager.playSingingBowlChime(432, 2.5);
  };

  const submitCompletion = () => {
    addRegulationRecord(selectedActivity, rating, completionNote);
    setShowCompletionModal(false);
    setCompletionNote('');
    resetPlayer();
    stopRegulationActivity();
  };

  const formatTime = (secs: number) => {
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${m}:${s < 10 ? '0' : ''}${s}`;
  };

  const getBreathPhaseInstruction = () => {
    if (selectedActivity.id === 'box_breathing') {
      switch (breathPhase) {
        case 'inhale':
          return 'Breathe in slowly through your nose...';
        case 'hold':
          return 'Hold gently with softened shoulders...';
        case 'exhale':
          return 'Smooth, steady exhale through your lips...';
        case 'rest':
          return 'Rest empty before your next breath...';
      }
    }
    if (selectedActivity.id === 'physiological_sigh') {
      switch (breathPhase) {
        case 'inhale':
          return 'Deep inhale through your nose (4s)...';
        case 'hold':
          return 'Second quick top-off sip of air (2s)...';
        case 'exhale':
          return 'Long, slow sigh out through your lips (8s)...';
        case 'rest':
          return 'Rest gently...';
      }
    }
    return 'Breathe naturally at your own pace...';
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
      {/* Header */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <h1 style={{ fontSize: '1.85rem', fontWeight: 800, margin: 0 }}>
            Somatic Nervous System Regulation
          </h1>
          <p style={{ fontSize: '0.92rem', color: 'var(--solace-secondary-text)', marginTop: '4px' }}>
            Curated polyvagal down-regulation, pacing, and grounding exercises.
          </p>
        </div>
      </div>

      {/* Activity Navigation Pills */}
      <div
        style={{
          display: 'flex',
          gap: '8px',
          overflowX: 'auto',
          paddingBottom: '4px',
          scrollbarWidth: 'none'
        }}
      >
        {CURATED_REGULATION_ACTIVITIES.map((act) => {
          const isSelected = selectedActivity.id === act.id;
          return (
            <button
              key={act.id}
              onClick={() => {
                setSelectedActivity(act);
                resetPlayer();
              }}
              className={`tag-chip ${isSelected ? 'selected' : ''}`}
              style={{ padding: '8px 16px', fontSize: '0.88rem', whiteSpace: 'nowrap' }}
            >
              <span>{act.title}</span>
            </button>
          );
        })}
      </div>

      {/* Interactive Exercise Space */}
      <div
        className="solace-card"
        style={{
          padding: '28px 24px',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          textAlign: 'center'
        }}
      >
        <span
          style={{
            fontSize: '0.78rem',
            fontWeight: 700,
            textTransform: 'uppercase',
            color: 'var(--active-mood-primary)',
            backgroundColor: 'var(--active-mood-bg)',
            padding: '4px 12px',
            borderRadius: 'var(--radius-pill)',
            border: '1px solid var(--active-mood-border)'
          }}
        >
          {selectedActivity.category} • Target: {selectedActivity.durationSeconds}s
        </span>

        <h2 style={{ fontSize: '1.45rem', fontWeight: 800, marginTop: '10px', marginBottom: '4px' }}>
          {selectedActivity.title}
        </h2>
        <p style={{ fontSize: '0.9rem', color: 'var(--solace-secondary-text)', maxWidth: '520px', lineHeight: 1.5, marginBottom: '24px' }}>
          {selectedActivity.description}
        </p>

        {/* Breathing Visualizer for Box Breathing & Physiological Sigh */}
        {(selectedActivity.id === 'box_breathing' || selectedActivity.id === 'physiological_sigh') && (
          <div className={`breathing-aura-container breath-phase-${breathPhase}`}>
            <div className="breathing-ring-outer" />
            <div className="breathing-orb-inner">
              <span style={{ fontSize: '1.25rem', fontWeight: 800, textTransform: 'capitalize' }}>
                {breathPhase}
              </span>
              <span style={{ fontSize: '2.5rem', fontWeight: 900, lineHeight: 1.1 }}>
                {phaseSecondsLeft}
              </span>
              <span style={{ fontSize: '0.75rem', opacity: 0.85 }}>seconds</span>
            </div>
          </div>
        )}

        {/* Dynamic Breathing Guidance Cue */}
        {(selectedActivity.id === 'box_breathing' || selectedActivity.id === 'physiological_sigh') && (
          <div
            style={{
              fontSize: '1.05rem',
              fontWeight: 600,
              color: 'var(--solace-primary-text)',
              minHeight: '32px',
              marginBottom: '16px'
            }}
          >
            {isRunning ? getBreathPhaseInstruction() : 'Press start to begin your breathing cadence'}
          </div>
        )}

        {/* Interactive 5-4-3-2-1 Grounding Checklist */}
        {selectedActivity.id === 'grounding_54321' && (
          <div
            style={{
              width: '100%',
              maxWidth: '560px',
              display: 'flex',
              flexDirection: 'column',
              gap: '10px',
              margin: '20px 0',
              textAlign: 'left'
            }}
          >
            {selectedActivity.instructions.map((step, idx) => {
              const isChecked = Boolean(groundingChecks[idx]);
              return (
                <div
                  key={idx}
                  onClick={() => {
                    setGroundingChecks({ ...groundingChecks, [idx]: !isChecked });
                    soundManager.playSoftTick();
                  }}
                  className={`solace-card interactive`}
                  style={{
                    padding: '12px 16px',
                    display: 'flex',
                    alignItems: 'center',
                    gap: '12px',
                    backgroundColor: isChecked ? 'var(--active-mood-bg)' : 'var(--solace-card-bg)',
                    borderColor: isChecked ? 'var(--active-mood-border)' : 'var(--solace-border)'
                  }}
                >
                  <div
                    style={{
                      width: '24px',
                      height: '24px',
                      borderRadius: '50%',
                      border: isChecked ? '2px solid var(--active-mood-primary)' : '2px solid var(--solace-border)',
                      backgroundColor: isChecked ? 'var(--active-mood-primary)' : 'transparent',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      color: '#FFF',
                      flexShrink: 0
                    }}
                  >
                    {isChecked && <Check size={14} />}
                  </div>
                  <span
                    style={{
                      fontSize: '0.92rem',
                      color: isChecked ? 'var(--solace-primary-text)' : 'var(--solace-secondary-text)',
                      textDecoration: isChecked ? 'line-through' : 'none',
                      fontWeight: isChecked ? 600 : 500
                    }}
                  >
                    {step}
                  </span>
                </div>
              );
            })}
          </div>
        )}

        {/* Step-by-step instructions list for Cognitive, Somatic & Gratitude */}
        {selectedActivity.id !== 'grounding_54321' &&
          selectedActivity.id !== 'box_breathing' &&
          selectedActivity.id !== 'physiological_sigh' && (
            <div
              style={{
                width: '100%',
                maxWidth: '560px',
                display: 'flex',
                flexDirection: 'column',
                gap: '12px',
                margin: '20px 0',
                textAlign: 'left'
              }}
            >
              {selectedActivity.instructions.map((inst, i) => (
                <div
                  key={i}
                  style={{
                    padding: '14px 18px',
                    borderRadius: 'var(--radius-lg)',
                    backgroundColor: 'var(--solace-surface-raised)',
                    border: '1px solid var(--solace-border)',
                    display: 'flex',
                    alignItems: 'flex-start',
                    gap: '12px'
                  }}
                >
                  <span
                    style={{
                      width: '24px',
                      height: '24px',
                      borderRadius: '50%',
                      backgroundColor: 'var(--active-mood-bg)',
                      color: 'var(--active-mood-primary)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      fontWeight: 700,
                      fontSize: '0.8rem',
                      flexShrink: 0
                    }}
                  >
                    {i + 1}
                  </span>
                  <span style={{ fontSize: '0.92rem', color: 'var(--solace-primary-text)', lineHeight: 1.5 }}>
                    {inst}
                  </span>
                </div>
              ))}
            </div>
          )}

        {/* Controls & Metrics */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px', margin: '16px 0 20px' }}>
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <span style={{ fontSize: '1.25rem', fontWeight: 800 }}>{formatTime(elapsedSeconds)}</span>
            <span style={{ fontSize: '0.72rem', color: 'var(--solace-secondary-text)' }}>Elapsed</span>
          </div>

          {(selectedActivity.id === 'box_breathing' || selectedActivity.id === 'physiological_sigh') && (
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <span style={{ fontSize: '1.25rem', fontWeight: 800 }}>{completedCycles}</span>
              <span style={{ fontSize: '0.72rem', color: 'var(--solace-secondary-text)' }}>Cycles</span>
            </div>
          )}
        </div>

        {/* Action Buttons */}
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', justifyContent: 'center' }}>
          <button
            onClick={togglePlay}
            className="btn-primary"
            style={{ minWidth: '150px' }}
          >
            {isRunning ? (
              <>
                <Pause size={18} />
                <span>Pause</span>
              </>
            ) : (
              <>
                <Play size={18} />
                <span>{elapsedSeconds > 0 ? 'Resume' : 'Begin Practice'}</span>
              </>
            )}
          </button>

          <button onClick={resetPlayer} className="btn-secondary">
            <RotateCcw size={16} />
            <span>Reset</span>
          </button>

          {elapsedSeconds >= 10 && (
            <button onClick={handleFinishSession} className="btn-secondary" style={{ borderColor: 'var(--active-mood-primary)' }}>
              <CheckCircle size={16} style={{ color: 'var(--active-mood-primary)' }} />
              <span>Complete Session</span>
            </button>
          )}
        </div>
      </div>

      {/* Session Completion Modal */}
      {showCompletionModal && (
        <div
          style={{
            position: 'fixed',
            inset: 0,
            backgroundColor: 'rgba(23, 23, 20, 0.6)',
            backdropFilter: 'blur(8px)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 1000,
            padding: '20px'
          }}
        >
          <div className="solace-card" style={{ maxWidth: '480px', width: '100%', padding: '24px' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 6px' }}>
              Practice Completed 🌿
            </h3>
            <p style={{ fontSize: '0.88rem', color: 'var(--solace-secondary-text)', margin: '0 0 20px', lineHeight: 1.5 }}>
              How does your nervous system feel after {formatTime(elapsedSeconds)} of {selectedActivity.title}?
            </p>

            <div style={{ display: 'flex', justifyContent: 'center', gap: '8px', margin: '14px 0 20px' }}>
              {[1, 2, 3, 4, 5].map((star) => (
                <button
                  key={star}
                  type="button"
                  onClick={() => setRating(star)}
                  style={{
                    background: 'transparent',
                    border: 'none',
                    cursor: 'pointer',
                    color: star <= rating ? 'var(--solace-accent-deep)' : 'var(--solace-border)'
                  }}
                >
                  <Star size={32} fill={star <= rating ? 'currentColor' : 'none'} />
                </button>
              ))}
            </div>

            <textarea
              value={completionNote}
              onChange={(e) => setCompletionNote(e.target.value)}
              placeholder="Optional: What did you notice in your body or thoughts?"
              className="input-field"
              rows={3}
              style={{ marginBottom: '18px' }}
            />

            <div style={{ display: 'flex', gap: '10px', justifyContent: 'flex-end' }}>
              <button
                type="button"
                onClick={() => setShowCompletionModal(false)}
                className="btn-ghost"
              >
                Skip
              </button>
              <button
                type="button"
                onClick={submitCompletion}
                className="btn-primary"
              >
                Save Record
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
