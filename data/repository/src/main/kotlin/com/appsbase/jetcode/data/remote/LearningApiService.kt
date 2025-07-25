package com.appsbase.jetcode.data.remote

import com.appsbase.jetcode.domain.model.Material
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.model.Topic

/**
 * API service for fetching learning content from API
 */
interface LearningApiService {
    suspend fun getSkills(): List<Skill>
    suspend fun getTopicsByIds(topicIds: List<String>): List<Topic>
    suspend fun getMaterialsByIds(materialIds: List<String>): List<Material>
}