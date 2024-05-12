package com.example.weatherapp.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.cities.jhb.CurrentWeather
import com.example.weatherapp.repository.CurrentRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class CurrentViewModel @Inject constructor(
    private val repositoryImpl: CurrentRepositoryImpl,
    private val context: Context
) : ViewModel() {

    // Johannesburg
    private val _myJoziData = MutableLiveData<CurrentWeather>()
    val myJoziData: LiveData<CurrentWeather> get() = _myJoziData

    fun getJoziData(){
        viewModelScope.launch {
            try {
                val response = repositoryImpl.getJohannesburgData("Johannesburg", "metric", context.getString(R.string.api_key))
                _myJoziData.value = response
            } catch (e: Exception) {
                Log.e("CurrentViewModel", "Error fetching data", e)
            }
        }
    }


}