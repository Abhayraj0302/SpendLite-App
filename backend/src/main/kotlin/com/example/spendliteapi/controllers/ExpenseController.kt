package com.example.spendliteapi.controllers

import com.example.spendliteapi.database.model.Expense
import com.example.spendliteapi.database.repository.ExpenseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/expenses")
class ExpenseController(
    private val repository: ExpenseRepository
) {
    @PostMapping
    fun save(@RequestBody body: Expense): ResponseEntity<Expense> =
        ResponseEntity.status(HttpStatus.CREATED).body(repository.save(body))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build()
        repository.deleteById(id)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody body: Expense): ResponseEntity<Expense> {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(repository.save(body.copy(id = id)))
    }

    @GetMapping
    fun getAll(
        @RequestParam(required = false) category: String?
    ): List<Expense> =
        if (category != null) repository.findByCategory(category)
        else repository.findAll()

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: String
    ): ResponseEntity<Expense> =
        repository.findById(id).map {
            ResponseEntity.ok(it)
        }.orElse(null) ?: ResponseEntity.notFound().build()
}