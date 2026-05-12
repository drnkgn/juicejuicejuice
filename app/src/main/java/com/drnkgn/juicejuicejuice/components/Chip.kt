package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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

@Composable
fun getColorMap(variant: String = "success"): List<Color> {
    return when (variant) {
        "success" -> {
            listOf(MaterialTheme.extColors.success, MaterialTheme.extColors.onSuccess)
        }
        "error" -> {
            listOf(MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError)
        }
        else -> {
            listOf(MaterialTheme.extColors.success, MaterialTheme.extColors.onSuccess)
        }
    }
}

@Composable
fun Chip(text: String = "", variant: String = "success") {
    val colorMap = getColorMap(variant)

    Row(
        modifier = Modifier
            .background(
                color = colorMap[0],
                shape = RoundedCornerShape(50.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            when (variant) {
                "success" -> Icons.Default.ExpandLess
                else -> Icons.Default.ExpandMore
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
