package com.example.cookingai


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cookingai.ui.theme.CookingCamera
import com.example.cookingai.ui.theme.History
import com.example.cookingai.ui.theme.SettiSreen
//import com.example.cookingai.models.MainViewModel

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.cookingai.ui.theme.MainScreenshot
import com.example.cookingai.ui.theme.TestPage

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable

import coil.compose.AsyncImage
import com.example.cookingai.models.RecipeViewModel
import com.example.cookingai.models.ServerViewModel
import com.example.cookingai.ui.theme.HistoryRecipes
import com.example.cookingai.ui.theme.ListOfIngredients
import com.example.cookingai.ui.theme.RecipeDetailScreen
import com.example.cookingai.ui.theme.TestServer

//import com.example.cookingai.ui.theme.MainScreenshot


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

class MainViewModel : ViewModel() {
    // Переменная для хранения последнего сделанного фото
    val lastPhotoUri = mutableStateOf<Uri?>(null)

    // Функция для обновления URI последнего фото
    fun updateLastPhoto(uri: Uri) {
        lastPhotoUri.value = uri
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val recipeViewModel = RecipeViewModel()
    val serverViewModel = ServerViewModel()
    val allRecipes = remember { mutableStateListOf<List<String>>() }


    Column {
        NavHost(navController = navController, startDestination = "MainScreen") {
            composable("TestPage") { TestPage(recipeViewModel) }
            composable("MainScreen") { MainScreenshot(navController) }
            composable("Settin") { SettiSreen(navController) }
            composable("CookingCamera") { CookingCamera(navController, viewModel, serverViewModel, recipeViewModel) }
            composable("History") { History(navController) }
            composable("ListOfIngredients") { ListOfIngredients(navController, viewModel, recipeViewModel) }
            composable("TestHistory") { TestServer(serverViewModel) }
            composable("recipe") { backStackEntry ->
                val recipe = backStackEntry.arguments?.getStringArrayList("recipe") ?: listOf()
                RecipeDetailScreen(
                    recipe = recipe,
                    navController = navController,
                    onSaveRecipe = { allRecipes.add(it) },
                    onDeleteRecipe = { recipeToDelete -> allRecipes.remove(recipeToDelete) }
                )
            }
            composable("allRecipes") {
                HistoryRecipes(
                    allRecipes = allRecipes,
                    navController = navController
                )
            }
        }

        // Отображаем последнее фото под кнопкой "История", если оно есть
        viewModel.lastPhotoUri.value?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Последнее фото",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(200.dp)
            )
        }
    }
}




@Composable
fun RequestPermissions() {
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA
    val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

    val cameraPermissionState = remember {
        ActivityCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED
    }

    val storagePermissionState = remember {
        ActivityCompat.checkSelfPermission(context, storagePermission) == PackageManager.PERMISSION_GRANTED
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        // Обработка разрешений
    }

    if (!cameraPermissionState || !storagePermissionState) {
        Button(onClick = {
            requestPermissionLauncher.launch(arrayOf(cameraPermission, storagePermission))
        }) {
            Text("Запросить разрешения")
        }
    }
}
