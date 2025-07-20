package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    val id: String,
    val skillId: String,
    val name: String,
    val description: String,
    val lessonIds: List<String> = emptyList(),
    val order: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val progress: Float = 0f
)
