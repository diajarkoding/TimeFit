package com.diajarkoding.timefit.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.diajarkoding.timefit.presentation.screens.detail.ScheduleDetailScreen
import com.diajarkoding.timefit.presentation.screens.detail.ScheduleDetailViewModel
import com.diajarkoding.timefit.presentation.screens.home.HomeScreen
import com.diajarkoding.timefit.presentation.screens.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Detail : Screen("detail/{scheduleId}") {
        fun createRoute(scheduleId: Int) = "detail/$scheduleId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ){
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(
            Screen.Detail.route,
            arguments = listOf(
                navArgument("scheduleId") {
                    type = NavType.IntType
                }
            )
        ) {
            ScheduleDetailScreen(
                navController = navController
            )
        }
    }
}