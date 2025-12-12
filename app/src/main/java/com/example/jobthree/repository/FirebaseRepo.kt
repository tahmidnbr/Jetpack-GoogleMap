package com.example.jobthree.repository

import androidx.compose.runtime.Composable
import com.example.jobthree.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepo {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun  signupUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: ""
                val username = generateUsernameFromEmail(email)
                val avatar = generateDiceAvatar(username)
                val user = User(uid = uid, email = email,username=username, avatarUrl = avatar)

                db.collection("users").document(uid)
                    .set(user)
                    .addOnCompleteListener { onSuccess() }
                    .addOnFailureListener { e -> onError(e.message ?: "Firestore Error") }
            }
            .addOnFailureListener { e -> onError (e.message ?: "Auth error") }
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener{onSuccess()}
            .addOnFailureListener { e -> onError(e.message?: "Login failed") }
    }

    fun storeUserLocation(
        uid: String, lat: Double, lng: Double
    ){
        val  userRef = db.collection("users").document(uid)
        userRef.update("lat", lat, "lng", lng)
    }

    fun getCurrentUserUid(): String?  = auth.currentUser?.uid

    // repository/FirebaseRepository.kt
    fun getAllUsers(onComplete: (List<User>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val users = result.map { it.toObject(User::class.java) }
                onComplete(users)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }

    fun getUserLocation(uid: String, onResult: (lat: Double?, lng: Double?) -> Unit) {
        val docRef = db.collection("users").document(uid)
        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val lat = document.getDouble("lat")
                val lng = document.getDouble("lng")
                onResult(lat, lng)
            } else {
                onResult(null, null)
            }
        }.addOnFailureListener {
            onResult(null, null)
        }
    }

    fun updateUserProfile(
        uid: String,
        username: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val avatar = generateDiceAvatar(username)
        val userRef = db.collection("users").document(uid)

        userRef.update(
            mapOf(
                "username" to username,
                "avatarUrl" to avatar
            )
        )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to update user profile")
            }
    }




    private fun  generateUsernameFromEmail(email: String) = email.substringBefore("@")
    private fun generateDiceAvatar(username: String): String{
        val seed = username
        return "https://api.dicebear.com/6.x/initials/png?seed=$seed"
    }
}