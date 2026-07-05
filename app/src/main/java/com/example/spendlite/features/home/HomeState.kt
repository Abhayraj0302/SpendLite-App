package com.example.spendlite.features.home

import com.example.spendlite.data.model.Expense

data class HomeState(
    val totalCountValue: Int,
    val totalCount: String,
    val selectedFilter: String,
    val filteredExpenses: List<Expense>
)