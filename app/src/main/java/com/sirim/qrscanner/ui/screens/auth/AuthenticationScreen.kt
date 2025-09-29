package com.sirim.qrscanner.ui.screens.auth

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.sirim.qrscanner.ui.components.LoadingOverlay
import com.sirim.qrscanner.ui.components.PrimaryButton
import com.sirim.qrscanner.ui.state.AuthenticationUiState
import java.util.concurrent.Executor
import androidx.fragment.app.FragmentActivity

@Composable
fun AuthenticationScreen(
    state: AuthenticationUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onErrorDismissed: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onBiometricLogin: () -> Unit,
    onBiometricFailure: (String) -> Unit
) {
    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            onNavigateToDashboard()
        }
    }

    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val executor: Executor = remember(context) { ContextCompat.getMainExecutor(context) }
    val biometricManager = remember(context) { BiometricManager.from(context) }
    val canAuthenticate = remember(state.hasStoredCredentials, biometricManager) {
        state.hasStoredCredentials &&
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
            BiometricManager.BIOMETRIC_SUCCESS
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Confirm your identity to use saved credentials")
            .setNegativeButtonText("Cancel")
            .build()
    }

    val biometricPrompt = remember(activity) {
        if (activity == null) {
            null
        } else {
            BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onBiometricLogin()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onBiometricFailure(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    onBiometricFailure("Biometric authentication failed")
                }
            })
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
        if (canAuthenticate && biometricPrompt != null) {
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = "Use biometrics",
                enabled = !state.isLoading,
                onClick = { biometricPrompt.authenticate(promptInfo) }
            )
        }
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
