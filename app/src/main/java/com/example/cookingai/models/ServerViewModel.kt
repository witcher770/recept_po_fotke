package com.example.cookingai.models

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookingai.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ServerViewModel : ViewModel() {
    val responseLiveData = MutableLiveData<String>() // Здесь будет ответ от сервера
    val photoResponseLiveData = MutableLiveData<String>() // Новый ответ на изображение

    fun sendText(text: String) {
        val request = RequestModel(text) // Создается модель запроса с текстом

        RetrofitClient.apiService.sendText(request).enqueue(object : Callback<ResponseModel> {
            // Этот метод выполняется, когда сервер отвечает на запрос
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) { // Проверка, что ответ от сервера успешный (код 2xx)
                    response.body()?.let {
                        responseLiveData.postValue(it.processedText) // Устанавливает полученный обработанный текст в LiveData
                    }
                } else {
                    responseLiveData.postValue("Ошибка: ${response.code()}") // Выводит ошибку с кодом, если ответ от сервера не успешен
                }
            }

            // Этот метод выполняется, если не удалось выполнить запрос (например, нет интернета)
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.e("API", "Request failed: ${t.message}")
                responseLiveData.postValue("Не удалось подключиться к серверу") // Выводит сообщение об ошибке
            }
        })
    }

    fun sendPhoto(photoUri: Uri, contentResolver: ContentResolver) {
        val file = File(photoUri.path)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)

        RetrofitClient.apiService.uploadPhoto(body).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    response.body()?.let { list ->
                        // Обработай список
                    }
                } else {
                    // Обработай ошибку
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                // Обработай ошибку
            }
        })
    }

    fun sendList(data: List<String>) {
        RetrofitClient.apiService.processList(data).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        // Обработай строку
                    }
                } else {
                    // Обработай ошибку
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // Обработай ошибку
            }
        })
    }

    fun sendImage(photoUri: Uri, contentResolver: ContentResolver) {
        val file = File(photoUri.path) // Получение файла по URI
        val mimeType = contentResolver.getType(photoUri) ?: "image/*" // Определяем MIME-тип
        val requestFile = RequestBody.create(mimeType.toMediaTypeOrNull(), file) // Создаем тело запроса
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile) // Создаем MultipartBody.Part

        // Отправка изображения на сервер
        RetrofitClient.apiService.processImage(body).enqueue(object : Callback<ResponseImageModel> {
            override fun onResponse(call: Call<ResponseImageModel>, response: Response<ResponseImageModel>) {
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        // Обновляем LiveData с полученными продуктами и рецептом
                        photoResponseLiveData.postValue(
                            "Продукты: ${result.products.joinToString(", ")}\nРецепт: ${result.recipe}"
                        )
                    }
                } else {
                    photoResponseLiveData.postValue("Ошибка: ${response.code()}") // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<ResponseImageModel>, t: Throwable) {
                Log.e("API", "Ошибка отправки изображения: ${t.message}")
                photoResponseLiveData.postValue("Не удалось подключиться к серверу") // Обработка ошибки
            }
        })
    }



}
