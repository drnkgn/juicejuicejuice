package com.drnkgn.juicejuicejuice.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.drnkgn.juicejuicejuice.states.Resource
import java.io.File

object Utils {
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