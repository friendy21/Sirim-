package com.sirim.qrscanner.work

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncScheduler @Inject constructor(
    private val workManager: WorkManager
) {

    fun schedule() {
        val request = PeriodicWorkRequestBuilder<RecordSyncWorker>(12, TimeUnit.HOURS)
            .addTag(WORK_TAG)
            .build()
        workManager.enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, request)
    }

    companion object {
        private const val WORK_TAG = "sirim-record-sync"
    }
}
