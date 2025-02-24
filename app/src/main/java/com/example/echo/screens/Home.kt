package com.example.echo.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.echo.R
import com.example.echo.item_view.ThreadItem
import com.example.echo.utils.Utils
import com.example.echo.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Home(navController: NavHostController) {

    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()
    val threadAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)


    Column() {

        Row {
            Image(
                painter = painterResource(R.drawable.threads_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(60.dp).padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = "Home", style = TextStyle(
                    fontFamily = Utils().getFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 24.sp,
                ), modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            items(threadAndUsers ?: emptyList()) { pairs ->

                ThreadItem(
                    thread = pairs.first,
                    user = pairs.second,
                    navController = navController,
                    userId = FirebaseAuth.getInstance().currentUser!!.uid
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    //  Home()
}