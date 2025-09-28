package com.sirim.qrscanner.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "sirim_records")
data class SirimRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "sirim_serial_no") val sirimSerialNo: String,
    @ColumnInfo(name = "batch_no") val batchNo: String?,
    @ColumnInfo(name = "brand_trademark") val brandTrademark: String?,
    @ColumnInfo(name = "model") val model: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "rating") val rating: String?,
    @ColumnInfo(name = "size") val size: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "is_synced") val isSynced: Boolean
)
