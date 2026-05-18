package com.drnkgn.juicejuicejuice.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UiStateHolder<T>(
    initial: Resource<T> = Resource.Idle
) {
    private val _state = MutableStateFlow(UiState(data = initial))

    @Composable
    fun toCollect() = _state.asStateFlow().collectAsState()

    fun set(data: Resource<T>? = null, isLoading: Boolean? = null) {
        _state.update {
            it.copy(
                isLoading = isLoading ?: it.isLoading,
                data = data ?: it.data
            )
        }
    }
}