package com.drnkgn.juicejuicejuice.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@Composable
fun JJJToggleableButton(
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    toggled: Boolean = false,
    onClick: () -> Unit,
    children: @Composable () -> Unit
) {
    if (toggled) {
        JJJButton(
            enabled = enabled,
            modifier = modifier,
            colors = JJJButtonColors.Primary,
            onClick = onClick,
            children = children
        )
    } else {
        JJJOutlinedButton(
            enabled = enabled,
            modifier = modifier,
            onClick = onClick,
            children = children
        )
    }
}

@Preview
@Composable
fun JJJToggleableButtonPreview() {
    JuiceJuiceJuiceTheme {
        JJJToggleableButton(
            toggled = false,
            onClick = { }
        ) {
            Text("Toggle Me")
        }
    }
}