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
import com.example.cookingai.models.ServerViewModel


@Composable
fun TestServer(serverViewModel: ServerViewModel) {
    val context = LocalContext.current
    val response by serverViewModel.responseLiveData.observeAsState()

    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите текст") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (inputText.isNotEmpty()) {
                    serverViewModel.sendText(inputText)
                } else {
                    Toast.makeText(context, "Введите текст", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Отправить")
        }

        Spacer(modifier = Modifier.height(16.dp))

        response?.let {
            Text(text = "Ответ сервера: $it")
        }
    }
}
