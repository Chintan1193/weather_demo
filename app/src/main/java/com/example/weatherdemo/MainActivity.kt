package com.example.weatherdemo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), WeatherAdapter.Listeners {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = WeatherAdapter(
            this,
            arrayOf(
                WeatherData("Mumbai", 40.2f, "sunny"),
                WeatherData("London", 32.0f, "cloudy"),
                WeatherData("New York", 21.2f, "snow"),
                WeatherData(
                    "Paris", 30.5f, "sunny"
                )
            ),
            this
        )
        val rvList: RecyclerView = findViewById(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = adapter

        val ivSearch: ImageView = findViewById(R.id.ivSearch)
        ivSearch.setOnClickListener {
            val edtSearch: EditText = findViewById(R.id.edtSearch)
            openDetailScreen(edtSearch.text.toString())
        }
    }

    private fun openDetailScreen(city: String) {
        if (city.isNotEmpty()) {
            startActivity(Intent(this, DetailActivity::class.java).putExtra("city", city))
        } else {
            Toast.makeText(this, getString(R.string.city_empty_msg), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCityClick(city: String) {
        openDetailScreen(city)
    }
}