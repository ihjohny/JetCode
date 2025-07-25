package com.appsbase.jetcode.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "practice_sets")
@TypeConverters(Converters::class)
data class PracticeSetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val quizIds: List<String>,
)
