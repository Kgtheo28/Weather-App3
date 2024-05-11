package com.example.weatherapp.pretoria.current

data class CurrentPretoriaWeather(
    val id: Int,
    val main: Main,
    val name: String,
    val weather: List<Weather>,
    val wind: Wind
)