package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Practice(
    val id: String,
    val type: PracticeType,
    val question: String,
    val options: List<String> = emptyList(), // for MCQ
    val correctAnswer: String,
    val explanation: String,
    val difficulty: Difficulty,
    val points: Int = 10,
)
