package com.sirimqrscanner.app.domain.usecase

import com.sirimqrscanner.app.domain.model.SirimRecord
import com.sirimqrscanner.app.domain.repository.SirimRecordRepository
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val repository: SirimRecordRepository
) {
    suspend operator fun invoke(record: SirimRecord) = repository.delete(record)
}
