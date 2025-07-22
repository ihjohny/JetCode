package com.appsbase.jetcode.core.data.di

import com.appsbase.jetcode.core.data.remote.LearningApiService
import com.appsbase.jetcode.core.data.remote.LearningApiServiceImpl
import com.appsbase.jetcode.core.data.remote.PracticeApiService
import com.appsbase.jetcode.core.data.remote.PracticeApiServiceImpl
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
