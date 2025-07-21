package com.appsbase.jetcode.core.common.util

import timber.log.Timber

suspend inline fun <T> fetchContent(
    contentType: String,
    apiCall: suspend () -> List<T>,
): List<T>? {
    return try {
        val content = apiCall()
        Timber.d("Successfully fetched ${content.size} $contentType")
        content
    } catch (e: Exception) {
        Timber.w(
            e,
            "Failed to fetch $contentType from remote, will continue with other content",
        )
        null
    }
}