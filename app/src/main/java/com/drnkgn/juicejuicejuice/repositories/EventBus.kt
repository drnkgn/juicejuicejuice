package com.drnkgn.juicejuicejuice.repositories

import com.drnkgn.juicejuicejuice.states.UIEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventBus @Inject constructor() {
    private val _event = MutableSharedFlow<UIEvent>(replay = 0, extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    fun send(event: UIEvent) {
        _event.tryEmit(event)
    }
}
