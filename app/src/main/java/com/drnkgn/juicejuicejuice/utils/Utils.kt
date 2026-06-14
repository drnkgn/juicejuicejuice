package com.drnkgn.juicejuicejuice.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.drnkgn.juicejuicejuice.states.Resource
import java.io.File

object Utils {
    fun validateDatabaseSchema(dbFile: File, expectedTables: Map<String, List<String>>): Boolean {
        val db = SQLiteDatabase.openDatabase(dbFile.absolutePath, null, SQLiteDatabase.OPEN_READONLY)

        return try {
            for ((tableName, expectedColumns) in expectedTables) {
                val cursor = db.rawQuery("PRAGMA table_info($tableName)", null)
                Log.d("JJJ", cursor.columnNames.contentToString())

                if (cursor.count == 0) {
                    Log.e("JJJ", "Table $tableName not found")
                    return false
                } else {
                    Log.i("JJJ", "Table $tableName found")
                }

                val actualColumns = mutableSetOf<String>()
                cursor.use {
                    while (it.moveToNext()) {
                        actualColumns.add(it.getString(1))
                    }
                }

                for (column in expectedColumns) {
                    if (!actualColumns.contains(column)) {
                        Log.e("JJJ", "Column $column not found in $tableName")
                        return false
                    } else {
                        Log.e("JJJ", "Column $column found in $tableName")
                    }
                }
            }
            true
        } catch (e: Exception) {
            Log.e("JJJ", "Validation failed: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    fun getFolderName(uri: Uri?): String {
        return uri?.lastPathSegment?.substringAfterLast(":") ?: "Unknown"
    }

    fun getFileName(uri: Uri, context: Context): String? {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0) cursor.getString(index) else null
        }
    }

    fun uriToFile(uri: Uri, context: Context): File {
        val fileName = getFileName(uri, context) ?: "imported_file"
        val destFile = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { input ->
            destFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return destFile
    }

    suspend fun<T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return try {
            val data = apiCall()

            if (data === null) {
                val exception = NullPointerException("Response body is null")

                Log.e("JJJ.safeApiCall", "Response body is null", exception)
                Resource.GenericError(exception)
            } else {
                Resource.Success(data)
            }
        } catch (e: Exception) {
            Log.e("JJJ.safeApiCall", e.message, e)
            Resource.GenericError(e)
        }
    }
}