package com.drnkgn.juicejuicejuice.screens.settings.dbSettings

import android.net.Uri
import android.provider.DocumentsContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.components.BDialog
import com.drnkgn.juicejuicejuice.components.JJJButton
import com.drnkgn.juicejuicejuice.enums.JJJButtonColors
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.utils.Utils

@Composable
fun ExportConfirmDialog(
    open: Boolean = false,
    uri: Uri? = null,
    isLoading: Boolean = false,
    onConfirm: (() -> Unit),
    onClose: (() -> Unit)
) {
    BDialog(
        open = open,
        title = "Export database",
        onDismissRequest = onClose
    ) {
        Text("Are you sure to export to ${Utils.getFolderName(uri)}?")
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
fun ExportConfirmDialogPreview() {
    JuiceJuiceJuiceTheme {
        ExportConfirmDialog(
            open = true,
            onConfirm = { },
            onClose = { }
        )
    }
}
