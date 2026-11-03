package com.example.spendlite.data.model

data class UserDto(
    val id: String = "",
    val email: String = "",
    val displayName: String? = null
)


data class Expense(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val date: String = "",
    var isExpense: Boolean = true
)