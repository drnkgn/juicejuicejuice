package com.drnkgn.juicejuicejuice.states

import java.io.IOException

sealed class Resource<out T> {
    data class Success<T>(val data: T): Resource<T>()
    data object Idle: Resource<Nothing>()
    data class NetworkError(val exception: IOException): Resource<Nothing>()
    data class GenericError(val exception: Exception): Resource<Nothing>()
    // data class HTTPError(val error: ParsedHttpExceptionResponse): Resource<Nothing>()
}