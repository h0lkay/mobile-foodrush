package com.example.kolobanovda_ki23_16_1b_pr2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.kolobanovda_ki23_16_1b_pr2.api.WeatherResult
import com.example.kolobanovda_ki23_16_1b_pr2.api.WeatherViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupWeather()

        findViewById<View>(R.id.deliveryAddressText)?.setOnClickListener {
            startActivity(Intent(this, AddressListActivity::class.java))
        }

        findViewById<View>(R.id.misterBurgerCard)?.setOnClickListener {
            startActivity(Intent(this, RestaurantActivity::class.java))
        }

        findViewById<View>(R.id.cartButton)?.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        findViewById<View>(R.id.profileButton)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun setupWeather() {
        val tempText = findViewById<TextView>(R.id.tempText)
        val weatherIcon = findViewById<ImageView>(R.id.weatherIcon)
        val weatherDesc = findViewById<TextView>(R.id.weatherDesc)

        lifecycleScope.launch {
            weatherViewModel.weatherState.collect { result ->
                when (result) {
                    is WeatherResult.Success -> {
                        tempText.text = "${result.temp.toInt()}°C"
                        weatherIcon.load(result.iconUrl) {
                            crossfade(true)
                            error(R.drawable.cloud)
                        }
                    }
                    is WeatherResult.Error -> {
                        tempText.text = "—"
                        weatherDesc.text = "Ошибка сети"
                    }
                    is WeatherResult.Loading -> {
                        tempText.text = "..."
                    }
                }
            }
        }

        weatherViewModel.fetchWeather()
    }
}
