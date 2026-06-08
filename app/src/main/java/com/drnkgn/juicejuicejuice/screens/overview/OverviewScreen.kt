package com.drnkgn.juicejuicejuice.screens.overview

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.drnkgn.juicejuicejuice.components.AppTopBar
import com.drnkgn.juicejuicejuice.components.Chip
import com.drnkgn.juicejuicejuice.components.ChipTrend
import com.drnkgn.juicejuicejuice.components.calendar.Calendar
import com.drnkgn.juicejuicejuice.db.dto.OverviewStatsDTO
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.fakes.FakeTransactions
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiState
import com.drnkgn.juicejuicejuice.states.UiStateHolder
import com.drnkgn.juicejuicejuice.states.getOrNull
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors
import java.time.LocalDate
import java.util.Locale
import kotlin.math.sign

@Composable
fun OverviewScreen(
    navController: NavController,
    overviewViewModel: OverviewViewModel = hiltViewModel()
) {
    val indexTransactionStateHolder = overviewViewModel.indexTransactionState
    val overviewStatsState by overviewViewModel.overviewStatsState.toCollect()

    fun refreshIndexedTransaction(
        date: LocalDate? = null,
        type: TransactionType? = null,
        withDeleted: Boolean = false
    ) {
        overviewViewModel.indexTransactions(date, type, withDeleted)
    }

    OverviewContent(
        navController,
        indexTransactionStateHolder,
        overviewStatsState,
        onRefreshIndexedTransaction = { date, type, withDeleted ->
            refreshIndexedTransaction(date, type, withDeleted)
        }
    )
}

@Composable
fun OverviewContent(
    navController: NavController,
    indexTransactionStateHolder: UiStateHolder<List<TransactionWithTags>>,
    overviewStatsState: UiState<OverviewStatsDTO>,
    onRefreshIndexedTransaction: (LocalDate, TransactionType?, Boolean) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val indexTransactionState by indexTransactionStateHolder.toCollect()

    var filterOpen by remember { mutableStateOf(false) }
    var filters by remember { mutableStateOf(FilterTransactionResult(null, false)) }

    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var transactions by remember { mutableStateOf<List<TransactionWithTags>>(emptyList()) }

    when (indexTransactionState.data) {
        is Resource.Success -> {
            transactions = (indexTransactionState.data as Resource.Success<List<TransactionWithTags>>).data
            indexTransactionStateHolder.set(data = Resource.Idle)
        }
        else -> { }
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            if (indexTransactionState.data is Resource.Idle) {
                onRefreshIndexedTransaction(selectedDate, filters.transactionType, filters.deleted)
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Home") {
                Row {
                    IconButton(
                        onClick = { filterOpen = true }
                    ) {
                        Icon(
                            Icons.Filled.FilterList,
                            contentDescription = "filter",
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate("settings") }
                    ) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "settings",
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = { navController.navigate("transactions/new") }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
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
                TopCard(modifier = Modifier.weight(1f)) {
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

                            val incomePctDiff = overviewStatsState.getOrNull()?.incomePctDiff ?: 0f
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
                        Text("Income")
                        Text(
                            text = "$%.2f".format(Locale.UK, overviewStatsState.getOrNull()?.income ?: 0f),
                            fontWeight = FontWeight.Bold, fontSize = 22.sp
                        )
                    }
                }
                TopCard(modifier = Modifier.weight(1f)) {
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

                            val expensePctDiff = overviewStatsState.getOrNull()?.expensePctDiff ?: 0f
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
                        Text("Expense")
                        Text(
                            text = "$%.2f".format(Locale.UK, overviewStatsState.getOrNull()?.expense ?: 0f),
                            fontWeight = FontWeight.Bold, fontSize = 22.sp
                        )
                    }
                }
            }
            Calendar(
                selectedDate = selectedDate,
                onValueChange = { day ->
                    selectedDate = day
                    onRefreshIndexedTransaction(day, filters.transactionType, filters.deleted)
                }
            )
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    "${transactions.size} transactions",
                    fontSize = 12.sp,
                    color = MaterialTheme.extColors.placeholder
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 10.dp, top = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(transactions) { transactionWithTags ->
                    TransactionItem(
                        transactionWithTags,
                        onClick = { navController.navigate("transactions/edit/${transactionWithTags.transaction.id}") }
                    )
                }
            }
            FilterTransactionDialog(
                open = filterOpen,
                currentFilters = filters,
                onConfirm = { values ->
                    filters = values.copy()
                    onRefreshIndexedTransaction(selectedDate, filters.transactionType, filters.deleted)
                },
                onClose = { filterOpen = false }
            )
        }
    }
}

@Preview
@Composable
fun OverviewContentPreview() {
    JuiceJuiceJuiceTheme {
        OverviewContent(
            rememberNavController(),
            UiStateHolder(initial = Resource.Success(FakeTransactions.fakeTransactions)),
            UiState(data = Resource.Success(OverviewStatsDTO(9f, 8f, 12.5f, -5.3f))),
            onRefreshIndexedTransaction = { _, _, _ -> }
        )
    }
}