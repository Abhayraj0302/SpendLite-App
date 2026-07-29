package com.example.spendlite.data.repository

import com.example.spendlite.data.local.Dao.ExpenseDao
import com.example.spendlite.data.local.entity.ExpenseEntity
import com.example.spendlite.data.mapper.toEntity
import com.example.spendlite.data.mapper.toExpenseModel
import com.example.spendlite.data.remote.FirebaseService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val dao: ExpenseDao
) {

    suspend fun addExpense(
        expense: ExpenseEntity
    ) {
        dao.insertExpense(expense)

        FirebaseService.addExpense(
            uid = expense.userId,
            expense = expense.toExpenseModel()
        )
    }

    suspend fun deleteExpense(
        expense: ExpenseEntity
    ) {

        dao.deleteExpense(expense)

        FirebaseService.deleteExpense(
            uid = expense.userId,
            expenseId = expense.id
        )
    }

    fun getExpenses(
        userId: String
    ): Flow<List<ExpenseEntity>> {

        return dao.getExpenses(userId)
    }

    suspend fun syncFromRemote(
        userId: String
    ) {
        val remoteExpenses =
            FirebaseService.getAllExpenses(userId)
        dao.clearForUser(userId)
        dao.insertAll(
            remoteExpenses.map {
                it.toEntity(userId)
            }
        )
    }
}