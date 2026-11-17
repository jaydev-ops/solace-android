import React, { useEffect, useRef } from 'react';
import { useSanctuary } from '../context/SanctuaryContext';
import { ShieldCheck, EyeOff, X, Lock } from 'lucide-react';

export const MemoryPermissionModal: React.FC = () => {
  const { isMemoryModalOpen, setIsMemoryModalOpen, preferences, updatePreferences } = useSanctuary();
  const dialogRef = useRef<HTMLDialogElement>(null);

  useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) return;

    if (isMemoryModalOpen) {
      if (!dialog.open) {
        dialog.showModal();
      }
    } else {
      if (dialog.open) {
        dialog.close();
      }
    }
  }, [isMemoryModalOpen]);

  const handleChoose = (consent: 'persisted' | 'ephemeral') => {
    updatePreferences({ memoryConsent: consent });
    setIsMemoryModalOpen(false);
  };

  return (
    <dialog
      ref={dialogRef}
      onClose={() => setIsMemoryModalOpen(false)}
      aria-labelledby="memory-dialog-title"
      style={{ padding: 0 }}
    >
      <div style={{ padding: '24px' }}>
        <div
          style={{
            display: 'flex',
            alignItems: 'flex-start',
            justifyContent: 'space-between',
            marginBottom: '16px'
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <div
              style={{
                width: '42px',
                height: '42px',
                borderRadius: 'var(--radius-pill)',
                backgroundColor: 'var(--active-mood-bg)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                color: 'var(--active-mood-primary)'
              }}
            >
              <Lock size={22} />
            </div>
            <div>
              <h2 id="memory-dialog-title" style={{ fontSize: '1.25rem', fontWeight: 700, margin: 0 }}>
                Sanctuary Memory & Privacy
              </h2>
              <p style={{ fontSize: '0.85rem', color: 'var(--solace-secondary-text)', margin: '2px 0 0' }}>
                You control how your reflections are kept.
              </p>
            </div>
          </div>
          <button
            onClick={() => setIsMemoryModalOpen(false)}
            className="btn-ghost"
            style={{ padding: '6px', borderRadius: 'var(--radius-pill)' }}
            aria-label="Close memory settings dialog"
          >
            <X size={20} />
          </button>
        </div>

        <p style={{ fontSize: '0.9rem', color: 'var(--solace-primary-text)', lineHeight: 1.6, marginBottom: '20px' }}>
          Solace+ is built on absolute emotional sovereignty. No thoughts, reflections, or check-ins
          are ever sent to our servers or third-party advertisers. Please choose how you want this device to handle your space:
        </p>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {/* Allow Memory Saving Option */}
          <div
            onClick={() => handleChoose('persisted')}
            className={`solace-card interactive`}
            style={{
              padding: '16px',
              border:
                preferences.memoryConsent === 'persisted'
                  ? '2px solid var(--active-mood-primary)'
                  : '1px solid var(--solace-border)',
              backgroundColor:
                preferences.memoryConsent === 'persisted'
                  ? 'var(--active-mood-bg)'
                  : 'var(--solace-card-bg)'
            }}
          >
            <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start' }}>
              <div
                style={{
                  width: '36px',
                  height: '36px',
                  borderRadius: 'var(--radius-pill)',
                  backgroundColor: 'var(--solace-surface-raised)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: 'var(--solace-success)',
                  flexShrink: 0
                }}
              >
                <ShieldCheck size={20} />
              </div>
              <div>
                <h3 style={{ fontSize: '1rem', fontWeight: 700, margin: 0 }}>
                  Allow Local Memory Saving (Recommended)
                </h3>
                <p style={{ fontSize: '0.82rem', color: 'var(--solace-secondary-text)', margin: '4px 0 0', lineHeight: 1.5 }}>
                  Persists your mood check-ins, journal reflections, and preferences strictly inside your browser's private local storage. Your history stays with you across sessions.
                </p>
              </div>
            </div>
          </div>

          {/* Ephemeral Session Option */}
          <div
            onClick={() => handleChoose('ephemeral')}
            className={`solace-card interactive`}
            style={{
              padding: '16px',
              border:
                preferences.memoryConsent === 'ephemeral'
                  ? '2px solid var(--active-mood-primary)'
                  : '1px solid var(--solace-border)',
              backgroundColor:
                preferences.memoryConsent === 'ephemeral'
                  ? 'var(--active-mood-bg)'
                  : 'var(--solace-card-bg)'
            }}
          >
            <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start' }}>
              <div
                style={{
                  width: '36px',
                  height: '36px',
                  borderRadius: 'var(--radius-pill)',
                  backgroundColor: 'var(--solace-surface-raised)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: 'var(--solace-warning)',
                  flexShrink: 0
                }}
              >
                <EyeOff size={20} />
              </div>
              <div>
                <h3 style={{ fontSize: '1rem', fontWeight: 700, margin: 0 }}>
                  Keep Ephemeral (Session-Only Mode)
                </h3>
                <p style={{ fontSize: '0.82rem', color: 'var(--solace-secondary-text)', margin: '4px 0 0', lineHeight: 1.5 }}>
                  Zero data written to disk. All check-ins and companion dialogue exist only in memory during this active tab and vanish permanently upon refresh or exit.
                </p>
              </div>
            </div>
          </div>
        </div>

        <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'flex-end' }}>
          <button
            onClick={() => setIsMemoryModalOpen(false)}
            className="btn-secondary"
            style={{ width: '100%', minHeight: '46px' }}
          >
            Save Choice & Continue
          </button>
        </div>
      </div>
    </dialog>
  );
};
