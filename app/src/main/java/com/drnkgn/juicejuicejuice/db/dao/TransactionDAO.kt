package com.drnkgn.juicejuicejuice.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.drnkgn.juicejuicejuice.db.entities.Transaction as TransactionEnt
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {
    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactionsWithTags(): List<TransactionWithTags>

    @Query("SELECT * FROM transactions WHERE DATE(transaction_at) = :date")
    suspend fun getAllTransactionsWithTagsByDate(date: String): List<TransactionWithTags>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionWithTagsById(id: Int): TransactionWithTags?

    @Insert
    suspend fun create(transaction: TransactionEnt): Long

    @Update
    suspend fun update(transaction: TransactionEnt)
}