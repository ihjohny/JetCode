package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting all available skills
 */
class GetSkillsUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(): Flow<Result<List<Skill>>> {
        return learningRepository.getSkills()
            .flowOn(dispatcherProvider.io)
    }
}

/**
 * Use case for getting a specific skill by ID
 */
class GetSkillByIdUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(skillId: String): Flow<Result<Skill>> {
        return learningRepository.getSkillById(skillId)
            .flowOn(dispatcherProvider.io)
    }
}

/**
 * Use case for searching learning content
 */
class SearchContentUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(query: String): Flow<Result<List<Any>>> {
        return learningRepository.searchContent(query)
            .flowOn(dispatcherProvider.io)
    }
}

/**
 * Use case for syncing content from remote
 */
class SyncContentUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(): Result<Unit> {
        return learningRepository.syncContent()
    }
}
