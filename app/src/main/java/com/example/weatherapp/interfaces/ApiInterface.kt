package com.example.weatherapp.interfaces

import com.example.weatherapp.cities.cpt.CapeTownCurrentWeather
import com.example.weatherapp.cities.jhb2.JohannesburgCurrentWeather
import com.example.weatherapp.cities.pta.PretoriaCurrentWeather
import com.example.weatherapp.weatherData.CurrentWeather2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface  {

    @GET("weather?")
    suspend fun getCurrentWeather2(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): CurrentWeather2

}