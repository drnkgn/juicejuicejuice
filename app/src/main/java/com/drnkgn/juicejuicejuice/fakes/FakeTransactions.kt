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
                type = TransactionType.Income,
                amount = 34.80f,
                transactionAt = LocalDateTime.now(),
                description = "Burgers"
            ),
            tags = listOf(
                Tag(id = 1, name = "food", type = TransactionType.Income),
                Tag(id = 2, name = "gas", type = TransactionType.Income),
                Tag(id = 3, name = "rent", type = TransactionType.Income),
            )
        )
    )
}