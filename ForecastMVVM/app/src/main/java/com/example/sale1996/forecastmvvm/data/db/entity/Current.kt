package com.example.sale1996.forecastmvvm.data.db.entity

import com.google.gson.annotations.SerializedName
/*
* Predstavlja Current element iz API response zahteva
* dok njegov ekvivalent "CurrentWeather" ce se koristiti
* kao Entity klasa jer ona za polja weatherDescription i weatherIcons
* ima tip String umesto Array-a, sto baza podrzava...
*
* Znaci ova klasa se koristi da bi smo izvukli response sa servera
* */
data class Current(
    val cloudcover: Int,
    val feelslike: Int,
    val humidity: Int,
    @SerializedName("observation_time")
    val observationTime: String,
    val precip: Int,
    val pressure: Int,
    val temperature: Int,
    @SerializedName("uv_index")
    val uvIndex: Int,
    val visibility: Int,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String>,
    @SerializedName("weather_icons")
    val weatherIcons: List<String>,
    @SerializedName("wind_degree")
    val windDegree: Int,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Int
)