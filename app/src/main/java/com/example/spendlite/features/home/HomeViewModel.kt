package com.example.spendlite.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.spendlite.core.utils.toFormattedAmount
import com.example.spendlite.data.repository.ExpenseRepository

class HomeViewModel : ViewModel() {

    var selectedFilter by mutableStateOf("All")
        private set
    val state: HomeState
        get() {
            val expenses = ExpenseRepository.expenses
            val credits = expenses.filter { !it.isExpense }.sumOf { it.amount }
            val debits = expenses.filter { it.isExpense }.sumOf { it.amount }
            val totalCountValue = (credits - debits).toInt()

            val filtered = when (selectedFilter) {
                "Expense" -> expenses.filter { it.isExpense }
                "Income" -> expenses.filter { !it.isExpense }
                "Pending" -> emptyList()
                else -> expenses
            }

            return HomeState(
                totalCountValue = totalCountValue,
                totalCount = kotlin.math.abs(totalCountValue).toDouble().toFormattedAmount(),
                selectedFilter = selectedFilter,
                filteredExpenses = filtered
            )
        }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnFilterSelected -> selectedFilter = event.filter
            is HomeEvent.OnDeleteExpense -> ExpenseRepository.deleteExpense(event.expense)
        }
    }
}