package com.example.weatherdemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter(
    private val context: Context, private val data: Array<WeatherData>,
    private val listeners: WeatherAdapter.Listeners? = null
) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    public interface Listeners {
        fun onCityClick(city: String)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCity: TextView = view.findViewById(R.id.tvCity)
        val tvTemp: TextView = view.findViewById(R.id.tvTemp)
        val ivWeather: ImageView = view.findViewById(R.id.ivWeather)
        val rootLayout: LinearLayoutCompat = view.findViewById(R.id.rootLayout)

        init {
            rootLayout.setOnClickListener {
                listeners?.onCityClick(data[adapterPosition].city)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvCity.text = data[position].city
        viewHolder.tvTemp.text = data[position].temp.toString() + "°C"

        when (data[position].type) {
            "sunny" -> viewHolder.ivWeather.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_sunny
                )
            )
            "cloudy" -> viewHolder.ivWeather.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_cloudy
                )
            )
            "snow" -> viewHolder.ivWeather.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_snow
                )
            )
            else -> viewHolder.ivWeather.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_sunny
                )
            )
        }
    }

    override fun getItemCount() = data.size
}