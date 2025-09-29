package com.sirim.scanner.domain.usecase

import com.sirim.scanner.domain.repository.AuthRepository

class RegisterUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> =
        authRepository.register(username, password)
}
