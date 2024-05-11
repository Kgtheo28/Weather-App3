package com.example.weatherapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.R
import com.example.weatherapp.cities.capeTownCurrent.CapeTownCurrent
import com.example.weatherapp.current.CurrentWeather
import com.example.weatherapp.interfaces.ApiInterface
import com.example.weatherapp.interfaces.CapeTownInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class CurrentRepositoryImpl @Inject constructor(
    private val apiInterface: CapeTownInterface) {

    suspend fun getCapeTownData(
        city: String,
        units: String,
        apiKey: String
    ): CapeTownCurrent {
        return apiInterface.getCapeTownData(city, units, apiKey)
    }
}