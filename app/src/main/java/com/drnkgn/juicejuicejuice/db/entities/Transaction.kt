package com.drnkgn.juicejuicejuice.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.states.TransactionUiState
import java.time.LocalDateTime

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: TransactionType,
    val amount: Float,
    @ColumnInfo(name = "transaction_at")
    val transactionAt: LocalDateTime,
    val description: String?,
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: String? = null,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: String? = null
)

fun Transaction.toUiState() = TransactionUiState(
    id = id,
    type = type,
    amount = amount,
    transactionAt = transactionAt,
    description = description,
    createdAt = createdAt,
    deletedAt = deletedAt
)