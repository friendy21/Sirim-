package com.sirim.scanner.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sirim_records")
data class SirimRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "sirim_serial_no")
    val sirimSerialNo: String,
    @ColumnInfo(name = "batch_no")
    val batchNumber: String?,
    @ColumnInfo(name = "brand_trademark")
    val brandTrademark: String?,
    val model: String?,
    val type: String?,
    val rating: String?,
    val size: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean,
    @ColumnInfo(name = "device_id")
    val deviceId: String?,
)
