package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Skill(
    val id: String,
    val name: String,
    val description: String,
    val iconUrl: String?,
    val topics: List<Topic> = emptyList(),
    val difficulty: Difficulty,
    val estimatedDuration: Int, // in minutes
    val isCompleted: Boolean = false,
    val progress: Float = 0f // 0.0 to 1.0
)
