package com.example.jobthree

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

@Preview(showBackground = true)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    locationViewModel: LocationViewModel
){
    Scaffold(
        topBar = {
            TopF(authViewModel = authViewModel, navController = navController)
        }
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

