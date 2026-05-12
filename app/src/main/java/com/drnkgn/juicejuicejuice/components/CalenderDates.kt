package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import java.time.LocalDate

@Composable
fun CalenderDates() {
    val start = LocalDate.now().minusDays(3)
    val selected by remember { mutableIntStateOf(3) }

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until 7) {
            DayChip(start.plusDays(i.toLong()), selected == i)
        }
    }
}

@Preview
@Composable
fun CalenderDatesPreview() {
    JuiceJuiceJuiceTheme {
        CalenderDates()
    }
}