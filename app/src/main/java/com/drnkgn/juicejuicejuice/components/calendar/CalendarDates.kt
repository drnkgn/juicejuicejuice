package com.drnkgn.juicejuicejuice.components.calendar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun CalendarDates(
    days: List<LocalDate>,
    currentDate: LocalDate = LocalDate.now(),
    pivotDate: LocalDate,
    selectedDate: LocalDate,
    expanded: Boolean,
    onValueChange: ((LocalDate) -> Unit)? = null
) {
    val density = LocalDensity.current
    var cellSize by remember { mutableStateOf(DpSize.Zero) }

    var selectedRowIndex by remember { mutableIntStateOf(
        (days.indexOf(selectedDate)/MAX_COL)
    ) }

    Column() {
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

        (0..<(days.size/MAX_COL)).map { row ->
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
                    items(days.subList(row * MAX_COL, (row + 1) * MAX_COL)) { day ->
                        DayCell(
                            day.dayOfMonth,
                            isCutoff = day.month != pivotDate.month,
                            isToday = day == currentDate,
                            isSelected = day == selectedDate,
                            isSunday = day.dayOfWeek == DayOfWeek.SUNDAY,
                            onClick = {
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
fun CalendarDatesPreview() {
    JuiceJuiceJuiceTheme {
        CalendarDates(
            days = getDisplayedDays(LocalDate.now()),
            pivotDate = LocalDate.now(),
            selectedDate = LocalDate.now(),
            expanded = true
        )
    }
}
