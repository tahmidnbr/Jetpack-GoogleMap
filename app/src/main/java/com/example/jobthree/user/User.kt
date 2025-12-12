package com.example.jobthree.user

data class User(
    val uid: String = "",
    val email: String = "",
    val username: String? = null,
    val avatarUrl: String? = null,
    val lat: Double? = null,
    val lng: Double? = null
)
