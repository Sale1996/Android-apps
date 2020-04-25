package com.example.sale1996.forecastmvvm.data.db.unitlocalized

import androidx.room.ColumnInfo

/*
* Ovo su data klase, ne entity klase...
* znaci mi ove klase mozemo izvlacimo ove objekte
* */
data class UniLocalizedUnitWeatherEntry(
    @ColumnInfo(name = "cloudcover")
    override val cloudcover: Int,
    @ColumnInfo(name = "feelslike")
    override val feelslike: Int,
    @ColumnInfo(name = "humidity")
    override val humidity: Int,
    @ColumnInfo(name = "observationTime")
    override val observationTime: String,
    @ColumnInfo(name = "precip")
    override val precip: Int,
    @ColumnInfo(name = "pressure")
    override val pressure: Int,
    @ColumnInfo(name = "temperature")
    override val temperature: Int,
    @ColumnInfo(name = "uvIndex")
    override val uvIndex: Int,
    @ColumnInfo(name = "visibility")
    override val visibility: Int,
    @ColumnInfo(name = "weatherCode")
    override val weatherCode: Int,
    @ColumnInfo(name = "weatherDescriptions")
    override val weatherDescriptions: String,
    @ColumnInfo(name = "weatherIcons")
    override val weatherIcons: String,
    @ColumnInfo(name = "windDegree")
    override val windDegree: Int,
    @ColumnInfo(name = "windDir")
    override val windDir: String,
    @ColumnInfo(name = "windSpeed")
    override val windSpeed: Int
) : UnitSpecificCurrentWeatherEntry