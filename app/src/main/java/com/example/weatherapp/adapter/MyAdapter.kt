package com.example.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.MyDataItem

class MyAdapter (private val context: Context, var dataList: List<MyDataItem>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var userId: TextView
        var title: TextView

        init {
            userId = itemView.findViewById(R.id.tv_id)
            title = itemView.findViewById(R.id.tv_title_1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userId.text = dataList[position].userId.toString()
        holder.title.text = dataList[position].title
    }

    fun setData(newList: List<MyDataItem>){
        dataList = newList
        notifyDataSetChanged()
    }
}