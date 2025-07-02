package com.appsbase.jetcode.feature.learning.presentation.lesson

import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.Practice

/**
 * MVI contracts for Lesson screen
 */

data class LessonState(
    val isLoading: Boolean = false,
    val lesson: Lesson? = null,
    val materials: List<Material> = emptyList(),
    val practices: List<Practice> = emptyList(),
    val currentMaterialIndex: Int = 0,
    val error: String? = null,
    val isCompleted: Boolean = false
) : UiState

sealed class LessonIntent : UiIntent {
    data class LoadLesson(val lessonId: String) : LessonIntent()
    data class MaterialSelected(val materialIndex: Int) : LessonIntent()
    data class PracticeClicked(val practiceId: String) : LessonIntent()
    data object MarkAsCompleted : LessonIntent()
    data object RetryClicked : LessonIntent()
}

sealed class LessonEffect : UiEffect {
    data class NavigateToPractice(val practiceId: String) : LessonEffect()
    data class ShowError(val message: String) : LessonEffect()
    data object ShowCompletionDialog : LessonEffect()
}
