package com.example.sale1996.forecastmvvm.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class PreferenceProvider(context: Context) {
    /*
    * Cak i nekad kad ovaj context se unisti, mi smo se vezal iza appContext te
    * onda nece biti problema
    * */
    private val appContext = context.applicationContext

    protected val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

}