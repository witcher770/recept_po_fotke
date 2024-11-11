package com.example.cookingai.network

import com.example.cookingai.models.RequestModel
import com.example.cookingai.models.ResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/process") // Адрес эндпоинта на сервере
    fun sendText(@Body request: RequestModel): Call<ResponseModel>
}

//data class RequestData(val data: String)
//data class ResponseData(val result: String)
//data class TextRequest(val text: String)
//data class TextResponse(val processedText: String)
