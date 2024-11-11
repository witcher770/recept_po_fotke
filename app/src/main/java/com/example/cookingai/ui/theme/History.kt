package com.example.cookingai.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
public fun History(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(), //.background(Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,) {
        Text(text = "В будущем тут будет история, но пока только кнопочка")
        Button(
            onClick = {
                navController.navigate("MainScreen")
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF98d7ff))
        ) {
            Text(text = "Назад в меню")
        }

        Box(modifier = Modifier
            .padding(20.dp)
            //.background(Color.Yellow)
            ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    //.verticalScroll(rememberScrollState())
                    //.background(Color.Green)
            ) {
                items(count = 10) {
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp)
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFF0E89CD),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(10.dp),
                        text = "Resept $it",
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center
                        )

                }
            }
        }
    }
}