package com.drnkgn.juicejuicejuice.states

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: Resource<T>?
)

fun <T> UiState<T>.getOrNull(): T? {
    return when (this.data) {
        is Resource.Success -> this.data.data
        else -> null
    }
}