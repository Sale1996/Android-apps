package com.example.sale1996.forecastmvvm.data.network.response

import com.example.sale1996.forecastmvvm.data.db.entity.Current
import com.example.sale1996.forecastmvvm.data.db.entity.WeatherLocation
import com.example.sale1996.forecastmvvm.data.db.entity.Request
import com.google.gson.annotations.SerializedName
/*
* Posto nam API za opis vremenskih uslova i URL-a tog vremenskog uslova
* daje kao listu stringova, a uglavnom daje samo jedan podatak onda sam
* iskoristio ovu pomocnu data klasu koja u sebi sadrzi Current objekat
* koji za ta polja ima kao tip podatka List<String>
* dok u bazu ce ici CurrentWeather klasa koja za ta polja ima tip String
*
* U WeatherNetworkDataSourceImpl mi cemo da ovaj pristigli response pretvorimo
* u CurrentAPIWeatherResponse i onda cemo sa tim konvertovanim raditi kao LiveData....
* */
data class CurrentAPIWeatherResponseWithArrays(
    @SerializedName("current")
    val current: Current,
    val location: WeatherLocation,
    val request: Request
)