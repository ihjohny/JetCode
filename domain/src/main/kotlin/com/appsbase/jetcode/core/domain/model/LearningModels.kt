package com.appsbase.jetcode.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain models for the learning platform
 */

@Serializable
data class Skill(
    val id: String,
    val name: String,
    val description: String,
    val iconUrl: String?,
    val topics: List<Topic> = emptyList(),
    val difficulty: Difficulty,
    val estimatedDuration: Int, // in minutes
    val isCompleted: Boolean = false,
    val progress: Float = 0f // 0.0 to 1.0
)

@Serializable
data class Topic(
    val id: String,
    val skillId: String,
    val name: String,
    val description: String,
    val lessons: List<Lesson> = emptyList(),
    val order: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val progress: Float = 0f
)

@Serializable
data class Lesson(
    val id: String,
    val topicId: String,
    val title: String,
    val description: String,
    val materials: List<Material> = emptyList(),
    val practices: List<Practice> = emptyList(),
    val order: Int,
    val duration: Int, // in minutes
    val isCompleted: Boolean = false,
    val score: Int? = null
)

@Serializable
data class Material(
    val id: String,
    val lessonId: String,
    val type: MaterialType,
    val title: String,
    val content: String,
    val order: Int,
    val metadata: Map<String, String> = emptyMap()
)

@Serializable
data class Practice(
    val id: String,
    val lessonId: String,
    val type: PracticeType,
    val question: String,
    val options: List<String> = emptyList(), // for MCQ
    val correctAnswer: String,
    val explanation: String,
    val difficulty: Difficulty,
    val points: Int = 10
)

@Serializable
enum class MaterialType {
    TEXT, MARKDOWN, CODE, IMAGE, VIDEO
}

@Serializable
enum class PracticeType {
    MCQ, CODE_CHALLENGE, OUTPUT_PREDICTION, FILL_BLANK
}

@Serializable
enum class Difficulty {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}

@Serializable
data class UserProgress(
    val userId: String,
    val skillProgresses: List<SkillProgress> = emptyList(),
    val totalPoints: Int = 0,
    val streak: Int = 0,
    val lastActivityDate: String? = null
)

@Serializable
data class SkillProgress(
    val skillId: String,
    val topicProgresses: List<TopicProgress> = emptyList(),
    val completedLessons: Int = 0,
    val totalLessons: Int = 0,
    val points: Int = 0,
    val isCompleted: Boolean = false
)

@Serializable
data class TopicProgress(
    val topicId: String,
    val lessonProgresses: List<LessonProgress> = emptyList(),
    val isCompleted: Boolean = false
)

@Serializable
data class LessonProgress(
    val lessonId: String,
    val isCompleted: Boolean = false,
    val score: Int = 0,
    val attempts: Int = 0,
    val timeSpent: Int = 0, // in seconds
    val completedAt: String? = null
)
