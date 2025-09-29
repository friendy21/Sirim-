package com.sirim.qrscanner.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.work.WorkManager
import com.sirim.qrscanner.core.common.DefaultDispatcherProvider
import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.data.repository.AuthenticationRepositoryImpl
import com.sirim.qrscanner.core.data.repository.RecordExportRepositoryImpl
import com.sirim.qrscanner.core.data.repository.SirimRecordRepositoryImpl
import com.sirim.qrscanner.core.data.repository.SynchronizationRepositoryImpl
import com.sirim.qrscanner.core.data.source.local.SirimRecordLocalDataSource
import com.sirim.qrscanner.core.data.source.local.TokenStorage
import com.sirim.qrscanner.core.data.source.remote.SirimRecordRemoteDataSource
import com.sirim.qrscanner.core.database.SirimDatabase
import com.sirim.qrscanner.core.domain.repository.AuthenticationRepository
import com.sirim.qrscanner.core.domain.repository.RecordExportRepository
import com.sirim.qrscanner.core.domain.repository.SirimRecordRepository
import com.sirim.qrscanner.core.domain.repository.SynchronizationRepository
import com.sirim.qrscanner.core.network.service.SirimApiService
import com.sirim.qrscanner.core.database.dao.SirimRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideMasterKey(@ApplicationContext context: Context): MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    @Provides
    @Singleton
    fun provideSecurePreferences(
        @ApplicationContext context: Context,
        masterKey: MasterKey
    ): android.content.SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "sirim_qr_scanner_secure",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): SirimDatabase = Room.databaseBuilder(
        context,
        SirimDatabase::class.java,
        "sirim_qr_scanner.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideSirimRecordDao(database: SirimDatabase): SirimRecordDao = database.sirimRecordDao()

    @Provides
    @Singleton
    fun provideTokenStorage(preferences: android.content.SharedPreferences) = TokenStorage(preferences)

    @Provides
    @Singleton
    fun provideLocalDataSource(dao: SirimRecordDao) =
        SirimRecordLocalDataSource(dao)

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideSirimApiService(retrofit: Retrofit): SirimApiService =
        retrofit.create(SirimApiService::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: SirimApiService) = SirimRecordRemoteDataSource(apiService)

    @Provides
    @Singleton
    fun provideSirimRecordRepository(
        tokenStorage: TokenStorage,
        remoteDataSource: SirimRecordRemoteDataSource,
        localDataSource: SirimRecordLocalDataSource,
        dispatcherProvider: DispatcherProvider
    ): SirimRecordRepository = SirimRecordRepositoryImpl(
        tokenStorage,
        remoteDataSource,
        localDataSource,
        dispatcherProvider
    )

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        apiService: SirimApiService,
        tokenStorage: TokenStorage,
        dispatcherProvider: DispatcherProvider
    ): AuthenticationRepository = AuthenticationRepositoryImpl(apiService, tokenStorage, dispatcherProvider)

    @Provides
    @Singleton
    fun provideSynchronizationRepository(
        tokenStorage: TokenStorage,
        remoteDataSource: SirimRecordRemoteDataSource,
        localDataSource: SirimRecordLocalDataSource,
        dispatcherProvider: DispatcherProvider
    ): SynchronizationRepository = SynchronizationRepositoryImpl(
        tokenStorage,
        remoteDataSource,
        localDataSource,
        dispatcherProvider
    )

    @Provides
    @Singleton
    fun provideRecordExportRepository(
        @ApplicationContext context: Context,
        dispatcherProvider: DispatcherProvider
    ): RecordExportRepository = RecordExportRepositoryImpl(context, dispatcherProvider)
}
