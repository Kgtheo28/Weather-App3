package com.example.weatherapp.repository

import com.example.weatherapp.cities.jhb.CurrentWeather
import com.example.weatherapp.interfaces.ApiInterface
import javax.inject.Inject

class CurrentWeatherRepository @Inject constructor(
    private val apiInterface: ApiInterface) {

    suspend fun getData (
        city: String,
        units: String,
        apiKey: String
    ): CurrentWeather {
        return apiInterface.getData(city, units, apiKey)
    }
}