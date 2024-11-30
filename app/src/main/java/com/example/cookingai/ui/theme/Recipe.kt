package com.example.cookingai.ui.theme


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cookingai.models.RecipeViewModel



import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookingai.models.RecipeDataModel
import com.example.cookingai.models.StorageRecipeViewModel



@Composable
fun Recipe(
    navController: NavController,
    recipeId: Int? = null, // ID рецепта или null для создания нового
    recipeViewModel: RecipeViewModel = viewModel(),
    storageRecipeViewModel: StorageRecipeViewModel = viewModel()
) {
//    // Если ID передан, загружаем данные из базы, иначе временные данные
//    val recipeData = if (recipeId != null) {
//        storageRecipeViewModel.getRecipeById(recipeId) // Получение рецепта по ID из базы
//    } else {
//        recipeViewModel.strings
//    }
//
//    var recipeName by remember { mutableStateOf(recipeData?.first ?: "") }
//    var recipeContent by remember { mutableStateOf(recipeData?.second ?: "") }

    // Если ID передан, загружаем данные из базы, иначе временные данные
    // Данные рецепта из базы данных (если передан ID)
    val recipeFromDatabase = recipeId?.let { id ->
        storageRecipeViewModel.getRecipeById(id).observeAsState()
    }?.value

    // Локальные переменные для редактирования
    var recipeName by remember {
        mutableStateOf(
            recipeFromDatabase?.name ?: recipeViewModel.strings.getOrNull(0) ?: ""
        )
    }
    var recipeContent by remember {
        mutableStateOf(
            recipeFromDatabase?.content ?: recipeViewModel.strings.getOrNull(1) ?: ""
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = recipeName,
            onValueChange = { recipeName = it },
            label = { Text("Название рецепта") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = recipeContent,
            onValueChange = { recipeContent = it },
            label = { Text("Описание рецепта") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            maxLines = 10
        )

        Button(
            onClick = {
                if (recipeId == null) {
                    // Создание нового рецепта
                    storageRecipeViewModel.addRecipe(recipeName, recipeContent)
                } else {
                    // Обновление существующего рецепта
                    storageRecipeViewModel.updateRecipe(
                        RecipeDataModel(
                        id = recipeId,
                        name = recipeName,
                        content = recipeContent
                    )
                    )
                }
                navController.navigate("RecipeListScreen")
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text(if (recipeId == null) "Сохранить новый рецепт" else "Обновить рецепт")
        }
    }
}

