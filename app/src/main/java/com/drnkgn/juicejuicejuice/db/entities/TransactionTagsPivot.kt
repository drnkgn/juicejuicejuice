package com.drnkgn.juicejuicejuice.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "transaction_tags",
    primaryKeys = ["transaction_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = Transaction::class,
            parentColumns = ["id"],
            childColumns = ["transaction_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["transaction_id"]),
        Index(value = ["tag_id"]),
    ]
)
data class TransactionTagsPivot(
    @ColumnInfo(name = "transaction_id") val transactionId: Int,
    @ColumnInfo(name = "tag_id") val tagId: Int,
)