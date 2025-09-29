package com.sirim.scanner.presentation.scan

fun parseSirimPayload(raw: String): ParsedSirimPayload? {
    val tokens = raw.split("|")
    if (tokens.size < 2) return null
    val map = tokens.mapNotNull {
        val parts = it.split(":", limit = 2)
        if (parts.size == 2) parts[0].trim() to parts[1].trim() else null
    }.toMap()
    val serial = map["SERIAL"] ?: map["SIRIM"] ?: tokens.first().trim()
    return ParsedSirimPayload(
        serial = serial,
        batch = map["BATCH"],
        brand = map["BRAND"],
        model = map["MODEL"],
        type = map["TYPE"],
        rating = map["RATING"],
        size = map["SIZE"]
    )
}
