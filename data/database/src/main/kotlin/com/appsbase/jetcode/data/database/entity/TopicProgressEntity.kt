package com.appsbase.jetcode.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "topic_progress")
@TypeConverters(Converters::class)
data class TopicProgressEntity(
    @PrimaryKey val id: String, // User ID + Topic ID
    val userId: String,
    val topicId: String,
    val currentMaterialIndex: Int,
    val updatedAt: Long,
)
