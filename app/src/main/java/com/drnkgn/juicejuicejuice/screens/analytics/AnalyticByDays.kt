package com.drnkgn.juicejuicejuice.screens.analytics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceA20
import com.drnkgn.juicejuicejuice.ui.theme.extColors
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultBar
import io.github.koalaplot.core.bar.DefaultBarPosition
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.AxisContent
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.rememberAxisStyle
import io.github.koalaplot.core.xygraph.rememberGridStyle

private const val BarWidth = 0.3f
private val YAxisRange = 0f..1f
private val XAxisRange = -0.5f..6.5f

private fun weekDaysMap(idx: Float) = when (idx) {
    0f -> "M"
    1f -> "T"
    2f -> "W"
    3f -> "T"
    4f -> "F"
    5f -> "S"
    6f -> "S"
    else -> ""
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun AnalyticByDays() {
    val barChartEntries = listOf(
        DefaultVerticalBarPlotEntry(0f, DefaultBarPosition(0f, 0.1f)),
        DefaultVerticalBarPlotEntry(1f, DefaultBarPosition(0f, 0.2f)),
        DefaultVerticalBarPlotEntry(2f, DefaultBarPosition(0f, 0.3f)),
        DefaultVerticalBarPlotEntry(3f, DefaultBarPosition(0f, 0.4f)),
        DefaultVerticalBarPlotEntry(4f, DefaultBarPosition(0f, 0.5f)),
        DefaultVerticalBarPlotEntry(5f, DefaultBarPosition(0f, 0.6f)),
        DefaultVerticalBarPlotEntry(6f, DefaultBarPosition(0f, 0.7f)),
    )

    ChartLayout {
        XYGraph(
            modifier = Modifier.height(120.dp),
            xAxisModel = FloatLinearAxisModel(
                XAxisRange,
                minimumMajorTickIncrement = 1f,
                minimumMajorTickSpacing = 10.dp,
                minViewExtent = 3f,
                minorTickCount = 0,
            ),
            yAxisModel = FloatLinearAxisModel(
                YAxisRange,
                minimumMajorTickIncrement = 0.5f,
                minorTickCount = 0,
            ),
            xAxisContent = AxisContent(
                labels = { it ->
                    Text(
                        weekDaysMap(it),
                        color = MaterialTheme.extColors.placeholder,
                        fontSize = 10.sp
                    )
                },
                title = { },
                style = rememberAxisStyle(
                    majorTickSize = 0.dp,
                    color = Color.Transparent
                ),
            ),
            yAxisContent = AxisContent(
                labels = { },
                title = { },
                style = rememberAxisStyle(
                    majorTickSize = 0.dp,
                    color = Color.Transparent
                ),
            ),
            gridStyle = rememberGridStyle(
                horizontalMajorStyle = LineStyle(
                    SolidColor(SurfaceA20))
                ,
                horizontalMinorStyle = null,
                verticalMajorStyle = null,
                verticalMinorStyle = null
            )
        ) {
            VerticalBarPlot(
                barChartEntries,
                bar = { _, _, _ ->
                    DefaultBar(
                        brush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            .fillMaxWidth(),
                    )
                },
                barWidth = BarWidth,
            )
        }
    }
}

@Preview
@Composable
private fun AnalyticByDaysPreview() {
    JuiceJuiceJuiceTheme {
        AnalyticByDays()
    }
}
