package com.example.jobthree.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jobthree.R

@Preview(showSystemUi = true)
@Composable
fun ChatScreen(){
    Scaffold(
        topBar = {
            ProfileInfo()
        }
    ){
        
    }
}

@Preview
@Composable
fun ProfileInfo(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xff212121))
            .statusBarsPadding()
        ,
        horizontalArrangement = Arrangement.Start,

        ) {
        Box(
            modifier = Modifier
                .padding(vertical = 12.dp)
        ){
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(12.dp)
                .clickable(
                    onClick = { /*navController.navigate("profile")*/ }
                )
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "profile",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "USERNAME",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}