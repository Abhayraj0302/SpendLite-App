package com.example.spendlite.ViewModelFiles

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.util.Locale


data class Expense(
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    val isExpense: Boolean = true
)
class ViewModelApp : ViewModel() {
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = _expenses

    val totalCount: String
        get() {
            val credits = _expenses.filter { !it.isExpense }.sumOf { it.amount }
            val debits  = _expenses.filter {  it.isExpense }.sumOf { it.amount }
            val total   = (credits - debits).toInt()
            return NumberFormat.getNumberInstance(Locale.US).format(total)
        }

    var selectedFilter by mutableStateOf("All")
        private set

    fun onFilterSelected(filter: String) {
        selectedFilter = filter
    }

    val filteredExpenses: List<Expense>
        get() = when (selectedFilter) {
            "Expense" -> _expenses.filter { it.isExpense }
            "Income"  -> _expenses.filter { !it.isExpense }
            "Pending" -> emptyList()
            else      -> _expenses
        }

    // ── Add Screen ──────────────────────────────────────────────
    var newspends by mutableStateOf("")
        private set

    fun updateNewSpends(value: String) { newspends = value }

    var selectexpenseCategory by mutableStateOf("Food")
        private set

    fun onCategorySelect(filter: String) { selectexpenseCategory = filter }

    fun addExpense(title: String, amount: Double, category: String, date: String,isExpense: Boolean) {
        _expenses.add(0, Expense(title, amount, category, date,isExpense))
        newspends = ""
        selectexpenseCategory = "Food"
    }
    fun deleteExpense(expense: Expense) {
        _expenses.remove(expense)
    }
}