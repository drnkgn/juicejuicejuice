package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors

enum class ChipTrend {
    Positive,
    Negative,
    Neutral
}

@Composable
fun getColorMap(variant: ChipTrend): List<Color> {
    return when (variant) {
        ChipTrend.Positive -> {
            listOf(MaterialTheme.extColors.success, MaterialTheme.extColors.onSuccess)
        }
        ChipTrend.Negative -> {
            listOf(MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError)
        }
        ChipTrend.Neutral -> {
            listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun Chip(
    text: String = "",
    color: ChipTrend = ChipTrend.Positive,
    trend: ChipTrend = ChipTrend.Positive
) {
    val colorMap = getColorMap(color)

    Row(
        modifier = Modifier
            .background(
                color = colorMap[0],
                shape = RoundedCornerShape(50.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            when (trend) {
                ChipTrend.Positive -> Icons.Default.ExpandLess
                ChipTrend.Negative -> Icons.Default.ExpandMore
                ChipTrend.Neutral -> Icons.Default.Remove
            },
            contentDescription = "Extra label",
            tint = colorMap[1],
            modifier = Modifier
                .padding(start = 4.dp)
                .size(14.dp)
        )
        Text(
            text,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .padding(start = 4.dp)
                .padding(end = 8.dp),
            color = colorMap[1],
            fontSize = 10.sp,
            lineHeight = 12.sp
        )
    }
}

@Preview
@Composable
fun ChipPreview() {
    JuiceJuiceJuiceTheme {
        Chip("10.45%")
    }
}
