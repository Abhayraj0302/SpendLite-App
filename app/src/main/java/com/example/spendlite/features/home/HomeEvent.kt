package com.example.spendlite.features.home

import com.example.spendlite.data.model.Expense

sealed class HomeEvent {
    data class OnFilterSelected(val filter: String) : HomeEvent()
    data class OnDeleteExpense(val expense: Expense) : HomeEvent()
}