package com.drnkgn.juicejuicejuice.fakes

import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.enums.TransactionType

object FakeTags {
    val tags = listOf(
        Tag(id = 1, name = "salary", type = TransactionType.Income),
        Tag(id = 2, name = "refund", type = TransactionType.Income),
        Tag(id = 3, name = "bonus", type = TransactionType.Income),

        Tag(id = 4, name = "food", type = TransactionType.Expense),
        Tag(id = 5, name = "gas", type = TransactionType.Expense),
        Tag(id = 6, name = "rent", type = TransactionType.Expense),
    )
}