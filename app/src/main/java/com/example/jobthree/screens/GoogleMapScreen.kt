package com.example.jobthree.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.jobthree.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun GMScreen(
    locationViewModel: LocationViewModel,
    uid: String,
    username: String
) {
    var lat by remember { mutableStateOf<Double?>(null) }
    var lng by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(uid) {
        locationViewModel.fetchUserLocation(uid) { userLat, userLng ->
            lat = userLat
            lng = userLng
        }
    }

    if (lat != null && lng != null) {
        val userLocation = LatLng(lat!!, lng!!)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(userLocation, 15f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = true,
                mapToolbarEnabled = true
            )
        ) {
            Marker(
                state = MarkerState(position = userLocation),
                title = username,
                snippet = "This is where $username is"
            )
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

