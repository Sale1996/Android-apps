package com.example.sale1996.forecastmvvm.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

/*
*
* LifecycleOwner ce biti nas aktivity
*
* */
class LifecycleBoundLocationManager(
    lifecycleOwner: LifecycleOwner,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationCallback: LocationCallback
): LifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

   /*
   * locationRequest nam govori koliko cesto zelim da se radi request poziv
   * */
    private val locationRequest = LocationRequest().apply {
       interval = 5000
       fastestInterval = 5000 //ovo je samo  5 sekundi, to se kasnije treba ubaciti tipa za sat-2
       priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
   }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startLocationUpdates(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback
        , null)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}