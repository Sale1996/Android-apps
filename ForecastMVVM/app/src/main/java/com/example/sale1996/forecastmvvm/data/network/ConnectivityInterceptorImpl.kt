package com.example.sale1996.forecastmvvm.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.sale1996.forecastmvvm.internal.NoConectivityException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptorImpl(
    context: Context
) : ConnectivityInterceptor {

    /*
    * Posto ovaj context moze biti activity context, nama je potreban
    * application pa izvlacimo to iz njega...
    *
    * application context nam treba da proverimo da li ima neta...
    * */
    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isOnline())
            throw NoConectivityException() //mi smo rucno napravili ovaj exception da bude jasnije...
        return chain.proceed(chain.request()) //ako je ok sve nastavi dalje slobodno
    }

    private fun isOnline(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}