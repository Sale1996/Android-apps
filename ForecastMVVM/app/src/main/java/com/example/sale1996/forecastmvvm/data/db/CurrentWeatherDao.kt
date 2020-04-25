package com.example.sale1996.forecastmvvm.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sale1996.forecastmvvm.data.db.entity.CURRENT_WEATHER_ID
import com.example.sale1996.forecastmvvm.data.db.entity.CurrentWeather
import com.example.sale1996.forecastmvvm.data.db.unitlocalized.UniLocalizedUnitWeatherEntry

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeather)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeather(): LiveData<UniLocalizedUnitWeatherEntry>

}