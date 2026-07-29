package com.example.spendlite.data.remote

import com.example.spendlite.data.model.Expense
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseService {

    private val db = FirebaseFirestore.getInstance()

    fun currentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    fun signIn(
        email: String,
        password: String
    ): Task<AuthResult> {
        return Firebase.auth
            .signInWithEmailAndPassword(email, password)
    }

    fun signUp(
        email: String,
        password: String
    ): Task<AuthResult> {
        return Firebase.auth
            .createUserWithEmailAndPassword(email, password)
    }

    fun signInWithGoogle(
        idToken: String
    ): Task<AuthResult> {

        val credential =
            GoogleAuthProvider.getCredential(idToken, null)

        return Firebase.auth
            .signInWithCredential(credential)
    }

    fun signOut() {
        Firebase.auth.signOut()
    }

    private fun expensesCollection(uid: String) =
        db.collection("users")
            .document(uid)
            .collection("expenses")

    suspend fun addExpense(
        uid: String,
        expense: Expense
    ) {
        expensesCollection(uid)
            .document(expense.id)
            .set(expense)
            .await()
    }

    suspend fun getAllExpenses(
        uid: String
    ): List<Expense> {

        val snapshot =
            expensesCollection(uid)
                .get()
                .await()

        return snapshot.toObjects(
            Expense::class.java
        )
    }

    suspend fun deleteExpense(
        uid: String,
        expenseId: String
    ) {
        expensesCollection(uid)
            .document(expenseId)
            .delete()
            .await()
    }
}