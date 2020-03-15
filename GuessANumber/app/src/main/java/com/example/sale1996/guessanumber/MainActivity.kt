package com.example.sale1996.guessanumber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var choosenNumber = 0
    var numberOfGuesses = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNewGame()

        edit_text.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                numberEntered()
                return@OnKeyListener true
            }
            false
        })
    }

    fun numberEntered(){
        val guessedNumber = edit_text.text.toString().toInt()
        numberOfGuesses++
        when (guessedNumber){
            choosenNumber -> {app_hint.text = getString(R.string.finished_game) + "$numberOfGuesses"
                              edit_text.isEnabled = false}
            in 1..choosenNumber -> {app_hint.text = getString(R.string.hint_higher)}
            else                -> {app_hint.text = getString(R.string.hint_lower)}
        }
    }

    fun startNewGame(view : View){
        setNewGame()
    }


    private fun setNewGame(){
        val random = Random()
        choosenNumber = random.nextInt(1000)
        numberOfGuesses = 0
        edit_text.isEnabled = true
        app_hint.text = "Play!"
        edit_text.setText("0")
    }
}
