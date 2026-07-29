package com.example.spendlite.core.utils

object Validation {
    private val AMOUNT_INPUT_REGEX = Regex("^\\d*\\.?\\d{0,2}$")
    fun isValidAmountInput(input: String): Boolean {
        return input.matches(AMOUNT_INPUT_REGEX)
    }
    fun isValidExpense(amount: Double?, title: String): Boolean {
        return amount != null && amount > 0 && title.isNotBlank()
    }
}