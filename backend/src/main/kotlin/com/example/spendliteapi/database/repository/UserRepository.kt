package com.example.spendliteapi.database.repository

import com.example.spendliteapi.database.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String>