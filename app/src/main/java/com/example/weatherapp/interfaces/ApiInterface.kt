package com.example.weatherapp.interfaces

import com.example.weatherapp.cities.capeTownCurrent.CapeTownCurrent
import com.example.weatherapp.current.CurrentWeather
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface  {

    @GET("weather?")
    fun getDataList(
        @Query("q") cityName: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): Call<CurrentWeather>
}