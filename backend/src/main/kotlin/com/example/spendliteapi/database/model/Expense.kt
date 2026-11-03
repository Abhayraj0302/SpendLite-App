package com.example.spendliteapi.database.model

import org.springframework.data.mongodb.core.mapping.Document


@Document("expenses")
data class Expense(
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val date: String = "",
    var isExpense: Boolean = true,
    val userId: String = ""
)