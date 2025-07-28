package com.appsbase.jetcode.feature.learning.presentation.topic_detail

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.domain.usecase.GetMaterialsByIdsUseCase
import com.appsbase.jetcode.domain.usecase.GetTopicByIdUseCase
import com.appsbase.jetcode.domain.usecase.GetTopicProgressUseCase
import com.appsbase.jetcode.domain.usecase.UpdateProgressUseCase
import com.appsbase.jetcode.feature.learning.presentation.topic_detail.TopicDetailEffect.ShowError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Topic Detail screen following MVI pattern
 */
class TopicDetailViewModel(
    private val getTopicByIdUseCase: GetTopicByIdUseCase,
    private val getMaterialsByIdsUseCase: GetMaterialsByIdsUseCase,
    private val getTopicProgressUseCase: GetTopicProgressUseCase,
    private val updateProgressUseCase: UpdateProgressUseCase,
) : BaseViewModel<TopicDetailState, TopicDetailIntent, TopicDetailEffect>(
    initialState = TopicDetailState()
) {

    override fun handleIntent(intent: TopicDetailIntent) {
        when (intent) {
            is TopicDetailIntent.LoadTopic -> loadTopic(intent.topicId)
            is TopicDetailIntent.NextMaterial -> nextMaterial()
            is TopicDetailIntent.PreviousMaterial -> previousMaterial()
            is TopicDetailIntent.StartPractice -> {}
            is TopicDetailIntent.RetryClicked -> retryLoading(intent.topicId)
        }
    }

    private fun loadTopic(topicId: String) {
        updateState(
            currentState().copy(
                isLoading = true,
                error = null,
            )
        )

        viewModelScope.launch {
            // Load topic details
            getTopicByIdUseCase(topicId).collect { topicResult ->
                when (topicResult) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                topic = topicResult.data,
                                isLoading = false,
                            )
                        )
                        loadMaterials(topicResult.data.materialIds)
                        Timber.d("Topic loaded successfully: ${topicResult.data.name}")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = topicResult.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "An unexpected error occurred"
                        }
                        updateState(
                            currentState().copy(
                                isLoading = false,
                                error = errorMessage,
                            )
                        )
                        sendEffect(ShowError(errorMessage))
                        Timber.e("Error loading topic: $errorMessage")
                    }
                }
            }
        }
    }

    private fun loadMaterials(materialIds: List<String>) {
        viewModelScope.launch {
            getMaterialsByIdsUseCase(materialIds).collect { materialsResult ->
                when (materialsResult) {
                    is Result.Success -> {
                        updateState(
                            currentState().copy(
                                materials = materialsResult.data
                            )
                        )
                        restoreOnProgressedIndex()
                        Timber.d("Materials loaded successfully: ${materialsResult.data.size} items")
                    }

                    is Result.Error -> {
                        val errorMessage = when (val exception = materialsResult.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "Failed to load materials"
                        }
                        Timber.e("Error loading materials: $errorMessage")
                    }
                }
            }
        }
    }

    private fun restoreOnProgressedIndex() {
        viewModelScope.launch {
            val topicId = currentState().topic?.id ?: return@launch
            getTopicProgressUseCase(topicId).first().let {
                when (it) {
                    is Result.Success -> {
                        val progress = it.data
                        if (progress != null) {
                            updateState(
                                currentState().copy(
                                    currentMaterialIndex = progress.currentMaterialIndex,
                                )
                            )
                            Timber.d("Progress restore successfully: $topicId, index: ${progress.currentMaterialIndex}")
                        } else {
                            val index = 0
                            Timber.d("No progress found for topic $topicId, starting from index $index")
                            updateState(currentState().copy(currentMaterialIndex = index))
                            saveProgress(updatedMaterialIndex = index) // 0 means enrolled to the topic
                        }
                    }

                    is Result.Error -> {
                        Timber.e("Error restoring progress for topic $topicId: ${it.exception.message}")
                    }
                }
            }
        }
    }

    private fun nextMaterial() {
        val state = currentState()
        val nextIndex = state.currentMaterialIndex + 1
        if (nextIndex < state.materials.size) {
            updateState(state.copy(currentMaterialIndex = nextIndex))
        } else {
            // All materials completed, update index to show completion state
            updateState(state.copy(currentMaterialIndex = nextIndex))
            Timber.d("All materials completed for topic: ${state.topic?.name}")
        }
        saveProgress(updatedMaterialIndex = nextIndex)
    }

    private fun previousMaterial() {
        val state = currentState()
        val prevIndex = state.currentMaterialIndex - 1
        if (prevIndex >= 0) {
            updateState(state.copy(currentMaterialIndex = prevIndex))
        }
    }

    private fun retryLoading(topicId: String) {
        loadTopic(topicId)
    }

    private fun saveProgress(updatedMaterialIndex: Int) {
        val mTopicId = currentState().topic?.id
        if (mTopicId.isNullOrEmpty()) return
        if (updatedMaterialIndex < 0 || updatedMaterialIndex > currentState().materials.size) return

        viewModelScope.launch {
            updateProgressUseCase(
                topicId = mTopicId,
                updatedMaterialIndex = updatedMaterialIndex,
            )
        }
    }
}
