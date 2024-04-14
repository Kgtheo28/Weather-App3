package com.example.weatherapp.api

object ApiClient {
    val apiService: ApiServices by lazy {
        RetrofitClient.retrofit.create(ApiServices::class.java)
    }
}