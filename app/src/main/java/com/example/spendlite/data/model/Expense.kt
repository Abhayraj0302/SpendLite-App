package com.example.spendlite.data.model


data class Expense(
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val date: String = "",
    var isExpense: Boolean = true
)