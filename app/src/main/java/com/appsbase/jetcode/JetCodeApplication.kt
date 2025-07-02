package com.appsbase.jetcode

import android.app.Application
import com.appsbase.jetcode.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/**
 * Main application class for JetCode
 * Initializes Koin DI, Timber logging, and other app-wide components
 */
class JetCodeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging
        if (com.appsbase.jetcode.BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize Koin for dependency injection
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@JetCodeApplication)
            modules(appModules)
        }

        Timber.d("JetCode Application initialized")
    }
}
