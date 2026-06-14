package com.drnkgn.juicejuicejuice.screens.settings.dbSettings

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.drnkgn.juicejuicejuice.components.AppTopBar
import com.drnkgn.juicejuicejuice.components.FormColumn
import com.drnkgn.juicejuicejuice.components.JJJButton
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiState
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@Composable
fun DBSettingsScreen(
    dbSettingsViewModel: DBSettingsViewModel = hiltViewModel()
) {
    val exportDBState by dbSettingsViewModel.exportDBState.toCollect()
    val importDBState by dbSettingsViewModel.importDBState.toCollect()

    DBSettingsContent(
        exportDBState = exportDBState,
        importDBState = importDBState,
        onExport = { context, folderUri ->
            dbSettingsViewModel.exportDatabase(context, folderUri)
        },
        onImport = { context, fileUri ->
            dbSettingsViewModel.importDatabase(context, fileUri)
        }
    )
}

@Composable
fun DBSettingsContent(
    exportDBState: UiState<Unit>,
    importDBState: UiState<Unit>,
    onExport: (Context, Uri) -> Unit,
    onImport: (Context, Uri) -> Unit,
) {
    val context = LocalContext.current

    var exportConfirmOpen by remember { mutableStateOf(false) }
    var importConfirmOpen by remember { mutableStateOf(false) }

    var exportFolderUri by remember { mutableStateOf<Uri?>(null) }
    var importFileUri by remember { mutableStateOf<Uri?>(null) }

    val folderPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            exportFolderUri = it
            exportConfirmOpen = true
        }
    }

    val filesPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            importFileUri = uri
            importConfirmOpen = true
        }
    }

    fun onExportConfirm() {
        exportFolderUri?.let { folderUri ->
            onExport(context, folderUri)
        }
    }

    fun onImportConfirm() {
        importFileUri?.let { fileUri ->
            onImport(context, fileUri)
        }
    }

    when (exportDBState.data) {
        is Resource.Success -> {
            Toast.makeText(context, "Export successfully", Toast.LENGTH_LONG).show()
            exportConfirmOpen = false
        }
        else -> { }
    }

    when (importDBState.data) {
        is Resource.Success -> {
            Toast.makeText(context, "Import successfully", Toast.LENGTH_LONG).show()
            importConfirmOpen = false
        }
        else -> { }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Database")
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
                FormColumn("Export database") {
                    JJJButton(
                        onClick = { folderPicker.launch(null) }
                    ) {
                        Text("Choose folder")
                    }
                }
                HorizontalDivider()
                FormColumn("Import database") {
                    JJJButton(
                        onClick = { filesPicker.launch(arrayOf("application/zip")) }
                    ) {
                        Text("Choose file")
                    }
                }
            }
        }
        ExportConfirmDialog(
            exportConfirmOpen,
            uri = exportFolderUri,
            isLoading = exportDBState.isLoading,
            onConfirm = { onExportConfirm() },
            onClose = { exportConfirmOpen = false }
        )
        ImportConfirmDialog(
            importConfirmOpen,
            isLoading = importDBState.isLoading,
            onConfirm = { onImportConfirm() },
            onClose = { importConfirmOpen = false }
        )
    }
}

@Preview
@Composable
private fun DBSettingsContentPreview() {
    JuiceJuiceJuiceTheme {
        DBSettingsContent(
            importDBState = UiState(data = Resource.Idle),
            exportDBState = UiState(data = Resource.Idle),
            onExport = { _, _ -> },
            onImport = { _, _ -> }
        )
    }
}
