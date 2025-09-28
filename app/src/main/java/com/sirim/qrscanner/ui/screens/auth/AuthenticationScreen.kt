package com.sirim.qrscanner.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.sirim.qrscanner.ui.components.LoadingOverlay
import com.sirim.qrscanner.ui.components.PrimaryButton
import com.sirim.qrscanner.ui.state.AuthenticationUiState

@Composable
fun AuthenticationScreen(
    state: AuthenticationUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onErrorDismissed: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            onNavigateToDashboard()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome back",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Log in with your secure credentials to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        TextField(
            value = state.username,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Username") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = "Log in",
            enabled = !state.isLoading,
            onClick = onLogin
        )
    }

    if (state.errorMessage != null) {
        AlertDialog(
            onDismissRequest = onErrorDismissed,
            confirmButton = {
                TextButton(onClick = onErrorDismissed) {
                    Text("Dismiss")
                }
            },
            title = { Text("Authentication failed") },
            text = { Text(state.errorMessage) }
        )
    }

    LoadingOverlay(isVisible = state.isLoading)
}
