package com.example.weatherapp.di

import com.example.weatherapp.current.CurrentWeather
import com.example.weatherapp.pretoria.current.CurrentPretoriaWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PretoriaApiInterface {
    @GET("weather?")
    fun getDataList(
        @Query("q") cityName: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): Call<CurrentPretoriaWeather>
}