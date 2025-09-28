package com.sirim.qrscanner.core.domain.repository

interface SynchronizationRepository {
    suspend fun synchronize(): Result<Unit>
    suspend fun lastSyncedAt(): Result<Long?>
}
