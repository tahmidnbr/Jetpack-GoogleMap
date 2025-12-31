package com.example.jobthree.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobthree.viewmodel.AuthViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passError by remember { mutableStateOf<String?>(null) }

    var passVisible by remember { mutableStateOf(false) }

    // --- LIVE VALIDATION ---
    fun validateEmail(value: String) {
        emailError = when {
            value.isBlank() -> "Email cannot be empty"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() ->
                "Invalid email format"
            else -> null
        }
    }

    fun validatePassword(value: String) {
        passError = when {
            value.isBlank() -> "Password cannot be empty"
            value.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // EMAIL
        OutlinedTextField(
            value = email,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email"
                )
            },
            onValueChange = {
                email = it
                validateEmail(it)      // LIVE validation here
            },
            label = { Text("Email", color = Color(0xff212121)) },
            isError = emailError != null,    // triggers red border
            supportingText = {
                emailError?.let { Text(it, color = Color.Red) }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        // PASSWORD
        OutlinedTextField(
            value = pass,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password"
                )
            },
            trailingIcon = {
                IconButton(onClick = { passVisible = !passVisible }) {
                    Icon(
                        imageVector = if (passVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = {
                pass = it
                validatePassword(it)     // LIVE validation here
            },
            label = { Text("Password", color = Color(0xff212121)) },
            isError = passError != null,   // triggers red border
            supportingText = {
                passError?.let { Text(it, color = Color.Red) }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Button(
            onClick = {
                // Final check before login
                validateEmail(email)
                validatePassword(pass)

                if (emailError == null && passError == null) {
                    authViewModel.login(email, pass) { success, error ->
                        if (success) {
                            navController.navigate("main")
                        } else {
                            Log.d("LoginScreen", "Login failed: $error")
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xff212121),
                contentColor = Color(0xffeeeeee)
            )
        ) {
            Text("Login")
        }
    }
}
