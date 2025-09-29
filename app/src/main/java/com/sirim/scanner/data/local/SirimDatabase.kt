package com.sirim.scanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SirimRecordEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SirimDatabase : RoomDatabase() {
    abstract fun recordDao(): SirimRecordDao
}
