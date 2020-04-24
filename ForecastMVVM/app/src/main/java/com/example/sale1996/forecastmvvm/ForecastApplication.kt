package com.example.sale1996.forecastmvvm

import android.app.Application
import com.example.sale1996.forecastmvvm.data.ForecastDatabase
import com.example.sale1996.forecastmvvm.data.network.*
import com.example.sale1996.forecastmvvm.data.repository.ForecastRepository
import com.example.sale1996.forecastmvvm.data.repository.ForecastRepositoryImpl
import com.example.sale1996.forecastmvvm.ui.weather.current.CurrentWeatherViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

/*
* Instancirali smo sve, ali opet se nece nista ucitavati ako ne prijavimo
* ovu klasu u android.manifest fajlu...
* */
class ForecastApplication: Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        //prvo importamo andoidXModul jer ce nam trebati
        import(androidXModule(this@ForecastApplication))

        //ovaj instance je iskoriscen iz AndroidXModula koji ce nam vratiti context kada nam bude trebalo
        //singleton jer nam je potrebna samo jedna i samo jedna instanca. ..
        bind() from singleton { ForecastDatabase(instance()) }
        //kada pozivamo instance<ForecastDatabase>() on ce pozvati iznad liniju i ubaciti na to mesto
        //instanciranu bazu
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton {ApixuWeatherApiService(instance())}
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance())}
        bind<ForecastRepository>() with singleton {ForecastRepositoryImpl(instance(), instance())}
        //posto factory nema svoj repository pisemo ovako.. a from provider znaci da prilikom
        //svakog novog bindovanja mi dobijamo novu instancu te fabrike
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }
    }

    override fun onCreate(){
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}