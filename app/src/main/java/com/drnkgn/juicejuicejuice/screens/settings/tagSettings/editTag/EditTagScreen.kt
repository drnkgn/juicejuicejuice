package com.drnkgn.juicejuicejuice.screens.settings.tagSettings.editTag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.drnkgn.juicejuicejuice.components.AppTopBar
import com.drnkgn.juicejuicejuice.components.FormColumn
import com.drnkgn.juicejuicejuice.components.IncomeExpenseToggle
import com.drnkgn.juicejuicejuice.components.JJJButton
import com.drnkgn.juicejuicejuice.components.JJJTextField
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.db.entities.toForm
import com.drnkgn.juicejuicejuice.enums.JJJButtonColors
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.fakes.FakeTags
import com.drnkgn.juicejuicejuice.screens.settings.tagSettings.TagSettingsViewModel
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiState
import com.drnkgn.juicejuicejuice.states.forms.TagForm
import com.drnkgn.juicejuicejuice.states.forms.toEntity
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

    when (updateTagState.data) {
        is Resource.Success -> {
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
    getTagState: UiState<Tag>,
    updateTagState: UiState<Unit>,
    removeTagState: UiState<Unit>,
    onUpdateTag: (Tag) -> Unit,
    onRemoveTag: (Tag) -> Unit
) {
    var tagForm by remember { mutableStateOf(TagForm(-1, "", TransactionType.Expense)) }

    var moreOptionExpanded by remember { mutableStateOf(false) }
    var removeTagDialogOpened by remember { mutableStateOf(false) }

    when (getTagState.data) {
        is Resource.Success -> {
            tagForm = getTagState.data.data.toForm()
        }
        else -> { }
    }

    when (removeTagState.data) {
        is Resource.Success -> {
            removeTagDialogOpened = false
            navController.popBackStack()
        }
        else -> {}
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Edit Tag ${if (tagForm.deletedAt !== null) "(Deleted)" else ""}"
            ) {
                if (tagForm.deletedAt === null) {
                    Box {
                        IconButton(
                            onClick = { moreOptionExpanded = true }
                        ) {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = "more",
                            )
                        }
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
                .padding(horizontal = 20.dp)
        ) {
            if (getTagState.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            } else if (getTagState.data is Resource.Success) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        FormColumn("Transaction Type") {
                            IncomeExpenseToggle(
                                transactionType = tagForm.type,
                                onChange = { type ->
                                    if (tagForm.deletedAt === null) {
                                        tagForm = tagForm.copy(type = type)
                                    }
                                }
                            )
                        }
                        FormColumn("Name") {
                            JJJTextField(
                                // isError = !error.isEmpty(),
                                enabled = tagForm.deletedAt === null,
                                placeholder = {
                                    Text(
                                        "Tag name",
                                        color = MaterialTheme.extColors.placeholder
                                    )
                                },
                                value = tagForm.name,
                                onValueChange = {
                                    tagForm = tagForm.copy(name = it)
                                }
                            )
                        }
                    }
                    Row {
                        JJJButton(
                            enabled = !updateTagState.isLoading && tagForm.deletedAt === null,
                            shape = RoundedCornerShape(10.dp),
                            colors = JJJButtonColors.Primary,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onUpdateTag(
                                    tagForm.toEntity()
                                )
                            }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (updateTagState.isLoading) {
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
                        tag = tagForm,
                        isLoading = updateTagState.isLoading,
                        onConfirm = {
                            onRemoveTag(tagForm.toEntity())
                        },
                        onClose = { removeTagDialogOpened = false }
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
            getTagState = UiState(
                data = Resource.Success(FakeTags.tags.first())
            ),
            onUpdateTag = { },
            onRemoveTag = { },
            updateTagState = UiState(data = Resource.Idle),
            removeTagState = UiState(data = Resource.Idle),
        )
    }
}
