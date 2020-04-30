package com.example.sale1996.forecastmvvm.data.provider

import com.example.sale1996.forecastmvvm.data.db.entity.Location

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: Location): Boolean
    suspend fun getPreferredLocationString(): String
}