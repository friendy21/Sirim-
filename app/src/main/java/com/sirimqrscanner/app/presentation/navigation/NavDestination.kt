package com.sirimqrscanner.app.presentation.navigation

sealed class NavDestination(val route: String) {
    data object Dashboard : NavDestination("dashboard")
    data object Scanner : NavDestination("scanner")
    data object Records : NavDestination("records")
    data object Export : NavDestination("export")
    data object Settings : NavDestination("settings")
}
