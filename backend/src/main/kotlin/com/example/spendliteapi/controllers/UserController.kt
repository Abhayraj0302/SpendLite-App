package com.example.spendliteapi.controllers

import com.example.spendliteapi.database.model.User
import com.example.spendliteapi.database.repository.UserRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val repository: UserRepository) {

    @PutMapping("/{uid}")
    fun upsert(@PathVariable uid: String, @RequestBody body: User): User {
        val existing = repository.findById(uid).orElse(null)
        return repository.save(existing ?: body.copy(id = uid))
    }
}