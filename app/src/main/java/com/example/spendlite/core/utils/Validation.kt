package com.example.spendlite.core.utils

object Validation {
    private val AMOUNT_INPUT_REGEX = Regex("^\\d*\\.?\\d{0,2}$")

    // Restricts raw text-field input to digits with up to 2 decimal places
    fun isValidAmountInput(input: String): Boolean {
        return input.matches(AMOUNT_INPUT_REGEX)
    }

    // Final check before saving an expense (used on the Save button)
    fun isValidExpense(amount: Double?, title: String): Boolean {
        return amount != null && amount > 0 && title.isNotBlank()
    }
}