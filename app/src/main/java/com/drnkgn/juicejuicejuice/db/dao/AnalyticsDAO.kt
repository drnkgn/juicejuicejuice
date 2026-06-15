package com.drnkgn.juicejuicejuice.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.drnkgn.juicejuicejuice.db.dto.DailySpend
import com.drnkgn.juicejuicejuice.db.dto.TagSpend
import java.time.LocalDate

@Dao
interface AnalyticsDAO {
    @Query("""
        SELECT
            strftime('%w', datetime(transaction_at, 'unixepoch')) as dayOfWeek,
            SUM(amount) as amountSum
        FROM transactions
        WHERE (DATE(transaction_at) >= :startOfWeek AND DATE(transaction_at) <= :endOfWeek)
        GROUP BY strftime('%w', datetime(transaction_at, 'unixepoch'))
        ORDER BY strftime('%w', datetime(transaction_at, 'unixepoch'))
    """)
    suspend fun getDailySpending(startOfWeek: LocalDate, endOfWeek: LocalDate): List<DailySpend>

    @Query("""
        SELECT
            tags.id as tagId,
            tags.name as tagName,
            SUM(txn.amount) as amountSum
        FROM transactions txn
        INNER JOIN transaction_tags tt ON tt.transaction_id = txn.id
        INNER JOIN tags ON tags.id = tt.tag_id
        WHERE (DATE(txn.transaction_at) >= :startOfWeek AND DATE(txn.transaction_at) <= :endOfWeek)
        GROUP BY tags.id
    """)
    suspend fun getSpendingByTags(startOfWeek: LocalDate, endOfWeek: LocalDate): List<TagSpend>
}