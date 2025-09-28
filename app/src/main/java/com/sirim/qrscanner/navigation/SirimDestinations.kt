package com.sirim.qrscanner.navigation

sealed class SirimDestination(val route: String) {
    data object Authentication : SirimDestination("auth")
    data object Dashboard : SirimDestination("dashboard")
    data object Records : SirimDestination("records")
    data object Scanner : SirimDestination("scanner")
}
