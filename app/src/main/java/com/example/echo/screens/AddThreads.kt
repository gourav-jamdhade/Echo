@file:OptIn(ExperimentalFoundationApi::class)

package com.example.echo.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.echo.R
import com.example.echo.navigation.Routes
import com.example.echo.utils.SharedPrefs
import com.example.echo.utils.Utils
import com.example.echo.viewmodels.AddThreadViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddThreads(navController: NavHostController) {

    val threadViewModel: AddThreadViewModel = viewModel()
    val isPosted by threadViewModel.isPosted.observeAsState()
    var thread by remember { mutableStateOf("") }
    val context = LocalContext.current
    //for Attachments
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->

            imageUri = uri

        }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->

            if (isGranted) {

                launcher.launch("image/*")
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Anyone Can Reply") }


    LaunchedEffect(isPosted) {
        if (isPosted == true) {
            thread = ""
            imageUri = null
            Toast.makeText(context, "Echo post added successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.Home.routes) {
                popUpTo(Routes.AddThreads.routes) {
                    inclusive = true
                }
                launchSingleTop = true

            }
        }
    }
    //UI CODE
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        val (crossPic, addThreadText, profilePic, editText, attachMediaIcon, replyText, privacyButton, button, imageBox, nameText) = createRefs()

        Image(painter = painterResource(R.drawable.close),
            contentDescription = "close",
            modifier = Modifier
                .constrainAs(crossPic) {
                    top.linkTo(parent.top, margin = 24.dp)
                    start.linkTo(parent.start)
                }
                .size(16.dp)
                .clickable {
                    navController.navigate(Routes.Home.routes) {
                        popUpTo(Routes.AddThreads.routes) {
                            inclusive = true
                        }
                        launchSingleTop = true

                    }
                })

        Text(text = "Create An Echo Post", style = TextStyle(
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontFamily = Utils().getFontFamily()
        ), modifier = Modifier.constrainAs(addThreadText) {
            top.linkTo(parent.top, margin = 24.dp)
            start.linkTo(crossPic.end, margin = 16.dp)
            bottom.linkTo(crossPic.bottom)
        })

//        val response = URL("https://picsum.photos/200").readText()
//        Log.d("NetworkTest", "Connected: $response")

        GlideImage(
            model = SharedPrefs.getImageUrl(context).ifEmpty {
                painterResource(R.drawable.no_image)
            },
            contentDescription = "profilePic",
            modifier = Modifier
                .constrainAs(profilePic) {
                    top.linkTo(addThreadText.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                }
                .size(32.dp)
                .clip(CircleShape)
                .clickable {},
            contentScale = ContentScale.Crop,
        )

        Text(text = SharedPrefs.getName(context), style = TextStyle(
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Utils().getFontFamily(),
            textAlign = TextAlign.Center,
        ), modifier = Modifier.constrainAs(nameText) {
            top.linkTo(profilePic.top)
            bottom.linkTo(profilePic.bottom)
            start.linkTo(profilePic.end, margin = 16.dp)
        })


        BasicTextFieldWithHint(hint = "Express yourself...", value = thread, onValueChange = {
            thread = it

        }, modifier = Modifier.constrainAs(editText) {
            top.linkTo(nameText.bottom, margin = 24.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })


        if (imageUri == null) {
            Image(painter = painterResource(R.drawable.baseline_attach_file_24),
                contentDescription = "close",
                modifier = Modifier
                    .constrainAs(attachMediaIcon) {
                        top.linkTo(editText.bottom, margin = 24.dp)
                        start.linkTo(parent.start)
                    }
                    .size(24.dp)
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED

                        if (isGranted) {
                            launcher.launch(
                                "image/*"
                            )
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    },
                contentScale = ContentScale.Crop
            )
        } else {

            var showFullImage by remember { mutableStateOf(false) }
            if (showFullImage) {
                ImagePreviewDialog(imageUri = imageUri) {
                    showFullImage = false // Close dialog when clicked
                }
            }
            Box(modifier = Modifier
                .background(Color.Gray.copy(alpha = .4f), shape = RoundedCornerShape(10.dp))
                .padding(12.dp)
                .constrainAs(imageBox) {
                    top.linkTo(editText.bottom, margin = 24.dp)
                    start.linkTo(editText.start)
                    end.linkTo(parent.end)

                }
                .height(200.dp)
            ) {
                Image(
                    painter =
                    rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "profilePic",
                    modifier = Modifier
//                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clickable {
                            showFullImage = true
                        },
                    contentScale = ContentScale.Crop
                )

                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "remove the attachment",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 20.dp, y = (-20).dp)
                        .clickable {
                            imageUri = null
                        }
                )

            }
        }

        TextButton(onClick = {
            showSheet = true

        }, modifier = Modifier.constrainAs(privacyButton) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
        }) {
            Text(
                text = selectedOption,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    fontFamily = Utils().getFontFamily()
                )
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Reply options",
                tint = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(16.dp)

            )
        }




        TextButton(onClick = {

            if (thread.isEmpty() && imageUri == null) {
                Toast.makeText(context, "Can not post an empty post", Toast.LENGTH_SHORT).show()
            } else if (imageUri == null) {
                threadViewModel.saveThreadData(
                    thread = thread,
                    userId=FirebaseAuth.getInstance().currentUser!!.uid,
                    imageUrl = ""
                )
            } else {

                threadViewModel.saveImageToBack4App(context, imageUri!!) { imageUrl ->

                    threadViewModel.saveThreadData(
                        thread,
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        imageUrl
                    )
                }
            }
        }, modifier = Modifier
            .constrainAs(button) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
            .background(Color(0xFF095aa3), shape = RoundedCornerShape(10))) {
            Text(
                text = "POST",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    fontFamily = Utils().getFontFamily()
                )
            )


        }


    }


    if (showSheet) {
        ReplyPrivacyBottomSheet(
            sheetState = sheetState,
            onDismiss = { showSheet = false },
            onOptionSelected = {
                selectedOption = it
                showSheet = false
            },

            )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyPrivacyBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column {
            Text(
                text = "Who can reply?",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = Utils().getFontFamily()
                ),
                modifier = Modifier.padding(bottom = 12.dp, start = 12.dp)
            )

            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            //Option 1: Anyone Can Reply
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onOptionSelected("Anyone Can Reply")
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_groups_24),
                    contentDescription = "groups"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Anyone Can Reply",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontFamily = Utils().getFontFamily()
                    )
                )

            }

            //Option 2: Only Followers Can Reply
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onOptionSelected("Only Followers Can Reply")
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "groups"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Only Followers Can Reply ",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontFamily = Utils().getFontFamily()
                    )
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

@Composable
fun ImagePreviewDialog(imageUri: Uri?, onDismiss: () -> Unit) {
    if (imageUri != null) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Full Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clickable { onDismiss() }, // Close when clicking on the full-screen image
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}


@Composable
fun BasicTextFieldWithHint(
    hint: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .border(1.dp, Color.Black.copy(alpha = .7f), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {

        if (value.isEmpty()) {
            Text(
                text = hint, style = TextStyle(
                    color = Color.Gray, fontSize = 14.sp, fontFamily = Utils().getFontFamily()
                )
            )
        }

        Column {


            BasicTextField(
                value = value, onValueChange = {
                    if (it.length <= 500) {
                        onValueChange(it)
                        coroutineScope.launch {
                            scrollState.scrollTo(scrollState.maxValue)
                        }
                    }

                }, textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = Utils().getFontFamily(),
                    fontWeight = FontWeight.Light
                ), modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 150.dp)
                    .verticalScroll(scrollState)

            )

            Text(
                text = "${value.length}/500",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )

        }
    }

}


@Preview(showBackground = true)
@Composable
fun AddThreadsPreview() {
//    AddThreads()
}