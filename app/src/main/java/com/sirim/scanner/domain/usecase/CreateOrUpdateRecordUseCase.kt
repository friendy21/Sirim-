package com.sirim.scanner.domain.usecase

import com.sirim.scanner.domain.model.SirimRecord
import com.sirim.scanner.domain.repository.SirimRecordRepository
import java.time.Instant

class CreateOrUpdateRecordUseCase(private val repository: SirimRecordRepository) {
    suspend operator fun invoke(record: SirimRecord): Result<Long> {
        val now = Instant.now().toEpochMilli()
        val updatedRecord = if (record.id == 0L) {
            record.copy(createdAt = now, updatedAt = now)
        } else {
            record.copy(updatedAt = now)
        }
        return repository.upsertRecord(updatedRecord)
    }
}
