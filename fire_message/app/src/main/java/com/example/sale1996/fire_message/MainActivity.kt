package com.example.sale1996.fire_message

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sale1996.fire_message.fragment.MyAccountFragment
import com.example.sale1996.fire_message.fragment.PeopleFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_people -> {
                    replaceFragment(PeopleFragment())
                    true
                }
                R.id.navigation_my_account -> {
                    replaceFragment(MyAccountFragment())
                    true
                }
                else -> false
            }
        }
    }

//    @SuppressLint("CommitTransaction")
//    private fun replaceFragment(fragment: Fragment){
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.nav_host_fragment, fragment)
//            commit()
//        }
//    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
