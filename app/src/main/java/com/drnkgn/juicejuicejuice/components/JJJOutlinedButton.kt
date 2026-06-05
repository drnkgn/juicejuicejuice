package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.enums.JJJButtonColors
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceTonalA0
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceTonalA10
import com.drnkgn.juicejuicejuice.ui.theme.WarningA0
import com.drnkgn.juicejuicejuice.ui.theme.WarningA10

enum class JJJOutlinedButtonVariant {
    Transparent,
    Filled,
    Light
}

@Composable
fun JJJOutlinedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    colors: JJJButtonColors = JJJButtonColors.Default,
    variant: JJJOutlinedButtonVariant = JJJOutlinedButtonVariant.Transparent,
    shape: Shape = RoundedCornerShape(10.dp),
    children: @Composable () -> Unit
) {
    OutlinedButton(
        enabled = enabled,
        modifier = modifier,
        shape = shape,
        contentPadding = contentPadding,
        border = BorderStroke(
            width = 1.dp,
            color = when (variant) {
                JJJOutlinedButtonVariant.Transparent -> when (colors) {
                    JJJButtonColors.Default -> MaterialTheme.colorScheme.outline
                    JJJButtonColors.Warning -> WarningA10
                    else -> MaterialTheme.colorScheme.outline
                }
                JJJOutlinedButtonVariant.Light,
                JJJOutlinedButtonVariant.Filled -> when (colors) {
                    JJJButtonColors.Default -> SurfaceTonalA10
                    JJJButtonColors.Warning -> WarningA10
                    else -> SurfaceTonalA10
                }
            },
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = when (variant) {
                JJJOutlinedButtonVariant.Transparent -> when (colors) {
                    JJJButtonColors.Default -> MaterialTheme.colorScheme.background
                    JJJButtonColors.Warning -> WarningA0
                    else -> MaterialTheme.colorScheme.background
                }
                JJJOutlinedButtonVariant.Filled -> when (colors) {
                    JJJButtonColors.Default -> SurfaceTonalA0
                    JJJButtonColors.Warning -> WarningA0
                    else -> SurfaceTonalA0
                }
                JJJOutlinedButtonVariant.Light -> when (colors) {
                    JJJButtonColors.Default -> MaterialTheme.colorScheme.tertiary
                    JJJButtonColors.Warning -> WarningA0
                    else -> MaterialTheme.colorScheme.tertiary
                }
            },
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = onClick
    ) {
        children()
    }
}

@Preview
@Composable
private fun JJJOutlinedButtonPreview() {
    JuiceJuiceJuiceTheme {
        JJJOutlinedButton(
            colors = JJJButtonColors.Warning,
            onClick = { }
        ) { Text("Click Me") }
    }
}
