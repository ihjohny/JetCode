package com.appsbase.jetcode.core.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.model.Topic
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

/**
 * Use case for getting topics for a specific skill
 */
class GetTopicsForSkillUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(skillId: String): Flow<Result<List<Topic>>> {
        return learningRepository.getTopicsForSkill(skillId)
            .flowOn(dispatcherProvider.io)
    }
}

/**
 * Use case for getting a lesson by ID with materials and practices
 */
class GetLessonByIdUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(lessonId: String): Flow<Result<Lesson>> {
        return learningRepository.getLessonById(lessonId)
            .flowOn(dispatcherProvider.io)
    }
}

/**
 * Use case for getting lessons for a specific topic
 */
class GetLessonsForTopicUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(topicId: String): Flow<Result<List<Lesson>>> {
        return learningRepository.getLessonsForTopic(topicId)
            .flowOn(dispatcherProvider.io)
    }
}
