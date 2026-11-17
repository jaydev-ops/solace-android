// ============================================================================
// Solace+ Conversational State Tracker (Web / TypeScript)
// Tracks multi-turn conversational progression, contextual emotion transitions,
// disclosed situations, and sliding-window template deliveries.
// ============================================================================

import { EmotionCategory } from './emotionClassifier.ts';
import { UserSituation } from './situationalContext.ts';

export type ConversationalStage =
  | 'STAGE_1_INITIAL_DISCLOSURE'
  | 'STAGE_2_CONTEXT_DISCOVERY'
  | 'STAGE_3_EMOTIONAL_SUPPORT'
  | 'STAGE_4_CONTINUED_CONVERSATION'
  | 'STAGE_5_SUPPORTIVE_FOLLOW_THROUGH';

export interface ConversationStateRecord {
  conversationId: string;
  primaryEmotion: EmotionCategory | null;
  secondaryEmotions: Set<EmotionCategory>;
  disclosedSituations: Set<UserSituation>;
  lastDisclosedSituation: UserSituation | null;
  currentStage: ConversationalStage;
  turnCount: IntRange;
  lastUserMessage: string;
  consecutiveIdenticalMessageCount: number;
  deliveredTemplateIds: string[];
  deliveredResponses: string[];
}

type IntRange = number;

export class ConversationalStateTracker {
  private static sessions = new Map<string, ConversationStateRecord>();

  static getOrCreate(conversationId = 'active_session'): ConversationStateRecord {
    const safeId = conversationId.trim() || 'active_session';
    let record = this.sessions.get(safeId);
    if (!record) {
      record = {
        conversationId: safeId,
        primaryEmotion: null,
        secondaryEmotions: new Set<EmotionCategory>(),
        disclosedSituations: new Set<UserSituation>(),
        lastDisclosedSituation: null,
        currentStage: 'STAGE_1_INITIAL_DISCLOSURE',
        turnCount: 0,
        lastUserMessage: '',
        consecutiveIdenticalMessageCount: 0,
        deliveredTemplateIds: [],
        deliveredResponses: []
      };
      this.sessions.set(safeId, record);
    }
    return record;
  }

  static clearSession(conversationId = 'active_session'): void {
    const safeId = conversationId.trim() || 'active_session';
    this.sessions.delete(safeId);
  }

  /**
   * Updates state upon receiving a user message and running classification.
   */
  static updateState(
    conversationId: string,
    userMessage: string,
    detectedCategory: EmotionCategory | null,
    secondaryCategories: EmotionCategory[],
    detectedSituation: UserSituation | null
  ): ConversationStateRecord {
    const state = this.getOrCreate(conversationId);
    const normalized = userMessage.trim().toLowerCase();

    // 1. Check for identical message repetition
    if (state.lastUserMessage.length > 0 && state.lastUserMessage === normalized) {
      state.consecutiveIdenticalMessageCount += 1;
    } else {
      state.consecutiveIdenticalMessageCount = 0;
      state.lastUserMessage = normalized;
    }

    state.turnCount += 1;

    // 2. Situation Tracking & Preservation
    if (detectedSituation) {
      state.disclosedSituations.add(detectedSituation);
      state.lastDisclosedSituation = detectedSituation;
    }

    // 3. Contextual Emotion Transitions
    // If we previously had an emotion (e.g. ANXIETY) and now learn of a situational trigger (e.g. BREAKUP),
    // elevate the situational emotion to primary and demote previous emotion to secondary!
    if (detectedSituation) {
      const previousPrimary = state.primaryEmotion;
      if (previousPrimary && previousPrimary !== detectedCategory) {
        state.secondaryEmotions.add(previousPrimary);
      }

      switch (detectedSituation) {
        case 'BREAKUP':
        case 'HEARTBREAK':
        case 'BEING_CHEATED_ON':
        case 'RELATIONSHIP_ARGUMENTS':
        case 'BEING_IGNORED':
        case 'BEING_REJECTED':
        case 'LONG_DISTANCE':
        case 'FEAR_OF_LOSING_SOMEONE':
        case 'FEELING_UNWANTED':
          state.primaryEmotion = 'RELATIONSHIP_PROBLEMS';
          if (previousPrimary === 'ANXIETY') {
            state.secondaryEmotions.add('ANXIETY');
          }
          state.secondaryEmotions.add('SADNESS');
          break;

        case 'MISSING_EX':
          state.primaryEmotion = 'SADNESS';
          state.secondaryEmotions.add('LONELINESS');
          state.secondaryEmotions.add('RELATIONSHIP_PROBLEMS');
          break;

        case 'EX_SOCIAL_MEDIA_CHECKING':
          state.primaryEmotion = 'OVERTHINKING';
          state.secondaryEmotions.add('RELATIONSHIP_PROBLEMS');
          state.secondaryEmotions.add('ANXIETY');
          break;

        case 'UNSUCCESSFUL_EFFORT':
        case 'EXAM_FAILURE':
        case 'POOR_GRADES':
        case 'ASSIGNMENT_PRESSURE':
        case 'DIFFICULTY_CONCENTRATING':
          state.primaryEmotion = 'ACADEMIC_STRESS';
          state.secondaryEmotions.add('ANXIETY');
          state.secondaryEmotions.add('LOW_SELF_ESTEEM');
          break;

        case 'ACADEMIC_COMPARISON':
        case 'PEER_ACHIEVEMENT_ENVY':
        case 'SOCIAL_MEDIA_ENVY':
          state.primaryEmotion = 'LOW_SELF_ESTEEM';
          state.secondaryEmotions.add('INSECURITY');
          state.secondaryEmotions.add('ANXIETY');
          break;

        case 'FRIENDSHIP_DRIFT':
        case 'ONE_SIDED_FRIENDSHIP':
        case 'THIRD_WHEEL':
        case 'LOSING_FRIENDS':
        case 'FEELING_EXCLUDED':
        case 'SOCIAL_REJECTION':
          state.primaryEmotion = 'LONELINESS';
          state.secondaryEmotions.add('SADNESS');
          state.secondaryEmotions.add('RELATIONSHIP_PROBLEMS');
          break;

        case 'BEING_JUDGED':
        case 'FRIENDSHIP_CONFLICTS':
          state.primaryEmotion = 'SOCIAL_ANXIETY';
          break;

        case 'PARENTAL_CAREER_PRESSURE':
        case 'SIBLING_COMPARISON':
        case 'FAMILY_CONFLICTS':
        case 'DISAPPOINTING_PARENTS':
          state.primaryEmotion = 'FAMILY_CONFLICT';
          state.secondaryEmotions.add('ANGER');
          state.secondaryEmotions.add('GUILT');
          break;

        case 'DIRECTIONLESS':
        case 'FEAR_WRONG_DEGREE':
        case 'FINANCIAL_PRESSURE':
        case 'CAREER_UNCERTAINTY':
          state.primaryEmotion = 'ANXIETY';
          state.secondaryEmotions.add('FEAR');
          state.secondaryEmotions.add('OVERTHINKING');
          break;

        case 'LOSS_OF_LOVED_ONE':
          state.primaryEmotion = 'GRIEF';
          state.secondaryEmotions.add('SADNESS');
          break;

        case 'PERSONAL_DISAPPOINTMENT':
          state.primaryEmotion = 'LOW_SELF_ESTEEM';
          state.secondaryEmotions.add('GUILT');
          break;

        // Batch 2: Scenario 6 (Misunderstood and Unheard)
        case 'PERSPECTIVE_DISMISSED':
        case 'LABELED_OVERLY_SENSITIVE':
        case 'COMMUNICATION_BREAKDOWN':
          state.primaryEmotion = 'FRUSTRATION';
          state.secondaryEmotions.add('LONELINESS');
          state.secondaryEmotions.add('ANGER');
          break;

        // Batch 2: Scenario 7 (Regret and Guilt)
        case 'PAST_DECISION_REGRET':
        case 'HURT_SOMEONE':
        case 'SELF_BLAME':
          state.primaryEmotion = 'GUILT';
          state.secondaryEmotions.add('SHAME');
          state.secondaryEmotions.add('OVERTHINKING');
          break;

        // Batch 2: Scenario 8 (Sleep Difficulties & Night Rumination)
        case 'CANNOT_SLEEP_INSOMNIA':
        case 'NIGHT_RUMINATION':
        case 'LATE_NIGHT_LONELINESS':
          state.primaryEmotion = 'SLEEP_DIFFICULTIES';
          state.secondaryEmotions.add('OVERTHINKING');
          state.secondaryEmotions.add('ANXIETY');
          break;

        // Batch 2: Scenario 9 (Social Anxiety & Fear of Judgement)
        case 'MEETING_NEW_PEOPLE':
        case 'POST_EVENT_RUMINATION':
        case 'FEELING_AWKWARD':
        case 'PUBLIC_SPEAKING_FEAR':
          state.primaryEmotion = 'SOCIAL_ANXIETY';
          state.secondaryEmotions.add('FEAR');
          state.secondaryEmotions.add('INSECURITY');
          break;

        // Batch 2: Scenario 10 (Procrastination & Productivity Distress)
        case 'PHONE_DOOMSCROLLING':
        case 'TASK_OVERWHELM':
        case 'AVOIDING_TASKS':
          state.primaryEmotion = 'PROCRASTINATION';
          state.secondaryEmotions.add('GUILT');
          state.secondaryEmotions.add('ANXIETY');
          break;

        // Batch 3: Scenario 11 (Emotional Exhaustion & Burnout)
        case 'MENTAL_DRAIN':
        case 'ANHEDONIA_LOSS_OF_INTEREST':
        case 'RESPONSIBILITY_OVERWHELM':
        case 'WITHDRAWAL_URGE':
          state.primaryEmotion = 'BURNOUT';
          state.secondaryEmotions.add('EMOTIONAL_EXHAUSTION');
          state.secondaryEmotions.add('DEPRESSION_DISTRESS');
          break;

        // Batch 3: Scenario 12 (Financial Stress & Dependence)
        case 'EXPENSE_MANAGEMENT':
        case 'PARENTAL_FINANCIAL_DEPENDENCE':
        case 'PEER_WEALTH_COMPARISON':
          state.primaryEmotion = 'ANXIETY';
          state.secondaryEmotions.add('INSECURITY');
          state.secondaryEmotions.add('SHAME');
          break;

        // Batch 3: Scenario 13 (Life Transitions & Unfamiliar Environments)
        case 'COLLEGE_START':
        case 'RELOCATING_NEW_CITY':
        case 'ROUTINE_LOSS':
        case 'NOSTALGIA_PAST':
          state.primaryEmotion = 'ANXIETY';
          state.secondaryEmotions.add('LONELINESS');
          state.secondaryEmotions.add('SADNESS');
          break;

        // Batch 3: Scenario 14 (Interpersonal Conflict & Boundary Setting)
        case 'HEATED_ARGUMENT':
        case 'DISRESPECT_FELT':
          state.primaryEmotion = 'ANGER';
          state.secondaryEmotions.add('FRUSTRATION');
          state.secondaryEmotions.add('RELATIONSHIP_PROBLEMS');
          break;

        // Batch 3: Scenario 15 (Rejection & Unreciprocated Feelings)
        case 'ROMANTIC_REJECTION':
        case 'UNRECIPROCATED_FEELINGS':
        case 'FEELING_UNWANTED_AFTER_NO':
          state.primaryEmotion = 'REJECTION';
          state.secondaryEmotions.add('HEARTBREAK');
          state.secondaryEmotions.add('LOW_SELF_ESTEEM');
          break;

        // Batch 4: Scenario 16 (Grief and Loss)
        case 'PET_LOSS':
        case 'ANNIVERSARY_GRIEF':
        case 'ABSENCE_LONGING':
          state.primaryEmotion = 'GRIEF';
          state.secondaryEmotions.add('SADNESS');
          state.secondaryEmotions.add('LONELINESS');
          break;

        // Batch 4: Scenario 17 (Body Image & Self-Confidence)
        case 'APPEARANCE_INSECURITY':
        case 'WEIGHT_DISSATISFACTION':
        case 'PHOTO_ANXIETY':
          state.primaryEmotion = 'INSECURITY';
          state.secondaryEmotions.add('LOW_SELF_ESTEEM');
          state.secondaryEmotions.add('SHAME');
          break;

        // Batch 4: Scenario 18 (Positive Emotions & Celebration)
        case 'ACADEMIC_ACHIEVEMENT':
        case 'PERSONAL_GOAL_REACHED':
        case 'GENERAL_GRATITUDE':
          state.primaryEmotion = 'POSITIVE_EMOTIONS';
          break;

        // Batch 4: Scenario 19 (Everyday Frustration & Casual Venting)
        case 'BAD_DAY':
        case 'IRRITATED_SMALL_THINGS':
        case 'CASUAL_VENTING':
          state.primaryEmotion = 'FRUSTRATION';
          state.secondaryEmotions.add('ANGER');
          break;

        // Batch 4: Scenario 20 (Romantic Relationship Support)
        case 'RELATIONSHIP_INSECURITY':
        case 'PARTNER_DISTANCING':
        case 'FEAR_OF_ABANDONMENT':
        case 'JEALOUSY':
          state.primaryEmotion = 'RELATIONSHIP_PROBLEMS';
          state.secondaryEmotions.add('INSECURITY');
          state.secondaryEmotions.add('ANXIETY');
          break;
      }
    } else if (detectedCategory) {
      if (!state.primaryEmotion) {
        state.primaryEmotion = detectedCategory;
      } else {
        state.secondaryEmotions.add(state.primaryEmotion);
        state.primaryEmotion = detectedCategory;
      }
    }

    secondaryCategories.forEach((sc) => state.secondaryEmotions.add(sc));

    // 4. Conversational Stage Transition
    if (state.disclosedSituations.has('EX_SOCIAL_MEDIA_CHECKING')) {
      state.currentStage = 'STAGE_5_SUPPORTIVE_FOLLOW_THROUGH';
    } else if (state.disclosedSituations.has('MISSING_EX')) {
      state.currentStage = 'STAGE_4_CONTINUED_CONVERSATION';
    } else if (state.turnCount >= 5) {
      state.currentStage = 'STAGE_5_SUPPORTIVE_FOLLOW_THROUGH';
    } else if (state.turnCount === 4) {
      state.currentStage = 'STAGE_4_CONTINUED_CONVERSATION';
    } else if (state.turnCount === 3) {
      state.currentStage = 'STAGE_3_EMOTIONAL_SUPPORT';
    } else if (state.turnCount === 2) {
      state.currentStage = 'STAGE_2_CONTEXT_DISCOVERY';
    } else {
      state.currentStage = 'STAGE_1_INITIAL_DISCLOSURE';
    }

    return state;
  }

  static recordDelivery(
    conversationId: string,
    templateId: string,
    responseText: string
  ): void {
    const state = this.getOrCreate(conversationId);
    state.deliveredTemplateIds.push(templateId);
    state.deliveredResponses.push(responseText);
  }
}
