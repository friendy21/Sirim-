package com.sirim.scanner.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sirim.scanner.presentation.records.RecordsScreen
import com.sirim.scanner.presentation.records.RecordsViewModel
import com.sirim.scanner.presentation.scan.ScanScreen
import com.sirim.scanner.presentation.scan.ScanViewModel
import com.sirim.scanner.presentation.export.ExportScreen
import com.sirim.scanner.presentation.export.ExportViewModel

@Composable
fun SirimNavGraph(
    navController: NavHostController,
    scanViewModel: ScanViewModel,
    recordsViewModel: RecordsViewModel,
    exportViewModel: ExportViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SirimDestination.Scan.route,
        modifier = modifier
    ) {
        composable(SirimDestination.Scan.route) {
            ScanScreen(viewModel = scanViewModel)
        }
        composable(SirimDestination.Records.route) {
            RecordsScreen(viewModel = recordsViewModel)
        }
        composable(SirimDestination.Export.route) {
            ExportScreen(viewModel = exportViewModel)
        }
    }
}
