package com.appsbase.jetcode.feature.practice.presentation

import com.appsbase.jetcode.core.common.mvi.UiEffect
import com.appsbase.jetcode.core.common.mvi.UiIntent
import com.appsbase.jetcode.core.common.mvi.UiState
import com.appsbase.jetcode.core.domain.model.Difficulty
import com.appsbase.jetcode.core.domain.model.PracticeSet
import com.appsbase.jetcode.core.domain.model.Quiz
import com.appsbase.jetcode.core.domain.model.QuizType

/**
 * MVI contracts for Practice screen
 */

data class QuizResult(
    val quiz: Quiz,
    val userAnswer: String,
    val isCorrect: Boolean,
    val timeTaken: Long = 0L
)

data class QuizStatistics(
    val totalQuizzes: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val averageTime: Long,
    val scorePercentage: Int,
    val difficultyBreakdown: Map<Difficulty, Int>,
    val typeBreakdown: Map<QuizType, Int>
) {
    val accuracy: Float get() = if (totalQuizzes > 0) correctAnswers.toFloat() / totalQuizzes else 0f
}

data class PracticeState(
    val isLoading: Boolean = false,
    val practiceSet: PracticeSet? = null,
    val quizzes: List<Quiz> = emptyList(),
    val currentQuizIndex: Int = 0,
    val userAnswer: String = "",
    val showAnswer: Boolean = false,
    val quizResults: List<QuizResult> = emptyList(),
    val isCompleted: Boolean = false,
    val showAllAnswers: Boolean = false,
    val startTime: Long = 0L,
    val error: String? = null
) : UiState {
    val currentQuiz: Quiz? get() = quizzes.getOrNull(currentQuizIndex)
    val progressLabel get() = "${(currentQuizIndex + 1).coerceAtMost(quizzes.size)} of ${quizzes.size}"
    val progressValue get() = if (quizzes.isNotEmpty()) (currentQuizIndex + 1f) / quizzes.size else 0f

    val statistics: QuizStatistics get() {
        val correctCount = quizResults.count { it.isCorrect }
        val avgTime = if (quizResults.isNotEmpty()) quizResults.map { it.timeTaken }.average().toLong() else 0L
        val scorePercentage = if (quizResults.isNotEmpty()) (correctCount * 100) / quizResults.size else 0

        val difficultyBreakdown = quizResults.groupBy { it.quiz.difficulty }
            .mapValues { it.value.count { result -> result.isCorrect } }

        val typeBreakdown = quizResults.groupBy { it.quiz.type }
            .mapValues { it.value.count { result -> result.isCorrect } }

        return QuizStatistics(
            totalQuizzes = quizResults.size,
            correctAnswers = correctCount,
            wrongAnswers = quizResults.size - correctCount,
            averageTime = avgTime,
            scorePercentage = scorePercentage,
            difficultyBreakdown = difficultyBreakdown,
            typeBreakdown = typeBreakdown
        )
    }
}

sealed class PracticeIntent : UiIntent {
    data class LoadPracticeSet(val practiceSetId: String) : PracticeIntent()
    data class AnswerChanged(val answer: String) : PracticeIntent()
    data object SubmitAnswer : PracticeIntent()
    data object NextQuiz : PracticeIntent()
    data object PreviousQuiz : PracticeIntent()
    data object ShowCorrectAnswer : PracticeIntent()
    data object HideAnswer : PracticeIntent()
    data object ShowAllAnswers : PracticeIntent()
    data object HideAllAnswers : PracticeIntent()
    data object RestartPractice : PracticeIntent()
    data class RetryClicked(val practiceSetId: String) : PracticeIntent()
}

sealed class PracticeEffect : UiEffect {
    data object NavigateBack : PracticeEffect()
    data class ShowError(val message: String) : PracticeEffect()
    data object QuizCompleted : PracticeEffect()
}
