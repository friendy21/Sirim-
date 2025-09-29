package com.sirim.scanner.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.sirim.scanner.data.repository.SirimRecordRepository
import com.sirim.scanner.domain.model.SirimRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ExportRecordsUseCase(
    private val repository: SirimRecordRepository,
    private val context: Context
) {
    suspend operator fun invoke(format: Format): Uri = withContext(Dispatchers.IO) {
        val records = repository.observeRecords().first()
        when (format) {
            Format.EXCEL -> exportExcel(records)
            Format.PDF -> exportPdf(records)
            Format.ZIP -> exportZip(records)
        }
    }

    private fun exportExcel(records: List<SirimRecord>): Uri {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("SIRIM Records")
        val header = sheet.createRow(0)
        val columns = listOf(
            "Serial", "Batch", "Brand", "Model", "Type", "Rating", "Size", "Updated"
        )
        columns.forEachIndexed { index, value ->
            header.createCell(index).setCellValue(value)
        }
        records.forEachIndexed { rowIndex, record ->
            val row = sheet.createRow(rowIndex + 1)
            row.createCell(0).setCellValue(record.sirimSerialNumber)
            row.createCell(1).setCellValue(record.batchNumber.orEmpty())
            row.createCell(2).setCellValue(record.brandTrademark.orEmpty())
            row.createCell(3).setCellValue(record.model.orEmpty())
            row.createCell(4).setCellValue(record.type.orEmpty())
            row.createCell(5).setCellValue(record.rating.orEmpty())
            row.createCell(6).setCellValue(record.size.orEmpty())
            row.createCell(7).setCellValue(record.updatedAt.toString())
        }
        val file = createTempFile("records", ".xlsx")
        FileOutputStream(file).use { workbook.write(it) }
        workbook.close()
        return toUri(file)
    }

    private fun exportPdf(records: List<SirimRecord>): Uri {
        val file = createTempFile("records", ".pdf")
        val pdfDocument = PdfDocument(PdfWriter(file))
        val document = Document(pdfDocument)
        document.add(Paragraph("SIRIM Records"))
        records.forEach { record ->
            document.add(Paragraph("Serial: ${record.sirimSerialNumber}"))
            record.batchNumber?.let { document.add(Paragraph("Batch: $it")) }
            record.brandTrademark?.let { document.add(Paragraph("Brand: $it")) }
            record.model?.let { document.add(Paragraph("Model: $it")) }
            record.type?.let { document.add(Paragraph("Type: $it")) }
            record.rating?.let { document.add(Paragraph("Rating: $it")) }
            record.size?.let { document.add(Paragraph("Size: $it")) }
            document.add(Paragraph("Updated: ${record.updatedAt}"))
            document.add(Paragraph(" "))
        }
        document.close()
        pdfDocument.close()
        return toUri(file)
    }

    private fun exportZip(records: List<SirimRecord>): Uri {
        val zipFile = createTempFile("records", ".zip")
        ZipOutputStream(FileOutputStream(zipFile)).use { zipStream ->
            records.forEach { record ->
                val entry = ZipEntry("${record.sirimSerialNumber}.txt")
                zipStream.putNextEntry(entry)
                val content = buildString {
                    appendLine("Serial: ${record.sirimSerialNumber}")
                    appendLine("Batch: ${record.batchNumber.orEmpty()}")
                    appendLine("Brand: ${record.brandTrademark.orEmpty()}")
                    appendLine("Model: ${record.model.orEmpty()}")
                    appendLine("Type: ${record.type.orEmpty()}")
                    appendLine("Rating: ${record.rating.orEmpty()}")
                    appendLine("Size: ${record.size.orEmpty()}")
                    appendLine("Updated: ${record.updatedAt}")
                }
                zipStream.write(content.toByteArray())
                zipStream.closeEntry()
            }
        }
        return toUri(zipFile)
    }

    private fun createTempFile(name: String, suffix: String): File {
        val directory = File(context.cacheDir, "exports").apply { mkdirs() }
        return File.createTempFile(name, suffix, directory)
    }

    private fun toUri(file: File): Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    enum class Format { EXCEL, PDF, ZIP }
}
