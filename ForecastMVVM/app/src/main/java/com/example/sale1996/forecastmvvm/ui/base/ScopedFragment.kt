package com.example.sale1996.forecastmvvm.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main //ovo nam govori da se job vrsi u Main coroutinu
    /*
    * Sve sto se vrti u Main coroutinu, znaci da mzoe da radi sa UI-em, sto je nama i potrebno
    * kod fragmenata i activity-a
    *
    * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    /*
    * Znaci kad god je fragment unisten mi cemo da prekinemo posao i necemo imati
    * rusenje aplikacije :D
    * */
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}