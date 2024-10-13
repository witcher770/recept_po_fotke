package com.example.cookingai

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MainScreenshot(navController: NavController) {
    Column() {
        settin(navController)
        Avatarka()
        Taste(navController)
        Spacer(modifier = Modifier.height(16.dp))
        history(navController)
    }
}

@Composable
fun settin(navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    navController.navigate("Settin")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.setti),
                    contentDescription = "setting",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(64.dp)
                )
            }
        }
    }
}

@Composable
private fun Avatarka() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize(0.5f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.powar),
                contentDescription = "powarenok",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(192.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun Taste(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navController.navigate("CookingCamera")
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF98d7ff))
            ) {
                Text(
                    text = "Попробовать!",
                    style = TextStyle(color = Color(0xFFF5F7F3), fontSize = 30.sp))
            }
        }
    }
}

@Composable
fun history(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.26f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navController.navigate("History")
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF98d7ff))
            ) {
                Text(
                    text = "История",
                    style = TextStyle(color = Color(0xFFF5F7F3), fontSize = 30.sp))
            }
        }
    }
}

