package com.example.cookingai.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingai.models.RequestModel
import com.example.cookingai.models.ResponseModel
import com.example.cookingai.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {
    val responseLiveData = MutableLiveData<String>() // Здесь будет ответ от сервера

    fun sendText(text: String) {
        val request = RequestModel(text)

        RetrofitClient.apiService.sendText(request).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        responseLiveData.postValue(it.processedText)
                    }
                } else {
                    responseLiveData.postValue("Ошибка: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                responseLiveData.postValue("Не удалось подключиться к серверу")
            }
        })
    }
}
