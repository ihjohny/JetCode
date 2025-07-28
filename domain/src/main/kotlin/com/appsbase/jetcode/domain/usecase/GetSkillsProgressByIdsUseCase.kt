package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.SkillProgress
import com.appsbase.jetcode.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting skill progress for multiple skills for a specific user
 */
class GetSkillsProgressByIdsUseCase(
    private val progressRepository: ProgressRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(
        skillIds: List<String>,
    ): Flow<Result<List<SkillProgress>>> {
        return progressRepository.getSkillsProgressByIds(
            skillIds = skillIds,
            userId = DummyUserId,
        ).flowOn(dispatcherProvider.io)
    }
}
