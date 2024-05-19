package com.example.weatherapp.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.cities.cpt.CapeTownCurrentWeather
import com.example.weatherapp.cities.jhb.CurrentWeather
import com.example.weatherapp.cities.pta.PretoriaCurrentWeather
import com.example.weatherapp.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val context: Context
) : ViewModel() {

    // Johannesburg
    private val _myData = MutableLiveData<CurrentWeather>()
    val myData: LiveData<CurrentWeather> get() = _myData

    // Pretoria
    private val _pretoriaData = MutableLiveData<PretoriaCurrentWeather>()
    val pretoriaData: LiveData<PretoriaCurrentWeather> get() = _pretoriaData

    // Cape Town
    private val _capeTownData = MutableLiveData<CapeTownCurrentWeather>()
    val capeTownData: LiveData<CapeTownCurrentWeather> get() = _capeTownData

    fun getJoziData(){
        viewModelScope.launch {
            try {
                val response = repository.getData("Johannesburg", "metric", context.getString(R.string.api_key))
                _myData.value = response
                Log.e("CityWeatherData", "Retrieving Data was SUCCESSFUL")
            } catch (e: Exception) {
                Log.e("CurrentViewModel", "Error fetching data", e)
            }
        }
    }

    fun getPretoriaData(){
        viewModelScope.launch {
            try {
                val response = repository.getPretoriaData("Pretoria", "metric", context.getString(R.string.api_key))
                _pretoriaData.value = response
                Log.e("CityWeatherData", "Retrieving Data was SUCCESSFUL")
            } catch (e: Exception) {
                Log.e("CurrentViewModel", "Error fetching data", e)
            }
        }
    }

    fun getCapeTownData(){
        viewModelScope.launch {
            try {
                val response = repository.getCapeTownData("Cape Town", "metric", context.getString(R.string.api_key))
                _capeTownData.value = response
                Log.e("CityWeatherData", "Retrieving Data was SUCCESSFUL")
            } catch (e: Exception) {
                Log.e("CurrentViewModel", "Error fetching data", e)
            }
        }
    }
}