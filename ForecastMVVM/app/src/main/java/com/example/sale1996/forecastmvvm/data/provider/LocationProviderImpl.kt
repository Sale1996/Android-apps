package com.example.sale1996.forecastmvvm.data.provider

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.sale1996.forecastmvvm.data.db.entity.WeatherLocation
import com.example.sale1996.forecastmvvm.internal.LocationPermissionNotGrantedException
import com.example.sale1996.forecastmvvm.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import kotlin.math.abs

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

/*
* Uz pomoc third party biblioteke koristimo fusedLocationProviderClient kako bi dobili
* trenutnu lokaciju
* */
class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context) , LocationProvider {


    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        //Posto hasDeviceLocationChanged baca exception mi moramo da ga obgrlimo try catch metodom
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException){
            false
        }

        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    override suspend fun getPreferredLocationString(): String {
        if(isUsingDeviceLocation()){
            try{
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude}, ${deviceLocation.longitude}"
            } catch (e: LocationPermissionNotGrantedException){
                return "${getCustomLocationName()}"
            }
        }
        else{
            return "${getCustomLocationName()}"
        }
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if(!isUsingDeviceLocation()) return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        // kada poredimo double vrednosti to se ne bi trebalo porediti sa "=="
        val comparisonThreshold = 0.03
        return abs(deviceLocation.latitude - lastWeatherLocation.lat.toDouble()) > comparisonThreshold &&
                abs(deviceLocation.longitude - lastWeatherLocation.lon.toDouble()) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val customLocationName = getCustomLocationName()
        return customLocationName != lastWeatherLocation.name
    }

    private fun isUsingDeviceLocation(): Boolean{
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun getCustomLocationName(): String?{
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    private fun getLastDeviceLocation(): Deferred<Location?>{
        return if(hasLocationPermission()) fusedLocationProviderClient.lastLocation.asDeferred()
            else throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean{
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}