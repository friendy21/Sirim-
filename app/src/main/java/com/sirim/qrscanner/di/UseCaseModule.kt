package com.sirim.qrscanner.di

import com.sirim.qrscanner.core.domain.repository.AuthenticationRepository
import com.sirim.qrscanner.core.domain.repository.SirimRecordRepository
import com.sirim.qrscanner.core.domain.repository.SynchronizationRepository
import com.sirim.qrscanner.core.domain.usecase.DeleteRecordUseCase
import com.sirim.qrscanner.core.domain.usecase.GetLastSynchronizationTimestampUseCase
import com.sirim.qrscanner.core.domain.usecase.LoginUseCase
import com.sirim.qrscanner.core.domain.usecase.LogoutUseCase
import com.sirim.qrscanner.core.domain.usecase.ObserveAuthenticationStatusUseCase
import com.sirim.qrscanner.core.domain.usecase.ObserveCachedCredentialsUseCase
import com.sirim.qrscanner.core.domain.usecase.ObserveRecordsUseCase
import com.sirim.qrscanner.core.domain.usecase.RefreshRecordsUseCase
import com.sirim.qrscanner.core.domain.usecase.SaveRecordUseCase
import com.sirim.qrscanner.core.domain.usecase.SearchRecordsUseCase
import com.sirim.qrscanner.core.domain.usecase.SynchronizeRecordsUseCase
import com.sirim.qrscanner.core.domain.usecase.UpdateCachedCredentialsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideObserveRecordsUseCase(repository: SirimRecordRepository) = ObserveRecordsUseCase(repository)

    @Provides
    @Singleton
    fun provideRefreshRecordsUseCase(repository: SirimRecordRepository) = RefreshRecordsUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchRecordsUseCase(repository: SirimRecordRepository) = SearchRecordsUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveRecordUseCase(repository: SirimRecordRepository) = SaveRecordUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteRecordUseCase(repository: SirimRecordRepository) = DeleteRecordUseCase(repository)

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthenticationRepository) = LoginUseCase(repository)

    @Provides
    @Singleton
    fun provideLogoutUseCase(repository: AuthenticationRepository) = LogoutUseCase(repository)

    @Provides
    @Singleton
    fun provideObserveAuthenticationStatusUseCase(repository: AuthenticationRepository) =
        ObserveAuthenticationStatusUseCase(repository)

    @Provides
    @Singleton
    fun provideObserveCachedCredentialsUseCase(repository: AuthenticationRepository) =
        ObserveCachedCredentialsUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateCachedCredentialsUseCase(repository: AuthenticationRepository) =
        UpdateCachedCredentialsUseCase(repository)

    @Provides
    @Singleton
    fun provideSynchronizeRecordsUseCase(repository: SynchronizationRepository) =
        SynchronizeRecordsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLastSynchronizationTimestampUseCase(repository: SynchronizationRepository) =
        GetLastSynchronizationTimestampUseCase(repository)
}
