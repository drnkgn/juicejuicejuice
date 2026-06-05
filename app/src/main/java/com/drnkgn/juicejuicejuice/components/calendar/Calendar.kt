package com.drnkgn.juicejuicejuice.components.calendar

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.utils.stateMapSaver
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

    val previousMonth = pivotDate.minusMonths(1)
    val previousMonthCutoff = previousMonth.lengthOfMonth() - (pivotMonthDays.first().dayOfWeek.value - 1)
    val previousMonthCutoffDays = ((previousMonthCutoff+1)..previousMonth.lengthOfMonth()).map { day ->
        LocalDate.of(previousMonth.year, previousMonth.month, day)
    }

    val nextMonth = pivotDate.plusMonths(1)
    val nextMonthCutoff = MAX_COL - pivotMonthDays.last().dayOfWeek.value
    val nextMonthCutoffDays = (1..nextMonthCutoff).map { day ->
        LocalDate.of(nextMonth.year, nextMonth.month, day)
    }

    return listOf(previousMonthCutoffDays, pivotMonthDays, nextMonthCutoffDays).flatten()
}

@Composable
fun Calendar(
    selectedDate: LocalDate = LocalDate.now(),
    onValueChange: ((LocalDate) -> Unit)? = null
) {
    val pageCount = Int.MAX_VALUE
    val pageCenter = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(initialPage = pageCenter, pageCount = { pageCount })

    val keepRange = 2
    val cache = rememberSaveable(saver = stateMapSaver<Int, List<LocalDate>>()) {
        mutableStateMapOf()
    }

    val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    val currentDate = LocalDate.now()

    var currentlyShownDate by remember { mutableStateOf(currentDate) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.settledPage) {
        val settledPage = pagerState.settledPage

        currentlyShownDate = currentDate.plusMonths((settledPage - pageCenter).toLong())
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
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                titleFormatter.format(currentlyShownDate),
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
            state = pagerState,
            userScrollEnabled = expanded
        ) { _ ->
            val targetPage = pagerState.targetPage
            val localPivotDate = currentDate.plusMonths((targetPage - pageCenter).toLong())

            CalendarDates(
                cache[targetPage] ?: getDisplayedDays(localPivotDate),
                currentDate, localPivotDate, selectedDate, expanded, onValueChange
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