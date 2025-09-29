package com.sirim.qrscanner.ui.state

data class AuthenticationUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false,
    val hasStoredCredentials: Boolean = false
)
