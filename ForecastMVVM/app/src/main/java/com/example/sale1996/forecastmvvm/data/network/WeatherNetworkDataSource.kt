package com.example.sale1996.forecastmvvm.data.network

import androidx.lifecycle.LiveData
import com.example.sale1996.forecastmvvm.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {

    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    /*
    * Ova funkcija je ekvivaletna onoj u ApixuWeatherApiService (getCurrentWeather)
    * stim da mi ovde ne vracamo direktno vrednost nego ce ova funkcija
    * da updejta downloadedCurrentWeatherLiveData koja je observovana
    *
    * i sto je najbitnije sve se ovo odvija asihrono
    * */
    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}