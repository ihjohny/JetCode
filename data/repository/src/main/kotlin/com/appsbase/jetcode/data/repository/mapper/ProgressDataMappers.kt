package com.appsbase.jetcode.data.repository.mapper

import com.appsbase.jetcode.data.database.entity.SkillProgressEntity
import com.appsbase.jetcode.data.database.entity.TopicProgressEntity
import com.appsbase.jetcode.domain.model.SkillProgress
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

fun SkillProgressEntity.toDomain(): SkillProgress = SkillProgress(
    id = id,
    userId = userId,
    skillId = skillId,
    completedMaterial = completedMaterial,
    totalMaterial = totalMaterial,
    updatedAt = updatedAt,
)

fun SkillProgress.toEntity(): SkillProgressEntity = SkillProgressEntity(
    id = id,
    userId = userId,
    skillId = skillId,
    completedMaterial = completedMaterial,
    totalMaterial = totalMaterial,
    updatedAt = updatedAt,
)
