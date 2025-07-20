package com.appsbase.jetcode.core.data.remote

import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.Practice
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.model.Topic

/**
 * API service for fetching learning content from GitHub
 */
interface LearningApiService {
    suspend fun getSkills(): List<Skill>
    suspend fun getTopics(skillId: String? = null): List<Topic>
    suspend fun getLessons(topicId: String? = null): List<Lesson>
    suspend fun getMaterials(lessonId: String? = null): List<Material>
    suspend fun getPractices(lessonId: String? = null): List<Practice>
}