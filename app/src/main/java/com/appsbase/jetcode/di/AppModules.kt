package com.appsbase.jetcode.di

import android.content.Context
import com.appsbase.jetcode.core.common.util.DefaultDispatcherProvider
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.core.data.remote.LearningApiService
import com.appsbase.jetcode.core.data.remote.LearningApiServiceImpl
import com.appsbase.jetcode.core.data.repository.LearningRepositoryImpl
import com.appsbase.jetcode.core.database.JetCodeDatabase
import com.appsbase.jetcode.core.domain.repository.LearningRepository
import com.appsbase.jetcode.core.domain.usecase.*
import com.appsbase.jetcode.core.network.NetworkClient
import com.appsbase.jetcode.feature.learning.di.learningModule
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Core Koin modules for dependency injection
 */

val coreModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}

val networkModule = module {
    single<HttpClient> {
        NetworkClient.create(
            baseUrl = "https://api.github.com/",
            enableLogging = true
        )
    }

    single<LearningApiService> {
        LearningApiServiceImpl(get())
    }
}

val databaseModule = module {
    single {
        JetCodeDatabase.create(androidContext())
    }

    single { get<JetCodeDatabase>().learningDao() }
    single { get<JetCodeDatabase>().userProgressDao() }
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
}

/**
 * All app modules combined
 */
val appModules = listOf(
    coreModule,
    networkModule,
    databaseModule,
    repositoryModule,
    useCaseModule,
    learningModule
)
