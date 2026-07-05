package com.example.spendlite.features.auth.signin

data class SignInState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordVisible: Boolean = false
)