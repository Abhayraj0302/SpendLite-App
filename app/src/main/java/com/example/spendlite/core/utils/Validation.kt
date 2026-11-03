package com.example.spendlite.core.utils

object Validation {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun isValidAmountInput(input: String): Boolean {
        if (input.isEmpty()) return true
        val regex = Regex("^\\d*(\\.\\d{0,2})?$")
        return regex.matches(input)
    }

    fun isValidExpense(amount: Double?, title: String): Boolean {
        return amount != null && amount > 0 && title.isNotBlank()
    }

    fun validateEmail(email: String): String? {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) {
            return "Email is required"
        }
        if (!EMAIL_REGEX.matches(trimmed)) {
            return "Enter a valid email address"
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isEmpty()) {
            return "Password is required"
        }
        if (password.length < 6) {
            return "Password must be at least 6 characters"
        }
        return null
    }
}
