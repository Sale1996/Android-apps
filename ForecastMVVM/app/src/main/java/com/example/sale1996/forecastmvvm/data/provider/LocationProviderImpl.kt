package com.example.sale1996.forecastmvvm.data.provider

import com.example.sale1996.forecastmvvm.data.db.entity.Location

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: Location): Boolean {
        return true
    }

    override suspend fun getPreferredLocationString(): String {
        return "Srbobran"
    }
}