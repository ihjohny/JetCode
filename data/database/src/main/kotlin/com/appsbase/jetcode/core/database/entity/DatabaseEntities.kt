package com.appsbase.jetcode.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "skills")
@TypeConverters(Converters::class)
data class SkillEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val iconUrl: String?,
    val difficulty: String,
    val estimatedDuration: Int,
    val isCompleted: Boolean = false,
    val progress: Float = 0f,
    val topicIds: List<String> = emptyList()
)

@Entity(tableName = "topics")
@TypeConverters(Converters::class)
data class TopicEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val order: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val progress: Float = 0f,
    val lessonIds: List<String> = emptyList()
)

@Entity(tableName = "lessons")
@TypeConverters(Converters::class)
data class LessonEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val order: Int,
    val duration: Int,
    val isCompleted: Boolean = false,
    val score: Int? = null,
    val materialIds: List<String> = emptyList(),
    val practiceIds: List<String> = emptyList()
)

@Entity(tableName = "materials")
@TypeConverters(Converters::class)
data class MaterialEntity(
    @PrimaryKey val id: String,
    val type: String,
    val title: String,
    val content: String,
    val order: Int,
    val metadata: Map<String, String> = emptyMap()
)

@Entity(tableName = "practices")
@TypeConverters(Converters::class)
data class PracticeEntity(
    @PrimaryKey val id: String,
    val type: String,
    val question: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val explanation: String,
    val difficulty: String,
    val points: Int = 10
)

@Entity(tableName = "user_progress")
@TypeConverters(Converters::class)
data class UserProgressEntity(
    @PrimaryKey val userId: String,
    val totalPoints: Int = 0,
    val streak: Int = 0,
    val lastActivityDate: String? = null
)

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey val id: String, // userId_lessonId
    val userId: String,
    val lessonId: String,
    val isCompleted: Boolean = false,
    val score: Int = 0,
    val attempts: Int = 0,
    val timeSpent: Int = 0,
    val completedAt: String? = null
)

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromStringMap(value: Map<String, String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringMap(value: String): Map<String, String> {
        return Json.decodeFromString(value)
    }
}
