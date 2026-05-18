package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

enum class ClickableTagVariant {
    Filled,
    Inverse,
    OutlineContrast,
    OutlineLight,
    Outline
}

@Composable
fun ClickableTag(
    variant: ClickableTagVariant = ClickableTagVariant.Filled,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    when (variant) {
        ClickableTagVariant.Outline,
        ClickableTagVariant.OutlineLight,
        ClickableTagVariant.OutlineContrast -> {
            JJJOutlinedButton(
                modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                contentPadding = PaddingValues(horizontal = 11.dp, vertical = 6.dp),
                variant = when (variant) {
                    ClickableTagVariant.Outline -> JJJOutlinedButtonVariant.Filled
                    ClickableTagVariant.OutlineLight -> JJJOutlinedButtonVariant.Light
                    ClickableTagVariant.OutlineContrast -> JJJOutlinedButtonVariant.Transparent
                    else -> JJJOutlinedButtonVariant.Transparent
                },
                shape = RoundedCornerShape(50.dp),
                onClick = onClick
            ) { content() }
        }
        else -> {
            JJJButton(
                modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                contentPadding = PaddingValues(horizontal = 11.dp, vertical = 6.dp),
                colors = when (variant) {
                    ClickableTagVariant.Filled -> JJJButtonColors.Primary
                    else -> JJJButtonColors.Default
                },
                shape = RoundedCornerShape(50.dp),
                onClick = onClick
            ) { content() }
        }
    }
}

@Preview
@Composable
fun ClickableTagPreview() {
    JuiceJuiceJuiceTheme {
        ClickableTag(
            variant = ClickableTagVariant.OutlineContrast,
            onClick = { }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tag")
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "remove tag",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
