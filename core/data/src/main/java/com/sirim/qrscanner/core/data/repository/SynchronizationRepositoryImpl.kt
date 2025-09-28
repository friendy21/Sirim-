package com.sirim.qrscanner.core.data.repository

import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.data.source.local.SirimRecordLocalDataSource
import com.sirim.qrscanner.core.data.source.local.TokenStorage
import com.sirim.qrscanner.core.data.source.remote.SirimRecordRemoteDataSource
import com.sirim.qrscanner.core.domain.repository.SynchronizationRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.time.Instant

class SynchronizationRepositoryImpl(
    private val tokenStorage: TokenStorage,
    private val remoteDataSource: SirimRecordRemoteDataSource,
    private val localDataSource: SirimRecordLocalDataSource,
    private val dispatcherProvider: DispatcherProvider
) : SynchronizationRepository {

    override suspend fun synchronize(): Result<Unit> = withContext(dispatcherProvider.io) {
        val token = tokenStorage.token.firstOrNull()
            ?: return@withContext Result.failure(IllegalStateException("No token available"))
        return@withContext runCatching {
            val pending = localDataSource.unsynced()
            if (pending.isNotEmpty()) {
                val synced = remoteDataSource.pushRecords(token, pending)
                synced.forEach { localDataSource.upsert(it.copy(isSynced = true)) }
            }
            val remoteRecords = remoteDataSource.fetchRecords(token)
            remoteRecords.forEach { localDataSource.upsert(it.copy(isSynced = true)) }
            val timestamp = Instant.now().toEpochMilli()
            tokenStorage.updateLastSync(timestamp)
        }
    }

    override suspend fun lastSyncedAt(): Result<Long?> = withContext(dispatcherProvider.io) {
        runCatching { tokenStorage.lastSync().firstOrNull() }
    }
}
