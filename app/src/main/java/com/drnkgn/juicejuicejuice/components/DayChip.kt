package com.drnkgn.juicejuicejuice.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DayChip(
    date: LocalDate = LocalDate.now(),
    selected: Boolean = false
) {
    Log.d("JJJ", MaterialTheme.colorScheme.primary.toString())

    Column(
        modifier = Modifier
            .background(
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = if (selected) 0.dp else 1.dp,
                color = if (selected) Color.Transparent else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date.format(DateTimeFormatter.ofPattern("E")),
            fontSize = 10.sp,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
            lineHeight = 15.sp
        )
        Text(
            text = date.format(DateTimeFormatter.ofPattern("d")),
            fontSize = 16.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview
@Composable
fun DayChipPreview() {
    JuiceJuiceJuiceTheme {
        DayChip(LocalDate.now())
    }
}