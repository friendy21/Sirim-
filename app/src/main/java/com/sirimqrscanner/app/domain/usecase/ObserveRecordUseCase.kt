package com.sirimqrscanner.app.domain.usecase

import com.sirimqrscanner.app.domain.model.SirimRecord
import com.sirimqrscanner.app.domain.repository.SirimRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecordUseCase @Inject constructor(
    private val repository: SirimRecordRepository
) {
    operator fun invoke(id: Long): Flow<SirimRecord?> = repository.observeRecord(id)
}
