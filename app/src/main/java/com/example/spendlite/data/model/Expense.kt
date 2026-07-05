package com.example.spendlite.data.model

data class Expense(
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    val isExpense: Boolean = true
)