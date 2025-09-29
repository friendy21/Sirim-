package com.sirim.qrscanner.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sirim.qrscanner.core.domain.repository.SynchronizationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class RecordSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val synchronizationRepository: SynchronizationRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val result = synchronizationRepository.synchronize()
        return when {
            result.isSuccess -> {
                Timber.d("Synchronization completed")
                Result.success()
            }
            result.exceptionOrNull() is IllegalStateException -> {
                Timber.w(result.exceptionOrNull(), "Skipping sync because no session token is available")
                Result.success()
            }
            else -> {
                Timber.e(result.exceptionOrNull(), "Synchronization failed")
                Result.retry()
            }
        }
    }
}
