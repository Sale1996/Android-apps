package com.example.sale1996.nebulainformator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToNebulaInfo(view : View){

        val button : ImageButton = view as ImageButton
        val tag: String = button.tag.toString()

        val detailIntent = Intent(this, NebulaDetailActivity::class.java)
        detailIntent.putExtra("nebula_tag", tag)
        startActivity(detailIntent)
    }
}
