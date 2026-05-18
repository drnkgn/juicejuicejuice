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
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceTonalA0
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceTonalA10

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
                JJJOutlinedButtonVariant.Transparent -> MaterialTheme.colorScheme.outline
                JJJOutlinedButtonVariant.Light,
                JJJOutlinedButtonVariant.Filled -> SurfaceTonalA10
            },
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = when (variant) {
                JJJOutlinedButtonVariant.Transparent -> MaterialTheme.colorScheme.background
                JJJOutlinedButtonVariant.Filled -> SurfaceTonalA0
                JJJOutlinedButtonVariant.Light -> MaterialTheme.colorScheme.tertiary
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
            enabled = false,
            onClick = { }
        ) { Text("Click Me") }
    }
}
