package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.model.UserCredentials
import com.sirim.qrscanner.core.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class ObserveCachedCredentialsUseCase(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(): Flow<UserCredentials?> = repository.getCachedCredentials()
}
