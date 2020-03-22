package com.example.sale1996.whatisaid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private val narrators = ArrayList<String>()
    private val sentences = ArrayList<String>()

    private lateinit var narratorListAdapter : ArrayAdapter<String>
    private val sentenceToNarrator = HashMap<String, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readDictionaryFile()
        setupList(true)


        list_of_narators.setOnItemClickListener { _, _, index, _ ->

            val clickedNarrator = narrators[index]
            if(clickedNarrator == sentenceToNarrator[said_sentence.text.toString()]){
                Toast.makeText(applicationContext, "BRAVO!", Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(applicationContext, "A jes, nije bas to", Toast.LENGTH_LONG).show()
            }
            setupList(false)
        }

    }


    private fun readDictionaryFile(){
        val reader = Scanner(resources.openRawResource(R.raw.narrator_sentences))
        while (reader.hasNextLine()){
            val line = reader.nextLine()
            val pieces = line.split("\t")
            if (pieces.size >= 2){
                sentences.add(pieces[0])
                narrators.add(pieces[1])
                sentenceToNarrator[pieces[0]] = pieces[1]
            }
        }
    }


    private fun setupList(isAppStart : Boolean) {

        // pick a random sentence
        val rand = Random()
        val index = rand.nextInt(sentences.size)
        val saidSentence = sentences[index]

        said_sentence.text = saidSentence

        // pick random narrators for the setence
        narrators.clear()
        narrators.add(sentenceToNarrator[saidSentence]!!)
        sentences.shuffle()
        for (otherSentence in sentences.subList(0,3)){
            if (otherSentence == saidSentence || narrators.size == 3){
                continue
            }
            narrators.add(sentenceToNarrator[otherSentence]!!)
        }
        narrators.shuffle()

        if(isAppStart){
            narratorListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, narrators)
            list_of_narators.adapter = narratorListAdapter
        }
        else{
            narratorListAdapter.notifyDataSetChanged()
        }
    }
}
