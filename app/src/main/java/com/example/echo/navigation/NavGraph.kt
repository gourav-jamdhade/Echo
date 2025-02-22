package com.example.echo.navigation

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.echo.screens.AddThreads
import com.example.echo.screens.BottomNav
import com.example.echo.screens.Home
import com.example.echo.screens.Login
import com.example.echo.screens.Notification
import com.example.echo.screens.Profile
import com.example.echo.screens.Register
import com.example.echo.screens.Search
import com.example.echo.screens.Splash


@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.Splash.routes) {
        composable(Routes.Splash.routes) {
            Splash(navController)
        }

        composable(Routes.AddThreads.routes) {
            AddThreads(navController)
        }

        composable(Routes.Notification.routes) {
            Notification()
        }

        composable(Routes.Home.routes) {
            Home()
        }

        composable(Routes.Profile.routes) {
            Profile(navController)
        }

        composable(Routes.Search.routes) {
            Search()
        }

        composable(Routes.BottomNav.routes) {
            BottomNav(navController)
        }

        composable(Routes.Login.routes) {
            Login(navController)
        }

        composable(Routes.Register.routes) {
            Register(navController)
        }


    }

}