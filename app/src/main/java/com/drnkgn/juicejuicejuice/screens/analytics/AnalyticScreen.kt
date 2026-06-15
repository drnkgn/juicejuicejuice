package com.drnkgn.juicejuicejuice.screens.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.drnkgn.juicejuicejuice.components.AppTopBar
import com.drnkgn.juicejuicejuice.components.Chip
import com.drnkgn.juicejuicejuice.components.ChipTrend
import com.drnkgn.juicejuicejuice.db.dto.DailySpend
import com.drnkgn.juicejuicejuice.db.dto.TagSpend
import com.drnkgn.juicejuicejuice.screens.overview.TopCard
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiState
import com.drnkgn.juicejuicejuice.states.getOrNull
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import java.time.LocalDate
import java.util.Locale
import kotlin.math.sign

@Composable
fun AnalyticScreen(
    navController: NavController,
    analyticViewModel: AnalyticViewModel = hiltViewModel()
) {
    val dailySpendingState by analyticViewModel.dailySpendingState.toCollect()
    val tagSpendingState by analyticViewModel.tagSpendingState.toCollect()

    LaunchedEffect(Unit) {
        analyticViewModel.getDailySpending(LocalDate.now())
        analyticViewModel.getSpendingByTags(LocalDate.now())
    }

    AnalyticContent(
        dailySpendingState,
        tagSpendingState
    )
}

@Composable
fun AnalyticContent(
    dailySpendingState: UiState<List<DailySpend>>,
    tagSpendingState: UiState<List<TagSpend>>
) {
    var dailySpending by remember { mutableStateOf((0..6).map { DailySpend(it, 0f) }) }
    var tagSpending by remember { mutableStateOf<List<TagSpend>>(emptyList()) }

    when (dailySpendingState.data) {
        is Resource.Success -> {
            dailySpending = dailySpendingState.data.data
        }
        else -> { }
    }

    when (tagSpendingState.data) {
        is Resource.Success -> {
            tagSpending = tagSpendingState.data.data
        }
        else -> { }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Analytics")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TopCard(
                    modifier = Modifier
                        .height(100.dp)
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = "Income"
                            )

                            val incomePctDiff = 0f
                            Chip(
                                text = "%.1f%%".format(Locale.UK, incomePctDiff),
                                color = when {
                                    incomePctDiff == 0f -> ChipTrend.Neutral
                                    incomePctDiff.sign == -1.0f -> ChipTrend.Positive
                                    incomePctDiff.sign == 1.0f -> ChipTrend.Negative
                                    else -> ChipTrend.Neutral
                                },
                                trend = when {
                                    incomePctDiff == 0f -> ChipTrend.Neutral
                                    incomePctDiff.sign == -1.0f -> ChipTrend.Negative
                                    incomePctDiff.sign == 1.0f -> ChipTrend.Positive
                                    else -> ChipTrend.Neutral
                                }
                            )
                        }
                        Text("Weekly Income")
                        Text(
                            text = "$%.2f".format(Locale.UK, 0f),
                            fontWeight = FontWeight.Bold, fontSize = 22.sp
                        )
                    }
                }
                TopCard(
                    modifier = Modifier
                        .height(100.dp)
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                                contentDescription = "Expense"
                            )

                            val expensePctDiff = 0f
                            Chip(
                                text = "%.1f%%".format(Locale.UK, expensePctDiff),
                                color = when {
                                    expensePctDiff == 0f -> ChipTrend.Neutral
                                    expensePctDiff.sign == -1.0f -> ChipTrend.Positive
                                    expensePctDiff.sign == 1.0f -> ChipTrend.Negative
                                    else -> ChipTrend.Neutral
                                },
                                trend = when {
                                    expensePctDiff == 0f -> ChipTrend.Neutral
                                    expensePctDiff.sign == -1.0f -> ChipTrend.Negative
                                    expensePctDiff.sign == 1.0f -> ChipTrend.Positive
                                    else -> ChipTrend.Neutral
                                }
                            )
                        }
                        Text("Weekly Expense")
                        Text(
                            text = "$%.2f".format(Locale.UK, 0f),
                            fontWeight = FontWeight.Bold, fontSize = 22.sp
                        )
                    }
                }
            }
            TopCard(
                modifier = Modifier.fillMaxWidth(),
                paddingValues = PaddingValues(20.dp)
            ) {
                Text("Daily Spending", color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(20.dp))
                AnalyticByDays(dailySpending)
            }
            Spacer(modifier = Modifier.height(20.dp))
            TopCard(
                modifier = Modifier.fillMaxWidth(),
                paddingValues = PaddingValues(20.dp)
            ) {
                when {
                    tagSpendingState.isLoading -> {
                        CircularProgressIndicator()
                    }
                    tagSpending.isEmpty() -> {
                        Text("No spending data yet")
                    }
                    else -> {
                        AnalyticByTags(tagSpending)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AnalyticContentPreview() {
    JuiceJuiceJuiceTheme {
        AnalyticContent(
            dailySpendingState = UiState(data = Resource.Idle),
            tagSpendingState = UiState(data = Resource.Idle),
        )
    }
}
