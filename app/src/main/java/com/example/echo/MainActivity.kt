package com.example.echo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.echo.navigation.NavGraph
import com.example.echo.ui.theme.EchoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                resources.getColor(R.color.main_color),
                resources.getColor(R.color.main_color)
            )
        )
        setContent {
            EchoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}



