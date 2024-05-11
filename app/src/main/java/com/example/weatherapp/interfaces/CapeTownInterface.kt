package com.example.weatherapp.interfaces

import com.example.weatherapp.cities.capeTownCurrent.CapeTownCurrent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CapeTownInterface {

    @GET("weather?")
    suspend fun getCapeTownData(
        @Query("q") cityName: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): CapeTownCurrent

}