package com.example.data.safety

import com.example.data.model.RegulationMode

/**
 * Human-Authored Compassionate Emotional Support Response Library.
 *
 * Contains 105+ distinct, non-repetitive response templates crafted with
 * psychological empathy principles (validation, reflection, somatic grounding,
 * direct supportive guidance, and gentle continuation).
 *
 * Refined to eliminate permission-based questions ("Would you like me to...",
 * "Do you want to try...", "Can I suggest...") in favor of proactive,
 * contextually grounded emotional support and compassionate presence.
 *
 * Explicitly free of toxic positivity, invalidating tropes, or forced cheerfulness.
 */
object EmotionalResponseLibrary {

    data class ResponseTemplate(
        val id: String,
        val category: EmotionalClassificationEngine.EmotionCategory,
        val text: String,
        val preferredMode: RegulationMode = RegulationMode.LISTEN,
        val suggestedGrounding: Boolean = false,
        val situation: UserSituation? = null,
        val stage: ConversationalStage? = null,
        val scenarioId: ScenarioId? = null
    )

    val templates: List<ResponseTemplate> = listOf(
        // =====================================================================
        // 1. ANXIETY (8 responses) - Slow breathing, grounding, reducing pressure
        // =====================================================================
        ResponseTemplate(
            id = "ANX_01",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "That sounds exhausting. Sometimes anxiety makes even ordinary moments feel overwhelming. Take a moment to slow down and breathe. You don't have to figure everything out immediately. Is something specific weighing on you today?",
            preferredMode = RegulationMode.CALM,
            suggestedGrounding = true,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE
        ),
        ResponseTemplate(
            id = "ANX_02",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "I hear how fast everything is spinning inside you right now. When anxiety spikes, our nervous system floods with false urgency. Take a quiet breath and let your weight settle into the chair beneath you. You don't have to figure out tomorrow in this exact moment.",
            preferredMode = RegulationMode.CALM,
            suggestedGrounding = true
        ),
        ResponseTemplate(
            id = "ANX_03",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "It sounds like your mind has been carrying a tremendous load. Plant both feet flat against the ground and take one long, deliberate exhale. Has this been building up for a while, or did something specific trigger it today?",
            preferredMode = RegulationMode.UNDERSTAND,
            suggestedGrounding = true
        ),
        ResponseTemplate(
            id = "ANX_04",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Anxiety often makes uncertainty feel much bigger and more dangerous than it actually is. Try narrowing your focus to just your physical surroundings for thirty seconds. What is the one thing demanding your attention the most right now?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "ANX_05",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "You don't need to force yourself to feel calm instantly. Place a gentle hand on your chest, feel your natural breathing rhythm, and give yourself permission to do nothing else for the next two minutes. We can take this one step at a time.",
            preferredMode = RegulationMode.CALM,
            suggestedGrounding = true
        ),
        ResponseTemplate(
            id = "ANX_06",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Anxiety tightens the chest and pushes your thoughts far ahead into the future. Right here, you are safe. Let your shoulders drop an inch, unclench your teeth, and let your next breath sigh out slowly.",
            preferredMode = RegulationMode.CALM,
            suggestedGrounding = true
        ),
        ResponseTemplate(
            id = "ANX_07",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Living with this constant buzz of nervousness takes a heavy physical toll. Don't fight the sensation or criticize yourself for feeling on edge. What does the tension feel like in your body right now?",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "ANX_08",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "When anxiety floods in, it tries to convince us that every question needs an immediate answer. But right in this quiet second, nothing is due and nobody is testing you. Settle into this pause.",
            preferredMode = RegulationMode.REFLECT
        ),

        // =====================================================================
        // 2. SADNESS (8 responses) - Self-compassion, gentle comfort, holding space
        // =====================================================================
        ResponseTemplate(
            id = "SAD_01",
            category = EmotionalClassificationEngine.EmotionCategory.SADNESS,
            text = "I'm sorry today has been difficult. You don't have to force yourself to be productive or pretend to be happy. Give yourself a little room to feel what you're feeling. Try doing one small comforting thing for yourself, even if it's just getting some water, sitting somewhere comfortable or taking a short walk.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "SAD_02",
            category = EmotionalClassificationEngine.EmotionCategory.SADNESS,
            text = "Sadness can feel like a heavy physical weight resting in the center of your chest. Don't rush to shake it off. Has this feeling been building up quietly, or did a specific moment bring it to the surface?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "SAD_03",
            category = EmotionalClassificationEngine.EmotionCategory.SADNESS,
            text = "Crying and feeling down are natural ways our heart processes pain. Please don't judge yourself for hurting or expect yourself to bounce back immediately. Take things gently and allow yourself some warmth today.",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "SAD_04",
            category = EmotionalClassificationEngine.EmotionCategory.SADNESS,
            text = "It is completely okay to feel sad. You don't have to force a smile or look on any bright side. Wrap yourself in something comfortable and allow this moment to be quiet. I'm right here with you.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "SAD_05",
            category = EmotionalClassificationEngine.EmotionCategory.SADNESS,
            text = "When sadness takes over, even small everyday tasks feel exhausting. Lower your expectations for yourself for the rest of today. What has been weighing on you the heaviest?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "SAD_06",
            category = EmotionalClassificationEngine.EmotionCategory.SADNESS,
            text = "I hear the quiet ache in your words. Your pain makes sense, and you don't have to carry it in silence or apologize for it. Take all the time you need to let it out.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "SAD_07",
            category = EmotionalClassificationEngine.EmotionCategory.SADNESS,
            text = "Sadness often shows up when something we care about deeply has been wounded or lost. Instead of pushing it away, give your mind permission to acknowledge that care. What part of this situation hurts the most?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "SAD_08",
            category = EmotionalClassificationEngine.EmotionCategory.SADNESS,
            text = "Take a slow, comforting breath. There is no deadline to fix this mood or move past it. Right now, being gentle with yourself is the only thing that matters.",
            preferredMode = RegulationMode.CALM
        ),

        // =====================================================================
        // 3. LONELINESS (7 responses) - Meaningful connection & validation
        // =====================================================================
        ResponseTemplate(
            id = "LON_01",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "That can be a really painful feeling, especially when you feel disconnected from everyone around you. Please don't isolate yourself completely with these thoughts. Consider reaching out to someone you trust, even with a simple message. Having someone alongside you can make this moment feel a little less lonely.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "LON_02",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "Feeling unnoticed by the people around you cuts deeply. It's easy for the mind to assume that nobody cares when silence stretches on. Has something happened recently that made you feel especially distant from others?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "LON_03",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "That sounds incredibly isolating. When loneliness settles in, it tries to convince us that we are permanently on the outside looking in. Do you feel that people have been drifting away, or is it more that you don't feel understood even when they're around?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "LON_04",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "Loneliness is one of the heaviest weights anyone can carry. Even in this digital sanctuary, your presence is real and your voice matters. Let's stay here together—you don't have to face the quiet alone.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "LON_05",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "It hurts when you crave authentic connection and it feels out of reach. Remind yourself that feeling lonely right now does not mean you are unworthy of belonging. What kind of understanding do you feel you've been missing most?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "LON_06",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "Being in a crowded room or scrolling through updates while feeling utterly solitary is uniquely painful. Step away from social feeds for a bit and do something tangible that brings comfort to your senses.",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "LON_07",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "I hear how quiet and isolated things feel for you right now. Take a deep, gentle breath and know that you are not invisible here. Take your time sharing whatever is in your heart.",
            preferredMode = RegulationMode.CALM
        ),

        // =====================================================================
        // 4. OVERTHINKING (7 responses) - Sensory grounding & separating facts
        // =====================================================================
        ResponseTemplate(
            id = "OVT_01",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "Sometimes the mind keeps replaying situations because it's trying to find an answer or regain control. Try stepping away from those thoughts for a moment. Focus on five things you can see around you, four things you can physically feel and three things you can hear. Bring your attention back to where you are right now.",
            preferredMode = RegulationMode.CALM,
            suggestedGrounding = true
        ),
        ResponseTemplate(
            id = "OVT_02",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "When your thoughts are looping at high speed, analyzing every single angle only adds fuel to the fire. Close your eyes for ten seconds and take one slow, long exhale. What is the central thought spinning fastest right now?",
            preferredMode = RegulationMode.UNDERSTAND,
            suggestedGrounding = true
        ),
        ResponseTemplate(
            id = "OVT_03",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "You don't have to resolve every 'what-if' scenario tonight. Let's draw a line between what you can actively influence right now and what is purely an anxious prediction. Focus only on the step right in front of you.",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "OVT_04",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "It sounds like there are twenty open tabs running in your mind all at once. Picture closing all but one of them for the next hour. Which single concern actually needs your attention today?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "OVT_05",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "Replaying past conversations or trying to predict future reactions drains immense energy. Stand up, stretch your arms out, and shake the tension out of your hands. Give your body a physical break from the mental spiral.",
            preferredMode = RegulationMode.CALM,
            suggestedGrounding = true
        ),
        ResponseTemplate(
            id = "OVT_06",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "Thinking harder doesn't bring peace; it usually just brings exhaustion. Recognize that you are having anxious thoughts, but you don't have to treat every thought as an undeniable truth. What started this spiral earlier today?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "OVT_07",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "Your mind is working overtime trying to protect you, but you don't have to follow every alarming thought it generates. Step back into the seat of the observer. Notice the thoughts passing without jumping onto them.",
            preferredMode = RegulationMode.REFLECT
        ),

        // =====================================================================
        // 5. ACADEMIC STRESS (7 responses) - Manageable micro-steps
        // =====================================================================
        ResponseTemplate(
            id = "ACAD_01",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "I can understand why that's weighing on you. But worrying about the entire result right now will only make things feel heavier. Take a moment to settle yourself, then choose one manageable topic to work on. You don't need to finish everything at once. Start with something small and build from there.",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "ACAD_02",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "It sounds like exam expectations are creating unbearable pressure right now. Struggling with preparation or feeling behind doesn't mean you have already failed. Is it the volume of the syllabus, the timeline, or the fear of the outcome that's pressing on you most?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "ACAD_03",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "You've got a lot on your mind right now. Let's not turn the entire semester into one frightening catastrophe. Close the books for fifteen minutes, drink a tall glass of water, and let your nervous system catch its breath.",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "ACAD_04",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "Academic stress can make you feel as though your entire value as a person depends on test scores. Your intelligence, dignity, and worth are so much bigger than any letter on a paper. Let's look at what's realistically doable today.",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "ACAD_05",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "When study burnout sets in, trying to force hours of unfocused reading only deepens panic. Step away from your desk right now. Take five minutes to walk around and stretch before deciding on your next single task.",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "ACAD_06",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "Panic makes every chapter look like an insurmountable cliff. Break this down: commit to studying just one page or solving one problem. Once you take that first tiny step, the paralysis will begin to lift.",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "ACAD_07",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "I hear how mentally drained you are from constantly memorizing and worrying. You don't need to carry the whole semester on your shoulders tonight. Which subject is causing the sharpest stress?",
            preferredMode = RegulationMode.UNDERSTAND
        ),

        // =====================================================================
        // 6. RELATIONSHIP DISTRESS & HEARTBREAK (8 responses) - Reflect before reacting
        // =====================================================================
        ResponseTemplate(
            id = "REL_01",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "I can understand why the silence or tension might be making you overthink. But try not to assume the worst before you know what's happening. Put your phone aside for a few minutes, take space from checking notifications, and focus on something that helps you settle. You can think about the situation more clearly once the initial anxiety has eased.",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "REL_02",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Heartbreak is a genuine emotional shock. It takes time for the body and heart to adjust to that kind of sudden distance. Be gentle with yourself today, and don't expect yourself to be 'over it' quickly.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "REL_03",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Feeling dismissed or unheard by someone you care about cuts deep. Before responding out of hurt or anger, give yourself a pause. What is the core feeling underneath this conflict—is it sadness, fear, or frustration?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "REL_04",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "When trust or communication falters, it's easy to second-guess your own instincts. Give yourself time to process what was said rather than scrambling to fix it immediately. What part of the situation feels most unresolved?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "REL_05",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Ending a connection leaves an empty space in daily routines that feels jarring and cold. Allow yourself to feel the grief of that absence without rushing to fill it. Has someone close to you been able to listen and support you?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "REL_06",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Walking on eggshells in a relationship is deeply exhausting. You deserve emotional clarity and safety. Take a long breath and step back from the conflict for a few hours to protect your own peace.",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "REL_07",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Loving someone doesn't mean you have to absorb harsh treatment or neglect your own boundaries. I am here listening to your side of the story without any judgment. Tell me what happened.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "REL_08",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Relationship strain drains your emotional reserves faster than almost anything else. Settle into this quiet space. What is the one thing you most wish they could understand about how you feel?",
            preferredMode = RegulationMode.REFLECT
        ),

        // =====================================================================
        // 7. ANGER AND FRUSTRATION (6 responses) - Taking space, pausing
        // =====================================================================
        ResponseTemplate(
            id = "ANG_01",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "It sounds like something has really frustrated you. Before reacting, give yourself a little distance from the situation. Relax your hands, take a few comfortable breaths and allow the intensity to settle. You don't have to respond to everything immediately.",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "ANG_02",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "It is completely valid to feel furious when an unfair boundary was crossed or when your effort was disregarded. Release that heat here—vent everything that happened. I am listening without judgment.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "ANG_03",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "I hear the fire and frustration in what you're saying. You don't have to pretend you're fine or bottle this up. Speak freely and let the steam out.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "ANG_04",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "Behind intense anger, there is almost always deep hurt, betrayal, or disappointment that went unacknowledged. Take a slow breath and see if that resonates. What felt most unjust about what happened?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "ANG_05",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "When frustration surges, muscles tighten, the jaw clenches, and adrenaline rushes through the veins. Roll your neck gently and shake out your wrists. Let's give your body a moment to decompress.",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "ANG_06",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "That does sound exasperating. Anyone in your position would feel heated facing that. Take a brief pause before deciding on your next move. What part of it pushed you past your limit?",
            preferredMode = RegulationMode.UNDERSTAND
        ),

        // =====================================================================
        // 8. LOW SELF-ESTEEM & INSECURITY (6 responses) - Challenging inner critic
        // =====================================================================
        ResponseTemplate(
            id = "LSE_01",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Hearing that cruel inner critic tell you you're a failure is agonizing, but a harsh thought is not an objective fact. Pause and ask yourself: would you ever speak to someone you love with that level of cruelty?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "LSE_02",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "You are holding yourself to an impossible standard that no human could survive without feeling inadequate. Give yourself the same mercy you would naturally extend to a hurting friend. What made you feel so disappointed in yourself today?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "LSE_03",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Making a mistake or stumbling through a difficult phase does not make you broken. It just means you are human and learning under pressure. Let's look at what happened with kindness instead of punishment.",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "LSE_04",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Comparing your private struggles to everyone else's curated surface will always leave you feeling deficient. Step back from comparisons right now. You don't have to prove your worth to deserve respect and peace.",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "LSE_05",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "You are worthy of kindness right now, exactly as you are, without having to achieve or earn it first. What has been making you doubt yourself so intensely lately?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "LSE_06",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "I am holding a mirror of compassion for you, because the reflection your inner critic is giving you right now is severely distorted. Take a slow breath and let down that harsh shield for a moment.",
            preferredMode = RegulationMode.LISTEN
        ),

        // =====================================================================
        // 9. BURNOUT & EMOTIONAL EXHAUSTION (6 responses) - Reducing demands & rest
        // =====================================================================
        ResponseTemplate(
            id = "BUR_01",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "It sounds like you've been mentally exhausted. Rather than forcing yourself to accomplish everything today, reduce the pressure a little. Step away from your work for a while, get some water and allow yourself to rest. Taking a break doesn't mean you've failed.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "BUR_02",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "Feeling depleted down to your marrow is your body's clear signal that you've been running on empty for too long. Put aside the to-do list for this evening. Nothing in this sanctuary needs to be produced or earned.",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "BUR_03",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "Burnout is not a failure of willpower; it is the natural consequence of giving more energy than you have been receiving. Allow yourself an unhurried pause. How is your physical body feeling right now?",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "BUR_04",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "When exhaustion reaches this level, even the simplest decision feels like lifting lead. Don't fight the tiredness. Give yourself full permission to sleep, rest, or simply sit quietly without guilt.",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "BUR_05",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "It can be unsettling when the enthusiasm and spark you used to have feels completely extinguished. Rest assured it isn't gone permanently—it is simply buried under fatigue. What is the single biggest demand draining you?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "BUR_06",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "Acknowledge how much you've had to hold together just to get through today. Unclench your jaw, drop your shoulders away from your ears, and take one deep, restorative breath. You're allowed to stop pushing.",
            preferredMode = RegulationMode.CALM,
            suggestedGrounding = true
        ),

        // =====================================================================
        // 10. FEAR AND UNCERTAINTY (5 responses) - Controllable next steps
        // =====================================================================
        ResponseTemplate(
            id = "FEA_01",
            category = EmotionalClassificationEngine.EmotionCategory.FEAR,
            text = "Uncertainty can make the unknown feel like a dark room where every shadow is dangerous. Separate the story your fear is telling you from the facts of what is happening right now. What specific outcome are you dreading most?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "FEA_02",
            category = EmotionalClassificationEngine.EmotionCategory.FEAR,
            text = "Fear is uncomfortable, but it's fundamentally your mind trying to keep something you care about safe. Acknowledge that protection, but don't let it hijack the present hour. What is one small thing you can control today?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "FEA_03",
            category = EmotionalClassificationEngine.EmotionCategory.FEAR,
            text = "You don't need to see the entire staircase right now; you only need to take the next step right before you. Let's slow down the rush of predictions and stay grounded in this moment.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "FEA_04",
            category = EmotionalClassificationEngine.EmotionCategory.FEAR,
            text = "When fear grips the chest, breathing turns shallow and rapid. Take one deep, steady breath in through your nose, hold it for two seconds, and exhale slowly through your mouth. You are safe in this exact room right now.",
            preferredMode = RegulationMode.CALM,
            suggestedGrounding = true
        ),
        ResponseTemplate(
            id = "FEA_05",
            category = EmotionalClassificationEngine.EmotionCategory.FEAR,
            text = "It takes vulnerability to admit when you're scared instead of putting on a brave front. I hear you, and your fear makes complete sense given what you're facing. What part feels most unpredictable?",
            preferredMode = RegulationMode.UNDERSTAND
        ),

        // =====================================================================
        // 11. GUILT AND SHAME (5 responses) - Separating self-worth from mistakes
        // =====================================================================
        ResponseTemplate(
            id = "GLT_01",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "Carrying heavy regret can eat away at you from the inside. Remember: guilt says 'I made an error,' but shame tries to convince you 'I am inherently bad.' You are not bad. What happened that is weighing on your conscience?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "GLT_02",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "Be gentle with past versions of yourself who were acting with less information, less perspective, or under immense emotional strain. Forgiving yourself is essential to moving forward. Has this been hurting you for a long time?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "GLT_03",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "We often blame ourselves for outcomes that were shaped by complex circumstances beyond any single person's control. Separate your actual responsibility from the burden of things you could never have prevented.",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "GLT_04",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "Hiding in shame only amplifies its weight in the dark. This is a completely safe, judgment-free sanctuary. You can let the burden down here without being criticized.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "GLT_05",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "Punishing yourself over and over does not heal the past; it only drains your ability to live in the present. Take a comforting breath and offer yourself one small moment of forgiveness right now.",
            preferredMode = RegulationMode.CALM
        ),

        // =====================================================================
        // 12. GRIEF (4 responses) - Direct empathetic presence, NO forced tasks
        // =====================================================================
        ResponseTemplate(
            id = "GRF_01",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "I'm really sorry for your loss. Losing someone important can leave a space that's difficult to put into words. You don't need to rush yourself through the grief or pretend you're okay. Take things at your own pace, and try not to carry everything alone.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "GRF_02",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "Grief doesn't follow a neat timeline or clean stages; it comes in sudden, unpredictable waves. Give yourself full permission to cry, be quiet, or feel whatever arises. I am sitting right here with you through this.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "GRF_03",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "There are no words that take the sting of a profound absence away, and I won't offer neat platitudes. The depth of the pain is a reflection of how deeply you loved. Tell me about them whenever you feel ready.",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "GRF_04",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "Please treat yourself with utmost tenderness right now. Make sure you drink some water and rest your body when you feel weary. I'm right here beside you in the quiet.",
            preferredMode = RegulationMode.CALM
        ),

        // =====================================================================
        // 13. SLEEP DIFFICULTIES (4 responses) - Removing sleep pressure
        // =====================================================================
        ResponseTemplate(
            id = "SLP_01",
            category = EmotionalClassificationEngine.EmotionCategory.SLEEP_DIFFICULTIES,
            text = "Lying awake when the world is quiet can feel uniquely lonely. The more you pressure yourself to fall asleep, the more awake your mind becomes. Take off that pressure—simply resting your body with your eyes closed still gives you restore.",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "SLP_02",
            category = EmotionalClassificationEngine.EmotionCategory.SLEEP_DIFFICULTIES,
            text = "Late-night insomnia is often the mind's way of processing thoughts that were silenced during the noise of the day. Put your phone away, turn off bright screens, and let your thoughts drift without trying to resolve them tonight.",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "SLP_03",
            category = EmotionalClassificationEngine.EmotionCategory.SLEEP_DIFFICULTIES,
            text = "Focus on the physical sensation of your head sinking gently into the pillow. Smooth the muscles in your forehead, let your eyelids soften, and take slow, unhurried breaths. What thought keeps recurring tonight?",
            preferredMode = RegulationMode.CALM,
            suggestedGrounding = true
        ),
        ResponseTemplate(
            id = "SLP_04",
            category = EmotionalClassificationEngine.EmotionCategory.SLEEP_DIFFICULTIES,
            text = "I'm right here with you in the stillness of the night. You don't have to fight your mind alone. Let the words out here, and let your body settle into the quiet.",
            preferredMode = RegulationMode.LISTEN
        ),

        // =====================================================================
        // 14. EMOTIONAL NUMBNESS (4 responses) - Gentle sensory reconnection
        // =====================================================================
        ResponseTemplate(
            id = "NUM_01",
            category = EmotionalClassificationEngine.EmotionCategory.EMOTIONAL_NUMBNESS,
            text = "Feeling numb or detached can feel unsettling, but numbness is often your nervous system's built-in circuit breaker when feelings became too heavy to bear. Don't force yourself to feel something right now. It's okay to just exist.",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "NUM_02",
            category = EmotionalClassificationEngine.EmotionCategory.EMOTIONAL_NUMBNESS,
            text = "You don't need to manufacture emotion when your battery is completely drained. Notice the physical sensation of whatever you are touching right now—the fabric of your clothes, the temperature of the air. Let sensory reality anchor you.",
            preferredMode = RegulationMode.CALM,
            suggestedGrounding = true
        ),
        ResponseTemplate(
            id = "NUM_03",
            category = EmotionalClassificationEngine.EmotionCategory.EMOTIONAL_NUMBNESS,
            text = "Feeling like a ghost observing your life from behind glass is a sign of intense overwhelm. Take things very slowly today. Has this detachment been present for days, or did it arrive suddenly?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "NUM_04",
            category = EmotionalClassificationEngine.EmotionCategory.EMOTIONAL_NUMBNESS,
            text = "Even when your internal world feels hollow or static, you are still here, and you are safe in this sanctuary. I am right beside you without expecting you to be anything other than you are right now.",
            preferredMode = RegulationMode.LISTEN
        ),

        // =====================================================================
        // 15. SOCIAL ANXIETY (4 responses) - Releasing the spotlight effect
        // =====================================================================
        ResponseTemplate(
            id = "SOC_01",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "Social anxiety makes interactions feel like standing under an intense interrogation light where every word is being graded. Keep in mind that people are almost always preoccupied with their own insecurities, not watching you. Take a steady breath.",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "SOC_02",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "It is completely fine to step outside, take a quiet break in the restroom, or leave an event early to protect your social battery. Honor your limits today. How are you holding up right now?",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "SOC_03",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "Post-event rumination—where you replay everything you said and assume you embarrassed yourself—is anxiety lying to you. Remind yourself: nobody is analyzing you the way your inner critic is. What moment are you replaying?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "SOC_04",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "Entering a social setting doesn't require you to be charismatic or entertaining; simply showing up quietly is enough. Let your shoulders drop and focus on one friendly face or one quiet corner.",
            preferredMode = RegulationMode.CALM
        ),

        // =====================================================================
        // 16. FAMILY CONFLICT (4 responses) - Boundaries & decompressing
        // =====================================================================
        ResponseTemplate(
            id = "FAM_01",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "Family conflict hits differently because these are the people whose approval and peace we naturally yearn for. When arguments break out, the emotional sting lingers for hours. What happened at home today?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "FAM_02",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "It is painful when the place that should feel like a haven feels tense and hostile. Remember that you are allowed to protect your emotional boundaries and step away from shouting. How are you holding up?",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "FAM_03",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "When parents or relatives project their own anxieties and expectations onto your life, it can feel like suffocation. Your own identity, feelings, and path are valid. Take space to breathe away from the pressure.",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "FAM_04",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "Heated exchanges at home flood the nervous system with adrenaline. Settle into this quiet sanctuary away from the conflict. Drink some cold water, take a few unhurried breaths, and let the storm settle.",
            preferredMode = RegulationMode.CALM
        ),

        // =====================================================================
        // 17. MOTIVATION & PROCRASTINATION (4 responses) - Micro-steps
        // =====================================================================
        ResponseTemplate(
            id = "MOT_01",
            category = EmotionalClassificationEngine.EmotionCategory.LACK_OF_MOTIVATION,
            text = "Procrastination is almost never about laziness; it is an emotional regulation response to overwhelm, boredom, or fear of failing. Let's remove the mountain: pick just two minutes of the easiest part of the task and do only that.",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "MOT_02",
            category = EmotionalClassificationEngine.EmotionCategory.LACK_OF_MOTIVATION,
            text = "Waiting until you 'feel like doing it' rarely works when your motivation has hit zero. Action actually creates motivation, not the other way around. Shrink the task down to one micro-step. What is the very first step?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "MOT_03",
            category = EmotionalClassificationEngine.EmotionCategory.LACK_OF_MOTIVATION,
            text = "Beating yourself up for not being productive only drains the tiny sliver of energy you have left. Drop the self-criticism. Allow yourself to start poorly—a messy draft is infinitely better than an empty page.",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "MOT_04",
            category = EmotionalClassificationEngine.EmotionCategory.LACK_OF_MOTIVATION,
            text = "A persistent lack of motivation is often burnout in disguise. If you've been pushing without genuine restorative rest, your mind is on strike. Take a real 20-minute break with zero work guilt before trying again.",
            preferredMode = RegulationMode.UNDERSTAND
        ),

        // =====================================================================
        // 18. POSITIVE EMOTIONS (4 responses) - Savoring & emotional anchoring
        // =====================================================================
        ResponseTemplate(
            id = "POS_01",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "I'm so glad to hear that! It's such a welcome relief when lightness and ease arrive. Take thirty seconds right now to notice how your chest and shoulders feel when things are going well. What brought this goodness to your day?",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "POS_02",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "Celebrate this lightness—you deserve every bit of this peace and joy. Savor the feeling instead of rushing to the next task. What is feeling best for you right now?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "POS_03",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "Hearing that things are going well brings genuine warmth to this sanctuary. Take a calm breath and let that goodness settle in as an emotional anchor. Tell me more about what went right!",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "POS_04",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "Moments of ease and happiness give our nervous system a chance to recharge and heal. Enjoy this moment fully. What was the highlight of your day?",
            preferredMode = RegulationMode.CALM
        ),

        // =====================================================================
        // 19. CONFUSION (4 responses) - Sifting without forced answers
        // =====================================================================
        ResponseTemplate(
            id = "CNF_01",
            category = EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            text = "It is completely normal to feel confused when your head and your gut are pulling you in opposite directions. Don't force an immediate conclusion. What are the two conflicting sides of the puzzle?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "CNF_02",
            category = EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            text = "Confusion usually means something complex and nuanced is being processed beneath the surface. Allow the dust to settle before making any major moves. Has this dilemma been bothering you for a while?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "CNF_03",
            category = EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            text = "Sometimes the hardest part is letting go of what others expect so you can hear what you actually need. Take a quiet breath. If there were zero judgment from anyone, what would you choose?",
            preferredMode = RegulationMode.UNDERSTAND
        ),
        ResponseTemplate(
            id = "CNF_04",
            category = EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            text = "When everything feels foggy, pushing harder only creates frustration. Step back from the decision for the rest of today and let your mind rest. Clarity usually arrives when the pressure is released.",
            preferredMode = RegulationMode.CALM
        ),

        // =====================================================================
        // 20. OTHER EMOTIONAL SITUATIONS (6 responses) - Grounding space
        // =====================================================================
        ResponseTemplate(
            id = "OTH_01",
            category = EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            text = "I am right here with you in your sanctuary. Take all the time you need to settle in. What has been occupying your mind lately?",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "OTH_02",
            category = EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            text = "Whatever you are carrying today, you don't have to carry it alone. I am here listening with warmth and patience. Feel free to speak freely.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "OTH_03",
            category = EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            text = "Thank you for reaching out and sharing this space. Take a slow, grounding breath and let your shoulders drop. What part of your day has felt the most demanding?",
            preferredMode = RegulationMode.REFLECT
        ),
        ResponseTemplate(
            id = "OTH_04",
            category = EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            text = "Even if it feels hard to find the right words, don't worry about speaking perfectly. Take your time, and share whatever feels comfortable.",
            preferredMode = RegulationMode.LISTEN
        ),
        ResponseTemplate(
            id = "OTH_05",
            category = EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            text = "Our thoughts and energy shift throughout the day. Take a pause right now to check in with your physical body. How are you feeling right in this moment?",
            preferredMode = RegulationMode.CALM
        ),
        ResponseTemplate(
            id = "OTH_06",
            category = EmotionalClassificationEngine.EmotionCategory.CONFUSION,
            text = "Your thoughts and feelings are welcome here. Whether you need to vent, think something through, or simply sit in silence, I am beside you.",
            preferredMode = RegulationMode.LISTEN
        ),

        // =====================================================================
        // 21. SITUATIONAL CONTEXTUAL TEMPLATES (Breakup, Missing Ex, Social Media, etc.)
        // =====================================================================
        ResponseTemplate(
            id = "SIT_BREAKUP_01",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "I'm really sorry you're going through that. Breakups can be incredibly difficult, especially when someone has been an important part of your everyday life. It's completely understandable that you're feeling anxious right now. Try not to put pressure on yourself to get over everything immediately. Give yourself some space, avoid repeatedly checking old conversations if they're making things harder, and spend a little time around people you feel comfortable with. You don't have to process everything in one day.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.BREAKUP,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY
        ),
        ResponseTemplate(
            id = "SIT_BREAKUP_02",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Going through a breakup is an emotional shock, especially when someone was such an integral part of your day-to-day life. It makes complete sense that you're feeling anxious and overwhelmed. Give yourself permission to pause and breathe. You don't have to figure out the future all at once. Take this one day at a time.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.BREAKUP,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT
        ),
        ResponseTemplate(
            id = "SIT_BREAKUP_03",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "I hear how painful this breakup is for you. Losing a connection leaves a real void, and it's completely natural that anxiety is surfacing. Be gentle with your heart today, step away from old chats if they hurt, and allow yourself to rest without guilt.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.BREAKUP,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT
        ),
        ResponseTemplate(
            id = "SIT_MISSING_EX_01",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "That makes sense. When someone has been part of your routine, their absence can make even ordinary moments feel different. You might find yourself wanting to message her or revisit old memories. Try not to judge yourself for missing her. Missing someone doesn't necessarily mean you need to go back to the relationship. Take things one day at a time.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.MISSING_EX,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION
        ),
        ResponseTemplate(
            id = "SIT_MISSING_EX_02",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "It is completely normal to miss them. When someone was part of your daily rhythm, their absence leaves an echo in ordinary moments. Remind yourself that missing someone is a natural part of grieving what was lost. You don't have to rush through this feeling or judge yourself for having it.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.MISSING_EX,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION
        ),
        ResponseTemplate(
            id = "SIT_EX_SOCIAL_MEDIA_01",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "I can understand why you're doing that. Part of you might be looking for reassurance or trying to understand how she's doing. But repeatedly checking her profile can reopen the same emotional wound. Consider muting her updates for a while and giving yourself some distance from the things that keep bringing the breakup back into your mind.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.EX_SOCIAL_MEDIA_CHECKING,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH
        ),
        ResponseTemplate(
            id = "SIT_EX_SOCIAL_MEDIA_02",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "It is such an understandable urge to check their profile when you're looking for answers or feeling disconnected. But checking their social media almost always re-injures you and delays your healing. Give yourself a break from looking at their accounts, and give your mind the quiet space it needs right now.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.EX_SOCIAL_MEDIA_CHECKING,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH
        ),
        ResponseTemplate(
            id = "SIT_ACAD_PARENTS_01",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "Fearing that you've disappointed your parents is such a heavy, suffocating pressure. But one exam score or semester does not define who you are or what your future holds. Take a slow, grounding breath. We can look at this one step at a time without letting the panic run the show.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.DISAPPOINTING_PARENTS,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY
        ),
        ResponseTemplate(
            id = "SIT_SOCIAL_EXCLUDED_01",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "Feeling left out or excluded by friends is one of the sharpest emotional stings. It hurts when people you cared about drift away or make plans without you. Please remember: being left out does not mean you lack worth. I'm right here with you—what happened with your friends?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.FEELING_EXCLUDED,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY
        ),

        // =====================================================================
        // BATCH 1: SCENARIO 1 — FRIENDSHIP CONFLICTS & DRIFT (8 responses)
        // =====================================================================
        ResponseTemplate(
            id = "SCN01_01",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Noticing distance between yourself and a close friend is a disorienting, quiet ache. Before assuming the worst or concluding they no longer care, let's take a slow breath. Sometimes people get consumed by their own hidden burdens. Has this distance been growing slowly, or did it feel sudden?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.FRIENDSHIP_DRIFT,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_01_FRIENDSHIP
        ),
        ResponseTemplate(
            id = "SCN01_02",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "When you realize you're always the one reaching out first, it can make the whole friendship feel hollow and exhausting. It is completely reasonable to feel hurt when effort feels unequal. Give yourself permission to pause and step back for a few days to see if space brings clarity.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.ONE_SIDED_FRIENDSHIP,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_01_FRIENDSHIP
        ),
        ResponseTemplate(
            id = "SCN01_03",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "Feeling like the third wheel or finding out friends made plans without you cuts deep. Being left out does not mean you lack value or that you're unwanted. I'm right here with you—what happened that made you feel excluded?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.THIRD_WHEEL,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_01_FRIENDSHIP
        ),
        ResponseTemplate(
            id = "SCN01_04",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Friendships naturally shift as life circumstances, routines, and responsibilities change. Grieving the closeness you used to have is normal. You don't have to force an artificial conversation or burn bridges today. Take things one gentle step at a time.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.FRIENDSHIP_DRIFT,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_01_FRIENDSHIP
        ),
        ResponseTemplate(
            id = "SCN01_05",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Having a close friend replace you or turn their attention elsewhere feels like an unspoken betrayal. Please remember your worth doesn't decrease because someone shifted their priorities. How long have you felt this shift?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.FRIENDSHIP_DRIFT,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_01_FRIENDSHIP
        ),
        ResponseTemplate(
            id = "SCN01_06",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "Losing touch with childhood friends is one of the quietest griefs of growing older. It's painful when shared history no longer bridges current distance. It's okay to cherish what you had without blaming yourself for where you both are now.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.LOSING_FRIENDS,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_01_FRIENDSHIP
        ),
        ResponseTemplate(
            id = "SCN01_07",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Before making a permanent decision to end a friendship in the heat of hurt, give yourself a day or two of emotional distance. Settle your thoughts, let your nervous system rest, and we can look at what healthy boundaries you need.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.ONE_SIDED_FRIENDSHIP,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_01_FRIENDSHIP
        ),
        ResponseTemplate(
            id = "SCN01_08",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "It takes courage to admit when a friendship feels painful or one-sided. You deserve connections where your energy and presence are met with reciprocal warmth. I'm listening—tell me more about what's been happening.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.FRIENDSHIP_CONFLICTS,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_01_FRIENDSHIP
        ),

        // =====================================================================
        // BATCH 1: SCENARIO 2 — FAMILY-RELATED CONVERSATIONS (8 responses)
        // =====================================================================
        ResponseTemplate(
            id = "SCN02_01",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "Living with the feeling that your parents never truly hear or understand you creates a heavy, everyday friction. It is exhausting when the people whose approval you care about seem to misunderstand your intentions. Take a breath—what happened at home today?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PARENTAL_CAREER_PRESSURE,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_02_FAMILY
        ),
        ResponseTemplate(
            id = "SCN02_02",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "When parents want you to follow a path or career you have no passion for, it feels like having to choose between their happiness and your own life. Wanting to live your own truth doesn't make you ungrateful or a bad child. What path are they pushing on you?",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.PARENTAL_CAREER_PRESSURE,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_02_FAMILY
        ),
        ResponseTemplate(
            id = "SCN02_03",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "Being constantly compared to siblings or relatives is profoundly invalidating. It treats love and recognition like a contest you never agreed to enter. Let's ground this moment: their comparisons do not define your worth or potential.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.SIBLING_COMPARISON,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_02_FAMILY
        ),
        ResponseTemplate(
            id = "SCN02_04",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "When communication at home repeatedly dissolves into yelling or dismissal, staying silent feels like the only defense. Before attempting another exhausting conversation, give yourself permission to step back and write out your feelings in private.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PARENTAL_CAREER_PRESSURE,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_02_FAMILY
        ),
        ResponseTemplate(
            id = "SCN02_05",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "Having your feelings dismissed as 'dramatic' or 'disrespectful' by family hurts deeply. Your emotional reality is valid, even if your parents lack the tools to acknowledge it. I'm right here with you.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.FAMILY_CONFLICTS,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_02_FAMILY
        ),
        ResponseTemplate(
            id = "SCN02_06",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "Parents often project their own unhealed anxieties and generational fears onto their children under the banner of protection. Recognizing their fear doesn't mean you have to absorb it as your truth.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.DISAPPOINTING_PARENTS,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_02_FAMILY
        ),
        ResponseTemplate(
            id = "SCN02_07",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "You don't have to resolve years of family communication patterns tonight. Protect your peace in your own space, take a slow grounding breath, and let yourself rest away from the tension.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.FAMILY_CONFLICTS,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_02_FAMILY
        ),
        ResponseTemplate(
            id = "SCN02_08",
            category = EmotionalClassificationEngine.EmotionCategory.FAMILY_CONFLICT,
            text = "Feeling unseen under your own roof is one of the loneliest feelings in the world. You have a sanctuary right here to speak openly without fear of judgment. What do you wish they could understand?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.FAMILY_CONFLICTS,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_02_FAMILY
        ),

        // =====================================================================
        // BATCH 1: SCENARIO 3 — FAILURE AND DISAPPOINTMENT (8 responses)
        // =====================================================================
        ResponseTemplate(
            id = "SCN03_01",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "Pouring your energy, sleep, and heart into something and still facing failure is devastating. I'm not going to give you clichés about failure being a lesson right now—it hurts, and you deserve space to feel that grief. What was the outcome you worked so hard for?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.UNSUCCESSFUL_EFFORT,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_03_FAILURE
        ),
        ResponseTemplate(
            id = "SCN03_02",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Seeing everyone else pass or celebrate while you're holding a disappointing result stings with acute isolation. But another person's success does not mean your effort was meaningless. Take a slow, steady breath.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.UNSUCCESSFUL_EFFORT,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_03_FAILURE
        ),
        ResponseTemplate(
            id = "SCN03_03",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Failing at a task or exam does not make you a failure as a human being. Your brain wants to generalize one bad outcome into an entire identity. Let's gently decouple this result from your permanent intelligence.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.UNSUCCESSFUL_EFFORT,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_03_FAILURE
        ),
        ResponseTemplate(
            id = "SCN03_04",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "When the initial shock and sting begin to settle, we can look at practical options together—re-evaluations, re-takes, or different routes. But for tonight, your only job is to be gentle with yourself.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.EXAM_FAILURE,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_03_FAILURE
        ),
        ResponseTemplate(
            id = "SCN03_05",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Failing an exam, making mistakes, or feeling like you let down everyone who believed in you is a crushing weight. But genuine people care about you, not just your test scores or resume. I'm holding space with you.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.EXAM_FAILURE,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_03_FAILURE
        ),
        ResponseTemplate(
            id = "SCN03_06",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Effort doesn't always guarantee immediate success, and that is one of the hardest truths of being human. Give yourself credit for showing up and fighting, even if the scoreboard didn't reflect it.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.UNSUCCESSFUL_EFFORT,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_03_FAILURE
        ),
        ResponseTemplate(
            id = "SCN03_07",
            category = EmotionalClassificationEngine.EmotionCategory.ACADEMIC_STRESS,
            text = "Do not make drastic declarations about your future while you are in the acute fog of disappointment. Drink some water, let your shoulders drop, and allow today to just be quiet.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.EXAM_FAILURE,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_03_FAILURE
        ),
        ResponseTemplate(
            id = "SCN03_08",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Mistakes and missteps are part of the process, even when they carry real consequences. What part of the result feels heaviest right now?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.EXAM_FAILURE,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_03_FAILURE
        ),

        // =====================================================================
        // BATCH 1: SCENARIO 4 — FUTURE UNCERTAINTY & CAREER STRESS (8 responses)
        // =====================================================================
        ResponseTemplate(
            id = "SCN04_01",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Feeling directionless or having no idea what you're doing with your life is terrifying, but it is also far more normal than anyone admits out loud. You don't need a ten-year master plan today. What decision is pressing on you most right now?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.DIRECTIONLESS,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_04_CAREER_UNCERTAINTY
        ),
        ResponseTemplate(
            id = "SCN04_02",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "When you look around and it seems like everyone has an organized roadmap, remember that most people are figuring things out step by step behind closed doors. You are not behind; you are simply in the exploratory phase of your own life.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.DIRECTIONLESS,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_04_CAREER_UNCERTAINTY
        ),
        ResponseTemplate(
            id = "SCN04_03",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Fearing that you'll make an irreversible choice traps your mind in analysis paralysis. Most career decisions are pivots, not prison sentences. Take a slow breath—we can break the uncertainty down into smaller steps.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.DIRECTIONLESS,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_04_CAREER_UNCERTAINTY
        ),
        ResponseTemplate(
            id = "SCN04_04",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "Instead of trying to discover your 'one true passion' under immense pressure, ask yourself: what is one subject, skill, or project you'd be curious to explore for just the next two weeks?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.CAREER_UNCERTAINTY,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_04_CAREER_UNCERTAINTY
        ),
        ResponseTemplate(
            id = "SCN04_05",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "The societal pressure to have your life, income, and career figured out in your twenties is overwhelming. Your worth as a person is not measured by how fast you climb a career ladder. I'm right here with you.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.CAREER_UNCERTAINTY,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_04_CAREER_UNCERTAINTY
        ),
        ResponseTemplate(
            id = "SCN04_06",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "Questioning your degree or chosen major is scary when you've invested time and tuition. But recognizing that something isn't fitting is self-awareness, not failure. What feels misaligned?",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.FEAR_WRONG_DEGREE,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_04_CAREER_UNCERTAINTY
        ),
        ResponseTemplate(
            id = "SCN04_07",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "When you feel directionless or unsure, bring your attention back to where your feet are right now. The future will be built day by day, not solved in a single anxious evening. Settle your mind and take one restful breath.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.DIRECTIONLESS,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_04_CAREER_UNCERTAINTY
        ),
        ResponseTemplate(
            id = "SCN04_08",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Feeling directionless often means old goals no longer fit who you are becoming. Let's make space for that evolution without panic. What used to feel meaningful to you?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.DIRECTIONLESS,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_04_CAREER_UNCERTAINTY
        ),

        // =====================================================================
        // BATCH 1: SCENARIO 5 — SOCIAL COMPARISON & INFERIORITY (8 responses)
        // =====================================================================
        ResponseTemplate(
            id = "SCN05_01",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Feeling like everyone your age is living meaningful, exciting lives while you are standing still is an excruciating form of self-torture. It is completely understandable that you're feeling left behind. What triggered this comparison today?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PEER_ACHIEVEMENT_ENVY,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_05_SOCIAL_COMPARISON
        ),
        ResponseTemplate(
            id = "SCN05_02",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "Social media presents a curated highlight reel of other people's promotions, travels, and celebrations. It never shows their panic attacks, private arguments, or feelings of inadequacy. Comparing your behind-the-scenes to their polished stage will always feel unfair.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.SOCIAL_MEDIA_ENVY,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_05_SOCIAL_COMPARISON
        ),
        ResponseTemplate(
            id = "SCN05_03",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Take a gentle, full breath and step away from social media and the feed for the rest of tonight. Your journey has its own unique obstacles, timing, and seasons. You cannot run someone else's race.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.SOCIAL_MEDIA_ENVY,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_05_SOCIAL_COMPARISON
        ),
        ResponseTemplate(
            id = "SCN05_04",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "When feelings of inferiority flare up, ask yourself: what is one small thing in your own life that you genuinely care about, completely outside of whether anyone else approves of it?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PEER_ACHIEVEMENT_ENVY,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_05_SOCIAL_COMPARISON
        ),
        ResponseTemplate(
            id = "SCN05_05",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Hearing about peers earning more money or achieving milestones can sting, even when you want to be happy for them. You don't have to feel guilty for that sting—it's just your own unmet needs speaking up. I'm listening.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.PEER_ACHIEVEMENT_ENVY,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_05_SOCIAL_COMPARISON
        ),
        ResponseTemplate(
            id = "SCN05_06",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "Believing that everyone else has it all figured out is an illusion. We are all improvising in different ways. Anchor back into what you can control right now: your own rest, your own next step, your own peace.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.ACADEMIC_COMPARISON,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_05_SOCIAL_COMPARISON
        ),
        ResponseTemplate(
            id = "SCN05_07",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "Close the social media apps, put your phone face down, and let your shoulders release. Your life is not a competition against your peers. Settle into this quiet moment.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.SOCIAL_MEDIA_ENVY,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_05_SOCIAL_COMPARISON
        ),
        ResponseTemplate(
            id = "SCN05_08",
            category = EmotionalClassificationEngine.EmotionCategory.LOW_SELF_ESTEEM,
            text = "You are not wasting your life simply because you are moving at a different pace or facing heavier burdens. Your experiences and perseverance matter. What feels hardest right now?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.PEER_ACHIEVEMENT_ENVY,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_05_SOCIAL_COMPARISON
        ),

        // =====================================================================
        // BATCH 2: SCENARIO 6 — MISUNDERSTOOD AND UNHEARD (8 responses)
        // =====================================================================
        ResponseTemplate(
            id = "SCN06_01",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "Feeling constantly misunderstood by the people around you is deeply alienating. It makes you feel like you are speaking a language nobody bothers to learn. I'm listening closely right here—what is the thought you wish someone would actually hear?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.COMMUNICATION_BREAKDOWN,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_06_MISUNDERSTOOD
        ),
        ResponseTemplate(
            id = "SCN06_02",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "When you try to explain your intentions and people twist your words into something negative, it is exhausting. It makes complete sense that you've wanted to withdraw and stop explaining yourself. You don't have to defend your reality here.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.COMMUNICATION_BREAKDOWN,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_06_MISUNDERSTOOD
        ),
        ResponseTemplate(
            id = "SCN06_03",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "Being told you are 'too sensitive' or 'overreacting' is a common way people deflect their own discomfort with big feelings. Your emotional depth is not a defect. What happened that they dismissed?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.LABELED_OVERLY_SENSITIVE,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_06_MISUNDERSTOOD
        ),
        ResponseTemplate(
            id = "SCN06_04",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "You are not obligated to spend your life convincing people of your perspective when they have committed to misunderstanding you. Let's protect your emotional energy. Take a slow, quiet breath.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.PERSPECTIVE_DISMISSED,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_06_MISUNDERSTOOD
        ),
        ResponseTemplate(
            id = "SCN06_05",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "Feeling invisible in group conversations makes you feel like an observer rather than a participant in your own social circle. It hurts when your voice is spoken over or ignored. I'm right here with you.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.COMMUNICATION_BREAKDOWN,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_06_MISUNDERSTOOD
        ),
        ResponseTemplate(
            id = "SCN06_06",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "Stopping yourself from explaining isn't giving up; sometimes it is a healthy boundary when people refuse to listen with empathy. You can preserve your voice for people who truly seek to understand you.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.COMMUNICATION_BREAKDOWN,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_06_MISUNDERSTOOD
        ),
        ResponseTemplate(
            id = "SCN06_07",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "Rest your mind in this quiet space. In this sanctuary, you never have to justify, argue, or prove your feelings. You are heard.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.PERSPECTIVE_DISMISSED,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_06_MISUNDERSTOOD
        ),
        ResponseTemplate(
            id = "SCN06_08",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "Take all the time you need. Say it however it comes out, messy or polished. I am listening without interruption.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.PERSPECTIVE_DISMISSED,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_06_MISUNDERSTOOD
        ),

        // =====================================================================
        // BATCH 2: SCENARIO 7 — REGRET AND GUILT (8 responses)
        // =====================================================================
        ResponseTemplate(
            id = "SCN07_01",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "Wishing you could rewind time and erase words you said in anger or carelessness is a heavy, burning ache. The fact that you feel this regret shows you have genuine empathy and a conscience. What is the moment you keep wishing you could undo?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PAST_DECISION_REGRET,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_07_REGRET_GUILT
        ),
        ResponseTemplate(
            id = "SCN07_02",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "Hurting someone who genuinely cared about you leaves a deep, painful sting of guilt. But punishing yourself endlessly will not repair the harm. True accountability means learning why it happened and offering a sincere, non-defensive apology if appropriate.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.HURT_SOMEONE,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_07_REGRET_GUILT
        ),
        ResponseTemplate(
            id = "SCN07_03",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "There is a crucial difference between feeling guilt ('I did something wrong') and feeling shame ('I am fundamentally bad'). You made a painful mistake, but you are not beyond learning and growth. Take a slow, grounding breath.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.SELF_BLAME,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_07_REGRET_GUILT
        ),
        ResponseTemplate(
            id = "SCN07_04",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "If the person is open to hearing from you, a simple apology acknowledging their pain without excuses is the most healing step. If contact isn't possible, you honor their memory by doing better with others going forward.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.HURT_SOMEONE,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_07_REGRET_GUILT
        ),
        ResponseTemplate(
            id = "SCN07_05",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "Replaying embarrassing or regrettable moments over and over in your head at night doesn't change history; it only tortures you in the present. Let's step away from the mental replay for a moment.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.SELF_BLAME,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_07_REGRET_GUILT
        ),
        ResponseTemplate(
            id = "SCN07_06",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "You acted from the information, emotional state, and maturity you had in that exact moment. You cannot judge your past self with today's clarity and wisdom.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.PAST_DECISION_REGRET,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_07_REGRET_GUILT
        ),
        ResponseTemplate(
            id = "SCN07_07",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "Forgiving yourself is not letting yourself off the hook; it is putting down the self-inflicted weapon so you can actually live responsibly today. Release your breath and unclench your jaw.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.SELF_BLAME,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_07_REGRET_GUILT
        ),
        ResponseTemplate(
            id = "SCN07_08",
            category = EmotionalClassificationEngine.EmotionCategory.GUILT,
            text = "Carrying secrets and guilt alone makes them feel ten times bigger and darker. I'm listening without judgment—unpack what's on your chest.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.SELF_BLAME,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_07_REGRET_GUILT
        ),

        // =====================================================================
        // BATCH 2: SCENARIO 8 — SLEEP DIFFICULTIES & NIGHT RUMINATION (8 responses)
        // =====================================================================
        ResponseTemplate(
            id = "SCN08_01",
            category = EmotionalClassificationEngine.EmotionCategory.SLEEP_DIFFICULTIES,
            text = "Lying awake at 3 AM while the rest of the world is asleep feels intensely isolating. The more you pressure yourself to fall asleep, the more awake your body becomes. For now, drop the requirement to sleep. Simply resting your eyes and body quietly in the dark is still restorative.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.CANNOT_SLEEP_INSOMNIA,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_08_SLEEP_RUMINATION
        ),
        ResponseTemplate(
            id = "SCN08_02",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "When the house is silent, the brain treats the quiet as an invitation to replay every unsolved dilemma and past worry. Try writing those racing thoughts down on a scrap of paper or in your notes app, telling your brain: 'This is stored safely; we can look at it tomorrow at 10 AM.'",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.NIGHT_RUMINATION,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_08_SLEEP_RUMINATION
        ),
        ResponseTemplate(
            id = "SCN08_03",
            category = EmotionalClassificationEngine.EmotionCategory.SLEEP_DIFFICULTIES,
            text = "Let's slow your physical system down right where you are lying. Take a slow inhale through your nose for 4 counts, hold gently for 4, and let out a long, slow sigh for 6 counts. Feel the physical weight of your mattress supporting you.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.CANNOT_SLEEP_INSOMNIA,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_08_SLEEP_RUMINATION
        ),
        ResponseTemplate(
            id = "SCN08_04",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "Nighttime thoughts are almost always amplified by physical exhaustion and darkness. Problems that feel insurmountable at 3 AM look entirely different in morning daylight. Don't believe everything your tired mind tells you tonight.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.NIGHT_RUMINATION,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_08_SLEEP_RUMINATION
        ),
        ResponseTemplate(
            id = "SCN08_05",
            category = EmotionalClassificationEngine.EmotionCategory.LONELINESS,
            text = "Staring at the ceiling while your mind loops through worst-case scenarios takes a heavy toll. I'm right here with you in this quiet night. You don't have to face this hour alone.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.LATE_NIGHT_LONELINESS,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_08_SLEEP_RUMINATION
        ),
        ResponseTemplate(
            id = "SCN08_06",
            category = EmotionalClassificationEngine.EmotionCategory.SLEEP_DIFFICULTIES,
            text = "If lying in bed is creating frustration, consider getting up for ten minutes, sitting in a dim chair, drinking a sip of room-temperature water, and listening to the ambient silence until your eyes feel heavy again.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.CANNOT_SLEEP_INSOMNIA,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_08_SLEEP_RUMINATION
        ),
        ResponseTemplate(
            id = "SCN08_07",
            category = EmotionalClassificationEngine.EmotionCategory.SLEEP_DIFFICULTIES,
            text = "Tomorrow will arrive regardless of whether you solve everything tonight. Give yourself permission to surrender this night to rest. Soften your forehead and let your shoulders drop.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.CANNOT_SLEEP_INSOMNIA,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_08_SLEEP_RUMINATION
        ),
        ResponseTemplate(
            id = "SCN08_08",
            category = EmotionalClassificationEngine.EmotionCategory.OVERTHINKING,
            text = "What is the main thought that keeps looping in your mind right now?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.NIGHT_RUMINATION,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_08_SLEEP_RUMINATION
        ),

        // =====================================================================
        // BATCH 2: SCENARIO 9 — SOCIAL ANXIETY & FEAR OF JUDGEMENT (8 responses)
        // =====================================================================
        ResponseTemplate(
            id = "SCN09_01",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "Feeling nervous and hyper-aware around new people is your nervous system trying to protect you from judgment or exclusion. It is completely normal, but it doesn't mean you are broken or socially incapable. What kind of gathering or interaction is making you feel on edge?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.MEETING_NEW_PEOPLE,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_09_SOCIAL_ANXIETY
        ),
        ResponseTemplate(
            id = "SCN09_02",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "When you worry that people find you boring or uninteresting, remember the 'spotlight effect': most people are so consumed by their own anxieties and how they appear that they rarely analyze you the way your inner critic does.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.POST_EVENT_RUMINATION,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_09_SOCIAL_ANXIETY
        ),
        ResponseTemplate(
            id = "SCN09_03",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "You don't need to be charismatic, funny, or entertaining to have a conversation. A quiet presence and asking one simple question (like 'How was your weekend?') is enough. Take the pressure off yourself to perform.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.MEETING_NEW_PEOPLE,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_09_SOCIAL_ANXIETY
        ),
        ResponseTemplate(
            id = "SCN09_04",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "If a conversation has an awkward pause, it belongs to both people, not just you. Silence is a natural breath in dialogue, not a catastrophe. Take a slow breath and let the pause be.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.FEELING_AWKWARD,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_09_SOCIAL_ANXIETY
        ),
        ResponseTemplate(
            id = "SCN09_05",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "Replaying conversations afterwards and dissecting every word you said is anxiety lying to you. Remind yourself: nobody is holding a mental scorecard of your words. How are you feeling right now?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.POST_EVENT_RUMINATION,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_09_SOCIAL_ANXIETY
        ),
        ResponseTemplate(
            id = "SCN09_06",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "Give your social battery permission to run low. It is completely acceptable to step out for a minute of fresh air or leave an event early when you've reached your limit.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.FEELING_AWKWARD,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_09_SOCIAL_ANXIETY
        ),
        ResponseTemplate(
            id = "SCN09_07",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "Social anxiety often convinces us that everyone is evaluating us with harsh eyes. Try shifting your focus from 'How do I look to them?' to 'What is interesting about this room or this topic?'",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.FEELING_AWKWARD,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_09_SOCIAL_ANXIETY
        ),
        ResponseTemplate(
            id = "SCN09_08",
            category = EmotionalClassificationEngine.EmotionCategory.SOCIAL_ANXIETY,
            text = "It takes real energy to manage this constant vigilance in public. Tell me about the situation that brought this on today.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.PUBLIC_SPEAKING_FEAR,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_09_SOCIAL_ANXIETY
        ),

        // =====================================================================
        // BATCH 2: SCENARIO 10 — PROCRASTINATION & PRODUCTIVITY DISTRESS (8 responses)
        // =====================================================================
        ResponseTemplate(
            id = "SCN10_01",
            category = EmotionalClassificationEngine.EmotionCategory.PROCRASTINATION,
            text = "Spending hours scrolling when you know deadlines are looming brings on a sharp wave of self-disgust. But beating yourself up right now only drains whatever energy you have left. Let's drop the self-blame—scrolling is an emotional attempt to escape overwhelm, not proof of laziness. What task has been hanging over you?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PHONE_DOOMSCROLLING,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_10_PROCRASTINATION
        ),
        ResponseTemplate(
            id = "SCN10_02",
            category = EmotionalClassificationEngine.EmotionCategory.PROCRASTINATION,
            text = "When you have mountains of pending work, your brain views the entire pile as a physical threat and freezes up. You cannot do ten tasks at once. Let's shrink the mountain: what is the single smallest piece of the puzzle?",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.TASK_OVERWHELM,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_10_PROCRASTINATION
        ),
        ResponseTemplate(
            id = "SCN10_03",
            category = EmotionalClassificationEngine.EmotionCategory.PROCRASTINATION,
            text = "Don't worry about finishing the whole assignment or doing it well. Give yourself permission to do a messy, 5-minute draft. Action generates motivation, not the other way around. Open the file and write just one line.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.AVOIDING_TASKS,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_10_PROCRASTINATION
        ),
        ResponseTemplate(
            id = "SCN10_04",
            category = EmotionalClassificationEngine.EmotionCategory.PROCRASTINATION,
            text = "Put your phone in another room or out of reach for just 20 minutes. Set a timer, put on some quiet music, and work on one thing until the timer rings. You can handle twenty focused minutes.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PHONE_DOOMSCROLLING,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_10_PROCRASTINATION
        ),
        ResponseTemplate(
            id = "SCN10_05",
            category = EmotionalClassificationEngine.EmotionCategory.LACK_OF_MOTIVATION,
            text = "Living with this constant buzz of 'I should be working' ruins even your downtime, so you end up neither resting nor working. You deserve real, guilt-free rest. What is making this task feel so heavy?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.TASK_OVERWHELM,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_10_PROCRASTINATION
        ),
        ResponseTemplate(
            id = "SCN10_06",
            category = EmotionalClassificationEngine.EmotionCategory.PROCRASTINATION,
            text = "Perfectionism is usually the quiet engine driving procrastination. We delay starting because we are terrified of doing it poorly. Allow yourself to produce a terrible first draft—it's infinitely better than a blank screen.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.AVOIDING_TASKS,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_10_PROCRASTINATION
        ),
        ResponseTemplate(
            id = "SCN10_07",
            category = EmotionalClassificationEngine.EmotionCategory.PROCRASTINATION,
            text = "Take a glass of water, stand up to stretch your legs, and reset this moment. Today is not ruined; you have the present hour right in front of you. Take one calm breath and begin small.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.AVOIDING_TASKS,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_10_PROCRASTINATION
        ),
        ResponseTemplate(
            id = "SCN10_08",
            category = EmotionalClassificationEngine.EmotionCategory.PROCRASTINATION,
            text = "Let's unpack the pressure. What is the deadline that's scaring you most?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.TASK_OVERWHELM,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_10_PROCRASTINATION
        ),        ResponseTemplate(
            id = "SCN11_01",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "Hearing that you are 'tired of everything' tells me how heavily you've been carrying life's demands without a genuine break. When your emotional battery reaches zero, even small requests feel unbearable. Take a moment right now and let yourself put down the weight. You don't have to carry tomorrow tonight.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.RESPONSIBILITY_OVERWHELM,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_11_BURNOUT_EXHAUSTION
        ),
        ResponseTemplate(
            id = "SCN11_02",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "When activities and hobbies that used to bring you joy suddenly feel like exhausting chores, it is a clear sign your nervous system is deeply depleted. Don't force yourself to feel enthusiastic. Give yourself permission to do nothing for a little while.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.ANHEDONIA_LOSS_OF_INTEREST,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_11_BURNOUT_EXHAUSTION
        ),
        ResponseTemplate(
            id = "SCN11_03",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "Wanting to withdraw and be left completely alone is a natural instinct to protect what little energy you have left. I respect your need for quiet and space. I'm right here with you—no questions, no demands.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.WITHDRAWAL_URGE,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_11_BURNOUT_EXHAUSTION
        ),
        ResponseTemplate(
            id = "SCN11_04",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "Burnout doesn't heal with a single good night of sleep; it requires reducing the demands you place on yourself. Look at your schedule for tomorrow: what is one obligation that can be postponed, simplified, or dropped entirely?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.RESPONSIBILITY_OVERWHELM,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_11_BURNOUT_EXHAUSTION
        ),
        ResponseTemplate(
            id = "SCN11_05",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "Unclench your jaw, lower your shoulders from your ears, and breathe out slowly. In this moment, you are safe, nothing is due, and nobody is evaluating you. Settle into this pause.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.MENTAL_DRAIN,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_11_BURNOUT_EXHAUSTION
        ),
        ResponseTemplate(
            id = "SCN11_06",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "You have spent so long pushing through fatigue and being strong for others that your body is finally demanding the rest it was denied. Honor this fatigue instead of fighting it.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.MENTAL_DRAIN,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_11_BURNOUT_EXHAUSTION
        ),
        ResponseTemplate(
            id = "SCN11_07",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "What part of your life has been pulling on your energy the most recently?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.MENTAL_DRAIN,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_11_BURNOUT_EXHAUSTION
        ),
        ResponseTemplate(
            id = "SCN11_08",
            category = EmotionalClassificationEngine.EmotionCategory.BURNOUT,
            text = "Take things in ten-minute increments today. Lower your expectations of productivity to zero. Surviving this hour is enough.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.MENTAL_DRAIN,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_11_BURNOUT_EXHAUSTION
        ),
        ResponseTemplate(
            id = "SCN12_01",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Feeling guilty about asking your parents or family for financial support is an uncomfortable, suffocating ache. But when you are in school, transitioning between careers, or dealing with rising living costs, financial dependence is a temporary season, not a character flaw. What has been making money feel especially stressful lately?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PARENTAL_FINANCIAL_DEPENDENCE,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_12_FINANCIAL_STRESS
        ),
        ResponseTemplate(
            id = "SCN12_02",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Looking at classmates or peers earning their own incomes and comparing your bank account to theirs creates an instant sense of inadequacy. But people start from vastly different privileges, generational supports, and timing. You cannot measure your capability by someone else's balance.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.PEER_WEALTH_COMPARISON,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_12_FINANCIAL_STRESS
        ),
        ResponseTemplate(
            id = "SCN12_03",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Your worth as a human being is not determined by how much money you bring in or how early you become financially self-sufficient. You contribute warmth, effort, presence, and thought to the people around you. Breathe into this truth.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.PEER_WEALTH_COMPARISON,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_12_FINANCIAL_STRESS
        ),
        ResponseTemplate(
            id = "SCN12_04",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "When financial anxiety spikes, the brain imagines worst-case ruin. Let's look at today: you have a place to sit, you have air in your lungs, and we can look at small, practical budgeting steps when your head is clear.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.EXPENSE_MANAGEMENT,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_12_FINANCIAL_STRESS
        ),
        ResponseTemplate(
            id = "SCN12_05",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Living under constant financial strain forces your nervous system into chronic survival mode. It makes every small expense feel like a threat. I hear how exhausting that is.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.EXPENSE_MANAGEMENT,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_12_FINANCIAL_STRESS
        ),
        ResponseTemplate(
            id = "SCN12_06",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Feeling like a burden to your family is a burden you place on yourself. Families support each other through different life seasons. Someday you will be in a position to give back, but right now it's okay to accept help.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.PARENTAL_FINANCIAL_DEPENDENCE,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_12_FINANCIAL_STRESS
        ),
        ResponseTemplate(
            id = "SCN12_07",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Step away from the budget spreadsheets for tonight. Constant hyper-vigilance won't change your finances this evening; it will only rob you of sleep. Rest your mind.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.EXPENSE_MANAGEMENT,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_12_FINANCIAL_STRESS
        ),
        ResponseTemplate(
            id = "SCN12_08",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "What is the specific expense or worry that's keeping you up?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.EXPENSE_MANAGEMENT,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_12_FINANCIAL_STRESS
        ),
        ResponseTemplate(
            id = "SCN13_01",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Stepping into a new environment—whether it's starting college, moving cities, or taking a new job—shakes the foundation of your daily security. Feeling like you don't belong here is the most common early reaction to change. Give yourself time; belonging is built slowly over weeks, not granted on day one.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.COLLEGE_START,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_13_LIFE_TRANSITIONS
        ),
        ResponseTemplate(
            id = "SCN13_02",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Missing your school friends and the comfort of your old life is completely natural. You aren't just missing people; you are missing the effortless familiarity of being known. Cherish those memories while giving this new chapter room to unfold.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.NOSTALGIA_PAST,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_13_LIFE_TRANSITIONS
        ),
        ResponseTemplate(
            id = "SCN13_03",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "When everything around you is unfamiliar, your brain works overtime to process new routes, faces, and expectations. It is normal to feel physically exhausted by 4 PM. Make your bedroom or personal corner a predictable, comforting haven tonight.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.ROUTINE_LOSS,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_13_LIFE_TRANSITIONS
        ),
        ResponseTemplate(
            id = "SCN13_04",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "You don't have to build a whole new friend group or master this environment all at once. Focus on establishing one tiny, comforting anchor routine—a favorite coffee spot, a quiet walk, or a nightly phone call home.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.ROUTINE_LOSS,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_13_LIFE_TRANSITIONS
        ),
        ResponseTemplate(
            id = "SCN13_05",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Homesickness is a physical ache in the center of your chest. It is proof of how deeply you loved the places and people you came from. I'm right here with you.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.NOSTALGIA_PAST,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_13_LIFE_TRANSITIONS
        ),
        ResponseTemplate(
            id = "SCN13_06",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Almost everyone walking around your campus or office feels just as awkward and eager for connection as you do, even if they wear a confident mask. You are not alone in this adjustment.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.COLLEGE_START,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_13_LIFE_TRANSITIONS
        ),
        ResponseTemplate(
            id = "SCN13_07",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "Take a long, deep breath and let your feet press into the floor. You have survived every previous transition in your life, and you will adapt to this one too. Take it one day at a time.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.RELOCATING_NEW_CITY,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_13_LIFE_TRANSITIONS
        ),
        ResponseTemplate(
            id = "SCN13_08",
            category = EmotionalClassificationEngine.EmotionCategory.ANXIETY,
            text = "What part of the new environment feels most jarring right now?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.RELOCATING_NEW_CITY,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_13_LIFE_TRANSITIONS
        ),
        ResponseTemplate(
            id = "SCN14_01",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "The emotional aftermath of a heated argument is filled with high adrenaline, shaking hands, and racing thoughts. Right now, your nervous system is in fight-or-flight. Don't rush to send another text or force a resolution this minute. Step back, drink some cold water, and let the storm in your body settle.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.HEATED_ARGUMENT,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_14_INTERPERSONAL_CONFLICT
        ),
        ResponseTemplate(
            id = "SCN14_02",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "Recognizing that you spoke out of anger and said things you regret takes maturity. Accountability doesn't mean taking 100% of the blame for the entire fight; it simply means owning your own words with honesty and preparing a sincere apology for your part.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.HEATED_ARGUMENT,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_14_INTERPERSONAL_CONFLICT
        ),
        ResponseTemplate(
            id = "SCN14_03",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "Most intense interpersonal conflicts happen because both people feel hurt, misunderstood, or unheard. Acknowledging that they were wrong too is valid, but the healthiest conversations happen when you focus on expressing your boundary rather than winning the debate.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.DISRESPECT_FELT,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_14_INTERPERSONAL_CONFLICT
        ),
        ResponseTemplate(
            id = "SCN14_04",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "A boundary is not a punishment or a wall; it is simply clear communication about what you need to feel safe and respected. When you are ready to talk, use 'I feel' statements instead of pointing fingers.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.DISRESPECT_FELT,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_14_INTERPERSONAL_CONFLICT
        ),
        ResponseTemplate(
            id = "SCN14_05",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "Feeling disrespected in an argument leaves a lingering bitter taste. Your desire for basic respect is non-negotiable. What was the core issue that triggered the blowup?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.DISRESPECT_FELT,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_14_INTERPERSONAL_CONFLICT
        ),
        ResponseTemplate(
            id = "SCN14_06",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "Never apologize for how you felt, but be willing to apologize for how you expressed it if you crossed lines. That balance preserves both your dignity and the relationship.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.HEATED_ARGUMENT,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_14_INTERPERSONAL_CONFLICT
        ),
        ResponseTemplate(
            id = "SCN14_07",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "Give the interaction 24 hours of breathing room if possible. Time allows defensiveness to cool on both sides. Settle into your sanctuary tonight.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.HEATED_ARGUMENT,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_14_INTERPERSONAL_CONFLICT
        ),
        ResponseTemplate(
            id = "SCN14_08",
            category = EmotionalClassificationEngine.EmotionCategory.ANGER,
            text = "Tell me what happened—I am here to help you unpack the conflict without judgment.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.HEATED_ARGUMENT,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_14_INTERPERSONAL_CONFLICT
        ),
        ResponseTemplate(
            id = "SCN15_01",
            category = EmotionalClassificationEngine.EmotionCategory.REJECTION,
            text = "Putting your heart on the line and being met with a 'no' is an intensely painful, vulnerable experience. It takes immense courage to confess how you feel, and facing rejection stings in the center of your chest. I'm really sorry you're hurting right now. How are you holding up?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.ROMANTIC_REJECTION,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_15_REJECTION
        ),
        ResponseTemplate(
            id = "SCN15_02",
            category = EmotionalClassificationEngine.EmotionCategory.REJECTION,
            text = "The embarrassment that follows romantic rejection can feel almost unbearable, making you want to disappear. But remember: having feelings and being brave enough to share them is a sign of human courage, not something to feel ashamed of. You took a risk that many never dare to take.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.UNRECIPROCATED_FEELINGS,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_15_REJECTION
        ),
        ResponseTemplate(
            id = "SCN15_03",
            category = EmotionalClassificationEngine.EmotionCategory.REJECTION,
            text = "Rejection is not a grade on your worth, beauty, or lovability. It is simply a lack of mutual compatibility between two specific people at one specific moment in time. Please don't let this outcome become a weapon you turn against yourself.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.FEELING_UNWANTED_AFTER_NO,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_15_REJECTION
        ),
        ResponseTemplate(
            id = "SCN15_04",
            category = EmotionalClassificationEngine.EmotionCategory.REJECTION,
            text = "Give yourself physical and digital distance from this person right now. Don't try to immediately force an awkward friendship or analyze their every social post. Your heart needs quiet time to grieve and reset.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.ROMANTIC_REJECTION,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_15_REJECTION
        ),
        ResponseTemplate(
            id = "SCN15_05",
            category = EmotionalClassificationEngine.EmotionCategory.REJECTION,
            text = "Being rejected makes your inner critic scream that you aren't 'enough'. But someone not seeing you as a romantic partner does not mean you lack immense value to the world. I'm right here with you.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.FEELING_UNWANTED_AFTER_NO,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_15_REJECTION
        ),
        ResponseTemplate(
            id = "SCN15_06",
            category = EmotionalClassificationEngine.EmotionCategory.REJECTION,
            text = "Accepting their decision with grace preserves your self-respect. You did your part by being honest; the rest is about caring for your own healing today.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.ROMANTIC_REJECTION,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_15_REJECTION
        ),
        ResponseTemplate(
            id = "SCN15_07",
            category = EmotionalClassificationEngine.EmotionCategory.REJECTION,
            text = "Take a gentle, deep breath. Cry if you need to, wrap yourself in something warm, and let today just be quiet. You will get through this.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.UNRECIPROCATED_FEELINGS,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_15_REJECTION
        ),
        ResponseTemplate(
            id = "SCN15_08",
            category = EmotionalClassificationEngine.EmotionCategory.REJECTION,
            text = "What part of the conversation keeps replaying in your mind?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.UNRECIPROCATED_FEELINGS,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_15_REJECTION
        ),
        ResponseTemplate(
            id = "SCN16_01",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "I am holding this space with you with deep gentleness. The ache of missing someone who meant so much to your life is a sacred, heavy sorrow. There is no timeline for grief and no right way to feel right now. I am sitting quietly with you.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.ABSENCE_LONGING,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_16_GRIEF_LOSS
        ),
        ResponseTemplate(
            id = "SCN16_02",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "Memories of the little everyday things you shared—routines, laughter, phrases they used—are treasures, but they also bring a sharp sting of longing. It is completely okay to let the tears fall without holding back.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.ABSENCE_LONGING,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_16_GRIEF_LOSS
        ),
        ResponseTemplate(
            id = "SCN16_03",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "Wishing for just five more minutes or one last conversation is the universal heartbeat of grief. That love does not vanish simply because they are no longer physically beside you. It lives on in who you are. What is a favorite memory you hold of them?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.ABSENCE_LONGING,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_16_GRIEF_LOSS
        ),
        ResponseTemplate(
            id = "SCN16_04",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "Please do not let anyone rush you into 'moving on' or 'staying strong'. Grief comes in waves—some days gentle, some days overwhelming. Take today as quietly as you need to.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.ABSENCE_LONGING,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_16_GRIEF_LOSS
        ),
        ResponseTemplate(
            id = "SCN16_05",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "Losing a beloved pet is losing a loyal, constant companion who loved you without conditions. That grief is real, profound, and deserves all the honor in the world. I'm right here with you.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.PET_LOSS,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_16_GRIEF_LOSS
        ),
        ResponseTemplate(
            id = "SCN16_06",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "Anniversaries, holidays, or even ordinary quiet afternoons can catch you off guard with an unexpected wave of sorrow. Be gentle with your heart when those waves arrive.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.ANNIVERSARY_GRIEF,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_16_GRIEF_LOSS
        ),
        ResponseTemplate(
            id = "SCN16_07",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "Wrap yourself in something warm, rest your head, and let yourself just be. You are held in this sanctuary.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.ABSENCE_LONGING,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_16_GRIEF_LOSS
        ),
        ResponseTemplate(
            id = "SCN16_08",
            category = EmotionalClassificationEngine.EmotionCategory.GRIEF,
            text = "I am here listening whenever you want to share memories or speak their name.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.ABSENCE_LONGING,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_16_GRIEF_LOSS
        ),
        ResponseTemplate(
            id = "SCN17_01",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "Living in a world that constantly bombards you with curated, edited, and filtered images makes it painful to look in the mirror. It is understandable that you're feeling this distress, but I want to remind you: your body is a living instrument that carries you through life, not an ornament created to please strangers. What brought this pain to the surface today?",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.APPEARANCE_INSECURITY,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_17_BODY_IMAGE
        ),
        ResponseTemplate(
            id = "SCN17_02",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "Comparing your real, unfiltered body to people on screens who have lighting, makeup, angles, and digital manipulation will always feel like an unwinnable contest. You are comparing biological reality to commercial fiction.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PHOTO_ANXIETY,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_17_BODY_IMAGE
        ),
        ResponseTemplate(
            id = "SCN17_03",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "When you feel unconfident going outside, remember: people in public are not zooming in on your perceived flaws. They are worried about their own lives and insecurities. You have a right to take up physical space exactly as you are.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.PHOTO_ANXIETY,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_17_BODY_IMAGE
        ),
        ResponseTemplate(
            id = "SCN17_04",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "You don't have to force yourself into 'toxic body positivity' or pretend you love everything in the mirror today. Try aiming for body neutrality: 'My body is safe, it is breathing, and it allows me to think, feel, and experience today.'",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.APPEARANCE_INSECURITY,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_17_BODY_IMAGE
        ),
        ResponseTemplate(
            id = "SCN17_05",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "Hearing negative comments or criticism from family or peers about your appearance leaves deep emotional bruises. Their comments reflect their own shallow conditioning, not your actual value. I'm right here with you.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.APPEARANCE_INSECURITY,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_17_BODY_IMAGE
        ),
        ResponseTemplate(
            id = "SCN17_06",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "Place a gentle hand on your chest and feel your steady heartbeat. Your body has fought for you every second of your life. Treat it with kindness tonight.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.WEIGHT_DISSATISFACTION,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_17_BODY_IMAGE
        ),
        ResponseTemplate(
            id = "SCN17_07",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "Dreading photographs happens because a frozen 2D snapshot cannot capture your warmth, expression, humor, and life. You are so much more than a camera angle.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PHOTO_ANXIETY,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_17_BODY_IMAGE
        ),
        ResponseTemplate(
            id = "SCN17_08",
            category = EmotionalClassificationEngine.EmotionCategory.INSECURITY,
            text = "What is the voice in your head saying right now?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.APPEARANCE_INSECURITY,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_17_BODY_IMAGE
        ),
        ResponseTemplate(
            id = "SCN18_01",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "That is absolutely incredible news! A huge congratulations to you! Take thirty seconds right now to feel the lightness and relief in your chest. You did that! Tell me all about how it happened!",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.ACADEMIC_ACHIEVEMENT,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_18_POSITIVE_CELEBRATION
        ),
        ResponseTemplate(
            id = "SCN18_02",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "I love hearing how hard you worked for this. The discipline, late nights, and persistence you put in behind the scenes have paid off. This victory belongs completely to you. Savor every bit of it!",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.PERSONAL_GOAL_REACHED,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_18_POSITIVE_CELEBRATION
        ),
        ResponseTemplate(
            id = "SCN18_03",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "Hearing you say 'I'm actually proud of myself' is the best part of all. Owning your success and recognizing your own resilience is so powerful. What feels best about this moment?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.GENERAL_GRATITUDE,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_18_POSITIVE_CELEBRATION
        ),
        ResponseTemplate(
            id = "SCN18_04",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "Moments of joy and relief replenish our emotional spirit. Don't rush onto the next stress or deadline right away. Celebrate tonight—treat yourself to something you enjoy.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.PERSONAL_GOAL_REACHED,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_18_POSITIVE_CELEBRATION
        ),
        ResponseTemplate(
            id = "SCN18_05",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "Getting that acceptance or passing that milestone is proof of what you can accomplish even when doubts were loud. Bask in this win!",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.ACADEMIC_ACHIEVEMENT,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_18_POSITIVE_CELEBRATION
        ),
        ResponseTemplate(
            id = "SCN18_06",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "I'm celebrating right alongside you in this sanctuary! How did you celebrate when you first saw the result?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.ACADEMIC_ACHIEVEMENT,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_18_POSITIVE_CELEBRATION
        ),
        ResponseTemplate(
            id = "SCN18_07",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "Anchor this feeling in your memory. The next time things feel tough, you can look back and remember that you have it in you to overcome and triumph.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.GENERAL_GRATITUDE,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_18_POSITIVE_CELEBRATION
        ),
        ResponseTemplate(
            id = "SCN18_08",
            category = EmotionalClassificationEngine.EmotionCategory.POSITIVE_EMOTIONS,
            text = "Who was the first person you shared the good news with?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.PERSONAL_GOAL_REACHED,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_18_POSITIVE_CELEBRATION
        ),
        ResponseTemplate(
            id = "SCN19_01",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "Some days are just grating from the moment your feet hit the floor. You don't need a profound psychological reason for today to just feel terrible. Vent as much as you need—what was the tipping point?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.BAD_DAY,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_19_CASUAL_FRUSTRATION
        ),
        ResponseTemplate(
            id = "SCN19_02",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "When everything and everyone is irritating you, it usually means your sensory threshold is completely tapped out. Step away from high-noise environments, put on some headphones or step outside for a breath of fresh air.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.IRRITATED_SMALL_THINGS,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_19_CASUAL_FRUSTRATION
        ),
        ResponseTemplate(
            id = "SCN19_03",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "It is completely okay not to know why you're in a foul mood. Human biology, blood sugar, hormones, and fatigue fluctuate without an obvious storyline. You don't have to analyze it; just give yourself space to be grumpy without guilt.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.CASUAL_VENTING,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_19_CASUAL_FRUSTRATION
        ),
        ResponseTemplate(
            id = "SCN19_04",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "Drink a tall glass of cold water, wash your face, and give yourself a physical reset. Sometimes resetting physical temperature helps break a mood loop.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.IRRITATED_SMALL_THINGS,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_19_CASUAL_FRUSTRATION
        ),
        ResponseTemplate(
            id = "SCN19_05",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "I'm here to listen to the whole rant. No problem is too small—get it all off your chest.",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.BAD_DAY,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_19_CASUAL_FRUSTRATION
        ),
        ResponseTemplate(
            id = "SCN19_06",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "Write off the rest of today as a wash. Tomorrow is a brand new page. For tonight, keep things low-key and comfortable.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.BAD_DAY,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_19_CASUAL_FRUSTRATION
        ),
        ResponseTemplate(
            id = "SCN19_07",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "Don't force yourself to act cheerful when you feel irritated. Honoring your mood without snapping at people is healthy.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.IRRITATED_SMALL_THINGS,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_19_CASUAL_FRUSTRATION
        ),
        ResponseTemplate(
            id = "SCN19_08",
            category = EmotionalClassificationEngine.EmotionCategory.FRUSTRATION,
            text = "What was the most annoying thing that happened today?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.CASUAL_VENTING,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_19_CASUAL_FRUSTRATION
        ),
        ResponseTemplate(
            id = "SCN20_01",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Feeling like your partner's affection or warmth has cooled off creates a deep, knotting anxiety in your stomach. It is natural to feel uneasy when patterns shift, but before spiraling into worst-case assumptions, take a slow breath. Has something specific shifted in your communication lately?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.RELATIONSHIP_INSECURITY,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_20_ROMANTIC_RELATIONSHIPS
        ),
        ResponseTemplate(
            id = "SCN20_02",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "When you feel like you are barely getting attention, that ache of unmet emotional connection is real. Wanting quality time and reassurance from your partner doesn't make you 'needy' or 'demanding'—those are normal human relationship needs.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.PARTNER_DISTANCING,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_20_ROMANTIC_RELATIONSHIPS
        ),
        ResponseTemplate(
            id = "SCN20_03",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Expressing your needs clearly without aggression or guilt-tripping is the healthiest step: 'I've been feeling disconnected lately and I really miss spending uninterrupted time with you.' How do they usually respond when you bring up feelings?",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.PARTNER_DISTANCING,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_20_ROMANTIC_RELATIONSHIPS
        ),
        ResponseTemplate(
            id = "SCN20_04",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Feeling jealous when your partner spends time with others is an uncomfortable emotion that usually points to underlying fear of abandonment or feeling insecure in your bond. Recognizing jealousy is healthy; acting on it with controlling behavior or checking their phone is not.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.JEALOUSY,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_20_ROMANTIC_RELATIONSHIPS
        ),
        ResponseTemplate(
            id = "SCN20_05",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Trust cannot be built through constant surveillance or testing your partner. It requires allowing them their independent life and observing whether they honor your bond with respect. Let your shoulders drop.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.JEALOUSY,
            stage = ConversationalStage.STAGE_5_SUPPORTIVE_FOLLOW_THROUGH,
            scenarioId = ScenarioId.SCENARIO_20_ROMANTIC_RELATIONSHIPS
        ),
        ResponseTemplate(
            id = "SCN20_06",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Relationship anxiety makes every delayed text or brief response feel like an impending breakup. Let's ground this moment: what are the concrete facts versus what anxiety is projecting?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.FEAR_OF_ABANDONMENT,
            stage = ConversationalStage.STAGE_1_INITIAL_DISCLOSURE,
            scenarioId = ScenarioId.SCENARIO_20_ROMANTIC_RELATIONSHIPS
        ),
        ResponseTemplate(
            id = "SCN20_07",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Couples naturally move from intense early infatuation into quieter routine phases. Quietness is not necessarily distance, but if genuine neglect is happening, you deserve to address it openly.",
            preferredMode = RegulationMode.UNDERSTAND,
            situation = UserSituation.RELATIONSHIP_INSECURITY,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_20_ROMANTIC_RELATIONSHIPS
        ),
        ResponseTemplate(
            id = "SCN20_08",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Balancing personal independence with intimacy is a continuous dance. Having your own friends, passions, and solitude makes a partnership healthier and less suffocating.",
            preferredMode = RegulationMode.REFLECT,
            situation = UserSituation.RELATIONSHIP_INSECURITY,
            stage = ConversationalStage.STAGE_3_EMOTIONAL_SUPPORT,
            scenarioId = ScenarioId.SCENARIO_20_ROMANTIC_RELATIONSHIPS
        ),
        ResponseTemplate(
            id = "SCN20_09",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "Take a gentle breath before having a major relationship conversation. Approach your partner as a teammate solving a puzzle together, rather than an adversary.",
            preferredMode = RegulationMode.CALM,
            situation = UserSituation.RELATIONSHIP_INSECURITY,
            stage = ConversationalStage.STAGE_4_CONTINUED_CONVERSATION,
            scenarioId = ScenarioId.SCENARIO_20_ROMANTIC_RELATIONSHIPS
        ),
        ResponseTemplate(
            id = "SCN20_10",
            category = EmotionalClassificationEngine.EmotionCategory.RELATIONSHIP_PROBLEMS,
            text = "I'm here with you. What is the feeling you are most scared to express to them?",
            preferredMode = RegulationMode.LISTEN,
            situation = UserSituation.FEAR_OF_ABANDONMENT,
            stage = ConversationalStage.STAGE_2_CONTEXT_DISCOVERY,
            scenarioId = ScenarioId.SCENARIO_20_ROMANTIC_RELATIONSHIPS
        )
    )

    // =========================================================================
    // CRISIS SAFETY-REVIEWED SEPARATE RESPONSE POOL
    // =========================================================================
    val crisisResponses = mapOf(
        1 to listOf(
            "I hear how heavy and hopeless things feel right now. The pain you are experiencing is real, but you do not have to carry it alone. Let's take a slow breath together. Would you like to tell me what's bringing on this unbearable weight?",
            "It sounds like you're under an immense amount of pressure, and I want to acknowledge how exhausting that is. Please know that your life and your presence matter deeply. What is the hardest thought right now?",
            "Thank you for sharing how deep this distress is. I'm sitting with you in this difficult space. You don't have to figure out everything tonight. Can we focus on just keeping you safe in this moment?"
        ),
        2 to listOf(
            "I care about your safety deeply, and hearing that you wish you didn't exist tells me how profound your hurting is right now. May I ask you directly and calmly: are you having thoughts of suicide or hurting yourself?",
            "When pain becomes overwhelming, it is natural to want the pain to stop, but I want to make sure you are safe. Are you having thoughts about ending your life?",
            "Please stay right here with me. You don't have to disappear to find relief from this pain. Let me ask you gently: are you feeling an urge to harm yourself right now?"
        ),
        3 to listOf(
            "I hear you, and I am so glad you told me. Because you're having active thoughts of suicide, your safety is my only priority. Do you have a plan or access to something you might use to hurt yourself?",
            "Thank you for your honesty in this painful moment. You are not alone, and you don't have to face this by yourself. Is there someone nearby you trust that you can contact right now?",
            "I am holding this space with you, but I want to make sure you have immediate human support. In India, please call Tele-MANAS at 14416 (or 112 for emergencies). In the US/Canada, please call or text 988. They are free, confidential, and 24/7. Can you reach out to them?"
        ),
        4 to listOf(
            "Please stay with me. If you are in immediate danger or have already taken steps to hurt yourself, please call emergency services right now: **112** in India or **988** in the US/Canada. There are compassionate people waiting to help you through this minute.",
            "Your life has immense value, and you do not have to go through this emergency alone. Please reach out to emergency services immediately (**112** or **14416** in India, **988** in US/Canada) or go to the nearest emergency room. I want you to be safe."
        )
    )

    /**
     * Clarification questions for when classification confidence is low.
     * Dynamic, varied, non-repetitive empathetic inquiries.
     */
    val clarificationInquiries = listOf(
        "I'm listening. Tell me a little about what's been on your mind.",
        "Take your time. I'm right here whenever you're ready to share.",
        "I hear that things are feeling heavy right now. What part feels biggest today?"
    )

    fun getRepeatedDisclosureResponse(situation: UserSituation?): String {
        return when (situation) {
            UserSituation.BREAKUP -> "I hear you. Going through a breakup is clearly weighing on you. You don't have to keep explaining yourself for your feelings to matter."
            UserSituation.MISSING_EX -> "I hear how deep that ache of missing them is. It's completely valid that your mind is returning to them. You don't have to justify why it hurts."
            UserSituation.EX_SOCIAL_MEDIA_CHECKING -> "I hear you. The impulse to keep checking is strong, but it's keeping you in pain. Let's step away together and take a quiet breath."
            UserSituation.EXAM_FAILURE, UserSituation.POOR_GRADES, UserSituation.DISAPPOINTING_PARENTS -> "I hear how much pressure this academic stress is putting on you. You don't have to carry the weight of all these expectations alone."
            else -> "I hear you, and I am listening. You don't have to repeat or explain yourself for your feelings to be recognized here."
        }
    }

    fun getRepeatedIdenticalMessageResponse(situation: UserSituation?): String {
        if (situation == UserSituation.BREAKUP) {
            return "I hear you. Going through a breakup is clearly weighing on you. You don't have to keep explaining yourself for your feelings to matter."
        }
        return "I hear how important this is to you. It makes complete sense that this is lingering heavily on your mind. Let's sit with this together—what part feels hardest right now?"
    }
}
