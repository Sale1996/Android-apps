package com.example.sale1996.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.example.sale1996.forecastmvvm.R
import com.example.sale1996.forecastmvvm.data.network.ApixuWeatherApiService
import com.example.sale1996.forecastmvvm.data.network.ConnectivityInterceptorImpl
import com.example.sale1996.forecastmvvm.data.network.WeatherNetworkDataSource
import com.example.sale1996.forecastmvvm.data.network.WeatherNetworkDataSourceImpl
import com.example.sale1996.forecastmvvm.internal.glide.GlideApp
import com.example.sale1996.forecastmvvm.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/*
* Treba se primetiti da je nasledio scopedFragment kako bi mogli da radimo
* sa coroutinama na lep nacin
* */
class CurrentWeatherFragment : ScopedFragment(), KodeinAware{

    override val kodein by closestKodein() //najblizi kodein je onaj sto smo definisali u forecastApp

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()



    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
             .get(CurrentWeatherViewModel::class.java)

        bindUI()
//        // TODO: Use the ViewModel
//        val apiService =
//            ApixuWeatherApiService(ConnectivityInterceptorImpl(this.requireContext())) //ovako smo pozvali invoke funkciju i dobili implementaciju
//        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService) //instanciramo klasu koja henlda zahteve i exceptione
//
//        //hajde da observamo objekat u weatherNetworkDataSource...
//        weatherNetworkDataSource.downloadedCurrentWeather.observe(viewLifecycleOwner, Observer {
//            text_input_current_weather.text = it.toString()
//
//        })
//
//        //od interfejsa
//        //Dispatchers Main smo naveli da pokrene korutinu koja je pokrenuta u UI niti i moze da menja
//        //UI
//        GlobalScope.launch(Dispatchers.Main) {
//            weatherNetworkDataSource.fetchCurrentWeather("London", " en")
//            //val currentWeatherResponse = apiService.getCurrentWeather("London").await()
//            //text_input_current_weather.text = currentWeatherResponse.toString()
//        }
    }

    /*
    * Posto dobijanje informacija iz viewmodela zahteva kotlinove coroutine
    * mi moramo preko njega i pozivati..ali treba obratiti paznju da
    * GlobalScope.launch nije pametno pozivati iz objekata koji imaju zivotni ciklus
    * jer kada se taj objekat srusi, srusice se i app
    * */

    //ali posto smo mi nasledili onaj fragment ne moramo da kucamo GlobalScope.launch
    //dovoljno je launch..
    private fun bindUI() = launch{
        val currentWeather = viewModel.weather.await()
        currentWeather.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer

            //sakrivamo loading view-e
            group_loading.visibility = View.GONE
            updateLocation("Srbobran")
            updateDateToToday()
            updateTemperatures(it.temperature, it.feelslike)
            updateCondition(it.weatherDescriptions)
            updatePrecipitation(it.precip)
            updateWind(it.windDir, it.windSpeed)
            updateVisibility(it.visibility)

            GlideApp.with(this@CurrentWeatherFragment)
                .load("${it.weatherIcons}")
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String{
        return if(viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Int, feelsLike: Int){
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_feels_like_temperature.text = "Feels like $feelsLike$unitAbbreviation"
    }

    private fun updateCondition(condition: String){
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Int){
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        textView_precipitation.text = "Preciptiation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWind(windDirection: String, windSpeed: Int) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_wind.text = "Wind: $windDirection, $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Int) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }



}
