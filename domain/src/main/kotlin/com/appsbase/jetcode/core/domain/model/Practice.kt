package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class PracticeType {
    MCQ, CODE_CHALLENGE, OUTPUT_PREDICTION, FILL_BLANK
}

@Serializable
data class Practice(
    val id: String,
    val type: PracticeType,
    val question: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val explanation: String,
    val difficulty: Difficulty,
    val points: Int = 10,
) : Content()
