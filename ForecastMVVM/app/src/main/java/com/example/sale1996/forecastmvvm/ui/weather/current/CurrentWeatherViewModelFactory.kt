package com.example.sale1996.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sale1996.forecastmvvm.data.repository.ForecastRepository

class CurrentWeatherViewModelFactory(
    private val forecastRepository: ForecastRepository
): ViewModelProvider.NewInstanceFactory() {

    //vracamo nas viewmodel preko ove fabrike..
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(forecastRepository) as T
    }
}