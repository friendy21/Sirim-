package com.sirimqrscanner.app.data.repository

import com.sirimqrscanner.app.data.local.SirimRecordDao
import com.sirimqrscanner.app.domain.model.SirimRecord
import com.sirimqrscanner.app.domain.repository.SirimRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SirimRecordRepositoryImpl @Inject constructor(
    private val dao: SirimRecordDao
) : SirimRecordRepository {

    override fun observeRecords(): Flow<List<SirimRecord>> = dao.observeRecords()

    override fun observeRecord(id: Long): Flow<SirimRecord?> = dao.observeRecord(id)

    override suspend fun upsert(record: SirimRecord) = dao.upsert(record)

    override suspend fun delete(record: SirimRecord) = dao.delete(record)

    override suspend fun clear() = dao.clear()
}
