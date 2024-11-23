package com.example.cookingai.models

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    var currentRecipe: List<String>? = null

    private val _strings = mutableStateListOf<String>() // Список строк
    val strings: List<String> get() = _strings // Только для чтения извне

    // Метод для инициализации списка
    fun initializeList(initialList: List<String>) {
        viewModelScope.launch {
            _strings.clear() // Очищаем текущий список
            _strings.addAll(initialList) // Добавляем элементы из переданного списка
        }
    }

    // Метод для получения списка (уже доступен через val strings)
    fun getList(): List<String> = strings


}