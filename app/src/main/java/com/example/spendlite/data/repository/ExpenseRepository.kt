package com.example.spendlite.data.repository

import android.util.Log
import com.example.spendlite.data.local.Dao.ExpenseDao
import com.example.spendlite.data.local.entity.ExpenseEntity
import com.example.spendlite.data.mapper.toEntity
import com.example.spendlite.data.mapper.toExpenseModel
import com.example.spendlite.data.remote.ExpenseApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val dao: ExpenseDao,
    private val api: ExpenseApi
) {

    suspend fun addExpense(
        expense: ExpenseEntity
    ) {
        dao.insertExpense(expense)

        try {
            val response = api.saveExpense(expense.toExpenseModel())
            if (response.isSuccessful) {
                Log.d("ExpenseRepository", "Expense saved to MongoDB: ${response.body()}")
            } else {
                Log.e("ExpenseRepository", "Failed to save to MongoDB: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("ExpenseRepository", "Network error saving expense to MongoDB: ${e.message}", e)
        }
    }

    suspend fun deleteExpense(
        expense: ExpenseEntity
    ) {
        dao.deleteExpense(expense)

        try {
            val response = api.deleteExpense(expense.id)
            if (response.isSuccessful) {
                Log.d("ExpenseRepository", "Expense deleted from MongoDB: ${expense.id}")
            } else {
                Log.e("ExpenseRepository", "Failed to delete from MongoDB: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ExpenseRepository", "Error deleting expense from MongoDB: ${e.message}", e)
        }
    }

    fun getExpenses(
        userId: String
    ): Flow<List<ExpenseEntity>> {
        return dao.getExpenses(userId)
    }
    suspend fun syncFromRemote(
        userId: String
    ) {
        try {
            val remoteExpenses = api.getAllExpenses()
            dao.clearForUser(userId)
            dao.insertAll(
                remoteExpenses.map {
                    it.toEntity(userId)
                }
            )
            Log.d("ExpenseRepository", "Synced ${remoteExpenses.size} expenses from MongoDB")
        } catch (e: Exception) {
            Log.e("ExpenseRepository", "Network error syncing from MongoDB: ${e.message}", e)
        }
    }
}
