package com.example.spendlite.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendlite.data.remote.FirebaseService
import com.example.spendlite.features.addexpense.AddExpenseScreen
import com.example.spendlite.features.auth.signin.SignInScreen
import com.example.spendlite.features.auth.signup.SignUpScreen
import com.example.spendlite.features.home.HomeScreen
import com.example.spendlite.ui.theme.AppBackground

@Composable
fun AppNavigation() {
    val currentuser = FirebaseService.currentUser()

    val startdestination =
        if (currentuser != null) NavRoutes.HOME
        else NavRoutes.SIGN_IN
    val navController = rememberNavController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        NavHost(
            navController = navController,
            startDestination = startdestination,
            enterTransition = { NavTransitions.slideInRight },
            exitTransition = { NavTransitions.slideOutLeft },
            popEnterTransition = { NavTransitions.slideInLeft },
            popExitTransition = { NavTransitions.slideOutRight }
        ) {
            composable(
                route = NavRoutes.HOME,
                enterTransition = {
                    if (initialState.destination.route == NavRoutes.SIGN_IN || initialState.destination.route == NavRoutes.SIGN_UP) {
                        NavTransitions.fadeThroughIn
                    } else if (initialState.destination.route == NavRoutes.ADD) {
                        NavTransitions.fadeThroughIn
                    } else {
                        NavTransitions.slideInRight
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == NavRoutes.SIGN_IN || targetState.destination.route == NavRoutes.ADD) {
                        NavTransitions.fadeThroughOut
                    } else {
                        NavTransitions.slideOutLeft
                    }
                }
            ) {
                HomeScreen(navController = navController)
            }
            composable(
                route = NavRoutes.ADD,
                enterTransition = { NavTransitions.slideInBottom },
                exitTransition = { NavTransitions.slideOutBottom },
                popExitTransition = { NavTransitions.slideOutBottom }
            ) {
                AddExpenseScreen(navController = navController)
            }
            composable(
                route = NavRoutes.SIGN_IN,
                enterTransition = {
                    if (initialState.destination.route == NavRoutes.HOME) {
                        NavTransitions.fadeThroughIn
                    } else {
                        NavTransitions.slideInRight
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == NavRoutes.HOME) {
                        NavTransitions.fadeThroughOut
                    } else {
                        NavTransitions.slideOutLeft
                    }
                }
            ) {
                SignInScreen(navController = navController)
            }
            composable(
                route = NavRoutes.SIGN_UP,
                exitTransition = {
                    if (targetState.destination.route == NavRoutes.HOME) {
                        NavTransitions.fadeThroughOut
                    } else {
                        NavTransitions.slideOutLeft
                    }
                }
            ) {
                SignUpScreen(navController = navController)
            }
        }
    }
}
