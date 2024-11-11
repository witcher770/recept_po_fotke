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
import com.example.cookingai.models.MainViewModel

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


//import com.example.cookingai.ui.theme.MainScreenshot


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "MainScreen") {
        composable("MainScreen") { MainScreenshot(navController) }
        composable("Settin") { SettiSreen(navController) }
        composable("CookingCamera") { CookingCamera(navController) }
        composable("History") { History(navController) }
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
