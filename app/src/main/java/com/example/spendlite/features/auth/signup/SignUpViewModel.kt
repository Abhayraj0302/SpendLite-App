package com.example.spendlite.features.auth.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.spendlite.data.remote.FirebaseService
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class SignUpViewModel : ViewModel() {

    var email by mutableStateOf("")
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var password by mutableStateOf("")
        private set
    var passwordVisible by mutableStateOf(false)
        private set

    val state: SignUpState
        get() = SignUpState(email, emailError, password, passwordVisible)

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnEmailChanged -> {
                email = event.value
                emailError = null
            }
            is SignUpEvent.OnPasswordChanged -> password = event.value
            is SignUpEvent.OnTogglePasswordVisibility -> passwordVisible = !passwordVisible
            is SignUpEvent.OnSignUpClick -> {
                FirebaseService.signUp(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            event.onResult(true)
                        } else {
                            emailError = when (task.exception) {
                                is FirebaseAuthInvalidCredentialsException -> "Invalid Email Format"
                                else -> task.exception?.message ?: "Signup Failed"
                            }
                            event.onResult(false)
                        }
                    }
            }
        }
    }
}