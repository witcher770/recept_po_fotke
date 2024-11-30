package com.example.cookingai.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.cookingai.models.RecipeDataModel

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeDataModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeDataModel)

    // Новый запрос для получения рецепта по ID
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): LiveData<RecipeDataModel?>

    @Update
    suspend fun updateRecipe(recipe: RecipeDataModel)
}