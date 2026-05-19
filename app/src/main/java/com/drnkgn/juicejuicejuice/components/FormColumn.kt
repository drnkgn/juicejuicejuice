package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceA40

@Composable
fun FormColumn(
    header: String = "",
    modifier: Modifier = Modifier,
    children: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            header.uppercase(),
            color = SurfaceA40,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        children()
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview
@Composable
fun FormColumnPreview() {
    JuiceJuiceJuiceTheme {
        FormColumn("Header") {
            JJJTextField(
                onValueChange = { }
            )
        }
    }
}