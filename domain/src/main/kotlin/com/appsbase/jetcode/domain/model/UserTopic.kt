package com.appsbase.jetcode.domain.model

data class UserTopic(
    val topic: Topic,
    val currentMaterialIndex: Int,
) {
    val progressValue: Float
        get() = when {
            topic.materialIds.isEmpty() -> NoProgress.toFloat()
            currentMaterialIndex <= NoProgress -> NoProgress.toFloat()
            else -> currentMaterialIndex.coerceAtLeast(0).toFloat() / topic.materialIds.size
        }
}