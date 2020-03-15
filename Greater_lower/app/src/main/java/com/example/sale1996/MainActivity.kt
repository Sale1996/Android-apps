package com.example.sale1996

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var score = 0
    var straitWins = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupEnvironment()
    }

    fun buttonClicked(view: View){
        checkAnswer(view == right_number)
    }

    private fun checkAnswer(isRight : Boolean){

        val leftButtonNumber = left_number.text.toString().toInt()
        val rightButtonNumber = right_number.text.toString().toInt()

        if( ((leftButtonNumber < rightButtonNumber) && isRight) || ((leftButtonNumber > rightButtonNumber) && !isRight)){
            score++
            straitWins++
            Toast.makeText(this, "TO!", Toast.LENGTH_SHORT).show()
        }
        else{
            if(score>0){
                score--
            }
            straitWins = 0
            Toast.makeText(this, "Noob!", Toast.LENGTH_SHORT).show()
        }

        number_of_points.text = "Score : $score"
        strait_win.text = "Strait wins: $straitWins"

        setupEnvironment()
    }

    private fun setupEnvironment(){
        val random = Random()
        val num1 = random.nextInt(100)
        var num2 = random.nextInt(100)

        while(num1==num2){
            num2= random.nextInt(100)
        }

        left_number.text = "$num1"
        right_number.text = "$num2"
    }
}
