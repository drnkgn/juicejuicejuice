package com.drnkgn.juicejuicejuice.utils

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.snapshots.SnapshotStateMap

fun <K : Any, V : Any> stateMapSaver() =
    Saver<SnapshotStateMap<K, V>, List<Pair<K, V>>>(
        save = { it.toList() },
        restore = {
            mutableStateMapOf<K, V>().apply {
                putAll(it)
            }
        }
    )