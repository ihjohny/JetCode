package com.appsbase.jetcode.di

import com.appsbase.jetcode.core.common.util.DefaultDispatcherProvider
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.data.di.dataModule
import com.appsbase.jetcode.core.data.repository.LearningRepositoryImpl
import com.appsbase.jetcode.core.database.di.databaseModule
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import com.appsbase.jetcode.core.domain.usecase.*
import com.appsbase.jetcode.core.network.di.networkModule
import com.appsbase.jetcode.feature.learning.di.learningModule
import org.koin.dsl.module

/**
 * Core Koin modules for dependency injection
 */

val coreModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}

val repositoryModule = module {
    single<LearningRepository> {
        LearningRepositoryImpl(
            learningDao = get(),
            apiService = get()
        )
    }

    // UserProgressRepository will be implemented similarly
}

val useCaseModule = module {
    factory {
        GetSkillsUseCase(
            learningRepository = get(),
            dispatcherProvider = get()
        )
    }

    factory {
        GetSkillByIdUseCase(
            learningRepository = get(),
            dispatcherProvider = get()
        )
    }

    factory {
        SearchContentUseCase(
            learningRepository = get(),
            dispatcherProvider = get()
        )
    }

    factory {
        SyncContentUseCase(
            learningRepository = get(),
            dispatcherProvider = get()
        )
    }

    factory {
        GetUserProgressUseCase(
            userProgressRepository = get(),
            dispatcherProvider = get()
        )
    }

    factory {
        CompleteLessonUseCase(
            userProgressRepository = get(),
            dispatcherProvider = get()
        )
    }

    factory {
        UpdateLessonProgressUseCase(
            userProgressRepository = get(),
            dispatcherProvider = get()
        )
    }

    factory {
        SyncUserProgressUseCase(
            userProgressRepository = get(),
            dispatcherProvider = get()
        )
    }

    factory {
        GetTopicsForSkillUseCase(
            learningRepository = get(),
            dispatcherProvider = get()
        )
    }

    factory {
        GetLessonByIdUseCase(
            learningRepository = get(),
            dispatcherProvider = get()
        )
    }

    factory {
        GetLessonsForTopicUseCase(
            learningRepository = get(),
            dispatcherProvider = get()
        )
    }
}

/**
 * All app modules combined
 */
val appModules = listOf(
    coreModule,
    networkModule,
    dataModule,
    databaseModule,
    repositoryModule,
    useCaseModule,
    learningModule
)
