package com.example.kolobanovda_ki23_16_1b_pr2.api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kolobanovda_ki23_16_1b_pr2.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherResult>(WeatherResult.Loading)
    val weatherState: StateFlow<WeatherResult> = _weatherState

    private val api: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    fun fetchWeather() {
        viewModelScope.launch {
            try {
                Log.d("WeatherInfo", "Fetching weather with API Key: ${BuildConfig.WEATHER_API_KEY}")
                val response = api.getCurrentWeather(apiKey = BuildConfig.WEATHER_API_KEY)
                Log.d("WeatherInfo", "Weather response: $response")
                
                _weatherState.value = WeatherResult.Success(
                    temp = response.main.temp,
                    iconUrl = "https://openweathermap.org/img/wn/${response.weather[0].icon}@2x.png"
                )
            } catch (e: Exception) {
                Log.e("WeatherInfo", "Error fetching weather", e)
                _weatherState.value = WeatherResult.Error
            }
        }
    }
}

sealed class WeatherResult {
    object Loading : WeatherResult()
    data class Success(val temp: Double, val iconUrl: String) : WeatherResult()
    object Error : WeatherResult()
}
