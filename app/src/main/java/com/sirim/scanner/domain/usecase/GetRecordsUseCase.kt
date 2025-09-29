package com.sirim.scanner.domain.usecase

import com.sirim.scanner.data.repository.SirimRecordRepository
import com.sirim.scanner.domain.model.SirimRecord
import kotlinx.coroutines.flow.Flow

class GetRecordsUseCase(private val repository: SirimRecordRepository) {
    operator fun invoke(): Flow<List<SirimRecord>> = repository.observeRecords()
}
