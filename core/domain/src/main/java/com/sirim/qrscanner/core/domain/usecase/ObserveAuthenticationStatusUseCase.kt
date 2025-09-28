package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthenticationStatusUseCase(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isLoggedIn
}
