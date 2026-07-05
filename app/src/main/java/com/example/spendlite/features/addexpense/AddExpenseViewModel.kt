package com.example.spendlite.features.addexpense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.spendlite.data.model.Expense
import com.example.spendlite.data.repository.ExpenseRepository

class AddExpenseViewModel : ViewModel() {

    var newspends by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf("Food")
        private set

    val state: AddExpenseState
        get() = AddExpenseState(newspends, selectedCategory)

    fun onEvent(event: AddExpenseEvent) {
        when (event) {
            is AddExpenseEvent.OnTitleChanged -> newspends = event.value
            is AddExpenseEvent.OnCategorySelected -> selectedCategory = event.category
            is AddExpenseEvent.OnSaveExpense -> {
                ExpenseRepository.addExpense(
                    Expense(
                        title = newspends,
                        amount = event.amount,
                        category = selectedCategory,
                        date = event.date,
                        isExpense = event.isExpense
                    )
                )
                newspends = ""
                selectedCategory = "Food"
            }
        }
    }
}