package com.drnkgn.juicejuicejuice.screens.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drnkgn.juicejuicejuice.db.AppDatabase
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.enums.UiState
import com.drnkgn.juicejuicejuice.utils.UiStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val database: AppDatabase
): ViewModel() {
    private val transactionDao = database.transaction()

    val indexTransactionState = UiStateHolder<List<TransactionWithTags>>(UiState.Idle)

    fun indexTransactions(date: LocalDate? = null) {
        var queryDate: String? = null
        if (date is LocalDate) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            queryDate = formatter.format(date)
        }

        viewModelScope.launch {
            indexTransactionState.set(UiState.Loading)
            indexTransactionState.set(
                UiState.Success(transactionDao.index(queryDate))
            )
        }
    }
}