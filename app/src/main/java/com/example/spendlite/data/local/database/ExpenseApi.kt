package com.example.spendlite.data.local.database

import com.example.spendlite.data.model.Expense
import com.example.spendlite.data.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ExpenseApi {
    @POST("expenses")
    suspend fun saveExpense(@Body expense: Expense): Response<Expense>

    @GET("expenses")
    suspend fun getAllExpenses(@Query("userId") userId: String): List<Expense>

    @PUT("users/{uid}")
    suspend fun upsertUser(@Path("uid") uid: String, @Body user: UserDto): Response<UserDto>

    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: String): Response<Unit>
}