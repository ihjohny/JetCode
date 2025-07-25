package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

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
