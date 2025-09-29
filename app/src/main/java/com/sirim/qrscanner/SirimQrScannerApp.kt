package com.sirim.qrscanner

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber
import com.sirim.qrscanner.work.SyncScheduler
import androidx.hilt.work.HiltWorkerFactory

@HiltAndroidApp
class SirimQrScannerApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var syncScheduler: SyncScheduler

    override fun onCreate() {
        super.onCreate()
        if (Timber.forest().isEmpty()) {
            Timber.plant(Timber.DebugTree())
        }
        syncScheduler.schedule()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
