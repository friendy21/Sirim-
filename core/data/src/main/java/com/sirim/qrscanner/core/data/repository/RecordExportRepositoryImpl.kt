package com.sirim.qrscanner.core.data.repository

import android.content.Context
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.domain.repository.ExportFormat
import com.sirim.qrscanner.core.domain.repository.RecordExportRepository
import java.io.File
import java.io.FileOutputStream
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class RecordExportRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider
) : RecordExportRepository {

    override suspend fun export(records: List<SirimRecord>, format: ExportFormat): File =
        withContext(dispatcherProvider.io) {
            val outputDir = File(context.cacheDir, "exports").apply { mkdirs() }
            when (format) {
                ExportFormat.EXCEL -> exportExcel(records, File(outputDir, "sirim_records.xlsx"))
                ExportFormat.PDF -> exportPdf(records, File(outputDir, "sirim_records.pdf"))
                ExportFormat.ZIP -> exportZip(records, File(outputDir, "sirim_records.zip"))
            }
        }

    private fun exportExcel(records: List<SirimRecord>, destination: File): File {
        XSSFWorkbook().use { workbook ->
            val sheet: XSSFSheet = workbook.createSheet("SIRIM Records")
            val header = sheet.createRow(0)
            val headers = listOf("Serial", "Brand", "Model", "Batch", "Type", "Rating", "Size", "Updated")
            headers.forEachIndexed { index, title ->
                val cell = header.createCell(index)
                cell.setCellValue(title)
                val style = workbook.createCellStyle()
                val font = workbook.createFont().apply { bold = true }
                style.setFont(font)
                style.alignment = HorizontalAlignment.CENTER
                cell.cellStyle = style
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault())
            records.forEachIndexed { rowIndex, record ->
                val row = sheet.createRow(rowIndex + 1)
                row.createCell(0).setCellValue(record.sirimSerialNo)
                row.createCell(1).setCellValue(record.brandTrademark.orEmpty())
                row.createCell(2).setCellValue(record.model.orEmpty())
                row.createCell(3).setCellValue(record.batchNo.orEmpty())
                row.createCell(4).setCellValue(record.type.orEmpty())
                row.createCell(5).setCellValue(record.rating.orEmpty())
                row.createCell(6).setCellValue(record.size.orEmpty())
                row.createCell(7).setCellValue(
                    formatter.format(record.updatedAt.atZone(ZoneId.systemDefault()))
                )
            }
            headers.indices.forEach { sheet.autoSizeColumn(it) }
            FileOutputStream(destination).use { output -> workbook.write(output) }
        }
        return destination
    }

    private fun exportPdf(records: List<SirimRecord>, destination: File): File {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.getDefault())
        PdfWriter(destination).use { writer ->
            PdfDocument(writer).use { pdf ->
                Document(pdf).use { document ->
                    document.add(Paragraph("SIRIM Records").setBold())
                    records.forEach { record ->
                        val paragraph = Paragraph().apply {
                            add("Serial: ${record.sirimSerialNo}\n")
                            record.brandTrademark?.let { add("Brand: $it\n") }
                            record.model?.let { add("Model: $it\n") }
                            record.batchNo?.let { add("Batch: $it\n") }
                            record.type?.let { add("Type: $it\n") }
                            record.rating?.let { add("Rating: $it\n") }
                            record.size?.let { add("Size: $it\n") }
                            add(
                                "Updated: ${
                                    formatter.format(record.updatedAt.atZone(ZoneId.systemDefault()))
                                }\n"
                            )
                        }
                        document.add(paragraph)
                    }
                }
            }
        }
        return destination
    }

    private fun exportZip(records: List<SirimRecord>, destination: File): File {
        ZipOutputStream(FileOutputStream(destination)).use { zipStream ->
            zipStream.putNextEntry(ZipEntry("records.csv"))
            val builder = StringBuilder()
            builder.append("serial,brand,model,batch,type,rating,size,updated\n")
            records.forEach { record ->
                builder.append(record.sirimSerialNo).append(',')
                builder.append(record.brandTrademark.orEmpty()).append(',')
                builder.append(record.model.orEmpty()).append(',')
                builder.append(record.batchNo.orEmpty()).append(',')
                builder.append(record.type.orEmpty()).append(',')
                builder.append(record.rating.orEmpty()).append(',')
                builder.append(record.size.orEmpty()).append(',')
                builder.append(record.updatedAt.toString()).append('\n')
            }
            zipStream.write(builder.toString().toByteArray())
            zipStream.closeEntry()
        }
        return destination
    }
}
