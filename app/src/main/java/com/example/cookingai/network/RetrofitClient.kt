package com.example.cookingai.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
    private const val BASE_URL = "http://92.53.120.84:25/" // Замените на адрес вашего сервера

    // Настройка OkHttpClient с увеличенными тайм-аутами
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Тайм-аут для подключения
        .readTimeout(30, TimeUnit.SECONDS)   // Тайм-аут для чтения ответа
        .writeTimeout(30, TimeUnit.SECONDS)  // Тайм-аут для записи запроса
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Используем кастомный OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
