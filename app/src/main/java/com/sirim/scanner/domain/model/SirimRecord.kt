package com.sirim.scanner.domain.model

data class SirimRecord(
    val id: Long = 0,
    val sirimSerialNo: String,
    val batchNumber: String? = null,
    val brandTrademark: String? = null,
    val model: String? = null,
    val type: String? = null,
    val rating: String? = null,
    val size: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val isSynced: Boolean,
    val deviceId: String? = null,
)
