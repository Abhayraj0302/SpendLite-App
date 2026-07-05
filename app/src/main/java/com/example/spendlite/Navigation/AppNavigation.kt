package com.example.spendlite.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendlite.data.remote.FirebaseService
import com.example.spendlite.features.addexpense.AddExpenseScreen
import com.example.spendlite.features.auth.signin.SignInScreen
import com.example.spendlite.features.auth.signup.SignUpScreen
import com.example.spendlite.features.home.HomeScreen

@Composable
fun AppNavigation() {
    val currentuser = FirebaseService.currentUser()

    val startdestination =
        if (currentuser != null) NavRoutes.HOME
        else NavRoutes.SIGN_IN              //------change to NavRoutes.SIGN_IN for normal auth to begin

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startdestination
    ) {
        composable(
            NavRoutes.HOME,
            enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) }
        ) {
            HomeScreen(navController = navController)
        }
        composable(
            NavRoutes.ADD,
            enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) }
        ) {
            AddExpenseScreen(navController = navController)
        }
        composable(
            NavRoutes.SIGN_IN,
            enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) }
        ) {
            SignInScreen(navController = navController)
        }
        composable(
            NavRoutes.SIGN_UP,
            enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) }
        ) {
            SignUpScreen(navController = navController)
        }
    }
}