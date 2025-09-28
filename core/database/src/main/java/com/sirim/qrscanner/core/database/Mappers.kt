package com.sirim.qrscanner.core.database

import com.sirim.qrscanner.core.database.entity.SirimRecordEntity
import com.sirim.qrscanner.core.domain.model.SirimRecord
import java.time.Instant

fun SirimRecordEntity.asDomain(): SirimRecord = SirimRecord(
    id = id,
    sirimSerialNo = sirimSerialNo,
    batchNo = batchNo,
    brandTrademark = brandTrademark,
    model = model,
    type = type,
    rating = rating,
    size = size,
    createdAt = Instant.ofEpochMilli(createdAt),
    updatedAt = Instant.ofEpochMilli(updatedAt),
    isSynced = isSynced
)

fun SirimRecord.asEntity(): SirimRecordEntity = SirimRecordEntity(
    id = id,
    sirimSerialNo = sirimSerialNo,
    batchNo = batchNo,
    brandTrademark = brandTrademark,
    model = model,
    type = type,
    rating = rating,
    size = size,
    createdAt = createdAt.toEpochMilli(),
    updatedAt = updatedAt.toEpochMilli(),
    isSynced = isSynced
)
