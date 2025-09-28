package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.model.UserCredentials
import com.sirim.qrscanner.core.domain.repository.AuthenticationRepository

class LoginUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(credentials: UserCredentials) {
        repository.login(credentials)
    }
}
