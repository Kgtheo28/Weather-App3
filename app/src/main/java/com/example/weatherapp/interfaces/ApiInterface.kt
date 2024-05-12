package com.example.weatherapp.interfaces

import com.example.weatherapp.cities.jhb.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface  {
    @GET("weather?")
    suspend fun getData(
        @Query("q") cityName: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): CurrentWeather
}