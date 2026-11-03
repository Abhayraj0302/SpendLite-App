package com.example.spendlite.data.mapper

import androidx.compose.material3.Button
import com.example.spendlite.data.local.entity.ExpenseEntity
import com.example.spendlite.data.model.Expense

fun Expense.toEntity(userId: String): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        userId = userId,
        title = title,
        amount = amount,
        category = category,
        date = date,
        isExpense = isExpense
    )
}

fun ExpenseEntity.toExpenseModel(): Expense {
    return Expense(
        id = id,
        userId = userId,
        title = title,
        amount = amount,
        category = category,
        date = date,
        isExpense = isExpense
    )
}