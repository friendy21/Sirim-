package com.sirim.scanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sirim.scanner.presentation.ExportViewModelFactory
import com.sirim.scanner.presentation.RecordsViewModelFactory
import com.sirim.scanner.presentation.ScanViewModelFactory
import com.sirim.scanner.presentation.export.ExportViewModel
import com.sirim.scanner.presentation.navigation.SirimDestination
import com.sirim.scanner.presentation.navigation.SirimNavGraph
import com.sirim.scanner.presentation.records.RecordsViewModel
import com.sirim.scanner.presentation.scan.ScanViewModel
import com.sirim.scanner.presentation.theme.SirimScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as SirimScannerApp
        app.container.scheduler.scheduleSync()
        setContent {
            SirimScannerTheme {
                SirimApp(app)
            }
        }
    }
}

@Composable
private fun SirimApp(app: SirimScannerApp) {
    val navController = rememberNavController()
    val scanViewModel: ScanViewModel = viewModel(factory = ScanViewModelFactory(app.container))
    val recordsViewModel: RecordsViewModel = viewModel(factory = RecordsViewModelFactory(app.container))
    val exportViewModel: ExportViewModel = viewModel(factory = ExportViewModelFactory(app.container))

    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            SirimBottomBar(currentRoute?.destination?.route) { destination ->
                if (currentRoute?.destination?.route != destination.route) {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { paddingValues ->
        SirimNavGraph(
            navController = navController,
            scanViewModel = scanViewModel,
            recordsViewModel = recordsViewModel,
            exportViewModel = exportViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun SirimBottomBar(currentRoute: String?, onDestinationSelected: (SirimDestination) -> Unit) {
    NavigationBar {
        SirimDestination.values().forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = { onDestinationSelected(destination) },
                icon = { Icon(imageVector = destination.icon(), contentDescription = destination.label) },
                label = { Text(text = destination.label) }
            )
        }
    }
}

@Composable
private fun SirimDestination.icon(): ImageVector {
    return when (this) {
        SirimDestination.Scan -> Icons.Filled.QrCodeScanner
        SirimDestination.Records -> Icons.Filled.List
        SirimDestination.Export -> Icons.Filled.Share
    }
}
