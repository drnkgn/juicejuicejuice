package com.drnkgn.juicejuicejuice.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@Composable
fun SettingsItem(
    icon: ImageVector,
    name: String = "",
    description: String = "",
    onClick: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
            .height(50.dp)
            .fillMaxWidth()
            .clickable { onClick?.invoke() },
        verticalAlignment = Alignment.Top
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    icon,
                    contentDescription = "settings icon",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 14.sp,
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    lineHeight = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingsItemPreview() {
    JuiceJuiceJuiceTheme {
        SettingsItem(
            Icons.Rounded.Tag,
            "Tag",
            "Manage all tags",
            onClick = { }
        )
    }
}