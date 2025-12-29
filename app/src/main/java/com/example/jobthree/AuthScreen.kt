package com.example.jobthree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jobthree.screens.LoginScreen
import com.example.jobthree.screens.RegScreen
import com.example.jobthree.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
){
    var clicked by remember { mutableStateOf(false) }
    val top =  if (clicked) "Good to see you!" else "Welcome"
    val down = if (clicked) "Don't have any account!" else "Already have an account!"

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(top, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Column() {
            when(clicked){
                true -> LoginScreen(navController = navController, authViewModel = authViewModel)
                false -> RegScreen(navController = navController, authViewModel = authViewModel)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(down,
            fontSize = 15.sp,
            modifier = Modifier.clickable(onClick = {clicked = !clicked}
            )
        )
    }
}