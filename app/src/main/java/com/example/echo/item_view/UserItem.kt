package com.example.echo.item_view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.echo.model.UserModel
import com.example.echo.navigation.Routes
import com.example.echo.utils.Utils


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserItem(
    user: UserModel,
    navController: NavHostController
) {

    Column() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {

                }
        ) {
            val (userImage, userName, name) = createRefs()

            Log.d("Image Thread Item", user.imageUrl.toString())
            GlideImage(
                model = user.imageUrl.toString(),
                contentDescription = "profilePic",
                modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate(Routes.Profile.routes) {
                            popUpTo(Routes.AddThreads.routes) {
                                inclusive = true
                            }
                            launchSingleTop = true

                        }
                    },
                contentScale = ContentScale.Crop,
            )

            Text(text = user.name, style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Utils().getFontFamily(),
                textAlign = TextAlign.Center,
            ), modifier = Modifier.constrainAs(name) {
                top.linkTo(userImage.top)
                start.linkTo(userImage.end, margin = 4.dp)
            })

            Text(text = "@${user.userName}", style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                fontFamily = Utils().getFontFamily(),
            ), modifier = Modifier.constrainAs(userName) {
                top.linkTo(name.bottom, margin = 2.dp)
                start.linkTo(name.start)
            })
        }
    }

    HorizontalDivider(color = Color.Black.copy(0.4f), thickness = 1.dp)
}

//@Preview(showBackground = true)
//@Composable
//fun UserItemPreview() {
//    //UserItem()
//}
