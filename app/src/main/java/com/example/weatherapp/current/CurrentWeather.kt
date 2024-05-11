package com.example.weatherapp.current

data class CurrentWeather(
    val base: String,
    val id: Int,
    val main: Main,
    val name: String,
    val weather: List<Weather>,
    val wind: Wind
)