package com.example.weatherapp.interfaces

import com.example.weatherapp.cities.cpt.CapeTownCurrentWeather
import com.example.weatherapp.cities.jhb2.JohannesburgCurrentWeather
import com.example.weatherapp.cities.pta.PretoriaCurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface  {
    @GET("weather?")
    suspend fun getData(
        @Query("q") cityName: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): JohannesburgCurrentWeather

    @GET("weather?")
    suspend fun getPretoriaData(
        @Query("q") cityName: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): PretoriaCurrentWeather

    @GET("weather?")
    suspend fun getCapeTownData(
        @Query("q") cityName: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): CapeTownCurrentWeather
}