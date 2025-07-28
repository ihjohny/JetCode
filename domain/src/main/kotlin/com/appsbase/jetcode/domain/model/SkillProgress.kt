package com.appsbase.jetcode.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SkillProgress(
    val id: String,
    val userId: String,
    val skillId: String,
    val completedMaterial: Int,
    val totalMaterial: Int,
    val updatedAt: Long,
)
