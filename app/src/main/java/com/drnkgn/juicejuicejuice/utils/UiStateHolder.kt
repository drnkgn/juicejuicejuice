package com.drnkgn.juicejuicejuice.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.drnkgn.juicejuicejuice.enums.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UiStateHolder<T>(
    initial: UiState<T> = UiState.Idle
) {
    private val _state = MutableStateFlow(initial)
    val state = _state.asStateFlow()

    @Composable
    fun toCollect() = _state.asStateFlow().collectAsState()

    fun set(value: UiState<T>) {
        _state.value = value
    }

    fun reset() {
        _state.value = UiState.Idle
    }
}
