package com.sirim.scanner.domain.repository

import com.sirim.scanner.domain.model.UserAccount
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(username: String, password: String): Result<Unit>
    suspend fun authenticate(username: String, password: String): Result<UserAccount>
    suspend fun updateLastLogin(userId: Long)
    suspend fun setBiometricEnabled(userId: Long, enabled: Boolean)
    fun observeCurrentUser(): Flow<UserAccount?>
}
