package com.example.sale1996.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.example.sale1996.forecastmvvm.R
import com.example.sale1996.forecastmvvm.data.network.ApixuWeatherApiService
import com.example.sale1996.forecastmvvm.data.network.ConnectivityInterceptorImpl
import com.example.sale1996.forecastmvvm.data.network.WeatherNetworkDataSource
import com.example.sale1996.forecastmvvm.data.network.WeatherNetworkDataSourceImpl
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrentWeatherFragment : Fragment() {

    companion object {
        fun newInstance() =
            CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel::class.java)
        // TODO: Use the ViewModel
        val apiService =
            ApixuWeatherApiService(ConnectivityInterceptorImpl(this.requireContext())) //ovako smo pozvali invoke funkciju i dobili implementaciju
        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService) //instanciramo klasu koja henlda zahteve i exceptione

        //hajde da observamo objekat u weatherNetworkDataSource...
        weatherNetworkDataSource.downloadedCurrentWeather.observe(viewLifecycleOwner, Observer {
            text_input_current_weather.text = it.toString()

        })

        //od interfejsa
        //Dispatchers Main smo naveli da pokrene korutinu koja je pokrenuta u UI niti i moze da menja
        //UI
        GlobalScope.launch(Dispatchers.Main) {
            weatherNetworkDataSource.fetchCurrentWeather("London", " en")
            //val currentWeatherResponse = apiService.getCurrentWeather("London").await()
            //text_input_current_weather.text = currentWeatherResponse.toString()
        }
    }

}
