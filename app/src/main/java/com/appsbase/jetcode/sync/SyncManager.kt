package com.appsbase.jetcode.sync

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.domain.usecase.SyncContentUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Manages background content synchronization when the app starts
 * Observes app lifecycle to trigger sync when app comes to foreground
 */
class SyncManager(
    private val syncContentUseCase: SyncContentUseCase,
) : DefaultLifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var hasSyncedInSession = false

    fun initialize() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        Timber.d("SyncManager initialized")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Trigger sync only once per app session when app comes to foreground
        if (!hasSyncedInSession) {
            triggerBackgroundSync()
            hasSyncedInSession = true
        }
    }

    private fun triggerBackgroundSync() {
        scope.launch {
            try {
                Timber.d("Starting background content sync...")
                when (val result = syncContentUseCase()) {
                    is Result.Success -> {
                        Timber.d("Background content sync completed successfully")
                    }

                    is Result.Error -> {
                        Timber.w("Background content sync failed: ${result.exception.message}")
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Unexpected error during background sync")
            }
        }
    }

    fun destroy() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        scope.cancel()
        Timber.d("SyncManager destroyed")
    }
}
