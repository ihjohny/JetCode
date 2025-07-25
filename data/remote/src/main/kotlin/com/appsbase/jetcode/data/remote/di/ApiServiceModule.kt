package com.appsbase.jetcode.data.remote.di

import com.appsbase.jetcode.data.remote.api_service.LearningApiService
import com.appsbase.jetcode.data.remote.api_service.LearningApiServiceImpl
import com.appsbase.jetcode.data.remote.api_service.PracticeApiService
import com.appsbase.jetcode.data.remote.api_service.PracticeApiServiceImpl
import org.koin.dsl.module

/**
 * api service module for dependency injection
 */
val apiServiceModule = module {
    single<LearningApiService> {
        LearningApiServiceImpl(get())
    }

    single<PracticeApiService> {
        PracticeApiServiceImpl(get())
    }
}
