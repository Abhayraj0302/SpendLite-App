package com.example.spendliteapi.database.repository

import com.example.spendliteapi.database.model.Expense
import org.springframework.data.mongodb.repository.MongoRepository

interface ExpenseRepository : MongoRepository<Expense, String> {
    fun findByCategory(category: String): List<Expense>
    fun findByUserId(userId: String): List<Expense>
}