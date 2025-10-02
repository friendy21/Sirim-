package com.sirim.scanner.domain.usecase

import com.sirim.scanner.domain.repository.SirimRecordRepository

class DeleteRecordUseCase(private val repository: SirimRecordRepository) {
    suspend operator fun invoke(recordId: Long): Result<Unit> = repository.deleteRecord(recordId)
}
