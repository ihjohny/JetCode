package com.appsbase.jetcode

import android.app.Application
import com.appsbase.jetcode.di.appModules
import com.appsbase.jetcode.sync.SyncManager
import org.koin.android.ext.android.inject
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

    private val syncManager: SyncManager by inject()

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize Koin for dependency injection
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@JetCodeApplication)
            modules(appModules)
        }

        // Initialize background sync manager
        syncManager.initialize()

        Timber.d("JetCode Application initialized")
    }

    override fun onTerminate() {
        super.onTerminate()
        syncManager.destroy()
    }
}
