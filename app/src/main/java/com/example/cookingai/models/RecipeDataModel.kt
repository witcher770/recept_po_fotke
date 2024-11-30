package com.example.cookingai.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeDataModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val content: String
)