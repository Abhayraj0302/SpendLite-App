package com.example.spendlite.data.local.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.spendlite.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: ExpenseEntity)

    @Insert
    suspend fun insertAll(expenses: List<ExpenseEntity>)

    @Query("SELECT * FROM expenses WHERE userId = :userId")
    fun getExpenses(userId: String): Flow<List<ExpenseEntity>>

    @Query("DELETE FROM expenses WHERE userId = :userId")
    suspend fun clearForUser(userId: String)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
}