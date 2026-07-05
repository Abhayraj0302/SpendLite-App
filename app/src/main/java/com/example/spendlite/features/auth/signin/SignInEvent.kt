package com.example.spendlite.features.auth.signin

sealed class SignInEvent {
    data class OnEmailChanged(val value: String) : SignInEvent()
    data class OnPasswordChanged(val value: String) : SignInEvent()
    object OnTogglePasswordVisibility : SignInEvent()
    data class OnLoginClick(val onResult: (success: Boolean) -> Unit) : SignInEvent()
    data class OnGoogleSignInResult(
        val idToken: String,
        val onResult: (success: Boolean, errorMessage: String?) -> Unit
    ) : SignInEvent()
}