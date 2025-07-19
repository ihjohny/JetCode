package com.appsbase.jetcode.core.domain.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.model.Topic
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for learning content
 */
interface LearningRepository {

    /**
     * Get all available skills
     */
    fun getSkills(): Flow<Result<List<Skill>>>

    /**
     * Get skill by ID with topics
     */
    fun getSkillById(skillId: String): Flow<Result<Skill>>

    /**
     * Get topics for a skill
     */
    fun getTopicsForSkill(skillId: String): Flow<Result<List<Topic>>>

    /**
     * Get lessons for a topic
     */
    fun getLessonsForTopic(topicId: String): Flow<Result<List<Lesson>>>

    /**
     * Get lesson by ID with materials and practices
     */
    fun getLessonById(lessonId: String): Flow<Result<Lesson>>

    /**
     * Sync content from remote source
     */
    suspend fun syncContent(): Result<Unit>

    /**
     * Search content
     */
    fun searchContent(query: String): Flow<Result<List<Any>>> // Can be Skill, Topic, or Lesson
}
