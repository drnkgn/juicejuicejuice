package com.drnkgn.juicejuicejuice.screens.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drnkgn.juicejuicejuice.db.AppDatabase
import com.drnkgn.juicejuicejuice.db.dto.OverviewStatsDTO
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.enums.TransactionType
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiStateHolder
import com.drnkgn.juicejuicejuice.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class OverviewViewModel @Inject constructor(
    database: AppDatabase
): ViewModel() {
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

            val pTxn = Utils.safeApiCall { transactionDao.index(queryDate, type, withDeleted) }
            val qTxn = Utils.safeApiCall { transactionDao.index(dayBeforeQueryDate, type, withDeleted) }

            indexTransactionState.set(isLoading = false, data = pTxn)
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

        // val txnIncPctDiff = ((pTxnIncTotal-qTxnIncTotal)/min(pTxnIncTotal, qTxnIncTotal))*100
        // val txnExpPctDiff = ((pTxnExpTotal-qTxnExpTotal)/min(pTxnExpTotal, qTxnExpTotal))*100
        val txnIncPctDiff = 0f
        val txnExpPctDiff = 0f

        return OverviewStatsDTO(
            income = pTxnIncTotal,
            expense = pTxnExpTotal,
            incomePctDiff = txnIncPctDiff,
            expensePctDiff = txnExpPctDiff
        )
    }
}