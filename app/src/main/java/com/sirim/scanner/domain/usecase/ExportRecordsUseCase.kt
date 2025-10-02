package com.sirim.scanner.domain.usecase

import com.sirim.scanner.domain.model.SirimRecord
import com.sirim.scanner.domain.repository.SirimRecordRepository

class ExportRecordsUseCase(private val repository: SirimRecordRepository) {
    suspend fun asExcel(records: List<SirimRecord>): Result<ByteArray> = repository.exportToExcel(records)
    suspend fun asPdf(records: List<SirimRecord>): Result<ByteArray> = repository.exportToPdf(records)
    suspend fun asZip(namedFiles: Map<String, ByteArray>): Result<ByteArray> = repository.exportToZip(namedFiles)
}
