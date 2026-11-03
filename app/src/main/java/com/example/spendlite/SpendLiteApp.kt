package com.example.spendlite

import android.app.Application
import com.example.spendlite.data.repository.ExpenseRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class SpendLiteApp : Application() {

    @Inject
    lateinit var repository: ExpenseRepository

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            auth.currentUser?.let { user ->
                appScope.launch {
                    repository.registerUser(user.uid, user.email ?: "")
                }
            }
        }
    }
}