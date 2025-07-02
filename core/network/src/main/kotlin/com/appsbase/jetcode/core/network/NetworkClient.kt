package com.appsbase.jetcode.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * Network client configuration
 */
object NetworkClient {

    fun create(
        baseUrl: String = "https://api.github.com/",
        enableLogging: Boolean = true
    ): HttpClient {
        return HttpClient(Android) {
            // JSON serialization
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            // Logging
            if (enableLogging) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Timber.d("Ktor: $message")
                        }
                    }
                    level = LogLevel.BODY
                }
            }

            // Authentication
            install(Auth) {
                bearer {
                    loadTokens {
                        // Load tokens from secure storage
                        // This will be implemented with DataStore
                        BearerTokens("", "")
                    }
                    refreshTokens {
                        // Refresh token logic
                        BearerTokens("", "")
                    }
                }
            }

            // Default request configuration
            defaultRequest {
                url(baseUrl)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Accept, ContentType.Application.Json)
                header("User-Agent", "JetCode-Android/1.0")
            }

            // Engine configuration
            engine {
                connectTimeout = 30_000
                socketTimeout = 30_000
            }
        }
    }
}
