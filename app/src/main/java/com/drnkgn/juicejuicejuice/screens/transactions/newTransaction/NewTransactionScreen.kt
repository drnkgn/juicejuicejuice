package com.drnkgn.juicejuicejuice.screens.transactions.newTransaction

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.drnkgn.juicejuicejuice.components.FormColumn
import com.drnkgn.juicejuicejuice.components.JJJButton
import com.drnkgn.juicejuicejuice.components.JJJButtonColors
import com.drnkgn.juicejuicejuice.components.JJJTextField
import com.drnkgn.juicejuicejuice.components.JJJToggleableButton
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.db.entities.Transaction
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.screens.transactions.SelectTagDialog
import com.drnkgn.juicejuicejuice.screens.transactions.TransactionViewModel
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiState
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun NewTransactionScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel = hiltViewModel()
) {
    val addTransactionState by transactionViewModel.createTransactionState.toCollect()
    val getAllTagsState by transactionViewModel.getAllTagsState.toCollect()

    LaunchedEffect(Unit) {
        transactionViewModel.getAllTags()
    }

    NewTransactionContent(
        navController,
        getAllTagsState,
        addTransactionState,
        onConfirm = { transaction, tags ->
            transactionViewModel.createTransaction(transaction, tags)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NewTransactionContent(
    navController: NavController,
    getAllTagsState: UiState<List<Tag>>,
    addTransactionState: UiState<Unit>,
    onConfirm: (Transaction, List<Tag>) -> Unit,
) {
    val context = LocalContext.current
    val calender = Calendar.getInstance()

    var tags by remember { mutableStateOf<List<Tag>>(emptyList()) }
    var tagsOpen by remember { mutableStateOf(false) }

    var datePickerOpen by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var transactionType by remember { mutableStateOf(TransactionType.Expense) }
    var amount by remember { mutableStateOf("")}
    var description by remember { mutableStateOf("")}
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var selectedTags by remember { mutableStateOf<List<Tag>>(emptyList())}

    val error = remember { mutableStateMapOf<String, String>() }

    val timePicker = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            selectedTime = LocalTime.of(hour, minute)
        },
        calender.get(Calendar.HOUR_OF_DAY),
        calender.get(Calendar.MINUTE),
        false
    )

    fun isInputValid(): Boolean {
        if (amount.isEmpty()) {
            error["error"] = "Amount is required"
            return false
        }

        if (selectedDate == null) {
            error["error"] = "Date is required"
            return false
        }

        if (selectedTime == null) {
            error["error"] = "Time is required"
            return false
        }

        if (selectedTags.isEmpty()) {
            error["error"] = "At least one tag is required"
            return false
        }

        return true
    }

    fun handleConfirmClicked() {
        if (!isInputValid())
            return

        onConfirm(
            Transaction(
                type = transactionType,
                amount = amount.toFloatOrNull() ?: 0f,
                transactionAt = LocalDateTime.of(selectedDate, selectedTime),
                description = description
            ),
            selectedTags
        )
    }

    when (addTransactionState.data) {
        is Resource.Success -> {
            navController.popBackStack()
        }
        else -> { }
    }

    when (getAllTagsState.data) {
        is Resource.Success -> {
            tags = getAllTagsState.data.data
        }
        else -> { }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                Text("New Transaction", fontWeight = FontWeight.Bold, fontSize = 26.sp)
            }
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                FormColumn("Transaction Type") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        JJJToggleableButton(
                            toggled = transactionType == TransactionType.Income,
                            modifier = Modifier.weight(1f),
                            onClick = { transactionType = TransactionType.Income }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                    contentDescription = "Income icon"
                                )
                                Text("Income")
                            }
                        }
                        JJJToggleableButton(
                            toggled = transactionType == TransactionType.Expense,
                            modifier = Modifier.weight(1f),
                            onClick = { transactionType = TransactionType.Expense }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                                    contentDescription = "Expense icon"
                                )
                                Text("Expense")
                            }
                        }
                    }
                }
                FormColumn("Amount") {
                    JJJTextField(
                        isError = !error.isEmpty(),
                        placeholder = {
                            Text(
                                "Enter an amount",
                                color = MaterialTheme.extColors.placeholder
                            )
                        },
                        leadingIcon = {
                            Text("RM", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        value = amount,
                        onValueChange = {
                            amount = it
                        }
                    )
                }
                FormColumn("Tags") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(40f)
                            )
                            .clickable { tagsOpen = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            if (selectedTags.isEmpty()) {
                                Text("Select tags", color = MaterialTheme.extColors.placeholder)
                            } else {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    selectedTags
                                        .forEach { tag ->
                                            JJJButton(
                                                contentPadding = PaddingValues(vertical = 5.dp, horizontal = 10.dp),
                                                colors = JJJButtonColors.Primary,
                                                shape = RoundedCornerShape(50.dp),
                                                onClick = { }
                                            ) { Text(tag.name) }
                                        }
                                }
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FormColumn("Date", modifier = Modifier.weight(1f)) {
                        JJJButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { datePickerOpen = true }
                        ) {
                            Text(
                                selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "dd / mm / yyyy",
                                color = if (selectedDate == null) MaterialTheme.extColors.placeholder else MaterialTheme.colorScheme.onTertiary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (datePickerOpen) {
                            DatePickerDialog(
                                colors = DatePickerDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.tertiary
                                ),
                                onDismissRequest = { datePickerOpen = false },
                                dismissButton = {
                                    TextButton(
                                        onClick = { datePickerOpen = false }
                                    ) {
                                        Text("Cancel")
                                    }
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            val millis = datePickerState.selectedDateMillis
                                            millis?.let {
                                                selectedDate = Instant.ofEpochMilli(it)
                                                    .atZone(ZoneId.systemDefault())
                                                    .toLocalDate()
                                            }
                                            datePickerOpen = false
                                        },
                                    ) {
                                        Text("Confirm")
                                    }
                                }
                            ) {
                                DatePicker(
                                    state = datePickerState,
                                    colors = DatePickerDefaults.colors(
                                        currentYearContentColor = MaterialTheme.colorScheme.onTertiary,
                                        subheadContentColor = MaterialTheme.colorScheme.onTertiary,
                                        headlineContentColor = MaterialTheme.colorScheme.onTertiary,
                                        dayContentColor = MaterialTheme.colorScheme.onTertiary,
                                        containerColor = MaterialTheme.colorScheme.tertiary,
                                    )
                                )
                            }
                        }
                    }
                    FormColumn("Time", modifier = Modifier.weight(1f)) {
                        JJJButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { timePicker.show() }
                        ) {
                            Text(
                                selectedTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "--:-- --",
                                color = if (selectedTime == null) MaterialTheme.extColors.placeholder else MaterialTheme.colorScheme.onTertiary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                FormColumn("Description") {
                    JJJTextField(
                        modifier = Modifier
                            .heightIn(min = 112.dp),
                        value = description,
                        placeholder = {
                            Text(
                                "Enter a description (optional)",
                                color = MaterialTheme.extColors.placeholder
                            )
                        },
                        onValueChange = { description = it }
                    )
                }
                Row {
                    Button(
                        enabled = !addTransactionState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.extColors.disabled
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        onClick ={ handleConfirmClicked() }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (addTransactionState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            }
                            Text(
                                "Confirm",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                SelectTagDialog(
                    open = tagsOpen,
                    tags, selectedTags,
                    onConfirm = { staged -> selectedTags = staged.toList() },
                    onClose = { tagsOpen = false }
                )
            }
        }
    }
}

@Preview
@Composable
fun NewTransactionContentPreview() {
    JuiceJuiceJuiceTheme {
        NewTransactionContent(
            rememberNavController(),
            getAllTagsState = UiState(data = Resource.Idle),
            addTransactionState = UiState(data = Resource.Idle),
            onConfirm = { _, _ ->  }
        )
    }
}
