package com.appsbase.jetcode.data.repository.di

import com.appsbase.jetcode.data.repository.remote.LearningApiService
import com.appsbase.jetcode.data.repository.remote.LearningApiServiceImpl
import com.appsbase.jetcode.data.repository.remote.PracticeApiService
import com.appsbase.jetcode.data.repository.remote.PracticeApiServiceImpl
import org.koin.dsl.module

/**
 * Data module for dependency injection
 */
val dataModule = module {
    single<LearningApiService> {
        LearningApiServiceImpl(get())
    }

    single<PracticeApiService> {
        PracticeApiServiceImpl(get())
    }
}
