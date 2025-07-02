package com.appsbase.jetcode.feature.learning.presentation.lesson

import androidx.lifecycle.viewModelScope
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import com.appsbase.jetcode.core.common.error.getUserMessage
import com.appsbase.jetcode.core.common.mvi.BaseViewModel
import com.appsbase.jetcode.core.domain.usecase.GetLessonByIdUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Lesson screen following MVI pattern
 */
class LessonViewModel(
    private val getLessonByIdUseCase: GetLessonByIdUseCase
) : BaseViewModel<LessonState, LessonIntent, LessonEffect>(
    initialState = LessonState()
) {

    override fun handleIntent(intent: LessonIntent) {
        when (intent) {
            is LessonIntent.LoadLesson -> loadLesson(intent.lessonId)
            is LessonIntent.MaterialSelected -> selectMaterial(intent.materialIndex)
            is LessonIntent.PracticeClicked -> handlePracticeClick(intent.practiceId)
            is LessonIntent.MarkAsCompleted -> markLessonAsCompleted()
            is LessonIntent.RetryClicked -> retryLoading()
        }
    }

    private fun loadLesson(lessonId: String) {
        updateState(currentState().copy(isLoading = true, error = null))

        viewModelScope.launch {
            getLessonByIdUseCase(lessonId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        updateState(currentState().copy(isLoading = true))
                    }
                    is Result.Success -> {
                        val lesson = result.data
                        updateState(
                            currentState().copy(
                                isLoading = false,
                                lesson = lesson,
                                materials = lesson.materials,
                                practices = lesson.practices,
                                isCompleted = lesson.isCompleted,
                                error = null
                            )
                        )
                        Timber.d("Lesson loaded successfully: ${lesson.title}")
                    }
                    is Result.Error -> {
                        val errorMessage = when (val exception = result.exception) {
                            is AppError -> exception.getUserMessage()
                            else -> exception.message ?: "Failed to load lesson"
                        }
                        updateState(
                            currentState().copy(
                                isLoading = false,
                                error = errorMessage
                            )
                        )
                        sendEffect(LessonEffect.ShowError(errorMessage))
                        Timber.e(result.exception, "Error loading lesson")
                    }
                }
            }
        }
    }

    private fun selectMaterial(materialIndex: Int) {
        updateState(currentState().copy(currentMaterialIndex = materialIndex))
        Timber.d("Material selected: index $materialIndex")
    }

    private fun handlePracticeClick(practiceId: String) {
        sendEffect(LessonEffect.NavigateToPractice(practiceId))
        Timber.d("Practice clicked: $practiceId")
    }

    private fun markLessonAsCompleted() {
        updateState(currentState().copy(isCompleted = true))
        sendEffect(LessonEffect.ShowCompletionDialog)
        Timber.d("Lesson marked as completed")
    }

    private fun retryLoading() {
        currentState().lesson?.let { lesson ->
            loadLesson(lesson.id)
        }
    }
}
