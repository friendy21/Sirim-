package com.sirim.scanner.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SirimRecordDao {
    @Query("SELECT * FROM sirim_records ORDER BY created_at DESC")
    fun observeRecords(): Flow<List<SirimRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(record: SirimRecordEntity): Long

    @Update
    suspend fun update(record: SirimRecordEntity)

    @Delete
    suspend fun delete(record: SirimRecordEntity)

    @Query("SELECT * FROM sirim_records WHERE sirim_serial_no = :serial LIMIT 1")
    suspend fun findBySerial(serial: String): SirimRecordEntity?

    @Query("SELECT * FROM sirim_records WHERE id = :id")
    suspend fun findById(id: Long): SirimRecordEntity?
}
