import React, { useState } from 'react';
import { useSanctuary } from '../context/SanctuaryContext';
import {
  Sparkles,
  ArrowRight,
  ShieldCheck,
  Lock,
  Clock,
  Heart,
  User,
  X
} from 'lucide-react';

export const LoginScreen: React.FC = () => {
  const { login, knownUsers, removeKnownUser } = useSanctuary();
  const [nameInput, setNameInput] = useState('');
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const clean = nameInput.trim();
    if (!clean) {
      setError('Please enter your name to enter the sanctuary.');
      return;
    }
    setError(null);
    login(clean);
  };

  const handleSelectKnown = (name: string) => {
    setNameInput(name);
    login(name);
  };

  const initial = nameInput.trim() ? nameInput.trim().charAt(0).toUpperCase() : null;

  return (
    <div
      style={{
        minHeight: '100dvh',
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '24px 20px',
        boxSizing: 'border-box',
        background: 'radial-gradient(ellipse at top, var(--solace-bg-gradient-top) 0%, var(--solace-bg-primary) 70%)'
      }}
    >
      <div
        style={{
          width: '100%',
          maxWidth: '460px',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          gap: '28px'
        }}
      >
        {/* Brand Header */}
        <div style={{ textAlign: 'center', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <div
            style={{
              width: '68px',
              height: '68px',
              borderRadius: '24px',
              background: 'linear-gradient(135deg, var(--solace-amber-primary) 0%, #B87302 100%)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              boxShadow: '0 8px 24px rgba(217, 142, 4, 0.25)',
              marginBottom: '16px'
            }}
          >
            <Sparkles size={34} style={{ color: '#FFFFFF' }} />
          </div>

          <div className="solace-wordmark" style={{ justifyContent: 'center', transform: 'scale(1.2)' }}>
            <span className="solace-wordmark-text" style={{ fontSize: '2rem' }}>solace</span>
            <div className="solace-plus-badge" style={{ fontSize: '1rem', padding: '1px 6px' }}>+</div>
          </div>

          <p
            style={{
              margin: '12px 0 0',
              fontSize: '0.98rem',
              color: 'var(--solace-secondary-text)',
              fontStyle: 'italic',
              letterSpacing: '0.01em'
            }}
          >
            "A quiet room inside, whenever you need it."
          </p>
        </div>

        {/* Login Card */}
        <div
          className="solace-card"
          style={{
            width: '100%',
            padding: '32px 28px',
            borderRadius: '28px',
            boxShadow: 'var(--shadow-lg)',
            display: 'flex',
            flexDirection: 'column',
            gap: '20px',
            boxSizing: 'border-box'
          }}
        >
          <div>
            <h2 style={{ fontSize: '1.35rem', fontWeight: 700, margin: '0 0 6px', color: 'var(--solace-primary-text)' }}>
              Enter Your Sanctuary
            </h2>
            <p style={{ fontSize: '0.88rem', color: 'var(--solace-secondary-text)', margin: 0, lineHeight: 1.5 }}>
              What should Solace call you during your conversations?
            </p>
          </div>

          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '18px' }}>
            {/* Input with live avatar preview */}
            <div>
              <label
                htmlFor="user-name-input"
                style={{
                  display: 'block',
                  fontSize: '0.82rem',
                  fontWeight: 600,
                  marginBottom: '8px',
                  color: 'var(--solace-primary-text)'
                }}
              >
                Your Name or Preferred Nickname
              </label>

              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <div
                  style={{
                    width: '46px',
                    height: '46px',
                    borderRadius: '50%',
                    backgroundColor: initial ? 'var(--solace-amber-primary)' : 'var(--solace-bg-secondary)',
                    color: initial ? '#FFFFFF' : 'var(--solace-secondary-text)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontWeight: 700,
                    fontSize: '1.1rem',
                    flexShrink: 0,
                    border: '1px solid var(--solace-border-subtle)',
                    transition: 'all 0.2s ease'
                  }}
                >
                  {initial || <User size={20} />}
                </div>

                <input
                  id="user-name-input"
                  type="text"
                  autoFocus
                  value={nameInput}
                  onChange={(e) => {
                    setNameInput(e.target.value);
                    if (error) setError(null);
                  }}
                  placeholder="e.g. Maya, Alex, Jayesh..."
                  className="input-field"
                  style={{
                    flex: 1,
                    fontSize: '1rem',
                    padding: '12px 16px',
                    borderRadius: '16px'
                  }}
                  maxLength={32}
                />
              </div>

              {error && (
                <p style={{ color: 'var(--solace-emergency)', fontSize: '0.82rem', margin: '6px 0 0', fontWeight: 500 }}>
                  {error}
                </p>
              )}
            </div>

            {/* Quick Pick: Returning Users */}
            {knownUsers.length > 0 && (
              <div>
                <span
                  style={{
                    display: 'block',
                    fontSize: '0.78rem',
                    fontWeight: 600,
                    color: 'var(--solace-secondary-text)',
                    marginBottom: '8px',
                    textTransform: 'uppercase',
                    letterSpacing: '0.04em'
                  }}
                >
                  Or continue as
                </span>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
                  {knownUsers.map((name) => (
                    <div
                      key={name}
                      style={{
                        display: 'inline-flex',
                        alignItems: 'center',
                        gap: '6px',
                        background: 'var(--solace-bg-secondary)',
                        border: '1px solid var(--solace-border-subtle)',
                        borderRadius: '20px',
                        padding: '4px 10px',
                        fontSize: '0.86rem',
                        fontWeight: 600
                      }}
                    >
                      <button
                        type="button"
                        onClick={() => handleSelectKnown(name)}
                        style={{
                          background: 'none',
                          border: 'none',
                          padding: 0,
                          color: 'var(--solace-primary-text)',
                          cursor: 'pointer',
                          display: 'flex',
                          alignItems: 'center',
                          gap: '4px'
                        }}
                      >
                        <span style={{ color: 'var(--solace-amber-primary)' }}>✨</span>
                        <span>{name}</span>
                      </button>

                      <button
                        type="button"
                        onClick={(e) => {
                          e.stopPropagation();
                          removeKnownUser(name);
                        }}
                        style={{
                          background: 'none',
                          border: 'none',
                          padding: '2px',
                          color: 'var(--solace-secondary-text)',
                          cursor: 'pointer',
                          display: 'flex',
                          alignItems: 'center'
                        }}
                        title={`Remove ${name}`}
                      >
                        <X size={12} />
                      </button>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Submit Button */}
            <button
              type="submit"
              className="btn-primary"
              disabled={!nameInput.trim()}
              style={{
                width: '100%',
                padding: '14px',
                fontSize: '1rem',
                fontWeight: 700,
                borderRadius: '16px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '8px',
                marginTop: '4px',
                cursor: nameInput.trim() ? 'pointer' : 'not-allowed',
                opacity: nameInput.trim() ? 1 : 0.6
              }}
            >
              <span>Enter Sanctuary</span>
              <ArrowRight size={18} />
            </button>
          </form>

          {/* Privacy Note */}
          <div
            style={{
              paddingTop: '16px',
              borderTop: '1px solid var(--solace-border-subtle)',
              display: 'flex',
              alignItems: 'center',
              gap: '10px'
            }}
          >
            <Lock size={16} style={{ color: 'var(--solace-secondary-text)', flexShrink: 0 }} />
            <p style={{ margin: 0, fontSize: '0.78rem', color: 'var(--solace-secondary-text)', lineHeight: 1.4 }}>
              <strong>100% Private &amp; Sovereign</strong>. Your name, mood records, and chat history remain safely stored only in this browser.
            </p>
          </div>
        </div>

        {/* Feature Highlights Footer */}
        <div
          style={{
            display: 'flex',
            flexWrap: 'wrap',
            justifyContent: 'center',
            gap: '16px',
            fontSize: '0.8rem',
            color: 'var(--solace-secondary-text)'
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            <Clock size={14} style={{ color: 'var(--solace-amber-primary)' }} />
            <span>Persistent Chat History</span>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            <ShieldCheck size={14} style={{ color: 'var(--solace-success)' }} />
            <span>Rule-Based Deterministic AI</span>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            <Heart size={14} style={{ color: '#E06A6A' }} />
            <span>Independent Crisis Safety</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginScreen;
