package com.drnkgn.juicejuicejuice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.drnkgn.juicejuicejuice.screens.AppNavigation
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JuiceJuiceJuiceTheme {
                AppNavigation()
            }
        }
    }
}