package com.sirim.scanner.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirim.scanner.domain.model.UserAccount
import com.sirim.scanner.domain.usecase.AuthenticateUserUseCase
import com.sirim.scanner.domain.usecase.RegisterUserUseCase
import com.sirim.scanner.domain.usecase.ToggleBiometricPreferenceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authenticateUser: AuthenticateUserUseCase,
    private val registerUser: RegisterUserUseCase,
    private val toggleBiometricPreference: ToggleBiometricPreferenceUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun signIn() {
        val username = _uiState.value.username
        val password = _uiState.value.password
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Username and password are required")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            authenticateUser(username, password)
                .onSuccess { account ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        authenticatedUser = account,
                        error = null
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Unable to authenticate"
                    )
                }
        }
    }

    fun register() {
        val username = _uiState.value.username
        val password = _uiState.value.password
        if (username.length < 4 || password.length < 8) {
            _uiState.value = _uiState.value.copy(error = "Credentials do not meet complexity requirements")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            registerUser(username, password)
                .onSuccess {
                    signIn()
                }
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Unable to register"
                    )
                }
        }
    }

    fun updateBiometricPreference(enabled: Boolean) {
        val user = _uiState.value.authenticatedUser ?: return
        viewModelScope.launch {
            toggleBiometricPreference(user.id, enabled)
            _uiState.value = _uiState.value.copy(
                authenticatedUser = user.copy(biometricEnabled = enabled)
            )
        }
    }

    fun signOut() {
        _uiState.value = AuthUiState()
    }
}

data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val authenticatedUser: UserAccount? = null,
)
