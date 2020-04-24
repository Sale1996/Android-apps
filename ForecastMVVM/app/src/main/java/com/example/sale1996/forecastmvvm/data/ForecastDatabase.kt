package com.example.sale1996.forecastmvvm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sale1996.forecastmvvm.data.db.CurrentWeatherDao
import com.example.sale1996.forecastmvvm.data.db.entity.CurrentWeatherEntry

@Database(
    entities = [CurrentWeatherEntry::class],
    version = 1
)
abstract class ForecastDatabase: RoomDatabase() {
    /*
    * Ova klasa ce sama da nam napravi implementaciju DAO
    * klase
    * */

    abstract fun currentWeatherDao(): CurrentWeatherDao

    companion object{
        //volatile znaci da sve niti mogu pristupati ovoj instanci
         @Volatile private var instance: ForecastDatabase? = null
        //LOCK ce se koristiti kako ne bi dozvolili u isto vreme niti da koriste ovu instancu
        private val LOCK = Any()

        /*
        * Kada kreiramo instancu baze ili joj pristupamo proveri da li je null
        * ako nije pozovi sihronizovani proces sa kljucem da kreiras...
        * jos jedna provera je za svaki slucaj ukoliko neko nije bas napravio
        * u momentu pozivanja novog procesa...
        * */
        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it //sta god build vratio to instanciraj u instance
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                ForecastDatabase::class.java, "forecast.db")
                .build()
    }

}