package com.example.cookingai


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cookingai.ui.theme.CookingAITheme
import com.example.cookingai.ui.theme.CookingCamera
import com.example.cookingai.ui.theme.History
import com.example.cookingai.ui.theme.SettiSreen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "MainScreen") {
        composable("MainScreen") { MainScreenshot(navController)}
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
