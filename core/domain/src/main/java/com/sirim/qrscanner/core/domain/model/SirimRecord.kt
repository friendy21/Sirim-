package com.sirim.qrscanner.core.domain.model

import java.time.Instant

data class SirimRecord(
    val id: Long = 0L,
    val sirimSerialNo: String = "",
    val batchNo: String? = null,
    val brandTrademark: String? = null,
    val model: String? = null,
    val type: String? = null,
    val rating: String? = null,
    val size: String? = null,
    val createdAt: Instant = Instant.EPOCH,
    val updatedAt: Instant = Instant.EPOCH,
    val isSynced: Boolean = false
)
