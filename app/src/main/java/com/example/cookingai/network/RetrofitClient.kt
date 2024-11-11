package com.example.cookingai.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//object RetrofitClient {
//    private const val BASE_URL = "https://timeweb.cloud/my/servers/3762693" // Замените на ваш URL сервера
//
//    // Экземпляр Retrofit
//    private val retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    // Функция для создания экземпляра ApiService
//    fun getApiService(): ApiService {
//        return retrofit.create(ApiService::class.java)
//    }
//}


object RetrofitClient {
    private const val BASE_URL = "https://timeweb.cloud/my/servers/3762693" // Замените на адрес вашего сервера

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
