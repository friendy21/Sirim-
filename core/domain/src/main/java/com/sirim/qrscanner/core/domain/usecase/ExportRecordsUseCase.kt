package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.domain.repository.ExportFormat
import com.sirim.qrscanner.core.domain.repository.RecordExportRepository
import java.io.File
import javax.inject.Inject

class ExportRecordsUseCase @Inject constructor(
    private val repository: RecordExportRepository
) {
    suspend operator fun invoke(records: List<SirimRecord>, format: ExportFormat): File {
        require(records.isNotEmpty()) { "No records available for export" }
        return repository.export(records, format)
    }
}
