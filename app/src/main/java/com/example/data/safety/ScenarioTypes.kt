package com.example.data.safety

enum class ScenarioId(val displayName: String, val group: ScenarioGroup) {
    SCENARIO_01_FRIENDSHIP("Friendship Conflicts & Drift", ScenarioGroup.INTERPERSONAL_RELATIONSHIPS),
    SCENARIO_02_FAMILY("Family-Related Emotional Conversations", ScenarioGroup.INTERPERSONAL_RELATIONSHIPS),
    SCENARIO_03_FAILURE("Failure and Disappointment", ScenarioGroup.ACADEMIC_CAREER_ACHIEVEMENT),
    SCENARIO_04_CAREER_UNCERTAINTY("Future Uncertainty & Career Stress", ScenarioGroup.ACADEMIC_CAREER_ACHIEVEMENT),
    SCENARIO_05_SOCIAL_COMPARISON("Social Comparison & Inferiority", ScenarioGroup.SELF_IMAGE_COGNITION),
    SCENARIO_06_MISUNDERSTOOD("Misunderstood and Unheard", ScenarioGroup.SELF_IMAGE_COGNITION),
    SCENARIO_07_REGRET_GUILT("Regret and Guilt", ScenarioGroup.SELF_IMAGE_COGNITION),
    SCENARIO_08_SLEEP_RUMINATION("Sleep Difficulties & Night Rumination", ScenarioGroup.LIFE_TRANSITIONS_ENVIRONMENT),
    SCENARIO_09_SOCIAL_ANXIETY("Social Anxiety & Fear of Judgement", ScenarioGroup.SELF_IMAGE_COGNITION),
    SCENARIO_10_PROCRASTINATION("Procrastination & Productivity Distress", ScenarioGroup.ACADEMIC_CAREER_ACHIEVEMENT),
    SCENARIO_11_BURNOUT_EXHAUSTION("Emotional Exhaustion & Burnout", ScenarioGroup.LIFE_TRANSITIONS_ENVIRONMENT),
    SCENARIO_12_FINANCIAL_STRESS("Financial Stress & Dependence", ScenarioGroup.ACADEMIC_CAREER_ACHIEVEMENT),
    SCENARIO_13_LIFE_TRANSITIONS("Life Transitions & Unfamiliarity", ScenarioGroup.LIFE_TRANSITIONS_ENVIRONMENT),
    SCENARIO_14_INTERPERSONAL_CONFLICT("Interpersonal Conflict & Boundaries", ScenarioGroup.INTERPERSONAL_RELATIONSHIPS),
    SCENARIO_15_REJECTION("Rejection & Unreciprocated Feelings", ScenarioGroup.INTERPERSONAL_RELATIONSHIPS),
    SCENARIO_16_GRIEF_LOSS("Grief and Loss", ScenarioGroup.LIFE_TRANSITIONS_ENVIRONMENT),
    SCENARIO_17_BODY_IMAGE("Body Image & Self-Confidence", ScenarioGroup.SELF_IMAGE_COGNITION),
    SCENARIO_18_POSITIVE_CELEBRATION("Positive Emotions & Celebration", ScenarioGroup.CASUAL_AND_WELLBEING),
    SCENARIO_19_CASUAL_FRUSTRATION("Everyday Frustration & Casual Venting", ScenarioGroup.CASUAL_AND_WELLBEING),
    SCENARIO_20_ROMANTIC_RELATIONSHIPS("Romantic Relationship Support", ScenarioGroup.INTERPERSONAL_RELATIONSHIPS)
}

enum class ScenarioGroup {
    INTERPERSONAL_RELATIONSHIPS,
    ACADEMIC_CAREER_ACHIEVEMENT,
    SELF_IMAGE_COGNITION,
    LIFE_TRANSITIONS_ENVIRONMENT,
    CASUAL_AND_WELLBEING
}
