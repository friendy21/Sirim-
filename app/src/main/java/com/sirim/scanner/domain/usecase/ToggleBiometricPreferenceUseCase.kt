package com.sirim.scanner.domain.usecase

import com.sirim.scanner.domain.repository.AuthRepository

class ToggleBiometricPreferenceUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(userId: Long, enabled: Boolean) {
        repository.setBiometricEnabled(userId, enabled)
    }
}
