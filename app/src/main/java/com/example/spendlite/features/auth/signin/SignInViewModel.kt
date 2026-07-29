package com.example.spendlite.features.auth.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendlite.data.remote.FirebaseService
import com.example.spendlite.data.repository.ExpenseRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var password by mutableStateOf("")
        private set
    var passwordVisible by mutableStateOf(false)
        private set

    val state: SignInState
        get() = SignInState(email, emailError, password, passwordVisible)

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.OnEmailChanged -> {
                email = event.value
                emailError = null
            }

            is SignInEvent.OnPasswordChanged -> password = event.value
            is SignInEvent.OnTogglePasswordVisibility -> passwordVisible = !passwordVisible
            is SignInEvent.OnLoginClick -> {
                FirebaseService.signIn(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSignInSuccess()
                            event.onResult(true)
                        } else {
                            emailError = when (task.exception) {
                                is FirebaseAuthInvalidUserException -> "Gmail is not Registered"
                                is FirebaseAuthInvalidCredentialsException -> "Invalid Credentials"
                                else -> task.exception?.message ?: "Login Failed!!"
                            }
                            event.onResult(false)
                        }
                    }
            }

            is SignInEvent.OnGoogleSignInResult -> {
                FirebaseService.signInWithGoogle(event.idToken)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSignInSuccess()
                            event.onResult(true, null)
                        } else {
                            event.onResult(
                                false,
                                "Firebase Auth Failed: ${task.exception?.message}"
                            )
                        }
                    }
            }
        }
    }

    private fun onSignInSuccess() {
        val uid = FirebaseService.currentUser()?.uid ?: return
        viewModelScope.launch {
            repository.syncFromRemote(uid)
        }
    }
}
