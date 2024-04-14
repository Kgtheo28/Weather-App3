package com.example.weatherapp.api

import com.example.weatherapp.data.MyData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServices {
    @GET("post")
    fun getPostById(@Path("id") postId: Int): Call<PostItem>
}