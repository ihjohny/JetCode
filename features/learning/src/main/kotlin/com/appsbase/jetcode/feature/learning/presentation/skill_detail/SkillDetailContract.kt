package com.appsbase.jetcode.feature.learning.presentation.skill_detail

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.model.Topic

/**
 * MVI contracts for Skill Detail screen
 */

data class SkillDetailState(
    val isLoading: Boolean = false,
    val skill: Skill? = null,
    val userTopics: List<UserTopic>? = null,
    val error: String? = null,
) : UiState {

    data class UserTopic(
        val topic: Topic,
        val currentMaterialIndex: Int,
    ) {
        val progressValue: Float
            get() = if (topic.materialIds.isNotEmpty()) {
                (currentMaterialIndex + 1f) / topic.materialIds.size
            } else {
                0f
            }
    }

}

sealed class SkillDetailIntent : UiIntent {
    data class LoadSkill(val skillId: String) : SkillDetailIntent()
    data class TopicClicked(val topicId: String) : SkillDetailIntent()
    data class RetryClicked(val skillId: String) : SkillDetailIntent()
}

sealed class SkillDetailEffect : UiEffect {
    data class NavigateToTopic(val topicId: String) : SkillDetailEffect()
    data class ShowError(val message: String) : SkillDetailEffect()
}
