package com.sirim.scanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sirim.scanner.presentation.AppDestination
import com.sirim.scanner.presentation.auth.AuthScreen
import com.sirim.scanner.presentation.auth.AuthViewModel
import com.sirim.scanner.presentation.dashboard.DashboardScreen
import com.sirim.scanner.presentation.records.RecordsScreen
import com.sirim.scanner.presentation.records.RecordsViewModel
import com.sirim.scanner.presentation.scan.ScanScreen
import com.sirim.scanner.presentation.scan.ScanViewModel
import com.sirim.scanner.presentation.theme.SirimScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appContainer = (application as SirimScannerApplication).appContainer
            SirimScannerTheme {
                SirimScannerApp(appContainer)
            }
        }
    }
}

@Composable
private fun SirimScannerApp(appContainer: AppContainer) {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(
                    authenticateUser = appContainer.authenticateUser,
                    registerUser = appContainer.registerUser,
                    toggleBiometricPreference = appContainer.toggleBiometricPreference
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    })

    val recordsViewModel: RecordsViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecordsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecordsViewModel(
                    observeRecords = appContainer.observeRecords,
                    saveRecordUseCase = appContainer.createOrUpdateRecord,
                    deleteRecordUseCase = appContainer.deleteRecord,
                    exportRecordsUseCase = appContainer.exportRecords
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    })

    val scanViewModel: ScanViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ScanViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    })

    val authState by authViewModel.uiState.collectAsState()

    if (!authState.isAuthenticated) {
        AuthScreen(
            state = authState,
            onUsernameChange = authViewModel::onUsernameChange,
            onPasswordChange = authViewModel::onPasswordChange,
            onSignIn = authViewModel::signIn,
            onRegister = authViewModel::register
        )
    } else {
        NavHost(navController = navController, startDestination = AppDestination.Dashboard.route) {
            composable(AppDestination.Dashboard.route) {
                val recordsState by recordsViewModel.uiState.collectAsState()
                DashboardScreen(
                    records = recordsState.records,
                    onNavigateToScan = { navController.navigate(AppDestination.Scan.route) },
                    onNavigateToRecords = { navController.navigate(AppDestination.Records.route) },
                    onLogout = {
                        authViewModel.signOut()
                        navController.navigate(AppDestination.Auth.route) {
                            popUpTo(AppDestination.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppDestination.Scan.route) {
                ScanScreen(
                    viewModel = scanViewModel,
                    onRecordCaptured = { record ->
                        recordsViewModel.saveRecord(record)
                        navController.navigate(AppDestination.Records.route)
                    }
                )
            }
            composable(AppDestination.Records.route) {
                val recordsState by recordsViewModel.uiState.collectAsState()
                RecordsScreen(
                    state = recordsState,
                    onSearchChange = recordsViewModel::onSearchQueryChange,
                    onSaveRecord = recordsViewModel::saveRecord,
                    onDeleteRecord = recordsViewModel::deleteRecord,
                    onExportExcel = recordsViewModel::exportRecordsExcel,
                    onExportPdf = recordsViewModel::exportRecordsPdf,
                    onExportBundle = recordsViewModel::exportRecordsBundle,
                    onMessageShown = recordsViewModel::clearTransientMessages
                )
            }
            composable(AppDestination.Auth.route) {
                // This destination allows navigation when logging out to reuse AuthScreen.
            }
        }
    }
}
