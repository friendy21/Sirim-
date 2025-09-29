package com.sirimqrscanner.app.domain.repository

import com.sirimqrscanner.app.domain.model.SirimRecord
import kotlinx.coroutines.flow.Flow

interface SirimRecordRepository {
    fun observeRecords(): Flow<List<SirimRecord>>
    fun observeRecord(id: Long): Flow<SirimRecord?>
    suspend fun upsert(record: SirimRecord): Long
    suspend fun delete(record: SirimRecord)
    suspend fun clear()
}
