package com.appsbase.jetcode.data.remote

import com.appsbase.jetcode.domain.model.PracticeSet
import com.appsbase.jetcode.domain.model.Quiz

/**
 * API service for fetching practice content from API
 */
interface PracticeApiService {
    suspend fun getPracticeSets(): List<PracticeSet>
    suspend fun getQuizzesByIds(quizIds: List<String>): List<Quiz>
}
