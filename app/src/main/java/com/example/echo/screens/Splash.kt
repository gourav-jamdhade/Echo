package com.example.echo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.echo.R
import com.example.echo.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun Splash(navController: NavHostController) {


    ConstraintLayout(modifier = Modifier.size(50.dp)) {

        val (image) = createRefs()
        Image(
            painter = painterResource(R.drawable.threads_logo),
            contentDescription = "logo",
            modifier = Modifier
                .constrainAs(image) {

                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(100.dp)
        )
    }

    LaunchedEffect(true) {
        delay(3000)

        if (FirebaseAuth.getInstance().currentUser != null) {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId){
                    inclusive =true
                }
                launchSingleTop = true
            }

        } else {
            navController.navigate(Routes.Login.routes) {
                popUpTo(navController.graph.startDestinationId){
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

    }


}