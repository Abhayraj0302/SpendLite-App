package com.example.spendlite.features.addexpense

sealed class AddExpenseEvent {
    data class OnTitleChanged(val value: String) : AddExpenseEvent()
    data class OnCategorySelected(val category: String) : AddExpenseEvent()
    data class OnSaveExpense(
        val amount: Double,
        val date: String,
        val isExpense: Boolean
    ) : AddExpenseEvent()
}