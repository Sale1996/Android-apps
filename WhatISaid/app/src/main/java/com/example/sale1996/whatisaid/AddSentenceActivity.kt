package com.example.sale1996.whatisaid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_sentence.*
import java.io.PrintStream

class AddSentenceActivity : AppCompatActivity() {

    private val NARRATOR_SENTENCE_PAIR_FILE_NAME = "new_narrator_sentences"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sentence)
    }

    fun addPairButtonClicked(view : View){
        val sentence = input_sentence.text.toString()
        val narrator = input_narrator.text.toString()

        val newFileLine = "$sentence\t$narrator"
        val outStream = PrintStream(openFileOutput(NARRATOR_SENTENCE_PAIR_FILE_NAME, MODE_PRIVATE))
        outStream.println(newFileLine)
        outStream.close()

        // returning info to main activity
        val returnIntent = Intent()
        returnIntent.putExtra("sentence", sentence)
        returnIntent.putExtra("narrator", narrator)
        setResult(RESULT_OK, returnIntent)
        finish()

    }
}

