package com.sirimqrscanner.app.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sirim_records")
data class SirimRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "sirim_serial_no") val sirimSerialNo: String,
    @ColumnInfo(name = "batch_no") val batchNo: String?,
    @ColumnInfo(name = "brand_trademark") val brandTrademark: String?,
    val model: String?,
    val type: String?,
    val rating: String?,
    val size: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_synced") val isSynced: Boolean = false,
    @ColumnInfo(name = "device_id") val deviceId: String? = null
)
