package com.drnkgn.juicejuicejuice.screens.overview

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.drnkgn.juicejuicejuice.components.AppTopBar
import com.drnkgn.juicejuicejuice.components.Chip
import com.drnkgn.juicejuicejuice.components.calendar.Calendar
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.fakes.FakeTransactions
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiState
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors
import java.time.LocalDate

@Composable
fun OverviewScreen(
    navController: NavController,
    overviewViewModel: OverviewViewModel = hiltViewModel()
) {
    val indexTransactionState by overviewViewModel.indexTransactionState.toCollect()

    fun refreshIndexedTransaction(
        date: LocalDate? = null,
        type: TransactionType? = null,
        withDeleted: Boolean = false
    ) {
        overviewViewModel.indexTransactions(date, type, withDeleted)
    }

    LaunchedEffect(Unit) {
        refreshIndexedTransaction()
    }

    OverviewContent(
        navController,
        indexTransactionState,
        onRefreshIndexedTransaction = { date, type, withDeleted ->
            refreshIndexedTransaction(date, type, withDeleted)
        }
    )
}

@Composable
fun OverviewContent(
    navController: NavController,
    indexTransactionState: UiState<List<TransactionWithTags>>,
    onRefreshIndexedTransaction: (LocalDate, TransactionType?, Boolean) -> Unit
) {
    var filterOpen by remember { mutableStateOf(false) }
    var filters by remember { mutableStateOf(FilterTransactionResult(null, false)) }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var transactions by remember { mutableStateOf<List<TransactionWithTags>>(emptyList()) }

    LaunchedEffect(indexTransactionState) {
        when (indexTransactionState.data) {
            is Resource.Success -> {
                transactions = indexTransactionState.data.data
            }
            else -> { }
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
                            Chip(text = "+12%")
                        }
                        Text("Income")
                        Text("$487.30", fontWeight = FontWeight.Bold, fontSize = 22.sp)
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
                                Icons.AutoMirrored.Filled.TrendingDown,
                                contentDescription = "Expense"
                            )
                            Chip(text = "-5%", variant = "error")
                        }
                        Text("Expense")
                        Text("$1,475.30", fontWeight = FontWeight.Bold, fontSize = 22.sp)
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
            UiState(data = Resource.Success(FakeTransactions.fakeTransactions)),
            onRefreshIndexedTransaction = { _, _, _ -> }
        )
    }
}