package com.appsbase.jetcode.domain.repository

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.domain.model.SkillProgress
import com.appsbase.jetcode.domain.model.TopicProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    suspend fun upsertTopicProgress(progress: TopicProgress): Result<Unit>

    fun getTopicProgressById(
        topicId: String,
        userId: String,
    ): Flow<Result<TopicProgress?>>

    fun getTopicsProgressByIds(
        topicIds: List<String>,
        userId: String,
    ): Flow<Result<List<TopicProgress>>>

    suspend fun upsertSkillProgress(progress: SkillProgress): Result<Unit>

    fun getSkillProgressById(
        skillId: String,
        userId: String,
    ): Flow<Result<SkillProgress?>>

    fun getAllSkillsProgress(userId: String): Flow<Result<List<SkillProgress>>>
}
