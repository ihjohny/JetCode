package com.appsbase.jetcode.data.remote.di

import com.appsbase.jetcode.data.remote.network.Constants
import com.appsbase.jetcode.data.remote.network.NetworkClient
import com.appsbase.jetcode.data.remote.api_service.LearningApiService
import com.appsbase.jetcode.data.remote.api_service.LearningApiServiceImpl
import com.appsbase.jetcode.data.remote.api_service.PracticeApiService
import com.appsbase.jetcode.data.remote.api_service.PracticeApiServiceImpl
import io.ktor.client.HttpClient
import org.koin.dsl.module

/**
 * api service module for dependency injection
 */
val remoteModule = module {
    single<HttpClient> {
        NetworkClient.create(
            baseUrl = Constants.BASE_URL,
            enableLogging = true,
        )
    }

    single<LearningApiService> {
        LearningApiServiceImpl(get())
    }

    single<PracticeApiService> {
        PracticeApiServiceImpl(get())
    }
}
