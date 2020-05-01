package com.example.sale1996.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.example.sale1996.forecastmvvm.data.db.CurrentWeatherDao
import com.example.sale1996.forecastmvvm.data.db.WeatherLocationDao
import com.example.sale1996.forecastmvvm.data.db.entity.WeatherLocation
import com.example.sale1996.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.example.sale1996.forecastmvvm.data.network.WeatherNetworkDataSource
import com.example.sale1996.forecastmvvm.data.network.response.CurrentAPIWeatherResponse
import com.example.sale1996.forecastmvvm.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

/*
* Primeti da su ulazi u ovu implementaicju interfejsi
* kako bi decouplovali sve
* */
class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val locationProvider: LocationProvider,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {

    init {
        //observeForever ide jer ono repository nema svoj lifecycle pa kad se app unisti
        //onda ce se unistiti i repository kao i njegov observe
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            //persist
            persistFetchedCurrentWeather(newCurrentWeather)
//            GlobalScope.launch(Dispatchers.IO) {
//                currentWeatherDao.upsert(newCurrentWeather.currentWeather)
//            }
        }
    }

    /*
    * out prefix od UnitSpecificCurrentWeather
    * nam govori da mozemo da vratimo kao rezultat i njegovu implementaciju pored njega samog...
    *
    * ova funkcija nam vraca vrednost koja se nalazi u lokalnoj bazi
    * */
    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        /*
        * I ovde cemo pozivati asihroni poziv. Razlog zbog koga je pozivan withContext, umesto
        * GlobalScope jeste sto withContext vraca povratnu vrednost, dok GlobalScope ne vraca...
        * */
        return withContext(Dispatchers.IO) {
            initWeatherData(metric)
            return@withContext currentWeatherDao.getWeather()
        }
    }


    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }
    }

    /*
    * metoda koja cuva response u bazu
    * */
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentAPIWeatherResponse){
        /*
        * Spomenuli smo da treba izbegavati GlobalScope.. ali kod repozitorijuma nije to slucaj
        * jer on nema svoj lifecycle... da smo kod fragmenta pokrenuli asihroni poziv i npr u medjuvremenu
        * se fragment unisti a nakon toga asihroni poziv dovbrsi svoj posao app ce dobiti exception..
        * ali u ovom slucaju posto repository nema svoj lifecycle nece doci do toga...
        * */
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeather)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData(isMetric: Boolean){
        val lastWeatherLocation = weatherLocationDao.getLocation().value

        if(lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)){
            fetchCurrentWeather(isMetric)
            return
        }
        //proverava vreme od zadnjeg poziva da li je potreban fetch
        if(isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime)){
            fetchCurrentWeather(isMetric)
        }
    }

    private suspend fun fetchCurrentWeather(isMetric: Boolean){
        /*
        * Primeti da ovde nema povratne vrednosti.. jer nam nije ni potrebno..
        * ovde pozivamo fetchCurrentWeather od networkDataSource-a i prosledjujemo
        * 2 parametra, te ta funkcija salje zahtev ka netu i updejta MutableLiveData
        * i posto smo se mi subscribovali na taj mutablelivedata nama ce se to automatski
        * auzirati ovde jer ga osluskujemo...
        * */
        var units = " m"
        if(isMetric){
            units = " i"
        }
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(), //tu prosledjujemo srbobran...
            " " + Locale.getDefault().language,//ovo smo uzeli lokal sa telefona...
             units
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean{
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

}