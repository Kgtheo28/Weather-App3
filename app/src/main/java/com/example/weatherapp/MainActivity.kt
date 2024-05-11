package com.example.weatherapp

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.BottomItemLayoutBinding
import com.example.weatherapp.databinding.SheetLayoutBinding
import com.example.weatherapp.viewModel.CurrentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Binding Layouts
    private lateinit var binding: ActivityMainBinding
    private lateinit var sheetLayoutBinding: SheetLayoutBinding
    private lateinit var forecastLayoutBinding: BottomItemLayoutBinding

    // ViewModel
    private lateinit var currentViewModel: CurrentViewModel

    // Dialogs
    private lateinit var dialog: BottomSheetDialog
    private lateinit var dialog2: BottomSheetDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentViewModel = ViewModelProvider(this).get(CurrentViewModel::class.java)


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



        sheetLayoutBinding.button.setOnClickListener {

            val searchEditText = sheetLayoutBinding.editTextText2

            val cityName = searchEditText.text.toString()
            when (cityName) {
                "Johannesburg " -> JHBWeather()
                "Cape Town " -> getCapeTownData()
                else -> Toast.makeText(this, "City not found",
                    Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }


    }

    private fun getCapeTownData() {
        currentViewModel.getCPTData()

        currentViewModel.myData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data

            // Main Weather Details
            binding.tvCityName.text = currentWeather.name
            binding.tvTemp.text = "${currentWeather.main.temp}^C"
            binding.tvDescription.text = currentWeather.weather[0].description

            /// First Card View Layout Details
            binding.humidityNumber.text = "${currentWeather.main.humidity}%"
            binding.airQuality.text = currentWeather.main.pressure.toString()

            // Second Card View Details
            binding.minTempNumber.text = "${currentWeather.main.temp_min}^C"
            binding.maxTempNumber.text = "${currentWeather.main.temp_max}^C"
            //binding.uvNumber.text =

            // Image Icon
            val iconId = currentWeather.weather[0].icon
            val imgUrl = "https://openweathermap.org/img/wn/$iconId.png"

            Picasso.get().load(imgUrl).into(binding.imageView)
        })
    }

    private fun JHBWeather() {

        currentViewModel.getJoziData()

        currentViewModel.myJoziData.observe(this, Observer { currentWeather ->
            // Update UI with the fetched weather data


            // Main Weather Details
            binding.tvCityName.text = currentWeather.name
            binding.tvTemp.text = "${currentWeather.main.temp}^C"
            binding.tvDescription.text = currentWeather.weather[0].description

            /// First Card View Layout Details
            binding.humidityNumber.text = "${currentWeather.main.humidity}%"
            binding.airQuality.text = currentWeather.main.pressure.toString()

            // Second Card View Details
            binding.minTempNumber.text = "${currentWeather.main.temp_min}^C"
            binding.maxTempNumber.text = "${currentWeather.main.temp_max}^C"
            //binding.uvNumber.text =


            // Image Icon
            val iconId = currentWeather.weather[0].icon
            val imgUrl = "https://openweathermap.org/img/wn/$iconId.png"

            Picasso.get().load(imgUrl).into(binding.imageView)
        })
    }

}




