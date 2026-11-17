package com.example.data.safety

import com.example.data.local.entity.SafetyStateEntity
import com.example.data.repository.SolaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Manages persistent safety state transitions, restoration after app restart,
 * and mapping between in-memory RiskState and Room SafetyStateEntity.
 */
class SafetyStateManager(
    private val repository: SolaceRepository
) {

    val activeStateFlow: Flow<RiskState> = repository.activeSafetyState.map { entity ->
        entity?.toRiskState() ?: RiskState()
    }

    suspend fun getInitialState(conversationId: String = "active_session"): RiskState {
        val entity = repository.getSafetyStateSync(conversationId)
        return entity?.toRiskState() ?: RiskState(conversationId = conversationId)
    }

    suspend fun persistState(state: RiskState) {
        repository.saveSafetyState(state.toEntity())
    }

    suspend fun resetState(conversationId: String = "active_session") {
        repository.clearSafetyState(conversationId)
    }

    companion object {
        fun SafetyStateEntity.toRiskState(): RiskState {
            val level = try {
                RiskLevel.valueOf(currentLevel)
            } catch (_: Exception) {
                RiskLevel.LEVEL_0_NORMAL
            }
            val highest = try {
                RiskLevel.valueOf(highestLevel)
            } catch (_: Exception) {
                RiskLevel.LEVEL_0_NORMAL
            }
            val convState = try {
                ConversationState.valueOf(conversationState)
            } catch (_: Exception) {
                ConversationState.NORMAL
            }

            val answersMap = questionAnswersJson.split(";")
                .filter { it.contains(":") }
                .associate {
                    val parts = it.split(":")
                    parts[0] to parts[1]
                }

            return RiskState(
                conversationId = conversationId,
                currentLevel = level,
                highestLevel = highest,
                conversationState = convState,
                unresolvedSafetyConcern = unresolvedSafetyConcern,
                emergencyTriggered = emergencyTriggered,
                lastAssessment = lastAssessment,
                lastScreeningQuestionAsked = lastScreeningQuestionAsked,
                awaitingScreeningAnswer = awaitingScreeningAnswer,
                pendingQuestionId = pendingQuestionId,
                lastAnsweredQuestionId = lastAnsweredQuestionId,
                lastAnswer = lastAnswer,
                questionAnswersMap = answersMap,
                turnCount = turnCount,
                helpRecommended = helpRecommended,
                humanSupportContacted = humanSupportContacted,
                safetyConfirmed = safetyConfirmed,
                lastConfirmationType = lastConfirmationType,
                updatedAt = updatedAt
            )
        }

        fun RiskState.toEntity(): SafetyStateEntity {
            val jsonMap = questionAnswersMap.entries.joinToString(";") { "${it.key}:${it.value}" }
            return SafetyStateEntity(
                conversationId = conversationId.ifBlank { "active_session" },
                currentLevel = currentLevel.name,
                highestLevel = highestLevel.name,
                conversationState = conversationState.name,
                unresolvedSafetyConcern = unresolvedSafetyConcern,
                emergencyTriggered = emergencyTriggered,
                lastAssessment = lastAssessment,
                lastScreeningQuestionAsked = lastScreeningQuestionAsked,
                awaitingScreeningAnswer = awaitingScreeningAnswer,
                pendingQuestionId = pendingQuestionId,
                lastAnsweredQuestionId = lastAnsweredQuestionId,
                lastAnswer = lastAnswer,
                questionAnswersJson = jsonMap,
                turnCount = turnCount,
                helpRecommended = helpRecommended,
                humanSupportContacted = humanSupportContacted,
                safetyConfirmed = safetyConfirmed,
                lastConfirmationType = lastConfirmationType,
                updatedAt = updatedAt
            )
        }
    }
}

