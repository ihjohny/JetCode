package com.appsbase.jetcode.data.remote.network

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.error.AppError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Extension functions for handling network responses safely
 */
suspend inline fun <reified T> HttpResponse.safeCall(): Result<T> {
    return try {
        if (status.isSuccess()) {
            val data = body<T>()
            Result.Success(data)
        } else {
            val error = when (status) {
                HttpStatusCode.Unauthorized -> AppError.BusinessError.Unauthorized
                HttpStatusCode.Forbidden -> AppError.BusinessError.Forbidden
                HttpStatusCode.NotFound -> AppError.DataError.NotFound
                else -> AppError.NetworkError.HttpError(
                    code = status.value, message = status.description
                )
            }
            Result.Error(error)
        }
    } catch (e: Exception) {
        Timber.e(e, "Network call failed")
        Result.Error(AppError.NetworkError.Unknown(e))
    }
}

/**
 * Extension function to execute network calls safely with Flow
 */
inline fun <reified T> safeNetworkCall(
    crossinline call: suspend () -> HttpResponse
): Flow<Result<T>> = flow {
    try {
        val response = call()
        emit(response.safeCall<T>())
    } catch (e: Exception) {
        Timber.e(e, "Network call exception")
        val error = when (e) {
            is UnknownHostException -> AppError.NetworkError.NoConnection
            is SocketTimeoutException -> AppError.NetworkError.Timeout
            else -> AppError.NetworkError.Unknown(e)
        }
        emit(Result.Error(error))
    }
}
