package com.example.sale1996.forecastmvvm.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sale1996.forecastmvvm.data.db.entity.CurrentWeather
import com.example.sale1996.forecastmvvm.data.network.response.CurrentAPIWeatherResponse
import com.example.sale1996.forecastmvvm.data.network.response.CurrentAPIWeatherResponseWithArrays
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

    private val _downloadedCurrentWeather = MutableLiveData<CurrentAPIWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentAPIWeatherResponse>
        get() = _downloadedCurrentWeather


    override suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String,
        units: String
    ) {
        try {
            /*
            * Ovde dobavljamo podatke sa API-a
            * */
            val fetchedCurrentWeather = apixuWeatherApiService
                .getCurrentWeather(location, languageCode, units)
                .await()

            //ovde vrsimo transformaciju array objekata u string
            val currentWeatherResponse = convertAPIWeatherToDatabaseWeather(fetchedCurrentWeather)

            _downloadedCurrentWeather.postValue(currentWeatherResponse)
        }
        catch (e: NoConectivityException){
            // ovde hvatamo exception ukoliko nemamo interneta
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

    private fun convertAPIWeatherToDatabaseWeather(fetchedCurrentWeather: CurrentAPIWeatherResponseWithArrays): CurrentAPIWeatherResponse? {

        val currentWeather = CurrentWeather(
            fetchedCurrentWeather.current.cloudcover,
            fetchedCurrentWeather.current.feelslike,
            fetchedCurrentWeather.current.humidity,
            fetchedCurrentWeather.current.observationTime,
            fetchedCurrentWeather.current.precip,
            fetchedCurrentWeather.current.pressure,
            fetchedCurrentWeather.current.temperature,
            fetchedCurrentWeather.current.uvIndex,
            fetchedCurrentWeather.current.visibility,
            fetchedCurrentWeather.current.weatherCode,
            fetchedCurrentWeather.current.weatherDescriptions.get(0),
            fetchedCurrentWeather.current.weatherIcons.get(0),
            fetchedCurrentWeather.current.windDegree,
            fetchedCurrentWeather.current.windDir,
            fetchedCurrentWeather.current.windSpeed)

        return CurrentAPIWeatherResponse(
            currentWeather,
            fetchedCurrentWeather.location,
            fetchedCurrentWeather.request
        )
    }
}