package com.drnkgn.juicejuicejuice.screens.transactions.editTransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.components.BDialog
import com.drnkgn.juicejuicejuice.components.JJJButton
import com.drnkgn.juicejuicejuice.db.entities.toForm
import com.drnkgn.juicejuicejuice.enums.JJJButtonColors
import com.drnkgn.juicejuicejuice.fakes.FakeTags
import com.drnkgn.juicejuicejuice.states.forms.TagForm
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteTransactionDialog(
    open: Boolean = true,
    isLoading: Boolean = false,
    onConfirm: (() -> Unit),
    onClose: (() -> Unit)
) {
    BDialog(
        open = open,
        title = "Delete transaction?",
        onDismissRequest = onClose
    ) {
        Text("Do you want to delete this record?")
        Row(
            modifier = Modifier.padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            JJJButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = onClose
            ) {
                Text("Cancel")
            }
            JJJButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                isLoading = isLoading,
                colors = JJJButtonColors.Primary,
                onClick = onConfirm
            ) {
                Text("Ok")
            }
        }
    }
}

@Preview
@Composable
private fun DeleteTransactionDialogPreview() {
    JuiceJuiceJuiceTheme {
        DeleteTransactionDialog(
            open = true,
            onConfirm = { },
            onClose = { }
        )
    }
}