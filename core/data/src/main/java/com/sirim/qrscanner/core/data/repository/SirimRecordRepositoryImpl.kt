package com.sirim.qrscanner.core.data.repository

import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.data.source.local.SirimRecordLocalDataSource
import com.sirim.qrscanner.core.data.source.local.TokenStorage
import com.sirim.qrscanner.core.data.source.remote.SirimRecordRemoteDataSource
import com.sirim.qrscanner.core.domain.model.SirimRecord
import com.sirim.qrscanner.core.domain.repository.SirimRecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class SirimRecordRepositoryImpl(
    private val tokenStorage: TokenStorage,
    private val remoteDataSource: SirimRecordRemoteDataSource,
    private val localDataSource: SirimRecordLocalDataSource,
    private val dispatcherProvider: DispatcherProvider
) : SirimRecordRepository {

    private val scope = CoroutineScope(SupervisorJob() + dispatcherProvider.io)
    private val cachedRecords: StateFlow<List<SirimRecord>> =
        localDataSource.observeRecords()
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override fun observeRecords(): Flow<List<SirimRecord>> = cachedRecords

    override fun observeRecord(id: Long): Flow<SirimRecord?> =
        cachedRecords.map { records -> records.firstOrNull { it.id == id } }

    override suspend fun refresh() {
        withContext(dispatcherProvider.io) {
            val token = tokenStorage.token.firstOrNull()
                ?: throw IllegalStateException("Authentication token missing for refresh")
            val remoteRecords = remoteDataSource.fetchRecords(token)
            localDataSource.replaceAll(remoteRecords)
            tokenStorage.updateLastSync(java.time.Instant.now().toEpochMilli())
        }
    }

    override suspend fun search(query: String): List<SirimRecord> = withContext(dispatcherProvider.io) {
        if (query.isBlank()) cachedRecords.value else localDataSource.search(query)
    }

    override suspend fun save(record: SirimRecord) {
        withContext(dispatcherProvider.io) {
            val now = java.time.Instant.now()
            val entity = record.copy(
                createdAt = record.createdAt.takeIf { it != java.time.Instant.EPOCH } ?: now,
                updatedAt = now
            )
            localDataSource.upsert(entity)
        }
    }

    override suspend fun delete(record: SirimRecord) {
        withContext(dispatcherProvider.io) {
            localDataSource.delete(record)
        }
    }
}
