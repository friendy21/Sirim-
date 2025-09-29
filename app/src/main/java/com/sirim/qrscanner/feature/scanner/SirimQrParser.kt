package com.sirim.qrscanner.feature.scanner

import com.sirim.qrscanner.core.domain.model.SirimRecord
import java.time.Instant

object SirimQrParser {
    private val keyAliases = mapOf(
        "serial" to listOf("serial", "serial_no", "sirim", "sirim_serial"),
        "brand" to listOf("brand", "trademark"),
        "model" to listOf("model", "type"),
        "batch" to listOf("batch", "lot", "batch_no"),
        "rating" to listOf("rating"),
        "size" to listOf("size"),
        "type" to listOf("type", "category")
    )

    fun parse(raw: String?, ocrText: String?): SirimRecord? {
        val payload = raw?.takeIf { it.isNotBlank() } ?: ocrText ?: return null
        val normalized = payload.replace("\r", "\n")
        val map = mutableMap(normalized)
        val serial = map["serial"] ?: normalized.lines().firstOrNull { it.isNotBlank() }
        if (serial.isNullOrBlank()) return null
        return SirimRecord(
            sirimSerialNo = serial.trim(),
            brandTrademark = map["brand"],
            model = map["model"],
            batchNo = map["batch"],
            rating = map["rating"],
            size = map["size"],
            type = map["type"],
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            isSynced = false
        )
    }

    private fun mutableMap(text: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val segments = text.split('\n', '|', ';', ',')
        segments.mapNotNull { segment ->
            val cleaned = segment.trim()
            if (cleaned.isBlank()) return@mapNotNull null
            val parts = cleaned.split(':', '=', '-').map { it.trim() }
            if (parts.size < 2) return@mapNotNull null
            val key = resolveKey(parts[0]) ?: return@mapNotNull null
            key to parts.subList(1, parts.size).joinToString(":").trim()
        }.forEach { (key, value) ->
            if (value.isNotBlank()) {
                result[key] = value
            }
        }
        return result
    }

    private fun resolveKey(rawKey: String): String? {
        val normalized = rawKey.lowercase().replace(" ", "").replace("_", "")
        return keyAliases.entries.firstOrNull { entry ->
            entry.value.any { alias -> normalized.contains(alias) }
        }?.key
    }
}
