package com.example.cookingai.models


data class ResponseImageModel(
    val products: List<String>, // Список продуктов
    val recipe: String          // Сгенерированный рецепт
)
