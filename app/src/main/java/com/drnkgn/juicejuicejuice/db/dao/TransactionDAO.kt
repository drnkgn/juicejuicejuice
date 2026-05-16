package com.drnkgn.juicejuicejuice.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.drnkgn.juicejuicejuice.db.entities.Transaction as TransactionEnt
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags

@Dao
interface TransactionDAO {
    @Query("""
        SELECT * FROM transactions
        WHERE (:date IS NULL AND DATE(transaction_at) = CURRENT_DATE OR DATE(transaction_at) = :date)
        ORDER BY created_at DESC
    """)
    suspend fun index(date: String? = null): List<TransactionWithTags>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionWithTagsById(id: Int): TransactionWithTags?

    @Insert
    suspend fun create(transaction: TransactionEnt): Long

    @Update
    suspend fun update(transaction: TransactionEnt)
}