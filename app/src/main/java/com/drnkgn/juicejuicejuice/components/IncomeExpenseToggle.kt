package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@Composable
fun IncomeExpenseToggle(
    enforceValue: Boolean = true,
    transactionType: TransactionType? = TransactionType.Expense,
    onChange: (TransactionType?) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        JJJToggleableButton(
            toggled = transactionType == TransactionType.Income,
            modifier = Modifier.weight(1f),
            onClick = {
                if (enforceValue) {
                    onChange(TransactionType.Income)
                } else {
                    onChange(when (transactionType) {
                        TransactionType.Income -> null
                        else -> TransactionType.Income
                    })
                }
            }
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
            onClick = {
                if (enforceValue) {
                    onChange(TransactionType.Expense)
                } else {
                    onChange(when (transactionType) {
                        TransactionType.Expense -> null
                        else -> TransactionType.Expense
                    })
                }
            }
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

@Preview
@Composable
private fun IncomeExpenseTogglePreview() {
    JuiceJuiceJuiceTheme {
        IncomeExpenseToggle(
            onChange = { }
        )
    }
}
