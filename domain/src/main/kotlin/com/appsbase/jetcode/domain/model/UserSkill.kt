package com.appsbase.jetcode.domain.model

data class UserSkill(
    val skill: Skill,
    val completedMaterial: Int,
    val totalMaterial: Int,
) {
    val progressValue: Float
        get() = if (totalMaterial > 0) {
            completedMaterial.toFloat() / totalMaterial
        } else {
            0f
        }

    val progressPercentageValue: String
        get() = "${(progressValue * 100).toInt()}"
}