package com.sirim.qrscanner.core.domain.repository

import com.sirim.qrscanner.core.domain.model.SirimRecord
import java.io.File

enum class ExportFormat(val mimeType: String) {
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PDF("application/pdf"),
    ZIP("application/zip")
}

interface RecordExportRepository {
    suspend fun export(records: List<SirimRecord>, format: ExportFormat): File
}
