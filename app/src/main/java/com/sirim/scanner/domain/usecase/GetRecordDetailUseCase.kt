package com.sirim.scanner.domain.usecase

import com.sirim.scanner.data.repository.SirimRecordRepository
import com.sirim.scanner.domain.model.SirimRecord

class GetRecordDetailUseCase(private val repository: SirimRecordRepository) {
    suspend operator fun invoke(id: Long): SirimRecord? = repository.getById(id)
}
