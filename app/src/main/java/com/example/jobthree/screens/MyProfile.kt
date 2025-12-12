package com.example.jobthree.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jobthree.R
import com.example.jobthree.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfile(
    navController: NavController,
    authViewModel: AuthViewModel
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()   // <-- Go back
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {innerPadding ->
        Profile(modifier = Modifier.padding(innerPadding), authViewModel = authViewModel)
    }
}


@Composable
fun Profile(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
){
    val context = LocalContext.current
    val currentUser by authViewModel.currentUser.collectAsState()

    var username by remember { mutableStateOf(currentUser?.username ?: "") }

    val avatarPainter = rememberAsyncImagePainter(
        model = currentUser?.avatarUrl,
        placeholder = painterResource(R.drawable.ic_launcher_background), // your placeholder drawable
        error = painterResource(R.drawable.ic_launcher_background)        // fallback if loading fails
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = avatarPainter,
                contentDescription = null,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    )
                    .size(100.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = {username = it},
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        "Username"
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))
            OutlinedTextField(
                value = currentUser?.email ?: "user@mail.com",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        "Email"
                    )
                },
                enabled = false,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = {
                    authViewModel.updateUser(
                        username = username,
                    ) { success, error ->
                        if (success) {
                            Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, error ?: "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Profile")
            }

            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = {},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff212121)
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Show Map")
            }
        }
    }
}