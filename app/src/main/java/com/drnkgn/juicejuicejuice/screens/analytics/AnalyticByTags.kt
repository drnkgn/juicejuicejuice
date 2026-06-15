package com.drnkgn.juicejuicejuice.screens.analytics

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.db.dto.TagSpend
import com.drnkgn.juicejuicejuice.ui.theme.DangerA20
import com.drnkgn.juicejuicejuice.ui.theme.InfoA20
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.PrimaryA20
import com.drnkgn.juicejuicejuice.ui.theme.SuccessA10
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceA50
import com.drnkgn.juicejuicejuice.ui.theme.WarningA20
import com.drnkgn.juicejuicejuice.ui.theme.extColors
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.legend.ColumnLegend2
import io.github.koalaplot.core.legend.LegendLocation
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import java.util.Locale

val PieCharColors = listOf(
    SuccessA10,
    InfoA20,
    WarningA20,
    DangerA20,
    PrimaryA20,
    SurfaceA50      // for "others"
)

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun AnalyticByTags(tagSpending: List<TagSpend>) {
    val sorted = tagSpending.sortedByDescending { it.amountSum }
    val mainEntries = sorted.take(5).toMutableList()

    if (mainEntries.size > 5) {
        val restEntriesSum = sorted.drop(5).fold(0f) { acc, spend -> acc + spend.amountSum }
        mainEntries.add(TagSpend(-1, "other", restEntriesSum))
    }

    val pieChartEntries = mainEntries.toList()

    ChartLayout(
        legendLocation = LegendLocation.RIGHT,
        legend = {
            ColumnLegend2(
                pieChartEntries.size,
                modifier = Modifier.padding(start = 20.dp),
                symbol = { i ->
                    Symbol(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(8.dp),
                        fillBrush = SolidColor(PieCharColors[i]),
                    )
                },
                label = { i ->
                    Text(
                        pieChartEntries[i].tagName,
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontSize = 14.sp
                    )
                },
                value = { i ->
                    val sum = pieChartEntries.fold(0f) { acc, spend -> acc + spend.amountSum }
                    val percent = (pieChartEntries[i].amountSum / sum) * 100f
                    Text(
                        "%.1f%%".format(Locale.UK, percent),
                        color = MaterialTheme.extColors.placeholder,
                        modifier = Modifier.align(Alignment.End),
                        fontSize = 12.sp
                    )
                },
            )
        }
    ) {
        PieChart(
            holeSize = 0.6f,
            labelConnector = { },
            values = pieChartEntries.map { it.amountSum },
            slice = { i ->
                DefaultSlice(
                    antiAlias = true,
                    gap = 1.5f,
                    color = PieCharColors[i]
                )
            }
        )
    }
}

@Preview
@Composable
fun AnalyticByTagsPreview() {
    JuiceJuiceJuiceTheme {
        AnalyticByTags(
            tagSpending = listOf(
                TagSpend(0, "food", 102.3f),
                TagSpend(0, "gas", 80.5f),
                TagSpend(0, "rent", 550f),
                TagSpend(0, "loan", 120f),
            )
        )
    }
}
