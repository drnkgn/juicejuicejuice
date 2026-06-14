package com.drnkgn.juicejuicejuice.screens.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drnkgn.juicejuicejuice.db.dto.OverviewStatsDTO
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.DatabaseManager
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiStateHolder
import com.drnkgn.juicejuicejuice.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    databaseManager: DatabaseManager
): ViewModel() {
    private val database = databaseManager.getDb()

    private val transactionDao = database.transaction()

    val indexTransactionState = UiStateHolder<List<TransactionWithTags>>(Resource.Idle)
    val overviewStatsState = UiStateHolder<OverviewStatsDTO>(Resource.Idle)

    fun indexTransactions(
        date: LocalDate? = null,
        type: TransactionType? = null,
        withDeleted: Boolean = false
    ) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var dayBeforeQueryDate: String? = null
        var queryDate: String? = null

        if (date is LocalDate) {
            val qDate = date.minusDays(1)
            queryDate = formatter.format(date)
            dayBeforeQueryDate = formatter.format(qDate)
        } else {
            val qDate = LocalDate.now().minusDays(1)
            dayBeforeQueryDate = formatter.format(qDate)
        }

        viewModelScope.launch {
            indexTransactionState.set(isLoading = true)
            overviewStatsState.set(isLoading = true)

            val txns = Utils.safeApiCall { transactionDao.index(queryDate, type, withDeleted) }
            val pTxn = Utils.safeApiCall { transactionDao.index(queryDate, null, false) }
            val qTxn = Utils.safeApiCall { transactionDao.index(dayBeforeQueryDate, null, false) }

            indexTransactionState.set(isLoading = false, data = txns)
            overviewStatsState.set(
                isLoading = false,
                data = when {
                    pTxn is Resource.Success && qTxn is Resource.Success -> {
                        Resource.Success(calcStatsFromTransactions(pTxn.data, qTxn.data))
                    }
                    else -> {
                        Resource.GenericError(Exception("Unable to obtain transactions statistics"))
                    }
                }
            )
        }
    }

    /**
     * @param pTxn List of transactions on the selected date
     * @param qTxn List of transactions on the day before selected date
     */
    fun calcStatsFromTransactions(
        pTxn: List<TransactionWithTags>,
        qTxn: List<TransactionWithTags>
    ): OverviewStatsDTO {
        val pTxnInc = pTxn.filter { txn -> txn.transaction.type == TransactionType.Income }
        val qTxnInc = qTxn.filter { txn -> txn.transaction.type == TransactionType.Income }
        val pTxnExp = pTxn.filter { txn -> txn.transaction.type == TransactionType.Expense }
        val qTxnExp = qTxn.filter { txn -> txn.transaction.type == TransactionType.Expense }

        val pTxnIncTotal = pTxnInc.fold(0f) { acc, txn -> acc + txn.transaction.amount }
        val qTxnIncTotal = qTxnInc.fold(0f) { acc, txn -> acc + txn.transaction.amount }
        val pTxnExpTotal = pTxnExp.fold(0f) { acc, txn -> acc + txn.transaction.amount }
        val qTxnExpTotal = qTxnExp.fold(0f) { acc, txn -> acc + txn.transaction.amount }

        val txnIncPctDiff = when {
            pTxnIncTotal == 0f && qTxnIncTotal == 0f -> 0f
            qTxnIncTotal == 0f -> 100f
            pTxnIncTotal == 0f -> -100f
            else -> ((pTxnIncTotal-qTxnIncTotal)/qTxnIncTotal)*100
        }

        val txnExpPctDiff = when {
            pTxnExpTotal == 0f && qTxnExpTotal == 0f -> 0f
            qTxnExpTotal == 0f -> 100f
            pTxnExpTotal == 0f -> -100f
            else -> ((pTxnExpTotal-qTxnExpTotal)/qTxnExpTotal)*100
        }

        return OverviewStatsDTO(
            income = pTxnIncTotal,
            expense = pTxnExpTotal,
            incomePctDiff = txnIncPctDiff,
            expensePctDiff = txnExpPctDiff
        )
    }
}