package com.sirim.scanner.sync

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class Scheduler(
    private val context: Context
) {
    fun scheduleSync() {
        val workRequest = PeriodicWorkRequestBuilder<SirimSyncWorker>(12, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    companion object {
        private const val WORK_NAME = "sirim_sync"
    }
}
