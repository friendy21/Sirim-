package com.sirim.scanner

import android.app.Application
import com.sirim.scanner.data.local.SirimScannerDatabase
import com.sirim.scanner.data.repository.LocalAuthRepository
import com.sirim.scanner.data.repository.LocalSirimRecordRepository
import com.sirim.scanner.domain.usecase.AuthenticateUserUseCase
import com.sirim.scanner.domain.usecase.CreateOrUpdateRecordUseCase
import com.sirim.scanner.domain.usecase.DeleteRecordUseCase
import com.sirim.scanner.domain.usecase.ExportRecordsUseCase
import com.sirim.scanner.domain.usecase.ObserveRecordsUseCase
import com.sirim.scanner.domain.usecase.RegisterUserUseCase
import com.sirim.scanner.domain.usecase.ToggleBiometricPreferenceUseCase
import com.sirim.scanner.util.DispatchersProvider
import com.sirim.scanner.util.StandardDispatchers

class SirimScannerApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer(this, StandardDispatchers)
    }
}

interface AppContainer {
    val dispatchers: DispatchersProvider
    val authenticateUser: AuthenticateUserUseCase
    val registerUser: RegisterUserUseCase
    val observeRecords: ObserveRecordsUseCase
    val createOrUpdateRecord: CreateOrUpdateRecordUseCase
    val deleteRecord: DeleteRecordUseCase
    val exportRecords: ExportRecordsUseCase
    val toggleBiometricPreference: ToggleBiometricPreferenceUseCase
}

class DefaultAppContainer(
    application: Application,
    override val dispatchers: DispatchersProvider,
) : AppContainer {

    private val database = SirimScannerDatabase.create(application)

    private val recordRepository = LocalSirimRecordRepository(
        recordDao = database.recordDao(),
        dispatchers = dispatchers
    )
    private val authRepository = LocalAuthRepository(
        userDao = database.userDao(),
        dispatchers = dispatchers
    )

    override val authenticateUser = AuthenticateUserUseCase(authRepository)
    override val registerUser = RegisterUserUseCase(authRepository)
    override val observeRecords = ObserveRecordsUseCase(recordRepository)
    override val createOrUpdateRecord = CreateOrUpdateRecordUseCase(recordRepository)
    override val deleteRecord = DeleteRecordUseCase(recordRepository)
    override val exportRecords = ExportRecordsUseCase(recordRepository)
    override val toggleBiometricPreference = ToggleBiometricPreferenceUseCase(authRepository)
}
