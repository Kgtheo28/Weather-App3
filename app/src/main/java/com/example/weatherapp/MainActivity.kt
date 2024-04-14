package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.MyAdapter
import com.example.weatherapp.api.ApiClient
import com.example.weatherapp.api.PostItem
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.di.MyDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import javax.inject.Inject



@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MyAdapter

    private lateinit var myStringBuilder: StringBuilder

    @Inject lateinit var dataViewModel: MyDataViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //dataViewModel.fetchMyData()

        binding.button.setOnClickListener {
            dataViewModel.fetchMyData()
            //Toast.makeText(this, "Retrofit is working", Toast.LENGTH_LONG).show()


        }
    }




}