package com.appsbase.jetcode.feature.learning.presentation.topic_detail

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.Quiz
import com.appsbase.jetcode.core.domain.model.Topic

/**
 * MVI contracts for Topic Detail screen
 */

data class TopicDetailState(
    val isLoading: Boolean = false,
    val topic: Topic? = null,
    val materials: List<Material> = emptyList(),
    val practices: List<Quiz> = emptyList(),
    val currentMaterialIndex: Int = 0,
    val currentPracticeIndex: Int = 0,
    val isShowingPractice: Boolean = false,
    val error: String? = null
) : UiState

sealed class TopicDetailIntent : UiIntent {
    data class LoadTopic(val topicId: String) : TopicDetailIntent()
    data object NextMaterial : TopicDetailIntent()
    data object PreviousMaterial : TopicDetailIntent()
    data object StartPractice : TopicDetailIntent()
    data object NextPractice : TopicDetailIntent()
    data object PreviousPractice : TopicDetailIntent()
    data class SubmitAnswer(val answer: String) : TopicDetailIntent()
    data object CompleteTopic : TopicDetailIntent()
    data class RetryClicked(val topicId: String) : TopicDetailIntent()
}

sealed class TopicDetailEffect : UiEffect {
    data object NavigateBack : TopicDetailEffect()
    data object ShowCorrectAnswer : TopicDetailEffect()
    data class ShowIncorrectAnswer(val correctAnswer: String) : TopicDetailEffect()
    data class ShowError(val message: String) : TopicDetailEffect()
    data class ShowTopicCompleted(val score: Int) : TopicDetailEffect()
}
