package com.example.spendlite.data.model

import com.google.firebase.firestore.PropertyName

data class Expense(
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val date: String = "",
    @get:PropertyName("isExpense")
    @set:PropertyName("isExpense")
    var isExpense: Boolean = true
)