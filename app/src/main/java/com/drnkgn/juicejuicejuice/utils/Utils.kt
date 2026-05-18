package com.drnkgn.juicejuicejuice.utils

import android.util.Log
import com.drnkgn.juicejuicejuice.states.Resource

object Utils {
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