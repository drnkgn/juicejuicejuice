package com.drnkgn.juicejuicejuice.screens.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.drnkgn.juicejuicejuice.components.AppTopBar
import com.drnkgn.juicejuicejuice.screens.overview.TopCard
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@Composable
fun AnalyticScreen(navController: NavController) {
    AnalyticContent()
}

@Composable
fun AnalyticContent() {
    Scaffold(
        topBar = {
            AppTopBar(title = "Analytics")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            TopCard() {
            }
        }
    }
}

@Preview
@Composable
fun AnalyticContentPreview() {
    JuiceJuiceJuiceTheme {
        AnalyticContent()
    }
}
