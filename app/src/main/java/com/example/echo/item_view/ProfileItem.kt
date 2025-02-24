package com.example.echo.item_view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.echo.model.ThreadModel
import com.example.echo.utils.SharedPrefs
import com.example.echo.utils.Utils

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileItem(
    thread: ThreadModel,
    navController: NavHostController,
    userId: String
) {

    val context = LocalContext.current

    Column() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (userImage, userName, threadImage, threadText) = createRefs()

            // Log.d("Image Thread Item", user.imageUrl.toString())
            GlideImage(
                model = SharedPrefs.getImageUrl(context = context),
                contentDescription = "profilePic",
                modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(32.dp)
                    .clip(CircleShape),
//                    .clickable {
//                        navController.navigate(Routes.Profile.routes) {
//                            launchSingleTop = true
//
//                        }
//                    },
                contentScale = ContentScale.Crop,
            )

            Log.d("Name", SharedPrefs.getName(context))
            Text(text = SharedPrefs.getName(context), style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Utils().getFontFamily(),
                textAlign = TextAlign.Center,
            ), modifier = Modifier.constrainAs(userName) {
                top.linkTo(userImage.top)
                start.linkTo(userImage.end, margin = 16.dp)
                bottom.linkTo(userImage.bottom)
            })

            Text(text = thread.thread, style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Utils().getFontFamily(),
            ), modifier = Modifier.constrainAs(threadText) {
                top.linkTo(userImage.bottom, margin = 16.dp)
                start.linkTo(userImage.start, margin = 16.dp)
            })

            if (thread.imageUrl != "") {


                var showFullImage by remember { mutableStateOf(false) }

                if (showFullImage) {
                    Dialog(onDismissRequest = { showFullImage = false }) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            GlideImage(
                                model = thread.imageUrl.toString(),
                                contentDescription = "Full screen thread image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { showFullImage = false },
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .constrainAs(threadImage) {
                            top.linkTo(threadText.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }

                ) {
                    GlideImage(
                        model = thread.imageUrl.toString(),
                        contentDescription = "thread image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clickable {
                                showFullImage = true
                            },
                        contentScale = ContentScale.Crop
                    )
                }

            }
        }

        HorizontalDivider(color = Color.Black.copy(alpha = 0.5f), thickness = 1.dp)
    }


}