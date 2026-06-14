package com.drnkgn.juicejuicejuice.fakes

import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.db.entities.Transaction
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.enums.TransactionType
import java.time.LocalDateTime

object FakeTransactions {
    val fakeTransactions = listOf(
        TransactionWithTags(
            Transaction(
                id = 1,
                type = TransactionType.Expense,
                amount = 34.80f,
                transactionAt = LocalDateTime.now(),
                description = "Burgers"
            ),
            tags = listOf(
                Tag(id = 1, name = "food", type = TransactionType.Expense),
            )
        ),
        TransactionWithTags(
            Transaction(
                id = 2,
                type = TransactionType.Expense,
                amount = 107.33f,
                transactionAt = LocalDateTime.now(),
                description = "Gas (Petronas)"
            ),
            tags = listOf(
                Tag(id = 2, name = "gas", type = TransactionType.Expense),
            )
        ),
        TransactionWithTags(
            Transaction(
                id = 3,
                type = TransactionType.Income,
                amount = 15f,
                transactionAt = LocalDateTime.now(),
                description = "Splits at food"
            ),
            tags = listOf(
                Tag(id = 3, name = "split", type = TransactionType.Income),
            )
        ),
        TransactionWithTags(
            Transaction(
                id = 4,
                type = TransactionType.Income,
                amount = 1000f,
                transactionAt = LocalDateTime.now(),
                description = "Payday"
            ),
            tags = listOf(
                Tag(id = 4, name = "pay", type = TransactionType.Income),
            )
        ),
    )
}