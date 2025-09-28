package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.repository.SirimRecordRepository

class RefreshRecordsUseCase(
    private val repository: SirimRecordRepository
) {
    suspend operator fun invoke() {
        repository.refresh()
    }
}
