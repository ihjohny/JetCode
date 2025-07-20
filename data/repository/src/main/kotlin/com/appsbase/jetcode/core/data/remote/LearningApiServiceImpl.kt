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

    override suspend fun getTopics(skillId: String?): List<Topic> {
        // For now, return sample data. In production, this would fetch from GitHub
        val allTopics = SampleData.getSampleTopics()
        return if (skillId != null) {
            allTopics.filter { it.skillId == skillId }
        } else {
            allTopics
        }
    }

    override suspend fun getLessons(topicId: String?): List<Lesson> {
        // For now, return sample data. In production, this would fetch from GitHub
        val allLessons = SampleData.getSampleLessons()
        return if (topicId != null) {
            allLessons.filter { it.topicId == topicId }
        } else {
            allLessons
        }
    }

    override suspend fun getMaterials(lessonId: String?): List<Material> {
        // For now, return sample data. In production, this would fetch from GitHub
        val allMaterials = SampleData.getSampleMaterials()
        return if (lessonId != null) {
            allMaterials.filter { it.lessonId == lessonId }
        } else {
            allMaterials
        }
    }

    override suspend fun getPractices(lessonId: String?): List<Practice> {
        // For now, return sample data. In production, this would fetch from GitHub
        val allPractices = SampleData.getSamplePractices()
        return if (lessonId != null) {
            allPractices.filter { it.lessonId == lessonId }
        } else {
            allPractices
        }
    }
}
