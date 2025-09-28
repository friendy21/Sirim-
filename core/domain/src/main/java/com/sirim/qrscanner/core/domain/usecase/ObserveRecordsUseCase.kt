package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.domain.repository.SirimRecordRepository
import kotlinx.coroutines.flow.Flow

class ObserveRecordsUseCase(
    private val repository: SirimRecordRepository
) {
    operator fun invoke(): Flow<List<SirimRecord>> = repository.observeRecords()
}
