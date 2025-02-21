package com.example.echo.navigation

sealed class Routes(val routes: String) {
    object Home : Routes("home")
    object AddThreads : Routes("addThreads")
    object Notification : Routes("notification")
    object Search : Routes("search")
    object Profile : Routes("profile")
    object Splash : Routes("splash")
    object BottomNav : Routes("bottom_nav")
    object Login : Routes("login")
    object Register : Routes("register")
}