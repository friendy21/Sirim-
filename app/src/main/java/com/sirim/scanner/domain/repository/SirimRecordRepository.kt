package com.sirim.scanner.domain.repository

import com.sirim.scanner.domain.model.SirimRecord
import kotlinx.coroutines.flow.Flow

interface SirimRecordRepository {
    fun observeRecords(): Flow<List<SirimRecord>>
    suspend fun upsertRecord(record: SirimRecord): Result<Long>
    suspend fun deleteRecord(recordId: Long): Result<Unit>
    suspend fun getRecord(id: Long): SirimRecord?
    suspend fun getRecordBySerial(serial: String): SirimRecord?
    suspend fun exportToExcel(records: List<SirimRecord>): Result<ByteArray>
    suspend fun exportToPdf(records: List<SirimRecord>): Result<ByteArray>
    suspend fun exportToZip(files: Map<String, ByteArray>): Result<ByteArray>
}
