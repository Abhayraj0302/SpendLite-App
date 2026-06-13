package com.example.spendlite.AppUI

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendlite.ViewModelFiles.ViewModelApp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun navigation() {
    val currentuser = FirebaseAuth.getInstance().currentUser

    val startdestination =
        if (currentuser != null) "home"
        else "SignInPage"              //------change to "SignInPage" for normal auth to begin


    val navController = rememberNavController()
    val vm: ViewModelApp = viewModel()
    NavHost(
        navController = navController,
        startDestination = startdestination

    ) {
        composable(
            "home",
            enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) }

        ) {
            MainScreen(navController = navController, vm = vm)
        }
        composable(
            "add",
            enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) }
        ) {
            AddScreen(vm = vm, navController = navController)
        }

        composable(
            "SignInPage",
            enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) }

        ) {
            SignInPage(navController = navController)
        }

        composable(
            "SignUpScreen",
            enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) }
        ) {
            signUppage(navController = navController)
        }
    }

}