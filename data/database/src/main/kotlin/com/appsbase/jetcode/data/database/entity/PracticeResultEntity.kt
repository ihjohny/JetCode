package com.appsbase.jetcode.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "practice_results")
@TypeConverters(Converters::class)
data class PracticeResultEntity(
    @PrimaryKey val id: String, // user id + practice set id
    val userId: String,
    val practiceSetId: String,
    val totalQuizzes: Int,
    val correctAnswers: Int,
    val averageTimeSeconds: Float,
    val updatedAt: Long,
)
