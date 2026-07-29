package com.example.spendlite.features.addexpense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendlite.data.local.entity.ExpenseEntity
import com.example.spendlite.data.remote.FirebaseService
import com.example.spendlite.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    var newspends by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf("Food")
        private set

    val state: AddExpenseState
        get() = AddExpenseState(
            newspends,
            selectedCategory
        )

    fun onEvent(event: AddExpenseEvent) {

        when (event) {

            is AddExpenseEvent.OnTitleChanged -> {
                newspends = event.value
            }

            is AddExpenseEvent.OnCategorySelected -> {
                selectedCategory = event.category
            }

            is AddExpenseEvent.OnSaveExpense -> {

                val userId =
                    FirebaseService.currentUser()?.uid
                        ?: return

                val expense = ExpenseEntity(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    title = newspends,
                    amount = event.amount,
                    category = selectedCategory,
                    date = event.date,
                    isExpense = event.isExpense
                )

                viewModelScope.launch {
                    repository.addExpense(expense)
                }

                newspends = ""
                selectedCategory = "Food"
            }
        }
    }
}