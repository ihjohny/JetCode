package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class QuizType {
    MCQ, CODE_CHALLENGE, OUTPUT_PREDICTION, FILL_BLANK
}

@Serializable
data class Quiz(
    val id: String,
    val type: QuizType,
    val question: String,
    val options: List<String>?,
    val correctAnswer: String,
    val explanation: String?,
    val difficulty: Difficulty,
    val attributes: List<String>? = null,
) : Content()
