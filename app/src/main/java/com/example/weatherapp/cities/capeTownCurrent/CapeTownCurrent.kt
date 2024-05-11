package com.example.weatherapp.cities.capeTownCurrent

data class CapeTownCurrent(
    val base: String,
    val cod: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>
)