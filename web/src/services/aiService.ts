import { ChatMessage, MoodId, RegulationModeId, SafetyQuestionId } from '../types/solace';
import { SafetyService, SafetyAssessment } from './safetyService';
import { DeterministicConversationEngine } from './conversationEngine';

// ============================================================================
// Solace AI Companion Service
// Google Gemini Direct Integration + Local Adaptive Sanctuary Engine
// ============================================================================

export interface AiRequestOptions {
  userName?: string;
  apiKey?: string;
  model?: 'gemini-2.5-flash' | 'gemini-2.0-flash' | 'gemini-1.5-flash';
  role?: 'companion' | 'somatic' | 'cbt' | 'wisdom';
  regulationMode: RegulationModeId;
  currentMood?: MoodId;
  moodIntensity?: number;
  recentJournalSnippet?: string;
  conversationHistory: ChatMessage[];
  pendingQuestionId?: SafetyQuestionId;
}

export interface AiResponseResult {
  text: string;
  safety: SafetyAssessment;
  isOfflineFallback: boolean;
}

export class AiService {
  /**
   * Main entrypoint: Evaluates safety first, then calls Gemini API if key is present,
   * or falls back seamlessly to the Adaptive Sanctuary Engine.
   */
  static async generateResponse(
    latestUserMessage: string,
    options: AiRequestOptions
  ): Promise<AiResponseResult> {
    // 1. Safety assessment on the latest user message
    const safety = SafetyService.evaluateMessage(latestUserMessage, options.pendingQuestionId);

    if (safety.requiresInterception && safety.responseText) {
      return {
        text: safety.responseText,
        safety,
        isOfflineFallback: false
      };
    }

    // 2. Check if a Gemini API Key is configured
    const apiKey = options.apiKey?.trim();

    if (apiKey) {
      try {
        const text = await this.callGeminiApi(latestUserMessage, apiKey, options);
        return {
          text,
          safety,
          isOfflineFallback: false
        };
      } catch (err) {
        console.warn('Gemini API call failed, switching gracefully to Adaptive Sanctuary Engine:', err);
      }
    }

    // 3. Deterministic Emotional Conversation Engine (100% Offline, Rule-Based, Sovereign)
    const turnResult = DeterministicConversationEngine.processTurn(
      latestUserMessage,
      options.userName,
      options.regulationMode,
      options.pendingQuestionId
    );

    return {
      text: turnResult.text,
      safety: turnResult.safety,
      isOfflineFallback: true
    };
  }

  /**
   * Direct Google Gemini REST API Client
   */
  private static async callGeminiApi(
    userMessage: string,
    apiKey: string,
    options: AiRequestOptions
  ): Promise<string> {
    const model = options.model || 'gemini-2.5-flash';
    const systemPrompt = this.buildSystemPrompt(options);

    // Format chat history into Gemini contents format
    const contents: Array<{ role: string; parts: Array<{ text: string }> }> = [];

    const recentHistory = options.conversationHistory.slice(-6);
    for (const msg of recentHistory) {
      contents.push({
        role: msg.role === 'user' ? 'user' : 'model',
        parts: [{ text: msg.text }]
      });
    }

    contents.push({
      role: 'user',
      parts: [{ text: userMessage }]
    });

    const payload = {
      contents,
      systemInstruction: {
        parts: [{ text: systemPrompt }]
      },
      generationConfig: {
        temperature: 0.75,
        maxOutputTokens: 600,
        topK: 40
      }
    };

    const modelsToTry = [model, 'gemini-2.0-flash', 'gemini-1.5-flash'];
    let lastError: Error | null = null;

    for (const targetModel of modelsToTry) {
      try {
        const url = `https://generativelanguage.googleapis.com/v1beta/models/${targetModel}:generateContent?key=${apiKey}`;
        const response = await fetch(url, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });

        if (!response.ok) {
          const errData = await response.json().catch(() => ({}));
          throw new Error(errData?.error?.message || `HTTP ${response.status}`);
        }

        const data = await response.json();
        const candidate = data.candidates?.[0];
        const generated = candidate?.content?.parts?.[0]?.text;

        if (generated && generated.trim().length > 0) {
          return generated.trim();
        }
      } catch (e: any) {
        lastError = e;
      }
    }

    throw lastError || new Error('No candidate returned from Gemini');
  }

  /**
   * Builds the System Instruction for Gemini
   */
  private static buildSystemPrompt(options: AiRequestOptions): string {
    const cleanName = options.userName?.trim();
    const hasName =
      cleanName &&
      !['sanctuary', 'user', 'guest', 'sanctuary guest'].includes(cleanName.toLowerCase());

    const nameInstruction = hasName
      ? `You are conversing with ${cleanName}. Address them warmly by name sparingly.`
      : `Address the user warmly in the second person. Do not make up names or use clinical labels.`;

    const moodContext = options.currentMood
      ? `User's latest recorded mood space is "${options.currentMood}" (Intensity: ${options.moodIntensity || 5}/10).`
      : '';

    const journalContext = options.recentJournalSnippet
      ? `User's recent reflection note: "${options.recentJournalSnippet}".`
      : '';

    const modeGuides: Record<RegulationModeId, string> = {
      listen:
        'MODE: LISTEN. Provide pure unconditional holding space. Reflect what you hear with deep compassion. Never offer unsolicited solutions or hurry the user.',
      understand:
        'MODE: UNDERSTAND. Help the user gently unlayer what is happening beneath the surface with warm, curious questions.',
      calm:
        'MODE: CALM. Ground the nervous system. Provide polyvagal cues: relaxing the shoulders, softening the jaw, slow unhurried breaths.',
      reflect:
        'MODE: REFLECT. Offer gentle cognitive mirroring and philosophical reframing. Help discover wisdom without invalidating pain.',
      refocus:
        'MODE: REFOCUS. Help reduce overwhelm into one tiny, kind, zero-pressure micro-step for the next 10 minutes.',
      gratitude:
        'MODE: GRATITUDE. Gently illuminate small, quiet mercies from today without forced positivity.'
    };

    return `
You are Solace, a gentle, private emotional sanctuary companion.
${nameInstruction}
${moodContext}
${journalContext}

Core Sanctuary Invariants:
1. Grounded, warm, and soothing. Never clinical, robotic, or dismissive.
2. Keep responses concise and readable (1 to 3 unhurried paragraphs).
3. Do not push toxic positivity or tell them to "look on the bright side" when they are in distress.
4. ${modeGuides[options.regulationMode] || modeGuides.listen}
`.trim();
  }

  /**
   * Adaptive Sanctuary Engine
   * Provides emotionally resonant, context-aware responses when offline or without an API key.
   */
  private static generateAdaptiveResponse(
    userMessage: string,
    options: AiRequestOptions
  ): string {
    const text = userMessage.toLowerCase();
    const name = options.userName?.trim();
    const greeting = name && name.toLowerCase() !== 'guest' ? `${name}, ` : '';

    // Check message intent / topic
    if (text.includes('breath') || text.includes('panic') || text.includes('tight') || text.includes('racing')) {
      return (
        `${greeting}take a slow pause with me right here. Put your hand gently over your chest or belly, and let your jaw soften.\n\n` +
        "You don't need to figure out everything in this single minute. Inhale gently for four counts, and let out a long, quiet sigh. I am here holding space with you."
      );
    }

    if (text.includes('tired') || text.includes('exhausted') || text.includes('burnout') || text.includes('sleep')) {
      return (
        `Your exhaustion is not a failing, ${greeting ? greeting : 'friend'}—it is your nervous system asking for tenderness.\n\n` +
        "Give yourself permission to do less today. Even five minutes of resting your eyes without your phone or expectations is a gentle gift to yourself."
      );
    }

    if (text.includes('work') || text.includes('deadline') || text.includes('busy') || text.includes('too much')) {
      return (
        "When everything demands your attention at once, everything feels like an emergency. But it isn't.\n\n" +
        "What is one tiny thing you can take off your shoulders for the rest of today? You are allowed to protect your peace."
      );
    }

    if (text.includes('lonely') || text.includes('alone') || text.includes('nobody')) {
      return (
        "Loneliness can feel so quiet and heavy, like an echo in an empty room.\n\n" +
        `I want you to know you are deeply welcome in this sanctuary. You don't have to put on a brave face here. What is the feeling asking of you right now?`
      );
    }

    // Fallbacks based on active Regulation Mode
    switch (options.regulationMode) {
      case 'calm':
        return (
          `Notice where your body is contacting the chair or ground right now. Drop your shoulders two inches down.\n\n` +
          "Your nervous system is safe in this moment. We can take another unhurried breath together whenever you're ready."
        );

      case 'understand':
        return (
          `Thank you for sharing that with me. It sounds like there are multiple layers tangled together here.\n\n` +
          "If you had to name the quietest, most vulnerable feeling underneath all of this, what would it say?"
        );

      case 'reflect':
        return (
          "Feelings are like weather systems moving through an open sky—intense, stormy at times, but always impermanent.\n\n" +
          "What would a dear, compassionate mentor say to you as you walk through this?"
        );

      case 'refocus':
        return (
          "When we feel paralyzed, big steps are impossible. But a micro-step is always waiting.\n\n" +
          "Could you drink a glass of water, step outside for fresh air, or stretch your hands? Just one kind thing for your body right now."
        );

      case 'gratitude':
        return (
          "Even on cloudy days, small warmth remains. Maybe it was the smell of tea, a cozy blanket, or simply making it through a hard hour.\n\n" +
          "What is one quiet mercy you can appreciate today?"
        );

      case 'listen':
      default:
        return (
          `I am listening with you, ${greeting}holding this space without any pressure or rush.\n\n` +
          "Everything you are feeling makes complete sense given what you are carrying. Take all the time you need."
        );
    }
  }
}
