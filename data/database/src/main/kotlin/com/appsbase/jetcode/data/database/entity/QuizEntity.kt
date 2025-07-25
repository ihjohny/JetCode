package com.appsbase.jetcode.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "quizzes")
@TypeConverters(Converters::class)
data class QuizEntity(
    @PrimaryKey val id: String,
    val type: String,
    val question: String,
    val options: List<String>?,
    val correctAnswer: String,
    val explanation: String?,
)