package com.example.spendlite.features.home

import com.example.spendlite.data.local.entity.ExpenseEntity

data class HomeState(
    val totalCountValue: Int,
    val totalCount: String,
    val selectedFilter: String,
    val filteredExpenses: List<ExpenseEntity>
)
