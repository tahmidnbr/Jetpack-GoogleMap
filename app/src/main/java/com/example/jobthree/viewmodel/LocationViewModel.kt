package com.example.jobthree.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.jobthree.repository.FirebaseRepo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationViewModel(
    application: Application,
    private val repo: FirebaseRepo
): AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    val userLat = mutableStateOf<Double?>(null)
    val userLng = mutableStateOf<Double?>(null)

    val  hasLocationPermission = mutableStateOf(false)

    fun updatePermission(granted: Boolean){
        hasLocationPermission.value = granted
        if (granted) fetchCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocation() {
        if (!hasLocationPermission.value) return

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    userLat.value = it.latitude
                    userLng.value = it.longitude

                    repo.getCurrentUserUid()?.let { uid ->
                        repo.storeUserLocation(uid, it.latitude, it.longitude)
                    }
                }
            }
    }

    fun fetchUserLocation(uid: String, onResult: (lat: Double?, lng: Double?) -> Unit) {
        repo.getUserLocation(uid) { lat, lng ->
            onResult(lat, lng)
        }
    }

}