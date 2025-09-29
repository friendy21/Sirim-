package com.sirimqrscanner.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint

import com.sirimqrscanner.app.presentation.navigation.SirimNavHost
import com.sirimqrscanner.app.presentation.theme.SirimQrScannerTheme
import com.sirimqrscanner.app.sync.SyncScheduler
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var syncScheduler: SyncScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        syncScheduler.scheduleDailySync()
        setContent {
            SirimQrScannerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    SirimNavHost()
                }
            }
        }
    }
}
