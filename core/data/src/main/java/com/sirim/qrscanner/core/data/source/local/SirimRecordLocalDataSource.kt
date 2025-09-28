package com.sirim.qrscanner.core.data.source.local

import com.sirim.qrscanner.core.database.Mappers.asDomain
import com.sirim.qrscanner.core.database.Mappers.asEntity
import com.sirim.qrscanner.core.database.dao.SirimRecordDao
import com.sirim.qrscanner.core.domain.model.SirimRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SirimRecordLocalDataSource(
    private val dao: SirimRecordDao
) {
    fun observeRecords(): Flow<List<SirimRecord>> = dao.observeRecords().map { entities ->
        entities.map { it.asDomain() }
    }

    fun observeRecord(id: Long): Flow<SirimRecord?> = dao.observeRecord(id).map { it?.asDomain() }

    suspend fun search(query: String): List<SirimRecord> = dao.search(query).map { it.asDomain() }

    suspend fun unsynced(): List<SirimRecord> = dao.unsynced().map { it.asDomain() }

    suspend fun upsert(record: SirimRecord) {
        if (record.id == 0L) {
            dao.insert(record.asEntity())
        } else {
            dao.update(record.asEntity())
        }
    }

    suspend fun markSynced(records: List<SirimRecord>) {
        records.forEach { record ->
            dao.update(record.copy(isSynced = true).asEntity())
        }
    }

    suspend fun delete(record: SirimRecord) {
        dao.delete(record.asEntity())
    }
}
