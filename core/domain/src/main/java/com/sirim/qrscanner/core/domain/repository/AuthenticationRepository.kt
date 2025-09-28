package com.sirim.qrscanner.core.domain.repository

import com.sirim.qrscanner.core.domain.model.UserCredentials
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val isLoggedIn: Flow<Boolean>
    suspend fun login(credentials: UserCredentials)
    suspend fun logout()
    suspend fun updateCachedCredentials(credentials: UserCredentials)
    fun getCachedCredentials(): Flow<UserCredentials?>
}
