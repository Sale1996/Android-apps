package com.example.sale1996.forecastmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.sale1996.forecastmvvm.R
import com.example.sale1996.forecastmvvm.data.ForecastDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        /*
        * Kako sad ovo funkcionise, pa jednostavno kada kliknemo neki menu item
        * u bottom navigation-u, android ce uzeti njegov ID i otici u navigation_graph
        * i pogledati koji fragment je vezan za taj ID i postaviti ga na mesto nav_host_fragment-a
        * */
        bottom_nav.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    /*
    * Ovo nam omogucava strelicu za back u tool bar-u
    * i ona ce najverovatnije da vraca na prvi fragment...
    * */
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}
