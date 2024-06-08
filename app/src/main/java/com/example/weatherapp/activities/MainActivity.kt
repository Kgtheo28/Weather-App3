package com.example.weatherapp.activities

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.NetworkChangeReceiver
import com.example.weatherapp.`object`.NetworkUtils
import com.example.weatherapp.R
import com.example.weatherapp.adapter.WeatherAdapter
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.BottomItemLayoutBinding
import com.example.weatherapp.databinding.SheetLayoutBinding
import com.example.weatherapp.viewModel.CurrentWeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Binding Layouts
    private lateinit var binding: ActivityMainBinding
    private lateinit var sheetLayoutBinding: SheetLayoutBinding

    // Weather Adapter
    private lateinit var weatherAdapter: WeatherAdapter

    // ViewModel
    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel

    // Dialogs
    private lateinit var dialog: BottomSheetDialog

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentWeatherViewModel = ViewModelProvider(this).get(CurrentWeatherViewModel::class.java)


        sheetLayoutBinding = SheetLayoutBinding.inflate(layoutInflater)

        dialog = BottomSheetDialog(this, R.style.BottomSheetTheme,)

        dialog.setContentView(sheetLayoutBinding.root)

        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        binding.imgSearch.setOnClickListener {
            dialog.show()
        }

        // Location Request
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            Log.e("location status", "Location found")
        } else {
            Log.e("location status", "Location not found")
        }

        // Search weather Data of any city
        sheetLayoutBinding.button.setOnClickListener {

            val searchEditText = sheetLayoutBinding.editTextText2

            val cityName = searchEditText.text.toString()
            val units = "metric"
            val apiKey = getString(R.string.api_key)

            if (cityName.isNotEmpty()) {
                currentWeatherViewModel.fetchWeatherFromAPI2(cityName, units, apiKey)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }

            currentWeatherViewModel.fetchWeatherFromAPI2(cityName, units, apiKey)

            dialog.dismiss()
        }

        // Create and register the BroadcastReceiver
        // Checking network Status
        networkChangeReceiver = NetworkChangeReceiver { isConnected ->
            currentWeatherViewModel.setConnectionStatus(isConnected)
        }
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)

        // Observe the network connection status
        currentWeatherViewModel.isConnected.observe(this, Observer { isConnected ->
            // Update the UI based on network connection status
            if (isConnected) {
                updateData()
            } else {
                displayDataFromDatabase()
            }
        })

    }

    // Request Location Permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            } else {
                // Permission denied
                Log.e("Location Permission", "Retrieving Location was unsuccessful")
            }
        }
    }

    private fun updateData() {
        if (NetworkUtils.isNetworkAvailable(applicationContext)) {
            // Network is available, fetch data from API and update UI
            Log.d("Checking Internet", "Internet Connection Available")
            getLastLocation()
            multipleCitiesWeather()
        } else {
            // Network is not available, display data from Room database
            Log.d("checking Internet", "Internet Connection Not Available")
            displayDataFromDatabase()
        }
    }

    private fun multipleCitiesWeather() {
        /// New city List

        weatherAdapter = WeatherAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = weatherAdapter
        }

        val YOUR_API_KEY = getString(R.string.api_key)
        val units = "metric"

        currentWeatherViewModel.fetchWeatherList(units, YOUR_API_KEY)

        currentWeatherViewModel.apiData2.observe(this, Observer { weatherList ->
            weatherAdapter = WeatherAdapter(weatherList)
            binding.recyclerView.adapter = weatherAdapter
        })
    }

    // When there is not internet Connection
    private fun displayDataFromDatabase() {
        currentWeatherViewModel.roomData.observe(this, Observer { weatherList ->
            if (weatherList != null && weatherList.isNotEmpty()) {
                val weatherData = weatherList[0] // Assuming you only store one entry in the database
                // Display weather data from Room database
                binding.tvCityName.text = weatherData.cityName
                binding.tvTemp.text = "${weatherData.actualTemp}C"
                binding.tvDescription.text = weatherData.description

                // Convert byte array to bitmap and display
                val bitmap = BitmapFactory.decodeByteArray(weatherData.imageData, 0, weatherData.imageData.size)
                binding.imageView.setImageBitmap(bitmap)

            } else {
                // Handle case where no data is available in Room database
                Toast.makeText(this, "No weather data available", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Automatic weather data from your current location
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
                                val units = "metric"
                                val apiKey = getString(R.string.api_key)

                                currentWeatherViewModel.fetchWeatherFromAPI2(cityName, units, apiKey)

                                currentWeatherViewModel.currentWeather.observe(this, Observer { currentWeather ->
                                    currentWeatherViewModel.addDataToLocalStorage(currentWeather)
                                })


                                currentWeatherViewModel.errorMessage.observe(this, Observer { errorMessage ->
                                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                                })

                                displayDataFromDatabase()

                            } else {
                                binding.tvCityName.text = "City not found"
                            }
                        }
                    }
                }
        }
    }





    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }
}




