package com.example.sale1996.forecastmvvm.data.network.response

import com.example.sale1996.forecastmvvm.data.db.entity.CurrentWeatherEntry
import com.example.sale1996.forecastmvvm.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    val location: WeatherLocation,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry
)