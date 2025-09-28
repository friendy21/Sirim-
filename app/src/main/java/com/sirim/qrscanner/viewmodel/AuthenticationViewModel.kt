package com.sirim.qrscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.qrscanner.core.common.DispatcherProvider
import com.sirim.qrscanner.core.domain.model.UserCredentials
import com.sirim.qrscanner.core.domain.usecase.LoginUseCase
import com.sirim.qrscanner.core.domain.usecase.ObserveAuthenticationStatusUseCase
import com.sirim.qrscanner.core.domain.usecase.ObserveCachedCredentialsUseCase
import com.sirim.qrscanner.core.domain.usecase.UpdateCachedCredentialsUseCase
import com.sirim.qrscanner.ui.state.AuthenticationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val observeAuthenticationStatusUseCase: ObserveAuthenticationStatusUseCase,
    observeCachedCredentialsUseCase: ObserveCachedCredentialsUseCase,
    private val updateCachedCredentialsUseCase: UpdateCachedCredentialsUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(AuthenticationUiState())
    val uiState: StateFlow<AuthenticationUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            observeCachedCredentialsUseCase().collectLatest { credentials ->
                if (credentials != null) {
                    _state.value = _state.value.copy(
                        username = credentials.username,
                        password = credentials.password
                    )
                }
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {
            observeAuthenticationStatusUseCase().collectLatest { isLoggedIn ->
                _state.value = _state.value.copy(isAuthenticated = isLoggedIn, isLoading = false)
            }
        }
    }

    fun updateUsername(username: String) {
        _state.value = _state.value.copy(username = username)
        persistCredentials()
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
        persistCredentials()
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    fun login() {
        val credentials = UserCredentials(_state.value.username, _state.value.password)
        viewModelScope.launch(dispatcherProvider.io) {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            runCatching { loginUseCase(credentials) }
                .onFailure { throwable ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to authenticate"
                    )
                }
        }
    }

    private fun persistCredentials() {
        val credentials = UserCredentials(_state.value.username, _state.value.password)
        viewModelScope.launch(dispatcherProvider.io) {
            updateCachedCredentialsUseCase(credentials)
        }
    }
}
