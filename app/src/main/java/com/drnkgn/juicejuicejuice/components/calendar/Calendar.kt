package com.drnkgn.juicejuicejuice.components.calendar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.math.exp

const val MAX_COL = 7

fun getDisplayedDays(currentDate: LocalDate): List<LocalDate> {
    val thisMonth = YearMonth.of(currentDate.year, currentDate.month)
    val thisMonthDays = (1..thisMonth.lengthOfMonth()).map { day ->
        LocalDate.of(currentDate.year, currentDate.month, day)
    }

    val lastMonthSinceNow = currentDate.minusMonths(1)
    val lastMonth = YearMonth.of(lastMonthSinceNow.year, lastMonthSinceNow.month)
    val lastMonthDays = (1..lastMonth.lengthOfMonth()).map { day ->
        LocalDate.of(lastMonth.year, lastMonth.month, day)
    }
    val lastMonthCutoff = thisMonthDays.first().dayOfWeek.value - 1 // minus one for 0 indexing
    val lastMonthCutoffDays = lastMonthDays
        .subList(lastMonthDays.size - lastMonthCutoff, lastMonthDays.size)

    val displayedDays = listOf(lastMonthCutoffDays, thisMonthDays).flatten()

    return displayedDays
}

@Composable
fun Calendar(
    selectedDate: LocalDate = LocalDate.now(),
    onValueChange: ((LocalDate) -> Unit)? = null
) {
    val density = LocalDensity.current

    val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    val currentDate = LocalDate.now()
    val displayedDays = getDisplayedDays(currentDate)

    var cellSize by remember { mutableStateOf(DpSize.Zero) }

    var expanded by remember { mutableStateOf(true) }
    var selectedRowIndex by remember { mutableIntStateOf(
        (displayedDays.indexOf(selectedDate)/MAX_COL)
    ) }

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
                titleFormatter.format(currentDate),
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

        LazyVerticalGrid(columns = GridCells.Fixed(MAX_COL)) {
            items(1) { dayOfWeek ->
                DayCell(
                    dayOfWeek,
                    modifier = Modifier
                        .onSizeChanged {
                            cellSize = with (density) {
                                DpSize(it.width.toDp(), it.height.toDp())
                            }
                        },
                    isDayOfWeek = true,
                )
            }
            items((1..6).toList()) { dayOfWeek ->
                DayCell(dayOfWeek, isDayOfWeek = true)
            }
        }

        (0..<(displayedDays.size/MAX_COL)).map { row ->
            val isSelectedRow = row == selectedRowIndex
            val alphaAnimate by animateFloatAsState(
                targetValue = when {
                    !isSelectedRow -> when {
                        expanded -> 1f
                        else -> 0f
                    }
                    else -> 1f
                }
            )

            val shinkHeightAnimate by animateDpAsState(
                targetValue = when {
                    !isSelectedRow -> when {
                        expanded -> cellSize.height
                        else -> 0.dp
                    }
                    else -> cellSize.height
                }
            )

            Box(
                modifier = Modifier
                    .height(shinkHeightAnimate)
                    .alpha(alphaAnimate)
            ) {
                LazyVerticalGrid(columns = GridCells.Fixed(MAX_COL)) {
                    items(displayedDays.subList(row * MAX_COL, (row + 1) * MAX_COL)) { day ->
                        DayCell(
                            day.dayOfMonth,
                            isCutoff = day.month != currentDate.month,
                            isToday = day == currentDate,
                            isSelected = day == selectedDate,
                            onClick = {
                                val index = displayedDays.indexOf(day)
                                selectedRowIndex = row

                                onValueChange?.invoke(day)
                            }
                        )
                    }
                }
            }
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