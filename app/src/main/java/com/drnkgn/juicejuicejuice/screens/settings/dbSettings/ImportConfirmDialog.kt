package com.drnkgn.juicejuicejuice.screens.settings.dbSettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.components.BDialog
import com.drnkgn.juicejuicejuice.components.JJJButton
import com.drnkgn.juicejuicejuice.enums.JJJButtonColors
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@Composable
fun ImportConfirmDialog(
    open: Boolean = false,
    isLoading: Boolean = false,
    onConfirm: (() -> Unit),
    onClose: (() -> Unit)
) {
    BDialog(
        open = open,
        title = "Import database",
        onDismissRequest = onClose
    ) {
        Text("Are you sure? This action will override your local database and is DESTRUCTIVE.")
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
fun ImportConfirmDialogPreview() {
    JuiceJuiceJuiceTheme {
        ImportConfirmDialog(
            open = true,
            onConfirm = { },
            onClose = { }
        )
    }
}
