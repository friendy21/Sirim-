package com.sirim.scanner

import android.app.Application
import com.sirim.scanner.di.AppContainer

class SirimScannerApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
