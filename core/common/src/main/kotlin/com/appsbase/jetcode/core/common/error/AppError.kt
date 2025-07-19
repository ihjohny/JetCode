package com.appsbase.jetcode.core.common.error

/**
 * Base class for all application errors
 */
sealed class AppError : Exception() {

    /**
     * Network related errors
     */
    sealed class NetworkError : AppError() {
        data object NoConnection : NetworkError() {
            private fun readResolve(): Any = NoConnection
        }
        data object Timeout : NetworkError() {
            private fun readResolve(): Any = Timeout
        }
        data class HttpError(val code: Int, override val message: String) : NetworkError()
        data class Unknown(override val cause: Throwable?) : NetworkError()
    }

    /**
     * Data related errors
     */
    sealed class DataError : AppError() {
        data object NotFound : DataError() {
            private fun readResolve(): Any = NotFound
        }
        data object CacheError : DataError() {
            private fun readResolve(): Any = CacheError
        }
        data object DatabaseError : DataError() {
            private fun readResolve(): Any = DatabaseError
        }
        data class ParseError(override val cause: Throwable?) : DataError()
    }

    /**
     * Business logic errors
     */
    sealed class BusinessError : AppError() {
        data object Unauthorized : BusinessError() {
            private fun readResolve(): Any = Unauthorized
        }
        data object Forbidden : BusinessError() {
            private fun readResolve(): Any = Forbidden
        }
        data object ValidationError : BusinessError() {
            private fun readResolve(): Any = ValidationError
        }
        data class Custom(override val message: String) : BusinessError()
    }

    /**
     * UI related errors
     */
    sealed class UIError : AppError() {
        data object Unknown : UIError() {
            private fun readResolve(): Any = Unknown
        }
        data class DisplayError(override val message: String) : UIError()
    }
}

/**
 * Extension to get user-friendly error messages
 */
fun AppError.getUserMessage(): String = when (this) {
    is AppError.NetworkError.NoConnection -> "No internet connection"
    is AppError.NetworkError.Timeout -> "Request timed out"
    is AppError.NetworkError.HttpError -> "Server error: $message"
    is AppError.NetworkError.Unknown -> "Network error occurred"
    is AppError.DataError.NotFound -> "Data not found"
    is AppError.DataError.CacheError -> "Cache error occurred"
    is AppError.DataError.DatabaseError -> "Database error occurred"
    is AppError.DataError.ParseError -> "Data parsing error"
    is AppError.BusinessError.Unauthorized -> "Unauthorized access"
    is AppError.BusinessError.Forbidden -> "Access forbidden"
    is AppError.BusinessError.ValidationError -> "Validation error"
    is AppError.BusinessError.Custom -> message
    is AppError.UIError.Unknown -> "Unknown error occurred"
    is AppError.UIError.DisplayError -> message
}
