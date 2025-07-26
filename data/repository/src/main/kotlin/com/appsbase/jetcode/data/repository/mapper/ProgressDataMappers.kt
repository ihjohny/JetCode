package com.appsbase.jetcode.data.repository.mapper

import com.appsbase.jetcode.data.database.entity.TopicProgressEntity
import com.appsbase.jetcode.domain.model.TopicProgress

/**
 * Mappers to convert between database entities and domain models
 */

fun TopicProgressEntity.toDomain(): TopicProgress = TopicProgress(
    id = id,
    userId = userId,
    topicId = topicId,
    currentMaterialIndex = currentMaterialIndex,
    updatedAt = updatedAt,
)

fun TopicProgress.toEntity(): TopicProgressEntity = TopicProgressEntity(
    id = id,
    userId = userId,
    topicId = topicId,
    currentMaterialIndex = currentMaterialIndex,
    updatedAt = updatedAt,
)