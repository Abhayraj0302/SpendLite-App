package com.example.spendlite.features.auth.signin

import com.example.spendlite.core.utils.AuthSource

data class SignInState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val passwordVisible: Boolean = false,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val loadingSource: AuthSource? = null,
    val errorTick: Int = 0
)
