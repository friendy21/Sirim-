package com.sirimqrscanner.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sirimqrscanner.app.domain.model.SirimRecord

@Database(
    entities = [SirimRecord::class],
    version = 1,
    exportSchema = true
)
abstract class SirimDatabase : RoomDatabase() {
    abstract fun sirimRecordDao(): SirimRecordDao
}
