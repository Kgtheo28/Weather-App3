package com.example.weatherapp.repository

import com.example.weatherapp.cities.capeTownCurrent.CapeTownCurrent
import com.example.weatherapp.cities.jhb.CurrentWeather
import com.example.weatherapp.interfaces.ApiInterface
import com.example.weatherapp.interfaces.CapeTownInterface
import javax.inject.Inject

class CurrentRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val capeTownInterface: CapeTownInterface) {

    suspend fun getJohannesburgData (
        city: String,
        units: String,
        apiKey: String
    ): CurrentWeather {
        return apiInterface.getDataList(city, units, apiKey)
    }
    suspend fun getCapeTownData(
        city: String,
        units: String,
        apiKey: String
    ): CapeTownCurrent {
        return capeTownInterface.getCapeTownData(city, units, apiKey)
    }
}