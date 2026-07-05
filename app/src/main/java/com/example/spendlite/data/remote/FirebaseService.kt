package com.example.spendlite.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FirebaseService {
    fun currentUser(): FirebaseUser? = Firebase.auth.currentUser

    fun signIn(email: String, password: String): Task<AuthResult> =
        Firebase.auth.signInWithEmailAndPassword(email, password)

    fun signUp(email: String, password: String): Task<AuthResult> =
        Firebase.auth.createUserWithEmailAndPassword(email, password)

    fun signInWithGoogle(idToken: String): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return Firebase.auth.signInWithCredential(credential)
    }

    fun signOut() = Firebase.auth.signOut()
}