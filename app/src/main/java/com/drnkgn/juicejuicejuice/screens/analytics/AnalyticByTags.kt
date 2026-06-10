package com.drnkgn.juicejuicejuice.screens.analytics

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun AnalyticByTags() {
    ChartLayout {
        PieChart(
            values = listOf(1.0f, 1.0f, 2.0f, 3.0f, 5.0f, 8.0f, 13.0f, 21.0f)
        )
    }
}

@Preview
@Composable
fun AnalyticByTagsPreview() {
    JuiceJuiceJuiceTheme {
        AnalyticByTags()
    }
}
