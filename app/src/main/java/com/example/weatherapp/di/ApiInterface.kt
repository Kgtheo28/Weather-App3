package com.example.weatherapp.di

import com.example.weatherapp.data.MyData
import com.example.weatherapp.data.MyDataItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface  {

    @GET("posts")
    fun getDataList(): Call<List<MyDataItem>>
}