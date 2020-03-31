package com.example.sale1996.randompetpicturegenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getRandomDogPicture(view : View){
        Ion.with(this)
            .load("https://dog.ceo/api/breeds/image/random")
            .asString()
            .setCallback { _, result ->
                // result comes here
                putPetImage(result, "dog")
            }
    }

    private fun putPetImage(result: String, petType: String){
        var url : String
        if(petType == "dog"){
            url = JSONObject(result).getString("message")
        }
        else{
            url = JSONObject("{\"cat\":$result}")
                .getJSONArray("cat")
                .getJSONObject(0)
                .getString("url")
        }

        Picasso.get()
            .load(url)
            .resize(150, 150)
            .centerCrop()
            .into(pet_image)
    }

    fun getRandomCatPicture(view : View){
        Ion.with(this)
            .load("https://api.thecatapi.com/v1/images/search")
            .asString()
            .setCallback { _, result ->
                // result comes here
                putPetImage(result, "cat")
            }
    }
}
