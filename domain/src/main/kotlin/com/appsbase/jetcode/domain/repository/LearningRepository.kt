package com.appsbase.jetcode.domain.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.domain.model.Content
import com.appsbase.jetcode.domain.model.Material
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.model.Topic
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
     * Get materials by list of IDs
     */
    fun getMaterialsByIds(materialIds: List<String>): Flow<Result<List<Material>>>

    /**
     * Sync content from remote source
     */
    suspend fun syncLearningContent(): Result<Unit>

    /**
     * Search content
     */
    fun searchLearningContent(query: String): Flow<Result<List<Content>>>
}
