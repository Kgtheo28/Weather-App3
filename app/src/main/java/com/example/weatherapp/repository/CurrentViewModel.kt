package com.example.weatherapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.cities.capeTownCurrent.CapeTownCurrent
import com.example.weatherapp.current.CurrentWeather
import com.example.weatherapp.interfaces.CapeTownInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CurrentViewModel @Inject constructor(
    private val repository: CurrentRepositoryImpl,
    private val context: Context
) : ViewModel() {

    private val _myData = MutableLiveData<CapeTownCurrent>()
    val myData: LiveData<CapeTownCurrent> get() = _myData

    fun getCPTData(){
        viewModelScope.launch {
            try {
                val response = repository.getCapeTownData("Cape Town", "metric", context.getString(R.string.api_key))
                _myData.value = response
            } catch (e: Exception) {
                Log.e("CurrentViewModel", "Error fetching data", e)
            }
        }
    }
}