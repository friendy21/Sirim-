package com.sirim.scanner.domain.usecase

import com.sirim.scanner.data.repository.SirimRecordRepository
import com.sirim.scanner.domain.model.SirimRecord

class SaveRecordUseCase(private val repository: SirimRecordRepository) {
    suspend operator fun invoke(record: SirimRecord): Long = repository.save(record)
}
