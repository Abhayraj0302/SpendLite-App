package com.example.spendlite.data.repository

import android.util.Log
import com.example.spendlite.core.utils.AuthConstants
import com.example.spendlite.data.local.Dao.ExpenseDao
import com.example.spendlite.data.local.database.ExpenseApi
import com.example.spendlite.data.local.entity.ExpenseEntity
import com.example.spendlite.data.mapper.toEntity
import com.example.spendlite.data.mapper.toExpenseModel
import com.example.spendlite.data.model.UserDto
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val dao: ExpenseDao,
    private val api: ExpenseApi
) {

    suspend fun registerUser(uid: String, email: String) {
        try {
            val response = api.upsertUser(uid, UserDto(id = uid, email = email))
            if (response.isSuccessful) {
                Log.d(TAG, "User registered in MongoDB: $uid")
            } else {
                Log.e(TAG, "Failed to register user: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            handleNetworkError("registering user in MongoDB", e)
        }
    }

    suspend fun addExpense(
        expense: ExpenseEntity
    ) {
        dao.insertExpense(expense)

        try {
            val response = api.saveExpense(expense.toExpenseModel())
            if (response.isSuccessful) {
                Log.d(TAG, "Expense saved to MongoDB: ${response.body()}")
            } else {
                Log.e(TAG, "Failed to save to MongoDB: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            handleNetworkError("saving expense to MongoDB", e)
        }
    }

    suspend fun deleteExpense(
        expense: ExpenseEntity
    ) {
        dao.deleteExpense(expense)

        try {
            val response = api.deleteExpense(expense.id)
            if (response.isSuccessful) {
                Log.d(TAG, "Expense deleted from MongoDB: ${expense.id}")
            } else {
                Log.e(TAG, "Failed to delete from MongoDB: ${response.code()}")
            }
        } catch (e: Exception) {
            handleNetworkError("deleting expense from MongoDB", e)
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
            val remoteExpenses = api.getAllExpenses(userId)
            dao.clearForUser(userId)
            dao.insertAll(
                remoteExpenses.map {
                    it.toEntity(userId)
                }
            )
            Log.d(TAG, "Synced ${remoteExpenses.size} expenses from MongoDB")
        } catch (e: Exception) {
            handleNetworkError("syncing from MongoDB", e)
        }
    }

    private fun handleNetworkError(action: String, e: Exception) {
        when (e) {
            is SocketTimeoutException, is ConnectException, is UnknownHostException -> {
                Log.w(
                    TAG,
                    "MongoDB backend server at ${AuthConstants.BASE_URL} is unreachable ($action). Operating in local offline mode: ${e.message}"
                )
            }
            is IOException -> {
                Log.w(TAG, "Network I/O error ($action): ${e.message}")
            }
            else -> {
                Log.e(TAG, "Error $action: ${e.message}", e)
            }
        }
    }

    private companion object {
        const val TAG = "ExpenseRepository"
    }
}