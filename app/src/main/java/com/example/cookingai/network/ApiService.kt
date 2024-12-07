package com.example.cookingai.network

import com.example.cookingai.models.RequestModel
import com.example.cookingai.models.ResponseImageModel
import com.example.cookingai.models.ResponseModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("/process_text") // Адрес эндпоинта на сервере
    fun sendText(@Body request: RequestModel): Call<ResponseModel>

    @Multipart
    @POST("/upload_photo")
    fun uploadPhoto(
        @Part photo: MultipartBody.Part
    ): Call<List<String>> // Возвращает список

    @Multipart
    @POST("/process_image") // Обновите путь, если сервер настроен иначе
    fun processImage(
        @Part image: MultipartBody.Part
    ): Call<ResponseImageModel> // Возвращает JSON-объект с продуктами и рецептом


    @POST("/process_list")
    fun processList(
        @Body data: List<String>
    ): Call<String> // Возвращает строку
}

//data class RequestData(val data: String)
//data class ResponseData(val result: String)
//data class TextRequest(val text: String)
//data class TextResponse(val processedText: String)
