package com.example.jobthree.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobthree.viewmodel.AuthViewModel

@Preview(showSystemUi = true)
@Composable
fun RegScreen(
    modifier: Modifier  = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
){
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var con by  remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(Color(0xffeeeeee))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text("Email",
                        color = Color(0xff212121)
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(18.dp))
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = {
                    Text("Password",
                        color = Color(0xff212121)
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(18.dp))
            OutlinedTextField(
                value = con,
                onValueChange = { con = it },
                label = {
                    Text("Confirm Password",
                        color = Color(0xff212121)
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = {
                    authViewModel.signup(email, pass){success, error ->
                        if (success){
                            navController.navigate("main")
                        } else {
                            Log.d("RegSreen", "Reg failed: $error")
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
                Text(
                    "Register",
                )
            }
            Spacer(modifier = Modifier.height(18.dp))

        }
    }
}