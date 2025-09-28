package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.repository.SynchronizationRepository

class GetLastSynchronizationTimestampUseCase(
    private val repository: SynchronizationRepository
) {
    suspend operator fun invoke(): Result<Long?> = repository.lastSyncedAt()
}
