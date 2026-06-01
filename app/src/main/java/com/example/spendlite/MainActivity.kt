package com.example.spendlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendlite.AppUI.AddScreen
import com.example.spendlite.AppUI.MainScreen
import com.example.spendlite.AppUI.SignInPage
import com.example.spendlite.AppUI.signUppage
import com.example.spendlite.ViewModelFiles.ViewModelApp
import com.example.spendlite.ui.theme.SpendLiteTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContent {
            SpendLiteTheme {
                val currentuser = FirebaseAuth.getInstance().currentUser

                val startdestination =
                    if(currentuser != null) "home"
                    else "SignInPage"

                val navController = rememberNavController()
                val vm: ViewModelApp = viewModel()
                NavHost(
                    navController = navController,
                    startDestination = "SignInPage"

                ) {
                    composable(
                        "home", exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) },
                        enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) }

                    ) {
                        MainScreen(navController = navController, vm = vm)
                    }
                    composable(
                        "add",
                        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                    ) {
                        AddScreen(vm = vm, navController = navController)
                    }

                    composable(
                        "SignInPage", exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) },
                        enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) }

                    ) {
                        SignInPage(navController = navController)
                    }

                    composable(
                        "SignUpScreen",
                        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                    ) {
                        signUppage(navController = navController)
                    }
                }
            }
        }
    }
}