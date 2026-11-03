package com.example.spendlite.core.utils

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

enum class AuthField {
    EMAIL,
    PASSWORD
}

sealed class AuthErrorResult {
    data class FieldError(val field: AuthField, val message: String) : AuthErrorResult()
    data class GeneralError(val message: String) : AuthErrorResult()
}

object AuthErrorMapper {

    const val INCORRECT_CREDENTIALS_MSG = "Incorrect email or password"
    const val NO_USER_FOUND_MSG = "No account found for this email"
    const val ACCOUNT_DISABLED_MSG = "This account has been disabled"
    const val WEAK_PASSWORD_MSG = "Password must be at least 6 characters"
    const val USER_COLLISION_MSG = "This email is already registered"
    const val INVALID_EMAIL_MSG = "Enter a valid email address"
    const val TOO_MANY_REQUESTS_MSG = "Too many attempts. Try again later."
    const val NETWORK_ERROR_MSG = "No internet connection"
    const val UNKNOWN_ERROR_MSG = "Something went wrong. Please try again."

    fun mapSignInError(e: Exception?): AuthErrorResult {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                AuthErrorResult.FieldError(AuthField.PASSWORD, INCORRECT_CREDENTIALS_MSG)
            }
            is FirebaseAuthInvalidUserException -> {
                val message = if (e.errorCode == "ERROR_USER_DISABLED") {
                    ACCOUNT_DISABLED_MSG
                } else {
                    NO_USER_FOUND_MSG
                }
                AuthErrorResult.FieldError(AuthField.EMAIL, message)
            }
            is FirebaseTooManyRequestsException -> {
                AuthErrorResult.GeneralError(TOO_MANY_REQUESTS_MSG)
            }
            is FirebaseNetworkException -> {
                AuthErrorResult.GeneralError(NETWORK_ERROR_MSG)
            }
            else -> {
                AuthErrorResult.GeneralError(e?.message ?: UNKNOWN_ERROR_MSG)
            }
        }
    }

    fun mapSignUpError(e: Exception?): AuthErrorResult {
        return when (e) {
            is FirebaseAuthWeakPasswordException -> {
                AuthErrorResult.FieldError(AuthField.PASSWORD, WEAK_PASSWORD_MSG)
            }
            is FirebaseAuthUserCollisionException -> {
                AuthErrorResult.FieldError(AuthField.EMAIL, USER_COLLISION_MSG)
            }
            is FirebaseAuthInvalidCredentialsException -> {
                AuthErrorResult.FieldError(AuthField.EMAIL, INVALID_EMAIL_MSG)
            }
            is FirebaseTooManyRequestsException -> {
                AuthErrorResult.GeneralError(TOO_MANY_REQUESTS_MSG)
            }
            is FirebaseNetworkException -> {
                AuthErrorResult.GeneralError(NETWORK_ERROR_MSG)
            }
            else -> {
                AuthErrorResult.GeneralError(e?.message ?: UNKNOWN_ERROR_MSG)
            }
        }
    }
}
