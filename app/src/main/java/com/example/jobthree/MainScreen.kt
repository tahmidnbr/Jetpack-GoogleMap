package com.example.jobthree

import android.Manifest
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jobthree.screens.FriendList
import com.example.jobthree.viewmodel.AuthViewModel
import com.example.jobthree.viewmodel.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth


@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    locationViewModel: LocationViewModel
){
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopF(authViewModel = authViewModel, navController = navController)
        },

        floatingActionButton = {
            FabMenu(navController=navController)
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        FriendList(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            authViewModel = authViewModel
        )
    }
    // Ask for location permission
    RequestLocation(locationViewModel = locationViewModel)
}


//user who logged
@Composable
fun TopF(authViewModel: AuthViewModel, navController: NavController){
    val currentUser by authViewModel.currentUser.collectAsState()

    val avatarPainter = rememberAsyncImagePainter(
        model = currentUser?.avatarUrl,
        placeholder = painterResource(R.drawable.ic_launcher_background), // your placeholder drawable
        error = painterResource(R.drawable.ic_launcher_background)        // fallback if loading fails
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xff212121))
            .statusBarsPadding()
        ,
        horizontalArrangement = Arrangement.End,

    ) {
        Box(
            modifier = Modifier
                .padding(12.dp)
                .clickable(
                    onClick = { navController.navigate("profile") }
                )
        ){
            Image(
                painter = avatarPainter,
                contentDescription = "profile",
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = CircleShape)
            )
        }
    }
}

@Composable
fun FabMenu(
    navController: NavController
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid ?: ""

    var expanded by remember { mutableStateOf(false) }

    val distance = 72.dp

    val topOffset by animateDpAsState(
        targetValue = if (expanded) -distance else 0.dp,
        label = "top"
    )

    val leftOffset by animateDpAsState(
        targetValue = if (expanded) -distance else 0.dp,
        label = "left"
    )

    val rightOffset by animateDpAsState(
        targetValue = if (expanded) distance else 0.dp,
        label = "right"
    )

    val alpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        label = "alpha"
    )

    Box(
        contentAlignment = Alignment.Center
    ) {

//        SmallFloatingActionButton(
//            onClick = {
//                /* Map */
//                navController.navigate("map")
//            },
//            modifier = Modifier
//                .offset(y = topOffset)
//                .alpha(alpha)
//        ) {
//            Icon(Icons.Default.LocationOn, null)
//        }

        SmallFloatingActionButton(
            onClick = { /* Edit */
                navController.navigate("map")
            },
            containerColor = Color(0xff212121),
            contentColor = Color.White,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .offset(x = leftOffset)
                .alpha(alpha)
        ) {
            Icon(Icons.Default.LocationOn, null)
        }

        SmallFloatingActionButton(
            onClick = { /* Logout */
                FirebaseAuth.getInstance().signOut()
                navController.navigate("auth") {
                    popUpTo("main") { inclusive = true }
                }
            },
            containerColor = Color(0xff212121),
            contentColor = Color.White,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .offset(x = rightOffset)
                .alpha(alpha)
        ) {
            Icon(Icons.Default.ExitToApp, null)
        }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = Color(0xff212121),
            contentColor = Color.White,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                if (expanded) Icons.Default.MenuOpen else Icons.Default.Menu,
                null
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocation(locationViewModel: LocationViewModel) {
    val permissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // Automatically launch permission request when the composable appears
    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        } else {
            locationViewModel.updatePermission(true)
        }
    }

    // When permission is granted, notify ViewModel
    LaunchedEffect(permissionState.status.isGranted) {
        if (permissionState.status.isGranted) {
            locationViewModel.updatePermission(true)
        }
    }
}

