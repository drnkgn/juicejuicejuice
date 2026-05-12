package com.drnkgn.juicejuicejuice.screens.settings.tagSettings.editTag

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.drnkgn.juicejuicejuice.components.FormColumn
import com.drnkgn.juicejuicejuice.components.JJJButton
import com.drnkgn.juicejuicejuice.components.JJJButtonColors
import com.drnkgn.juicejuicejuice.components.JJJTextField
import com.drnkgn.juicejuicejuice.components.JJJToggleableButton
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.db.entities.toUiState
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.enums.UiState
import com.drnkgn.juicejuicejuice.fakes.FakeTags
import com.drnkgn.juicejuicejuice.screens.settings.tagSettings.TagSettingsViewModel
import com.drnkgn.juicejuicejuice.states.TagUIState
import com.drnkgn.juicejuicejuice.states.toEntity
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors

@Composable
fun EditTagScreen(
    navController: NavController,
    tagSettingsViewModel: TagSettingsViewModel = hiltViewModel(),
    tagId: Int? = null
) {
    val getTagState by tagSettingsViewModel.getTagState.toCollect()
    val updateTagState by tagSettingsViewModel.updateTagState.toCollect()
    val removeTagState by tagSettingsViewModel.removeTagState.toCollect()

    when (updateTagState) {
        is UiState.Success<*> -> {
            navController.popBackStack()
        }
        else -> { }
    }

    LaunchedEffect(Unit) {
        tagId?.let { tagSettingsViewModel.getTag(it) }
    }

    EditTagContent(
        navController = navController,
        getTagState = getTagState,
        updateTagState = updateTagState,
        removeTagState = removeTagState,
        onUpdateTag = { tag -> tagSettingsViewModel.updateTag(tag) },
        onRemoveTag = { tag -> tagSettingsViewModel.removeTag(tag) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTagContent(
    navController: NavController,
    getTagState: UiState<Tag> = UiState.Idle,
    updateTagState: UiState<Unit> = UiState.Idle,
    removeTagState: UiState<Unit> = UiState.Idle,
    onUpdateTag: (Tag) -> Unit,
    onRemoveTag: (Tag) -> Unit
) {
    var tag by remember { mutableStateOf(TagUIState(-1, "", TransactionType.Expense)) }

    var moreOptionExpanded by remember { mutableStateOf(false) }
    var removeTagDialogOpened by remember { mutableStateOf(false) }

    LaunchedEffect(getTagState) {
        when (getTagState) {
            is UiState.Success -> {
                tag = getTagState.data.toUiState()
            }
            else -> { }
        }
    }

    when (removeTagState) {
        is UiState.Success -> {
            removeTagDialogOpened = false
            navController.popBackStack()
        }
        else -> {}
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Edit Tag ${if (tag.deletedAt !== null) "(Deleted)" else ""}",
                    fontWeight = FontWeight.Bold, fontSize = 26.sp
                )
                if (tag.deletedAt === null) {
                    Box {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "more",
                            modifier = Modifier.clickable { moreOptionExpanded = true }
                        )
                        DropdownMenu(
                            expanded = moreOptionExpanded,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(20.dp),
                            onDismissRequest = { moreOptionExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Remove tag",
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                },
                                onClick = {
                                    moreOptionExpanded = false
                                    removeTagDialogOpened = true
                                }
                            )
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (getTagState is UiState.Success) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        FormColumn("Transaction Type") {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                JJJToggleableButton(
                                    toggled = tag.type == TransactionType.Income,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        if (tag.deletedAt === null)
                                            tag = tag.copy(type = TransactionType.Income)
                                    }
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
                                    toggled = tag.type == TransactionType.Expense,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        if (tag.deletedAt === null)
                                            tag = tag.copy(type = TransactionType.Expense)
                                    }
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
                                enabled = tag.deletedAt === null,
                                placeholder = {
                                    Text(
                                        "Tag name",
                                        color = MaterialTheme.extColors.placeholder
                                    )
                                },
                                value = tag.name,
                                onValueChange = {
                                    tag = tag.copy(name = it)
                                }
                            )
                        }
                    }
                    Row {
                        JJJButton(
                            enabled = updateTagState !is UiState.Loading && tag.deletedAt === null,
                            shape = RoundedCornerShape(10.dp),
                            colors = JJJButtonColors.Primary,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onUpdateTag(
                                    tag.toEntity()
                                )
                            }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (updateTagState is UiState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                }
                                Text(
                                    "Update",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                    RemoveTagDialog(
                        open = removeTagDialogOpened,
                        tag = tag,
                        isLoading = updateTagState is UiState.Loading,
                        onConfirm = {
                            onRemoveTag(tag.toEntity())
                        },
                        onClose = { removeTagDialogOpened = false }
                    )
                }
            } else if (getTagState is UiState.Loading) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun EditTagContentPreview() {
    JuiceJuiceJuiceTheme {
        EditTagContent(
            navController = rememberNavController(),
            getTagState = UiState.Success(
                FakeTags.tags.first()
            ),
            // getTagState = UiState.Loading,
            onUpdateTag = { },
            onRemoveTag = { },
        )
    }
}
