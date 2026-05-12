package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

enum class ClickableTagVariant {
    Filled,
    Inverse,
    Outline
}

@Composable
fun ClickableTag(
    variant: ClickableTagVariant = ClickableTagVariant.Filled,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (variant != ClickableTagVariant.Outline) {
        JJJButton(
            colors = when (variant) {
                ClickableTagVariant.Filled -> JJJButtonColors.Primary
                else -> JJJButtonColors.Default
            },
            shape = RoundedCornerShape(50.dp),
            onClick = onClick
        ) { content() }
    } else {
        JJJOutlinedButton(
            shape = RoundedCornerShape(50.dp),
            onClick = onClick
        ) { content() }
    }
}

@Preview
@Composable
fun ClickableTagPreview() {
    JuiceJuiceJuiceTheme {
        ClickableTag(
            variant = ClickableTagVariant.Outline,
            onClick = { }
        ) {
            Text("Tag")
        }
    }
}
