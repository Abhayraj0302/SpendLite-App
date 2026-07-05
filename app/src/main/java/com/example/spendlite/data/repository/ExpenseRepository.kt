package com.example.spendlite.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.spendlite.data.model.Expense

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