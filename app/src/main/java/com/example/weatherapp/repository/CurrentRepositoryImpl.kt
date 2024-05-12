package com.example.weatherapp.repository

import com.example.weatherapp.cities.jhb.CurrentWeather
import com.example.weatherapp.interfaces.ApiInterface
import javax.inject.Inject

class CurrentRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface) {

    suspend fun getJohannesburgData (
        city: String,
        units: String,
        apiKey: String
    ): CurrentWeather {
        return apiInterface.getDataList(city, units, apiKey)
    }
}