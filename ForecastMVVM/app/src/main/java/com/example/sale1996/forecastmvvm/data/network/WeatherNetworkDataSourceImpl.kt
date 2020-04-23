package com.example.sale1996.forecastmvvm.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sale1996.forecastmvvm.data.network.response.CurrentWeatherResponse
import com.example.sale1996.forecastmvvm.internal.NoConectivityException

class WeatherNetworkDataSourceImpl(

    private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {

    /*
        Ovako posto LiveData mi ne mozemo menjati jelte... napravili smo
        objekat koji predstavlja mutableLiveData i onda kada neko bude trazio
        livedata objekat .. mi cemo mutablelivedata castovati u livedata i vratiti nazad

        Taj sto bude trazio rezultat nece nista moci da menja u tom objektu sto je i nas cilj
        , dakle jedino mesto gde mozemo menjati objekat je ova klasa i nigde vise...
     */

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather


    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather = apixuWeatherApiService
                .getCurrentWeather(location, languageCode)
                .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConectivityException){
            // ovde hvatamo exception ukoliko nemamo interneta
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}