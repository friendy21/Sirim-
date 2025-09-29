package com.sirim.scanner.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SirimRecordEntity::class, UserEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SirimScannerDatabase : RoomDatabase() {
    abstract fun recordDao(): SirimRecordDao
    abstract fun userDao(): UserDao

    companion object {
        fun create(context: Context): SirimScannerDatabase = Room.databaseBuilder(
            context,
            SirimScannerDatabase::class.java,
            "sirim_scanner.db"
        ).fallbackToDestructiveMigration().build()
    }
}
