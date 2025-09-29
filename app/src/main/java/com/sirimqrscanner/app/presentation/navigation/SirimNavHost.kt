package com.sirimqrscanner.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sirimqrscanner.app.presentation.dashboard.DashboardScreen
import com.sirimqrscanner.app.presentation.export.ExportScreen
import com.sirimqrscanner.app.presentation.records.RecordsScreen
import com.sirimqrscanner.app.presentation.scanner.ScannerScreen
import com.sirimqrscanner.app.presentation.settings.SettingsScreen

@Composable
fun SirimNavHost() {
    val navController = rememberNavController()
    val destinations = listOf(
        NavDestination.Dashboard,
        NavDestination.Scanner,
        NavDestination.Records,
        NavDestination.Export,
        NavDestination.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                destinations.forEach { destination ->
                    val selected = currentDestination?.route == destination.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = when (destination) {
                                    NavDestination.Dashboard -> Icons.Outlined.Home
                                    NavDestination.Scanner -> Icons.Outlined.CameraAlt
                                    NavDestination.Records -> Icons.Outlined.Archive
                                    NavDestination.Export -> Icons.Outlined.Archive
                                    NavDestination.Settings -> Icons.Outlined.Settings
                                },
                                contentDescription = destination.route
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavDestination.Dashboard.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavDestination.Dashboard.route) {
                DashboardScreen(onStartScan = {
                    navController.navigate(NavDestination.Scanner.route)
                })
            }
            composable(NavDestination.Scanner.route) {
                ScannerScreen(onScanCompleted = { navController.navigate(NavDestination.Records.route) })
            }
            composable(NavDestination.Records.route) {
                RecordsScreen()
            }
            composable(NavDestination.Export.route) {
                ExportScreen()
            }
            composable(NavDestination.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
