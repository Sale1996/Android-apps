package com.example.sale1996.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.example.sale1996.forecastmvvm.data.db.entity.WeatherLocation
import com.example.sale1996.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    /*
    * Posto zelimo da se ova funkcija poziva asihrono iz coroutina
    * mi dodajemo prefix suspend ispred funkcije
    * */
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>

}