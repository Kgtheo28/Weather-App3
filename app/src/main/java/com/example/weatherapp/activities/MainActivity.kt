package com.example.weatherapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.cities.jhb2.JohannesburgCurrentWeather
import com.example.weatherapp.data.WeatherEntity
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.BottomItemLayoutBinding
import com.example.weatherapp.databinding.SheetLayoutBinding
import com.example.weatherapp.viewModel.CurrentWeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Binding Layouts
    private lateinit var binding: ActivityMainBinding
    private lateinit var sheetLayoutBinding: SheetLayoutBinding
    private lateinit var forecastLayoutBinding: BottomItemLayoutBinding

    // ViewModel
    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel

    // Dialogs
    private lateinit var dialog: BottomSheetDialog
    private lateinit var dialog2: BottomSheetDialog

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentWeatherViewModel = ViewModelProvider(this).get(CurrentWeatherViewModel::class.java)


        sheetLayoutBinding = SheetLayoutBinding.inflate(layoutInflater)
        forecastLayoutBinding = BottomItemLayoutBinding.inflate(layoutInflater)

        dialog = BottomSheetDialog(this, R.style.BottomSheetTheme,)
        dialog2 = BottomSheetDialog(this, R.style.BottomSheetTheme)

        dialog.setContentView(sheetLayoutBinding.root)
        dialog2.setContentView(forecastLayoutBinding.root)

        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        binding.imgSearch.setOnClickListener {
            dialog.show()
        }

        binding.cardView1.setOnClickListener{
            val intent = Intent(this, JohannesburgActivity::class.java)
            // Start the new activity
            startActivity(intent)
        }
        binding.cardView2.setOnClickListener{
            val intent = Intent(this, PretoriaActivity::class.java)
            // Start the new activity
            startActivity(intent)
        }
        binding.cardView3.setOnClickListener{
            val intent = Intent(this, CapeTownActivity::class.java)
            // Start the new activity
            startActivity(intent)
        }


        /*
        sheetLayoutBinding.button.setOnClickListener {

            val searchEditText = sheetLayoutBinding.editTextText2

            val cityName = searchEditText.text.toString()
            when (cityName) {
                "Johannesburg " -> JHBWeather()
                else -> Toast.makeText(this, "City not found",
                    Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

         */

        currentWeatherViewModel.getJoziData()
        currentWeatherViewModel.getPretoriaData()
        currentWeatherViewModel.getCapeTownData()

        binding.btnForecast.setOnClickListener {
            addWeatherDataToRoomDatabase()
        }



        currentWeatherViewModel.myData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data
            binding.actualTempNumber.text = "${currentWeather.main.temp}C"
            binding.minTempNumber.text = "${currentWeather.main.temp_max}C"
            binding.maxTempNumber.text = "${currentWeather.main.temp_max}C"
        })


        currentWeatherViewModel.pretoriaData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data
            binding.actualPtaTempNumber.text = "${currentWeather.main.temp}C"
            binding.minPtaTempNumber.text = "${currentWeather.main.temp_max}C"
            binding.maxPtaTempNumber.text = "${currentWeather.main.temp_max}C"

        })

        currentWeatherViewModel.capeTownData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data
            binding.actualCptTempNumber.text = "${currentWeather.main.temp}C"
            binding.minCptTempNumber.text = "${currentWeather.main.temp_max}C"
            binding.maxCptTempNumber.text = "${currentWeather.main.temp_max}C"
        })



        // Location Request
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            getLastLocation()
        }



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            } else {
                // Permission denied
                Log.e("LocationPermission", "Retrieving Location was unsuccessful")
            }
        }
    }
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses != null) {
                            if (addresses.isNotEmpty()) {
                                val cityName = addresses[0].locality ?: "City not found"
                                binding.tvCityName.text = cityName

                                when (cityName) {
                                    "Johannesburg" -> JHBWeather()
                                    "Pretoria" -> PTAWeather()
                                    "Cape Town" -> getCapeTownData()
                                }

                            } else {
                                binding.tvCityName.text = "City not found"
                            }
                        }
                    }
                }
        }
    }

    fun JHBWeather(){
        currentWeatherViewModel.myData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data
            binding.tvTemp.text = "${currentWeather.main.temp}C"
            binding.tvDescription.text = "${currentWeather.weather[0].description}"
            // Image Icon
            val iconId = currentWeather.weather[0].icon
            val imgUrl = "https://openweathermap.org/img/wn/$iconId.png"
            Picasso.get().load(imgUrl).into(binding.imageView)

        })
    }

    fun PTAWeather(){
        currentWeatherViewModel.pretoriaData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data
            binding.tvTemp.text = "${currentWeather.main.temp}C"
            binding.tvDescription.text = "${currentWeather.weather[0].description}"
            // Image Icon
            val iconId = currentWeather.weather[0].icon
            val imgUrl = "https://openweathermap.org/img/wn/$iconId.png"
            Picasso.get().load(imgUrl).into(binding.imageView)

        })
    }
    fun getCapeTownData(){
        currentWeatherViewModel.capeTownData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data
            binding.tvTemp.text = "${currentWeather.main.temp}C"
            binding.tvDescription.text = "${currentWeather.weather[0].description}"
            // Image Icon
            val iconId = currentWeather.weather[0].icon
            val imgUrl = "https://openweathermap.org/img/wn/$iconId.png"
            Picasso.get().load(imgUrl).into(binding.imageView)

        })
    }


    // The New weather fetching and storing method
    fun addData(cityName: String, actualTemp: Int, maxTemp: Int, minTemp: Int, description: String) {
        val weatherData = WeatherEntity(0, cityName, actualTemp, maxTemp, minTemp, description)
        currentWeatherViewModel.addWeatherDataToRoom(weatherData)
        Toast.makeText(this, "Successfully added", Toast.LENGTH_LONG).show()
    }
    fun addWeatherDataToRoomDatabase() {
        currentWeatherViewModel.myData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data
            val cityName = currentWeather.name
            val actualTemp = currentWeather.main.temp.toInt()
            val maxTemp = currentWeather.main.temp_max.toInt()
            val minTemp = currentWeather.main.temp_min.toInt()
            val description = currentWeather.weather[0].description // Assuming weather is a list

            // Display weather data in UI
            binding.tvCityName.text = cityName
            binding.actualTempNumber.text = actualTemp.toString()
            binding.tvMaxTemp.text = maxTemp.toString()
            binding.minTempNumber.text = minTemp.toString()

            // Add weather data to Room database
            addData(cityName, actualTemp, maxTemp, minTemp, description)
        })
    }
    // IT ENDS HERE
}




