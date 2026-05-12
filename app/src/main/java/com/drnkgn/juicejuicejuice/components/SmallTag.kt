package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@Composable
fun SmallTag(text: String = "") {
    Box(
        modifier = Modifier
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(50.dp)
            ),
    ) {
        Text(
            text,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 10.sp,
            lineHeight = 12.sp
        )
    }
}

@Preview
@Composable
fun SmallTagPreview() {
    JuiceJuiceJuiceTheme {
        SmallTag("food")
    }
}