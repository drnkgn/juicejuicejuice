package com.drnkgn.juicejuicejuice.screens

import androidx.lifecycle.ViewModel
import com.drnkgn.juicejuicejuice.repositories.EventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    val eventBus: EventBus
): ViewModel() { }