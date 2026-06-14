package com.drnkgn.juicejuicejuice.screens.settings.dbSettings

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drnkgn.juicejuicejuice.DatabaseManager
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiStateHolder
import com.drnkgn.juicejuicejuice.utils.DBUtils
import com.drnkgn.juicejuicejuice.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject

@HiltViewModel
class DBSettingsViewModel @Inject constructor(
    val databaseManager: DatabaseManager
): ViewModel() {
    private val database = databaseManager.getDb()

    val exportDBState = UiStateHolder<Unit>(Resource.Idle)
    val importDBState = UiStateHolder<Unit>(Resource.Idle)

    private val databaseName = "app_database"

    private fun getDatabaseDir(context: Context) = File(context.filesDir.parent, "databases")

    private fun writeFile(
        databaseDir: File,
        filename: String,
        buffer: ByteArray,
        zis: ZipInputStream
    ) {
        val outFile = File(databaseDir, filename)

        outFile.parentFile?.mkdirs()

        FileOutputStream(outFile).use { fos ->
            var len: Int
            while (zis.read(buffer).also { len = it } > 0) {
                fos.write(buffer, 0, len)
            }
        }
    }

    private fun cleanup(databaseDir: File) {
        val tmpDbWal = File(databaseDir, "$databaseName-tmp-wal")
        val tmpDbShm = File(databaseDir, "$databaseName-tmp-shm")
        val tmpDb = File(databaseDir, "$databaseName-tmp")

        val dbWal = File(databaseDir, "$databaseName-wal")
        val dbShm = File(databaseDir, "$databaseName-shm")
        val db = File(databaseDir, databaseName)

        tmpDbWal.copyTo(dbWal, overwrite = true)
        tmpDbShm.copyTo(dbShm, overwrite = true)
        tmpDb.copyTo(db, overwrite = true)

        tmpDbWal.delete()
        tmpDbShm.delete()
        tmpDb.delete()
    }

    fun exportDatabase(
        context: Context,
        folderUri: Uri,
    ) {
        viewModelScope.launch {
            exportDBState.set(isLoading = true)

            try {
                DBUtils.checkpoint(database)

                val fileName = "$databaseName.zip"
                val parentUri = DocumentsContract.buildDocumentUriUsingTree(
                    folderUri,
                    DocumentsContract.getTreeDocumentId(folderUri)
                )
                val outputUri = DocumentsContract.createDocument(
                    context.contentResolver, parentUri,
                    "application/zip",
                    fileName
                )

                val databaseDir = getDatabaseDir(context)
                val filesToExport = listOf(
                    File(databaseDir, databaseName),
                    File(databaseDir, "$databaseName-wal"),
                    File(databaseDir, "$databaseName-shm")
                ).filter { it.exists() }

                outputUri?.let { outputUri ->
                    context.contentResolver.openOutputStream(outputUri)?.use { output ->
                        ZipOutputStream(output).use { zip ->
                            filesToExport.forEach { file ->
                                zip.putNextEntry(ZipEntry(file.name))

                                file.inputStream().use { input ->
                                    input.copyTo(zip)
                                }

                                zip.closeEntry()
                            }
                        }
                    }
                }

                exportDBState.set(isLoading = false, data = Resource.Success(Unit))
            } catch (ex: Exception) {
                exportDBState.set(isLoading = false, data = Resource.GenericError(ex))
                Log.e("JJJ", "Failed to export database", ex)
            }
        }
    }

    fun importDatabase(
        context: Context,
        fileUri: Uri
    ) {
        viewModelScope.launch {
            importDBState.set(isLoading = true)

            try {
                val databaseDir = getDatabaseDir(context)

                val inputStream = context.contentResolver.openInputStream(fileUri)
                    ?: throw IllegalArgumentException("Cannot open zip file")
                val zipStream = ZipInputStream(BufferedInputStream(inputStream))

                zipStream.use { zip ->
                    var entry = zip.nextEntry
                    val buffer = ByteArray(4096)

                    while (entry != null) {
                        if (!entry.isDirectory) {
                            if (entry.name.endsWith("wal")) {
                                writeFile(databaseDir, "$databaseName-tmp-wal", buffer, zip)
                            } else if (entry.name.endsWith("shm")) {
                                writeFile(databaseDir, "$databaseName-tmp-shm", buffer, zip)
                            } else {
                                writeFile(databaseDir, "$databaseName-tmp", buffer, zip)
                            }
                        }

                        zip.closeEntry()
                        entry = zip.nextEntry
                    }
                }

                val tmpDbFile = File(databaseDir, "$databaseName-tmp")
                val validationResult = DBUtils.validateDatabaseSchema(
                    database, tmpDbFile
                )

                if (validationResult) {
                    cleanup(databaseDir)
                    Utils.restartApp(context)
                }

                importDBState.set(isLoading = false, data = Resource.Success(Unit))
            } catch (ex: Exception) {
                importDBState.set(isLoading = false, data = Resource.GenericError(ex))
                Log.e("JJJ", "Failed to import database", ex)
            }
        }
    }
}