package com.example.weatherdemo

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {

    private var city: String = ""
    private var progressDialog: ProgressDialog? = null

    private lateinit var tvCity: TextView
    private lateinit var ivWeather: ImageView
    private lateinit var tvWeather: TextView
    private lateinit var tvTemp: TextView
    private lateinit var tvWind: TextView
    private lateinit var tvHumidity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViews()

        if (intent.extras != null && intent.hasExtra("city")) {
            city = intent.getStringExtra("city") ?: ""

            tvCity.text = city
        }

        getWeatherData()
    }

    private fun initViews() {
        tvCity = findViewById(R.id.tvCity)
        ivWeather = findViewById(R.id.ivWeather)
        tvWeather = findViewById(R.id.tvWeather)
        tvTemp = findViewById(R.id.tvTemp)
        tvWind = findViewById(R.id.tvWind)
        tvHumidity = findViewById(R.id.tvHumidity)
    }

    private fun getWeatherData() {
        val BASE_URL = "https://api.openweathermap.org/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiManager = retrofit.create(APIManager::class.java)
        val call = apiManager.getWeatherData(city, "YOUR_KEY", "metric")

        showProgressDialog()

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                hideProgressDialog()

                if (response.body() != null) {
                    Log.d("APICall", response.body().toString())

                    val body = response.body()
                    tvTemp.text =
                        (body?.asJsonObject?.get("main")?.asJsonObject?.get("temp_max")?.asString
                            ?: "-") + "°C"
                    tvWind.text =
                        (body?.asJsonObject?.get("wind")?.asJsonObject?.get("speed")?.asString
                            ?: "-") + " km/h"
                    tvHumidity.text =
                        (body?.asJsonObject?.get("main")?.asJsonObject?.get("humidity")?.asString
                            ?: "-") + "%"

                    tvWeather.text = body?.asJsonObject?.get("weather")?.asJsonArray?.get(0)?.asJsonObject?.get("main")?.asString
                        ?: ""

                    when (body?.asJsonObject?.get("weather")?.asJsonArray?.get(0)?.asJsonObject?.get("main")?.asString
                        ?: "") {
                        "sunny", "clear sky" -> ivWeather.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailActivity,
                                R.drawable.ic_sunny
                            )
                        )
                        "Clouds" -> ivWeather.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailActivity,
                                R.drawable.ic_cloudy
                            )
                        )
                        "Haze" -> ivWeather.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailActivity,
                                R.drawable.ic_haze
                            )
                        )
                        "snow" -> ivWeather.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailActivity,
                                R.drawable.ic_snow
                            )
                        )
                        else -> ivWeather.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailActivity,
                                R.drawable.ic_sunny
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                hideProgressDialog()

                Toast.makeText(this@DetailActivity, getString(R.string.err_msg), Toast.LENGTH_LONG)
                    .show()

                Log.d("APICall", "Failed: ${t.localizedMessage}")
            }
        })
    }

    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }

        progressDialog?.setMessage(getString(R.string.please_wait_))

        progressDialog?.show()
    }

    private fun hideProgressDialog() {
        progressDialog?.hide()
        progressDialog = null
    }
}