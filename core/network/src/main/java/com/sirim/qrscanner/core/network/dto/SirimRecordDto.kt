package com.sirim.qrscanner.core.network.dto

data class SirimRecordDto(
    val id: Long?,
    val sirimSerialNo: String,
    val batchNo: String?,
    val brandTrademark: String?,
    val model: String?,
    val type: String?,
    val rating: String?,
    val size: String?,
    val createdAt: Long,
    val updatedAt: Long
)
