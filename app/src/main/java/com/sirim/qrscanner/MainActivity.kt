package com.sirim.qrscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.core.view.WindowCompat
import com.sirim.qrscanner.navigation.SirimNavHost
import com.sirim.qrscanner.ui.theme.SirimTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SirimTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    SirimNavHost()
                }
            }
        }
    }
}
