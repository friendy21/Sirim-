package com.sirim.qrscanner.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sirim.qrscanner.core.database.dao.SirimRecordDao
import com.sirim.qrscanner.core.database.entity.SirimRecordEntity

@Database(
    entities = [SirimRecordEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(TimeInstantConverter::class)
abstract class SirimDatabase : RoomDatabase() {
    abstract fun sirimRecordDao(): SirimRecordDao
}
