package com.drnkgn.juicejuicejuice.states

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: Resource<T>?
)