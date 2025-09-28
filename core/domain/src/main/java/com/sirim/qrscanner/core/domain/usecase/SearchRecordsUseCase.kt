package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.domain.repository.SirimRecordRepository

class SearchRecordsUseCase(
    private val repository: SirimRecordRepository
) {
    suspend operator fun invoke(query: String): List<SirimRecord> = repository.search(query)
}
