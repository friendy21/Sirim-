package com.sirim.scanner.data.repository

import com.sirim.scanner.data.local.SirimRecordDao
import com.sirim.scanner.data.local.SirimRecordEntity
import com.sirim.scanner.domain.model.SirimRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SirimRecordRepository(private val dao: SirimRecordDao) {
    fun observeRecords(): Flow<List<SirimRecord>> = dao.observeAll().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getById(id: Long): SirimRecord? = dao.getById(id)?.toDomain()

    suspend fun save(record: SirimRecord): Long {
        val entity = record.toEntity()
        return dao.upsert(entity)
    }

    suspend fun delete(record: SirimRecord) {
        record.id?.let { id ->
            dao.getById(id)?.let { dao.delete(it) }
        }
    }

    private fun SirimRecordEntity.toDomain(): SirimRecord = SirimRecord(
        id = id,
        sirimSerialNumber = sirimSerialNumber,
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

    private fun SirimRecord.toEntity(): SirimRecordEntity = SirimRecordEntity(
        id = id ?: 0L,
        sirimSerialNumber = sirimSerialNumber,
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
