package com.drnkgn.juicejuicejuice.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.drnkgn.juicejuicejuice.db.entities.Transaction as TransactionEnt
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.enums.TransactionType

@Dao
interface TransactionDAO {
    @Query("""
        SELECT * FROM transactions
        WHERE (:date IS NULL AND DATE(transaction_at) = CURRENT_DATE OR DATE(transaction_at) = :date)
        AND (:type IS NULL OR type = :type)
        AND (:withDeleted = 1 AND deleted_at IS NOT NULL OR deleted_at IS NULL)
        ORDER BY created_at DESC
    """)
    suspend fun index(
        date: String? = null,
        type: TransactionType? = null,
        withDeleted: Boolean = false
    ): List<TransactionWithTags>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionWithTagsById(id: Int): TransactionWithTags?

    @Insert
    suspend fun create(transaction: TransactionEnt): Long

    @Update
    suspend fun update(transaction: TransactionEnt)
}