package com.drnkgn.juicejuicejuice.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drnkgn.juicejuicejuice.db.AppDatabase
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.db.entities.Transaction
import com.drnkgn.juicejuicejuice.db.entities.TransactionTagsPivot
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.enums.UiState
import com.drnkgn.juicejuicejuice.utils.UiStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val database: AppDatabase
): ViewModel() {
    private val transactionTagsDao = database.transactionTags()
    private val transactionDao = database.transaction()
    private val tagDao = database.tag()

    val createTransactionState = UiStateHolder<Unit>(UiState.Idle)
    val updateTransactionState = UiStateHolder<Unit>(UiState.Idle)
    val getTransactionState = UiStateHolder<TransactionWithTags>(UiState.Idle)
    val getAllTagsState = UiStateHolder<List<Tag>>(UiState.Idle)

    fun getTransaction(id: Int) {
        viewModelScope.launch {
            getTransactionState.set(UiState.Loading)
            val transaction = transactionDao.getTransactionWithTagsById(id)

            getTransactionState.set(
                if (transaction == null)
                    UiState.Error("No transaction found for id $id")
                else
                    UiState.Success(transaction)
            )
        }
    }

    fun getAllTags() {
        viewModelScope.launch {
            getAllTagsState.set(UiState.Loading)
            getAllTagsState.set(UiState.Success(tagDao.getAllTags()))
        }
    }

    fun createTransaction(transaction: Transaction, tags: List<Tag>) {
        viewModelScope.launch {
            createTransactionState.set(UiState.Loading)

            val id = transactionDao.create(transaction)
            transactionTagsDao.createMany(
                tags.map { tag -> TransactionTagsPivot(id.toInt(), tag.id) }
            )

            createTransactionState.set(UiState.Success(Unit))
        }
    }

    fun updateTransaction(transaction: Transaction, tags: List<Tag>) {
        viewModelScope.launch {
            updateTransactionState.set(UiState.Loading)
            transactionDao.update(transaction)
            transactionTagsDao.update(
                transactionId = transaction.id,
                tags.map { tag -> tag.id }
            )
            updateTransactionState.set(UiState.Success(Unit))
        }
    }
}