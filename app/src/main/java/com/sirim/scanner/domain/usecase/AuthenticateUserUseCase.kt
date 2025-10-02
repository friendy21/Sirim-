package com.sirim.scanner.domain.usecase

import com.sirim.scanner.domain.model.UserAccount
import com.sirim.scanner.domain.repository.AuthRepository

class AuthenticateUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Result<UserAccount> =
        authRepository.authenticate(username, password)
}
