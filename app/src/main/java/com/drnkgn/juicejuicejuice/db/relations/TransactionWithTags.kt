package com.drnkgn.juicejuicejuice.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.db.entities.Transaction
import com.drnkgn.juicejuicejuice.db.entities.TransactionTagsPivot

data class TransactionWithTags(
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            TransactionTagsPivot::class,
            parentColumn = "transaction_id",
            entityColumn = "tag_id"
        )
    )
    val tags: List<Tag>
)