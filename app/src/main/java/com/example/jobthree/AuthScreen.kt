package com.example.jobthree

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val tabs = listOf("Login", "Register")
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color(0xff212121))
        ) {
            //Spacer(modifier = Modifier.height(20.dp))
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xff212121)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            when(selectedTab){
                0 -> LoginScreen(navController =  navController, authViewModel = authViewModel)
                1 -> RegScreen(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}