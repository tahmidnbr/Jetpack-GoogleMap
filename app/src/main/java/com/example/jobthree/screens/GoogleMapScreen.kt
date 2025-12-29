package com.example.jobthree.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.jobthree.viewmodel.AuthViewModel
import com.example.jobthree.viewmodel.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(
    locationViewModel: LocationViewModel,
    friendUid: String,
    friendName: String
) {
    val myLat = locationViewModel.userLat.value
    val myLng = locationViewModel.userLng.value

    var friendLat by remember { mutableStateOf<Double?>(null) }
    var friendLng by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(friendUid) {
        locationViewModel.fetchUserLocation(friendUid) { lat, lng ->
            friendLat = lat
            friendLng = lng
        }
    }

    val cameraPositionState = rememberCameraPositionState()
    var animationDone by remember { mutableStateOf(false) }

    /** 🔥 IMMEDIATE positioning (NO animation) */
    LaunchedEffect(myLat, myLng) {
        if (myLat == null || myLng == null) return@LaunchedEffect

        cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(myLat, myLng),
                16f
            )
        )
    }

    /** 🎬 Animate to friend */
    LaunchedEffect(myLat, myLng, friendLat, friendLng) {
        if (
            animationDone ||
            myLat == null || myLng == null ||
            friendLat == null || friendLng == null
        ) return@LaunchedEffect

        animationDone = true

        val bounds = LatLngBounds.builder()
            .include(LatLng(myLat, myLng))
            .include(LatLng(friendLat!!, friendLng!!))
            .build()

        delay(600)

        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(bounds, 150),
            1000
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        if (myLat != null && myLng != null) {
            Marker(
                state = MarkerState(LatLng(myLat, myLng)),
                title = "You"
            )
        }

        if (friendLat != null && friendLng != null) {
            Marker(
                state = MarkerState(LatLng(friendLat!!, friendLng!!)),
                title = friendName
            )
        }

        if (
            myLat != null && myLng != null &&
            friendLat != null && friendLng != null
        ) {
            Polyline(
                points = listOf(
                    LatLng(myLat, myLng),
                    LatLng(friendLat!!, friendLng!!)
                ),
                color = Color.Blue,
                width = 6f
            )
        }
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun AllUsersMapScreen(
    authViewModel: AuthViewModel,
) {
    // Load all users
    LaunchedEffect(Unit) {
        authViewModel.fetchAllUsers()
    }

    val users by authViewModel.allUser.collectAsState()

    // Filter valid users
    val validUsers = users.filter {
        it.lat != null &&
                it.lng != null &&
                it.lat != 0.0 &&
                it.lng != 0.0 &&
                it.lat in -90.0..90.0 &&
                it.lng in -180.0..180.0
    }

    // Get active user
    val currentUid = FirebaseAuth.getInstance().currentUser?.uid
    val me = validUsers.find { it.uid == currentUid }

    // If active user location not yet available, show loader
    if (me == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Camera fixed at active user's location
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(me.lat!!, me.lng!!), 16f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {

        // Active user marker
        Marker(
            state = MarkerState(LatLng(me.lat!!, me.lng!!)),
            title = "You",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        )

        // Other users
        validUsers.filter { it.uid != currentUid }.forEach { user ->
            Marker(
                state = MarkerState(LatLng(user.lat!!, user.lng!!)),
                title = user.username ?: "User",
                snippet = user.email
            )
        }
    }
}

