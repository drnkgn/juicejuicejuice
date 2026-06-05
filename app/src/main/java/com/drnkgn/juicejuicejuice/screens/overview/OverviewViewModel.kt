package com.drnkgn.juicejuicejuice.screens.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drnkgn.juicejuicejuice.db.AppDatabase
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

@HiltViewModel
class OverviewViewModel @Inject constructor(
    database: AppDatabase
): ViewModel() {
    private val transactionDao = database.transaction()

    val indexTransactionState = UiStateHolder<List<TransactionWithTags>>(Resource.Idle)

    fun indexTransactions(
        date: LocalDate? = null,
        type: TransactionType? = null,
        withDeleted: Boolean = false
    ) {
        var queryDate: String? = null
        if (date is LocalDate) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            queryDate = formatter.format(date)
        }

        viewModelScope.launch {
            indexTransactionState.set(isLoading = true)
            indexTransactionState.set(
                isLoading = false,
                data = Utils.safeApiCall { transactionDao.index(queryDate, type, withDeleted) }
            )
        }
    }
}