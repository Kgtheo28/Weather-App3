package com.example.weatherapp.di

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.R
import com.example.weatherapp.current.CurrentWeather
import com.example.weatherapp.current.Main
import com.example.weatherapp.pretoria.current.CurrentPretoriaWeather
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class MyDataViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    private val pretoriaApiInterface: PretoriaApiInterface,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _myData = MutableLiveData<CurrentWeather>()
    val myData: LiveData<CurrentWeather> get() = _myData

    private val _myPretoriaData = MutableLiveData<CurrentPretoriaWeather>()
    val myPretoriaData: LiveData<CurrentPretoriaWeather> get() = _myPretoriaData

    fun fetchMyData() {
        apiInterface.getDataList(
            "johannesburg",
            "metric",
            context.getString(R.string.api_key)).enqueue(object : retrofit2.Callback<CurrentWeather> {
            override fun onResponse(
                call: Call<CurrentWeather>?,
                response: Response<CurrentWeather>?

            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        _myData.value = response.body()
                        Log.e("MyDataViewModel", "Johannesburg data: ${response.message()}")
                    } else {
                        Log.e("MyDataViewModel", "Failed to fetch Johannesburg data: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<CurrentWeather>?, t: Throwable) {
                Log.e("MyDataViewModel", "Error fetching data", t)
            }
        })
    }

    fun fetchPretoriaData(){
        pretoriaApiInterface.getDataList("pretoria", "metric", context.getString(R.string.api_key)).enqueue(object : retrofit2.Callback<CurrentPretoriaWeather> {
            override fun onResponse(
                call: Call<CurrentPretoriaWeather>?,
                response: Response<CurrentPretoriaWeather>?

            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        _myPretoriaData.value = response.body()
                        Log.e("MyDataViewModel", "Pretoria data: ${response.message()}")
                    } else {
                        Log.e("MyDataViewModel", "Failed to fetch data: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<CurrentPretoriaWeather>?, t: Throwable) {
                Log.e("MyDataViewModel", "Error fetching data", t)
            }
        })

    }
}
