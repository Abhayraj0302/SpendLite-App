package com.example.spendlite.features.auth.signup

sealed class SignUpEvent {
    data class OnEmailChanged(val value: String) : SignUpEvent()
    data class OnPasswordChanged(val value: String) : SignUpEvent()
    object OnTogglePasswordVisibility : SignUpEvent()
    data class OnSignUpClick(val onResult: (success: Boolean) -> Unit) : SignUpEvent()
}