package com.example.spendlite.features.home

import com.example.spendlite.data.local.entity.ExpenseEntity

sealed class HomeEvent {
    data class OnFilterSelected(val filter: String) : HomeEvent()
    data class OnDeleteExpense(val expense: ExpenseEntity) : HomeEvent()
}
