package com.appsbase.jetcode.domain.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.domain.model.TopicProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    suspend fun upsertProgress(progress: TopicProgress): Result<Unit>

    fun getProgressByTopicAndUser(topicId: String, userId: String): Flow<Result<TopicProgress?>>

    fun getProgressByTopicsIdsAndUser(topicIds: List<String>, userId: String): Flow<Result<List<TopicProgress>>>
}