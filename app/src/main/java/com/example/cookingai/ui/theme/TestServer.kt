package com.example.cookingai.ui.theme

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cookingai.MainViewModel
import com.example.cookingai.models.ServerViewModel

@Composable
fun TestServer(serverViewModel: ServerViewModel, mainViewModel: MainViewModel) {
    val context = LocalContext.current

    // LiveData для текста и изображения
    val textResponse by serverViewModel.responseLiveData.observeAsState()
    val photoResponse by serverViewModel.photoResponseLiveData.observeAsState() // Новый LiveData для фото

    // Состояние для текстового ввода
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поле ввода текста
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите текст") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка отправки текста
        Button(
            onClick = {
                if (inputText.isNotEmpty()) {
                    serverViewModel.sendText(inputText) // Отправляем текст
                } else {
                    Toast.makeText(context, "Введите текст", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Отправить текст")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение ответа на текст
        textResponse?.let {
            Text(text = "Ответ на текст: $it")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка отправки изображения
        Button(
            onClick = {
                val photoUri = mainViewModel.lastPhotoUri.value
                if (photoUri != null) {
                    serverViewModel.sendImage(photoUri, context.contentResolver) // Отправляем изображение
                } else {
                    Toast.makeText(context, "Изображение не выбрано", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Отправить изображение")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение ответа на изображение
        photoResponse?.let {
            Text(text = "Ответ на изображение: $it")
        }
    }
}

