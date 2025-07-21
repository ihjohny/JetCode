package com.appsbase.jetcode.core.domain.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.Practice
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
     * Get skill by ID
     */
    fun getSkillById(skillId: String): Flow<Result<Skill>>

    /**
     * Get topic by ID
     */
    fun getTopicById(topicId: String): Flow<Result<Topic>>

    /**
     * Get topics by list of IDs
     */
    fun getTopicsByIds(topicIds: List<String>): Flow<Result<List<Topic>>>

    /**
     * Get lessons by list of IDs
     */
    fun getLessonsByIds(lessonIds: List<String>): Flow<Result<List<Lesson>>>

    /**
     * Get lesson by ID
     */
    fun getLessonById(lessonId: String): Flow<Result<Lesson>>

    /**
     * Get materials by list of IDs
     */
    fun getMaterialsByIds(materialIds: List<String>): Flow<Result<List<Material>>>

    /**
     * Get practices by list of IDs
     */
    fun getPracticesByIds(practiceIds: List<String>): Flow<Result<List<Practice>>>

    /**
     * Sync content from remote source
     */
    suspend fun syncContent(): Result<Unit>

    /**
     * Search content
     */
    fun searchContent(query: String): Flow<Result<List<Any>>> // Can be Skill, Topic, or Lesson
}
