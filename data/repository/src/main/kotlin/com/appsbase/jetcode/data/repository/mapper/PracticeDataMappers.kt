package com.appsbase.jetcode.data.repository.mapper

import com.appsbase.jetcode.data.database.entity.PracticeSetEntity
import com.appsbase.jetcode.data.database.entity.QuizEntity
import com.appsbase.jetcode.domain.model.PracticeSet
import com.appsbase.jetcode.domain.model.Quiz
import com.appsbase.jetcode.domain.model.QuizType

/**
 * Mappers to convert between database entities and domain models
 */

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
