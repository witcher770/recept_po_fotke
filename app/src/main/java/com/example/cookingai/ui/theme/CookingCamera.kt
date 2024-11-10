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


//@Composable
//public fun CookingCamera(navController: NavController) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,) {
//        Text(text = "В будущем тут будет камера, но пока только кнопочка")
//        Button(
//            onClick = {
//                navController.navigate("MainScreen")
//            },
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF98d7ff))
//        ) {
//            Text(text = "Назад в меню")
//        }
//    }
//}

//@Composable
//fun CookingCamera(navController: NavController) {
//    var permissionsGranted by remember { mutableStateOf(false) }
//
//    RequestCameraPermissions { granted ->
//        permissionsGranted = granted
//    }
//
//    if (permissionsGranted) {
//        CameraScreen()
//    } else {
//        Text("Разрешения не предоставлены. Пожалуйста, дайте доступ к камере.")
//    }
//}
//
//@Composable
//fun RequestCameraPermissions(onPermissionsResult: (Boolean) -> Unit) {
//    val context = LocalContext.current
//    val cameraPermission = Manifest.permission.CAMERA
//    val cameraPermissionState = remember {
//        ActivityCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED
//    }
//
//    val requestPermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { isGranted: Boolean ->
//            onPermissionsResult(isGranted)
//        }
//    )
//
//    if (!cameraPermissionState) {
//        Button(onClick = { requestPermissionLauncher.launch(cameraPermission) }) {
//            Text("Запросить разрешение на использование камеры")
//        }
//    } else {
//        onPermissionsResult(true)
//    }
//}
//
////@Composable
////fun RequestCameraPermission() {
////    val context = LocalContext.current
////    var hasCameraPermission by remember {
////        mutableStateOf(
////            ContextCompat.checkSelfPermission(
////                context,
////                Manifest.permission.CAMERA
////            ) == PackageManager.PERMISSION_GRANTED
////        )
////    }
////
////    val launcher = rememberLauncherForActivityResult(
////        contract = ActivityResultContracts.RequestPermission()
////    ) { isGranted: Boolean ->
////        hasCameraPermission = isGranted
////    }
////
////    if (!hasCameraPermission) {
////        Button(onClick = {
////            launcher.launch(Manifest.permission.CAMERA)
////        }) {
////            Text("Дать разрешение на камеру")
////        }
////    } else {
////        Text("Разрешение предоставлено")
////    }
////}
//
//@Composable
//fun CameraScreen() {
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
//
//    AndroidView(
//        factory = { ctx ->
//            val previewView = PreviewView(ctx)
//            cameraProviderFuture.addListener({
//                val cameraProvider = cameraProviderFuture.get()
//                val preview = Preview.Builder().build().also {
//                    it.setSurfaceProvider(previewView.surfaceProvider)
//                }
//
//                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
//            }, ContextCompat.getMainExecutor(ctx))
//
//            previewView
//        },
//        modifier = Modifier.fillMaxSize()
//    )
//}

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
fun CookingCamera(navController: NavController) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    var imageCapture: ImageCapture? = remember { null }
    val context = LocalContext.current  // Переместим контекст сюда


    CameraPermissionRequester(
        onPermissionGranted = { hasCameraPermission = true },
        onPermissionDenied = { /* Обработка ошибки */ }
    )

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview(
                imageCapture = { capture -> imageCapture = capture },
                onImageCaptured = { uri ->
                    // Обработка сохраненного изображения
                },
                onError = { exception ->
                    // Обработка ошибки
                }
            )

            // Кнопка для захвата фото
            IconButton(
                onClick = {
                    imageCapture?.let { capture ->
                        takePhoto(
                            imageCapture = capture,
                            context = context,
                            onImageCaptured = { uri ->
                                // Действие с сохраненным фото
                            },
                            onError = { exception ->
                                // Обработка ошибки
                            }
                        )
                    }
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
        }
    } else {
        Text("Запрос разрешения на использование камеры")
    }
}
