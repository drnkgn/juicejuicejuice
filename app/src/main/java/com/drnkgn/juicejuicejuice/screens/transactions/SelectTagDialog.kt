package com.drnkgn.juicejuicejuice.screens.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.components.BDialog
import com.drnkgn.juicejuicejuice.components.ClickableTag
import com.drnkgn.juicejuicejuice.components.ClickableTagVariant
import com.drnkgn.juicejuicejuice.components.FormColumn
import com.drnkgn.juicejuicejuice.components.JJJOutlinedButton
import com.drnkgn.juicejuicejuice.components.JJJOutlinedButtonVariant
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.fakes.FakeTags
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SelectTagDialog(
    open: Boolean = true,
    tags: List<Tag>,
    selectedTags: List<Tag>,
    onConfirm: ((List<Tag>) -> Unit),
    onClose: (() -> Unit)
) {
    val stagedTags = remember { mutableStateListOf<Tag>() }

    LaunchedEffect(open) {
        if (open) {
            stagedTags.clear()
            stagedTags.addAll(selectedTags)
        }
    }

    BDialog(
        open = open,
        title = "Select tags",
        onDismissRequest = onClose
    ) {
        FormColumn("Selected Tags") {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                stagedTags
                    .forEach { tag ->
                        ClickableTag(
                            variant = ClickableTagVariant.Filled,
                            onClick = { stagedTags.remove(tag) }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(tag.name)
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "remove tag",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
            }
        }
        HorizontalDivider()
        FormColumn("Available Tags") {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                tags
                    .filter { tag -> !stagedTags.contains(tag) }
                    .forEach { tag ->
                        ClickableTag(
                            variant = ClickableTagVariant.OutlineLight,
                            onClick = { stagedTags.add(tag) }
                        ) {
                            Text(tag.name)
                        }
                    }
            }
        }
        JJJOutlinedButton(
            variant = JJJOutlinedButtonVariant.Filled,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onConfirm(stagedTags)
                onClose()
            }
        ) {
            Text("Done", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview
@Composable
fun SelectTagDialogPreview() {
    JuiceJuiceJuiceTheme {
        val tags = FakeTags.tags.toList()
        val selectedTags = tags.toMutableList()
        selectedTags.removeAt(0)

        SelectTagDialog(
            open = true,
            tags = tags,
            selectedTags = selectedTags,
            onConfirm = { },
            onClose = { }
        )
    }
}