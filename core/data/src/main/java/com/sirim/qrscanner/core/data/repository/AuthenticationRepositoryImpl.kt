package com.sirim.qrscanner.core.data.repository

import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.data.source.local.TokenStorage
import com.sirim.qrscanner.core.domain.model.UserCredentials
import com.sirim.qrscanner.core.domain.repository.AuthenticationRepository
import com.sirim.qrscanner.core.network.dto.LoginRequest
import com.sirim.qrscanner.core.network.service.SirimApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val apiService: SirimApiService,
    private val tokenStorage: TokenStorage,
    private val dispatcherProvider: DispatcherProvider
) : AuthenticationRepository {

    override val isLoggedIn: Flow<Boolean> = tokenStorage.token.map { !it.isNullOrBlank() }

    override suspend fun login(credentials: UserCredentials) {
        val response = withContext(dispatcherProvider.io) {
            apiService.login(LoginRequest(credentials.username, credentials.password))
        }
        tokenStorage.saveToken(response.token)
        tokenStorage.updateCredentials(credentials)
    }

    override suspend fun logout() {
        withContext(dispatcherProvider.io) { tokenStorage.clearToken() }
    }

    override suspend fun updateCachedCredentials(credentials: UserCredentials) {
        withContext(dispatcherProvider.io) { tokenStorage.updateCredentials(credentials) }
    }

    override fun getCachedCredentials(): Flow<UserCredentials?> = tokenStorage.credentials()
}
