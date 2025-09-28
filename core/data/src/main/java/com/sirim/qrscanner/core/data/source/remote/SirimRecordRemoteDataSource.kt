package com.sirim.qrscanner.core.data.source.remote

import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.network.dto.SirimRecordDto
import com.sirim.qrscanner.core.network.service.SirimApiService
import java.time.Instant

class SirimRecordRemoteDataSource(
    private val apiService: SirimApiService
) {
    suspend fun fetchRecords(token: String): List<SirimRecord> = apiService.fetchRecords("Bearer $token").map { dto ->
        dto.toDomain()
    }

    suspend fun pushRecords(token: String, records: List<SirimRecord>): List<SirimRecord> =
        apiService.upsertRecords("Bearer $token", records.map { it.toDto() }).map { it.toDomain() }
}

private fun SirimRecordDto.toDomain(): SirimRecord = SirimRecord(
    id = id ?: 0L,
    sirimSerialNo = sirimSerialNo,
    batchNo = batchNo,
    brandTrademark = brandTrademark,
    model = model,
    type = type,
    rating = rating,
    size = size,
    createdAt = Instant.ofEpochMilli(createdAt),
    updatedAt = Instant.ofEpochMilli(updatedAt),
    isSynced = true
)

private fun SirimRecord.toDto(): SirimRecordDto = SirimRecordDto(
    id = id.takeIf { it != 0L },
    sirimSerialNo = sirimSerialNo,
    batchNo = batchNo,
    brandTrademark = brandTrademark,
    model = model,
    type = type,
    rating = rating,
    size = size,
    createdAt = createdAt.toEpochMilli(),
    updatedAt = updatedAt.toEpochMilli()
)
