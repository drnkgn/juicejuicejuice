package com.drnkgn.juicejuicejuice.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drnkgn.juicejuicejuice.db.AppDatabase
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.db.entities.Transaction
import com.drnkgn.juicejuicejuice.db.entities.TransactionTagsPivot
import com.drnkgn.juicejuicejuice.db.relations.TransactionWithTags
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiStateHolder
import com.drnkgn.juicejuicejuice.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    database: AppDatabase
): ViewModel() {
    private val transactionTagsDao = database.transactionTags()
    private val transactionDao = database.transaction()
    private val tagDao = database.tag()

    val createTransactionState = UiStateHolder<Unit>(Resource.Idle)
    val updateTransactionState = UiStateHolder<Unit>(Resource.Idle)
    val getTransactionState = UiStateHolder<TransactionWithTags>(Resource.Idle)
    val getAllTagsState = UiStateHolder<List<Tag>>(Resource.Idle)

    fun getTransaction(id: Int) {
        viewModelScope.launch {
            getTransactionState.set(isLoading = true)
            val transaction = transactionDao.getTransactionWithTagsById(id)

            getTransactionState.set(
                isLoading = false,
                data = when {
                    transaction == null -> {
                        Resource.GenericError(Exception("No transaction found for id $id"))
                    }
                    else -> Resource.Success(transaction)
                }
            )
        }
    }

    fun getAllTags() {
        viewModelScope.launch {
            getAllTagsState.set(isLoading = true)
            getAllTagsState.set(
                isLoading = false,
                data = Utils.safeApiCall { tagDao.getAllTags() }
            )
        }
    }

    fun createTransaction(transaction: Transaction, tags: List<Tag>) {
        viewModelScope.launch {
            createTransactionState.set(isLoading = true)

            val id = transactionDao.create(transaction)
            transactionTagsDao.createMany(
                tags.map { tag -> TransactionTagsPivot(id.toInt(), tag.id) }
            )

            createTransactionState.set(
                data = Resource.Success(Unit)
            )
        }
    }

    fun updateTransaction(transaction: Transaction, tags: List<Tag>) {
        viewModelScope.launch {
            updateTransactionState.set(isLoading = true)

            transactionDao.update(transaction)
            transactionTagsDao.update(
                transactionId = transaction.id,
                tags.map { tag -> tag.id }
            )

            updateTransactionState.set(
                isLoading = false,
                data = Resource.Success(Unit)
            )
        }
    }
}