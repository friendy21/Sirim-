package com.sirim.scanner.data.repository

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.UnitValue
import com.sirim.scanner.data.local.SirimRecordDao
import com.sirim.scanner.data.local.SirimRecordEntity
import com.sirim.scanner.domain.model.SirimRecord
import com.sirim.scanner.domain.repository.SirimRecordRepository
import com.sirim.scanner.util.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class LocalSirimRecordRepository(
    private val recordDao: SirimRecordDao,
    private val dispatchers: DispatchersProvider
) : SirimRecordRepository {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC)

    override fun observeRecords(): Flow<List<SirimRecord>> =
        recordDao.observeRecords().map { list -> list.map { it.toDomain() } }

    override suspend fun upsertRecord(record: SirimRecord): Result<Long> = withContext(dispatchers.io) {
        runCatching {
            val existing = if (record.id != 0L) recordDao.findById(record.id) else null
            val entity = record.toEntity(existing?.id ?: 0L)
            val id = recordDao.upsert(entity)
            id.takeIf { it != 0L } ?: existing?.id ?: throw IllegalStateException("Unable to persist record")
        }
    }

    override suspend fun deleteRecord(recordId: Long): Result<Unit> = withContext(dispatchers.io) {
        runCatching {
            val entity = recordDao.findById(recordId) ?: return@runCatching
            recordDao.delete(entity)
        }
    }

    override suspend fun getRecord(id: Long): SirimRecord? = withContext(dispatchers.io) {
        recordDao.findById(id)?.toDomain()
    }

    override suspend fun getRecordBySerial(serial: String): SirimRecord? = withContext(dispatchers.io) {
        recordDao.findBySerial(serial)?.toDomain()
    }

    override suspend fun exportToExcel(records: List<SirimRecord>): Result<ByteArray> = withContext(dispatchers.default) {
        runCatching {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("SIRIM Records")
            val header = listOf(
                "Serial", "Batch", "Brand", "Model", "Type", "Rating", "Size", "Created", "Updated", "Synced", "Device"
            )
            val headerRow = sheet.createRow(0)
            header.forEachIndexed { index, title ->
                headerRow.createCell(index).setCellValue(title)
            }
            records.forEachIndexed { rowIndex, record ->
                val row = sheet.createRow(rowIndex + 1)
                val values = listOf(
                    record.sirimSerialNo,
                    record.batchNumber.orEmpty(),
                    record.brandTrademark.orEmpty(),
                    record.model.orEmpty(),
                    record.type.orEmpty(),
                    record.rating.orEmpty(),
                    record.size.orEmpty(),
                    formatter.format(Instant.ofEpochMilli(record.createdAt)),
                    formatter.format(Instant.ofEpochMilli(record.updatedAt)),
                    if (record.isSynced) "Yes" else "No",
                    record.deviceId.orEmpty()
                )
                values.forEachIndexed { cellIndex, value ->
                    row.createCell(cellIndex).setCellValue(value)
                }
            }
            repeat(header.size) { column -> sheet.autoSizeColumn(column) }
            ByteArrayOutputStream().use { output ->
                workbook.write(output)
                workbook.close()
                output.toByteArray()
            }
        }
    }

    override suspend fun exportToPdf(records: List<SirimRecord>): Result<ByteArray> = withContext(dispatchers.default) {
        runCatching {
            ByteArrayOutputStream().use { output ->
                val pdfDocument = PdfDocument(PdfWriter(output))
                val document = Document(pdfDocument)
                document.add(Paragraph("SIRIM QR Code Records"))
                val table = Table(floatArrayOf(2f, 2f, 2f, 2f, 1f)).apply {
                    setWidth(UnitValue.createPercentValue(100f))
                }
                val columns = listOf("Serial", "Brand", "Model", "Type", "Synced")
                columns.forEach { column -> table.addHeaderCell(column) }
                records.forEach { record ->
                    table.addCell(record.sirimSerialNo)
                    table.addCell(record.brandTrademark.orEmpty())
                    table.addCell(record.model.orEmpty())
                    table.addCell(record.type.orEmpty())
                    table.addCell(if (record.isSynced) "Yes" else "No")
                }
                document.add(table)
                document.close()
                pdfDocument.close()
                output.toByteArray()
            }
        }
    }

    override suspend fun exportToZip(files: Map<String, ByteArray>): Result<ByteArray> = withContext(dispatchers.default) {
        runCatching {
            ByteArrayOutputStream().use { output ->
                ZipOutputStream(output).use { zip ->
                    files.forEach { (fileName, bytes) ->
                        val entry = ZipEntry(fileName)
                        zip.putNextEntry(entry)
                        zip.write(bytes)
                        zip.closeEntry()
                    }
                }
                output.toByteArray()
            }
        }
    }

    private fun SirimRecordEntity.toDomain(): SirimRecord = SirimRecord(
        id = id,
        sirimSerialNo = sirimSerialNo,
        batchNumber = batchNumber,
        brandTrademark = brandTrademark,
        model = model,
        type = type,
        rating = rating,
        size = size,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced,
        deviceId = deviceId
    )

    private fun SirimRecord.toEntity(existingId: Long): SirimRecordEntity = SirimRecordEntity(
        id = existingId,
        sirimSerialNo = sirimSerialNo,
        batchNumber = batchNumber,
        brandTrademark = brandTrademark,
        model = model,
        type = type,
        rating = rating,
        size = size,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced,
        deviceId = deviceId
    )
}
