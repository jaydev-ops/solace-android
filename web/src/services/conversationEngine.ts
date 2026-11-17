// ============================================================================
// Solace+ Deterministic Emotional Conversation Engine (Web / TypeScript)
// 100% Offline, Sovereign, Non-Repetitive Dialogue & Safety State Machine
// ============================================================================

import { EmotionCategory, EmotionClassifier } from './emotionClassifier.ts';
import {
  RESPONSE_TEMPLATES,
  CRISIS_RESPONSES,
  CLARIFICATION_INQUIRIES,
  getRepeatedDisclosureResponse,
  getRepeatedIdenticalMessageResponse,
  ResponseTemplate
} from './responseLibrary.ts';
import { SafetyService, SafetyAssessment } from './safetyService.ts';
import { RegulationModeId, RiskLevel, SafetyQuestionId, SAFETY_QUESTIONS } from '../types/solace.ts';
import { SituationalContextEngine, UserSituation } from './situationalContext.ts';
import {
  ConversationalStateTracker,
  ConversationalStage,
  ConversationStateRecord
} from './conversationalStateTracker.ts';

export interface EngineTurnResult {
  text: string;
  templateId?: string;
  category: EmotionCategory | null;
  mode: RegulationModeId;
  safety: SafetyAssessment;
  isClarification: boolean;
  situation?: UserSituation | null;
  stage?: ConversationalStage;
}

export class DeterministicConversationEngine {
  private static recentTemplateIds: string[] = [];
  private static readonly SLIDING_WINDOW_SIZE = 10;

  /**
   * Clears in-memory recent template history and state tracker
   */
  static clearSession(conversationId = 'active_session'): void {
    this.recentTemplateIds = [];
    ConversationalStateTracker.clearSession(conversationId);
  }

  /**
   * Processes a conversation turn deterministically with full multi-turn contextual intelligence.
   */
  static processTurn(
    userMessage: string,
    userName?: string,
    preferredMode?: RegulationModeId,
    pendingQuestionId?: SafetyQuestionId,
    conversationId = 'active_session'
  ): EngineTurnResult {
    const trimmed = userMessage.trim();

    // =========================================================================
    // STEP 1: SAFETY ASSESSMENT ON THE LATEST USER MESSAGE
    // =========================================================================
    const safety = SafetyService.evaluateMessage(trimmed, pendingQuestionId);

    // If an assessment question was answered or crisis requires immediate interception
    if (safety.requiresInterception && safety.responseText) {
      return {
        text: safety.responseText,
        templateId: `SAFETY_${safety.questionId || 'CRISIS'}`,
        category: 'SUICIDAL_IDEATION',
        mode: 'calm',
        safety,
        isClarification: false
      };
    }

    // =========================================================================
    // STEP 2: DETERMINISTIC SITUATION & EMOTION CLASSIFICATION
    // =========================================================================
    const situationResult = SituationalContextEngine.detectSituation(trimmed);
    const classification = EmotionClassifier.classify(trimmed);
    let detectedCat = classification.primaryCategory;

    // =========================================================================
    // STEP 3: LEVEL 4 CRITICAL EMERGENCY & LEVEL 3 CRISIS OVERRIDE
    // =========================================================================
    if (detectedCat === 'IMMEDIATE_PHYSICAL_DANGER') {
      const emergencyText = CRISIS_RESPONSES[4][0];
      return {
        text: emergencyText,
        templateId: 'CRISIS_LVL_4',
        category: detectedCat,
        mode: 'calm',
        safety: {
          riskLevel: RiskLevel.LEVEL_4_CRITICAL,
          matchedKeywords: ['immediate_physical_danger'],
          requiresInterception: true,
          responseText: emergencyText,
          showEmergencyCard: true
        },
        isClarification: false
      };
    }

    if (detectedCat === 'SUICIDAL_IDEATION' || detectedCat === 'SELF_HARM') {
      const screeningText =
        "I hear how much pain you are carrying right now, and I want you to know you are not alone in this sanctuary. Because your life and safety are precious, may I ask gently:\n\n" +
        SAFETY_QUESTIONS[SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS];
      return {
        text: screeningText,
        templateId: 'CRISIS_LVL_3_TRIGGER',
        category: detectedCat,
        mode: 'calm',
        safety: {
          riskLevel: RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
          matchedKeywords: ['suicide_self_harm'],
          requiresInterception: true,
          questionId: SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS,
          responseText: screeningText,
          showEmergencyCard: true
        },
        isClarification: false
      };
    }

    // =========================================================================
    // STEP 4: MULTI-TURN CONVERSATION STATE & CONTEXTUAL EMOTION TRANSITION
    // =========================================================================
    const stateRecord = ConversationalStateTracker.updateState(
      conversationId,
      trimmed,
      detectedCat,
      classification.secondaryCategories,
      situationResult.primarySituation
    );

    // =========================================================================
    // STEP 5: REPEATED MESSAGE / REPEATED DISCLOSURE HANDLING
    // =========================================================================
    if (
      stateRecord.consecutiveIdenticalMessageCount === 1 &&
      stateRecord.lastDisclosedSituation === 'BREAKUP'
    ) {
      const repeatResponse =
        "I hear you. Going through a breakup is clearly weighing on you. You don't have to keep explaining yourself for your feelings to matter.";
      this.recordTemplateUsed('REPEAT_DISCLOSURE_BREAKUP');
      ConversationalStateTracker.recordDelivery(
        conversationId,
        'REPEAT_DISCLOSURE_BREAKUP',
        repeatResponse
      );
      return {
        text: repeatResponse,
        templateId: 'REPEAT_DISCLOSURE_BREAKUP',
        category: stateRecord.primaryEmotion,
        mode: 'listen',
        safety,
        isClarification: false,
        situation: stateRecord.lastDisclosedSituation,
        stage: stateRecord.currentStage
      };
    }

    // =========================================================================
    // STEP 6: CATEGORY NORMALIZATION & SITUATIONAL MAPPING
    // Map fine-grained categories to response pools so no category is orphaned
    // =========================================================================
    let effectiveCategory: EmotionCategory | null = stateRecord.primaryEmotion;
    switch (stateRecord.primaryEmotion) {
      case 'HEARTBREAK':
      case 'REJECTION':
        effectiveCategory = 'RELATIONSHIP_PROBLEMS';
        break;
      case 'PANIC':
        effectiveCategory = 'ANXIETY';
        break;
      case 'WORK_RELATED_STRESS':
        effectiveCategory = 'BURNOUT';
        break;
      case 'DEPRESSION_DISTRESS':
      case 'EMOTIONAL_EXHAUSTION':
        effectiveCategory = 'SADNESS';
        break;
      case 'FRUSTRATION':
        effectiveCategory = 'ANGER';
        break;
      case 'INSECURITY':
        effectiveCategory = 'LOW_SELF_ESTEEM';
        break;
      case 'SHAME':
        effectiveCategory = 'GUILT';
        break;
      case 'PROCRASTINATION':
        effectiveCategory = 'LACK_OF_MOTIVATION';
        break;
      default:
        effectiveCategory = stateRecord.primaryEmotion;
        break;
    }

    // =========================================================================
    // STEP 7: LOW-CONFIDENCE CLARIFICATION CHECK (NON-REPETITIVE ROTATION)
    // =========================================================================
    if (!effectiveCategory && !situationResult.primarySituation) {
      const unusedClarification =
        CLARIFICATION_INQUIRIES.find((q) => !this.recentTemplateIds.includes(q)) ||
        CLARIFICATION_INQUIRIES[0];

      this.recordTemplateUsed(unusedClarification);
      ConversationalStateTracker.recordDelivery(
        conversationId,
        'CLARIFICATION',
        unusedClarification
      );

      return {
        text: unusedClarification,
        templateId: 'CLARIFICATION',
        category: null,
        mode: preferredMode || 'listen',
        safety,
        isClarification: true,
        stage: stateRecord.currentStage
      };
    }

    // =========================================================================
    // STEP 8: CONVERSATIONAL MODE RESOLUTION (LISTEN, UNDERSTAND, REFLECT, CALM)
    // =========================================================================
    const resolvedMode: RegulationModeId =
      preferredMode || (effectiveCategory ? this.resolveModeForCategory(effectiveCategory) : 'listen');

    // =========================================================================
    // STEP 9: RESPONSE RELEVANCE SCORING & SELECTION
    // =========================================================================
    // A. Prefer situation-specific templates if situation is recognized
    const currentSituation =
      situationResult.primarySituation || stateRecord.lastDisclosedSituation;
    const situationCandidates = currentSituation
      ? RESPONSE_TEMPLATES.filter((t) => t.situation === currentSituation)
      : [];

    let selected: ResponseTemplate;

    if (situationCandidates.length > 0) {
      const unusedSituational = situationCandidates.filter(
        (t) => !this.recentTemplateIds.includes(t.id)
      );
      const sitPool = unusedSituational.length > 0 ? unusedSituational : situationCandidates;
      const scoredSit = sitPool
        .map((t) => {
          let score = 0;
          if (t.preferredMode === resolvedMode) score += 10;
          if (classification.intensity === 'HIGH' && t.suggestedGrounding) score += 5;
          if (t.stage === stateRecord.currentStage) score += 15;
          return { template: t, score };
        })
        .sort((a, b) => b.score - a.score);
      selected = scoredSit[0].template;
    } else {
      // B. Fall back to emotion category templates (excluding situation-specific templates)
      let candidates = RESPONSE_TEMPLATES.filter(
        (t) => t.category === effectiveCategory && !t.situation
      );
      if (candidates.length === 0) {
        candidates = RESPONSE_TEMPLATES.filter(
          (t) =>
            (classification.secondaryCategories.includes(t.category) ||
              stateRecord.secondaryEmotions.has(t.category)) &&
            !t.situation
        );
      }
      if (candidates.length === 0) {
        candidates = RESPONSE_TEMPLATES.filter(
          (t) => t.category === 'CONFUSION' && !t.situation
        );
      }

      // Exclude templates used in the last 10 turns
      const unusedCandidates = candidates.filter((t) => !this.recentTemplateIds.includes(t.id));
      const pool = unusedCandidates.length > 0 ? unusedCandidates : candidates;

      // Score candidates based on mode alignment, stage, and intensity
      const scored = pool
        .map((t) => {
          let score = 0;
          if (t.preferredMode === resolvedMode) score += 10;
          if (classification.intensity === 'HIGH' && t.suggestedGrounding) score += 5;
          if (t.stage === stateRecord.currentStage) score += 15;
          return { template: t, score };
        })
        .sort((a, b) => b.score - a.score);

      selected = scored[0].template;
    }

    this.recordTemplateUsed(selected.id);
    ConversationalStateTracker.recordDelivery(conversationId, selected.id, selected.text);

    // =========================================================================
    // STEP 10: SAFE PERSONALIZATION (Prevents single-letter placeholder bugs like "p.")
    // =========================================================================
    let finalText = selected.text;
    const rawName = userName?.trim();
    const isValidHumanName =
      Boolean(rawName) &&
      (rawName?.length ?? 0) >= 2 &&
      /^[a-zA-Z\s]+$/.test(rawName!) &&
      !['sanctuary', 'user', 'guest', 'sanctuary guest', 'anonymous'].includes(
        rawName!.toLowerCase()
      );

    if (isValidHumanName && !finalText.includes(rawName!)) {
      if (/^(hello|hi)\b/i.test(finalText)) {
        const firstSpace = finalText.indexOf(' ');
        if (firstSpace !== -1) {
          finalText =
            finalText.substring(0, firstSpace) +
            `, ${rawName}` +
            finalText.substring(firstSpace);
        }
      }
    }

    return {
      text: finalText,
      templateId: selected.id,
      category: effectiveCategory,
      mode: resolvedMode,
      safety,
      isClarification: false,
      situation: currentSituation,
      stage: stateRecord.currentStage
    };
  }

  private static recordTemplateUsed(templateId: string): void {
    this.recentTemplateIds.push(templateId);
    while (this.recentTemplateIds.length > this.SLIDING_WINDOW_SIZE) {
      this.recentTemplateIds.shift();
    }
  }

  private static resolveModeForCategory(cat: EmotionCategory): RegulationModeId {
    switch (cat) {
      case 'PANIC':
      case 'ANXIETY':
      case 'BURNOUT':
      case 'SLEEP_DIFFICULTIES':
        return 'calm';

      case 'OVERTHINKING':
      case 'ACADEMIC_STRESS':
      case 'WORK_RELATED_STRESS':
      case 'RELATIONSHIP_PROBLEMS':
      case 'HEARTBREAK':
      case 'REJECTION':
      case 'FAMILY_CONFLICT':
      case 'SOCIAL_ANXIETY':
      case 'ANGER':
      case 'FRUSTRATION':
      case 'CONFUSION':
      case 'PROCRASTINATION':
      case 'LACK_OF_MOTIVATION':
        return 'understand';

      case 'LOW_SELF_ESTEEM':
      case 'INSECURITY':
      case 'GUILT':
      case 'SHAME':
      case 'FEAR':
      case 'EMOTIONAL_NUMBNESS':
        return 'reflect';

      case 'SADNESS':
      case 'LONELINESS':
      case 'GRIEF':
      case 'DEPRESSION_DISTRESS':
      case 'EMOTIONAL_EXHAUSTION':
      case 'POSITIVE_EMOTIONS':
      default:
        return 'listen';
    }
  }
}
