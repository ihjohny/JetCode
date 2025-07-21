package com.appsbase.jetcode.core.data.remote

import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.Practice
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.model.Topic
import io.ktor.client.HttpClient

class LearningApiServiceImpl(
    private val httpClient: HttpClient
) : LearningApiService {

    companion object {
        private const val GITHUB_CONTENT_BASE = "repos/your-username/jetcode-content/contents"
        private const val SKILLS_ENDPOINT = "$GITHUB_CONTENT_BASE/skills.json"
        private const val TOPICS_ENDPOINT = "$GITHUB_CONTENT_BASE/topics.json"
        private const val LESSONS_ENDPOINT = "$GITHUB_CONTENT_BASE/lessons.json"
        private const val MATERIALS_ENDPOINT = "$GITHUB_CONTENT_BASE/materials.json"
        private const val PRACTICES_ENDPOINT = "$GITHUB_CONTENT_BASE/practices.json"
    }

    override suspend fun getSkills(): List<Skill> {
        // For now, return sample data. In production, this would fetch from GitHub
        return SampleData.getSampleSkills()
    }

    override suspend fun getTopicsByIds(topicIds: List<String>): List<Topic> {
        if (topicIds.isEmpty()) return emptyList()

        // Get all topics and filter by the requested IDs
        val allTopics = SampleData.getSampleTopics()
        return allTopics.filter { topic -> topic.id in topicIds }
    }

    override suspend fun getLessonsByIds(lessonIds: List<String>): List<Lesson> {
        if (lessonIds.isEmpty()) return emptyList()

        // Get all lessons and filter by the requested IDs
        val allLessons = SampleData.getSampleLessons()
        return allLessons.filter { lesson -> lesson.id in lessonIds }
    }

    override suspend fun getMaterialsByIds(materialIds: List<String>): List<Material> {
        if (materialIds.isEmpty()) return emptyList()

        // Get all materials and filter by the requested IDs
        val allMaterials = SampleData.getSampleMaterials()
        return allMaterials.filter { material -> material.id in materialIds }
    }

    override suspend fun getPracticesByIds(practiceIds: List<String>): List<Practice> {
        if (practiceIds.isEmpty()) return emptyList()

        // Get all practices and filter by the requested IDs
        val allPractices = SampleData.getSamplePractices()
        return allPractices.filter { practice -> practice.id in practiceIds }
    }
}
