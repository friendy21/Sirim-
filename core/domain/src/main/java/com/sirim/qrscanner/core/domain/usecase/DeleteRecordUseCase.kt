package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.domain.repository.SirimRecordRepository

class DeleteRecordUseCase(
    private val repository: SirimRecordRepository
) {
    suspend operator fun invoke(record: SirimRecord) {
        repository.delete(record)
    }
}
