package com.drnkgn.juicejuicejuice.components.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

const val MAX_COL = 7

fun getDisplayedDays(pivotDate: LocalDate): List<LocalDate> {
    val pivotMonth = YearMonth.of(pivotDate.year, pivotDate.month)
    val pivotMonthDays = (1..pivotMonth.lengthOfMonth()).map { day ->
        LocalDate.of(pivotDate.year, pivotDate.month, day)
    }

    val previousMonthSincePivot = pivotDate.minusMonths(1)
    val previousMonth = YearMonth.of(previousMonthSincePivot.year, previousMonthSincePivot.month)
    val previousMonthDays = (1..previousMonth.lengthOfMonth()).map { day ->
        LocalDate.of(previousMonth.year, previousMonth.month, day)
    }
    val previousMonthCutoff = pivotMonthDays.first().dayOfWeek.value - 1 // minus one for 0 indexing
    val previousMonthCutoffDays = previousMonthDays
        .subList(previousMonthDays.size - previousMonthCutoff, previousMonthDays.size)

    val nextMonthSincePivot = pivotDate.plusMonths(1)
    val nextMonth = YearMonth.of(nextMonthSincePivot.year, nextMonthSincePivot.month)
    val nextMonthDays = (1..nextMonth.lengthOfMonth()).map { day ->
        LocalDate.of(nextMonth.year, nextMonth.month, day)
    }
    val nextMonthCutoff = MAX_COL - pivotMonthDays.last().dayOfWeek.value
    val nextMonthCutoffDays = nextMonthDays
        .subList(0, nextMonthCutoff)

    val displayedDays = listOf(previousMonthCutoffDays, pivotMonthDays, nextMonthCutoffDays).flatten()

    return displayedDays
}

@Composable
fun Calendar(
    selectedDate: LocalDate = LocalDate.now(),
    onValueChange: ((LocalDate) -> Unit)? = null
) {
    val pageCount = Int.MAX_VALUE
    val pageCenter = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(initialPage = pageCenter, pageCount = { pageCount })

    val keepRange = 3
    val cache = remember { mutableStateMapOf<Int, List<LocalDate>>() }

    val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    val currentDate = LocalDate.now()

    var pivotDate by remember { mutableStateOf(currentDate) }
    var expanded by remember { mutableStateOf(true) }

    LaunchedEffect(pagerState.settledPage) {
        val settledPage = pagerState.settledPage

        pivotDate = currentDate.plusMonths((settledPage - pageCenter).toLong())
        for (p in (settledPage - keepRange)..(settledPage + keepRange)) {
            if (cache[p] == null) {
                val signedIndex = (p - pageCenter).toLong()
                val date = currentDate.plusMonths(signedIndex)
                val result = withContext(Dispatchers.Default) { getDisplayedDays(date) }

                cache[p] = result
            }
        }

        val min = settledPage - keepRange
        val max = settledPage + keepRange
        val toRemove = cache.keys.filter { it !in min..max }
        toRemove.forEach { cache.remove(it) }
    }

    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                titleFormatter.format(pivotDate),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            TextButton(
                onClick = { expanded = !expanded }
            ) {
                Text(
                    text = when {
                        expanded -> "collapse"
                        else -> "expand"
                    },
                    fontWeight = FontWeight.Normal
                )
            }
        }

        HorizontalPager(
            state = pagerState
        ) { page ->
            val gson = GsonBuilder()
                .registerTypeAdapter(LocalDate::class.java, JsonSerializer<LocalDate> { src, _, _ ->
                    JsonPrimitive(src.toString())
                })
                .create()

            val json = gson.toJson(cache)

            Log.d("JJJ", json)
            CalendarDates(
                cache[pagerState.targetPage] ?: getDisplayedDays(pivotDate),
                pivotDate, selectedDate, expanded, onValueChange
            )
        }
    }
}

@Preview
@Composable
fun CalendarPreview() {
    JuiceJuiceJuiceTheme {
        Calendar(
            selectedDate = LocalDate.now(),
        )
    }
}