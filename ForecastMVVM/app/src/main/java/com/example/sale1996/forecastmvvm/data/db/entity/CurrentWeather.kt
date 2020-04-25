package com.example.sale1996.forecastmvvm.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeather(
    val cloudcover: Int,
    val feelslike: Int,
    val humidity: Int,
    @SerializedName("observation_time")
    val observationTime: String,
    val precip: Float,
    val pressure: Int,
    val temperature: Int,
    @SerializedName("uv_index")
    val uvIndex: Int,
    val visibility: Int,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("weather_descriptions")
    val weatherDescriptions: String,
    @SerializedName("weather_icons")
    val weatherIcons: String,
    @SerializedName("wind_degree")
    val windDegree: Int,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Int
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int =
        CURRENT_WEATHER_ID
}
/*
* Ovo sa generisanjem ID-a je namerno uradjeno iz razloga
* sto mi zelimo da u bazi imamo SAMO 1 current wather objekat
* jer ono samo tad je current weather haha
* */