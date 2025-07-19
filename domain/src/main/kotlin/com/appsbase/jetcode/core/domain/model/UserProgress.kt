package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProgress(
    val userId: String,
    val skillProgresses: List<SkillProgress> = emptyList(),
    val totalPoints: Int = 0,
    val streak: Int = 0,
    val lastActivityDate: String? = null
)
