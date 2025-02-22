package com.example.echo.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.echo.model.BottomNavItem
import com.example.echo.navigation.Routes


@Composable
fun BottomNav(navController: NavHostController) {

    val navController1 = rememberNavController()
    Scaffold(bottomBar = { MyBottomBar(navController1) }) { innerPadding ->
        NavHost(
            navController = navController1, startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Routes.Home.routes) {
                Home()
            }

            composable(route = Routes.AddThreads.routes) {
                AddThreads(navController)
            }

            composable(route = Routes.Notification.routes) {
                Notification()
            }

            composable(route = Routes.Splash.routes) {
                Splash(navController)
            }

            composable(route = Routes.Profile.routes) {
                Profile(navController)
            }

            composable(route = Routes.Search.routes) {
                Search()
            }
        }


    }


}

@Composable
fun MyBottomBar(navController1: NavHostController) {

    val backStackEntry = navController1.currentBackStackEntryAsState()
    val list = listOf(
        BottomNavItem(
            title = "Home",
            route = Routes.Home.routes,
            icon = Icons.Default.Home
        ),

        BottomNavItem(
            title = "Search",
            route = Routes.Search.routes,
            icon = Icons.Default.Search
        ),


        BottomNavItem(
            title = "AddThread",
            route = Routes.AddThreads.routes,
            icon = Icons.Default.Add
        ),

        BottomNavItem(
            title = "Notification",
            route = Routes.Notification.routes,
            icon = Icons.Default.Notifications
        ),


        BottomNavItem(
            title = "Profile",
            route = Routes.Profile.routes,
            icon = Icons.Default.Person
        )
    )


    BottomAppBar {
        list.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route

            NavigationBarItem(selected = selected, onClick = {
                navController1.navigate(item.route) {
                    popUpTo(navController1.graph.findStartDestination().id) {
                        saveState = true;

                    }
                    launchSingleTop = true;
                }
            }, icon = {

                Icon(imageVector = item.icon, contentDescription = item.title)
            })

        }
    }
}
