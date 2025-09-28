package com.sirim.qrscanner.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.sirim.qrscanner.core.common.DefaultDispatcherProvider
import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.data.repository.AuthenticationRepositoryImpl
import com.sirim.qrscanner.core.data.repository.SirimRecordRepositoryImpl
import com.sirim.qrscanner.core.data.repository.SynchronizationRepositoryImpl
import com.sirim.qrscanner.core.data.source.local.SirimRecordLocalDataSource
import com.sirim.qrscanner.core.data.source.local.TokenStorage
import com.sirim.qrscanner.core.data.source.remote.SirimRecordRemoteDataSource
import com.sirim.qrscanner.core.database.SirimDatabase
import com.sirim.qrscanner.core.domain.repository.AuthenticationRepository
import com.sirim.qrscanner.core.domain.repository.SirimRecordRepository
import com.sirim.qrscanner.core.domain.repository.SynchronizationRepository
import com.sirim.qrscanner.core.network.service.SirimApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = androidx.datastore.preferences.core.PreferenceDataStoreFactory.create(
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    ) {
        context.preferencesDataStoreFile("sirim_qr_scanner")
    }

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
    fun provideSirimRecordDao(database: SirimDatabase) = database.sirimRecordDao()

    @Provides
    @Singleton
    fun provideTokenStorage(dataStore: DataStore<Preferences>) = TokenStorage(dataStore)

    @Provides
    @Singleton
    fun provideLocalDataSource(dao: com.sirim.qrscanner.core.database.dao.SirimRecordDao) =
        SirimRecordLocalDataSource(dao)

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
        localDataSource: SirimRecordLocalDataSource,
        dispatcherProvider: DispatcherProvider
    ): SirimRecordRepository = SirimRecordRepositoryImpl(localDataSource, dispatcherProvider)

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
}
