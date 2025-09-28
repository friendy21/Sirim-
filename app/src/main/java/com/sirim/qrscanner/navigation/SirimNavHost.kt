package com.sirim.qrscanner.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sirim.qrscanner.ui.screens.auth.AuthenticationScreen
import com.sirim.qrscanner.ui.screens.dashboard.DashboardScreen
import com.sirim.qrscanner.ui.screens.records.RecordListScreen
import com.sirim.qrscanner.ui.screens.scanner.ScannerScreen
import com.sirim.qrscanner.viewmodel.AuthenticationViewModel
import com.sirim.qrscanner.viewmodel.DashboardViewModel
import com.sirim.qrscanner.viewmodel.RecordListViewModel
import com.sirim.qrscanner.viewmodel.ScannerViewModel

@Composable
fun SirimNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = SirimDestination.Authentication.route
    ) {
        composable(route = SirimDestination.Authentication.route) {
            val viewModel: AuthenticationViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            AuthenticationScreen(
                state = state,
                onUsernameChange = viewModel::updateUsername,
                onPasswordChange = viewModel::updatePassword,
                onLogin = viewModel::login,
                onErrorDismissed = viewModel::clearError,
                onNavigateToDashboard = {
                    navController.navigate(SirimDestination.Dashboard.route) {
                        popUpTo(SirimDestination.Authentication.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = SirimDestination.Dashboard.route) {
            val viewModel: DashboardViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            DashboardScreen(
                state = state,
                onOpenScanner = { navController.navigate(SirimDestination.Scanner.route) },
                onOpenRecords = { navController.navigate(SirimDestination.Records.route) },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(SirimDestination.Authentication.route) {
                        popUpTo(SirimDestination.Dashboard.route) { inclusive = true }
                    }
                },
                onSync = viewModel::synchronize
            )
        }
        composable(route = SirimDestination.Records.route) {
            val viewModel: RecordListViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            RecordListScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onRefresh = viewModel::refresh,
                onDeleteRecord = viewModel::deleteRecord,
                onQueryChange = viewModel::updateQuery
            )
        }
        composable(route = SirimDestination.Scanner.route) {
            val viewModel: ScannerViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            ScannerScreen(
                state = state,
                onRecordConfirmed = { record ->
                    viewModel.saveRecord(record)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() },
                onManualEntry = viewModel::updateDraft
            )
        }
    }
}
