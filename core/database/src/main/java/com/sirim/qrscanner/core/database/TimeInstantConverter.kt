package com.sirim.qrscanner.core.database

import androidx.room.TypeConverter
import java.time.Instant

class TimeInstantConverter {
    @TypeConverter
    fun toInstant(epochMillis: Long?): Instant? = epochMillis?.let(Instant::ofEpochMilli)

    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = instant?.toEpochMilli()
}
