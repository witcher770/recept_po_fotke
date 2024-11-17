package com.example.cookingai.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.cookingai.MainViewModel


@Composable
fun CameraPermissionRequester(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            onPermissionGranted()
        }
    }
}


@Composable
fun CameraPreview(
    imageCapture: (ImageCapture) -> Unit,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val capture = remember { ImageCapture.Builder().build() }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, capture
                    )
                    imageCapture(capture) // Передаем объект ImageCapture
                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(context))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}


fun takePhoto(
    imageCapture: ImageCapture,
    context: Context,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val outputDirectory = context.filesDir
    val photoFile = File(
        outputDirectory,
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            .format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    imageCapture.takePicture(
        outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onImageCaptured(Uri.fromFile(photoFile))
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }
        }
    )
}


@Composable
fun CookingCamera(navController: NavController, viewModel: MainViewModel) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    // var imageCapture: ImageCapture? = remember { null }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) } // Инициализация с null
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) } // Хранит URI фото
    var showSuccessMessage by remember { mutableStateOf(false) } // Показывает сообщение об успехе
    val context = LocalContext.current

    // Запрос разрешения на камеру
    CameraPermissionRequester(
        onPermissionGranted = { hasCameraPermission = true
                                Log.d("Camera", "Camera permission granted")},
        onPermissionDenied = { /* Обработка ошибки */
                                Log.e("Camera", "Camera permission denied")}
    )

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Предварительный просмотр с камеры
            CameraPreview(
                imageCapture = { capture -> imageCapture = capture
                    Log.d("Camera", "imageCapture initialized") },
                onImageCaptured = { uri ->
                    Log.d("Camera", "Image captured: $uri")
                    capturedImageUri = uri // Сохраняем URI фото
                    viewModel.updateLastPhoto(uri) // Сохраните URI в ViewModel
                    showSuccessMessage = true // Показать сообщение об успехе
                },
                onError = { exception ->
                    // Обработка ошибки при захвате фото
                    Log.e("Camera", "Error capturing image: $exception")
                }
            )

            // Кнопка для захвата фото
            IconButton(
                onClick = {
                    if (imageCapture != null) {
                        Log.d("Camera", "Attempting to take photo...")
                        takePhoto(
                            imageCapture = imageCapture!!,
                            context = context,
                            onImageCaptured = { uri ->
                                Log.d("Camera", "Photo taken: $uri")
                                capturedImageUri = uri
                                viewModel.updateLastPhoto(uri)
                                showSuccessMessage = true // Показать сообщение об успехе

                            },
                            onError = { exception ->
                                Log.e("Camera", "Error in takePhoto: $exception")
                            }
                        )
                    } else {
                        Log.e("Camera", "imageCapture is null when attempting to take photo")
                    }

                    // здесь возможно стоит поставить переход
                    //navController.navigate("ListOfIngredients?photoUri=$uri")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .size(64.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Сделать фото",
                    tint = Color.Black,
                    modifier = Modifier.size(48.dp)
                )
            }

            // Сообщение об успешном создании фото
            if (showSuccessMessage) {
                Text(
                    text = "Фото сделано!",
                    color = Color.Green,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color.White)
                        .padding(8.dp)
                )
                // Таймер для скрытия сообщения через 2 секунды
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000) // Ждем 2 секунды
                    showSuccessMessage = false // Скрываем сообщение
                }
            }

            // Отображение сохраненного фото (если есть)
            capturedImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Сделанное фото",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(200.dp)
                        .padding(top = 16.dp)
                )
            }


            // Перемещение навигации в LaunchedEffect
            LaunchedEffect(capturedImageUri) {
                capturedImageUri?.let {
                    // Переход только после захвата фото
                    Log.d("Camera", "Navigating to ListOfIngredients")
                    navController.navigate("ListOfIngredients?photoUri=$it")
                }
            }
        }

    } else {
        Text("Запрос разрешения на использование камеры")
    }
}

