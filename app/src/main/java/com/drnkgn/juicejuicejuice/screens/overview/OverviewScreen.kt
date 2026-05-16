package com.drnkgn.juicejuicejuice.screens.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.drnkgn.juicejuicejuice.components.Chip
import com.drnkgn.juicejuicejuice.components.calendar.Calendar
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.enums.UiState
import com.drnkgn.juicejuicejuice.fakes.FakeTransactions
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors
import java.time.LocalDate

@Composable
fun OverviewScreen(
    navController: NavController,
    overviewViewModel: OverviewViewModel = hiltViewModel()
) {
    val getAllTransactionsState by overviewViewModel.indexTransactionState.toCollect()

    fun handleSelectDateChange(date: LocalDate? = null) {
        overviewViewModel.indexTransactions(date)
    }

    LaunchedEffect(Unit) {
        handleSelectDateChange()
    }

    OverviewContent(
        navController,
        getAllTransactionsState,
        onSelectDateChange = { date ->
            handleSelectDateChange(date)
        }
    )
}

@Composable
fun OverviewContent(
    navController: NavController,
    indexTransactionState: UiState<List<TransactionWithTags>>,
    onSelectDateChange: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var transactions by remember { mutableStateOf<List<TransactionWithTags>>(emptyList()) }

    LaunchedEffect(indexTransactionState) {
        when (indexTransactionState) {
            is UiState.Success -> {
                transactions = indexTransactionState.data
            }
            else -> { }
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Home", fontWeight = FontWeight.Bold, fontSize = 26.sp)
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "settings",
                    modifier = Modifier.clickable { navController.navigate("settings") }
                )
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
                    .padding(vertical = 20.dp),
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
                    onSelectDateChange(day)
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
        }
    }
}

@Preview
@Composable
fun OverviewContentPreview() {
    JuiceJuiceJuiceTheme {
        OverviewContent(
            rememberNavController(),
            UiState.Success(FakeTransactions.fakeTransactions),
            onSelectDateChange = { }
        )
    }
}