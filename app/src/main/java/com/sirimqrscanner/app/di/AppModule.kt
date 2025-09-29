package com.sirimqrscanner.app.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.sirimqrscanner.app.data.local.SirimDatabase
import com.sirimqrscanner.app.data.local.SirimRecordDao
import com.sirimqrscanner.app.data.repository.SirimRecordRepositoryImpl
import com.sirimqrscanner.app.domain.repository.SirimRecordRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): SirimDatabase = Room.databaseBuilder(
        context,
        SirimDatabase::class.java,
        "sirim_records.db"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideSirimRecordDao(database: SirimDatabase): SirimRecordDao = database.sirimRecordDao()

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSirimRecordRepository(
        impl: SirimRecordRepositoryImpl
    ): SirimRecordRepository
}
