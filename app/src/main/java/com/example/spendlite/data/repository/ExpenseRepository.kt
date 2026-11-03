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
        val localExpense = expense.copy(isSynced = false, isDeleted = false)
        dao.insertExpense(localExpense)

        try {
            val response = api.saveExpense(expense.toExpenseModel())
            if (response.isSuccessful) {
                dao.markSynced(expense.id)
                Log.d(TAG, "Expense saved and marked synced on MongoDB: ${expense.id}")
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
        if (!expense.isSynced) {
            dao.deleteExpenseById(expense.id)
        } else {
            dao.insertExpense(expense.copy(isDeleted = true, isSynced = false))
        }

        try {
            val response = api.deleteExpense(expense.id)
            if (response.isSuccessful) {
                dao.deleteExpenseById(expense.id)
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
            val unsyncedList = dao.getUnsyncedExpenses(userId)
            for (unsynced in unsyncedList) {
                try {
                    val response = api.saveExpense(unsynced.toExpenseModel())
                    if (response.isSuccessful) {
                        dao.markSynced(unsynced.id)
                        Log.d(TAG, "Pushed offline expense to MongoDB: ${unsynced.id}")
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to push offline expense ${unsynced.id}: ${e.message}")
                }
            }

            val pendingDeletions = dao.getPendingDeletedExpenses(userId)
            for (pending in pendingDeletions) {
                try {
                    val response = api.deleteExpense(pending.id)
                    if (response.isSuccessful) {
                        dao.deleteExpenseById(pending.id)
                        Log.d(TAG, "Pushed offline deletion to MongoDB: ${pending.id}")
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to push offline deletion ${pending.id}: ${e.message}")
                }
            }

            val remoteExpenses = api.getAllExpenses(userId)
            dao.clearSyncedForUser(userId)
            val remoteEntities = remoteExpenses.map {
                it.toEntity(userId).copy(isSynced = true, isDeleted = false)
            }
            dao.insertAll(remoteEntities)
            Log.d(TAG, "Synced ${remoteExpenses.size} remote expenses from MongoDB")
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
