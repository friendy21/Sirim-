package com.sirim.scanner.domain.model

data class SirimRecord(
    val id: Long? = null,
    val sirimSerialNumber: String,
    val batchNumber: String? = null,
    val brandTrademark: String? = null,
    val model: String? = null,
    val type: String? = null,
    val rating: String? = null,
    val size: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val isSynced: Boolean = false,
    val deviceId: String? = null
)
