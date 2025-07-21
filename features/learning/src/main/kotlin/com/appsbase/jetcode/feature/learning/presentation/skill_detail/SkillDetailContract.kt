package com.appsbase.jetcode.feature.learning.presentation.skill_detail

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.model.Topic

/**
 * MVI contracts for Skill Detail screen
 */

data class SkillDetailState(
    val isLoading: Boolean = false,
    val skill: Skill? = null,
    val topics: List<Topic> = emptyList(),
    val error: String? = null
) : UiState

sealed class SkillDetailIntent : UiIntent {
    data class LoadSkill(val skillId: String) : SkillDetailIntent()
    data class TopicClicked(val topicId: String) : SkillDetailIntent()
    data class RetryClicked(val skillId: String) : SkillDetailIntent()
}

sealed class SkillDetailEffect : UiEffect {
    data class NavigateToTopic(val topicId: String) : SkillDetailEffect()
    data class ShowError(val message: String) : SkillDetailEffect()
}
