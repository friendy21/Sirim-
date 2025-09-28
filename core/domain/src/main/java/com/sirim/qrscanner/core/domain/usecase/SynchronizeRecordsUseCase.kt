package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.repository.SynchronizationRepository

class SynchronizeRecordsUseCase(
    private val repository: SynchronizationRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.synchronize()
}
