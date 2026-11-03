package com.example.spendlite.data.local.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spendlite.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<ExpenseEntity>)

    @Query("SELECT * FROM expenses WHERE userId = :userId AND isDeleted = 0")
    fun getExpenses(userId: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND isSynced = 0 AND isDeleted = 0")
    suspend fun getUnsyncedExpenses(userId: String): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND isDeleted = 1")
    suspend fun getPendingDeletedExpenses(userId: String): List<ExpenseEntity>

    @Query("UPDATE expenses SET isSynced = 1 WHERE id = :id")
    suspend fun markSynced(id: String)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: String)

    @Query("DELETE FROM expenses WHERE userId = :userId AND isSynced = 1 AND isDeleted = 0")
    suspend fun clearSyncedForUser(userId: String)
}
