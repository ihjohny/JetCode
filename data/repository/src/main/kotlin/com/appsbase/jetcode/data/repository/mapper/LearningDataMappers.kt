package com.appsbase.jetcode.data.repository.mapper

import com.appsbase.jetcode.data.database.entity.MaterialEntity
import com.appsbase.jetcode.data.database.entity.SkillEntity
import com.appsbase.jetcode.data.database.entity.TopicEntity
import com.appsbase.jetcode.domain.model.Difficulty
import com.appsbase.jetcode.domain.model.Material
import com.appsbase.jetcode.domain.model.MaterialType
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.model.Topic

/**
 * Mappers to convert between database entities and domain models
 */

fun SkillEntity.toDomain(): Skill = Skill(
    id = id,
    name = name,
    description = description,
    iconUrl = iconUrl,
    difficulty = Difficulty.valueOf(difficulty),
    estimatedDuration = estimatedDuration,
    topicIds = topicIds,
)

fun Skill.toEntity(): SkillEntity = SkillEntity(
    id = id,
    name = name,
    description = description,
    iconUrl = iconUrl,
    difficulty = difficulty.name,
    estimatedDuration = estimatedDuration,
    topicIds = topicIds,
)

fun TopicEntity.toDomain(): Topic = Topic(
    id = id,
    name = name,
    description = description,
    materialIds = materialIds,
    practiceSetId = practiceSetId,
    duration = duration,
)

fun Topic.toEntity(): TopicEntity = TopicEntity(
    id = id,
    name = name,
    description = description,
    materialIds = materialIds,
    practiceSetId = practiceSetId,
    duration = duration,
)

fun MaterialEntity.toDomain(): Material = Material(
    id = id,
    type = MaterialType.valueOf(type),
    title = title,
    content = content,
    metadata = metadata,
)

fun Material.toEntity(): MaterialEntity = MaterialEntity(
    id = id,
    type = type.name,
    title = title,
    content = content,
    metadata = metadata,
)
