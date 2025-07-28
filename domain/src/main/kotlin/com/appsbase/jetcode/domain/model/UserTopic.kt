package com.appsbase.jetcode.domain.model

data class UserTopic(
    val topic: Topic,
    val currentMaterialIndex: Int,
) {
    val progressValue: Float
        get() = if (topic.materialIds.isNotEmpty()) {
            (currentMaterialIndex + 1f) / topic.materialIds.size
        } else {
            0f
        }
}