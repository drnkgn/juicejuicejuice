package com.drnkgn.juicejuicejuice.screens.settings.dbSettings

import android.net.Uri
import android.provider.DocumentsContract
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
import com.drnkgn.juicejuicejuice.components.AppTopBar
import com.drnkgn.juicejuicejuice.components.FormColumn
import com.drnkgn.juicejuicejuice.components.JJJButton
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import java.io.File

@Composable
fun DBSettingsScreen() {
    DBSettingsContent()
}

@Composable
fun DBSettingsContent() {
    val context = LocalContext.current

    var exportConfirmOpen by remember { mutableStateOf(false) }

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var fileToExport by remember { mutableStateOf<File?>(null) }

    val folderPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            selectedUri = it
            fileToExport = File(context.dataDir, "app_database")
            exportConfirmOpen = true
        }
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
                        onClick = { }
                    ) {
                        Text("Choose file")
                    }
                }
            }
        }
        ExportConfirmDialog(
            exportConfirmOpen,
            uri = selectedUri,
            onConfirm = {
                selectedUri?.let {
                    val fileName = "app_db_export"
                    val newFileUri = DocumentsContract.buildDocumentUriUsingTree(
                        it,
                        "${DocumentsContract.getTreeDocumentId(it)}/$fileName"
                    )

                    context.contentResolver.openOutputStream(newFileUri)?.use { output ->
                        fileToExport?.inputStream()?.copyTo(output)
                        Toast.makeText(context, "Export successfully", Toast.LENGTH_LONG).show()
                    }
                }
            },
            onClose = { exportConfirmOpen = false }
        )
    }
}

@Preview
@Composable
private fun DBSettingsContentPreview() {
    JuiceJuiceJuiceTheme {
        DBSettingsContent()
    }
}
