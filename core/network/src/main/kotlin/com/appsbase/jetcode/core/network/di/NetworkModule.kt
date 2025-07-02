package com.appsbase.jetcode.core.network.di

import com.appsbase.jetcode.core.network.NetworkClient
import io.ktor.client.HttpClient
import org.koin.dsl.module

/**
 * Network module for dependency injection
 */
val networkModule = module {
    single<HttpClient> {
        NetworkClient.create(
            baseUrl = "https://api.github.com/",
            enableLogging = true
        )
    }
}
