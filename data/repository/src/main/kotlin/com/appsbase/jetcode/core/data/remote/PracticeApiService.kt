package com.appsbase.jetcode.core.data.remote

import com.appsbase.jetcode.core.domain.model.PracticeSet
import com.appsbase.jetcode.core.domain.model.Quiz

/**
 * API service for fetching practice content from API
 */
interface PracticeApiService {
    suspend fun getPracticeSets(): List<PracticeSet>
    suspend fun getQuizzesByIds(quizIds: List<String>): List<Quiz>
}
