package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LessonProgress(
    val lessonId: String,
    val isCompleted: Boolean = false,
    val score: Int = 0,
    val attempts: Int = 0,
    val timeSpent: Int = 0, // in seconds
    val completedAt: String? = null,
)
