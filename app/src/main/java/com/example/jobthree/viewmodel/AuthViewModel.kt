package com.example.jobthree.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.jobthree.repository.FirebaseRepo
import com.example.jobthree.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(
    private val repo: FirebaseRepo
): ViewModel() {
    private val _allUser = MutableStateFlow<List<User>>(emptyList())
    val allUser: StateFlow<List<User>> = _allUser

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun signup(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        repo.signupUser(email, password,
            onSuccess = { onComplete(true, null) },
            onError = { onComplete(false, it) }
        )
    }

    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        repo.loginUser(email, password,
            onSuccess = { onComplete(true, null) },
            onError = { onComplete(false, it) }
        )
    }

    fun updateLocation(lat: Double, lng: Double) {
        repo.getCurrentUserUid()?.let { uid ->
            repo.storeUserLocation(uid, lat, lng)
        }
    }

    fun fetchAllUsers() {
        repo.getAllUsers { users ->
            _allUser.value = users
            val uid = repo.getCurrentUserUid()
            _currentUser.value = users.find { it.uid == uid }
        }
    }

    fun selectUser(user: User) {
        _selectedUser.value = user
    }

    fun updateUser(username: String, onComplete: (Boolean, String?) -> Unit) {
        val uid = repo.getCurrentUserUid()

        if (uid == null) {
            onComplete(false, "User not logged in")
            return
        }

        repo.updateUserProfile(
            uid = uid,
            username = username,
            onSuccess = {
                fetchAllUsers() // refresh user
                onComplete(true, null)
            },
            onError = { error ->
                onComplete(false, error)
            }
        )
    }


}