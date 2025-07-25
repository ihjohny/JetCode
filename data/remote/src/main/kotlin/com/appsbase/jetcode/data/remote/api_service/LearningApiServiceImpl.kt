package com.appsbase.jetcode.data.remote.api_service

import com.appsbase.jetcode.data.remote.network.Constants.GITHUB_CONTENT_BASE
import com.appsbase.jetcode.domain.model.Material
import com.appsbase.jetcode.domain.model.SampleData
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.model.Topic
import io.ktor.client.HttpClient

class LearningApiServiceImpl(
    private val httpClient: HttpClient
) : LearningApiService {

    companion object {
        private const val SKILLS_ENDPOINT = "$GITHUB_CONTENT_BASE/skills.json"
        private const val TOPICS_ENDPOINT = "$GITHUB_CONTENT_BASE/topics.json"
        private const val MATERIALS_ENDPOINT = "$GITHUB_CONTENT_BASE/materials.json"
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

    override suspend fun getMaterialsByIds(materialIds: List<String>): List<Material> {
        if (materialIds.isEmpty()) return emptyList()

        // Get all materials and filter by the requested IDs
        val allMaterials = SampleData.getSampleMaterials()
        return allMaterials.filter { material -> material.id in materialIds }
    }
}
