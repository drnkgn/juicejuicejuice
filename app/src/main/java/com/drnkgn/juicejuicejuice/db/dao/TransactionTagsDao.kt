package com.drnkgn.juicejuicejuice.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.drnkgn.juicejuicejuice.db.entities.TransactionTagsPivot

@Dao
interface TransactionTagsDao {
    @Query("DELETE FROM transaction_tags WHERE transaction_id = :transactionId")
    suspend fun deleteTransactionRelations(transactionId: Int)

    @Insert
    suspend fun create(ref: TransactionTagsPivot)

    @Insert
    suspend fun createMany(ref: List<TransactionTagsPivot>)

    @Transaction
    suspend fun update(transactionId: Int, tagIds: List<Int>) {
        deleteTransactionRelations(transactionId)
        createMany(tagIds.map { tagId -> TransactionTagsPivot(transactionId, tagId) })
    }
}