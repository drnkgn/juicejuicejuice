package com.drnkgn.juicejuicejuice.screens.overview

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
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drnkgn.juicejuicejuice.components.SmallTag
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.fakes.FakeTransactions
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItem(
    transactionWithTags: TransactionWithTags,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .clickable { onClick?.invoke() }
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
            .height(70.dp)
            .fillMaxWidth(),
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
                    when (transactionWithTags.transaction.type) {
                        TransactionType.Income -> Icons.Default.Savings
                        TransactionType.Expense -> Icons.Default.Paid
                    },
                    contentDescription = "Savings",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$${String.format("%.2f", transactionWithTags.transaction.amount)}",
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 14.sp,
                    )
                    Text(
                        text = transactionWithTags.transaction.transactionAt.format(DateTimeFormatter.ofPattern("hh.mm a")),
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                    )
                }
                transactionWithTags.transaction.description?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    transactionWithTags.tags.forEach { tag ->
                        SmallTag(tag.name)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ItemPreview() {
    JuiceJuiceJuiceTheme {
        TransactionItem(
            FakeTransactions.fakeTransactions.first()
        )
    }
}