package com.sirimqrscanner.app.domain.usecase

import com.sirimqrscanner.app.domain.model.SirimRecord
import com.sirimqrscanner.app.domain.repository.SirimRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecordsUseCase @Inject constructor(
    private val repository: SirimRecordRepository
) {
    operator fun invoke(): Flow<List<SirimRecord>> = repository.observeRecords()
}
