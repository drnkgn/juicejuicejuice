package com.drnkgn.juicejuicejuice.screens.settings.tagSettings.editTag

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
import com.drnkgn.juicejuicejuice.components.JJJButtonColors
import com.drnkgn.juicejuicejuice.db.entities.toUiState
import com.drnkgn.juicejuicejuice.fakes.FakeTags
import com.drnkgn.juicejuicejuice.states.TagUIState
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveTagDialog(
    open: Boolean = true,
    tag: TagUIState,
    isLoading: Boolean = false,
    onConfirm: (() -> Unit),
    onClose: (() -> Unit)
) {
    BDialog(
        open = open,
        title = "Tag \"${tag.name}\"",
        onDismissRequest = onClose
    ) {
        Text("Do you want to remove this tag?")
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
private fun RemoveTagDialogPreview() {
    JuiceJuiceJuiceTheme {
        RemoveTagDialog(
            open = true,
            tag = FakeTags.tags.first().toUiState(),
            onConfirm = { },
            onClose = { }
        )
    }
}