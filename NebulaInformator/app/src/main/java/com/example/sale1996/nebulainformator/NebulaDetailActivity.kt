package com.example.sale1996.nebulainformator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_nebula_detail.*

class NebulaDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nebula_detail)

        if (intent != null){

            val nebulaTag = intent.getStringExtra("nebula_tag") ?: "cats_eye_nebula"
            setNebulaDetails(nebulaTag)
        }
    }

    fun setNebulaDetails(nebulaTag : String){

        val nebulaImageID = resources.getIdentifier(nebulaTag, "drawable", packageName)
        val nebulaTextFileID = resources.getIdentifier("$nebulaTag"+"_txt", "raw", packageName)
        val fileText = resources.openRawResource(nebulaTextFileID).bufferedReader().readText()

        nebula_name.text = (nebulaTag.toString().replace("_", " "))
        nebula_detail_image.setImageResource(nebulaImageID)
        nebula_detail_desc.text = fileText

    }


}
