package com.example.sale1996.forecastmvvm.data.db.unitlocalized

import androidx.room.ColumnInfo

interface UnitSpecificCurrentWeatherEntry {
    val cloudcover: Int
    val feelslike: Int
    val humidity: Int
    val observationTime: String
    val precip: Int
    val pressure: Int
    val temperature: Int
    val uvIndex: Int
    val visibility: Int
    val weatherCode: Int
    val weatherDescriptions: String
    val weatherIcons: String
    val windDegree: Int
    val windDir: String
    val windSpeed: Int
}