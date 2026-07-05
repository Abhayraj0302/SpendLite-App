package com.example.spendlite.features.auth.signup

data class SignUpState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordVisible: Boolean = false
)