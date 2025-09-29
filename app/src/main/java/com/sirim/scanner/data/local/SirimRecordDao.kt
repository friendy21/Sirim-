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
    @Query("SELECT * FROM sirim_records ORDER BY updated_at DESC")
    fun observeAll(): Flow<List<SirimRecordEntity>>

    @Query("SELECT * FROM sirim_records WHERE id = :id")
    suspend fun getById(id: Long): SirimRecordEntity?

    @Query("SELECT * FROM sirim_records WHERE sirim_serial_no = :serial LIMIT 1")
    suspend fun getBySerial(serial: String): SirimRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(record: SirimRecordEntity): Long

    @Update
    suspend fun update(record: SirimRecordEntity)

    @Delete
    suspend fun delete(record: SirimRecordEntity)
}
