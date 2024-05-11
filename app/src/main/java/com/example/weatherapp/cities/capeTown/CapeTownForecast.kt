package com.example.weatherapp.cities.capeTown

data class CapeTownForecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastData>,
    val message: Int
)