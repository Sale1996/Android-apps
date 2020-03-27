package com.example.sale1996.exploresolarsystem

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val SOLARSYSTEM_OBJECTS = listOf(
            "sun",
            "mercury",
            "venus",
            "earth",
            "moon",
            "mars",
            "jupiter",
            "saturn",
            "uranus",
            "neptune",
            "pluto"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        for (solarObject in SOLARSYSTEM_OBJECTS){
            createObjectView(solarObject)
        }

    }

    private fun createObjectView(objectName : String){
        // null iz razloga sto necemo odma da dodamo na layout nepripremljen flag
        val flag = layoutInflater.inflate(R.layout.solar_system_object, null)
        val imageButton = flag.findViewById<ImageButton>(R.id.object_image_button)
        imageButton.setOnClickListener {
            showSolarObjectInfo(objectName)
        }

        val objectNameTextView = flag.findViewById<TextView>(R.id.object_name)

        //get image
        val imageID = resources.getIdentifier(objectName, "drawable", packageName)
        imageButton.setImageResource(imageID)

        objectNameTextView.text = objectName


        planetary_system_main_layout.addView(flag)

    }

    private fun showSolarObjectInfo(objectName: String){

        //get object text
        val fileID = resources.getIdentifier(objectName + "_txt", "raw", packageName)
        val fileText = resources.openRawResource(fileID).bufferedReader().readLines().toString()

        val mp3FileID = resources.getIdentifier(objectName, "raw", packageName)
        val player = MediaPlayer.create(this, mp3FileID)
        player.start()

        val builder = AlertDialog.Builder(this)
        builder.setTitle(objectName)
        builder.setMessage(fileText.substring(1, fileText.length -1))
        builder.setPositiveButton("Got it") { _,_ ->
            player.stop()
        }
        val dialog = builder.create()
        dialog.show()
    }


    /*

        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tv.layoutParams = params

         */

    /*
    * val params = LinearLayout.LayoutParams(
    *           ViewGroup.LayoutParams.MATCH_PARENT,
    *           ViewGroup.LayoutParams.WRAP_CONTENT)
    *
    * params.weight = 1
    * params.gravity = Gravity.TOP //ili Gravity.Center
    * */

    /*
        val params = ConstraintLayout.LayoutParams(0,0)
        params.leftToLeft = parentLayout.id
        params.rightToright = parentLayout.id

     */

    /*
        val tv = TextView(this)
        tv.text = "Proba proba"
        tv.textSize = 20.0f
        planetary_system_main_layout.addView(tv)
     */
}
