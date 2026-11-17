import { EmergencyResource, RiskLevel, SafetyQuestionId, SAFETY_QUESTIONS } from '../types/solace';

// ============================================================================
// Solace+ Safety Engine & Crisis Interception System
// ============================================================================

export const EMERGENCY_RESOURCES: EmergencyResource[] = [
  {
    name: 'Emergency Services (India)',
    number: '112',
    description: 'National emergency assistance across India',
    category: 'emergency',
    country: 'IN'
  },
  {
    name: 'Tele-MANAS Helpline',
    number: '14416',
    description: 'Government of India 24/7 free toll-free mental health helpline',
    category: 'mental_health',
    country: 'IN'
  },
  {
    name: 'KIRAN Helpline',
    number: '1800-599-0019',
    description: 'Ministry of Social Justice 24/7 mental health helpline',
    category: 'mental_health',
    country: 'IN'
  },
  {
    name: '988 Suicide & Crisis Lifeline',
    number: '988',
    description: 'Free, confidential 24/7 call & text support (US & Canada)',
    category: 'mental_health',
    country: 'US'
  },
  {
    name: 'Crisis Text Line',
    number: '741741',
    description: 'Text HOME to 741741 for 24/7 crisis support via SMS',
    category: 'crisis_text',
    country: 'GLOBAL'
  }
];

// Crisis & Harm Keywords
const SUICIDE_KEYWORDS = [
  'suicide', 'suicidal', 'su1cide', 'kill myself', 'killing myself',
  'end my life', 'ending my life', 'take my life', 'end everything',
  'ending everything', "don't want to live", 'dont want to live',
  'do not want to live', "don't want to exist", 'dont want to exist',
  'wish i was dead', 'wish i were dead', 'rather be dead',
  'dying', 'want to die', 'ready to die', 'disappear forever',
  'never wake up', 'better off without me', 'better off dead',
  'life is not worth living', 'end it all', 'ending it all', 'kms'
];

const SELF_HARM_KEYWORDS = [
  'hurt myself', 'hurting myself', 'harm myself', 'harming myself',
  'cut myself', 'cutting myself', 'self-harm', 'self harm', 'selfharm',
  'overdose', 'overdosed', 'overdosing', 'poison myself', 'burn myself'
];

const SEVERE_DISTRESS_KEYWORDS = [
  'hopeless', 'hopelessness', 'worthless', 'worthlessness',
  'nobody needs me', 'no one needs me', 'no reason to live',
  'cannot take this anymore', "can't go on", 'unbearable pain'
];

const YES_REGEX = /\b(yes|yeah|yep|yup|i\s+do|i\s+have|sure|definitely|absolutely)\b/i;
const NO_REGEX = /\b(no|nope|nah|never|not\s+really|not\s+at\s+all|no\s+way|nothing\s+like\s+that|not\s+right\s+now|i\s+do\s*n['o]t\s+think\s+so|dont\s+think\s+so|i\s+am\s+not|i['\s]*m\s+not|im\s+not|not\s+me|no\s+i\s+do\s*n['o]t|definitely\s+not|absolutely\s+not|no\s+thoughts?|never\s+had\s+thoughts?|not\s+having\s+thoughts?|not\s+suicidal|no\s+suicidal\s+thoughts?|not\s+trying\s+to\s+hurt\s+myself|not\s+hurting\s+myself|don'?t\s+want\s+to\s+die|do\s+not\s+want\s+to\s+die|(i['\s]*m|i\s+am|im|i\s+m)\s+(good|fine|okay|ok|safe|better|alright|all\s+right|doing\s+well|doing\s+good|doing\s+fine|doing\s+ok|doing\s+okay|feeling\s+good|feeling\s+fine|feeling\s+ok|feeling\s+okay|feeling\s+better|happy|chilling|great|peaceful)|(doing\s+(good|fine|well|ok|okay)|all\s+(good|fine|okay|ok|right|alright)|feeling\s+(good|fine|ok|okay|better|great))|(just\s+overwhelmed|overwhelmed|describing\s+how\s+overwhelmed|just\s+stressed|just\s+tired|just\s+exhausted|feeling\s+overwhelmed|so\s+overwhelmed)|^(good|fine|okay|ok|safe|better|great|alright|all\s+right|chilling|doing\s+good|doing\s+well|doing\s+fine|all\s+good)$)\b/i;

export interface SafetyAssessment {
  riskLevel: RiskLevel;
  matchedKeywords: string[];
  requiresInterception: boolean;
  questionId?: SafetyQuestionId;
  responseText?: string;
  showEmergencyCard: boolean;
}

export class SafetyService {
  /**
   * Scans text for explicit crisis triggers
   */
  static detectKeywords(message: string): { hasCrisis: boolean; hasHarm: boolean; matches: string[] } {
    const text = message.toLowerCase();
    const matches: string[] = [];

    let hasCrisis = false;
    let hasHarm = false;

    for (const kw of SUICIDE_KEYWORDS) {
      if (text.includes(kw)) {
        matches.push(kw);
        hasCrisis = true;
      }
    }

    for (const kw of SELF_HARM_KEYWORDS) {
      if (text.includes(kw)) {
        matches.push(kw);
        hasHarm = true;
      }
    }

    for (const kw of SEVERE_DISTRESS_KEYWORDS) {
      if (text.includes(kw)) {
        matches.push(kw);
      }
    }

    return { hasCrisis, hasHarm, matches };
  }

  /**
   * Evaluates input, progression state machine, and pending safety questions.
   */
  static evaluateMessage(
    userMessage: string,
    pendingQuestionId?: SafetyQuestionId
  ): SafetyAssessment {
    const trimmed = userMessage.trim();

    // 1. If an assessment question was pending, interpret the user's answer
    if (pendingQuestionId) {
      return this.handleQuestionProgression(trimmed, pendingQuestionId);
    }

    // 2. Keyword detection on unsolicited user input
    const { hasCrisis, hasHarm, matches } = this.detectKeywords(trimmed);

    if (hasCrisis || hasHarm) {
      return {
        riskLevel: RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
        matchedKeywords: matches,
        requiresInterception: true,
        questionId: SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS,
        responseText:
          "I hear how much pain you are carrying right now, and I want you to know you are not alone in this sanctuary. Because your life and safety are precious, may I ask gently:\n\n" +
          SAFETY_QUESTIONS[SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS],
        showEmergencyCard: true
      };
    }

    return {
      riskLevel: RiskLevel.LEVEL_0_NORMAL,
      matchedKeywords: [],
      requiresInterception: false,
      showEmergencyCard: false
    };
  }

  /**
   * Deterministic Question Progression State Machine
   */
  private static handleQuestionProgression(
    userMessage: string,
    pendingQuestionId: SafetyQuestionId
  ): SafetyAssessment {
    const normalized = userMessage.toLowerCase().replace(/[.,!?;:"'’\-–—_/\\#@$%^&*()+=[\]{}<>|~`]+/g, ' ').replace(/\s+/g, ' ').trim();
    const isNo = NO_REGEX.test(userMessage) || NO_REGEX.test(normalized);
    const isYes = !isNo && (YES_REGEX.test(userMessage) || YES_REGEX.test(normalized));

    switch (pendingQuestionId) {
      case SafetyQuestionId.CURRENT_SUICIDAL_THOUGHTS:
        if (isYes) {
          return {
            riskLevel: RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
            matchedKeywords: ['answered_yes_ideation'],
            requiresInterception: true,
            questionId: SafetyQuestionId.IMMEDIATE_INTENT,
            responseText:
              "I hear you, and thank you for being completely truthful with me about how heavy things are. Your safety is what matters most right now.\n\n" +
              SAFETY_QUESTIONS[SafetyQuestionId.IMMEDIATE_INTENT],
            showEmergencyCard: true
          };
        } else if (isNo) {
          return {
            riskLevel: RiskLevel.LEVEL_1_DISTRESS,
            matchedKeywords: ['answered_no_ideation'],
            requiresInterception: true,
            responseText:
              "I am so deeply relieved to hear that. Even without thoughts of ending your life, the weight you are feeling is real and valid. Let's take a slow breath together. Would you like to unpack what brought on this heavy feeling?",
            showEmergencyCard: false
          };
        }
        break;

      case SafetyQuestionId.IMMEDIATE_INTENT:
        if (isYes) {
          return {
            riskLevel: RiskLevel.LEVEL_4_CRITICAL,
            matchedKeywords: ['answered_yes_intent'],
            requiresInterception: true,
            questionId: SafetyQuestionId.PLAN_AND_TIMEFRAME,
            responseText:
              "I appreciate your courage in telling me. Because you're having thoughts of acting, I want to make sure you have immediate human care around you.\n\n" +
              SAFETY_QUESTIONS[SafetyQuestionId.PLAN_AND_TIMEFRAME],
            showEmergencyCard: true
          };
        } else if (isNo) {
          return {
            riskLevel: RiskLevel.LEVEL_2_ELEVATED,
            matchedKeywords: ['answered_no_intent'],
            requiresInterception: true,
            questionId: SafetyQuestionId.HUMAN_SUPPORT,
            responseText:
              "It brings me relief that you don't intend to act on these thoughts right now. Having these thoughts is still exhausting, though.\n\n" +
              SAFETY_QUESTIONS[SafetyQuestionId.HUMAN_SUPPORT],
            showEmergencyCard: true
          };
        }
        break;

      case SafetyQuestionId.PLAN_AND_TIMEFRAME:
      case SafetyQuestionId.ACTION_ALREADY_TAKEN:
        return {
          riskLevel: RiskLevel.LEVEL_4_CRITICAL,
          matchedKeywords: ['critical_escalation'],
          requiresInterception: true,
          responseText:
            "Please stay right here with me. You do not have to navigate this darkness by yourself. There are compassionate people ready to listen and keep you safe right now.\n\n" +
            "In India, please call **112** for emergency assistance or **14416** (Tele-MANAS, 24/7 free). In the US/Canada, call or text **988**. Please reach out to them immediately—they want to be there for you.",
          showEmergencyCard: true
        };

      case SafetyQuestionId.HUMAN_SUPPORT:
        if (isYes) {
          return {
            riskLevel: RiskLevel.LEVEL_2_ELEVATED,
            matchedKeywords: ['trusted_person_available'],
            requiresInterception: true,
            responseText:
              "That is so wonderful to hear. Could you reach out to them right now and let them know you'd like company? Having someone physically near makes the heaviest burdens feel smaller.",
            showEmergencyCard: true
          };
        } else {
          return {
            riskLevel: RiskLevel.LEVEL_3_SUICIDAL_IDEATION,
            matchedKeywords: ['no_trusted_person'],
            requiresInterception: true,
            responseText:
              "Even if there is no one nearby right at this moment, trained counselors at Tele-MANAS (14416) or 988 are ready to be that presence for you 24/7, completely free and confidential. You deserve support today.",
            showEmergencyCard: true
          };
        }
    }

    // Default fallback when answer was ambiguous
    return {
      riskLevel: RiskLevel.LEVEL_2_ELEVATED,
      matchedKeywords: ['ambiguous_safety_response'],
      requiresInterception: true,
      responseText:
        "I want to make sure I am keeping you safe and holding you gently. Could you tell me a little more about whether you are safe right now, or if you'd like us to pause and do a grounding breath?",
      showEmergencyCard: true
    };
  }
}
