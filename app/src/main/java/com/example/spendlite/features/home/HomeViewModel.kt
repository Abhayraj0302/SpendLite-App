package com.example.spendlite.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendlite.core.utils.toFormattedAmount
import com.example.spendlite.data.local.entity.ExpenseEntity
import com.example.spendlite.data.remote.FirebaseService
import com.example.spendlite.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val selectedFilterFlow = MutableStateFlow("All")

    private val userId: String?
        get() = FirebaseService.currentUser()?.uid

    private val expensesFlow: Flow<List<ExpenseEntity>> =
        userId?.let { repository.getExpenses(it) } ?: flowOf(emptyList())

    val state: StateFlow<HomeState> =
        combine(expensesFlow, selectedFilterFlow) { expenses, filter ->
            val credits = expenses.filter { !it.isExpense }.sumOf { it.amount }
            val debits = expenses.filter { it.isExpense }.sumOf { it.amount }
            val totalCountValue = (credits - debits).toInt()

            val filtered = when (filter) {
                "Expense" -> expenses.filter { it.isExpense }
                "Income" -> expenses.filter { !it.isExpense }
                else -> expenses
            }

            HomeState(
                totalCountValue = totalCountValue,
                totalCount = kotlin.math.abs(totalCountValue).toDouble().toFormattedAmount(),
                selectedFilter = filter,
                filteredExpenses = filtered
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeState(
                totalCountValue = 0,
                totalCount = "0",
                selectedFilter = "All",
                filteredExpenses = emptyList()
            )
        )

    init {
        userId?.let { uid ->
            viewModelScope.launch {
                repository.syncFromRemote(uid)
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnFilterSelected -> {
                selectedFilterFlow.value = event.filter
            }

            is HomeEvent.OnDeleteExpense -> {
                viewModelScope.launch {
                    repository.deleteExpense(event.expense)
                }
            }
        }
    }
}
