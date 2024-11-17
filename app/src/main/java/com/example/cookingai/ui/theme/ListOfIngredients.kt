package com.example.cookingai.ui.theme

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cookingai.MainViewModel
import androidx.lifecycle.ViewModel

@Composable
fun ListOfIngredients(navController: NavController, viewModel: MainViewModel = viewModel()){

    // Используем состояние для списка
    val itemList = remember { mutableStateListOf("сосиска", "сорделька", "колбаска", "котлетка", "тефтелька") }

    var newItem by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(),
        //.background(Color.Red), //,
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Top )
    {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            //.background(Color.Green),
            contentAlignment = Alignment.TopCenter,
            ) {
            // Отображаем последнее фото под кнопкой "История", если оно есть
            viewModel.lastPhotoUri.value?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Последнее фото",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(200.dp)
                )
            }
        }
        // Поле ввода для добавления нового элемента
        TextField(
            value = newItem,
            onValueChange = { newItem = it },
            placeholder = { Text("Введите новый ингредиент") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка для добавления нового элемента
        Button(
            onClick = {
                if (newItem.isNotBlank()) {
                    itemList.add(newItem.trim())
                    newItem = "" // Очистка поля ввода
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text("Добавить ингредиент")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.95f)
                .border(
                    width = 2.dp, // Толщина рамки
                    color = Color.Black, // Цвет рамки
                    shape = RoundedCornerShape(8.dp) // Скругленные углы рамки
                )
                .padding(10.dp) // Отступ внутри рамки
                //.background(Color.Blue)
        ) {
            itemsIndexed(itemList) { index, value ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Поле ввода для редактирования элемента
                    TextField(
                        value = value,
                        onValueChange = { newValue -> itemList[index] = newValue },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Кнопка для удаления элемента
                    IconButton(onClick = { itemList.removeAt(index) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить элемент"
                        )
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), // Добавляем отступы от краев экрана
            contentAlignment = Alignment.BottomCenter // Размещаем содержимое внизу по центру
        ) {
            Button(
                onClick = {
                    navController.navigate("MainScreen")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF98d7ff))
            ) {
                Text(text = "Подтвердить")
            }
        }


    }
}