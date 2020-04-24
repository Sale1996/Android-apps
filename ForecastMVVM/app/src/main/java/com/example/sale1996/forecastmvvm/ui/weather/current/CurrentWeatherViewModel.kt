package com.example.sale1996.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.sale1996.forecastmvvm.data.repository.ForecastRepository
import com.example.sale1996.forecastmvvm.internal.UnitSystem
import com.example.sale1996.forecastmvvm.internal.lazyDeferred

/*
* Treba obratiti paznju da sada nama treba ViewModelProvider kojem prosledjujemo
* ViewModelFactory da bi nam napravio ovaj viewModel.. zasto?
* Zato sto viewModel sluzi za cuvanje podataka jednog view-a (aktivnosti,fragmenta)
* i kada se view unisti i ponovo pokrene mi ne zelimo da nam se izgubi sav kesirani sadrzaj
* stoga kada pozovemo opet providera on ce nam vratiti postojeci viewmodel sa kesiranim podacima
* umesto da kreiramo svaki put novog (jer onda se gubi smisao viewModela...)
* */
class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    private val unitSystem = UnitSystem.METRIC //get from settings later

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    /*
    * Dobavljanje trenutnog vremenskog stanja vrsice se preko lazy poziva
    * jer ne zelimo svaki put kad se kreira current weather viewmodel da pozivamo ovo
    * nego samo kada view zatrazi...
    * */
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }

}
