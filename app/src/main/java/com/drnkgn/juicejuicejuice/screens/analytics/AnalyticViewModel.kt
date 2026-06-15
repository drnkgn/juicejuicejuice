package com.drnkgn.juicejuicejuice.screens.analytics

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drnkgn.juicejuicejuice.DatabaseManager
import com.drnkgn.juicejuicejuice.db.dto.DailySpend
import com.drnkgn.juicejuicejuice.db.dto.TagSpend
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiStateHolder
import com.drnkgn.juicejuicejuice.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AnalyticViewModel @Inject constructor(
    databaseManager: DatabaseManager
): ViewModel() {
    private val database = databaseManager.getDb()

    private val analyticsDao = database.analytics()

    val dailySpendingState = UiStateHolder<List<DailySpend>>(Resource.Idle)
    val tagSpendingState = UiStateHolder<List<TagSpend>>(Resource.Idle)

    fun getDailySpending(date: LocalDate) {
        viewModelScope.launch {
            dailySpendingState.set(isLoading = true)

            val resource = Utils.safeApiCall {
                val result = analyticsDao.getDailySpending(
                    date.with(DayOfWeek.MONDAY), date.with(DayOfWeek.SUNDAY),
                )

                (0..6).map { dayOfWeek ->
                    val amountSum = result.find { it.dayOfWeek == dayOfWeek }?.amountSum ?: 0f
                    DailySpend(dayOfWeek, amountSum)
                }
            }

            dailySpendingState.set(isLoading = false, data = resource)
        }
    }

    fun getSpendingByTags(date: LocalDate) {
        viewModelScope.launch {
            tagSpendingState.set(isLoading = true)
            val resource = Utils.safeApiCall {
                analyticsDao.getSpendingByTags(
                    date.with(DayOfWeek.MONDAY), date.with(DayOfWeek.SUNDAY),
                )
            }

            Log.d("JJJ", resource.toString())
            tagSpendingState.set(isLoading = false, data = resource)
        }
    }
}