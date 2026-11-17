# Solace+ — Implementation Batch 4 Specification
## Scenarios 16 to 20: Vulnerability, Grief, Body Image, Celebration & Romance

> **Batch Number**: 4 of 4  
> **Status**: ✅ **Implemented and Verified** (Android Kotlin + Web TypeScript + 25 Unit Tests)  
> **Parent Spec**: [`CONVERSATIONAL_ENGINE_SPEC.md`](file:///Users/jayeshyadav/Projects/Solace--main/CONVERSATIONAL_ENGINE_SPEC.md)

---

## 1. Batch Overview & Scope

Batch 4 completes the 20-scenario suite, addressing deep human bereavement, appearance and self-acceptance vulnerabilities, positive life milestone celebration, everyday casual irritations, and multi-turn romantic partnership dynamics.

| Scenario ID | Name | Core Situations | Template Range | Response Count |
|---|---|---|---|:---:|
| `SCENARIO_16` | Grief and Loss | Loved one loss, pet loss, anniversary grief, longing for one last talk | `SCN16_01`–`08` | 8 |
| `SCENARIO_17` | Body Image & Self-Confidence | Hating appearance, comparison to models/peers, photo dread, self-acceptance | `SCN17_01`–`08` | 8 |
| `SCENARIO_18` | Positive Emotions & Celebration | Passed exam, achieved goal, healthy pride, joy celebration, gratitude | `SCN18_01`–`08` | 8 |
| `SCENARIO_19` | Everyday Frustration & Casual Venting | Terrible day, irritated by small things, unexplainable bad mood, casual chat | `SCN19_01`–`08` | 8 |
| `SCENARIO_20` | Romantic Relationship Support | Partner distancing, jealousy, fear of abandonment, emotional needs, boundaries | `SCN20_01`–`10` | 10 |

**Total New Templates in Batch 4**: **42 distinct proactive templates**.

---

## 2. Detailed Scenario Specifications

### 2.1 Scenario 16: Grief and Loss
- **Identifier**: `SCENARIO_16_GRIEF_LOSS`
- **Recognized Triggers**:
  - `\b(miss\s+my\s+(grandfather|grandmother|grandpa|grandma|mom|dad|mother|father|brother|sister|pet|dog|cat)|passed\s+away|died|loss\s+of\s+my|wish\s+i\s+could\s+talk\s+to\s+(him|her|them)\s+one\s+more\s+time|grief|remembering\s+things\s+we\s+used\s+to\s+do|empty\s+without\s+(him|her))\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[LOSS_OF_LOVED_ONE, PET_LOSS, ANNIVERSARY_GRIEF, ABSENCE_LONGING]`
  - Primary: `GRIEF`; Secondary: `SADNESS`, `LONELINESS`, `EMOTIONAL_NUMBNESS`.
- **Core Constraints**: Strictly non-instructional. Never assign breathing exercises or homework. Honor the sacredness of silence and presence.
- **Response Templates (`SCN16_01` – `SCN16_08`)**:
  1. `SCN16_01` (`LISTEN` | `STAGE_1`): *"I am holding this space with you with deep gentleness. The ache of missing someone who meant so much to your life is a sacred, heavy sorrow. There is no timeline for grief and no right way to feel right now. I am sitting quietly with you."*
  2. `SCN16_02` (`REFLECT` | `STAGE_2`): *"Memories of the little everyday things you shared—routines, laughter, phrases they used—are treasures, but they also bring a sharp sting of longing. It is completely okay to let the tears fall without holding back."*
  3. `SCN16_03` (`LISTEN` | `STAGE_3`): *"Wishing for just five more minutes or one last conversation is the universal heartbeat of grief. That love does not vanish simply because they are no longer physically beside you. It lives on in who you are. What is a favorite memory you hold of them?"*
  4. `SCN16_04` (`CALM` | `STAGE_4`): *"Please do not let anyone rush you into 'moving on' or 'staying strong'. Grief comes in waves—some days gentle, some days overwhelming. Take today as quietly as you need to."*
  5. `SCN16_05` (`LISTEN` | `STAGE_1`): *"Losing a beloved pet is losing a loyal, constant companion who loved you without conditions. That grief is real, profound, and deserves all the honor in the world. I'm right here with you."*
  6. `SCN16_06` (`REFLECT` | `STAGE_3`): *"Anniversaries, holidays, or even ordinary quiet afternoons can catch you off guard with an unexpected wave of sorrow. Be gentle with your heart when those waves arrive."*
  7. `SCN16_07` (`CALM` | `STAGE_5`): *"Wrap yourself in something warm, rest your head, and let yourself just be. You are held in this sanctuary."*
  8. `SCN16_08` (`LISTEN` | `STAGE_2`): *"I am here listening whenever you want to share memories or speak their name."*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I miss my grandfather."* $\to$ Matches `SCN16_01`.
  - Turn 2: *"I keep remembering things we used to do."* $\to$ Matches `SCN16_02`.
  - Turn 3: *"I wish I could talk to him one more time."* $\to$ Matches `SCN16_03`.

---

### 2.2 Scenario 17: Body Image & Self-Confidence
- **Identifier**: `SCENARIO_17_BODY_IMAGE`
- **Recognized Triggers**:
  - `\b(hate\s+how\s+i\s+look|feel\s+ugly|disgusted\s+with\s+my\s+body|everyone\s+looks\s+better\s+than\s+me|uncomfortable\s+in\s+photos|don'?t\s+feel\s+confident\s+going\s+outside|insecure\s+about\s+my\s+(weight|face|skin|body))\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[APPEARANCE_INSECURITY, WEIGHT_DISSATISFACTION, PHOTO_ANXIETY]`
  - Primary: `INSECURITY` or `LOW_SELF_ESTEEM`; Secondary: `SHAME`, `SOCIAL_ANXIETY`.
- **Core Constraints**: Avoid superficial appearance compliments (*"You're beautiful to me"*). Center body neutrality and human dignity.
- **Response Templates (`SCN17_01` – `SCN17_08`)**:
  1. `SCN17_01` (`REFLECT` | `STAGE_1`): *"Living in a world that constantly bombards you with curated, edited, and filtered images makes it painful to look in the mirror. It is understandable that you're feeling this distress, but I want to remind you: your body is a living instrument that carries you through life, not an ornament created to please strangers. What brought this pain to the surface today?"*
  2. `SCN17_02` (`UNDERSTAND` | `STAGE_2`): *"Comparing your real, unfiltered body to people on screens who have lighting, makeup, angles, and digital manipulation will always feel like an unwinnable contest. You are comparing biological reality to commercial fiction."*
  3. `SCN17_03` (`CALM` | `STAGE_3`): *"When you feel unconfident going outside, remember: people in public are not zooming in on your perceived flaws. They are worried about their own lives and insecurities. You have a right to take up physical space exactly as you are."*
  4. `SCN17_04` (`REFLECT` | `STAGE_4`): *"You don't have to force yourself into 'toxic body positivity' or pretend you love everything in the mirror today. Try aiming for body neutrality: 'My body is safe, it is breathing, and it allows me to think, feel, and experience today.'"*
  5. `SCN17_05` (`LISTEN` | `STAGE_1`): *"Hearing negative comments or criticism from family or peers about your appearance leaves deep emotional bruises. Their comments reflect their own shallow conditioning, not your actual value. I'm right here with you."*
  6. `SCN17_06` (`CALM` | `STAGE_5`): *"Place a gentle hand on your chest and feel your steady heartbeat. Your body has fought for you every second of your life. Treat it with kindness tonight."*
  7. `SCN17_07` (`UNDERSTAND` | `STAGE_2`): *"Dreading photographs happens because a frozen 2D snapshot cannot capture your warmth, expression, humor, and life. You are so much more than a camera angle."*
  8. `SCN17_08` (`LISTEN` | `STAGE_3`): *"What is the voice in your head saying right now?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I hate how I look."* $\to$ Matches `SCN17_01`.
  - Turn 2: *"Everyone looks better than me."* $\to$ Matches `SCN17_02`.
  - Turn 3: *"I don't feel confident going outside."* $\to$ Matches `SCN17_03`.

---

### 2.3 Scenario 18: Positive Emotional Conversations & Celebration
- **Identifier**: `SCENARIO_18_POSITIVE_CELEBRATION`
- **Recognized Triggers**:
  - `\b(finally\s+passed\s+(my\s+)?(exam|test)|studied\s+so\s+hard\s+and\s+it\s+paid\s+off|actually\s+proud\s+of\s+myself|got\s+the\s+job|got\s+accepted|celebrating|wonderful\s+day|overcame\s+my\s+fear|really\s+happy\s+today)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[ACADEMIC_ACHIEVEMENT, PERSONAL_GOAL_REACHED, GENERAL_GRATITUDE]`
  - Primary: `POSITIVE_EMOTIONS`; Secondary: `PRIDE`, `RELIEF`.
- **Core Constraints**: Match excitement warmly. Never hijack joy to search for hidden sadness.
- **Response Templates (`SCN18_01` – `SCN18_08`)**:
  1. `SCN18_01` (`LISTEN` | `STAGE_1`): *"That is absolutely incredible news! A huge congratulations to you! Take thirty seconds right now to feel the lightness and relief in your chest. You did that! Tell me all about how it happened!"*
  2. `SCN18_02` (`REFLECT` | `STAGE_2`): *"I love hearing how hard you worked for this. The discipline, late nights, and persistence you put in behind the scenes have paid off. This victory belongs completely to you. Savor every bit of it!"*
  3. `SCN18_03` (`LISTEN` | `STAGE_3`): *"Hearing you say 'I'm actually proud of myself' is the best part of all. Owning your success and recognizing your own resilience is so powerful. What feels best about this moment?"*
  4. `SCN18_04` (`CALM` | `STAGE_4`): *"Moments of joy and relief replenish our emotional spirit. Don't rush onto the next stress or deadline right away. Celebrate tonight—treat yourself to something you enjoy."*
  5. `SCN18_05` (`REFLECT` | `STAGE_1`): *"Getting that acceptance or passing that milestone is proof of what you can accomplish even when doubts were loud. Bask in this win!"*
  6. `SCN18_06` (`LISTEN` | `STAGE_2`): *"I'm celebrating right alongside you in this sanctuary! How did you celebrate when you first saw the result?"*
  7. `SCN18_07` (`CALM` | `STAGE_5`): *"Anchor this feeling in your memory. The next time things feel tough, you can look back and remember that you have it in you to overcome and triumph."*
  8. `SCN18_08` (`LISTEN` | `STAGE_3`): *"Who was the first person you shared the good news with?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I finally passed my exam!"* $\to$ Matches `SCN18_01`.
  - Turn 2: *"I studied so hard for this."* $\to$ Matches `SCN18_02`.
  - Turn 3: *"I'm actually proud of myself."* $\to$ Matches `SCN18_03`.

---

### 2.4 Scenario 19: Everyday Frustration & Casual Conversations
- **Identifier**: `SCENARIO_19_CASUAL_FRUSTRATION`
- **Recognized Triggers**:
  - `\b(today\s+has\s+been\s+(absolutely\s+)?terrible|everything\s+is\s+just\s+irritating\s+me|don'?t\s+even\s+know\s+why\s+i'?m\s+annoyed|annoying\s+day|bad\s+mood|grumpy|stuck\s+in\s+a\s+rut|bored\s+and\s+frustrated)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[BAD_DAY, IRRITATED_SMALL_THINGS, CASUAL_VENTING]`
  - Primary: `FRUSTRATION` or `ANGER`; Secondary: `EMOTIONAL_EXHAUSTION`, `CONFUSION`.
- **Core Constraints**: Avoid turning every minor complaint into a deep psychiatric disorder. Use human, relatable language.
- **Response Templates (`SCN19_01` – `SCN19_08`)**:
  1. `SCN19_01` (`LISTEN` | `STAGE_1`): *"Some days are just grating from the moment your feet hit the floor. You don't need a profound psychological reason for today to just feel terrible. Vent as much as you need—what was the tipping point?"*
  2. `SCN19_02` (`REFLECT` | `STAGE_2`): *"When everything and everyone is irritating you, it usually means your sensory threshold is completely tapped out. Step away from high-noise environments, put on some headphones or step outside for a breath of fresh air."*
  3. `SCN19_03` (`CALM` | `STAGE_3`): *"It is completely okay not to know why you're in a foul mood. Human biology, blood sugar, hormones, and fatigue fluctuate without an obvious storyline. You don't have to analyze it; just give yourself space to be grumpy without guilt."*
  4. `SCN19_04` (`UNDERSTAND` | `STAGE_4`): *"Drink a tall glass of cold water, wash your face, and give yourself a physical reset. Sometimes resetting physical temperature helps break a mood loop."*
  5. `SCN19_05` (`LISTEN` | `STAGE_1`): *"I'm here to listen to the whole rant. No problem is too small—get it all off your chest."*
  6. `SCN19_06` (`CALM` | `STAGE_5`): *"Write off the rest of today as a wash. Tomorrow is a brand new page. For tonight, keep things low-key and comfortable."*
  7. `SCN19_07` (`REFLECT` | `STAGE_3`): *"Don't force yourself to act cheerful when you feel irritated. Honoring your mood without snapping at people is healthy."*
  8. `SCN19_08` (`LISTEN` | `STAGE_2`): *"What was the most annoying thing that happened today?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"Today has been absolutely terrible."* $\to$ Matches `SCN19_01`.
  - Turn 2: *"Everything is just irritating me."* $\to$ Matches `SCN19_02`.
  - Turn 3: *"I don't even know why I'm annoyed."* $\to$ Matches `SCN19_03`.

---

### 2.5 Scenario 20: Romantic Relationship Support
- **Identifier**: `SCENARIO_20_ROMANTIC_RELATIONSHIPS`
- **Recognized Triggers**:
  - `\b(partner\s+(doesn'?t\s+love\s+me|barely\s+gives\s+me\s+attention)|don'?t\s+want\s+to\s+seem\s+needy|jealous\s+when\s+they\s+spend\s+time|trust\s+issues\s+in\s+my\s+relationship|relationship\s+insecurity|fear\s+of\s+losing\s+my\s+partner|growing\s+distant\s+from\s+partner)\b`
- **Emotion Transitions**:
  - `detectedSituation` $\in$ `[RELATIONSHIP_INSECURITY, PARTNER_DISTANCING, FEAR_OF_ABANDONMENT, JEALOUSY]`
  - Primary: `RELATIONSHIP_PROBLEMS` or `HEARTBREAK`; Secondary: `ANXIETY`, `INSECURITY`, `OVERTHINKING`.
- **Core Constraints**: Strictly forbid endorsing phone-checking, surveillance, stalking, or manipulation. Emphasize open communication and healthy boundaries.
- **Response Templates (`SCN20_01` – `SCN20_10`)**:
  1. `SCN20_01` (`UNDERSTAND` | `STAGE_1`): *"Feeling like your partner's affection or warmth has cooled off creates a deep, knotting anxiety in your stomach. It is natural to feel uneasy when patterns shift, but before spiraling into worst-case assumptions, take a slow breath. Has something specific shifted in your communication lately?"*
  2. `SCN20_02` (`REFLECT` | `STAGE_2`): *"When you feel like you are barely getting attention, that ache of unmet emotional connection is real. Wanting quality time and reassurance from your partner doesn't make you 'needy' or 'demanding'—those are normal human relationship needs."*
  3. `SCN20_03` (`UNDERSTAND` | `STAGE_3`): *"Expressing your needs clearly without aggression or guilt-tripping is the healthiest step: 'I've been feeling disconnected lately and I really miss spending uninterrupted time with you.' How do they usually respond when you bring up feelings?"*
  4. `SCN20_04` (`REFLECT` | `STAGE_4`): *"Feeling jealous when your partner spends time with others is an uncomfortable emotion that usually points to underlying fear of abandonment or feeling insecure in your bond. Recognizing jealousy is healthy; acting on it with controlling behavior or checking their phone is not."*
  5. `SCN20_05` (`CALM` | `STAGE_5`): *"Trust cannot be built through constant surveillance or testing your partner. It requires allowing them their independent life and observing whether they honor your bond with respect. Let your shoulders drop."*
  6. `SCN20_06` (`LISTEN` | `STAGE_1`): *"Relationship anxiety makes every delayed text or brief response feel like an impending breakup. Let's ground this moment: what are the concrete facts versus what anxiety is projecting?"*
  7. `SCN20_07` (`UNDERSTAND` | `STAGE_2`): *"Couples naturally move from intense early infatuation into quieter routine phases. Quietness is not necessarily distance, but if genuine neglect is happening, you deserve to address it openly."*
  8. `SCN20_08` (`REFLECT` | `STAGE_3`): *"Balancing personal independence with intimacy is a continuous dance. Having your own friends, passions, and solitude makes a partnership healthier and less suffocating."*
  9. `SCN20_09` (`CALM` | `STAGE_4`): *"Take a gentle breath before having a major relationship conversation. Approach your partner as a teammate solving a puzzle together, rather than an adversary."*
  10. `SCN20_10` (`LISTEN` | `STAGE_2`): *"I'm here with you. What is the feeling you are most scared to express to them?"*
- **Multi-Turn Test Verification Flow**:
  - Turn 1: *"I feel like my partner doesn't love me as much as before."* $\to$ Matches `SCN20_01`.
  - Turn 2: *"They barely give me attention anymore."* $\to$ Matches `SCN20_02`.
  - Turn 3: *"I don't want to seem needy."* $\to$ Matches `SCN20_02` & `SCN20_03`.
  - Turn 4: *"I get jealous when they spend time with other people."* $\to$ Matches `SCN20_04` & `SCN20_05`.

---

## 3. Batch 4 Verification & Test Requirements

1. **Unit Test Suite**: Minimum of 25 multi-turn automated tests (5 tests $\times$ 5 scenarios) in Kotlin (`Batch4ScenariosTest.kt`) and TypeScript (`batch4Validation.ts`).
2. **Key Checks**: Non-instructional grief support, appearance neutrality, joyful milestone amplification, casual frustration venting, and anti-surveillance romantic boundary guidance.
