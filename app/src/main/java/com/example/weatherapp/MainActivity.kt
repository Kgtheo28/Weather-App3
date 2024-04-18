package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.MyAdapter
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.di.MyDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var dataViewModel: MyDataViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)






        binding.btnJhb.setOnClickListener {

            dataViewModel.fetchMyData()

            dataViewModel.myData.observe(this, Observer { currentWeather ->
                // Update UI with the fetched weather data
                binding.tvCityName.text = currentWeather.name
                binding.tvTime.text = currentWeather.timezone.toString()
                binding.tvTemp.text = currentWeather.main.temp.toString()
                binding.windSpeedNumber.text = currentWeather.wind.speed.toString()
                binding.humidityNumber.text = currentWeather.main.humidity.toString()
            })
        }

        binding.btnPta.setOnClickListener {

            dataViewModel.fetchPretoriaData()

            dataViewModel.myPretoriaData.observe(this, Observer { currentWeather ->
                // Update UI with the fetched weather data
                binding.tvCityName.text = currentWeather.name
                binding.tvTime.text = currentWeather.timezone.toString()
                binding.tvTemp.text = currentWeather.main.temp.toString()
                binding.windSpeedNumber.text = currentWeather.wind.speed.toString()
                binding.humidityNumber.text = currentWeather.main.humidity.toString()
            })
        }


    }
}