package com.sirim.qrscanner.core.domain.usecase

import com.sirim.qrscanner.core.domain.repository.AuthenticationRepository

class LogoutUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
