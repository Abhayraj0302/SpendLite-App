package com.example.spendlite.features.auth.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendlite.core.utils.AuthErrorMapper
import com.example.spendlite.core.utils.AuthErrorResult
import com.example.spendlite.core.utils.AuthField
import com.example.spendlite.core.utils.AuthSource
import com.example.spendlite.core.utils.Validation
import com.example.spendlite.data.remote.FirebaseService
import com.example.spendlite.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

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
    var passwordError by mutableStateOf<String?>(null)
        private set
    var passwordVisible by mutableStateOf(false)
        private set
    var generalError by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var loadingSource by mutableStateOf<AuthSource?>(null)
        private set
    var errorTick by mutableIntStateOf(0)
        private set

    val state: SignInState
        get() = SignInState(
            email = email,
            emailError = emailError,
            password = password,
            passwordError = passwordError,
            passwordVisible = passwordVisible,
            generalError = generalError,
            isLoading = isLoading,
            loadingSource = loadingSource,
            errorTick = errorTick
        )

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.OnEmailChanged -> {
                if (isLoading) return
                email = event.value
                emailError = null
                generalError = null
            }

            is SignInEvent.OnPasswordChanged -> {
                if (isLoading) return
                password = event.value
                passwordError = null
                generalError = null
            }

            is SignInEvent.OnTogglePasswordVisibility -> {
                if (isLoading) return
                passwordVisible = !passwordVisible
            }

            is SignInEvent.OnLoginClick -> {
                if (isLoading) return

                val eError = Validation.validateEmail(email)
                val pError = Validation.validatePassword(password)
                if (eError != null || pError != null) {
                    emailError = eError
                    passwordError = pError
                    errorTick++
                    event.onResult(false)
                    return
                }

                isLoading = true
                loadingSource = AuthSource.EMAIL
                emailError = null
                passwordError = null
                generalError = null

                viewModelScope.launch {
                    try {
                        withTimeout(15_000) {
                            FirebaseService.signIn(email, password).await()
                        }
                        onSignInSuccess()
                        event.onResult(true)
                    } catch (e: Exception) {
                        if (e is CancellationException && e !is TimeoutCancellationException) {
                            throw e
                        }
                        isLoading = false
                        loadingSource = null
                        errorTick++

                        if (e is TimeoutCancellationException) {
                            generalError = "Request timed out. Check your connection and try again."
                        } else {
                            when (val result = AuthErrorMapper.mapSignInError(e)) {
                                is AuthErrorResult.FieldError -> {
                                    when (result.field) {
                                        AuthField.EMAIL -> emailError = result.message
                                        AuthField.PASSWORD -> passwordError = result.message
                                    }
                                }
                                is AuthErrorResult.GeneralError -> {
                                    generalError = result.message
                                }
                            }
                        }
                        event.onResult(false)
                    }
                }
            }

            is SignInEvent.OnGoogleSignInResult -> {
                if (isLoading) return

                isLoading = true
                loadingSource = AuthSource.GOOGLE
                emailError = null
                passwordError = null
                generalError = null

                viewModelScope.launch {
                    try {
                        withTimeout(15_000) {
                            FirebaseService.signInWithGoogle(event.idToken).await()
                        }
                        onSignInSuccess()
                        event.onResult(true, null)
                    } catch (e: Exception) {
                        if (e is CancellationException && e !is TimeoutCancellationException) {
                            throw e
                        }
                        isLoading = false
                        loadingSource = null
                        errorTick++

                        val errorMessage = if (e is TimeoutCancellationException) {
                            "Request timed out. Check your connection and try again."
                        } else {
                            e.message ?: "Google Sign-In Failed"
                        }
                        generalError = errorMessage
                        event.onResult(false, errorMessage)
                    }
                }
            }
        }
    }

    private suspend fun onSignInSuccess() {
        val user = FirebaseService.currentUser() ?: return
        repository.registerUser(user.uid, user.email ?: "")
        repository.syncFromRemote(user.uid)
    }
}
