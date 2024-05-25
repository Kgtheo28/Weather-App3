package com.example.weatherapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
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
import com.example.weatherapp.`object`.Network
import com.example.weatherapp.R
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
import java.io.ByteArrayOutputStream
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
            //addWeatherDataToRoomDatabase()
            updateUI()
        }


        /*

        currentWeatherViewModel.myData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data
            binding.actualTempNumber.text = "${currentWeather.main.temp}C"
            binding.minTempNumber.text = "${currentWeather.main.temp_max}C"
            binding.maxTempNumber.text = "${currentWeather.main.temp_max}C"
        })

         */


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
                                   // "Johannesburg" -> JHBWeather()
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

    private fun updateUI() {
        if (Network.isNetworkAvailable(applicationContext)) {
            // Network is available, fetch data from API and update UI
            fetchDataFromAPI()
        } else {
            // Network is not available, display data from Room database
            displayDataFromDatabase()
            Toast.makeText(this, "No Network", Toast.LENGTH_LONG).show()
        }
    }

    private fun displayDataFromDatabase() {
        currentWeatherViewModel.roomData.observe(this, Observer { weatherList ->
            if (weatherList != null && weatherList.isNotEmpty()) {
                val weatherData = weatherList[0] // Assuming you only store one entry in the database
                // Display weather data from Room database
                binding.tvCityName.text = weatherData.cityName
                binding.tvTemp.text = "${weatherData.actualTemp}C"
                binding.actualTempNumber.text = "${weatherData.actualTemp}C"
                binding.maxTempNumber.text = "${weatherData.maxTemp}C"
                binding.minTempNumber.text = "${weatherData.minTemp}C"
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

    private fun fetchDataFromAPI() {
        currentWeatherViewModel.myData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data
            val cityName = currentWeather.name
            val actualTemp = currentWeather.main.temp.toLong()
            val maxTemp = currentWeather.main.temp_max.toLong()
            val minTemp = currentWeather.main.temp_min.toLong()
            val description = currentWeather.weather[0].description
            // Image URL
            val iconId = currentWeather.weather[0].icon
            val imgUrl = "https://openweathermap.org/img/wn/$iconId.png"

            Picasso.get().load(imgUrl).into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null) {
                        // Convert Bitmap to ByteArray
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                        val imageData = byteArrayOutputStream.toByteArray()

                        // Add weather data including image to Room database
                        addData(cityName, actualTemp, maxTemp, minTemp, description, imageData)
                    }
                }

                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                    Log.e("ImageDownload", "Failed to load image: ${e?.message}")
                    Toast.makeText(this@MainActivity, "Failed to download image", Toast.LENGTH_SHORT).show()

                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

            })


            // Display weather data in UI
            binding.tvCityName.text = cityName
            binding.tvTemp.text = "${actualTemp}C"
            binding.actualTempNumber.text = "${actualTemp}C"
            binding.maxTempNumber.text = "${maxTemp}C"
            binding.minTempNumber.text = "${minTemp}C"
            binding.tvDescription.text = description

            Picasso.get().load(imgUrl).into(binding.imageView)

        })
    }


    fun addData(cityName: String, actualTemp: Long, maxTemp: Long, minTemp: Long, description: String, image: ByteArray) {
        val weatherData = WeatherEntity(0, cityName, actualTemp, maxTemp, minTemp, description, image)
        currentWeatherViewModel.addWeatherDataToRoom(weatherData)
        Log.d("RoomDatabase", "Weather data added: $cityName, $actualTemp, $maxTemp, $minTemp, $description")
        Toast.makeText(this, "Successfully added", Toast.LENGTH_LONG).show()
    }

    // IT ENDS HERE
}




