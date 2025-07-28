package com.appsbase.jetcode.data.repository.mapper

import com.appsbase.jetcode.data.database.entity.PracticeResultEntity
import com.appsbase.jetcode.domain.model.PracticeSetResult
import com.appsbase.jetcode.domain.model.PracticeSessionStatistics

/**
 * Mappers to convert between database entities and domain models for practice results
 */

fun PracticeResultEntity.toDomain(): PracticeSetResult = PracticeSetResult(
    id = id,
    userId = userId,
    practiceSetId = practiceSetId,
    practiceSessionStatistics = PracticeSessionStatistics(
        totalQuizzes = totalQuizzes,
        correctAnswers = correctAnswers,
        averageTimeSeconds = averageTimeSeconds,
    ),
    updatedAt = updatedAt,
)

fun PracticeSetResult.toEntity(): PracticeResultEntity = PracticeResultEntity(
    id = id,
    userId = userId,
    practiceSetId = practiceSetId,
    totalQuizzes = practiceSessionStatistics.totalQuizzes,
    correctAnswers = practiceSessionStatistics.correctAnswers,
    averageTimeSeconds = practiceSessionStatistics.averageTimeSeconds,
    updatedAt = updatedAt,
)
