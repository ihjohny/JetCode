package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.SkillProgress
import com.appsbase.jetcode.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting skill progress for a specific user and skill
 */

class GetSkillProgressUseCase(
    private val progressRepository: ProgressRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(
        skillId: String,
    ): Flow<Result<SkillProgress?>> {
        return progressRepository.getSkillProgressById(
            skillId = skillId,
            userId = DummyUserId,
        ).flowOn(dispatcherProvider.io)
    }
}
