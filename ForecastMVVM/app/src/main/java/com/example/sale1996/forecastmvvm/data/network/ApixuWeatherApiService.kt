package com.example.sale1996.forecastmvvm.data.network

import com.example.sale1996.forecastmvvm.data.network.response.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/*
* Ovaj interfejs cemo koristiti uz retrofit kako bi dobavljali sadrzaj sa interneta
*
* */

const val API_KEY = "a5946d085ac8c71eb64240f663d2382b"
//kako izgelda URI
//http://api.weatherstack.com/current?access_key=a5946d085ac8c71eb64240f663d2382b&query=New%20York

interface ApixuWeatherApiService {


    /*
    * Deferred je deo kotlin coorutina, i on nam govori ok kada posaljemo zahtev
    * on ce zaustaviti ovo njegovo da saceka zahtev, ali dalje sve ostalo moze da radi
    * asihronizovano.. kada dodje zahtev, on se otpuca i nastavi svoje...
    * */
    @GET("current.json")
    fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en"
    ): Deferred<CurrentWeatherResponse>


    /*
    * Nama sada treba neka klasa koja ce da komunicira sa ovim interfejsom
    * i ta klasa ce zapravo da fetchuje podatke sa API-a
    * */




    companion object{
        /*
        * U parametru funkcije invoke prosledjujemo interfejs za nas exception
        * fora je u tome sto hocemo da injectamo depedency tako da nemamo problema
        * sa menjanjem implementacije za excetpion
        * */
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): ApixuWeatherApiService {
            //pravimo presretaca koji ce da ubacuje vrednost API KEY-a
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key",
                        API_KEY
                    )
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            //sada hocemo da ovaj requestInterceptor da dodamo u OKHttp klijent da ga
            //napravimo pravim interceptorom
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            //sada sa ovom povratnomm vrednoscu mi cemo dobiti retrofit klasu
            //koja implementira nas interfejs i gde smo definisali jos neka polja
            /*
            * addCallAdapterFactory je dodat posto koristimo Coroutine od Kotlina, odnosno
            * Deferred za nasu povratnu vrednost
            *
            * addConverterFactory tu smo naveli da zelimo ulazni JSON da se pretvori u
            * Gson objekat, isti onaj objekat koji je napravljen na pocetku preko automatskog
            * kreiranja kotlin klasa iz JSON-a
            *
            * */
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.weatherstack.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApixuWeatherApiService::class.java)
        }
    }

}