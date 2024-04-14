package com.example.weatherapp.di

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.adapter.MyAdapter
import com.example.weatherapp.data.MyDataItem
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class MyDataViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _myData = MutableLiveData<List<MyDataItem>>()
    val myData: LiveData<List<MyDataItem>> get() = _myData

    private lateinit var myAdapter: MyAdapter

    fun fetchMyData() {
        apiInterface.getDataList().enqueue(object : retrofit2.Callback<List<MyDataItem>?> {
            override fun onResponse(
                call: Call<List<MyDataItem>?>,
                response: Response<List<MyDataItem>?>
            ) {
                if (response.isSuccessful) {
                    _myData.value = response.body()
                    Toast.makeText(context, "Retrofit is working", Toast.LENGTH_LONG).show()


                } else {
                    Log.e("MyDataViewModel", "Failed to fetch data: ${response.message()}")
                    Toast.makeText(context, "Retrofit is not working", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
                Log.e("MyDataViewModel", "Error fetching data", t)
                Toast.makeText(context, "Retrofit is working", Toast.LENGTH_LONG).show()
            }
        })
    }
}
