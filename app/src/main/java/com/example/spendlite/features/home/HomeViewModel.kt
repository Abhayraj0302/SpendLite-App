package com.example.spendlite.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendlite.core.utils.DateUtils
import com.example.spendlite.core.utils.toFormattedAmount
import com.example.spendlite.data.local.entity.ExpenseEntity
import com.example.spendlite.data.remote.FirebaseService
import com.example.spendlite.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val selectedFilterFlow = MutableStateFlow("All")
    private val userIdFlow = MutableStateFlow(FirebaseService.currentUser()?.uid)

    private val expensesFlow: Flow<List<ExpenseEntity>> =
        userIdFlow.flatMapLatest { uid ->
            if (uid != null) {
                repository.getExpenses(uid)
            } else {
                flowOf(emptyList())
            }
        }

    private data class ProcessedExpense(
        val entity: ExpenseEntity,
        val timestamp: Long,
        val month: Int,
        val year: Int
    )

    val state: StateFlow<HomeState> =
        combine(expensesFlow, selectedFilterFlow) { expenses, filter ->
            val now = Calendar.getInstance()
            val currentMonth = now.get(Calendar.MONTH)
            val currentYear = now.get(Calendar.YEAR)

            val lastMonthCal = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }
            val lastMonth = lastMonthCal.get(Calendar.MONTH)
            val lastYear = lastMonthCal.get(Calendar.YEAR)

            val cal = Calendar.getInstance()

            var credits = 0.0
            var debits = 0.0
            var currentMonthExpenseTotal = 0.0
            var lastMonthExpenseTotal = 0.0

            val processedList = ArrayList<ProcessedExpense>(expenses.size)

            for (expense in expenses) {
                if (expense.isExpense) {
                    debits += expense.amount
                } else {
                    credits += expense.amount
                }

                val dateObj = DateUtils.parseDate(expense.date)
                val timestamp = dateObj?.time ?: 0L
                var month = -1
                var year = -1

                if (dateObj != null) {
                    cal.time = dateObj
                    month = cal.get(Calendar.MONTH)
                    year = cal.get(Calendar.YEAR)

                    if (expense.isExpense) {
                        if (month == currentMonth && year == currentYear) {
                            currentMonthExpenseTotal += expense.amount
                        } else if (month == lastMonth && year == lastYear) {
                            lastMonthExpenseTotal += expense.amount
                        }
                    }
                }

                processedList.add(ProcessedExpense(expense, timestamp, month, year))
            }

            val totalCountValue = (credits - debits).toInt()

            val filteredProcessed = when (filter) {
                "Expense" -> processedList.filter { it.entity.isExpense }
                "Income" -> processedList.filter { !it.entity.isExpense }
                else -> processedList
            }.sortedByDescending { it.timestamp }

            val filteredEntities = filteredProcessed.map { it.entity }

            val monthComparisonPercent = if (lastMonthExpenseTotal > 0) {
                (((currentMonthExpenseTotal - lastMonthExpenseTotal) / lastMonthExpenseTotal) * 100)
                    .roundToInt()
            } else null

            HomeState(
                totalCountValue = totalCountValue,
                totalCount = abs(totalCountValue).toDouble().toFormattedAmount(),
                selectedFilter = filter,
                filteredExpenses = filteredEntities,
                monthComparisonPercent = monthComparisonPercent,
                spendingIncreased = currentMonthExpenseTotal > lastMonthExpenseTotal
            )
        }
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HomeState()
            )

    init {
        FirebaseService.currentUser()?.let { user ->
            userIdFlow.value = user.uid
            viewModelScope.launch(Dispatchers.IO) {
                repository.registerUser(user.uid, user.email ?: "")
                repository.syncFromRemote(user.uid)
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnFilterSelected -> {
                selectedFilterFlow.value = event.filter
            }

            is HomeEvent.OnDeleteExpense -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteExpense(event.expense)
                }
            }
        }
    }
}
