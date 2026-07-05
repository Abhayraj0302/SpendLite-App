package com.example.spendlite.features.addexpense

data class AddExpenseState(
    val newspends: String = "",
    val selectedCategory: String = "Food"
)