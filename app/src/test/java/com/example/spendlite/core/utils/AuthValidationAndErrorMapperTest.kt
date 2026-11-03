package com.example.spendlite.core.utils

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthValidationAndErrorMapperTest {

    @Test
    fun `validateEmail returns error for empty email`() {
        val result = Validation.validateEmail("")
        assertEquals("Email is required", result)
    }

    @Test
    fun `validateEmail returns error for invalid email formats`() {
        assertEquals("Enter a valid email address", Validation.validateEmail("invalid"))
        assertEquals("Enter a valid email address", Validation.validateEmail("test@"))
        assertEquals("Enter a valid email address", Validation.validateEmail("test@domain"))
    }

    @Test
    fun `validateEmail returns null for valid email`() {
        assertNull(Validation.validateEmail("user@example.com"))
        assertNull(Validation.validateEmail("john.doe@sub.domain.co"))
    }

    @Test
    fun `validatePassword returns error for empty password`() {
        val result = Validation.validatePassword("")
        assertEquals("Password is required", result)
    }

    @Test
    fun `validatePassword returns error for short password`() {
        val result = Validation.validatePassword("12345")
        assertEquals("Password must be at least 6 characters", result)
    }

    @Test
    fun `validatePassword returns null for valid password`() {
        assertNull(Validation.validatePassword("123456"))
    }

    @Test
    fun `mapSignInError maps invalid credentials to password field`() {
        val exception = FirebaseAuthInvalidCredentialsException("code", "message")
        val result = AuthErrorMapper.mapSignInError(exception)
        assertEquals(
            AuthErrorResult.FieldError(AuthField.PASSWORD, AuthErrorMapper.INCORRECT_CREDENTIALS_MSG),
            result
        )
    }

    @Test
    fun `mapSignInError maps invalid user to email field`() {
        val exception = FirebaseAuthInvalidUserException("code", "message")
        val result = AuthErrorMapper.mapSignInError(exception)
        assertEquals(
            AuthErrorResult.FieldError(AuthField.EMAIL, AuthErrorMapper.NO_USER_FOUND_MSG),
            result
        )
    }

    @Test
    fun `mapSignInError maps disabled user to email field`() {
        val exception = FirebaseAuthInvalidUserException("ERROR_USER_DISABLED", "message")
        val result = AuthErrorMapper.mapSignInError(exception)
        assertEquals(
            AuthErrorResult.FieldError(AuthField.EMAIL, AuthErrorMapper.ACCOUNT_DISABLED_MSG),
            result
        )
    }

    @Test
    fun `mapSignInError maps network exception to general error`() {
        val exception = FirebaseNetworkException("Network error")
        val result = AuthErrorMapper.mapSignInError(exception)
        assertEquals(
            AuthErrorResult.GeneralError(AuthErrorMapper.NETWORK_ERROR_MSG),
            result
        )
    }

    @Test
    fun `mapSignUpError maps weak password before generic invalid credentials`() {
        val exception = FirebaseAuthWeakPasswordException("code", "Password too weak", "reason")
        val result = AuthErrorMapper.mapSignUpError(exception)
        assertEquals(
            AuthErrorResult.FieldError(AuthField.PASSWORD, AuthErrorMapper.WEAK_PASSWORD_MSG),
            result
        )
    }

    @Test
    fun `mapSignUpError maps user collision to email field`() {
        val exception = FirebaseAuthUserCollisionException("code", "User exists")
        val result = AuthErrorMapper.mapSignUpError(exception)
        assertEquals(
            AuthErrorResult.FieldError(AuthField.EMAIL, AuthErrorMapper.USER_COLLISION_MSG),
            result
        )
    }

    @Test
    fun `mapSignUpError maps invalid credentials to email field`() {
        val exception = FirebaseAuthInvalidCredentialsException("code", "Invalid email")
        val result = AuthErrorMapper.mapSignUpError(exception)
        assertEquals(
            AuthErrorResult.FieldError(AuthField.EMAIL, AuthErrorMapper.INVALID_EMAIL_MSG),
            result
        )
    }

    @Test
    fun `mapSignUpError maps too many requests to general error`() {
        val exception = FirebaseTooManyRequestsException("Too many requests")
        val result = AuthErrorMapper.mapSignUpError(exception)
        assertEquals(
            AuthErrorResult.GeneralError(AuthErrorMapper.TOO_MANY_REQUESTS_MSG),
            result
        )
    }
}
