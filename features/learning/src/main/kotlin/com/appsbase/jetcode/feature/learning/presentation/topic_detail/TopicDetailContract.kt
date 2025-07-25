package com.appsbase.jetcode.feature.learning.presentation.topic_detail

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.domain.model.Material
import com.appsbase.jetcode.domain.model.Topic

/**
 * MVI contracts for Topic Detail screen
 */

data class TopicDetailState(
    val isLoading: Boolean = false,
    val topic: Topic? = null,
    val materials: List<Material> = emptyList(),
    val currentMaterialIndex: Int = 0,
    val error: String? = null
) : UiState {
    val progressValueLabel get() = "${(currentMaterialIndex + 1).coerceAtMost(materials.size)} of ${materials.size}"
}

sealed class TopicDetailIntent : UiIntent {
    data class LoadTopic(val topicId: String) : TopicDetailIntent()
    data object NextMaterial : TopicDetailIntent()
    data object PreviousMaterial : TopicDetailIntent()
    data object StartPractice : TopicDetailIntent()
    data class RetryClicked(val topicId: String) : TopicDetailIntent()
}

sealed class TopicDetailEffect : UiEffect {
    data object NavigateBack : TopicDetailEffect()
    data class NavigateToPractice(val practiceSetId: String) : TopicDetailEffect()
    data class ShowError(val message: String) : TopicDetailEffect()
}
