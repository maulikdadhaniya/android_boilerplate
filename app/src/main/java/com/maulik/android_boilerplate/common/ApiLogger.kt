package com.maulik.android_boilerplate.common

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * Custom logger for API requests and responses using Timber.
 * This logger will only work in debug builds when Timber is planted with a debug tree.
 */
class ApiLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Timber.Forest.tag("API").d(message)
    }
}