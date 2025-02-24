package com.example.echo.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.echo.R
import com.example.echo.item_view.ProfileItem
import com.example.echo.navigation.Routes
import com.example.echo.utils.SharedPrefs
import com.example.echo.utils.Utils
import com.example.echo.viewmodels.AuthViewModel
import com.example.echo.viewmodels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Profile(navController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)


    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Routes.Login.routes) {
                popUpTo(0) {
                    inclusive = true
                }
                //launchSingleTop = true
            }
        }
    }

    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }


    val profileViewModel: ProfileViewModel = viewModel()
    val threads by profileViewModel.threads.observeAsState(null)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {

        ConstraintLayout(modifier = Modifier.padding(horizontal = 16.dp)) {
            val (name, username, bio, topRow, following, followers, about) = createRefs()

            Row(modifier = Modifier
                .constrainAs(topRow) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(parent.start)
                }
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                GlideImage(
                    model = SharedPrefs.getImageUrl(context)
                        .ifEmpty { painterResource(R.drawable.person_icon) },
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = colorResource(R.color.main_color),
                            shape = CircleShape
                        )
                )

                OutlinedButton(
                    onClick = {
                        authViewModel.logout()
                    },
                    modifier = Modifier.align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.main_color),
                        contentColor = Color.White
                    ),

                    contentPadding = PaddingValues(horizontal = 48.dp)
                ) {

                    Text(
                        text = "Logout", style = TextStyle(
                            fontFamily = Utils().getFontFamily(),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                }
            }


            Text(
                text = SharedPrefs.getName(context), modifier = Modifier.constrainAs(name) {
                    top.linkTo(topRow.bottom)
                    start.linkTo(parent.start)
                    //end.linkTo(topRow.end)
                }, style = TextStyle(
                    fontFamily = Utils().getFontFamily(),
                    color = Color.Black,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,

                    ), maxLines = 1
            )

            Text(
                text = "@${SharedPrefs.getUserName(context)}",
                modifier = Modifier.constrainAs(username) {
                    top.linkTo(name.bottom)
                    start.linkTo(name.start)
                },
                style = TextStyle(
                    fontFamily = Utils().getFontFamily(),
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic

                ),
                maxLines = 1
            )


            Text(

                text = "Followers: 0",
                modifier = Modifier.constrainAs(followers) {
                    top.linkTo(username.bottom, margin = 8.dp)
                    start.linkTo(username.start)
                },
                style = TextStyle(
                    fontFamily = Utils().getFontFamily(),
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,

                    ),
            )

            Text(

                text = "Following: 0",
                modifier = Modifier.constrainAs(following) {
                    top.linkTo(followers.top)
                    start.linkTo(followers.end)
                    end.linkTo(parent.end)
                    bottom.linkTo(followers.bottom)
                },
                style = TextStyle(
                    fontFamily = Utils().getFontFamily(),
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,

                    ),
            )

            Text(

                text = "Your Bio:",
                modifier = Modifier.constrainAs(about) {
                    top.linkTo(followers.bottom, margin = 12.dp)
                    start.linkTo(followers.start)
                },
                style = TextStyle(
                    fontFamily = Utils().getFontFamily(),
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,

                    ),
            )

            // val bioText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +

            Column(modifier = Modifier.constrainAs(bio) {
                top.linkTo(about.bottom, margin = 8.dp)
                start.linkTo(about.start)
            }) {
                Box(
                    modifier = Modifier
                        .heightIn(max = if (!isExpanded) 40.dp else 90.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = SharedPrefs.getBio(context),
                        style = TextStyle(
                            fontFamily = Utils().getFontFamily(),
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        maxLines = if (!isExpanded) 3 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (SharedPrefs.getBio(context).length > 100) {
                    Text(text = if (isExpanded) "Read Less" else "Read More",
                        color = colorResource(R.color.main_color),
                        fontWeight = FontWeight.Normal,
                        fontFamily = Utils().getFontFamily(),
                        modifier = Modifier.clickable { isExpanded = !isExpanded })
                }

            }
        }


        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.Blue.copy(alpha = .4f))
        Spacer(modifier = Modifier.height(8.dp))

        if (threads?.isEmpty() == false) {

            Text(

                text = "Your Threads:", style = TextStyle(
                    fontFamily = Utils().getFontFamily(),
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                ), modifier = Modifier.padding(top = 8.dp, start = 16.dp)
            )

            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                items(threads ?: emptyList()) { thread ->

                    thread?.let {
                        FirebaseAuth.getInstance().currentUser?.uid?.let { it1 ->
                            ProfileItem(
                                thread = it,
                                navController = navController,
                                userId = it1
                            )
                        }
                    }

                }
            }
        }


    }


}


//@Preview(showBackground = true)
//@Composable
//fun ProfilePreview() {
//   // Profile()
//}

