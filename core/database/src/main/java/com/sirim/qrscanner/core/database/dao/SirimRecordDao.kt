package com.sirim.qrscanner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sirim.qrscanner.core.database.entity.SirimRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SirimRecordDao {
    @Query("SELECT * FROM sirim_records ORDER BY updated_at DESC")
    fun observeRecords(): Flow<List<SirimRecordEntity>>

    @Query("SELECT * FROM sirim_records WHERE id = :id")
    fun observeRecord(id: Long): Flow<SirimRecordEntity?>

    @Query("SELECT * FROM sirim_records WHERE sirim_serial_no LIKE '%' || :query || '%' OR brand_trademark LIKE '%' || :query || '%' OR batch_no LIKE '%' || :query || '%' ORDER BY updated_at DESC")
    suspend fun search(query: String): List<SirimRecordEntity>

    @Query("SELECT * FROM sirim_records WHERE is_synced = 0")
    suspend fun unsynced(): List<SirimRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SirimRecordEntity)

    @Update
    suspend fun update(entity: SirimRecordEntity)

    @Delete
    suspend fun delete(entity: SirimRecordEntity)

    @Query("SELECT COUNT(*) FROM sirim_records")
    suspend fun count(): Int
}
