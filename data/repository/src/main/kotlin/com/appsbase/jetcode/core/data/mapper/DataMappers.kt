package com.appsbase.jetcode.core.data.mapper

import com.appsbase.jetcode.core.database.entity.MaterialEntity
import com.appsbase.jetcode.core.database.entity.PracticeSetEntity
import com.appsbase.jetcode.core.database.entity.QuizEntity
import com.appsbase.jetcode.core.database.entity.SkillEntity
import com.appsbase.jetcode.core.database.entity.TopicEntity
import com.appsbase.jetcode.core.domain.model.Difficulty
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.MaterialType
import com.appsbase.jetcode.core.domain.model.PracticeSet
import com.appsbase.jetcode.core.domain.model.Quiz
import com.appsbase.jetcode.core.domain.model.QuizType
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

fun PracticeSetEntity.toDomain(): PracticeSet = PracticeSet(
    id = id,
    name = name,
    description = description,
    quizIds = quizIds,
)

fun PracticeSet.toEntity(): PracticeSetEntity = PracticeSetEntity(
    id = id,
    name = name,
    description = description,
    quizIds = quizIds,
)

fun QuizEntity.toDomain(): Quiz = Quiz(
    id = id,
    type = QuizType.valueOf(type),
    question = question,
    options = options,
    correctAnswer = correctAnswer,
    explanation = explanation,
)

fun Quiz.toEntity(): QuizEntity = QuizEntity(
    id = id,
    type = type.name,
    question = question,
    options = options,
    correctAnswer = correctAnswer,
    explanation = explanation,
)
