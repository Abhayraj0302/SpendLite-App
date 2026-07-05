package com.example.spendlite.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.spendlite.data.model.Expense

// Singleton in-memory store — same storage your old ViewModelApp used
// (a mutableStateListOf, no persistence), just moved out so both
// HomeViewModel and AddExpenseViewModel can share the same list.
object ExpenseRepository {
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = _expenses

    fun addExpense(expense: Expense) {
        _expenses.add(0, expense)
    }

    fun deleteExpense(expense: Expense) {
        _expenses.remove(expense)
    }
}