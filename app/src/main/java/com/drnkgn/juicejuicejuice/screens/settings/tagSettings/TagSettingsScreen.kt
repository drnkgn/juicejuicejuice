package com.drnkgn.juicejuicejuice.screens.settings.tagSettings

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.drnkgn.juicejuicejuice.components.AppTopBar
import com.drnkgn.juicejuicejuice.components.ClickableTag
import com.drnkgn.juicejuicejuice.components.ClickableTagVariant
import com.drnkgn.juicejuicejuice.components.FormColumn
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.fakes.FakeTags
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiState
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors

@Composable
fun TagSettingsScreen(
    navController: NavController,
    tagSettingsViewModel: TagSettingsViewModel = hiltViewModel()
) {
    val getAllTagsState by tagSettingsViewModel.getAllTagsState.toCollect()

    LaunchedEffect(Unit) {
        tagSettingsViewModel.getAllTags()
    }

    TagSettingsContent(navController, getAllTagsState)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSettingsContent(
    navController: NavController,
    getAllTagsState: UiState<List<Tag>>,
) {
    var tags by remember { mutableStateOf<List<Tag>>(emptyList()) }

    when (getAllTagsState.data) {
        is Resource.Success -> {
            tags = getAllTagsState.data.data
        }
        else -> { }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Tags")
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    navController.navigate("settings/tags/new")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                FormColumn("Income") {
                    val incomeTags = tags.filter { tag -> tag.type == TransactionType.Income }

                    if (incomeTags.isEmpty()) {
                        Text(
                            "No income-related tags created yet",
                            color = MaterialTheme.extColors.placeholder,
                            fontSize = 14.sp
                        )
                    } else {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            incomeTags.forEach { tag ->
                                ClickableTag(
                                    variant = ClickableTagVariant.Inverse,
                                    onClick = {
                                        Log.d("JJJ", "$tag")
                                        navController.navigate("settings/tags/edit/${tag.id}")
                                    }
                                ) {
                                    Text(tag.name)
                                }
                            }
                        }
                    }
                }
                HorizontalDivider()
                FormColumn("Expense") {
                    val expenseTags = tags.filter { tag -> tag.type == TransactionType.Expense }

                    if (expenseTags.isEmpty()) {
                        Text(
                            "No expense-related tags created yet",
                            color = MaterialTheme.extColors.placeholder,
                            fontSize = 14.sp
                        )
                    } else {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            expenseTags.forEach { tag ->
                                ClickableTag(
                                    variant = when {
                                        tag.deletedAt === null -> ClickableTagVariant.Inverse
                                        else -> ClickableTagVariant.OutlineContrast
                                    },
                                    onClick = {
                                        navController.navigate("settings/tags/edit/${tag.id}")
                                    }
                                ) {
                                    Text(
                                        tag.name,
                                        textDecoration = when {
                                            tag.deletedAt === null -> null
                                            else -> TextDecoration.LineThrough
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TagSettingsScreenPreview() {
    JuiceJuiceJuiceTheme {
        TagSettingsContent(
            rememberNavController(),
            UiState(data = Resource.Success(FakeTags.tags))
        )
    }
}
