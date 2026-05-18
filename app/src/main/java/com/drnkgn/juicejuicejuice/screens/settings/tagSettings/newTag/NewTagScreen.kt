package com.drnkgn.juicejuicejuice.screens.settings.tagSettings.newTag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.drnkgn.juicejuicejuice.components.AppTopBar
import com.drnkgn.juicejuicejuice.components.FormColumn
import com.drnkgn.juicejuicejuice.components.JJJButton
import com.drnkgn.juicejuicejuice.components.JJJButtonColors
import com.drnkgn.juicejuicejuice.components.JJJTextField
import com.drnkgn.juicejuicejuice.components.JJJToggleableButton
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.screens.settings.tagSettings.TagSettingsViewModel
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiState
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors

@Composable
fun NewTagScreen(
    navController: NavController,
    tagSettingsViewModel: TagSettingsViewModel = hiltViewModel()
) {
    val createTagState by tagSettingsViewModel.createTagState.toCollect()

    when (createTagState.data) {
        is Resource.Success -> {
            navController.popBackStack()
        }
        else -> { }
    }

    NewTagContent(
        createTagState = createTagState,
        onCreateTag = { tag -> tagSettingsViewModel.createTag(tag)}
    )
}

@Composable
fun NewTagContent(
    createTagState: UiState<Unit> = UiState(data = Resource.Idle),
    onCreateTag: (Tag) -> Unit
) {
    var tagType by remember { mutableStateOf(TransactionType.Expense) }
    var tagName by remember { mutableStateOf("")}

    Scaffold(
        topBar = {
            AppTopBar(title = "New Tag")
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    FormColumn("Transaction Type") {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            JJJToggleableButton(
                                toggled = tagType == TransactionType.Income,
                                modifier = Modifier.weight(1f),
                                onClick = { tagType = TransactionType.Income }
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
                                toggled = tagType == TransactionType.Expense,
                                modifier = Modifier.weight(1f),
                                onClick = { tagType = TransactionType.Expense }
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
                    FormColumn("Name") {
                        JJJTextField(
                            // isError = !error.isEmpty(),
                            placeholder = {
                                Text(
                                    "Tag name",
                                    color = MaterialTheme.extColors.placeholder
                                )
                            },
                            value = tagName,
                            onValueChange = {
                                tagName = it
                            }
                        )
                    }
                }
                Row {
                    JJJButton(
                        // enabled = addTransactionState !is UiState.Loading,
                        shape = RoundedCornerShape(10.dp),
                        colors = JJJButtonColors.Primary,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onCreateTag(
                                Tag(name = tagName, type = tagType)
                            )
                        }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (createTagState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            }
                            Text(
                                "Confirm",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun NewTagContentPreview() {
    JuiceJuiceJuiceTheme {
        NewTagContent(
            onCreateTag = { },
        )
    }
}
