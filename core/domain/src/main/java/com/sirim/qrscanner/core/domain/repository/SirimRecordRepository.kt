package com.sirim.qrscanner.core.domain.repository

import com.sirim.qrscanner.core.domain.model.SirimRecord
import kotlinx.coroutines.flow.Flow

interface SirimRecordRepository {
    fun observeRecords(): Flow<List<SirimRecord>>
    fun observeRecord(id: Long): Flow<SirimRecord?>
    suspend fun refresh()
    suspend fun search(query: String): List<SirimRecord>
    suspend fun save(record: SirimRecord)
    suspend fun delete(record: SirimRecord)
}
