package com.example.spendlite.data.remote

import com.example.spendlite.data.model.Expense
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ExpenseApi {
    @POST("expenses")
    suspend fun saveExpense(@Body expense: Expense): Response<Expense>

    @GET("expenses")
    suspend fun getAllExpenses(
        @Query("category") category: String? = null
    ): List<Expense>

    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: String): Response<Unit>
}