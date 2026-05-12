package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.extColors

enum class JJJButtonColors {
    Default,
    Primary
}

@Composable
fun JJJButton(
    enabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    colors: JJJButtonColors = JJJButtonColors.Default,
    shape: Shape = RoundedCornerShape(10.dp),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit,
    children: @Composable () -> Unit,
) {
    Button(
        enabled = enabled && !isLoading,
        contentPadding = contentPadding,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = when (colors) {
                JJJButtonColors.Primary -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.tertiary
            },
            contentColor = when (colors) {
                JJJButtonColors.Primary -> MaterialTheme.colorScheme.onPrimary
                else -> MaterialTheme.colorScheme.onTertiary
            },
        ),
        onClick = onClick
    ) {
        if (isLoading) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.extColors.onDisabled,
                    strokeWidth = 2.dp
                )
                Text("Loading")
            }
        } else {
            children()
        }
    }
}

@Preview
@Composable
fun JJJButtonPreview() {
    JuiceJuiceJuiceTheme {
        JJJButton(
            isLoading = true,
            onClick = { }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = "Income icon"
                )
                Text("Income")
            }
        }
    }
}