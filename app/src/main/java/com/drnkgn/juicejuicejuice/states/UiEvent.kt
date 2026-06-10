package com.drnkgn.juicejuicejuice.states

sealed class UIEvent {
    data object RefreshOverviewScreen: UIEvent()
    data object NoEvent: UIEvent()
}
