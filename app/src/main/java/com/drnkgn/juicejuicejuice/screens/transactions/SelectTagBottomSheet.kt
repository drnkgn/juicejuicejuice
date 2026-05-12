package com.drnkgn.juicejuicejuice.screens.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.components.ClickableTag
import com.drnkgn.juicejuicejuice.components.ClickableTagVariant
import com.drnkgn.juicejuicejuice.components.FormColumn
import com.drnkgn.juicejuicejuice.components.JJJButton
import com.drnkgn.juicejuicejuice.components.JJJButtonColors
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.fakes.FakeTags
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SelectTagBottomSheet(
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

    if (open) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.background,
            dragHandle = null,
            onDismissRequest = onClose
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 30.dp, start = 20.dp, bottom = 20.dp, end = 20.dp)
            ) {
                Text("Select tags", fontWeight = FontWeight.Bold, fontSize = 26.sp)
            }
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .padding(20.dp)
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
                                    Text(tag.name)
                                }
                            }
                    }
                }
                FormColumn("Available Tags") {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        tags
                            .filter { tag -> !stagedTags.contains(tag) }
                            .forEach { tag ->
                                ClickableTag(
                                    variant = ClickableTagVariant.Inverse,
                                    onClick = { stagedTags.add(tag) }
                                ) {
                                    Text(tag.name)
                                }
                            }
                    }
                }
                JJJButton(
                    modifier = Modifier.fillMaxWidth(),
                    colors = JJJButtonColors.Primary,
                    onClick = {
                        onConfirm(stagedTags)
                        onClose()
                    }
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Preview
@Composable
fun SelectTagBottomSheetPreview() {
    JuiceJuiceJuiceTheme {
        val tags = FakeTags.tags.toList()
        val selectedTags = tags.toMutableList()
        selectedTags.removeAt(0)

        SelectTagBottomSheet(
            tags = tags,
            selectedTags = selectedTags,
            onConfirm = { },
            onClose = { }
        )
    }
}