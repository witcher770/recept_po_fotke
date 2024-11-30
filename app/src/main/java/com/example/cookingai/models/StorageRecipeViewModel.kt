package com.example.cookingai.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cookingai.data.RecipeDatabase
import com.example.cookingai.models.RecipeDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StorageRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()

    val recipeList: Flow<List<RecipeDataModel>> = recipeDao.getAllRecipes()

    fun addRecipe(name: String, content: String) {
        viewModelScope.launch {
            recipeDao.insertRecipe(RecipeDataModel(name = name, content = content))
        }
    }

    // Новый метод для получения рецепта по ID
    fun getRecipeById(recipeId: Int): LiveData<RecipeDataModel?> {
        return recipeDao.getRecipeById(recipeId)
    }

    // Метод для обновления существующего рецепта
    fun updateRecipe(recipe: RecipeDataModel) {
        viewModelScope.launch {
            recipeDao.updateRecipe(recipe)
        }
    }
}
