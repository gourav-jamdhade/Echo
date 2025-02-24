package com.example.echo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.echo.R
import com.example.echo.item_view.UserItem
import com.example.echo.utils.Utils
import com.example.echo.viewmodels.SearchViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Search(navController: NavHostController) {

    val searchViewModel: SearchViewModel = viewModel()
    val users by searchViewModel.usersList.observeAsState(null)

    val currentUser = FirebaseAuth.getInstance().currentUser

    var search by remember {
        mutableStateOf("")
    }

    Column() {

        Text(
            text = "Search", style = TextStyle(
                fontFamily = Utils().getFontFamily(),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 24.sp,
            ), modifier = Modifier.padding(top = 16.dp, start = 16.dp)
        )

        OutlinedTextField(value = search,
            onValueChange = {
                search = it
            },
            label = {
                Text("Search User")
            },
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Utils().getFontFamily(),
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(
                    R.color.main_color
                ),
                unfocusedBorderColor = colorResource(
                    R.color.main_color
                ),
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = colorResource(
                        R.color.main_color
                    )
                )
            }

        )

        Spacer(modifier = Modifier.height(12.dp))
        


        LazyColumn(
        ) {

            val filterItems = users?.filter {
                it.name.contains(search, ignoreCase = true)
            }
            items(filterItems ?: emptyList()) { user ->

                if (user.userId != currentUser?.uid) {
                    UserItem(
                        user = user,
                        navController = navController,
                    )
                }


            }

        }
    }
}