package com.example.jobthree.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.MapStyleOptions
import com.example.jobthree.R
import com.example.jobthree.viewmodel.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
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
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(myLat, myLng))
                    .zoom(17f)
                    .tilt(60f)      // 👈 3D angle
                    .bearing(0f)
                    .build()
            )
        )
    }

    /** 🎬 Animate to friend (KEEP 3D) */
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

        // Fit both points (this WILL go 2D)
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(bounds, 150),
            900
        )

        // Re-apply 3D camera after bounds animation
        delay(950)

        cameraPositionState.animate(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(friendLat!!, friendLng!!))
                    .zoom(17f)
                    .tilt(60f)      // 👈 back to 3D
                    .bearing(30f)
                    .build()
            ),
            800
        )
    }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(
            isBuildingEnabled = true,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                LocalContext.current,
                R.raw.exported_style
            )
        ),
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
    }
}

