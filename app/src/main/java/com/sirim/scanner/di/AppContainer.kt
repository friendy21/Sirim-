package com.sirim.scanner.di

import android.content.Context
import androidx.room.Room
import com.sirim.scanner.data.local.SirimDatabase
import com.sirim.scanner.data.repository.SirimRecordRepository
import com.sirim.scanner.domain.usecase.DeleteRecordUseCase
import com.sirim.scanner.domain.usecase.ExportRecordsUseCase
import com.sirim.scanner.domain.usecase.GetRecordDetailUseCase
import com.sirim.scanner.domain.usecase.GetRecordsUseCase
import com.sirim.scanner.domain.usecase.SaveRecordUseCase
import com.sirim.scanner.sync.Scheduler

class AppContainer(context: Context) {
    private val database: SirimDatabase = Room.databaseBuilder(
        context,
        SirimDatabase::class.java,
        "sirim.db"
    ).fallbackToDestructiveMigration().build()

    private val repository = SirimRecordRepository(database.recordDao())

    val getRecords = GetRecordsUseCase(repository)
    val getRecordDetail = GetRecordDetailUseCase(repository)
    val saveRecord = SaveRecordUseCase(repository)
    val deleteRecord = DeleteRecordUseCase(repository)
    val exportRecords = ExportRecordsUseCase(repository, context)
    val scheduler = Scheduler(context)
}
