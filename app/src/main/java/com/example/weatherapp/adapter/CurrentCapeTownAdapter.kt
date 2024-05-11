package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.current.CurrentWeather

class CurrentCapeTownAdapter (private var data: List<CurrentWeather>) : RecyclerView.Adapter<CurrentCapeTownAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityNameTextView: TextView = itemView.findViewById(R.id.tv_weather_condition)
        val temperatureTextView: TextView = itemView.findViewById(R.id.tv_temperature2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWeather = data[position]
        holder.cityNameTextView.text = currentWeather.main.temp_max.toString()
        holder.temperatureTextView.text = currentWeather.main.temp_min.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<CurrentWeather>) {
        data = newData
        notifyDataSetChanged()
    }
}
