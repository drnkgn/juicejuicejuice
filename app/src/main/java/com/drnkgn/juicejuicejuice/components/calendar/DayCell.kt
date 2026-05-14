package com.drnkgn.juicejuicejuice.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayCell(
    day: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isCutoff: Boolean = false,
    isToday: Boolean = false,
    isSelected: Boolean = false,
    isDayOfWeek: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }

    var modifier = modifier
        .aspectRatio(1f)
        .background(
            color = when {
                isSelected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.tertiary
            },
            shape = RoundedCornerShape(10.dp)
        )

    if (isToday) {
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
    }

    if (onClick !== null) {
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            when {
                isDayOfWeek -> DayOfWeek.of(day + 1)
                    .getDisplayName(
                        TextStyle.SHORT_STANDALONE,
                        Locale.ENGLISH
                    )

                else -> day.toString()
            },
            modifier = Modifier
                .fillMaxWidth(),
            color = when {
                isCutoff -> MaterialTheme.colorScheme.outline
                isDayOfWeek -> MaterialTheme.extColors.placeholder
                isSelected -> MaterialTheme.colorScheme.onPrimary
                else -> MaterialTheme.colorScheme.onTertiary
            },
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        // if (isToday) {
        //     Box(
        //         modifier = Modifier
        //             .size(5.dp)
        //             .background(
        //                 color = MaterialTheme.extColors.info,
        //                 shape = RoundedCornerShape(5.dp)
        //             )
        //     )
        // }
    }
}

@Preview
@Composable
fun DayCellPreview() {
    JuiceJuiceJuiceTheme {
        DayCell(20, isSelected = true, isToday = true)
    }
}
