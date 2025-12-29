package com.example.jobthree.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jobthree.R
import com.example.jobthree.user.User
import com.example.jobthree.viewmodel.AuthViewModel

@Composable
fun FriendList(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
){
    val users by authViewModel.allUser.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    val friends = users.filter { it.uid != currentUser?.uid }
    LaunchedEffect(Unit) {
        authViewModel.fetchAllUsers()                     // fetch users from Firebase
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xffeeeeee))
    ) {
        LazyColumn() {
            items(friends){ user ->
                FriendCard(navController=navController, user = user)
            }
        }
    }
}

//friends only not user wo logged
@Composable
fun FriendCard(
    navController: NavController,
    user: User
){
    var expended by remember { mutableStateOf(false) }
    val avatarPainter = rememberAsyncImagePainter(
        model = user.avatarUrl,
        placeholder = painterResource(R.drawable.ic_launcher_background), // your placeholder drawable
        error = painterResource(R.drawable.ic_launcher_background)        // fallback if loading fails
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3E2E2)
            )
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = avatarPainter,
                    contentDescription = user.username ?: "User Avatar",
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .size(60.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        user.username?.toUpperCase() ?: user.email,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(user.email, color = Color.Black, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        expended = !expended
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),

                ) {
                    Icon(
                        imageVector = if (!expended) {Icons.Default.KeyboardArrowDown} else Icons.Default.KeyboardArrowUp ,
                        contentDescription = "viewMap",
                        tint = Color(0xff212121)
                    )
                }
            }
        }

        if (expended){
            FriendCardExtra(navController = navController, user = user)
        }
    }

}


//extra composable to be shown when button expend
@Composable
fun FriendCardExtra(
    navController: NavController,
    user: User
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //show map button designs
        Button(
            onClick = {
                navController.navigate("map/${user.uid}/${user.username}")
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
        ) {
            //show location button
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "show-location",
                    tint = Color(0xff212121)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Show in map",
                    color = Color(0xff212121),
                    fontSize = 12.sp
                )
            }
        }

        Button(
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
        ) {
            //sent message button
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "show-location",
                    tint = Color(0xff212121)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Send a message",
                    color = Color(0xff212121),
                    fontSize = 12.sp
                )
            }
        }
    }
}