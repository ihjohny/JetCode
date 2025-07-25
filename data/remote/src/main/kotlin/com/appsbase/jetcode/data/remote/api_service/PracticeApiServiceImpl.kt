package com.appsbase.jetcode.data.remote.api_service

import com.appsbase.jetcode.data.remote.network.Constants.GITHUB_CONTENT_BASE
import com.appsbase.jetcode.domain.model.PracticeSet
import com.appsbase.jetcode.domain.model.Quiz
import com.appsbase.jetcode.domain.model.SampleData
import io.ktor.client.HttpClient

class PracticeApiServiceImpl(
    private val httpClient: HttpClient
) : PracticeApiService {

    companion object {
        private const val PRACTICE_SETS_ENDPOINT = "$GITHUB_CONTENT_BASE/practice-sets.json"
        private const val QUIZZES_ENDPOINT = "$GITHUB_CONTENT_BASE/quizzes.json"
    }

    override suspend fun getPracticeSets(): List<PracticeSet> {
        // For now, return sample data. In production, this would fetch from GitHub
        return SampleData.getSamplePracticeSets()
    }

    override suspend fun getQuizzesByIds(quizIds: List<String>): List<Quiz> {
        if (quizIds.isEmpty()) return emptyList()

        // Get all quizzes and filter by the requested IDs
        val allQuizzes = SampleData.getSamplePractices()
        return allQuizzes.filter { quiz -> quiz.id in quizIds }
    }
}
