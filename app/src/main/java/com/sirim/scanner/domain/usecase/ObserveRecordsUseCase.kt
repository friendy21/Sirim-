package com.sirim.scanner.domain.usecase

import com.sirim.scanner.domain.model.SirimRecord
import com.sirim.scanner.domain.repository.SirimRecordRepository
import kotlinx.coroutines.flow.Flow

class ObserveRecordsUseCase(private val repository: SirimRecordRepository) {
    operator fun invoke(): Flow<List<SirimRecord>> = repository.observeRecords()
}
