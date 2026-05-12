package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BDialog(
    open: Boolean = true,
    title: String,
    onDismissRequest: (() -> Unit),
    content: @Composable () -> Unit
) {
    if (open) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.background,
            dragHandle = null,
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 30.dp, start = 20.dp, bottom = 20.dp, end = 20.dp)
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            }
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun BDialogPreview() {
    JuiceJuiceJuiceTheme {
        BDialog(
            open = true,
            title = "Bottom dialog example",
            content = {
                Text("This is an example of a bottom dialog")
                Row(
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    JJJButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { }
                    ) {
                        Text("Ok")
                    }
                }
            },
            onDismissRequest = { }
        )
    }
}