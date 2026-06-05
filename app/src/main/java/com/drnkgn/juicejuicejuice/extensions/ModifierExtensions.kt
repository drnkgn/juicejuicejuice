package com.drnkgn.juicejuicejuice.extensions

import androidx.compose.ui.Modifier

fun Modifier.ifTrue(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
): Modifier {
    return if (condition) this.then(modifier()) else this
}
