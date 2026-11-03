package com.example.spendlite.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    val isExpense: Boolean,
    val isSynced: Boolean = true,
    val isDeleted: Boolean = false
)
