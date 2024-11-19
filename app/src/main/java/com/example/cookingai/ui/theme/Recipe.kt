package com.example.cookingai.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RecipeDetailScreen(
    recipe: List<String>, // где-то здесь передаем сюда список
    navController: NavController,
    onSaveRecipe: (List<String>) -> Unit,
    onDeleteRecipe: (List<String>) -> Unit
) {
    LaunchedEffect(recipe) {
        if (recipe.isNotEmpty()) onSaveRecipe(recipe)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Название рецепта
        Text(
            text = recipe[0],
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Содержание рецепта
        Text(
            text = recipe[1],
            style = MaterialTheme.typography.body1,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Кнопка назад
            Button(onClick = { navController.navigate("allRecipes") }) {
                Text("Назад к рецептам")
            }

            // Кнопка удалить
            Button(
                onClick = {
                    onDeleteRecipe(recipe)
                    navController.navigate("allRecipes") {
                        popUpTo("recipe") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.error)
            ) {
                Text("Удалить", color = MaterialTheme.colors.onError)
            }
        }
    }
}
