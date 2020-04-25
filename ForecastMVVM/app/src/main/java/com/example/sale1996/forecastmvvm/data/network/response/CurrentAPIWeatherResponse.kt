package com.example.sale1996.forecastmvvm.data.network.response


import com.example.sale1996.forecastmvvm.data.db.entity.CurrentWeather
import com.example.sale1996.forecastmvvm.data.db.entity.Location
import com.example.sale1996.forecastmvvm.data.db.entity.Request
import com.google.gson.annotations.SerializedName

data class CurrentAPIWeatherResponse(
    @SerializedName("current")
    val currentWeather: CurrentWeather,
    val location: Location,
    val request: Request
)