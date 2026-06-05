package com.drnkgn.juicejuicejuice.screens.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.drnkgn.juicejuicejuice.components.BDialog
import com.drnkgn.juicejuicejuice.components.ClickableTag
import com.drnkgn.juicejuicejuice.components.ClickableTagSize
import com.drnkgn.juicejuicejuice.components.ClickableTagVariant
import com.drnkgn.juicejuicejuice.components.FormColumn
import com.drnkgn.juicejuicejuice.components.FormColumnSpacings
import com.drnkgn.juicejuicejuice.components.IncomeExpenseToggle
import com.drnkgn.juicejuicejuice.components.JJJOutlinedButton
import com.drnkgn.juicejuicejuice.components.JJJOutlinedButtonVariant
import com.drnkgn.juicejuicejuice.enums.JJJButtonColors
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors

data class FilterTransactionResult(
    val transactionType: TransactionType?,
    val deleted: Boolean
)

@Composable
fun FilterTransactionDialog(
    open: Boolean = true,
    currentFilters: FilterTransactionResult,
    onConfirm: (FilterTransactionResult) -> Unit,
    onClose: () -> Unit
) {
    var stagedFilters by remember { mutableStateOf(currentFilters) }

    LaunchedEffect(open) {
        if (open) {
            stagedFilters = currentFilters
        }
    }
    
    BDialog(
        open = open,
        title = "Filter transactions",
        onDismissRequest = onClose
    ) {
        FormColumn(
            header = "Type",
            spacings = FormColumnSpacings(top = 0.dp)
        ) {
            IncomeExpenseToggle(
                enforceValue = false,
                transactionType = stagedFilters.transactionType,
                onChange = { type ->
                    stagedFilters = stagedFilters.copy(transactionType = type)
                }
            )
        }
        HorizontalDivider()
        FormColumn("Status") {
            ClickableTag(
                colors = when {
                    stagedFilters.deleted -> JJJButtonColors.Warning
                    else -> JJJButtonColors.Default
                },
                size = ClickableTagSize.Medium,
                variant = ClickableTagVariant.OutlineLight,
                onClick = {
                    stagedFilters = stagedFilters.copy(deleted = !stagedFilters.deleted)
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = "removed icon",
                        modifier = Modifier.size(18.dp),
                        tint = when {
                            stagedFilters.deleted -> MaterialTheme.colorScheme.onTertiary
                            else -> MaterialTheme.extColors.placeholder
                        }
                    )
                    Text("Deleted records")
                }
            }
        }
        JJJOutlinedButton(
            variant = JJJOutlinedButtonVariant.Filled,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onConfirm(stagedFilters)
                onClose()
            }
        ) {
            Text("Apply filters", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview
@Composable
private fun FilterTransactionDialogPreview() {
    JuiceJuiceJuiceTheme {
        FilterTransactionDialog(
            open = true,
            currentFilters = FilterTransactionResult(TransactionType.Expense, false),
            onConfirm = { },
            onClose = { }
        )
    }
}
