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
    suspend fun getTopicsByIds(topicIds: List<String>): List<Topic>
    suspend fun getLessonsByIds(lessonIds: List<String>): List<Lesson>
    suspend fun getMaterialsByIds(materialIds: List<String>): List<Material>
    suspend fun getPracticesByIds(practiceIds: List<String>): List<Practice>
}