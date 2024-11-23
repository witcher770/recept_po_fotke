package com.example.cookingai.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookingai.models.RecipeViewModel

@Composable
fun TestPage(recipeViewModel: RecipeViewModel = viewModel()) {
    //var itemList = remember { mutableStateListOf<String>() }
    //itemList = remember { recipeViewModel.strings }
    //itemList.addAll(recipeViewModel.getList()) // передаем данные из списка с прошлой страницы
    // Получаем список из ViewModel
    //val itemList by remember { mutableStateOf(recipeViewModel.strings) }
    val itemList = recipeViewModel.strings

    Column(modifier = Modifier.fillMaxSize(),
        //.background(Color.Red), //,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top )
    {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            if (itemList.isEmpty()) {
//                Text("Список пуст")
//            } else {
//                Text("Список содержит элементы")
//            }
//        }


        Row(modifier = Modifier.padding(16.dp)) {
            // Перебираем список и отображаем каждую строку
            itemList.forEach { item ->
                Text(text = item, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }

}
