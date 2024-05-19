package com.example.weatherapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.BottomItemLayoutBinding
import com.example.weatherapp.databinding.SheetLayoutBinding
import com.example.weatherapp.viewModel.CurrentWeatherViewModel
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
    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel

    // Dialogs
    private lateinit var dialog: BottomSheetDialog
    private lateinit var dialog2: BottomSheetDialog



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
    }


}




