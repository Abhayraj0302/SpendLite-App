package com.example.spendlite.features.home

import androidx.compose.runtime.Immutable
import com.example.spendlite.data.local.entity.ExpenseEntity

@Immutable
data class HomeState(
    val totalCountValue: Int = 0,
    val totalCount: String = "0",
    val selectedFilter: String = "All",
    val filteredExpenses: List<ExpenseEntity> = emptyList(),
    val monthComparisonPercent: Int? = null,
    val spendingIncreased: Boolean = false
)
