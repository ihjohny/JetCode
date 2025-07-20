package com.appsbase.jetcode.core.data.mapper

import com.appsbase.jetcode.core.database.entity.LessonEntity
import com.appsbase.jetcode.core.database.entity.MaterialEntity
import com.appsbase.jetcode.core.database.entity.PracticeEntity
import com.appsbase.jetcode.core.database.entity.SkillEntity
import com.appsbase.jetcode.core.database.entity.TopicEntity
import com.appsbase.jetcode.core.domain.model.Difficulty
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.MaterialType
import com.appsbase.jetcode.core.domain.model.Practice
import com.appsbase.jetcode.core.domain.model.PracticeType
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.model.Topic

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
    isCompleted = isCompleted,
    progress = progress,
    topicIds = topicIds
)

fun Skill.toEntity(): SkillEntity = SkillEntity(
    id = id,
    name = name,
    description = description,
    iconUrl = iconUrl,
    difficulty = difficulty.name,
    estimatedDuration = estimatedDuration,
    isCompleted = isCompleted,
    progress = progress,
    topicIds = topicIds
)

fun TopicEntity.toDomain(): Topic = Topic(
    id = id,
    skillId = skillId,
    name = name,
    description = description,
    order = order,
    isUnlocked = isUnlocked,
    isCompleted = isCompleted,
    progress = progress,
    lessonIds = lessonIds
)

fun Topic.toEntity(): TopicEntity = TopicEntity(
    id = id,
    skillId = skillId,
    name = name,
    description = description,
    order = order,
    isUnlocked = isUnlocked,
    isCompleted = isCompleted,
    progress = progress,
    lessonIds = lessonIds
)

fun LessonEntity.toDomain(): Lesson = Lesson(
    id = id,
    topicId = topicId,
    title = title,
    description = description,
    order = order,
    duration = duration,
    isCompleted = isCompleted,
    score = score,
    materialIds = materialIds,
    practiceIds = practiceIds
)

fun Lesson.toEntity(): LessonEntity = LessonEntity(
    id = id,
    topicId = topicId,
    title = title,
    description = description,
    order = order,
    duration = duration,
    isCompleted = isCompleted,
    score = score,
    materialIds = materialIds,
    practiceIds = practiceIds
)

fun MaterialEntity.toDomain(): Material = Material(
    id = id,
    lessonId = lessonId,
    type = MaterialType.valueOf(type),
    title = title,
    content = content,
    order = order,
    metadata = metadata
)

fun Material.toEntity(): MaterialEntity = MaterialEntity(
    id = id,
    lessonId = lessonId,
    type = type.name,
    title = title,
    content = content,
    order = order,
    metadata = metadata
)

fun PracticeEntity.toDomain(): Practice = Practice(
    id = id,
    lessonId = lessonId,
    type = PracticeType.valueOf(type),
    question = question,
    options = options,
    correctAnswer = correctAnswer,
    explanation = explanation,
    difficulty = Difficulty.valueOf(difficulty),
    points = points
)

fun Practice.toEntity(): PracticeEntity = PracticeEntity(
    id = id,
    lessonId = lessonId,
    type = type.name,
    question = question,
    options = options,
    correctAnswer = correctAnswer,
    explanation = explanation,
    difficulty = difficulty.name,
    points = points
)
