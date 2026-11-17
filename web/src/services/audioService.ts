// ============================================================================
// Solace+ Web Audio Service
// Synthesizes soothing somatic soundscapes & singing bowls via Web Audio API
// Zero external asset dependencies
// ============================================================================

class AudioService {
  private audioCtx: AudioContext | null = null;
  private isEnabled: boolean = true;

  constructor() {
    // AudioContext will be initialized on first user interaction per browser autoplay policies
  }

  private getContext(): AudioContext | null {
    if (typeof window === 'undefined') return null;
    if (!this.audioCtx) {
      const AudioContextClass = window.AudioContext || (window as any).webkitAudioContext;
      if (AudioContextClass) {
        this.audioCtx = new AudioContextClass();
      }
    }
    if (this.audioCtx && this.audioCtx.state === 'suspended') {
      this.audioCtx.resume();
    }
    return this.audioCtx;
  }

  public setEnabled(enabled: boolean) {
    this.isEnabled = enabled;
  }

  public getEnabled(): boolean {
    return this.isEnabled;
  }

  /**
   * Plays a warm, gentle singing bowl chime with acoustic harmonics (432Hz Solfeggio frequency)
   */
  public playSingingBowlChime(fundamentalFreq: number = 432, durationSeconds: number = 2.5) {
    if (!this.isEnabled) return;
    const ctx = this.getContext();
    if (!ctx) return;

    try {
      const now = ctx.currentTime;
      const masterGain = ctx.createGain();
      masterGain.connect(ctx.destination);
      masterGain.gain.setValueAtTime(0.12, now);
      masterGain.gain.exponentialRampToValueAtTime(0.0001, now + durationSeconds);

      // Warm harmonics: Fundamental, Octave, and 5th
      const harmonics = [1, 2, 2.98];
      const harmonicWeights = [0.6, 0.25, 0.15];

      harmonics.forEach((mult, index) => {
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();

        osc.type = 'sine';
        osc.frequency.setValueAtTime(fundamentalFreq * mult, now);

        gain.gain.setValueAtTime(harmonicWeights[index], now);
        gain.gain.exponentialRampToValueAtTime(0.001, now + durationSeconds);

        osc.connect(gain);
        gain.connect(masterGain);

        osc.start(now);
        osc.stop(now + durationSeconds);
      });
    } catch (e) {
      console.warn('Audio playback not permitted yet:', e);
    }
  }

  /**
   * Gentle breath phase change cue
   */
  public playBreathCue(phase: 'inhale' | 'hold' | 'exhale' | 'rest') {
    if (!this.isEnabled) return;
    switch (phase) {
      case 'inhale':
        this.playSingingBowlChime(396, 1.8);
        break;
      case 'hold':
        this.playSingingBowlChime(432, 1.2);
        break;
      case 'exhale':
        this.playSingingBowlChime(320, 2.0);
        break;
      case 'rest':
        this.playSingingBowlChime(256, 1.0);
        break;
    }
  }

  /**
   * Subtle water droplet click for UI interactions
   */
  public playSoftTick() {
    if (!this.isEnabled) return;
    const ctx = this.getContext();
    if (!ctx) return;

    try {
      const now = ctx.currentTime;
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();

      osc.type = 'sine';
      osc.frequency.setValueAtTime(800, now);
      osc.frequency.exponentialRampToValueAtTime(400, now + 0.06);

      gain.gain.setValueAtTime(0.04, now);
      gain.gain.exponentialRampToValueAtTime(0.0001, now + 0.06);

      osc.connect(gain);
      gain.connect(ctx.destination);

      osc.start(now);
      osc.stop(now + 0.06);
    } catch {
      // Ignore user gesture restrictions
    }
  }
}

export const soundManager = new AudioService();
