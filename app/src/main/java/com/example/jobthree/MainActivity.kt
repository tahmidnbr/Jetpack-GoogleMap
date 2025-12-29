package com.example.jobthree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jobthree.repository.FirebaseRepo
import com.example.jobthree.screens.AllUsersMapScreen
import com.example.jobthree.screens.LoginScreen
import com.example.jobthree.screens.MapScreen
import com.example.jobthree.screens.MyProfile
import com.example.jobthree.screens.RegScreen
import com.example.jobthree.ui.theme.JobThreeTheme
import com.example.jobthree.viewmodel.AuthViewModel
import com.example.jobthree.viewmodel.LocationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobThreeTheme {
                val  navController = rememberNavController()
                val authViewModel = remember { AuthViewModel(FirebaseRepo()) }
                val locationViewModel = remember { LocationViewModel(application = application, repo = FirebaseRepo()) }

                NavHost(
                    navController = navController,
                    startDestination = "auth"
                ){
                    composable("main") {
                        MainScreen(navController = navController, authViewModel, locationViewModel = locationViewModel)
                    }
                    composable("auth") {
                        AuthScreen(navController  = navController, authViewModel = authViewModel)
                    }
                    composable("login") {
                        LoginScreen(navController = navController, authViewModel = authViewModel)
                    }
                    composable("reg") {
                        RegScreen(navController = navController, authViewModel = authViewModel)
                    }
                    composable(
                        "map/{uid}/{username}",
                        arguments = listOf(
                            navArgument("uid") { type = NavType.StringType },
                            navArgument("username") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val uid = backStackEntry.arguments?.getString("uid") ?: ""
                        val username = backStackEntry.arguments?.getString("username") ?: "Unknown"
                        MapScreen(locationViewModel = locationViewModel,  uid, username)
                    }

                    composable("profile") {
                        MyProfile(navController = navController,  authViewModel = authViewModel)
                    }
                    composable("map") {
                        AllUsersMapScreen(authViewModel=authViewModel)
                    }



                }
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    AuthScreen(
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
}
