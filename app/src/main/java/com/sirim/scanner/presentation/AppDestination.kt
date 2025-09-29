package com.sirim.scanner.presentation

sealed class AppDestination(val route: String) {
    data object Auth : AppDestination("auth")
    data object Dashboard : AppDestination("dashboard")
    data object Scan : AppDestination("scan")
    data object Records : AppDestination("records")
}
