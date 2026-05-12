package com.drnkgn.juicejuicejuice.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drnkgn.juicejuicejuice.enums.TransactionType
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
    val createdAt: String? = null
)