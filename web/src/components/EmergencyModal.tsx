import React, { useEffect, useRef, useState } from 'react';
import { useSanctuary } from '../context/SanctuaryContext';
import { EMERGENCY_RESOURCES } from '../services/safetyService';
import { Phone, PhoneCall, Copy, Check, X, ShieldAlert, Heart } from 'lucide-react';

export const EmergencyModal: React.FC = () => {
  const { isEmergencyModalOpen, setIsEmergencyModalOpen } = useSanctuary();
  const dialogRef = useRef<HTMLDialogElement>(null);
  const [copiedNumber, setCopiedNumber] = useState<string | null>(null);

  useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) return;

    if (isEmergencyModalOpen) {
      if (!dialog.open) {
        dialog.showModal();
      }
    } else {
      if (dialog.open) {
        dialog.close();
      }
    }
  }, [isEmergencyModalOpen]);

  const handleClose = () => {
    setIsEmergencyModalOpen(false);
  };

  const handleCopy = (num: string) => {
    navigator.clipboard.writeText(num);
    setCopiedNumber(num);
    setTimeout(() => setCopiedNumber(null), 2500);
  };

  return (
    <dialog
      ref={dialogRef}
      onClose={handleClose}
      aria-labelledby="emergency-dialog-title"
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
                backgroundColor: 'var(--solace-emergency-soft)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                color: 'var(--solace-emergency)'
              }}
            >
              <ShieldAlert size={22} />
            </div>
            <div>
              <h2
                id="emergency-dialog-title"
                style={{ fontSize: '1.25rem', fontWeight: 700, margin: 0 }}
              >
                Immediate Crisis Support
              </h2>
              <p style={{ fontSize: '0.85rem', color: 'var(--solace-secondary-text)', margin: '2px 0 0' }}>
                Compassionate, confidential care available 24/7.
              </p>
            </div>
          </div>
          <button
            onClick={handleClose}
            className="btn-ghost"
            style={{ padding: '6px', borderRadius: 'var(--radius-pill)' }}
            aria-label="Close crisis support dialog"
          >
            <X size={20} />
          </button>
        </div>

        <div
          style={{
            backgroundColor: 'var(--solace-surface-raised)',
            padding: '14px 16px',
            borderRadius: 'var(--radius-lg)',
            border: '1px solid var(--solace-border)',
            marginBottom: '20px',
            fontSize: '0.9rem',
            lineHeight: 1.5,
            color: 'var(--solace-primary-text)'
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '4px' }}>
            <Heart size={16} style={{ color: 'var(--solace-emergency)' }} />
            <strong style={{ fontWeight: 600 }}>You are not alone right now.</strong>
          </div>
          If you or someone you know is in immediate danger or having thoughts of self-harm, please
          connect with these free, verified resources immediately.
        </div>

        {/* Resources List */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {EMERGENCY_RESOURCES.map((resource) => (
            <div
              key={resource.number}
              style={{
                padding: '16px',
                borderRadius: 'var(--radius-lg)',
                backgroundColor: 'var(--solace-card-bg)',
                border: '1px solid var(--solace-border)',
                display: 'flex',
                flexDirection: 'column',
                gap: '8px'
              }}
            >
              <div
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                  gap: '8px'
                }}
              >
                <div>
                  <h3 style={{ fontSize: '1rem', fontWeight: 700, margin: 0 }}>{resource.name}</h3>
                  <p style={{ fontSize: '0.8rem', color: 'var(--solace-secondary-text)', margin: '2px 0 0' }}>
                    {resource.description}
                  </p>
                </div>
                <span
                  style={{
                    fontSize: '1.1rem',
                    fontWeight: 800,
                    fontFamily: 'monospace',
                    color: 'var(--solace-emergency)'
                  }}
                >
                  {resource.number}
                </span>
              </div>

              <div style={{ display: 'flex', gap: '8px', marginTop: '6px' }}>
                <a
                  href={`tel:${resource.number}`}
                  className="btn-primary"
                  style={{
                    flex: 1,
                    textDecoration: 'none',
                    minHeight: '40px',
                    padding: '8px 14px',
                    fontSize: '0.85rem',
                    backgroundColor: 'var(--solace-emergency)',
                    color: '#FFF',
                    boxShadow: 'none'
                  }}
                >
                  <PhoneCall size={16} />
                  <span>Call Now</span>
                </a>
                <button
                  type="button"
                  onClick={() => handleCopy(resource.number)}
                  className="btn-secondary"
                  style={{ minHeight: '40px', padding: '8px 14px', fontSize: '0.85rem' }}
                  aria-label={`Copy helpline number ${resource.number}`}
                >
                  {copiedNumber === resource.number ? (
                    <>
                      <Check size={16} style={{ color: 'var(--solace-success)' }} />
                      <span>Copied</span>
                    </>
                  ) : (
                    <>
                      <Copy size={16} />
                      <span>Copy</span>
                    </>
                  )}
                </button>
              </div>
            </div>
          ))}
        </div>

        <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'flex-end' }}>
          <button
            onClick={handleClose}
            className="btn-secondary"
            style={{ width: '100%', minHeight: '46px' }}
          >
            Return to Sanctuary
          </button>
        </div>
      </div>
    </dialog>
  );
};
