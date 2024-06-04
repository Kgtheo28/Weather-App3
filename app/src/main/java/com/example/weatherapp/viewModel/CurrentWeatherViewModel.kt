package com.example.weatherapp.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.weatherData.CurrentWeather2
import com.example.weatherapp.data.WeatherEntity
import com.example.weatherapp.repository.Repository
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val repository: Repository,
    private val context: Context
) : ViewModel() {


    val roomData: LiveData<List<WeatherEntity>> = repository.getAllWeatherData()

    val networkStatus: MutableLiveData<Boolean> = MutableLiveData()

    val apiData = MutableLiveData<CurrentWeather2>()
    val currentWeather: LiveData<CurrentWeather2>
        get() = apiData

    val apiData2 = MutableLiveData<List<CurrentWeather2>>()
    val errorMessage = MutableLiveData<String>()



    private val southAfricanCities = listOf(
        "Cape Town", "Johannesburg", "Durban", "Pretoria", "Port Elizabeth",
        "Bloemfontein", "East London", "Kimberley", "Polokwane", "Pietermaritzburg"
    )




    // Retrieve Weather Data from API and store it in Room Database

    fun fetchWeatherFromAPI2(city: String, units: String,  apiKey: String) {
        viewModelScope.launch {
            try {
                val currentWeather = repository.getCurrentWeather2(city, units, apiKey)
                apiData.postValue(currentWeather)
            } catch (e: Exception) {
                errorMessage.postValue("Error fetching weather data")
            }
        }
    }

    // the Second RecyclerView list
    fun fetchWeatherList(units: String,  apiKey: String) {
        viewModelScope.launch {
            try {
                val weatherData = southAfricanCities.map { city ->
                    async { repository.getCurrentWeather2(city, units, apiKey) }
                }.awaitAll()
                apiData2.postValue(weatherData)
            } catch (e: Exception) {
                errorMessage.postValue("Error fetching weather data: ${e.message}")
            }
        }
    }



    // add weather data to Room Database

    fun addDataToLocalStorage(currentWeather: CurrentWeather2){

        val iconId = currentWeather.weather[0].icon
        val imgUrl = "https://openweathermap.org/img/wn/$iconId.png"

        Picasso.get().load(imgUrl).into(object : com.squareup.picasso.Target {

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                if (bitmap != null) {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val imageData = byteArrayOutputStream.toByteArray()
                    addData(currentWeather, imageData)

                }
            }

            private fun addData(currentWeather: CurrentWeather2, imageData: ByteArray) {

                val weatherData = WeatherEntity(
                    0,
                    currentWeather.name,
                    currentWeather.main.temp.toLong(),
                    currentWeather.main.temp_max.toLong(),
                    currentWeather.main.temp_min.toLong(),
                    currentWeather.weather[0].description,
                    imageData
                )
                addWeatherDataToDatabase(weatherData)
                Log.e("AddingDataToDatabase", "Weather Data added again")
            }

            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                Log.e("ImageDownload", "Failed to load image: ${e?.message}")
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })
    }

    fun addWeatherDataToDatabase(weatherEntity: WeatherEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWeatherData(weatherEntity)
        }
    }

}
