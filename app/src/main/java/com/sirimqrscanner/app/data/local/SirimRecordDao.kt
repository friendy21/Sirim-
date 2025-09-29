package com.sirimqrscanner.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sirimqrscanner.app.domain.model.SirimRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SirimRecordDao {
    @Query("SELECT * FROM sirim_records ORDER BY updated_at DESC")
    fun observeRecords(): Flow<List<SirimRecord>>

    @Query("SELECT * FROM sirim_records WHERE id = :id")
    fun observeRecord(id: Long): Flow<SirimRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(record: SirimRecord): Long

    @Delete
    suspend fun delete(record: SirimRecord)

    @Query("DELETE FROM sirim_records")
    suspend fun clear()
}
